package com.xiaojiezhu.springbootplus.web.ex;


/**
 * @author zxj<br>
 * 时间 2017/12/12 15:22
 * 说明 ...
 */
public class ExceptionMapping {

    /**
     * 是否打印异常
     * @param throwable
     * @return
     */
    public static boolean isPrintError(Throwable throwable){
        if(throwable instanceof NoticeException){
            return false;
        }
        return true;
    }

    /**
     * 根据异常获取状态码
     * @param throwable
     * @return
     */
    public static int getErrorCode(Throwable throwable){
        return 500;
    }
}
