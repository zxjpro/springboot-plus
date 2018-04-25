# springboot-plus

#### 项目介绍
对spring boot的功能进行了增强

- 全局异常处理
- 替换jackson为fastjson
- json返回值封装到result对象中

#### 软件架构
软件架构说明


#### 安装教程

直接加入pom依赖即可

#### 使用说明

```java
@SpringBootPlus
@SpringBootApplication
public class WebServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebServerApplication.class,args);
    }
}
```
