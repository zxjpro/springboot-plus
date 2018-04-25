package com.xiaojiezhu.springbootplus.web.annotation;

import java.lang.annotation.*;

/**
 *
 * 时间 2018/4/25 16:20<br/>
 *
 * 说明 此注解不能与 {@link org.springframework.web.bind.annotation.ResponseBody} 并存 <br/>
 *
 *     这个注解会返回一段Json,封装后的json，封装格式 {@link com.xiaojiezhu.springbootplus.Result}，原方法的返回值会封装到data中
 *      <pre>
 *          比如返回的数据是String类型的，返回内容是 "HelloWorld"，那么如果方法中带有这个注解，那么实际返回格式为
 *
 *          {
 *              "code" : 0,
 *              "msg" : "SUCCESS",
 *              "data" : "HelloWorld"
 *          }
 *
 *          同理，如果返回的本身是一个对象或者是一个数组的话，那么data里面会嵌套json
 *      </pre>
 *      @author zxj
 */
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResultResponseBody {
}
