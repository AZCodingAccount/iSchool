package com.community.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-03 22:42
 * @description: 评论VO
 **/
@Data
public class CommentsVO implements Serializable {
    /**
     *
     */
    private Long id;

    /**
     * 点评对象id
     */
    private Long objId;

    /**
     * 用户id
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
     * 用户头像
     */
    private String userAvatar;

    /**
     * 评论用户名
     */
    private String username;


    /**
     * 回复数
     */
    private Integer replyCount;

    /**
     * 发布时间
     */
    private LocalDateTime pubTime;

    private static final long serialVersionUID = 1L;
}
