package com.xiaojiezhu.springbootplus.lock;

import com.xiaojiezhu.springbootplus.DateUtil;
import com.xiaojiezhu.springbootplus.MethodInfo;
import com.xiaojiezhu.springbootplus.lock.annotation.PLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * time 2019/3/14 15:23
 *
 * @author xiaojie.zhu <br>
 */
public class DefaultMethodInvocation implements MethodInvocation {

    public final Logger log = LoggerFactory.getLogger("springboot.plus.lock");

    private static final Object[] EMPTY = new Object[]{};

    private static final Pattern PATTERN = Pattern.compile("\\{([\\d]{1})\\}");

    private final ConcurrentHashMap<String , MethodInfo> methodInfos = new ConcurrentHashMap<>();

    private final LockFactory lockFactory;

    public DefaultMethodInvocation(LockFactory lockFactory) {
        this.lockFactory = lockFactory;
    }

    @Override
    public Object invoke(ProceedingJoinPoint point) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        String signatureString = methodSignature.toLongString();
        MethodInfo methodInfo = methodInfos.get(signatureString);
        if(methodInfo == null){
            methodInfo = createMethodInfo(point.getTarget() , point.getArgs(), methodSignature);
            methodInfos.put(signatureString , methodInfo);
        }

        String lockString = methodInfo.buildString(point.getArgs());

        String logName = methodInfo.getSimpleClassName() + "." + methodInfo.getMethodName() + "()";

        log.debug("准备切入锁 : " + logName);
        DLock dLock = lockFactory.newLock(lockString, methodInfo.getExpireMs());
        dLock.lockInterruptibly(methodInfo.getExpireMs() , TimeUnit.MILLISECONDS);

        log.debug("锁成功 : " + logName);
        Object proceed;
        try {
            proceed = point.proceed();
        } finally {
            dLock.unlock();
            log.debug("锁释放 : " + logName);
        }
        return proceed;
    }

    private MethodInfo createMethodInfo(Object target , Object[] args, MethodSignature methodSignature) {
        Method method = methodSignature.getMethod();
        PLock pLock = method.getAnnotation(PLock.class);
        if(pLock == null){
            throw new RuntimeException("DLock annotation is null");
        }
        if(args == null){
            args = EMPTY;
        }

        String p = pLock.value();

        // 存放对应参数的索引
        List<Integer> argIndexs = new ArrayList<>();
        List<String> splits = new ArrayList<>();

        Matcher matcher = PATTERN.matcher(p);

        int lastFindIndex = 0;

        while (matcher.find()){
            String tag = matcher.group();
            int argIndex = Integer.parseInt(matcher.group(1));
            argIndexs.add(argIndex);


            int index = p.indexOf(tag, lastFindIndex);
            splits.add(p.substring(lastFindIndex , index));
            lastFindIndex = index += 3;
        }
        String suffix = p.substring(lastFindIndex);

        int needArgLength = Collections.max(argIndexs) + 1;
        if(args.length < needArgLength){
            throw new IllegalArgumentException("format string fail , " + pLock.value() + " required as least " + needArgLength + " args");
        }

        Assert.notNull(pLock.timeUnit() , methodSignature.toLongString() + " " + PLock.class.getSimpleName() + "() annotation timeUnit atrribute not be null");
        if(pLock.expireTime() <= 0){
            throw new IllegalArgumentException(methodSignature.toLongString() + " " + PLock.class.getSimpleName() + "() annotation expireTime must > 0");
        }
        long expireMs = DateUtil.ms(pLock.expireTime(), pLock.timeUnit());


        MethodInfo methodInfo = new MethodInfo();
        methodInfo.setClassName(target.getClass().getName());
        methodInfo.setSimpleClassName(target.getClass().getSimpleName());
        methodInfo.setMethodName(methodSignature.getMethod().getName());
        methodInfo.setExpireMs(expireMs);
        methodInfo.setSignature(methodSignature.toLongString());
        methodInfo.setArgIndexs(argIndexs);
        methodInfo.setSplitString(splits);
        methodInfo.setSuffix(suffix);
        return methodInfo;
    }




}
