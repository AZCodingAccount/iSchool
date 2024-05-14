package com.search.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-06 18:12
 * @description: 我的message类
 **/
@Data
// @NoArgsConstructor
// @AllArgsConstructor
public class MyMessage implements MessageInterface{
    private String name;
    private String content;

    public MyMessage() {
    }

    public MyMessage(String name, String content) {
        this.name = name;
        this.content = content;
    }
}
