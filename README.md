# springboot-plus

# 项目介绍
对spring boot的功能进行了增强

- 全局异常处理
- 替换jackson为fastjson
- json返回值封装到result对象中


# 安装教程

直接加入pom依赖即可

# 使用说明

## 一般使用

直接使用@SpringBootPlus即可

```java
@SpringBootPlus
@SpringBootApplication
public class WebServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebServerApplication.class,args);
    }
}
```

## 功能说明
|类|功能|
|-|-|
|com.xiaojiezhu.springbootplus.web.FastJsonSupport|返回数据时，使用fastjson|
|com.xiaojiezhu.springbootplus.web.ResultValueConfiguration|把返回值包装成为result|
|com.xiaojiezhu.springbootplus.web.ex.ResultExceptionHandler|在出现异常时，也包装成为result输出错误结果|
