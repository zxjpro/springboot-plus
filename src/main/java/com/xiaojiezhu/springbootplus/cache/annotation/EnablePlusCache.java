package com.xiaojiezhu.springbootplus.cache.annotation;

import com.xiaojiezhu.springbootplus.cache.configuration.CacheConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * time 2019/3/15 11:54
 *
 * @author xiaojie.zhu <br>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({CacheConfiguration.class})
public @interface EnablePlusCache {
}
