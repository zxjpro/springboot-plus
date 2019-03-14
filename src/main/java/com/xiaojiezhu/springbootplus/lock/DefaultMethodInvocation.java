package com.xiaojiezhu.springbootplus.lock;

import com.xiaojiezhu.springbootplus.lock.annotation.PLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * time 2019/3/14 15:23
 *
 * @author xiaojie.zhu <br>
 */
public class DefaultMethodInvocation implements MethodInvocation {

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
            methodInfo = createMethodInfo(point.getArgs(), methodSignature);
            methodInfos.put(signatureString , methodInfo);
        }

        String lockString = methodInfo.buildString(point.getArgs());


        Object proceed = point.proceed();
        return proceed;
    }

    private MethodInfo createMethodInfo(Object[] args, MethodSignature methodSignature) {
        Method method = methodSignature.getMethod();
        PLock PLock = method.getAnnotation(PLock.class);
        if(PLock == null){
            throw new RuntimeException("PLock annotation is null");
        }
        if(args == null){
            args = EMPTY;
        }

        String p = PLock.value();

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
            throw new IllegalArgumentException("format string fail , " + PLock.value() + " required as least " + needArgLength + " args");
        }

        MethodInfo methodInfo = new MethodInfo();
        methodInfo.setSignature(methodSignature.toLongString());
        methodInfo.setArgIndexs(argIndexs);
        methodInfo.setSplitString(splits);
        methodInfo.setSuffix(suffix);
        return methodInfo;
    }




}
