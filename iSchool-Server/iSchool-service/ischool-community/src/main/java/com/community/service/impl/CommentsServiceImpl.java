package com.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.client.service.UserFeignClient;
import com.common.dto.SocialDataDto;
import com.common.dto.UserDto;
import com.community.mapper.CommentObjMapper;
import com.community.mapper.CommentsMapper;
import com.community.mapper.ReplyCommentsMapper;
import com.community.mapper.UserCommentLikesMapper;
import com.community.model.dto.AddCommentRequest;
import com.community.model.entity.CommentObj;
import com.community.model.entity.Comments;
import com.community.model.entity.ReplyComments;
import com.community.model.entity.UserCommentLikes;
import com.community.model.vo.CommentsVO;
import com.community.service.CommentsService;
import com.ischool.exception.BusinessException;
import com.ischool.model.BaseResponse;
import com.ischool.model.ErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author AlbertZhang
 * @description 针对表【comments】的数据库操作Service实现
 * @createDate 2024-05-03 22:24:26
 */
@Service
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comments>
        implements CommentsService {
    @Autowired
    CommentObjMapper commentObjMapper;

    @Autowired
    UserFeignClient userFeignClient;

    @Autowired
    ReplyCommentsMapper replyCommentsMapper;

    @Autowired
    UserCommentLikesMapper userCommentLikesMapper;

    /**
     * @param addCommentRequest
     * @return void
     * @description 发布一级评论
     **/
    @Override
    public void add(AddCommentRequest addCommentRequest, Long id) {
        // 1：参数校验
        Long objId = addCommentRequest.getObjId();
        String content = addCommentRequest.getContent();

        if (id <= 0 || objId <= 0 || StringUtils.isBlank(content)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 校验id或者objId是否存在
        CommentObj commentObj = commentObjMapper.selectById(objId);
        // todo:rpc调用查询user表
        if (commentObj == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 2：插入评论数据
        Comments comments = new Comments();
        comments.setUserId(id);
        comments.setObjId(objId);
        comments.setContent(content);
        this.baseMapper.insert(comments);

        // todo:发送消息给消息队列实时计算搜索词热度
    }

    /**
     * @param
     * @return java.util.List<com.community.model.vo.CommentsVO>
     * @description 获取某个点评对象下的一级评论相关信息
     **/
    @Override
    public List<CommentsVO> getList(Long objId, Long requestUserId) {
        // 1:校验参数
        if (objId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        CommentObj commentObj = commentObjMapper.selectById(objId);
        if (commentObj == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数不合法");
        }

        // 2: 查询数据
        List<Comments> comments = this.baseMapper.selectList(new LambdaQueryWrapper<Comments>()
                .eq(Comments::getObjId, objId));
        List<CommentsVO> commentsVOList = new ArrayList<>();
        for (Comments comment : comments) {
            CommentsVO commentsVO = new CommentsVO();
            BeanUtils.copyProperties(comment, commentsVO);
            // 1：填充评论对应的头像url和名称
            Long userId = comment.getUserId();
            BaseResponse<UserDto> res = userFeignClient.getLoginUser(userId);
            UserDto loginUser = res.getData();
            String userAvatar = "";
            String nickname = "";
            if (loginUser != null) {
                userAvatar = loginUser.getUserAvatar();
                nickname = loginUser.getNickname();
            } else {
                userAvatar = "";
                nickname = "用户已注销";
                commentsVO.setUserId(0L);   // 用户id置为0
            }
            commentsVO.setUserAvatar(userAvatar);
            commentsVO.setUsername(nickname);

            // 2: 填充对应的回复评论数
            Long count = replyCommentsMapper.selectCount(new LambdaQueryWrapper<ReplyComments>()
                    .eq(ReplyComments::getReplyCommentId, comment.getId()));

            commentsVO.setReplyCount(count);

            // 3: 加入用户是否点赞
            Long commentId = comment.getId();
            UserCommentLikes userCommentLikes = userCommentLikesMapper.selectOne(new LambdaQueryWrapper<UserCommentLikes>()
                    .eq(UserCommentLikes::getUserId, requestUserId)
                    .eq(UserCommentLikes::getCommentId, commentId));
            Boolean liked = Boolean.FALSE;
            if (userCommentLikes != null) {
                liked = Boolean.TRUE;
            }
            commentsVO.setLiked(liked);

            // 4:加入列表
            commentsVOList.add(commentsVO);
        }
        return commentsVOList;
    }

    /**
     * @param commentId
     * @return void
     * @description 给一级评论点赞
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCommentLikes(Long userId, Long commentId) {
        // 1: 校验参数
        Comments comments = this.baseMapper.selectById(commentId);
        if (comments == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "点赞评论不存在");
        }

        // 2: 修改点赞数
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
     * @description 取消一级评论点赞
     **/
    @Override
    public void decreaseCommentLikes(Long userId, Long commentId) {
        // 1: 校验参数
        Comments comments = this.baseMapper.selectById(commentId);
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

    }

    /**
     * @param id
     * @return com.common.dto.SocialDataDto
     * @description 获取一级评论的点赞和评论
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
        LambdaQueryWrapper<Comments> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comments::getUserId, id);
        List<Comments> commentsList = this.baseMapper.selectList(queryWrapper);

        // 获取评论数量
        int likesNum = 0;
        // 获取点赞数量
        for (Comments comments : commentsList) {
            likesNum += comments.getLikes();
        }

        socialDataDto.setTotalLikes(likesNum);

        return socialDataDto;
    }
}




