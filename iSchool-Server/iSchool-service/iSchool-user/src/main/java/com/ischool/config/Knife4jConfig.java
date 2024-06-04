package com.ischool.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// @Configuration
public class Knife4jConfig {
    // @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("大数据量下的教务文档管理系统")
                        .version("1.0")
                        .description("iSchool接口文档")
                        .termsOfService("https://www.ischool.com")
                        .contact(new Contact().name("Ljx").url("https://www.bugdesigner.cn")
                                .email("han892577@qq.com"))
                );
    }



}