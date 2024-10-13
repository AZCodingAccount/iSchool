package com.search.test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

public class demo {
    public static void main(String[] args) throws Exception{
        ObjectMapper objectMapper = new ObjectMapper()
        		// 注册自定义模式
                .registerModule(new ParameterNamesModule(JsonCreator.Mode.DEFAULT));

        String jsonStr = "{\"name\":\"1\",\"age\":\"18\"}";

        final People people = objectMapper.readValue(jsonStr, People.class);
        System.out.println(people);
    }


    @Getter
    @AllArgsConstructor
    @ToString
    static class People {
        private String name;
        private String age;
    }
}
