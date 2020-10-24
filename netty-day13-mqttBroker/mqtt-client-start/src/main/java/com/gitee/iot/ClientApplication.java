package com.gitee.iot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import static org.springframework.boot.SpringApplication.run;

/**
 * <p>启动类
 * @author jie
 */
@SuppressWarnings("ALL")
@SpringBootApplication
@EnableAspectJAutoProxy //注解开启对aspectJ的支持
public class ClientApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = run(ClientApplication.class, args);
    }
}
