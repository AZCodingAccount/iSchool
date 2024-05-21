package com.community.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName user_comment_likes
 */
@TableName(value ="user_comment_likes")
@Data
public class UserCommentLikes implements Serializable {
    /**
     * 记录id
     */
    @TableId
    private Long id;

    /**
     * 点赞的用户id
     */
    private Long userId;

    /**
     * 点赞的评论id
     */
    private Long commentId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}