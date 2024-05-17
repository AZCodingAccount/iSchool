package com.search.demo.springai;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-05 16:10
 * @description: 测试SpringAI能否正常使用的测试类
 **/
@RestController
public class SpringAIDemo {

    OpenAiChatClient chatClient;

    // 用户检索预设
    private static final String systemPrompt = "你是一个中国大学专业的教务文档搜索助手，你需要对用户的关键检索词进行回答，这些关键词大概有竞赛、保研政策等方面、" +
            "你需要相关的搜索结果和自己的推荐，以帮助用户拓宽视野，多角度看待问题。回答请完整，必须使用中文回答！！！";

    // 历史消息列表(初始化有个系统预设)
    static List<Message> historyMessage = new ArrayList<>(List.of(new SystemMessage(systemPrompt)));

    // 历史消息列表的最大长度
    static int maxLen = 10;


    // 创建client
    public SpringAIDemo() {
        var openAiApi = new OpenAiApi("https://jiekou.wlai.vip/", "sk-YBNqrFIxlQRPIsmD1e65079cBe2146F189F72a4845Bd2f8f");
        var openAiChatOptions = OpenAiChatOptions.builder()
                // .withModel(OpenAiApi.ChatModel.GPT_4_TURBO_PREVIEW.value)   // 0.06/1K TOKENS
                .withModel(OpenAiApi.ChatModel.GPT_3_5_TURBO.value)
                // .withModel(OpenAiApi.ChatModel.GPT_4.value)              // 0.24/1K TOKENS
                // .withTemperature(0.4F)
                // .withMaxTokens(200)
                .build();
        this.chatClient = new OpenAiChatClient(openAiApi, openAiChatOptions);
    }

    // 最基础使用
    public String aiMessage(String keyword) {
        return chatClient.call(keyword);
    }

    // 用户检索预设
    public String aiMessageWithPreset(String keyword) {
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


    /*
     * 用户聊天上下文功能
     * */
    public String aiMessageWithContext(String msg) {
        // 1：添加处理用户输入
        historyMessage.add(new UserMessage(msg));
        // 发给AI前对历史消息对列的长度进行检查，如果超出最大长度，就删除旧的对话信息
        if (historyMessage.size() > maxLen) {
            historyMessage = historyMessage.subList(historyMessage.size() - maxLen - 1, historyMessage.size());
            // 添加系统预设到第一条
            historyMessage.add(0, new SystemMessage(systemPrompt));
        }
        // 2：获取AssistantMessage
        ChatResponse chatResponse = chatClient.call(new Prompt(historyMessage));
        AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
        // 3：将AI回复的消息放到历史消息列表中
        historyMessage.add(assistantMessage);
        // 4：返回用户响应信息
        return assistantMessage.getContent();
    }
}
