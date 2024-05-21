package com.client.service;

import com.common.dto.UserDto;
import com.common.vo.SchoolVO;
import com.ischool.config.FeignClientConfig;
import com.ischool.model.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


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
    @GetMapping("/rpc")
    BaseResponse<UserDto> getLoginUser(@RequestHeader("id") Long id);


    /**
     * @param id
     * @return java.lang.Boolean
     * @description 检查用户id是否存在
     **/
    @GetMapping("/id")
    Boolean checkId(@RequestParam("id") Long id);


    /**
     * @param
     * @return java.util.List<com.common.vo.SchoolVO>
     * @description 获取所有学校列表
     **/
    @GetMapping("/schools")
    BaseResponse<List<SchoolVO>> getSchoolList();

}
