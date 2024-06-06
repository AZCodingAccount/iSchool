package com.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.community.model.dto.AddCommentObjRequest;
import com.community.model.dto.CommentObjSearchParam;
import com.community.model.dto.ScoreRequest;
import com.community.model.entity.CommentObj;
import com.community.model.vo.CommentObjVO;

import java.util.List;

/**
 * @author Ljx
 * @description 针对表【obj】的数据库操作Service
 * @createDate 2024-05-03 21:10:19
 */
public interface CommentObjService extends IService<CommentObj> {

    /**
     * @param addCommentObjRequest
     * @return void
     * @description 添加点评对象
     **/
    void add(AddCommentObjRequest addCommentObjRequest);

    /**
     * @param keyword
     * @param type
     * @param userId
     * @return java.util.List<com.community.model.entity.CommentObj>
     * @description 搜索点评对象
     **/
    List<CommentObjVO> search(String keyword, String type, Long userId);

    /**
     * @param scoreRequest
     * @param id
     * @return void
     * @description 点评对象评分
     **/
    void score(ScoreRequest scoreRequest, Long id);
}
