package com.community.controller;

import com.client.service.SearchFeignClient;
import com.common.dto.MessageDto;
import com.community.model.dto.AddCommentRequest;
import com.community.model.dto.AddReplyCommentRequest;
import com.community.model.vo.CommentsVO;
import com.community.model.vo.ReplyCommentsVO;
import com.community.service.ReplyCommentsService;
import com.ischool.model.BaseResponse;
import com.ischool.model.ErrorCode;
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
 * @author: AlbertZhang
 * @create: 2024-05-04 15:31
 * @description: 回复的评论控制器
 **/
@RestController
@RequestMapping("/reply_comment")
@Slf4j
@Tag(name = "二级评论相关接口")
public class ReplyCommentController {

    @Autowired
    ReplyCommentsService replyCommentsService;

    @Autowired
    SearchFeignClient searchFeignClient;

    private static final String systemPrompt = "你是一个评论审核机器人，请筛选下面用户发表的评论，包括但不限于涉黄、涉黑、暴力、涉政等内容，请帮助我识别这个评论，如果有上述问题的话，请返回“不合格”，反之返回“合格”。注意，请严格按照上面要求的格式返回";


    /**
     * @param addReplyCommentRequest
     * @param id
     * @return com.ischool.model.BaseResponse<java.lang.Object>
     * @description 添加二级评论信息
     **/
    @PostMapping
    @Operation(summary = "添加二级评论信息")
    public BaseResponse<Object> addComment(
            @RequestBody AddReplyCommentRequest addReplyCommentRequest, @Parameter(hidden = true) @RequestHeader("id") Long id) {
        log.info("添加二级评论信息，评论信息为:{}", addReplyCommentRequest);
        // 校验是否含有违规信息
        String aiRes = searchFeignClient.chat(systemPrompt, addReplyCommentRequest.getReplyContent());
        log.info("ai回答的信息:{}", aiRes);
        if (aiRes.equals("不合格")) {
            return Result.error(ErrorCode.PARAMS_ERROR, "评论不合规");
        }
        replyCommentsService.add(addReplyCommentRequest, id);
        return Result.success();
    }


    /**
     * @param replyCommentId
     * @return com.ischool.model.BaseResponse<java.util.List < com.community.model.vo.ReplyCommentsVO>>
     * @description 获取某一级评论下的所有二级评论
     **/
    @GetMapping("{replyCommentId}")
    @Operation(summary = "获取某一级评论下的所有二级评论")
    public BaseResponse<List<ReplyCommentsVO>> getCommentsList(@Parameter(description = "一级评论id", example = "1789548655582642177") @PathVariable Long replyCommentId,
                                                               @Parameter(hidden = true) @RequestHeader("id") Long userId) {
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
    @Operation(summary = "给二级评论点赞")
    public BaseResponse<Object> addCommentLikes(@Parameter(description = "当前点赞二级评论id", example = "1789548655582642177") @PathVariable Long commentId,
                                                @Parameter(hidden = true) @RequestHeader("id") Long userId) {
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
    @Operation(summary = "取消二级评论点赞")
    public BaseResponse<Object> decreaseCommentLikes(@Parameter(description = "当前取消点赞二级评论id", example = "1789548655582642177") @PathVariable Long commentId,
                                                     @Parameter(hidden = true) @RequestHeader("id") Long userId) {
        log.info("用户{}取消点赞，点赞评论id为{}", userId, commentId);
        replyCommentsService.decreaseCommentLikes(userId, commentId);
        return Result.success();
    }

}
