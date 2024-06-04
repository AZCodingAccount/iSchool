package com.community.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.client.service.UserFeignClient;
import com.community.mapper.CommentObjMapper;
import com.community.mapper.ReplyCommentsMapper;
import com.community.model.constants.CommentObjTypeConstants;
import com.community.model.dto.AddCommentObjRequest;
import com.community.model.dto.ScoreRequest;
import com.community.model.entity.CommentObj;
import com.community.model.entity.Comments;
import com.community.model.entity.ReplyComments;
import com.community.model.entity.UserObjStars;
import com.community.model.vo.CommentObjVO;
import com.community.service.CommentObjService;
import com.ischool.exception.BusinessException;
import com.ischool.model.ErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ljx
 * @description 针对表【obj】的数据库操作Service实现
 * @createDate 2024-05-03 21:10:19
 */
@Service
public class CommentObjServiceImpl extends ServiceImpl<CommentObjMapper, CommentObj>
        implements CommentObjService {

    @Autowired
    UserFeignClient userFeignClient;

    @Autowired
    UserObjStarsServiceImpl userObjStarsService;

    @Autowired
    CommentsServiceImpl commentsService;

    @Autowired
    ReplyCommentsMapper replyCommentsMapper;


    /**
     * @param addCommentObjRequest
     * @return void
     * @description 添加点评对象
     **/
    public void add(AddCommentObjRequest addCommentObjRequest) {
        // 参数校验
        String name = addCommentObjRequest.getName();
        String type = addCommentObjRequest.getType();
        if (StringUtils.isAnyBlank(name, type)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (!CommentObjTypeConstants.isLegal(type)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "点评对象类型不合法");
        }

        // 不能重名
        LambdaQueryWrapper<CommentObj> lqw = new LambdaQueryWrapper<CommentObj>();
        lqw.eq(CommentObj::getName, name);
        CommentObj commentObj = this.baseMapper.selectOne(lqw);
        if (commentObj != null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "点评对象名称重复");
        }

        // 插入数据库
        CommentObj commentObj1 = new CommentObj();
        BeanUtils.copyProperties(addCommentObjRequest, commentObj1);
        this.baseMapper.insert(commentObj1);
    }

    /**
     * @return java.util.List<com.community.model.entity.CommentObj>
     * @description 搜索点评对象
     **/
    @Override
    public List<CommentObjVO> search(String keyword, String type, Long userId) {
        // 1：参数校验
        if (StringUtils.isNotBlank(type) && !CommentObjTypeConstants.isLegal(type)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 2：查询数据
        LambdaQueryWrapper<CommentObj> lqw = new LambdaQueryWrapper<>();
        lqw.like(StringUtils.isNotBlank(keyword), CommentObj::getName, keyword);
        lqw.eq(StringUtils.isNotBlank(type), CommentObj::getType, type);

        List<CommentObj> commentObjs = this.baseMapper.selectList(lqw);
        if (commentObjs == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        // 3: 填充当前用户对点评对象的评分
        List<CommentObjVO> commentObjVOList = new ArrayList<>();
        for (CommentObj commentObj : commentObjs) {
            CommentObjVO commentObjVO = new CommentObjVO();
            BeanUtils.copyProperties(commentObj, commentObjVO);
            Long objId = commentObj.getId();
            // 获取点评对象的评分
            UserObjStars userObjStars = userObjStarsService.getBaseMapper().selectOne(new LambdaQueryWrapper<UserObjStars>()
                    .eq(UserObjStars::getUserId, userId)
                    .eq(UserObjStars::getObjId, objId));
            Double score = 0d;
            if (userObjStars != null) {
                score = userObjStars.getScore();
            }
            commentObjVO.setUserScore(score);
            // 获取评论数
            Long count1 = commentsService.getBaseMapper().selectCount(new LambdaQueryWrapper<Comments>()
                    .eq(Comments::getObjId, objId));
            Long count2 = replyCommentsMapper.selectCount(new LambdaQueryWrapper<ReplyComments>()
                    .eq(ReplyComments::getObjId, objId));
            commentObjVO.setCommentCount((int) (count1 + count2));
            commentObjVOList.add(commentObjVO);
        }
        return commentObjVOList;
    }

    /**
     * @param scoreRequest
     * @param id
     * @return void
     * @description 点评对象评分
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void score(ScoreRequest scoreRequest, Long id) {
        // 1：参数校验
        Long commentObjId = scoreRequest.getCommentObjId();
        Double score = scoreRequest.getScore();
        // 1.1: 合法性校验
        if (commentObjId == null || score == null || id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (score < 0 || score > 10) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "评分应在0-10之间");
        }

        // 1.2: 检查点评对象id和用户id是否存在
        CommentObj commentObj = this.baseMapper.selectById(commentObjId);
        Boolean checked = userFeignClient.checkId(id);
        if (!checked || commentObj == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户id或点评对象id不存在");
        }

        // 2:构造查询条件。
        LambdaQueryWrapper<UserObjStars> queryWrapper = new LambdaQueryWrapper<UserObjStars>()
                .eq(UserObjStars::getUserId, id)
                .eq(UserObjStars::getObjId, commentObjId);
        // 获取之前的评分
        UserObjStars originUserObjStars = userObjStarsService.getBaseMapper().selectOne(queryWrapper);
        Double newScore = 0d;


        // 3：计算并更新点评对象评分
        Double score1 = commentObj.getScore();
        Integer scoreCount = commentObj.getCount();
        if (originUserObjStars != null) {
            Double originScore = originUserObjStars.getScore();
            // 3.1: 删除插入的评分记录
            userObjStarsService.getBaseMapper().delete(queryWrapper);
            // 3.2: 评分数减1
            commentObj.setCount(commentObj.getCount() - 1);
            this.baseMapper.updateById(commentObj);
            // 业务逻辑是这样的，再次评分需要计算之前的总评分，去掉原来的评分，加上新评分
            newScore = (score1 * scoreCount - originScore + score) / scoreCount;
        } else {
            // 业务逻辑是这样的，再次评分需要计算之前的总评分，去掉原来的评分，加上新评分
            newScore = (score1 * scoreCount + score) / (scoreCount + 1);
        }
        commentObj.setCount(commentObj.getCount() + 1);
        commentObj.setScore(newScore);
        this.baseMapper.updateById(commentObj);

        // 4: 插入更新记录表数据
        UserObjStars userObjStars = new UserObjStars();
        userObjStars.setUserId(id);
        userObjStars.setObjId(commentObjId);
        userObjStars.setScore(score);

        userObjStarsService.getBaseMapper().insert(userObjStars);
    }
}




