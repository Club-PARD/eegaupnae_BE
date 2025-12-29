package com.picpick.dtos;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AIAssessmentResponse {
    private Long id;
    private Long scanLogId;

    // price comparison summary
    private String priceComparison;     // e.g. "마트 3,500원 vs 온라인 3,200원, 9% 비쌈"
    private Integer unitPricePer100g;   // 100g당 가격
    private Integer unitPricePerUse;    // 1회당 가격

    private Double score;              // 0.0 ~ 5.0
    private Integer priceDiffPercent;  // 온라인 대비 %

    private String qualityComment;     // [품질]
    private String priceComment;       // [가격]
    private String conclusionComment;  // [결론]

    // alternatives rendered as objects in API
    private List<AlternativeItemDto> alternatives;

    private LocalDateTime createdAt;
}
