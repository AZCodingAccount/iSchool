package com.community.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * @TableName reply_comments
 */
@TableName(value = "reply_comments")
@Data
public class ReplyComments implements Serializable {
    /**
     *
     */
    @TableId
    private Long id;

    /**
     * 当前评论对象id
     */
    private Long objId;
    /**
     * 所属父级评论id
     */
    private Long parentCommentId;
    /**
     * 当前评论用户id
     */
    private Long userId;

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
     * 评论是否已读
     */
    private Integer readed;

    /**
     * 发布时间
     */
    private LocalDateTime pubTime;

    private static final long serialVersionUID = 1L;
}