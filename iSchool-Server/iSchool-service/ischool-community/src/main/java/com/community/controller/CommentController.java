package com.community.controller;

import com.community.model.dto.AddCommentRequest;
import com.community.model.vo.CommentsVO;
import com.community.service.CommentsService;

import com.ischool.model.BaseResponse;
import com.ischool.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: iSchool-Server
 * @author: Ljx
 * @create: 2024-05-03 22:04
 * @description: 评论控制器
 **/
@RestController
@RequestMapping("/comment")
@Slf4j
@Tag(name = "一级评论相关接口")
public class CommentController {
    @Autowired
    CommentsService commentsService;

    /**
     * @param addCommentRequest
     * @param id
     * @return com.common.model.BaseResponse<java.lang.Object>
     * @description 添加一级评论
     **/
    @PostMapping
    @Operation(summary = "添加评论信息")
    public BaseResponse<Object> addComment(@RequestBody AddCommentRequest addCommentRequest,
                                           @Parameter(hidden = true) @RequestHeader("id") Long id) {
        log.info("添加评论信息，评论信息为:{}", addCommentRequest);
        commentsService.add(addCommentRequest, id);
        return Result.success();
    }

    /**
     * @param objId
     * @return com.ischool.model.BaseResponse<java.util.List < com.community.model.vo.CommentsVO>>
     * @description 获取某一点评对象的所有评论
     **/
    @GetMapping("{objId}")
    @Operation(summary = "获取某一点评对象的所有评论")
    public BaseResponse<List<CommentsVO>> getCommentsList(@Parameter(description = "点评对象id") @PathVariable Long objId) {
        log.info("获取点评对象{}的所有评论", objId);
        List<CommentsVO> commentsVOS = commentsService.getList(objId);
        return Result.success(commentsVOS);
    }

    /**
     * @param commentId
     * @return com.ischool.model.BaseResponse<java.lang.Object>
     * @description 给一级评论点赞
     **/
    @PutMapping("like/{commentId}")
    @Operation(summary = "给一级评论点赞")
    public BaseResponse<Object> addCommentLikes(@Parameter(description = "一级评论id") @PathVariable Long commentId,
                                                @Parameter(hidden = true) @RequestHeader("id") Long userId) {
        log.info("用户{}点赞，点赞评论id为{}", userId, commentId);
        commentsService.addCommentLikes(userId, commentId);
        return Result.success();
    }

    /**
     * @param commentId
     * @return com.ischool.model.BaseResponse<java.lang.Object>
     * @description 取消一级评论点赞
     **/
    @DeleteMapping("like/{commentId}")
    @Operation(summary = "取消一级评论点赞")
    public BaseResponse<Object> decreaseCommentLikes(@Parameter(description = "一级评论id") @PathVariable Long commentId,
                                                     @Parameter(hidden = true) @RequestHeader("id") Long userId) {
        log.info("用户{}取消点赞，取消点赞评论id为{}", userId, commentId);
        commentsService.decreaseCommentLikes(userId, commentId);
        return Result.success();
    }
}
