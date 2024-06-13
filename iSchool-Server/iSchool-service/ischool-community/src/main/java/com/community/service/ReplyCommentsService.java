package com.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.dto.MessageDto;
import com.common.dto.SocialDataDto;
import com.community.model.dto.AddReplyCommentRequest;
import com.community.model.entity.ReplyComments;
import com.community.model.vo.ReplyCommentsVO;

import java.util.List;

/**
 * @author AlbertZhang
 * @description 针对表【reply_comments】的数据库操作Service
 * @createDate 2024-05-03 22:25:06
 */
public interface ReplyCommentsService extends IService<ReplyComments> {

    /**
     * @param addReplyCommentRequest
     * @param id
     * @return void
     * @description 添加二级评论id
     **/
    void add(AddReplyCommentRequest addReplyCommentRequest, Long id);

    /**
     * @param replyCommentId
     * @return java.util.List<com.community.model.vo.ReplyCommentsVO>
     * @description 获取某一一级评论下的所有二级评论
     **/
    List<ReplyCommentsVO> getList(Long userId, Long replyCommentId);

    /**
     * @param commentId
     * @return void
     * @description 给二级评论点赞
     **/
    void addCommentLikes(Long userId, Long commentId);

    /**
     * @param commentId
     * @return void
     * @description 用户取消点赞
     **/
    void decreaseCommentLikes(Long userId, Long commentId);

    /**
     * @param id
     * @return java.util.List<com.common.dto.MessageDto>
     * @description 获取用户未读消息列表
     **/
    List<MessageDto> getUnreadMessageList(Long id);

    /**
     * @param id
     * @param messageId
     * @return void
     * @description 标记已读消息
     **/
    Boolean readMessage(Long id, Long messageId);

    /**
     * @param id
     * @return com.common.dto.SocialDataDto
     * @description 获取二级评论的所有点赞和评论
     **/
    SocialDataDto getSocialData(Long id);
}
