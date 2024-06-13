package com.search.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-05 21:00
 * @description: 生产者代码
 **/
@Component
@Slf4j
public class AiMessageProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(String message) {
        log.info("生产者生产消息，消息内容为:{}",message);
        rabbitTemplate.convertAndSend(AiMQConstant.AI_EXCHANGE_NAME, AiMQConstant.AI_ROUTING_KEY, message);
    }
}

