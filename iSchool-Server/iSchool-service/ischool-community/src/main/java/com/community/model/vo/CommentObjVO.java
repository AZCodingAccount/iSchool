package com.community.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @TableName obj
 */
@Data
public class CommentObjVO implements Serializable {
    /**
     *
     */
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
     * 用户评分，未评分为0
     */
    private Double userScore;

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