package com.community.controller;

import com.community.model.dto.AddCommentObjRequest;
import com.community.model.dto.CommentObjSearchParam;
import com.community.model.dto.ScoreRequest;
import com.community.model.entity.CommentObj;
import com.community.service.CommentObjService;

import com.ischool.model.BaseResponse;
import com.ischool.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-03 21:05
 * @description: 对象控制器
 **/
@RestController
@RequestMapping("/comment_obj")
@Slf4j
public class CommentObjController {
    @Autowired
    CommentObjService commentObjService;

    /**
     * @param addCommentObjRequest
     * @return com.common.model.BaseResponse<java.lang.Object>
     * @description 添加点评对象
     **/
    @PostMapping
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
    public BaseResponse<List<CommentObj>> searchCommentObj(@RequestParam("keyword") String keyword, @RequestParam
    String type) {
        log.info("查询点评对象，查询参数为{}:{}", keyword, type);
        // todo：添加点赞量最高的评论
        List<CommentObj> commentObjs = commentObjService.search(keyword, type);
        return Result.success(commentObjs);
    }

    // 删改暂时不实现

    @PutMapping("score")
    public BaseResponse<Object> score(@RequestBody ScoreRequest scoreRequest,
                                      @RequestHeader Long id) {
        log.info("用户{}评分，评分参数为{}", id, scoreRequest);
        commentObjService.score(scoreRequest, id);
        return Result.success();
    }
}
