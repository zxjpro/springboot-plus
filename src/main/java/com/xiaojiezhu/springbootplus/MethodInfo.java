package com.xiaojiezhu.springbootplus;

import com.xiaojiezhu.springbootplus.cache.DataType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * time 2019/3/14 15:25
 *
 * @author xiaojie.zhu <br>
 */
public class MethodInfo {

    /**
     * 方法签名
     */
    private String signature;

    /**
     * 方法名
     */
    private String methodName;

    private String simpleClassName;

    private String className;

    /**
     * 切割出来的部分
     */
    private List<String> splitString;

    /**
     * 字符串后缀
     */
    private String suffix;

    /**
     * 需要注入到锁定字符串的参数对应的索引
     */
    private List<Integer> argIndexs;

    /**
     * 过期的毫秒数
     */
    private long expireMs;

    /**
     * 方法的返回值类型
     */
    private Class<?> returnType;

    /**
     * 该方法返回值的数据类型
     */
    private DataType dataType;




    /**
     * 根据参数构建字符串
     * @param args
     * @return
     */
    public String buildString(Object[] args){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < splitString.size(); i++) {
            sb.append(splitString.get(i));
            String value = String.valueOf(args[argIndexs.get(i)]);
            sb.append(value);
        }

        sb.append(suffix);

        return sb.toString();
    }


    public long getExpireMs() {
        return expireMs;
    }

    public void setExpireMs(long expireMs) {
        this.expireMs = expireMs;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getSimpleClassName() {
        return simpleClassName;
    }

    public void setSimpleClassName(String simpleClassName) {
        this.simpleClassName = simpleClassName;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public List<String> getSplitString() {
        return splitString;
    }

    public void setSplitString(List<String> splitString) {
        this.splitString = splitString;
    }

    public List<Integer> getArgIndexs() {
        return argIndexs;
    }

    public void setArgIndexs(List<Integer> argIndexs) {
        this.argIndexs = argIndexs;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
