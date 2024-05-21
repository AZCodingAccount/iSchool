package com.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-07 10:27
 * @description: 信息传输Dto
 **/
@Data
@NoArgsConstructor
public class MessageDto {

    private Long id;

    /**
     * 当前评论用户id
     */
    private Long userId;

    /**
     * 当前评论用户昵称
     */
    private String userNickname;

    /**
     * 当前评论用户昵称
     */
    private String objName;
    /**
     * 当前评论对象id
     */
    private Long objId;

    /**
     * 回复用户id
     */
    private Long replyUserId;

    /**
     * 回复评论id
     */
    private Long replyCommentId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论点赞数
     */
    private Integer likes;

    /**
     * 发布时间
     */
    private LocalDateTime pubTime;
}
