package com.search.job;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-16 21:49
 * @description: 同步数据定时任务
 **/
@Component
@Slf4j
public class SyncDataTask {

    @XxlJob("fullDataJobHandler")
    public void syncFullData() {
        log.info("全量同步数据");
        HashMap<String, Object> body = new HashMap<>();
        // todo: 后续从数据库查，动态添加
        body.put("school", "HRBUST");

        // 通知远程python程序去同步数据
        String result = HttpUtil.createPost("http://127.0.0.1:10086/full_sync_data")
                .contentType("application/json")
                .body(JSONUtil.toJsonStr(body))
                .execute()
                .body();
        log.info("同步成功，写入日志");

    }

    @XxlJob("incrDataJobHandler")
    public void syncIncrData() {
        log.info("增量同步数据");
        HashMap<String, Object> body = new HashMap<>();
        // todo: 后续从数据库查，动态添加
        body.put("school", "HRBUST");
        // todo:从redis中拿
        body.put("end_article_id", 478);

        // 通知远程python程序去同步数据
        String result = HttpUtil.createPost("http://127.0.0.1:10086/incr_sync_data")
                .contentType("application/json")
                .body(JSONUtil.toJsonStr(body))
                .execute()
                .body();
        log.info("同步成功，写入日志");
    }

}
