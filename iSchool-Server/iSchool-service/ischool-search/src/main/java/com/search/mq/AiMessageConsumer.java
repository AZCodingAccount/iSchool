package com.search.mq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.search.ai.AIUtil;
import com.search.redis.RedisKeyConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.messaging.handler.annotation.Header;

import java.io.IOException;
import java.util.Map;


/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-05 21:27
 * @description: Ai消息消费者
 **/
@Component
@Slf4j
public class AiMessageConsumer {
    @Autowired
    AIUtil aiUtil;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(AiMQConstant.AI_QUEUE_NAME),
                    exchange = @Exchange(name = AiMQConstant.AI_EXCHANGE_NAME, type = ExchangeTypes.DIRECT),
                    key = AiMQConstant.AI_ROUTING_KEY
            ), ackMode = "MANUAL")
    public void receiverMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        log.info("AiMessageConsumer receive message: {}", message);
        // 处理业务逻辑
        // 调用AI生成接口，todo: 如果超时，重试三次，拒绝该消息，并配置死信队列
        String searchResult = aiUtil.search(message);
        log.info("AiMessageConsumer search result: {}", searchResult);
        //  将消息写入到redis
        redisTemplate.opsForValue().set(RedisKeyConstant.AI_SEARCH_RESULT + message, searchResult);
        try {
            channel.basicAck(deliveryTag, false);   // 手动确认
        } catch (IOException e) {
            log.error("AiMessageConsumer ack message error,{}", e.getMessage());
        }
    }
}
