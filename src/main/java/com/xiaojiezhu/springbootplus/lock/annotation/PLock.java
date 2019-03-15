package com.xiaojiezhu.springbootplus.lock.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * time 2019/3/13 11:44
 *
 * @author xiaojie.zhu <br>
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PLock {

    /**
     * 如果不加参数，则默认锁全局方法
     * @return
     */
    String value() default "";

    /**
     * 锁的过期时间
     * @return
     */
    long expireTime();

    /**
     * 时间单位
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}
