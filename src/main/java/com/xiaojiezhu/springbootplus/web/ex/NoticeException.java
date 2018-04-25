package com.xiaojiezhu.springbootplus.web.ex;

/**
 * @author zxj<br>
 * 时间 2017/12/12 15:23
 * 说明 只抛异常，不打印异常信息
 */
public class NoticeException extends RuntimeException {
    public NoticeException() {
    }

    public NoticeException(String message) {
        super(message);
    }

    public NoticeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoticeException(Throwable cause) {
        super(cause);
    }

    public NoticeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
