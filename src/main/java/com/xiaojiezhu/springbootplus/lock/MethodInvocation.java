package com.xiaojiezhu.springbootplus.lock;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * time 2019/3/14 15:22
 *
 * @author xiaojie.zhu <br>
 */
public interface MethodInvocation {

    /**
     *
     * @param point
     */
    Object invoke(ProceedingJoinPoint point) throws Throwable;
}
