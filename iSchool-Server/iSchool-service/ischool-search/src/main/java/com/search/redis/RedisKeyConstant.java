package com.search.redis;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-06 14:50
 * @description: Redis键的常量值
 **/
public class RedisKeyConstant {

    /*
     * AI搜索缓存
     * 格式：ai:search:"蓝桥杯"
     */
    public static final String AI_SEARCH_RESULT = "search:ai:";

    /*
     * AI聊天历史记录缓存
     * 格式：ai:chat:"1785932839872155650"
     */
    public static final String AI_CHAT_HISTORY = "ai:chat:";


    /*
     * 用户查询缓存
     * 格式：search:user:20:蓝桥杯（20是页数，蓝桥杯是keyword）
     */
    public static final String USER_SEARCH_LIST = "search:user:";


}