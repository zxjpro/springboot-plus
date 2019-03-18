package com.xiaojiezhu.springbootplus.cache;

import com.xiaojiezhu.cache.Cache;
import com.xiaojiezhu.springbootplus.AbstractMethodInvocation;
import com.xiaojiezhu.springbootplus.MethodContext;
import com.xiaojiezhu.springbootplus.MethodInfo;
import com.xiaojiezhu.springbootplus.cache.annotation.PCache;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;


/**
 * time 2019/3/15 12:04
 *
 * @author xiaojie.zhu <br>
 */
public class CacheMethodInvocation extends AbstractMethodInvocation {
    public static final Logger log = LoggerFactory.getLogger("springboot.plus.cache");
    private CacheProxy cacheProxy;


    public CacheMethodInvocation(Cache cache, MethodContext methodContext) {
        super(methodContext);
        this.cacheProxy = new DefaultCacheProxy(cache);
    }

    @Override
    protected Object invoke0(ProceedingJoinPoint point, MethodInfo methodInfo) throws Throwable {
        String cacheKey = methodInfo.buildString(point.getArgs());

        String logName = methodInfo.getSimpleClassName() + "." + methodInfo.getMethodName() + "() , " + cacheKey;

        log.debug("进入缓存插件 : " + logName);

        Object object = this.cacheProxy.get(cacheKey , methodInfo.getDataType());

        if(object == null){
            log.debug("缓存中不存在值，初始化值到缓存中 : " + logName);
            object = point.proceed();
            this.cacheProxy.set(cacheKey , object , methodInfo.getDataType());
            if(methodInfo.getExpireMs() != -1){
                this.cacheProxy.expire(cacheKey , methodInfo.getExpireMs());
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

        if(DataType.AUTO == pCache.dataType()){
            if(Object.class == method.getReturnType()){
                throw new IllegalArgumentException("方法反回类型指定为 Object 时，必须指定 @PCache(dataType=xx)");
            }
            annotationAttribute.setDataType(dataType(method));
        }else{
            annotationAttribute.setDataType(pCache.dataType());
        }
        return annotationAttribute;
    }


}
