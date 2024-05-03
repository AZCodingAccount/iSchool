package com.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.community.model.dto.AddCommentRequest;
import com.community.model.entity.Comments;
import com.community.model.vo.CommentsVO;

import java.util.List;

/**
 * @author Albert han
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
    List<CommentsVO> getList(Long objId);
}
