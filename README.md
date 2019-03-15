[TOC]

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


# 分布式锁

springboot-plus 提供了分布式锁的功能。

首先，在启动类中，加上 `@EnableLock` 注解，它位于 `com.xiaojiezhu.springbootplus.lock.annotation.EnableLock`

```java
import com.xiaojiezhu.springbootplus.lock.annotation.EnableLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@EnableLock
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class , args);
    }
}
```

## 初始化本地锁

springboot-plus 对锁做了一个高度的抽象，也就意味着，在开发阶段，使用`本地锁`就可以了，而上线到生产环境时，不需要修改任何代码，
就可以使用`分布式锁`。

本地锁我们不需要做任何额外的配置，springboot-plus 会初始化`本地锁`的所有环境，在代码中直接使用即可。


## 初始化分布式锁

初始化分布式锁，需要 `redis` 的支持，所以我们需要注入 `redis` 的配置。

> 在这个项目中，我们并没有使用 `jedis` 的官方 api，确实是因为 `jedis` 的 api 不友好，现在连 spring 都已经弃用 `jedis` 了。
而在 `springboot-plus` 中，我们使用的是 `redisson`，这也是一个很好用的 redis 客户端


我们初始化一个 `redisson` 的客户端

```java
    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379").setPassword("123");
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }
```

我们要做的仅此而已，当我们注入一个 `redisson` 客户端时，`springboot-plus` 会自动把锁的机制注册成为 `分布式锁`


## 使用方法

使用方法很简单，我们只需要在需要加锁的方法中，加上 `@PLock()` 注解就可以了，它位于 `com.xiaojiezhu.springbootplus.lock.annotation.PLock`

代码如下

```java
    @PLock(value = "key{0}hello{2}a" , expireTime = 3000 , timeUnit = TimeUnit.MILLISECONDS)
    public String b(String name , int age , String nickName){
        System.out.println("b");
        return "b";
    }

```

这里介绍一下 `@PLock()` 注解的参数

- value 

这是锁的key，只要持有相同的 key ， 就会去竞争同一把锁，也就意味着 `@PLock()` 注解锁定的不是这个方法，而是注解上面的字符串，
如果有多个方法拥有着同一个锁定字符串，那么会去竞争同一把锁。key 里面可以包含占位符 `{0}` 代表着取方法中的第一个参数。

如果传入的参数分别为 `知问` , 1 , `知问`，那么锁定的字符串为 `key知问hello知问a`。我们也可以不传入标识符，这样会对整个字符串锁定，

一般情况下，我们会对具体的一个会员ID，或者订单号，进行局部的锁定


- expireTime 这是过期时间，在获取锁时，在全局只能有一个线程占用锁，除非方法结束，又或者这里的锁过期时间到了，这主要是为了
在分布式锁的情况下，redis宕机，或者应用程序宕机造成的无限死锁。
- timeUnit 这里是过期时间的单位



在运行成功时，会打印出下面的日志，我们可以用它来判断是否正常运行

```java
2019-03-15 11:29:40.853 DEBUG 205384 --- [nio-8090-exec-5] springboot.plus.lock                     : 准备切入锁 : HelloServiceImpl.a()
2019-03-15 11:29:40.857 DEBUG 205384 --- [nio-8090-exec-5] springboot.plus.lock                     : 锁成功 : HelloServiceImpl.a()
2019-03-15 11:29:40.865 DEBUG 205384 --- [nio-8090-exec-5] springboot.plus.lock                     : 锁释放 : HelloServiceImpl.a()
```

注意，这是 `DEBUG` 级别的日志，如果我们需要它显示，需要手工开启

```xml
<logger name="springboot.plus.lock" level="DEBUG"></logger>
```



## 重要提示：

因为 `springboot-plus` 的锁机制是通过 `aop` 来实现的，而 `aop` 在使用的过程中有限制，就是在同一个类中自调用，那么切面是不生效的

如下

```java
@Service
public class HelloServiceImpl implements HelloService{



    @Override
    @PLock(value = "hello{1}{2}world{4}" , expireTime = 2000)
    public String a(String name , int age , String nation , int grade , String img){
        this.b(name); // 这种调用方式，是不会进入第二个切面的
        b(name); // 这种方式也不会进入第二个切面
        return "a";
    }

    @Override
    @PLock(value = "key{0}hello{0}a" , expireTime = 3000 , timeUnit = TimeUnit.MILLISECONDS)
    public String b(String name){
        System.out.println("b");
        return "b";
    }
}
```

也就是说在方法的自调用，调用的是具体的实例，而不是被代理的实例。

解决办法如下

```java
@Service
public class HelloServiceImpl implements HelloService{

    // 这里可以预先注入实例
    @Autowired
    private HelloService helloService;

    @Override
    @PLock(value = "hello{1}{2}world{4}" , expireTime = 2000)
    public String a(String name , int age , String nation , int grade , String img){
        ((HelloService) AopContext.currentProxy()).b(); // 这是拿到代理对象再调用
        helloService.b(name); // 这里是使用预先注入的对象
        return "a";
    }

    @Override
    @PLock(value = "key{0}hello{0}a" , expireTime = 3000 , timeUnit = TimeUnit.MILLISECONDS)
    public String b(String name){
        System.out.println("b");
        return "b";
    }
}
```

通过上面提供的两种方法，就能解决在方法内调用，切面不生效的问题
