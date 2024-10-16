package com.client.service;

import com.common.dto.UserDto;
import com.common.vo.SchoolVO;
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
 * @description: 搜索微服务提供的接口
 **/
@FeignClient(name = "ischool-search", path = "/api/v1/search")
public interface SearchFeignClient {

    /**
     * 跟ai聊天（不支持历史记录）
     *
     * @param role    预设
     * @param message 聊天消息
     * @return
     */
    @GetMapping("/ai/chat/rpc")
    String chat(@RequestParam("role") String role, @RequestParam("message") String message);
}
