package com.community.model.dto;

import lombok.Data;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-04 15:34
 * @description: 添加回复评论
 **/
@Data
public class AddReplyCommentRequest {
    /*
     * 回复的评论id
     */
    private Long commentId;
    /*
     * 回复的内容
     */
    private String replyContent;
    /*
     * 回复给的用户
     */
    private Long replyUserId;
}