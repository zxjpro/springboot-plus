package com.xiaojiezhu.springbootplus;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * time 2019/3/15 12:52
 *
 * @author xiaojie.zhu <br>
 */
public class SimpleMethodContext implements MethodContext {

    private final ConcurrentHashMap<Method , MethodInfo> datas = new ConcurrentHashMap<>();

    @Override
    public void put(Method method, MethodInfo methodInfo) {
        datas.put(method , methodInfo);
    }

    @Override
    public MethodInfo get(Method method) {
        return datas.get(method);
    }


    private SimpleMethodContext(){}


    public static SimpleMethodContext getInstance(){
        return Instance.INSTANCE;
    }

    private static class Instance{
        private static final SimpleMethodContext INSTANCE = new SimpleMethodContext();
    }
}
