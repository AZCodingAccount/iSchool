package com.ischool.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-04-20 20:42
 * @description: 登录实体类
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    private String username;
    private String password;
}
