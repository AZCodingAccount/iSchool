package com.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.dto.MessageDto;
import com.common.dto.SocialDataDto;
import com.community.model.dto.AddReplyCommentRequest;
import com.community.model.entity.ReplyComments;
import com.community.model.vo.ReplyCommentsVO;

import java.util.List;

/**
* @author Albert han
* @description 针对表【reply_comments】的数据库操作Service
* @createDate 2024-05-03 22:25:06
*/
public interface ReplyCommentsService extends IService<ReplyComments> {

    /**
     * @description 添加二级评论id
     * @param addReplyCommentRequest
     * @param id
     * @return void
     **/
    void add(AddReplyCommentRequest addReplyCommentRequest, Long id);

    /**
     * @description 获取某一一级评论下的所有二级评论
     * @param replyCommentId
     * @return java.util.List<com.community.model.vo.ReplyCommentsVO>
     **/
    List<ReplyCommentsVO> getList(Long replyCommentId);

    /**
     * @description 给二级评论点赞
     * @param commentId
     * @return void
     **/
    void addCommentLikes(Long commentId);

    /**
     * @description 用户取消点赞
     * @param commentId
     * @return void
     **/
    void decreaseCommentLikes(Long commentId);

    /**
     * @description 获取用户未读消息列表
     * @param id
     * @return java.util.List<com.common.dto.MessageDto>
     **/
    List<MessageDto> getUnreadMessageList(Long id);

    /**
     * @description 标记已读消息
     * @param id
     * @param messageId
     * @return void
     **/
    Boolean readMessage(Long id, Long messageId);

    /**
     * @description 获取二级评论的所有点赞和评论
     * @param id
     * @return com.common.dto.SocialDataDto
     **/
    SocialDataDto getSocialData(Long id);
}
