package com.community.controller;

import com.community.model.dto.AddCommentObjRequest;
import com.community.model.dto.CommentObjSearchParam;
import com.community.model.dto.ScoreRequest;
import com.community.model.entity.CommentObj;
import com.community.model.vo.CommentObjVO;
import com.community.service.CommentObjService;

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
 * @create: 2024-05-03 21:05
 * @description: 对象控制器
 **/
@RestController
@RequestMapping("/comment_obj")
@Slf4j
@Tag(name = "点评对象相关接口")
public class CommentObjController {
    @Autowired
    CommentObjService commentObjService;

    /**
     * @param addCommentObjRequest
     * @return com.common.model.BaseResponse<java.lang.Object>
     * @description 添加点评对象
     **/
    @PostMapping
    @Operation(summary = "添加点评对象")
    public BaseResponse<Object> addCommentObj(@RequestBody AddCommentObjRequest addCommentObjRequest) {
        log.info("添加点评对象，点评对象为{}", addCommentObjRequest);
        commentObjService.add(addCommentObjRequest);
        return Result.success();
    }

    /**
     * @param keyword
     * @param type
     * @return com.common.model.BaseResponse<java.util.List < com.community.model.entity.CommentObj>>
     * @description 搜索点评对象
     **/
    @GetMapping("/search")
    @Operation(summary = "搜索点评对象")
    public BaseResponse<List<CommentObjVO>> searchCommentObj(
            @Parameter(description = "搜索关键字", example = "服务端") @RequestParam("keyword") String keyword
            , @Parameter(description = "搜索类型", example = "课程") @RequestParam("type") String type
            , @Parameter(hidden = true) @RequestHeader("id") Long userId) {
        log.info("用户{}查询点评对象，查询参数为{}:{}", userId, keyword, type);
        // todo：添加点赞量最高的评论
        List<CommentObjVO> commentObjVOS = commentObjService.search(keyword, type, userId);
        return Result.success(commentObjVOS);
    }

    // 删改暂时不实现

    @PutMapping("score")
    @Operation(summary = "用户评分")
    public BaseResponse<Object> score(@RequestBody ScoreRequest scoreRequest,
                                      @Parameter(hidden = true) @RequestHeader("id") Long id) {
        log.info("用户{}评分，评分参数为{}", id, scoreRequest);
        commentObjService.score(scoreRequest, id);
        return Result.success();
    }
}
