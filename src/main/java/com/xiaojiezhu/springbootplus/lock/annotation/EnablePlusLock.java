package com.xiaojiezhu.springbootplus.lock.annotation;

import com.xiaojiezhu.springbootplus.lock.configuration.LockConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * time 2019/3/14 15:12
 *
 * @author xiaojie.zhu <br>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({LockConfiguration.class})
public @interface EnablePlusLock {
}
