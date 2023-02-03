package com.qy.javassist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * javassist应用程序
 *
 * @author qiaoyan
 * @date 2023-02-02 16:25:07
 */
@ComponentScan(basePackages = {"com.qy"})
@SpringBootApplication
public class JavassistApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavassistApplication.class, args);
    }

}
