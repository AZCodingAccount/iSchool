package com.search.controller;

import com.ischool.model.BaseResponse;
import com.ischool.model.PageResult;
import com.ischool.model.Result;
import com.search.model.dto.SearchAnnouncementRequest;
import com.search.model.vo.AnnouncementVO;
import com.search.model.vo.searchAnnouncementVO;
import com.search.redis.RedisKeyConstant;
import com.search.service.InfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-04 21:32
 * @description: 搜索控制器
 **/
@RestController
@RequestMapping
@Slf4j
@Tag(name = "搜索相关接口")
public class SearchController {

    @Autowired
    InfoService infoService;


    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * @param searchAnnouncementRequest
     * @param school
     * @return com.ischool.model.BaseResponse<com.ischool.model.PageResult < com.search.model.vo.SearchAnnouncementVO>>
     * @description 查询公告信息
     **/
    @GetMapping("/page")
    @Operation(summary = "搜索公告")
    public BaseResponse<PageResult<searchAnnouncementVO>> searchAnnouncement(SearchAnnouncementRequest searchAnnouncementRequest,
                                                                             @Parameter(hidden = true) @RequestHeader("school") String school) {
        log.info("用户搜索，搜索信息为：{}", searchAnnouncementRequest);
        Integer pageNum = searchAnnouncementRequest.getPageNum();
        Integer pageSize = searchAnnouncementRequest.getPageSize();
        String keyword = searchAnnouncementRequest.getKeyword();
        // 首先看是否命中缓存
        String key = RedisKeyConstant.USER_SEARCH_LIST + pageSize + ":" + keyword;
        Object object = redisTemplate.opsForValue().get(key);
        if (pageNum == 1 && object != null) {
            // 直接返回
            return Result.success((PageResult<searchAnnouncementVO>) object);
        }
        // 简单MySQL
        // PageResult<SearchAnnouncementVO> pageResult = infoService.search(searchAnnouncementRequest, school);

        // 使用es
        PageResult<searchAnnouncementVO> pageResult = infoService.searchFromES(searchAnnouncementRequest, school);

        // 如果pageNum=1，那我就把当前值缓存下来
        if (pageNum == 1) {
            redisTemplate.opsForValue().set(key, pageResult);
        }
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    public BaseResponse<AnnouncementVO> searchAnnoById(@PathVariable("id") Long id) {
        log.info("搜索id为{}的公告", id);
        String key = RedisKeyConstant.USER_SEARCH_BY_ID + id;
        Object object = redisTemplate.opsForValue().get(key);
        if (object != null) {   // 命中缓存
            return Result.success((AnnouncementVO) object);
        }
        AnnouncementVO searchAnnouncementVO = infoService.searchByIdFromES(id);
        // 缓存起来
        redisTemplate.opsForValue().set(key, searchAnnouncementVO);
        return Result.success(searchAnnouncementVO);
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
