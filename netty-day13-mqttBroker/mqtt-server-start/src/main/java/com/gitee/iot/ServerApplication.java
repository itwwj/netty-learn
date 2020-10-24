package com.gitee.iot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


/**
 * <p>启动类
 *  @EnableAspectJAutoProxy //注解开启对aspectJ的支持
 * @author jie
 */
@SpringBootApplication
@EnableAspectJAutoProxy
public class ServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class,args);
    }
}
