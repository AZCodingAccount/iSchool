package com.community.model.dto;

import lombok.Data;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-03 22:26
 * @description: 添加评论请求
 **/
@Data
public class AddCommentRequest {
    /**
     * 点评对象id
     */
    private Long objId;


    /**
     * 评论内容
     */
    private String content;
}
