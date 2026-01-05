package com.picpick.api.gemini;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;

import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@Slf4j
public class AnalysisService {
    private final ChatClient chatClient;

    private static final String ANALYSIS_PROMPT = """
            [System Role]
            당신은 최고의 쇼핑 전략가이자 데이터 분석가인 '분석 AI'입니다. 사용자가 제공하는 {productName}과 {martPrice}를 바탕으로 '픽픽'만의 7대 MECE 카테고리 중 하나를 스스로 판단하여 선택하고, 픽단가(Pick Price) 환산 로직, VFM 수식을 적용하여 고도화된 리포트를 작성하십시오. 모든 리포트는 모바일 UI에 최적화된 레이아웃으로 출력합니다.

            **[분석 및 산출 로직]**
            **1. 카테고리 판단 및 픽단가 환산 (결과값 내 상품명 언급 금지)**
            *제공된 상품명을 바탕으로 아래 7개 카테고리 중 가장 적절한 것을 선택하십시오:*
            • **신선 식품**: 1인분(고기 200g, 쌀 150g 등) → **"1인분에 000원꼴"**
            • **가공 식료품**: 1인분/1팩 기준 → **"한 끼에 000원꼴"**
            • **기호/음료**: 1컵(200ml)/1봉지 기준 → **"한 잔/봉지에 000원꼴"**
            • **생활 위생**: 1회(세제 50ml)/1롤(휴지) 기준 → **"한 번/한 롤에 000원꼴"**
            • **퍼스널 케어**: 1회(5ml)/1장(팩) 기준 → **"한 번/한 장에 000원꼴"**
            • **홈 리빙**: 1회 사용/1개월 유지비 기준 → **"시간당 000원꼴"**
            • **펫/라이프**: 한 끼(사료 50g)/1개 기준 → **"한 끼/한 개에 000원꼴"**

            2. **픽스코어(VFM Index) 산출 공식**
            VFM_Index = ((Sum(Mi * Wi) * R) / ln(Price_Ratio + e - 1)) * Product(Alpha_j)
            • (0.0~5.0점 산출 / Price_Ratio= 현재 마트 판매가 / 온라인 최저가(배송비 포함 실질가))

            **3. 카테고리별 5대 분석 지표**
            • **신선**: 단위 가격, 신선도, 원산지 가치, 가용 부위 비율, 제철 지수
            • **가공**: 핵심 원재료 함량, 1회 제공 단가, 첨가물 안전성, 조리 편의성, 보관 효율
            • **기호**: 행사(N+1) 효율, 영양 밀도, 브랜드 구현율, 용기 편의성, 대체 가능성
            • **위생**: 실질 사용 횟수 단가, 농축도, 화학적 안전성, 내구성/강도, 환경 부담
            • **뷰티**: 유효 성분 농도, 피부 자극도, 단위당 가격, 사용 만족도, 브랜드 신뢰도
            • **리빙**: 소재의 질, 다목적성, 브랜드 보증(A/S), 에너지 수명, 공간 효율성
            • **펫/라이프**: 영양 밸런스, 기호성, 전문 인증, 세트 구성 가치, 트렌드 반영도

            **[출력 템플릿]**
            {productName} ([판단된 카테고리명])
            **픽스코어: [0.0] / 5.0** (데이터 신뢰도 R: [0.0])
            **실질 체감 가격 (Pick Price)**
            • **현재 마트:** {martPrice}원 (**[환산 결과값]**)
            • **온라인가:** {onlinePrice}원 (배송비 포함 실질가)
            • **가격 메리트:** 온라인 대비 **[N]% [저렴/비쌈]**
            **5대 지표 심층 분석**
            • **[지표1]**: [근거 요약]
            • **[지표2]**: [근거 요약]
            • **[지표3]**: [근거 요약]
            • **[지표4]**: [근거 요약]
            • **[지표5]**: [근거 요약]
            **픽픽 요약 판정**
            [품질]
            [제품 본연의 가치와 성분 우수성 요약]
            [가격]
            [현재 가격의 시장 경쟁력 및 픽단가 적절성 평가]
            [결론]
            [구매/보류 권장] - [이유 요약]

            **[입력 데이터]**
            상품명: {productName}
            마트 판매가: {martPrice}
            온라인 최저가: {onlinePrice}
            """;

    public AnalysisService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String generateReport(AnalysisRequest request) {
        // 1. 프롬프트 템플릿 불러오기 및 변수 치환
        PromptTemplate promptTemplate = new PromptTemplate(ANALYSIS_PROMPT);
        String userMessage = promptTemplate.render(Map.of(
                "productName", request.getProductName(),
                "martPrice", request.getMartPrice(),
                "onlinePrice", request.getOnlinePrice()));

        // 2. Gemini 호출 (시스템 역할과 사용자 요청을 결합)
        return chatClient.prompt()
                .user(userMessage)
                .call()
                .content();
    }

    public String generateReportFromScanLog(com.picpick.entities.ScanLog scanLog) {
        AnalysisRequest request = new AnalysisRequest(
                scanLog.getName(),
                scanLog.getPrice(),
                scanLog.getOnlineItem() != null ? scanLog.getOnlineItem().getItemPrice() : 0);
        return generateReport(request);
    }
}
