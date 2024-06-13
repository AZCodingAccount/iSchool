package com.search.controller;

import com.ischool.model.BaseResponse;
import com.ischool.model.ErrorCode;
import com.ischool.model.Result;
import com.search.ai.AIUtil;
import com.search.mq.AiMessageProducer;
import com.search.redis.RedisKeyConstant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "AI模块相关接口")
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
    @Operation(summary = "通知后端去生成ai建议")
    public BaseResponse<Object> aiSearch(@Parameter(description = "用户搜索关键词", required = true, example = "蓝桥杯") @RequestParam("keyword") String keyword) {
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
    @Operation(summary = "获取ai生成好的评论", description = "系统生成成功直接返回，如果系统还未生成完成，系统返回错误码50010")
    public BaseResponse<Object> aiSearchRes(@Parameter(description = "用户搜索关键词", required = true, example = "蓝桥杯")
                                            @RequestParam("keyword") String keyword) {
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
    @Operation(summary = "用户进行聊天")
    public BaseResponse<String> chat(@Parameter(description = "用户聊天信息", required = true, example = "你好") @RequestParam("message") String message,
                                     @Parameter(hidden = true) @RequestHeader("id") Long id) {
        log.info("用户{}聊天，聊天信息为：{}", id, message);
        String chatString = aiUtil.chat(id, message);
        return Result.success(chatString);
    }


}
