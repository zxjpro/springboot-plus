package com.xiaojiezhu.springbootplus.web.ex;

import com.xiaojiezhu.springbootplus.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;

/**
 * @author zxj<br>
 * 时间 2017/12/12 15:20
 * 说明 ...
 */
@ControllerAdvice
public class ResultExceptionHandler {
    public final Logger LOG = LoggerFactory.getLogger(getClass());

    @ResponseBody
    @ExceptionHandler
    public Result<?> handler(Throwable throwable){
        if(ExceptionMapping.isPrintError(throwable)){
            LOG.error(throwable.getMessage() , throwable);
        }

        int errorCode = 500;
        if(throwable instanceof NoticeException){
            NoticeException noticeException = (NoticeException) throwable;
            errorCode = noticeException.getErrorCode();
        }

        //不输出SQL异常
        String errorMsg = "系统异常";
        if(!(throwable instanceof SQLException)){
            errorMsg = throwable.getMessage();
        }

        Result<?> result = new Result<>(errorCode,errorMsg);
        return result;
    }
}
