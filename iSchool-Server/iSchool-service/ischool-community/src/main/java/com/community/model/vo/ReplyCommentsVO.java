package com.community.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-04 15:55
 * @description: 回复评论数VO
 **/
@Data
public class ReplyCommentsVO implements Serializable {

    /**
     * 评论id
     */
    private Long id;
    /**
     * 当前评论对象id
     */
    private Long objId;

    /**
     * 发送评论的用户id
     */
    private Long userId;

    /**
     * 评论内容
     */
    private String content;


    /**
     * 点赞数
     */
    private Integer likes;

    /**
     * 用户是否点赞
     */
    private Boolean liked;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 评论用户名
     */
    private String username;

    /**
     * 回复的id
     */
    private Long replyUserId;
    /**
     * 回复的用户名
     */
    private String replyUsername;

    /**
     * 回复的用户头像
     */
    private String replyUserAvatar;

    /**
     * 发布时间
     */
    private LocalDateTime pubTime;

    private static final long serialVersionUID = 1L;
}
