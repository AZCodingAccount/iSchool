package com.common.dto;


import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 返回给前端的数据
 */
@Data
@Schema(description = "用户信息DTO")
public class UserDto implements Serializable {
    /**
     * 用户id
     */
    @Schema(description = "用户id", example = "1789548655582642177")
    private Long userId;

    /**
     * 用户名
     */
    @Schema(description = "用户名", example = "nick123", minLength = 4)
    private String username;
    /**
     * 昵称
     */
    @Schema(description = "用户昵称", example = "尼克", minLength = 6)
    private String nickname;

    /**
     * 用户性别 男  女
     */
    @Schema(description = "用户性别", example = "男")
    private String gender;

    /**
     * 用户年龄
     */
    @Schema(description = "用户年龄", example = "20", minimum = "0", maximum = "200")
    private Integer age;

    /**
     * 用户头像url
     */
    @Schema(description = "用户头像url", example = "https://ischool-bucket.oss-cn-beijing.aliyuncs.com/4c079b7b-0873-4c99-a666-0874a1595811.jpg")
    private String userAvatar;

    /**
     * 用户邮箱
     */
    @Schema(description = "用户邮箱", example = "han892577@qq.com")
    private String email;

    /**
     * 用户所属学校名称
     */
    @Schema(description = "用户所属学校名称", example = "哈尔滨理工大学")
    private String schoolName;

    /**
     * 用户所属学校简写
     */
    @Schema(description = "用户所属学校简写", example = "HRBUST")
    private String schoolAbbr;


    /**
     * 用户获赞总数
     */
    @Schema(description = "用户在社区模块的获赞总数", example = "100")
    private Integer totalLikes;

    /**
     * 用户被评论总数
     */
    @Schema(description = "用户在社区模块的被评论总数", example = "200")
    private Integer totalComments;

    /**
     * 用户被评论总数
     */
    @Schema(description = "用户未读评论的总数", example = "10")
    private Integer unReadCommentsCount;

    private static final long serialVersionUID = 1L;
}