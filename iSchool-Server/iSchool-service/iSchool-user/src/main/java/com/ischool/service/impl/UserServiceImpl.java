package com.ischool.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.client.service.CommunityFeignClient;
import com.common.dto.MessageDto;
import com.common.dto.SocialDataDto;
import com.common.dto.UserDto;
import com.ischool.exception.BusinessException;
import com.ischool.mapper.UserMapper;
import com.ischool.model.ErrorCode;
import com.ischool.model.dto.LoginDto;
import com.ischool.model.dto.UpdateUserDto;
import com.ischool.model.entity.User;
import com.ischool.model.enums.UserRoleEnum;
import com.ischool.model.pojo.JwtProperties;
import com.ischool.service.UserService;
import com.ischool.utils.JwtUtil;
import com.ischool.utils.RandomNicknameSuffix;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Ljx
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2024-05-02 13:46:01
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    JwtProperties jwtProperties;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    CommunityFeignClient communityFeignClient;

    public static final String SALT = "common";

    /**
     * @param loginDto
     * @return com.intellilh.model.entity.UserDto
     * @description 用户登录
     **/
    @Override
    public String login(LoginDto loginDto) {
        // 1: 校验参数
        if (loginDto == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String username = loginDto.getUsername();
        String password = loginDto.getPassword();
        if (StringUtils.isAnyBlank(username, password)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 校验是否合法
        if (username.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名不符合规范");
        }
        if (password.length() < 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码不符合规范");
        }
        // 加密密码
        password = DigestUtils.md5DigestAsHex((SALT + password).getBytes());


        // 2：查询是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>()
                .eq("username", username)
                .eq("password", password);
        User user = this.baseMapper.selectOne(queryWrapper);

        if (user == null) {     // 用户名或密码有误
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名或密码错误");
        }

        // 3：拼装数据并返回
        // 3.1：生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        String role = UserRoleEnum.USER.getText();  // 默认角色为用户
        UserRoleEnum roleEnum = UserRoleEnum.getEnumByValue(user.getRole());
        if (roleEnum != null) {
            role = roleEnum.getText();
        }
        String school = user.getSchool();

        claims.put("userRole", role); // 标识用户或管理员
        claims.put("school", school);
        return JwtUtil.createJWT(
                jwtProperties.getSecretKey(),
                jwtProperties.getTtl(),
                claims);
    }

    /**
     * @param loginDto
     * @return void
     * @description 用户注册
     **/
    @Override
    public void register(LoginDto loginDto) {
        // 1: 校验参数
        if (loginDto == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String username = loginDto.getUsername();
        String password = loginDto.getPassword();
        if (StringUtils.isAnyBlank(username, password)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 校验是否合法
        if (username.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名不符合规范");
        }
        if (password.length() < 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码不符合规范");
        }

        // 2:查询用户名是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().eq("username", username);
        Long count = this.baseMapper.selectCount(queryWrapper);
        if (count != 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名已存在");
        }

        // 3: 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());

        // 4：插入数据库
        User user = new User();

        user.setUsername(username);
        user.setPassword(encryptPassword);
        // 生成随机用户名后缀
        String suffix = RandomNicknameSuffix.generateUsernameSuffix(5);
        user.setNickname("user" + suffix);
        this.save(user);
    }

    /**
     * @param updateUserDto
     * @param id
     * @param role
     * @return void
     * @description 更新用户信息
     **/
    @Override
    public void updateUserInfo(UpdateUserDto updateUserDto, Long id, String role) {

        // 1: 校验参数
        if (id <= 0 || !Objects.equals(role, UserRoleEnum.USER.getText())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String password = updateUserDto.getPassword();
        Integer age = updateUserDto.getAge();
        if (password != null && password.length() < 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度不能小于6");
        }
        if (age != null && (age <= 0 || age > 200)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "年龄不合法");
        }

        // 2:组装用户信息
        User user = new User();
        user.setUserId(id);

        // 2.1：查询出之前的用户信息
        User oldUser = getById(id);
        if (oldUser == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }

        // 2.2：拼接用户信息
        BeanUtils.copyProperties(updateUserDto, user);
        user.setUserId(id);
        user.setUsername(oldUser.getUsername());

        // 3: 加密
        if (StringUtils.isNotBlank(password)) {
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
            user.setPassword(encryptPassword);
        }

        // 3: 更新用户信息
        updateById(user);
    }

    /**
     * @param id
     * @return com.intellilh.model.entity.UserDto
     * @description 获取登录用户个人信息
     **/
    @Override
    public UserDto getLoginUser(Long id) {

        // 1：校验参数
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }

        User oldUser = getById(id);
        if (oldUser == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }

        // 2：获取总点赞和总回复
        SocialDataDto socialData = communityFeignClient.getSocialData(id);
        Integer totalLikes = socialData.getTotalLikes();
        Integer totalComments = socialData.getTotalComments();

        // 4: 获取未读信息个数
        List<MessageDto> unreadMessageList = communityFeignClient.getUnreadMessageList(id);
        int count = unreadMessageList.size();

        // 5：拼装数据
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(oldUser, userDto);

        userDto.setTotalLikes(totalLikes);
        userDto.setTotalComments(totalComments);
        userDto.setUnReadCommentsCount(count);
        return userDto;
    }

    /**
     * @param id
     * @return java.lang.Boolean
     * @description 检查用户id是否存在
     **/
    @Override
    public Boolean checkId(Long id) {
        User user = this.baseMapper.selectById(id);
        return user != null;
    }

    /**
     * @param id
     * @return com.common.dto.UserDto
     * @description 获取用户信息
     **/
    @Override
    public UserDto getUser(Long id) {
        // 1：校验参数
        if (id <= 0) {
            return null;
        }

        User oldUser = getById(id);
        if (oldUser == null) {
            return null;
        }


        // 5：拼装数据
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(oldUser, userDto);
        return userDto;
    }
}




