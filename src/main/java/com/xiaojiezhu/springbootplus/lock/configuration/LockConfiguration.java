package com.xiaojiezhu.springbootplus.lock.configuration;

import com.xiaojiezhu.springbootplus.lock.DefaultMethodInvocation;
import com.xiaojiezhu.springbootplus.lock.LockFactory;
import com.xiaojiezhu.springbootplus.lock.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;

/**
 * time 2019/3/13 11:41
 *
 * @author xiaojie.zhu <br>
 */
@AutoConfigureAfter(LockConfiguration.LockFactoryConfiguration.class)
@Aspect()
@Import(LockConfiguration.LockFactoryConfiguration.class)
public class LockConfiguration implements ApplicationContextAware {

    private ApplicationContext context;
    private MethodInvocation methodInvocation;

    @Pointcut("@annotation(com.xiaojiezhu.springbootplus.lock.annotation.PLock)")
    public void lockPoint(){}


    @ConditionalOnClass(name = "org.redisson.api.RedissonClient")
    protected static class LockFactoryConfiguration{

        @ConditionalOnBean(RedissonClient.class)
        @Bean
        public LockFactory lockRedisFactory(RedissonClient redissonClient){
            return new LockFactory(redissonClient);
        }
    }




    @ConditionalOnMissingBean(LockFactory.class)
    @Bean
    public LockFactory lockJavaFactory(){
        return new LockFactory(null);
    }

    @Bean
    public MethodInvocation lockMethodInvocation(LockFactory lockFactory){
        return new DefaultMethodInvocation(lockFactory);
    }

    @Around("lockPoint()")
    public Object dox(ProceedingJoinPoint point) throws Throwable {
        return getMethodInvocation().invoke(point);
    }

    private MethodInvocation getMethodInvocation(){
        if(methodInvocation == null){
            methodInvocation = context.getBean(MethodInvocation.class);
        }
        return methodInvocation;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
