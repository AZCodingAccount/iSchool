package com.community.controller;

import com.common.dto.MessageDto;
import com.common.dto.SocialDataDto;
import com.community.service.CommentsService;
import com.community.service.ReplyCommentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: iSchool-Server
 * @author: Ljx
 * @create: 2024-05-07 11:09
 * @description: 远程调用控制器
 **/
@RestController
@RequestMapping
@Slf4j
@Tag(name = "社区模块暴露出的RPC相关接口")
public class FeignClientController {

    @Autowired
    ReplyCommentsService replyCommentsService;

    @Autowired
    CommentsService commentsService;

    /**
     * @param id
     * @return java.util.List<com.common.dto.MessageDto>
     * @description 获取用户未读消息列表
     **/
    @GetMapping("/unread_messages")
    @Operation(summary = "获取用户未读消息列表", description = "供后端系统远程调用，前端不需关注")
    public List<MessageDto> getUnreadMessageList(@RequestParam("id") Long id) {
        log.info("远程调用：获取用户{}未读消息列表", id);
        List<MessageDto> messageDtoList = replyCommentsService.getUnreadMessageList(id);
        return messageDtoList;
    }

    /**
     * @param id
     * @param messageId
     * @return void
     * @description 已读消息
     **/
    @PutMapping("/read_message")
    @Operation(summary = "标记消息为已读", description = "供后端系统远程调用，前端不需关注")
    public Boolean readMessage(@RequestParam("id") Long id, @RequestParam("messageId") Long messageId) {
        log.info("远程调用：用户{}将消息{}标记为已读", id, messageId);
        return replyCommentsService.readMessage(id, messageId);
    }

    /**
     * @param id
     * @return java.util.List<com.common.dto.SocialDataDto>
     * @description 获取用户的点赞和评论
     **/
    @GetMapping("/social_data")
    @Operation(summary = "获取用户社交相关数据", description = "供后端系统远程调用，前端不需关注")
    public SocialDataDto getSocialData(@RequestParam("id") Long id) {
        log.info("远程调用：获取用户{}的社交数据", id);
        // 1: 获取一级和二级评论的点赞和评论数据
        SocialDataDto socialData1 = commentsService.getSocialData(id);
        SocialDataDto socialData2 = replyCommentsService.getSocialData(id);

        // 2: 拼装数据
        SocialDataDto resultData = new SocialDataDto();

        resultData.setTotalLikes(socialData1.getTotalLikes() + socialData2.getTotalLikes());

        resultData.setTotalComments(socialData2.getTotalComments());
        return resultData;
    }
}
