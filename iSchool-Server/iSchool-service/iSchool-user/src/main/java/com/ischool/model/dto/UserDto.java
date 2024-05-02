package com.ischool.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 返回给前端的数据
 */
@TableName(value = "user")
@Data
public class UserDto implements Serializable {
    /**
     * 用户id
     */
    @TableId
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




    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}