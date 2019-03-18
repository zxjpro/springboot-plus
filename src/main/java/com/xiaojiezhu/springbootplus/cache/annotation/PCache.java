package com.xiaojiezhu.springbootplus.cache.annotation;

import com.xiaojiezhu.springbootplus.cache.DataType;

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

    /**
     * 缓存的数据类型，一般情况下设置 AUTO 即可，框架会自动根据 method　的返回值类型来决定，如果　method 返回的是 Object 类型，那么就必须手工指定一个类型
     * 提示：如果是 java bean 的类型，因为需要进行 json 序列化，所以必须要有空的构造方法
     * 提示：不要缓存有状态的对象
     * @return
     */
    DataType dataType() default DataType.AUTO;
}
