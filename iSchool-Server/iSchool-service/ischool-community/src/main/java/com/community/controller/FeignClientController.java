package com.community.controller;

import com.common.dto.MessageDto;
import com.common.dto.SocialDataDto;
import com.community.service.CommentsService;
import com.community.service.ReplyCommentsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-07 11:09
 * @description: 远程调用控制器
 **/
@RestController
@RequestMapping
@Slf4j
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
