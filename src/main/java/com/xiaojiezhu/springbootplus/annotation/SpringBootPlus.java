package com.xiaojiezhu.springbootplus.annotation;

import com.xiaojiezhu.springbootplus.web.FastJsonSupport;
import com.xiaojiezhu.springbootplus.web.ResultValueConfiguration;
import com.xiaojiezhu.springbootplus.web.ex.ResultExceptionHandler;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author zxj<br>
 * 时间 2018/4/25 16:42
 * 说明 启用所有的springbootplus功能
 */
@Documented
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({FastJsonSupport.class,
        ResultValueConfiguration.class,
        ResultExceptionHandler.class})
public @interface SpringBootPlus {
}
