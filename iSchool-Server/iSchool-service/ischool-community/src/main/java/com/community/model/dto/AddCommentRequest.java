package com.community.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "添加一级评论请求实体", requiredProperties = {"objId", "content"})
public class AddCommentRequest {
    /**
     * 点评对象id
     */
    @Schema(description = "点评对象id", example = "1789548655582642177")
    private Long objId;


    /**
     * 评论内容
     */
    @Schema(description = "评论内容", example = "1789548655582642177")
    private String content;
}
