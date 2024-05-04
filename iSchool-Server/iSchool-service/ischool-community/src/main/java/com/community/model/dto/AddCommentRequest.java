package com.community.model.dto;

import lombok.Data;

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
