package com.ischool.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-04-21 12:57
 * @description: 更新用户信息
 **/
@TableName(value = "user")
@Data
public class UpdateUserDto implements Serializable {

    /**
     * 昵称
     */
    private String nickname;
    /**
     * 密码，加密存储
     */
    private String password;

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
     * 用户学校
     */
    private String school;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
