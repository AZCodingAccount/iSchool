package com.search.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import com.ischool.model.BaseResponse;
import com.ischool.model.ErrorCode;
import com.ischool.model.PageResult;
import com.ischool.model.Result;
import com.search.ai.AIUtil;
import com.search.model.vo.SearchAnnouncementVO;
import com.search.mq.AiMessageProducer;
import com.search.redis.RedisKeyConstant;
import com.search.service.InfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-04 21:32
 * @description: 搜索控制器
 **/
@RestController
@RequestMapping
@Slf4j
public class SearchController {

    @Autowired
    InfoService infoService;


    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return com.ischool.model.BaseResponse<java.util.List < SearchAnnouncementVO>>
     * @description MySQL普通分页查询公告信息
     **/
    @GetMapping("/page")
    public BaseResponse<PageResult<SearchAnnouncementVO>> searchAnnouncement(@RequestParam("keyword") String keyword,
                                                                             @RequestParam("pageNum") Integer pageNum,
                                                                             @RequestParam("pageSize") Integer pageSize) {
        log.info("用户搜索，搜索信息为：{}，分页查询：{}，{}", keyword, pageNum, pageSize);
        // 首先看是否命中缓存
        String key = RedisKeyConstant.USER_SEARCH_LIST + pageSize + ":" + keyword;
        Object object = redisTemplate.opsForValue().get(key);
        if (pageNum == 1 && object != null) {
            // 直接返回
            return Result.success((PageResult<SearchAnnouncementVO>) object);
        }
        PageResult<SearchAnnouncementVO> pageResult = infoService.search(keyword, pageNum, pageSize);
        // 如果pageNum=1，那我就把当前值缓存下来
        if (pageNum == 1) {
            redisTemplate.opsForValue().set(key, pageResult);
        }
        return Result.success(pageResult);
    }


    /*
        todo:
            搜索：
                1: MySQL切分不查全量内容
                2: Elasticsearch全文检索
                3: 添加Redis缓存
            数据同步：
                1：爬虫定时任务定期写入数据(并更新redis缓存的值)
     */
}
