package com.ischool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.dto.UserDto;
import com.ischool.model.dto.LoginDto;
import com.ischool.model.dto.UpdateUserDto;
import com.ischool.model.entity.User;


/**
* @author Albert han
* @description 针对表【user】的数据库操作Service
* @createDate 2024-05-02 13:46:01
*/
public interface UserService extends IService<User> {

    /**
     * @param loginDto
     * @return com.intellilh.model.entity.UserDto
     * @description 用户登录
     * @date 2024-04-20 21:43
     **/
    String login(LoginDto loginDto);

    /**
     * @param loginDto
     * @return void
     * @description 用户注册
     **/
    void register(LoginDto loginDto);

    /**
     * @param updateUserDto
     * @param id
     * @param role
     * @return void
     * @description 更新用户信息
     **/
    void updateUserInfo(UpdateUserDto updateUserDto, Long id, String role);

    /**
     * @param id
     * @return com.intellilh.model.entity.UserDto
     * @description 获取已登录用户个人信息
     **/
    UserDto getLoginUser(Long id);

}
