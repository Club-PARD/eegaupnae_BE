package com.picpick.api.gemini;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@Service
public class ChatService {

    private final ChatClient chatClient;

    public ChatService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public ChatResponse chat(ChatRequest request) {
        log.info("Received chat request: {}", request.getMessage());
        // Build a prompt, set the user message, call the AI model, and get the content
        String response = chatClient.prompt()
                .user(request.getMessage())
                .call()
                .content();
        return new ChatResponse(response);
    }

    public Flux<String> chatStream(ChatRequest request) {
        log.info("Received streaming chat request: {}", request.getMessage());
        // Build a prompt, set the user message, and stream the content
        return chatClient.prompt()
                .user(request.getMessage())
                .stream()
                .content();
    }
}
