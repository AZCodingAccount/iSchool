package com.search.rabbitmq;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-05 21:00
 * @description: 生产者代码
 **/
@SpringBootTest
public class AiMessageProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void sendMessage() {
        rabbitTemplate.convertAndSend("test", "hello");
    }
}

