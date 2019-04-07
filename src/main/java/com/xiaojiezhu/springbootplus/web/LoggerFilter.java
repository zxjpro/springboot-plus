package com.xiaojiezhu.springbootplus.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author xiaojie.zhu
 * time 2019-04-07 09:08
 */
public class LoggerFilter implements Filter {

    public final Logger LOG = LoggerFactory.getLogger(getClass());

    /**
     * 大于则打印日志
     */
    public static final int SLOW_TIME = 1000;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        long startTime = System.currentTimeMillis();

        try {
            filterChain.doFilter(servletRequest , servletResponse);
        }finally {

            long endTime = System.currentTimeMillis();

            long useTime = endTime - startTime;

            if(useTime > SLOW_TIME){

                HttpServletRequest request = (HttpServletRequest) servletRequest;
                LOG.warn("请求慢日志： " + request.getRequestURI() + " 用时 " + useTime);
            }

        }




    }

    @Override
    public void destroy() {

    }
}
