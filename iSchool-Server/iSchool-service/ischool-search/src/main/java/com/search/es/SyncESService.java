package com.search.es;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-19 17:25
 * @description: ES数据同步实现类
 **/

import com.search.service.InfoService;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SyncESService {

    // 操作info表
    @Autowired
    private InfoService infoService;

    // 操作announcement索引
    @Autowired
    private AnnouncementEsDao announcementEsDao;


    private static final int BATCH_SIZE = 100;

    public void syncESFullData(String school) {
        int batchNum = 1;
        long startTime = System.currentTimeMillis();
        int totalCount = 0;


        List<AnnouncementESDTO> announcementESDTOList = infoService.findBySchoolLimitBatchSize(school, BATCH_SIZE, batchNum);
        while (!announcementESDTOList.isEmpty()) {
            announcementESDTOList = infoService.findBySchoolLimitBatchSize(school, BATCH_SIZE, batchNum);
            announcementEsDao.saveAll(announcementESDTOList);
            batchNum += 1;
            totalCount += announcementESDTOList.size();
        }
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime) / 1000;
        log.info("学校 {} 全量从MySQL同步到ES的数据成功，耗时{}秒，共同步{}条", school, duration, totalCount);
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
        announcementEsDao.saveAll(announcementESDTOList);
        int size = announcementESDTOList.size();
        log.info("增量同步学校 {} 的数据成功，共同步{}条", school, size);
    }
}

