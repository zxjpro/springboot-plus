package com.xiaojiezhu.springbootplus;

/**
 * @author zxj<br>
 * 时间 2018/4/25 16:23
 * 说明 ...
 */
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    public static final Result<?> SUCCESS = new Result<Object>(0,"SUCCESS");


    public Result() {
    }

    public Result(int code) {
        this.code = code;
    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
