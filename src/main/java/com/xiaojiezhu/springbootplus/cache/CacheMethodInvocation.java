package com.xiaojiezhu.springbootplus.cache;

import com.xiaojiezhu.cache.Cache;
import com.xiaojiezhu.springbootplus.AbstractMethodInvocation;
import com.xiaojiezhu.springbootplus.MethodContext;
import com.xiaojiezhu.springbootplus.MethodInfo;
import com.xiaojiezhu.springbootplus.cache.annotation.PCache;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

import static com.xiaojiezhu.springbootplus.lock.configuration.LockConfiguration.log;

/**
 * time 2019/3/15 12:04
 *
 * @author xiaojie.zhu <br>
 */
public class CacheMethodInvocation extends AbstractMethodInvocation {

    private Cache cache;


    public CacheMethodInvocation(Cache cache, MethodContext methodContext) {
        super(methodContext);
        this.cache = cache;
    }

    @Override
    protected Object invoke0(ProceedingJoinPoint point, MethodInfo methodInfo) throws Throwable {
        String cacheKey = methodInfo.buildString(point.getArgs());

        String logName = methodInfo.getSimpleClassName() + "." + methodInfo.getMethodName() + "() , " + cacheKey;

        log.debug("进入缓存插件 : " + logName);

        Object object = this.cache.getObject(cacheKey);

        if(object == null){
            log.debug("缓存中不存在值，初始化值到缓存中 : " + logName);
            object = point.proceed();
            cache.set(cacheKey , object);
            if(methodInfo.getExpireMs() != -1){
                cache.expire(cacheKey , methodInfo.getExpireMs());
            }

        }else{
            log.debug("缓存中已存在值 : " + logName);
        }

        return object;
    }

    @Override
    protected AnnotationAttribute getAnnotationAttribute(Method method) {
        PCache pCache = method.getAnnotation(PCache.class);
        if(pCache == null){
            throw new RuntimeException("PCache annotation is null");
        }
        if(pCache.expireTime() == 0 || pCache.expireTime() < -1){
            throw new IllegalArgumentException("@PCache() 设置的值不能小于-1 ， 且不能等于0");
        }

        AnnotationAttribute annotationAttribute = new AnnotationAttribute();
        annotationAttribute.setPattern(pCache.value());
        annotationAttribute.setTime(pCache.expireTime());
        annotationAttribute.setTimeUnit(pCache.timeUnit());
        return annotationAttribute;
    }


}
