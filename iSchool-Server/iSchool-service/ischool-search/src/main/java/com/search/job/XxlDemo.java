package com.search.job;

import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-16 20:41
 * @description: xxl测试类
 **/
@Component
public class XxlDemo {

    @XxlJob("demoJobHandler")
    public void test() {
        System.out.println("hello");
    }
}
