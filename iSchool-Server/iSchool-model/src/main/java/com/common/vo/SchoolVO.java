package com.common.vo;


import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "系统支持的学校实体类")
public class SchoolVO implements Serializable {
    /**
     * 学校id
     */
    @Schema(description = "学校id", example = "1789548655582642177")
    private Long id;

    /**
     * 学校名称
     */
    @Schema(description = "学校名称", example = "哈尔滨理工大学")
    private String schoolName;

    /**
     * 学校缩写
     */
    @Schema(description = "学校缩写", example = "HRBUST")
    private String schoolAbbr;
    @Serial
    private static final long serialVersionUID = 1L;
}