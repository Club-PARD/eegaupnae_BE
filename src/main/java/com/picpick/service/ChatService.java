package com.picpick.service;

import com.picpick.dto.ChatRequest;
import com.picpick.dto.ChatResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ChatService {

    private final ChatClient chatClient;

    public ChatService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public ChatResponse chat(ChatRequest request) {
        String response = chatClient.prompt()
                .user(request.getMessage())
                .call()
                .content();
        return new ChatResponse(response);
    }

    public Flux<String> chatStream(ChatRequest request) {
        return chatClient.prompt()
                .user(request.getMessage())
                .stream()
                .content();
    }
}
