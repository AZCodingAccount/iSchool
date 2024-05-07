package com.common.dto;

import lombok.Data;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-07 11:11
 * @description: 用户社交属性
 **/
@Data
public class SocialDataDto {
    /**
     * 用户获赞总数
     */
    private Integer totalLikes;

    /**
     * 用户被评论总数
     */
    private Integer totalComments;
}
