package com.search.patch;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.ai.chat.messages.Media;
import org.springframework.ai.chat.messages.MessageType;

import java.util.List;
import java.util.Map;

public abstract class SystemMessageMixin {
    @JsonCreator
    public SystemMessageMixin(
        @JsonProperty("messageType") MessageType messageType,
        @JsonProperty("textContent") String textContent,
        @JsonProperty("mediaData") List<Media> mediaData,
        @JsonProperty("properties") Map<String, Object> properties) {
        // 构造函数的内容不重要，因为它永远不会被调用
    }
}
