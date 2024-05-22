package com.ischool.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "用户登录DTO", requiredProperties = {"username", "password"})
public class LoginDto {
    @Schema(description = "用户名", minLength = 4)
    private String username;
    @Schema(description = "用户密码", maxLength = 6)
    private String password;
}
