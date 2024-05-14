package org.springframework.ai.chat.messages;

import java.util.Map;

/**
 * Lets the generative know the content was generated as a response to the user. This role
 * indicates messages that the generative has previously generated in the conversation. By
 * including assistant messages in the series, you provide context to the generative about
 * prior exchanges in the conversation.
 */
public class AssistantMessage extends AbstractMessage {


    public AssistantMessage() {
        super(MessageType.ASSISTANT);
    }

    public AssistantMessage(String content) {
        super(MessageType.ASSISTANT, content);
    }

    public AssistantMessage(String content, Map<String, Object> properties) {
        super(MessageType.ASSISTANT, content, properties);
    }

    @Override
    public String toString() {
        return "AssistantMessage{" + "content='" + getContent() + '\'' + ", properties=" + properties + ", messageType="
                + messageType + '}';
    }

}