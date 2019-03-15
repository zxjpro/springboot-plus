package com.xiaojiezhu.springbootplus.lock;

import com.xiaojiezhu.springbootplus.AbstractMethodInvocation;
import com.xiaojiezhu.springbootplus.MethodContext;
import com.xiaojiezhu.springbootplus.MethodInfo;
import com.xiaojiezhu.springbootplus.lock.annotation.PLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * time 2019/3/14 15:23
 *
 * @author xiaojie.zhu <br>
 */
public class LockMethodInvocation extends AbstractMethodInvocation {

    public final Logger log = LoggerFactory.getLogger("springboot.plus.lock");

    private final LockFactory lockFactory;

    public LockMethodInvocation(LockFactory lockFactory , MethodContext methodContext) {
        super(methodContext);
        this.lockFactory = lockFactory;
    }

    @Override
    protected Object invoke0(ProceedingJoinPoint point, MethodInfo methodInfo) throws Throwable {
        String lockString = methodInfo.buildString(point.getArgs());

        String logName = methodInfo.getSimpleClassName() + "." + methodInfo.getMethodName() + "() , " + lockString;

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


    @Override
    protected AnnotationAttribute getAnnotationAttribute(Method method) {
        PLock pLock = method.getAnnotation(PLock.class);
        if(pLock == null){
            throw new RuntimeException("PLock annotation is null");
        }

        AnnotationAttribute annotationAttribute = new AnnotationAttribute();
        annotationAttribute.setPattern(pLock.value());
        annotationAttribute.setTime(pLock.expireTime());
        annotationAttribute.setTimeUnit(pLock.timeUnit());
        return annotationAttribute;
    }


}
