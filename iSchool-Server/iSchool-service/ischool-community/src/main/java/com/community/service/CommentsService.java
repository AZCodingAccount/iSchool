package com.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.dto.SocialDataDto;
import com.community.model.dto.AddCommentRequest;
import com.community.model.entity.Comments;
import com.community.model.vo.CommentsVO;

import java.util.List;

/**
 * @author Ljx
 * @description 针对表【comments】的数据库操作Service
 * @createDate 2024-05-03 22:24:26
 */
public interface CommentsService extends IService<Comments> {

    /**
     * @param addCommentRequest
     * @return void
     * @description 发布一级评论
     **/
    void add(AddCommentRequest addCommentRequest, Long id);

    /**
     * @param
     * @return List<CommentsVO>
     * @description 获取某个点评对象的所有一级评论数据
     **/
    List<CommentsVO> getList(Long objId,Long requestUserId);

    /**
     * @param commentId
     * @return void
     * @description 给一级评论点赞
     **/
    void addCommentLikes(Long userId, Long commentId);

    /**
     * @param commentId
     * @return void
     * @description 取消一级评论点赞
     **/
    void decreaseCommentLikes(Long userId,Long commentId);

    /**
     * @param id
     * @return com.common.dto.SocialDataDto
     * @description 获取一级评论下的点赞和评论
     **/
    SocialDataDto getSocialData(Long id);
}
