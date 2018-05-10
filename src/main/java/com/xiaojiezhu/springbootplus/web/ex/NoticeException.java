package com.xiaojiezhu.springbootplus.web.ex;

/**
 * @author zxj<br>
 * 时间 2017/12/12 15:23
 * 说明 只抛异常，不打印异常信息
 */
public class NoticeException extends RuntimeException {

    private int errorCode;

    public NoticeException() {
        this.errorCode = 500;
    }


    public NoticeException(int errorCode) {
        this.errorCode = errorCode;
    }

    public NoticeException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public NoticeException(String message, Throwable cause, int errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public NoticeException(Throwable cause, int errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public NoticeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int errorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
