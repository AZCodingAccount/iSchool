package com.ischool.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 
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
     * 用户角色，1学生、2管理员
     */
    private Integer role;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}