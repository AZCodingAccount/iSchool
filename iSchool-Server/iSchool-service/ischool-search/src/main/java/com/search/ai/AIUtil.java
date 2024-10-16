package com.search.ai;

import com.ischool.exception.BusinessException;
import com.ischool.model.ErrorCode;
import com.search.redis.RedisKeyConstant;
import jakarta.annotation.PostConstruct;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-05 16:10
 * @description: 测试SpringAI能否正常使用的测试类
 **/
@Component
public class AIUtil {

    @Autowired
    OpenAiChatClient chatClient;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    // 用户检索预设
    private static final String systemPrompt = "你是一个中国大学专业的教务文档搜索助手，你需要对用户的关键检索词进行回答，这些关键词大概有竞赛、保研政策等方面、" +
            "你需要相关的搜索结果和自己的推荐，以帮助用户拓宽视野，多角度看待问题。回答请完整，必须使用中文回答！！！";

    // 历史消息列表(初始化有个系统预设) todo:考虑高并发


    // 历史消息列表的最大长度
    static int maxLen = 5;

    /**
     * @param keyword
     * @return java.lang.String
     * @description 根据关键词问ai有关更多的信息
     **/
    public String search(String keyword) {
        // 1：构建请求消息
        List<Message> userMessage = new ArrayList<>();
        userMessage.add(new SystemMessage(systemPrompt));
        userMessage.add(new UserMessage("请详细叙述" + keyword + "的内容"));
        Prompt prompt = new Prompt(userMessage);
        // 2：调用
        ChatResponse chatResponse = chatClient.call(prompt);
        AssistantMessage assistantMessage = chatResponse.getResult().getOutput();

        // 3：返回
        return assistantMessage.getContent();
    }


    /**
     * @param content
     * @return java.lang.String
     * @description 跟ai聊天
     **/
    @SuppressWarnings("unchecked")
    public String chat(Long id, String content) {

        String key = RedisKeyConstant.AI_CHAT_HISTORY + id; // redis key
        // 每次从redis中获取数据
        List<Message> cacheHistoryMessages = (List<Message>) (List<?>) redisTemplate.opsForList().range(key, 0, -1);
        if (cacheHistoryMessages == null || cacheHistoryMessages.isEmpty()) { // 第一次聊天，初始化redis缓存
            cacheHistoryMessages = new ArrayList<>(List.of(new SystemMessage(systemPrompt)));
            redisTemplate.opsForList().rightPushAll(key, cacheHistoryMessages);
        }

        // 1：添加处理用户输入
        cacheHistoryMessages.add(new UserMessage(content));
        // 发给AI前对历史消息对列的长度进行检查，如果超出最大长度，就删除旧的对话信息
        if (cacheHistoryMessages.size() > maxLen) {
            cacheHistoryMessages = cacheHistoryMessages.subList(cacheHistoryMessages.size() - maxLen - 1, cacheHistoryMessages.size());
            // 添加系统预设到第一条
            cacheHistoryMessages.add(0, new SystemMessage(systemPrompt));
            // 删除redis中的这个数据
            redisTemplate.delete(key); // 删除旧的历史消息
            redisTemplate.opsForList().rightPushAll(key, cacheHistoryMessages.toArray()); // 存储新的历史消息
        }
        // 2：获取AssistantMessage
        ChatResponse chatResponse = chatClient.call(new Prompt(cacheHistoryMessages));
        AssistantMessage assistantMessage = chatResponse.getResult().getOutput();

        // 3：将AI回复的消息放到历史消息列表中
        cacheHistoryMessages.add(assistantMessage);

        // 4：更新redis中的对话缓存
        redisTemplate.delete(key); // 删除旧的历史消息
        redisTemplate.opsForList().rightPushAll(key, cacheHistoryMessages.toArray());

        // 5：返回用户响应信息
        return assistantMessage.getContent();
    }

    /*
        使用预设跟ai进行一问一答
     */
    public String chatWithRole(String role, String message) {
        // 1：构建请求消息
        List<Message> userMessage = new ArrayList<>();
        userMessage.add(new SystemMessage(role));
        userMessage.add(new UserMessage(message));
        Prompt prompt = new Prompt(userMessage);

        // 2：调用
        ChatResponse chatResponse = chatClient.call(prompt);

        AssistantMessage assistantMessage = chatResponse.getResult().getOutput();

        // 3：返回
        return assistantMessage.getContent();
    }
}
