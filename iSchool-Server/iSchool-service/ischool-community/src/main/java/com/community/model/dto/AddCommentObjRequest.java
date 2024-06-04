package com.community.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: iSchool-Server
 * @author: Ljx
 * @create: 2024-05-03 21:30
 * @description: 添加点评对象DTO
 **/
@Data
@Schema(description = "点评对象请求实体", requiredProperties = {"name", "type"})
public class AddCommentObjRequest implements Serializable {
    /**
     * 点评对象名称
     */
    @Schema(description = "点评对象名称", example = "服务端架构设计")
    private String name;
    /**
     * 点评对象类型（课程|竞赛|老师）
     */
    @Schema(description = "点评对象类型", example = "课程")
    private String type;


    private static final long serialVersionUID = 1L;
}
