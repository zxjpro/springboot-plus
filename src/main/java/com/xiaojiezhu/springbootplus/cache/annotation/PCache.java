package com.xiaojiezhu.springbootplus.cache.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * time 2019/3/15 12:00
 *
 * @author xiaojie.zhu <br>
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PCache {

    /**
     * 缓存的key，支持占位符
     * @return
     */
    String value();

    /**
     * 缓存过期时间
     * @return
     */
    long expireTime() default -1;

    /**
     * 时间单位
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}
