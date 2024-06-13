package com.ischool.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-04-21 12:57
 * @description: 更新用户信息
 **/
@Data
@Schema(description = "更新用户信息dto")
public class UpdateUserDto implements Serializable {


    /**
     * 昵称
     */
    @Schema(description = "用户昵称", example = "尼克")
    private String nickname;
    /**
     * 密码，加密存储
     */
    @Schema(description = "用户密码(不传为空)", example = "123456")
    private String password;

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
    @Schema(description = "用户头像的url", example = "https://ischool-bucket.oss-cn-beijing.aliyuncs.com/4c079b7b-0873-4c99-a666-0874a1595811.jpg")
    private String userAvatar;

    /**
     * 用户邮箱
     */
    @Schema(description = "用户邮箱", example = "han892577@qq.com")
    private String email;
    /**
     * 用户学校
     */
    @Schema(description = "用户学校(英文简写而非学校名称)", example = "HRBUST")
    private String schoolAbbr;

    private static final long serialVersionUID = 1L;
}
