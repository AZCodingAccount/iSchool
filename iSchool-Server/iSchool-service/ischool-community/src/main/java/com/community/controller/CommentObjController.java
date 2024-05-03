package com.community.controller;

import com.community.model.dto.AddCommentObjRequest;
import com.community.model.dto.CommentObjSearchParam;
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

    // 删改暂时不实现
    /**
     * @description 搜索点评对象
     * @param commentObjSearchParam
     * @return com.common.model.BaseResponse<java.util.List<com.community.model.entity.CommentObj>>
     **/
    @GetMapping
    public BaseResponse<List<CommentObj>> searchCommentObj(@RequestBody CommentObjSearchParam commentObjSearchParam) {
        log.info("查询点评对象，查询参数为{}", commentObjSearchParam);
        List<CommentObj> commentObjs = commentObjService.search(commentObjSearchParam);
        return Result.success(commentObjs);
    }
}
