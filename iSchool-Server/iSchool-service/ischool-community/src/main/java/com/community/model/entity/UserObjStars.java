package com.community.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName user_obj_stars
 */
@TableName(value ="user_obj_stars")
@Data
public class UserObjStars implements Serializable {
    /**
     * 记录id
     */
    @TableId
    private Long id;

    /**
     * 评分的用户id
     */
    private Long userId;

    /**
     * 评分的对象id(一级二级)
     */
    private Long objId;

    /**
     * 评分的分数
     */
    private Double score;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}