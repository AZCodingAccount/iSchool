package com.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.client.service.UserFeignClient;
import com.common.dto.UserDto;
import com.community.mapper.ReplyCommentsMapper;
import com.community.model.dto.AddReplyCommentRequest;
import com.community.model.entity.Comments;
import com.community.model.entity.ReplyComments;
import com.community.model.vo.ReplyCommentsVO;
import com.community.service.CommentsService;
import com.community.service.ReplyCommentsService;
import com.ischool.exception.BusinessException;
import com.ischool.model.BaseResponse;
import com.ischool.model.ErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Albert han
 * @description 针对表【reply_comments】的数据库操作Service实现
 * @createDate 2024-05-03 22:25:06
 */
@Service
public class ReplyCommentsServiceImpl extends ServiceImpl<ReplyCommentsMapper, ReplyComments>
        implements ReplyCommentsService {

    @Autowired
    CommentsService commentsService;

    @Autowired
    UserFeignClient userFeignClient;

    /**
     * @param addReplyCommentRequest
     * @param id
     * @return void
     * @description 添加二级评论id
     **/
    @Override
    public void add(AddReplyCommentRequest addReplyCommentRequest, Long id) {
        // 1：校验参数
        Long commentId = addReplyCommentRequest.getCommentId();
        String replyContent = addReplyCommentRequest.getReplyContent();
        Long replyUserId = addReplyCommentRequest.getReplyUserId();
        if (commentId == null || replyUserId == null || StringUtils.isBlank(replyContent)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 2: 添加评论
        ReplyComments replyComments = new ReplyComments();
        replyComments.setUserId(id);
        replyComments.setReplyCommentId(replyUserId);
        replyComments.setContent(replyContent);
        replyComments.setReplyCommentId(commentId);
        this.baseMapper.insert(replyComments);

        // todo:发送消息给消息队列更新实时热度
    }

    /**
     * @param replyCommentId
     * @return java.util.List<com.community.model.vo.ReplyCommentsVO>
     * @description 获取某一一级评论下的所有二级评论
     **/
    @Override
    public List<ReplyCommentsVO> getList(Long replyCommentId) {

        // 1：参数校验
        if (replyCommentId == null || replyCommentId < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 1.1：查询当前回复的评论是否存在
        Comments comments = commentsService.getById(replyCommentId);
        if (comments == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "要回复的评论不存在");
        }

        // 2: 查询数据库
        LambdaQueryWrapper<ReplyComments> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ReplyComments::getReplyCommentId, replyCommentId);
        // 降序排列
        queryWrapper.orderByDesc(ReplyComments::getPubTime);
        List<ReplyComments> replyCommentsList = this.baseMapper.selectList(queryWrapper);
        if (replyCommentsList.isEmpty()) {
            return new ArrayList<>();
        }
        // 3: 转换成VO
        List<ReplyCommentsVO> replyCommentsVOList = new ArrayList<>();
        for (ReplyComments replyComments : replyCommentsList) {
            ReplyCommentsVO replyCommentsVO = new ReplyCommentsVO();
            BeanUtils.copyProperties(replyComments, replyCommentsVO);
            // 3.1: 填充用户头像和评论用户名
            Long userId = replyComments.getUserId();
            BaseResponse<UserDto> res = userFeignClient.getLoginUser(userId);
            UserDto userDto = res.getData();
            String userAvatar = "";
            String username = "";
            if (userDto != null) {
                userAvatar = userDto.getUserAvatar();
                username = userDto.getNickname();
            } else {  // 用户已注销
                userAvatar = "";
                username = "用户已注销";
            }
            replyCommentsVO.setUserAvatar(userAvatar);
            replyCommentsVO.setUsername(username);

            // 3.2: 填充回复的用户名
            // (根据回复的评论id查到评论——>根据评论查到用户id——>根据用户id查到用户信息——>得到用户名)
            Comments replyComment = commentsService.getBaseMapper().selectOne(new LambdaQueryWrapper<Comments>().
                    eq(Comments::getId, replyCommentId));
            Long replyCommentUserId = replyComment.getUserId();
            UserDto replyUserInfo = userFeignClient.getLoginUser(replyCommentUserId).getData();
            String replyUsername = "";
            if (replyUserInfo == null) {
                replyUsername = "用户已注销";
            } else {
                replyUsername = replyUserInfo.getUsername();
            }
            replyCommentsVO.setReplyUsername(replyUsername);
            replyCommentsVOList.add(replyCommentsVO);
        }
        return replyCommentsVOList;
    }

    /**
     * @param commentId
     * @return void
     * @description 给二级评论点赞
     **/
    @Override
    public void addCommentLikes(Long commentId) {
        // 1: 校验参数
        ReplyComments comments = this.baseMapper.selectById(commentId);
        if (comments == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "点赞评论不存在");
        }

        // 2: 修改数据库
        comments.setLikes(comments.getLikes() + 1);
        this.baseMapper.updateById(comments);

        // todo:发送消息给消息队列实时计算搜索词热度

    }
}




