package com.search.job;

import com.search.es.SyncESService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program: iSchool-Server
 * @author: Ljx
 * @create: 2024-05-16 20:41
 * @description: xxl测试类
 **/
@Component
public class XxlDemo {

    @Autowired
    SyncESService esService;

    @XxlJob("demoJobHandler")
    public void test() {
        System.out.println("hello");
    }

}
