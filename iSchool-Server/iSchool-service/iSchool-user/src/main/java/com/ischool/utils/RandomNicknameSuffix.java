package com.ischool.utils;

import java.util.Random;

/**
 * @program: iSchool-Server
 * @author: AlbertZhang
 * @create: 2024-05-02 14:11
 * @description: 生成随机的用户名后缀
 **/
public class RandomNicknameSuffix {
    private static final Random RANDOM = new Random();
    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyz";

    public static String generateUsernameSuffix(int length) {
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(ALPHABET.length());
            stringBuilder.append(ALPHABET.charAt(index));
        }
        return stringBuilder.toString();
    }

}
