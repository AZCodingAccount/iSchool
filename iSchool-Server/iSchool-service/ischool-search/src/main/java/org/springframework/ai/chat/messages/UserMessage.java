/*
 * Copyright 2023 - 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.ai.chat.messages;

import java.util.Arrays;
import java.util.List;

import org.springframework.core.io.Resource;

/**
 * A message of the type 'user' passed as input Messages with the user role are from the
 * end-user or developer. They represent questions, prompts, or any input that you want
 * the generative to respond to.
 */
public class UserMessage extends AbstractMessage {

    public UserMessage() {
        super(MessageType.USER);
    }

    public UserMessage(String message) {
        super(MessageType.USER, message);
    }

    public UserMessage(Resource resource) {
        super(MessageType.USER, resource);
    }

    public UserMessage(String textContent, List<Media> mediaList) {
        super(MessageType.USER, textContent, mediaList);
    }

    public UserMessage(String textContent, Media... media) {
        this(textContent, Arrays.asList(media));
    }

    @Override
    public String toString() {
        return "UserMessage{" + "content='" + getContent() + '\'' + ", properties=" + properties + ", messageType="
                + messageType + '}';
    }

}
