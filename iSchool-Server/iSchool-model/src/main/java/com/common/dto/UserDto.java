package com.common.dto;



import java.io.Serializable;

import lombok.Data;

/**
 * 返回给前端的数据
 */
@Data
public class UserDto implements Serializable {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;
    /**
     * 昵称
     */
    private String nickname;

    /**
     * 用户性别 男  女
     */
    private String gender;

    /**
     * 用户年龄
     */
    private Integer age;

    /**
     * 用户头像url
     */
    private String userAvatar;

    /**
     * 用户邮箱
     */
    private String email;


    /**
     * 用户获赞总数
     */
    private Integer totalLikes;

    /**
     * 用户被评论总数
     */
    private Integer totalComments;

    private static final long serialVersionUID = 1L;
}