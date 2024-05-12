package com.client.service;

import com.common.dto.MessageDto;
import com.common.dto.SocialDataDto;
import com.ischool.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-07 10:36
 * @description: 社区远程调用类
 **/
@FeignClient(name = "ischool-community", path = "/api/v1/community", configuration = FeignClientConfig.class)

public interface CommunityFeignClient {

    /**
     * @param id
     * @return java.util.List<com.common.dto.MessageDto>
     * @description 获取用户所有未读消息列表
     **/
    @GetMapping("/unread_messages")
    List<MessageDto> getUnreadMessageList(@RequestParam("id") Long id);

    /**
     * @param id
     * @param messageId
     * @return void
     * @description 消息已读
     **/
    @PutMapping("/read_message")
    Boolean readMessage(@RequestParam("id") Long id, @RequestParam("messageId") Long messageId);

    /**
     * @param id
     * @return com.common.dto.SocialDataDto
     * @description 获取当前用户的社交数据
     **/
    @GetMapping("/social_data")
    SocialDataDto getSocialData(@RequestParam("id") Long id);
}
