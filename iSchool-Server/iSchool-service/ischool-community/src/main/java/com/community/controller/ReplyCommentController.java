package com.community.controller;

import com.common.dto.MessageDto;
import com.community.model.dto.AddCommentRequest;
import com.community.model.dto.AddReplyCommentRequest;
import com.community.model.vo.CommentsVO;
import com.community.model.vo.ReplyCommentsVO;
import com.community.service.ReplyCommentsService;
import com.ischool.model.BaseResponse;
import com.ischool.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-04 15:31
 * @description: 回复的评论控制器
 **/
@RestController
@RequestMapping("/reply_comment")
@Slf4j
public class ReplyCommentController {

    @Autowired
    ReplyCommentsService replyCommentsService;


    /**
     * @param addReplyCommentRequest
     * @param id
     * @return com.ischool.model.BaseResponse<java.lang.Object>
     * @description 添加二级评论信息
     **/
    @PostMapping
    public BaseResponse<Object> addComment(@RequestBody AddReplyCommentRequest addReplyCommentRequest, @RequestHeader("id") Long id) {
        log.info("添加二级评论信息，评论信息为:{}", addReplyCommentRequest);
        replyCommentsService.add(addReplyCommentRequest, id);
        return Result.success();
    }


    /**
     * @param replyCommentId
     * @return com.ischool.model.BaseResponse<java.util.List < com.community.model.vo.ReplyCommentsVO>>
     * @description 获取某一级评论下的所有二级评论
     **/
    @GetMapping("{replyCommentId}")
    public BaseResponse<List<ReplyCommentsVO>> getCommentsList(@PathVariable Long replyCommentId,
                                                               @RequestHeader("id") Long userId) {
        log.info("用户{}获取一级评论{}下的所有评论", userId, replyCommentId);
        List<ReplyCommentsVO> replyCommentsVOList = replyCommentsService.getList(userId, replyCommentId);
        return Result.success(replyCommentsVOList);
    }

    /**
     * @param commentId
     * @return com.ischool.model.BaseResponse<java.lang.Object>
     * @description 给二级评论点赞
     **/
    @PutMapping("like/{commentId}")
    public BaseResponse<Object> addCommentLikes(@PathVariable Long commentId,
                                                @RequestHeader("id") Long userId) {
        log.info("用户{}点赞，点赞评论id为{}", userId, commentId);
        replyCommentsService.addCommentLikes(userId, commentId);
        return Result.success();
    }

    /**
     * @param commentId
     * @return com.ischool.model.BaseResponse<java.lang.Object>
     * @description 取消二级评论点赞
     **/
    @DeleteMapping("like/{commentId}")
    public BaseResponse<Object> decreaseCommentLikes(@PathVariable Long commentId,
                                                     @RequestHeader("id") Long userId) {
        log.info("用户{}取消点赞，点赞评论id为{}", userId, commentId);
        replyCommentsService.decreaseCommentLikes(userId, commentId);
        return Result.success();
    }

}
