package com.xiaojiezhu.springbootplus.cache.configuration;

import com.xiaojiezhu.cache.Cache;
import com.xiaojiezhu.springbootplus.MethodInvocation;
import com.xiaojiezhu.springbootplus.SimpleMethodContext;
import com.xiaojiezhu.springbootplus.cache.CacheMethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * time 2019/3/15 11:58
 *
 * @author xiaojie.zhu <br>
 */
@EnableAspectJAutoProxy(proxyTargetClass = true,exposeProxy = true)
@Aspect
public class CacheConfiguration implements ApplicationContextAware {

    public static final Logger log = LoggerFactory.getLogger("springboot.plus.cache");

    public static final String CACHE_METHOD_INVOCATION = "cacheMethodInvocation";
    private ApplicationContext context;
    private MethodInvocation methodInvocation;

    @Pointcut("@annotation(com.xiaojiezhu.springbootplus.cache.annotation.PCache)")
    public void cachePoint(){}



    @Bean(name = CACHE_METHOD_INVOCATION)
    public MethodInvocation cacheMethodInvocation(Cache cache){
        log.info("缓存插件初始化");
        return new CacheMethodInvocation(cache , new SimpleMethodContext());
    }



    @Around("cachePoint()")
    public Object cache(ProceedingJoinPoint point) throws Throwable {
        MethodInvocation methodInvocation = getMethodInvocation();
        Object object = methodInvocation.invoke(point);
        return object;
    }

    public MethodInvocation getMethodInvocation() {
        if(methodInvocation == null){
            methodInvocation = context.getBean(CACHE_METHOD_INVOCATION , MethodInvocation.class);
        }
        return methodInvocation;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
