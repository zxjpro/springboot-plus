package com.xiaojiezhu.springbootplus.lock.configuration;

import com.xiaojiezhu.springbootplus.lock.DefaultMethodInvocation;
import com.xiaojiezhu.springbootplus.lock.LockFactory;
import com.xiaojiezhu.springbootplus.lock.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Aspect()
public class LockConfiguration implements ApplicationContextAware {
    public static final Logger log = LoggerFactory.getLogger(LockConfiguration.class);

    private ApplicationContext context;
    private MethodInvocation methodInvocation;

    @Pointcut("@annotation(com.xiaojiezhu.springbootplus.lock.annotation.PLock)")
    public void lockPoint(){}


    @ConditionalOnClass(name = "org.redisson.api.RedissonClient")
    protected static class LockFactoryConfiguration{

        @ConditionalOnBean(RedissonClient.class)
        @Bean
        public LockFactory lockRedisFactory(RedissonClient redissonClient){
            log.info("");
            log.info("初始化分布式锁");
            log.info("");
            return new LockFactory(redissonClient);
        }
    }




    @ConditionalOnMissingBean(LockFactory.class)
    @Bean
    public LockFactory lockJavaFactory(){
        log.info("");
        log.info("初始化java锁");
        log.info("");
        return new LockFactory(null);
    }

    @Bean
    public MethodInvocation lockMethodInvocation(LockFactory lockFactory){
        log.info("锁机制初始化成功");
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
