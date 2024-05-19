package com.common.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 支持的学校表
 *
 * @TableName school
 */
@Data
public class SchoolVO implements Serializable {
    /**
     * 学校id
     */
    private Long id;

    /**
     * 学校名称
     */
    private String schoolName;
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}