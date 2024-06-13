package com.community.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.community.mapper.UserCommentLikesMapper;
import com.community.model.entity.UserCommentLikes;
import com.community.service.UserCommentLikesService;
import org.springframework.stereotype.Service;

/**
* @author AlbertZhang
* @description 针对表【user_comment_likes】的数据库操作Service实现
* @createDate 2024-05-21 10:54:20
*/
@Service
public class UserCommentLikesServiceImpl extends ServiceImpl<UserCommentLikesMapper, UserCommentLikes>
    implements UserCommentLikesService {

}




