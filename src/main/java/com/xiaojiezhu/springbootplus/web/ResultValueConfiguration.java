package com.xiaojiezhu.springbootplus.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @author zxj<br>
 * 时间 2018/4/25 19:02
 * 说明 ...
 */
@Configuration
public class ResultValueConfiguration extends WebMvcConfigurerAdapter {
    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        returnValueHandlers.add(new ResultMethodHandler());
    }
}
