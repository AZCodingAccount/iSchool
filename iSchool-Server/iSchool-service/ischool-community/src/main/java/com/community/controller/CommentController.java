package com.community.controller;

import com.community.model.dto.AddCommentRequest;
import com.community.model.vo.CommentsVO;
import com.community.service.CommentsService;

import com.ischool.model.BaseResponse;
import com.ischool.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-03 22:04
 * @description: 评论控制器
 **/
@RestController
@RequestMapping("/comment")
@Slf4j
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
    public BaseResponse<Object> addComment(@RequestBody AddCommentRequest addCommentRequest,
                                           @RequestHeader("id") Long id) {
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
    public BaseResponse<List<CommentsVO>> getCommentsList(@PathVariable Long objId) {
        log.info("获取点评对象{}的所有评论", objId);
        List<CommentsVO> commentsVOS = commentsService.getList(objId);
        return Result.success(commentsVOS);
    }

    /**
     * @description 给一级评论点赞
     * @param commentId
     * @return com.ischool.model.BaseResponse<java.lang.Object>
     **/
    @PutMapping("like/{commentId}")
    public BaseResponse<Object> addCommentLikes(@PathVariable Long commentId) {
        log.info("用户点赞，点赞评论id为{}", commentId);
        commentsService.addCommentLikes(commentId);
        return Result.success();
    }

    /**
     * @description 取消一级评论点赞
     * @param commentId
     * @return com.ischool.model.BaseResponse<java.lang.Object>
     **/
    @DeleteMapping("like/{commentId}")
    public BaseResponse<Object> decreaseCommentLikes(@PathVariable Long commentId) {
        log.info("用户点赞，点赞评论id为{}", commentId);
        commentsService.decreaseCommentLikes(commentId);
        return Result.success();
    }
}
