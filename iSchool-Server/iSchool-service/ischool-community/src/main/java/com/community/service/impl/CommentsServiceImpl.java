package com.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.client.service.UserFeignClient;
import com.common.dto.UserDto;
import com.community.mapper.CommentObjMapper;
import com.community.mapper.CommentsMapper;
import com.community.mapper.ReplyCommentsMapper;
import com.community.model.dto.AddCommentRequest;
import com.community.model.entity.CommentObj;
import com.community.model.entity.Comments;
import com.community.model.entity.ReplyComments;
import com.community.model.vo.CommentsVO;
import com.community.service.CommentsService;
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
    public List<CommentsVO> getList(Long objId) {
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
            String userAvatar = loginUser.getUserAvatar();
            String nickname = loginUser.getNickname();
            commentsVO.setUserAvatar(userAvatar);
            commentsVO.setUsername(nickname);
            // 2: 填充对应的回复评论数
            Long count = replyCommentsMapper.selectCount(new LambdaQueryWrapper<ReplyComments>()
                    .eq(ReplyComments::getReplyCommentId, comment.getId()));

            commentsVO.setReplyCount(count);
            // 3:加入列表
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
    public void addCommentLikes(Long commentId) {
        // 1: 校验参数
        Comments comments = this.baseMapper.selectById(commentId);
        if (comments == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "点赞评论不存在");
        }

        // 2: 修改数据库
        comments.setLikes(comments.getLikes() + 1);
        this.baseMapper.updateById(comments);


        // todo:发送消息给消息队列实时计算搜索词热度

    }
}




