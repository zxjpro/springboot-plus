package com.xiaojiezhu.springbootplus.web.ex;

import com.xiaojiezhu.springbootplus.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zxj<br>
 * 时间 2017/12/12 15:20
 * 说明 ...
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler
    public Result<?> handler(Throwable throwable){
        if(ExceptionMapping.isPrintError(throwable)){
            throwable.printStackTrace();
        }

        int errorCode = 500;
        if(throwable instanceof NoticeException){
            NoticeException noticeException = (NoticeException) throwable;
            errorCode = noticeException.getErrorCode();
        }
        Result<?> result = new Result<>(errorCode,throwable.getMessage());
        return result;
    }
}
