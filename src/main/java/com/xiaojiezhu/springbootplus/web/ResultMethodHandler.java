package com.xiaojiezhu.springbootplus.web;

import com.alibaba.fastjson.JSON;
import com.xiaojiezhu.springbootplus.Result;
import com.xiaojiezhu.springbootplus.web.annotation.ResultResponseBody;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.AsyncHandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * @author zxj<br>
 * 时间 2018/4/25 16:29
 * 说明 ...
 */
public class ResultMethodHandler implements AsyncHandlerMethodReturnValueHandler {

    //返回true就被处理
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return returnType.getMethodAnnotation(ResultResponseBody.class) != null;
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        //设置为已经完成该处理
        mavContainer.setRequestHandled(true);
        HttpServletResponse response = (HttpServletResponse) webRequest.getNativeResponse();
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            //把得到的数据，包装到Resuest中返回
            Result<Object> success = new Result<>(0, "SUCCESS", returnValue);
            writer.write(JSON.toJSONString(success));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(writer != null){
                writer.close();
            }
        }
    }


    //返回true就被处理
    @Override
    public boolean isAsyncReturnValue(Object returnValue, MethodParameter returnType) {
        boolean r = returnType.getMethodAnnotation(ResultResponseBody.class) != null;
        return r;
    }


}
