package com.community.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public enum CommentObjEnums {

    USER("user", 1),
    ADMIN("admin", 2);
    private final String text;

    private final Integer value;

    CommentObjEnums(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static CommentObjEnums getEnumByValue(Integer value) {
        if (value == 0) {
            return null;
        }
        for (CommentObjEnums anEnum : CommentObjEnums.values()) {
            if (Objects.equals(anEnum.value, value)) {
                return anEnum;
            }
        }
        return null;
    }

    public Integer getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}