package com.community.model.dto;

import lombok.Data;

/**
 * @program: iSchool-Server
 * @author: Ljx
 * @create: 2024-05-03 21:52
 * @description: 点评对象查询参数
 **/
@Data
public class CommentObjSearchParam {
    private String keyword; // 搜索关键字
    private String type;    // 搜索类型
}
