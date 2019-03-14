package com.xiaojiezhu.springbootplus.lock.annotation;

import java.lang.annotation.*;

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
}
