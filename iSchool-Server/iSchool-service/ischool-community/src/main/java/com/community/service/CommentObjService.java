package com.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.community.model.dto.AddCommentObjRequest;
import com.community.model.dto.CommentObjSearchParam;
import com.community.model.entity.CommentObj;

import java.util.List;

/**
* @author Albert han
* @description 针对表【obj】的数据库操作Service
* @createDate 2024-05-03 21:10:19
*/
public interface CommentObjService extends IService<CommentObj> {

    /**
     * @description 添加点评对象
     * @param addCommentObjRequest
     * @return void
     **/
    void add(AddCommentObjRequest addCommentObjRequest);

    /**
     * @description 搜索点评对象
     * @param commentObjSearchParam
     * @return java.util.List<com.community.model.entity.CommentObj>
     **/
    List<CommentObj> search(CommentObjSearchParam commentObjSearchParam);
}
