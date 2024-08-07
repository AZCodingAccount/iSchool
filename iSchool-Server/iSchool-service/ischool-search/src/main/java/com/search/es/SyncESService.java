package com.search.es;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-19 17:25
 * @description: ES数据同步实现类
 **/

import com.search.redis.RedisKeyConstant;
import com.search.service.InfoService;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SyncESService {

    // 操作info表
    @Autowired
    private InfoService infoService;

    // 操作announcement索引
    @Autowired
    private AnnouncementEsDao announcementEsDao;

    // 操作缓存数据
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    private static final int BATCH_SIZE = 100;

    public void syncESFullData(String school) {
        int batchNum = 1;
        long startTime = System.currentTimeMillis();
        int totalCount = 0;

        List<AnnouncementESDTO> announcementESDTOList = infoService.findBySchoolLimitBatchSize(school, BATCH_SIZE, batchNum);
        while (!announcementESDTOList.isEmpty()) {
            // 从mysql中查询数据
            announcementESDTOList = infoService.findBySchoolLimitBatchSize(school, BATCH_SIZE, batchNum);
            // 给数据添加plainText字段并进行数据清洗
            List<AnnouncementESDTO> objectList = announcementESDTOList.stream().peek(item -> {
                String plainText = Jsoup.parse(item.getContent()).text().replaceFirst(".*?浏览次数：\\d+\\s*", "");
                item.setPureText(plainText);
            }).toList();
            announcementEsDao.saveAll(objectList);
            batchNum += 1;
            totalCount += announcementESDTOList.size();
        }
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime) / 1000;
        log.info("学校 {} 全量从MySQL同步到ES的数据成功，耗时{}秒，共同步{}条", school, duration, totalCount);
        deleteAllUserSearchCaches();    // 删除缓存数据
    }

    /**
     * @param school
     * @param lastEndArticleId
     * @return void
     * @description 增量同步MySQL数据到ES
     **/
    public void syncESIncrData(String school, Long lastEndArticleId) {
        if (lastEndArticleId == null) {
            lastEndArticleId = 0L;
        }
        List<AnnouncementESDTO> announcementESDTOList = infoService.findBySchoolAndIdGreaterThan(school, lastEndArticleId);
        // 给数据添加去除标签的字段并且去除标题的内容
        List<AnnouncementESDTO> objectList = announcementESDTOList.stream().peek(item -> {
            String plainText = Jsoup.parse(item.getContent()).text().replaceFirst(".*?浏览次数：\\d+\\s*", "");
            item.setPureText(plainText);
        }).toList();
        announcementEsDao.saveAll(objectList);
        int size = objectList.size();
        log.info("增量同步学校 {} 的数据成功，共同步{}条", school, size);
        deleteAllUserSearchCaches();    // 删除缓存数据
    }


    /**
     * 删除所有用户查询相关的缓存
     */
    public void deleteAllUserSearchCaches() {
        // 获取所有与用户查询相关的键
        Set<String> keys1 = redisTemplate.keys(RedisKeyConstant.USER_SEARCH_LIST + "*");
        Set<String> keys2 = redisTemplate.keys(RedisKeyConstant.USER_SEARCH_BY_ID + "*");
        if (keys1 != null && !keys1.isEmpty()) {
            // 删除所有匹配的键
            redisTemplate.delete(keys1);
        }
        if (keys2 != null && !keys2.isEmpty()) {
            // 删除所有匹配的键
            redisTemplate.delete(keys2);
        }
    }
}

