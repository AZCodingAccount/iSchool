package com.community.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 
 * @TableName obj
 */
@TableName(value ="obj")
@Data
public class CommentObj implements Serializable {
    /**
     * 
     */
    @TableId
    private Long id;

    /**
     * 点评对象名称
     */
    private String name;
    /**
     * 点评对象类型（课程|竞赛|老师）
     */
    private String type;

    /**
     * 评论数
     */
    private Integer commentCount;

    /**
     * 平均评分
     */
    private Double score;

    /**
     * 
     */
    private Integer count;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}