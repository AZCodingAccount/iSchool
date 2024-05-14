package com.search.controller;

import com.ischool.model.BaseResponse;
import com.ischool.model.ErrorCode;
import com.ischool.model.Result;
import com.search.ai.AIUtil;
import com.search.mq.AiMessageProducer;
import com.search.redis.RedisKeyConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-06 15:42
 * @description:
 **/
@RestController
@RequestMapping("/ai")
@Slf4j
public class AIController {


    @Autowired
    AiMessageProducer aiMessageProducer;

    @Autowired
    AIUtil aiUtil;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * @param keyword
     * @return com.ischool.model.BaseResponse<java.lang.Object>
     * @description 通知消息队列生成ai评论
     **/
    @GetMapping("/toGenRes")
    public BaseResponse<Object> aiSearch(@RequestParam("keyword") String keyword) {
        log.info("用户搜索，搜索信息为：{}", keyword);
    /*    // 使用雪花算法生成一个id，用于追踪这个搜索信息（后来发现不需要了，直接缓存了redis的keyword，拿着keyword就行）
        String id = IdUtil.getSnowflakeNextIdStr();
        Map<String, Object> message = new HashMap<>();
        message.put("id", id);
        message.put("message", keyword);*/
        // 先查看redis中是否有这个消息，有就直接返回
        if (Boolean.TRUE.equals(redisTemplate.hasKey(keyword))) {
            return Result.success();
        }
        // 通知消息队列生成消息
        aiMessageProducer.sendMessage(keyword);
        return Result.success();
    }

    /**
     * @param keyword
     * @return com.ischool.model.BaseResponse<java.lang.Object>
     * @description 获取ai生成的评论
     **/
    @GetMapping("/genRes")
    public BaseResponse<Object> aiSearchRes(@RequestParam("keyword") String keyword) {
        log.info("用户获取搜索结果，搜索信息为：{}", keyword);
        String key = RedisKeyConstant.AI_SEARCH_RESULT + keyword;
        // 先查看redis中是否有这个消息，有就直接返回（为了防止过期的情况）
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            Object aiRes = redisTemplate.opsForValue().get(RedisKeyConstant.AI_SEARCH_RESULT + keyword);
            String res = String.valueOf(aiRes);
            // 信息不为空，直接返回
            if (StringUtils.isNotBlank(res)) {
                return Result.success(res);
            }
        }
        // 信息为空或者消息队列没有消息，通知消息队列生成消息
        aiMessageProducer.sendMessage(keyword);
        return Result.error(ErrorCode.API_REQUEST_ERROR, "AI评论生成中，请稍后重试");
    }


    /**
     * @param message
     * @return com.ischool.model.BaseResponse<java.lang.String>
     * @description 用户聊天
     **/
    @GetMapping("/chat")
    public BaseResponse<String> chat(@RequestParam("message") String message,
                                     @RequestHeader("id") Long id) {
        log.info("用户{}聊天，聊天信息为：{}", id, message);
        String chatString = aiUtil.chat(id, message);
        return Result.success(chatString);
    }


}
