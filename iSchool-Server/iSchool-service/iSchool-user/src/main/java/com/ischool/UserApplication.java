package com.ischool;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-04-20 16:08
 * @description: 用户模块启动类
 **/
@SpringBootApplication
@MapperScan("com.ischool.mapper")
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

}
