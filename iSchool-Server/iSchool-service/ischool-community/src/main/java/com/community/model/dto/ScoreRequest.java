package com.community.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-04 14:34
 * @description: 点评对象评分请求
 **/
@Data
@Schema(description = "评分请求实体", requiredProperties = {"commentObjId", "score"})
public class ScoreRequest {

    @Schema(description = "评分的对象id", example = "1789548655582642177")
    private Long commentObjId;

    @Schema(description = "评分分数", example = "7.5")
    private Double score;
}
