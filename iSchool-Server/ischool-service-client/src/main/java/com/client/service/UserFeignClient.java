package com.client.service;

import com.common.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-03 23:12
 * @description: 用户微服务提供的接口
 **/
@FeignClient(name = "ischool-user", path = "/api/v1/user")
public interface UserFeignClient {

    /**
     * @param id
     * @return com.common.dto.UserDto
     * @description 获取用户信息
     **/
    @GetMapping
    UserDto getLoginUser(@RequestHeader("id") Long id);

}
