package com.search.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import com.ischool.model.BaseResponse;
import com.ischool.model.PageResult;
import com.ischool.model.Result;
import com.search.model.vo.SearchAnnouncementVO;
import com.search.mq.AiMessageProducer;
import com.search.service.InfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    AiMessageProducer aiMessageProducer;

    /**
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return com.ischool.model.BaseResponse<java.util.List < SearchAnnouncementVO>>
     * @description MySQL普通分页查询公告信息
     **/
    @GetMapping
    public BaseResponse<PageResult<SearchAnnouncementVO>> searchAnnouncement(@RequestParam("keyword") String keyword,
                                                                             @RequestParam("pageNum") Integer pageNum,
                                                                             @RequestParam("pageSize") Integer pageSize) {
        log.info("用户搜索，搜索信息为：{}，分页查询：{}，{}", keyword, pageNum, pageSize);
        PageResult<SearchAnnouncementVO> pageResult = infoService.search(keyword, pageNum, pageSize);
        return Result.success(pageResult);
    }

    /**
     * @param keyword
     * @return com.ischool.model.BaseResponse<java.lang.Object>
     * @description 生成ai评论
     **/
    @GetMapping("/ai")
    public BaseResponse<String> aiSearch(@RequestParam("keyword") String keyword) {
        log.info("用户搜索，搜索信息为：{}", keyword);
        // 使用雪花算法生成一个id，用于追踪这个搜索信息
        String id = IdUtil.getSnowflakeNextIdStr();
        Map<String, Object> message = new HashMap<>();
        message.put("id", id);
        message.put("message", keyword);
        aiMessageProducer.sendMessage(message);
        return Result.success(id);
    }



    /*
        todo:
            搜索：
                1: MySQL切分不查全量内容
                2: Elasticsearch全文检索
                3: 添加Redis缓存
            数据同步：
                1：爬虫定时任务定期写入数据
            Spring AI：
                1：AI检索+聊天
     */
}
