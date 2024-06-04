package com.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.client.service.UserFeignClient;
import com.common.dto.MessageDto;
import com.common.dto.SocialDataDto;
import com.common.dto.UserDto;
import com.community.mapper.ReplyCommentsMapper;
import com.community.mapper.UserCommentLikesMapper;
import com.community.model.dto.AddReplyCommentRequest;
import com.community.model.entity.CommentObj;
import com.community.model.entity.Comments;
import com.community.model.entity.ReplyComments;
import com.community.model.entity.UserCommentLikes;
import com.community.model.vo.ReplyCommentsVO;
import com.community.service.CommentObjService;
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
import java.util.stream.Collectors;

/**
 * @author Ljx
 * @description 针对表【reply_comments】的数据库操作Service实现
 * @createDate 2024-05-03 22:25:06
 */
@Service
public class ReplyCommentsServiceImpl extends ServiceImpl<ReplyCommentsMapper, ReplyComments>
        implements ReplyCommentsService {

    @Autowired
    CommentObjService commentObjService;

    @Autowired
    CommentsService commentsService;

    @Autowired
    UserFeignClient userFeignClient;

    @Autowired
    UserCommentLikesMapper userCommentLikesMapper;

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
        Long objId = addReplyCommentRequest.getObjId();
        Long parentCommentId = addReplyCommentRequest.getParentCommentId();
        if (parentCommentId == null || commentId == null || replyUserId == null || objId == null || StringUtils.isBlank(replyContent)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 2: 添加评论
        ReplyComments replyComments = new ReplyComments();
        replyComments.setObjId(objId);
        replyComments.setUserId(id);
        replyComments.setReplyUserId(replyUserId);
        replyComments.setContent(replyContent);
        replyComments.setReplyCommentId(commentId);
        replyComments.setParentCommentId(parentCommentId);
        this.baseMapper.insert(replyComments);

        // todo:发送消息给消息队列更新实时热度
    }

    /**
     * @param replyCommentId
     * @return java.util.List<com.community.model.vo.ReplyCommentsVO>
     * @description 获取某一一级评论下的所有二级评论
     **/
    @Override
    public List<ReplyCommentsVO> getList(Long userId, Long replyCommentId) {

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
        queryWrapper.eq(ReplyComments::getParentCommentId, replyCommentId);
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
            Long commentsUserId = replyComments.getUserId();
            BaseResponse<UserDto> res = userFeignClient.getLoginUser(commentsUserId);
            UserDto userDto = res.getData();
            String userAvatar = "";
            String username = "";
            if (userDto != null) {
                userAvatar = userDto.getUserAvatar();
                username = userDto.getNickname();
            } else {  // 用户已注销
                userAvatar = "";
                username = "用户已注销";
                replyCommentsVO.setUserId(0L);
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
            String replyUserAvatar = "";
            if (replyUserInfo == null) {
                replyUsername = "用户已注销";
            } else {
                replyUsername = replyUserInfo.getNickname();
                replyUserAvatar = replyUserInfo.getUserAvatar();
                replyCommentsVO.setReplyUserId(0L);
            }
            replyCommentsVO.setReplyUsername(replyUsername);
            replyCommentsVO.setReplyUserAvatar(replyUserAvatar);

            // 3.3: 填充用户是否点赞
            Long commentId = replyComments.getId();
            UserCommentLikes userCommentLikes = userCommentLikesMapper.selectOne(new LambdaQueryWrapper<UserCommentLikes>()
                    .eq(UserCommentLikes::getUserId, userId)
                    .eq(UserCommentLikes::getCommentId, commentId));
            Boolean liked = Boolean.FALSE;
            if (userCommentLikes != null) {
                liked = Boolean.TRUE;
            }
            replyCommentsVO.setLiked(liked);
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
    public void addCommentLikes(Long userId, Long commentId) {
        // 1: 校验参数
        ReplyComments comments = this.baseMapper.selectById(commentId);
        if (comments == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "点赞评论不存在");
        }

        // 2: 修改数据库
        comments.setLikes(comments.getLikes() + 1);
        this.baseMapper.updateById(comments);

        // 3: 添加点赞记录
        UserCommentLikes userCommentLikes = new UserCommentLikes();
        userCommentLikes.setUserId(userId);
        userCommentLikes.setCommentId(commentId);

        userCommentLikesMapper.insert(userCommentLikes);

        // todo:发送消息给消息队列实时计算搜索词热度

    }

    /**
     * @param commentId
     * @return void
     * @description 取消二级评论的点赞
     **/
    @Override
    public void decreaseCommentLikes(Long userId, Long commentId) {
        // 1: 校验参数
        ReplyComments comments = this.baseMapper.selectById(commentId);
        if (comments == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "点赞评论不存在");
        }

        // 2: 修改数据库
        comments.setLikes(comments.getLikes() - 1);
        this.baseMapper.updateById(comments);

        // 3: 删除点赞记录
        UserCommentLikes userCommentLikes = new UserCommentLikes();
        userCommentLikes.setUserId(userId);
        userCommentLikes.setCommentId(commentId);

        userCommentLikesMapper.delete(new LambdaQueryWrapper<UserCommentLikes>()
                .eq(UserCommentLikes::getUserId, userId)
                .eq(UserCommentLikes::getCommentId, commentId));

        // todo:发送消息给消息队列实时计算搜索词热度
    }

    /**
     * @param id
     * @return java.util.List<com.common.dto.MessageDto>
     * @description 获取用户未读消息列表
     **/
    @Override
    public List<MessageDto> getUnreadMessageList(Long id) {
        // 1:校验参数
        Boolean checked = userFeignClient.checkId(id);
        if (!checked) {
            return new ArrayList<>();
        }

        // 2：查询数据
        LambdaQueryWrapper<ReplyComments> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ReplyComments::getReplyUserId, id)
                .eq(ReplyComments::getReaded, 0);
        List<ReplyComments> replyComments = this.baseMapper.selectList(queryWrapper);

        List<MessageDto> messageDtoList = replyComments.stream().map(item -> {
            MessageDto messageDto = new MessageDto();
            BeanUtils.copyProperties(item, messageDto);
            // 获取userNickname
            UserDto userDto = userFeignClient.getLoginUser(item.getUserId()).getData();
            String nickname = userDto.getNickname();
            // 获取当前评论对象
            CommentObj commentObj = commentObjService.getBaseMapper().selectById(messageDto.getObjId());
            String objName = commentObj.getName();
            messageDto.setObjName(objName);
            messageDto.setUserNickname(nickname);
            return messageDto;
        }).toList();

        // 3：返回结果
        return messageDtoList;
    }

    /**
     * @param id
     * @param messageId
     * @return void
     * @description 标记已读消息
     **/
    @Override
    public Boolean readMessage(Long id, Long messageId) {
        // 校验参数
        if (id == null || messageId == null) {
            return false;
        }
        // 校验用户或者消息是否存在
        Boolean checked = userFeignClient.checkId(id);
        ReplyComments replyComments = this.baseMapper.selectById(messageId);
        if (!checked || replyComments == null) {
            return false;
        }

        // 2: 操作数据库
        ReplyComments comments = new ReplyComments();
        comments.setReplyUserId(id);
        comments.setId(messageId);
        comments.setReaded(1);
        int i = this.baseMapper.updateById(comments);
        return i >= 1;
    }

    /**
     * @param id
     * @return com.common.dto.SocialDataDto
     * @description 获取二级评论的点赞和评论
     **/
    @Override
    public SocialDataDto getSocialData(Long id) {
        // 1: 校验参数
        Boolean checked = userFeignClient.checkId(id);

        if (checked == null || !checked) {
            return null;
        }

        // 获取数据
        SocialDataDto socialDataDto = new SocialDataDto();
        LambdaQueryWrapper<ReplyComments> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ReplyComments::getReplyUserId, id);
        List<ReplyComments> commentsList = this.baseMapper.selectList(queryWrapper);

        // 获取评论数量
        int commentsNum = commentsList.size();
        // 获取点赞数量
        LambdaQueryWrapper<ReplyComments> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(ReplyComments::getUserId, id);
        List<ReplyComments> commentsList2 = this.baseMapper.selectList(queryWrapper2);

        int likesNum = 0;
        // 获取点赞数量
        for (ReplyComments comments : commentsList2) {
            likesNum += comments.getLikes();
        }

        socialDataDto.setTotalComments(commentsNum);
        socialDataDto.setTotalLikes(likesNum);

        return socialDataDto;

    }
}




