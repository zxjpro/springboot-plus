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
     * 锁定的字符串
     * @return
     */
    String value();

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
