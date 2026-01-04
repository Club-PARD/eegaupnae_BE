package com.picpick.api.gemini;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AnalysisService {
    private final ChatClient chatClient;

    public AnalysisService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public ChatResponse analyzeProduct(ProductRequest req) {
        // 1. Validation
        if (req.martPrice() <= 0 || req.onlinePrice() <= 0) {
            log.warn("Invalid prices received for product: {}. Mart: {}, Online: {}",
                    req.productName(), req.martPrice(), req.onlinePrice());
            return new ChatResponse("상품 가격 정보가 부정확하여 분석을 진행할 수 없습니다.");
        }

        // 2. Format the Prompt (Only 3 inputs)
        String userPrompt = String.format("""
                분석을 시작해 주세요.
                - 상품명: %s
                - 마트가: %d원
                - 온라인가: %d원
                """,
                req.productName(), req.martPrice(), req.onlinePrice());

        // 3. Call Gemini
        String response = chatClient.prompt()
                .system("""
                        당신은 최고의 쇼핑 전략가이자 데이터 분석가인 '분석 AI'입니다. 사용자가 제공하는 [상품명]과 [마트 판매가]를 바탕으로 '픽픽'만의 7대 MECE 카테고리와 픽단가(Pick Price) 환산 로직, VFM 수식을 적용하여 고도화된 리포트를 작성하십시오. 모든 리포트는 모바일 UI에 최적화된 레이아웃으로 출력합니다.
                        """)
                .user(userPrompt)
                .call()
                .content();

        return new ChatResponse(response);
    }
}
