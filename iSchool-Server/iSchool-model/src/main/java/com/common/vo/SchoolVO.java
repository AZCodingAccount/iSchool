package com.common.vo;


import lombok.Data;

import java.io.Serial;
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

    /**
     * 学校缩写
     */
    private String schoolAbbr;
    @Serial
    private static final long serialVersionUID = 1L;
}