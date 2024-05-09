package com.community.model.dto;

import lombok.Data;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-04 14:34
 * @description: 点评对象评分请求
 **/
@Data
public class ScoreRequest {
    private Long commentObjId;
    private Double score;
}
