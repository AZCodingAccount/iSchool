// package com.search.redis;
//
// import org.springframework.ai.chat.messages.Message;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.redis.core.RedisTemplate;
// import org.springframework.stereotype.Component;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
//
// import java.util.List;
//
// /**
//  * @program: iSchool-Server
//  * @author: Ljx
//  * @create: 2024-05-06 11:39
//  * @description:
//  **/
// @RestController
// @RequestMapping("/redis")
// public class RedisDemo {
//     @Autowired
//     private RedisTemplate<String, Object> redisTemplate;
//
//     @PostMapping
//     public void add() {
//         redisTemplate.opsForValue().set("name", new MyMessage("zhangsan","张三"));
//     }
//
//     @GetMapping
//     public String get() {
//         Object object = redisTemplate.opsForValue().get("name");
//         System.out.println(object);
//         return String.valueOf(object);
//     }
//
//     @PostMapping("list")
//     public void addList() {
//         redisTemplate.opsForList().rightPushAll("list",
//                 new MyMessage("zhangsan", "你好"), new MyMessage("lisi", "你也好"));
//     }
//
//     @GetMapping("list")
//     public void getList() {
//         List<MessageInterface> messages = (List<MessageInterface>) (List<?>) redisTemplate.opsForList().range("list", 0, -1);
//         System.out.println(messages);
//     }
// }
