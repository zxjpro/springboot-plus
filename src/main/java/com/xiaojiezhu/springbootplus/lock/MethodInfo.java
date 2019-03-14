package com.xiaojiezhu.springbootplus.lock;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * time 2019/3/14 15:25
 *
 * @author xiaojie.zhu <br>
 */
class MethodInfo {

    /**
     * 方法签名
     */
    private String signature;

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

        sb.insert(0 , signature);

        return sb.toString();
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
