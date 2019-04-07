package com.xiaojiezhu.springbootplus.web;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiaojie.zhu
 * time 2019-04-07 09:50
 */
@Configuration
public class LoggerFilterConfiguration {


    @Bean
    public FilterRegistrationBean loggerFilterBean(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.addUrlPatterns("/*");
        bean.setFilter(new LoggerFilter());
        return bean;
    }
}
