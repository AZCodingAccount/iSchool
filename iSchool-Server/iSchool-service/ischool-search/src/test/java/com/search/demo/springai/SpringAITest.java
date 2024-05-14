package com.search.demo.springai;


import org.junit.jupiter.api.Test;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-05 17:02
 * @description: 测试springai
 **/
public class SpringAITest {


    /**
     * @param
     * @return void
     * @description 最基本的一问一答
     **/
    @Test
    public void testPlainAiMessage() {
        SpringAIDemo springAIDemo = new SpringAIDemo();
        String message = "请写一首关于程序员的七言绝句";
        String result = springAIDemo.aiMessage(message);
        System.out.println(result);
    }

    /**
     * @param
     * @return void
     * @description 有预设回答的一问一答
     **/
    @Test
    public void testPresetAiMessage() {
        SpringAIDemo springAIDemo = new SpringAIDemo();
        String message = "蓝桥杯";
        String result = springAIDemo.aiMessageWithPreset(message);
        System.out.println(result);
    }

    @Test
    public void testContextAiMessage() {
        SpringAIDemo springAIDemo = new SpringAIDemo();
        String message = "我等下会问你关于我叫什么的问题，现在我叫张三";
        String result = springAIDemo.aiMessageWithContext(message);
        System.out.println(result);
        String message2 = "我叫什么？";
        String result2 = springAIDemo.aiMessageWithContext(message2);
        System.out.println(result2);
    }

}
