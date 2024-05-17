package com.search.job;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.search.redis.RedisKeyConstant;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.context.XxlJobHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-16 21:49
 * @description: 同步数据定时任务
 **/
@Component
@Slf4j
public class SyncDataTask {
    @Autowired
    public RedisTemplate<String, Object> redisTemplate;

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
        // 处理返回的状态
        if ("ok".equals(result)) {
            log.info("同步成功");
        } else {
            XxlJobHelper.handleFail("全量同步数据时Python程序存在异常，请查看错误日志进行排查");
        }

    }

    @XxlJob("incrDataJobHandler")
    public void syncIncrData() {
        log.info("增量同步数据");
        HashMap<String, Object> body = new HashMap<>();
        // todo: 后续从数据库查，动态添加
        body.put("school", "HRBUST");
        Integer articleId = (Integer) redisTemplate.opsForValue().get(RedisKeyConstant.SYNC_END_ARTICLE_ID);
        body.put("end_article_id", articleId);
        // 通知远程python程序去同步数据
        String result = HttpUtil.createPost("http://127.0.0.1:10086/incr_sync_data")
                .contentType("application/json")
                .body(JSONUtil.toJsonStr(body))
                .execute()
                .body();
        JSONObject jsonObject = JSONUtil.parseObj(result);
        if ("ok".equals(jsonObject.get("msg"))) {
            // 将id写入redis
            Integer newArticleId = (Integer) jsonObject.get("articleId");
            redisTemplate.opsForValue().set(RedisKeyConstant.SYNC_END_ARTICLE_ID, newArticleId);
            log.info("数据同步成功");
        } else {
            XxlJobHelper.handleFail("增量同步数据时Python程序存在异常，请查看错误日志进行排查");
        }
    }

}
