package com.qy.groovy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * groovy应用程序
 *
 * @author qiaoyan
 * @date 2023-02-02 17:17:57
 */
@ComponentScan(basePackages = {"com.qy"})
@SpringBootApplication
public class GroovyApplication {

    public static void main(String[] args) {
        SpringApplication.run(GroovyApplication.class, args);
    }

}
