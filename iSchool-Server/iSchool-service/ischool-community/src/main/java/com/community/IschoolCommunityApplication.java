package com.community;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@MapperScan("com.community.mapper")
@EnableFeignClients(basePackages = {"com.client.service"})
public class IschoolCommunityApplication {

	public static void main(String[] args) {
		SpringApplication.run(IschoolCommunityApplication.class, args);
	}

}
