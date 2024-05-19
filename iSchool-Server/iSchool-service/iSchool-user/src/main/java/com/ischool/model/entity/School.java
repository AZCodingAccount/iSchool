package com.ischool.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 支持的学校表
 * @TableName school
 */
@TableName(value ="school")
@Data
public class School implements Serializable {
    /**
     * 学校id
     */
    @TableId
    private Long id;

    /**
     * 学校名称
     */
    private String schoolName;

    /**
     * 学校接入系统创建时间
     */
    private LocalDateTime createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}