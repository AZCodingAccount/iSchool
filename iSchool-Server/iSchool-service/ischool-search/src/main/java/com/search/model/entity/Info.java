package com.search.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * @TableName info
 */
@TableName(value = "info")
@Data
public class Info implements Serializable {
    /**
     *
     */
    @TableId
    private Long id;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 公告发布时间
     */
    private LocalDate pubTime;

    /**
     * 公告url
     */
    private String url;

    /**
     * 公告所属学校
     */
    private String school;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}