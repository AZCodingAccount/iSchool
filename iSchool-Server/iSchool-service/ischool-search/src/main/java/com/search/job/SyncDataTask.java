package com.search.job;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.client.service.UserFeignClient;
import com.common.vo.SchoolVO;
import com.search.es.SyncESService;
import com.search.redis.RedisKeyConstant;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.context.XxlJobHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: iSchool-Server
 * @author: Ljx
 * @create: 2024-05-16 21:49
 * @description: 同步数据定时任务
 **/
@Component
@Slf4j
public class SyncDataTask {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    UserFeignClient userFeignClient;

    @Autowired
    SyncESService esService;

    @XxlJob("fullDataJobHandler")
    public void syncFullData() {
        log.info("全量同步数据");
        HashMap<String, Object> body = new HashMap<>();
        List<SchoolVO> schoolList = userFeignClient.getSchoolList().getData();
        for (SchoolVO schoolVO : schoolList) {
            String schoolName = schoolVO.getSchoolName();
            body.put("school", schoolName);
            // 通知远程python程序去同步数据
            String result = HttpUtil.createPost("http://127.0.0.1:10086/full_sync_data")
                    .contentType("application/json")
                    .body(JSONUtil.toJsonStr(body))
                    .execute()
                    .body();
            // 处理返回的状态
            if ("ok".equals(result)) {
                log.info("学校{}同步成功", schoolName);
                esService.syncESFullData(schoolName);
            } else {
                XxlJobHelper.handleFail("全量同步学校" + schoolName + "数据时Python程序存在异常，请查看错误日志进行排查");
            }
        }
    }

    @XxlJob("incrDataJobHandler")
    public void syncIncrData() {
        log.info("增量同步数据");
        HashMap<String, Object> body = new HashMap<>();
        List<SchoolVO> schoolList = userFeignClient.getSchoolList().getData();
        for (SchoolVO schoolVO : schoolList) {
            String schoolName = schoolVO.getSchoolName();
            body.put("school", schoolName);
            String redisKey = RedisKeyConstant.SYNC_END_ARTICLE_ID + schoolName;
            Long articleId = (Long) redisTemplate.opsForValue().get(redisKey);
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
                Long newArticleId = (Long) jsonObject.get("articleId");
                redisTemplate.opsForValue().set(redisKey, newArticleId);
                log.info("学校{}数据同步成功", schoolName);
                // 将数据同步到es中
                esService.syncESIncrData(schoolName, articleId);
            } else {
                XxlJobHelper.handleFail("增量同步" + schoolName + "大学数据时Python程序存在异常，请查看错误日志进行排查");
            }
        }
    }


    @XxlJob("testES")
    public void testSyncES2MySQL() {
        System.out.println("测试同步执行器启动了");
        esService.syncESFullData("HRBUST");
    }

}
