package com.community.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.community.mapper.CommentObjMapper;
import com.community.model.constants.CommentObjTypeConstants;
import com.community.model.dto.AddCommentObjRequest;
import com.community.model.dto.CommentObjSearchParam;
import com.community.model.entity.CommentObj;
import com.community.service.CommentObjService;
import com.common.exception.BusinessException;
import com.common.model.ErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Albert han
 * @description 针对表【obj】的数据库操作Service实现
 * @createDate 2024-05-03 21:10:19
 */
@Service
public class CommentObjServiceImpl extends ServiceImpl<CommentObjMapper, CommentObj>
        implements CommentObjService {

    /**
     * @param addCommentObjRequest
     * @return void
     * @description 添加点评对象
     **/
    public void add(AddCommentObjRequest addCommentObjRequest) {
        // 1：参数校验
        String name = addCommentObjRequest.getName();
        String type = addCommentObjRequest.getType();
        if (StringUtils.isAnyBlank(name, type)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (CommentObjTypeConstants.isLegal(type)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "点评对象类型不合法");
        }

        // 不能重名
        CommentObj commentObj = this.baseMapper.selectOne(new LambdaQueryWrapper<CommentObj>()
                .eq(CommentObj::getName, name));
        if (commentObj != null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "点评对象名称重复");
        }

        // 插入数据库
        CommentObj commentObj1 = new CommentObj();
        BeanUtils.copyProperties(addCommentObjRequest, commentObj1);
        this.baseMapper.insert(commentObj1);
    }

    /**
     * @param commentObjSearchParam
     * @return java.util.List<com.community.model.entity.CommentObj>
     * @description 搜索点评对象
     **/
    @Override
    public List<CommentObj> search(CommentObjSearchParam commentObjSearchParam) {
        // 1：校验参数
        String keyword = commentObjSearchParam.getKeyword();
        String type = commentObjSearchParam.getType();
        if (CommentObjTypeConstants.isLegal(type)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 2：查询数据
        LambdaQueryWrapper<CommentObj> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(keyword), CommentObj::getName, keyword);
        queryWrapper.eq(StringUtils.isNotBlank(type), CommentObj::getType, type);

        List<CommentObj> commentObjs = this.baseMapper.selectList(queryWrapper);
        if (commentObjs == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return commentObjs;
    }
}




