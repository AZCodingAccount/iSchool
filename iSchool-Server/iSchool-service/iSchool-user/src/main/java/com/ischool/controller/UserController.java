package com.ischool.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.client.service.CommunityFeignClient;
import com.common.dto.MessageDto;
import com.common.dto.UserDto;


import com.ischool.exception.BusinessException;
import com.ischool.model.BaseResponse;
import com.ischool.model.ErrorCode;
import com.ischool.model.Result;
import com.ischool.model.dto.LoginDto;
import com.ischool.model.dto.UpdateUserDto;
import com.ischool.model.entity.User;
import com.ischool.model.enums.UserRoleEnum;
import com.ischool.service.UserService;
import com.ischool.utils.AliOssUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;


/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-04-20 20:10
 * @description: 用户控制器
 **/
@RestController
@RequestMapping
@Slf4j
@Tag(name = "用户管理", description = "个人中心相关操作")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AliOssUtil aliOssUtil;

    @Autowired
    CommunityFeignClient communityFeignClient;

    /**
     * @param loginDto
     * @return com.common.model.BaseResponse<com.common.dto.UserDto>
     * @description 用户登录
     **/
    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public BaseResponse<String> login(@RequestBody LoginDto loginDto) {
        log.info("用户登录，当前登录请求信息为{}", loginDto);
        // 查找登录用户信息
        String jwt = userService.login(loginDto);
        return Result.success(jwt);
    }


    /**
     * @param id
     * @return com.common.model.BaseResponse<com.common.dto.UserDto>
     * @description 获取用户登录信息
     **/
    @GetMapping
    @Operation(summary = "获取用户登录信息", description = "常常用于第一次登陆成功或路由守卫检查用户登录态")
    public BaseResponse<UserDto> getLoginUser(@Parameter(hidden = true) @RequestHeader("id") Long id) {
        log.info("获取id为{}的用户登录信息", id);
        UserDto userDto = userService.getLoginUser(id);
        return Result.success(userDto);
    }


    /**
     * @param loginDto
     * @return com.common.model.BaseResponse<java.lang.Object>
     * @description 用户注册
     **/
    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public BaseResponse<Object> register(@RequestBody LoginDto loginDto) {
        log.info("用户注册，注册信息为：{}", loginDto);
        userService.register(loginDto);
        return Result.success();
    }

    /**
     * @param updateUserDto
     * @return com.common.model.BaseResponse<java.lang.Object>
     * @description 修改用户信息
     **/
    @PutMapping
    @Operation(summary = "修改用户信息")
    public BaseResponse<Object> updateUserInfo(@RequestBody UpdateUserDto updateUserDto,
                                               @Parameter(hidden = true) @RequestHeader("id") Long id,
                                               @Parameter(hidden = true) @RequestHeader("role") String role, HttpServletRequest request) {
        log.info("用户{}，角色为{}，修改信息，要修改的信息为：{}", id, role, updateUserDto);
        userService.updateUserInfo(updateUserDto, id, role);
        return Result.success();
    }

    /**
     * @param id
     * @param role
     * @return com.common.model.BaseResponse<java.lang.Object>
     * @description 普通用户注销
     **/
    @DeleteMapping
    @Operation(summary = "用户注销")
    public BaseResponse<Object> deleteUser(@Parameter(hidden = true) @RequestHeader("id") Long id,
                                           @Parameter(hidden = true) @RequestHeader("role") String role) {
        log.info("用户{}，角色为{}，删除用户", id, role);

        if (!role.equals(UserRoleEnum.USER.getText())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不允许注销");
        }
        LambdaQueryWrapper<User> deleteWrapper = new LambdaQueryWrapper<User>().eq(User::getUserId, id);
        userService.remove(deleteWrapper);
        // todo: 删除comments、obj、task这三张表的内容
        log.info("用户删除成功，id为{}", id);
        return Result.success();
    }


    /**
     * @param file
     * @param id
     * @param role
     * @return com.common.model.BaseResponse<java.lang.String>
     * @description 文件上传
     **/
    @PostMapping("/upload")
    @Operation(summary = "文件上传")
    public BaseResponse<String> upload(@Parameter(description = "文件对象") MultipartFile file,
                                       @Parameter(hidden = true) @RequestHeader("id") Long id,
                                       @Parameter(hidden = true) @RequestHeader("role") String role) {
        log.info("文件上传，{}", file);
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = null;
            if (originalFilename != null) {
                extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            }
            String objectName = UUID.randomUUID() + extension;
            String filePath = aliOssUtil.upload(file.getBytes(), objectName, originalFilename);
            // 将照片url更新到数据库
            UpdateUserDto userDto = new UpdateUserDto();
            userDto.setUserAvatar(filePath);
            userService.updateUserInfo(userDto, id, role);
            return Result.success(filePath);
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "文件上传失败，请稍后重试！");
        }
    }


    /**
     * @param id
     * @return java.lang.Boolean
     * @description 检查用户id是否合法
     **/
    @GetMapping("/id")
    @Operation(summary = "检查用户id是否合法", description = "供后端系统远程调用，前端不需关注")
    public Boolean checkId(@RequestParam("id") Long id) {
        log.info("检查id{}是否合法，远程调用", id);
        return userService.checkId(id);
    }


    /**
     * @param id
     * @return com.common.model.BaseResponse<com.common.dto.UserDto>
     * @description 获取用户信息——》远程调用
     **/
    @GetMapping("/rpc")
    @Operation(summary = "获取用户信息", description = "供后端系统远程调用，前端不需关注")
    public BaseResponse<UserDto> getUser(@RequestHeader("id") Long id) {
        log.info("获取id为{}的用户登录信息，远程调用", id);
        UserDto userDto = userService.getUser(id);
        return Result.success(userDto);
    }


    /**
     * @param
     * @return com.ischool.model.Result<List < MessageDto>>
     * @description 获取所有未读的信息
     **/
    @GetMapping("/messages")
    @Operation(summary = "获取用户所有未读信息")
    public BaseResponse<List<MessageDto>> getMessageList(@Parameter(hidden = true) @RequestHeader("id") Long id) {
        log.info("获取用户{}所有未读信息", id);
        if (!checkId(id)) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }
        List<MessageDto> unreadMessageList = communityFeignClient.getUnreadMessageList(id);
        return Result.success(unreadMessageList);
    }

    /**
     * @param messageId
     * @return com.ischool.model.Result<java.lang.Boolean>
     * @description 当前消息已读
     **/
    @PutMapping("/read/messages")
    @Operation(summary = "将消息标记为已读", description = "点击去查看以后发送的请求")
    public BaseResponse<Object> readMessage(@Parameter(description = "要标记的消息id", example = "1789548655582642177") @RequestParam("messageId") Long messageId,
                                            @Parameter(hidden = true) @RequestHeader("id") Long id) {
        log.info("用户{}将消息{}标记为已读", id, messageId);
        Boolean b = communityFeignClient.readMessage(id, messageId);
        if (!b) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "标记消息为已读失败");
        }
        return Result.success();
    }


}
