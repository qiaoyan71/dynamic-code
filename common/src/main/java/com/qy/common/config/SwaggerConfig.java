package com.qy.common.config;//package cn.dmego.seata.common.config;
//
//import com.google.common.base.Predicates;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
///**
// * swagger2配置
// *
// * @author qiaoyan
// * @date 2022-11-24 15:09:42
// */
//@Configuration
//@EnableSwagger2
//public class SwaggerConfig {
//    @Bean
//    public Docket webApiConfig(){
//        return new Docket(DocumentationType.SWAGGER_2)
//                .groupName("webApi")
//                .apiInfo(webApiInfo())
//                .select()
//                .paths(Predicates.not(PathSelectors.regex("/admin/.*")))
//                .paths(Predicates.not(PathSelectors.regex("/error.*")))
//                .build();
//    }
//    private ApiInfo webApiInfo(){
//        return new ApiInfoBuilder()
//                .title("API文档")
//                .description("微服务接口定义")
//                .version("1.0")
//                .contact(new Contact("doc", "http://qydoc.com", "abc@qq.com"))
//                .build();
//    }
//}