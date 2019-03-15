package com.xiaojiezhu.springbootplus;

import java.lang.reflect.Method;

/**
 * time 2019/3/15 12:43
 *
 * @author xiaojie.zhu <br>
 */
public interface MethodContext {

    void put(Method method , MethodInfo methodInfo);


    MethodInfo get(Method method);




}
