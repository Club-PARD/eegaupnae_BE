package com.picpick.dtos;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
/**
 * 결과 페이지
 */
@Data
public class ScanDetailResponse {
    // product & prices
    private String productName;
    private Integer offlinePrice;
    private Integer onlinePrice;

    // mart info
    private Long martId;
    private String martName;
    private String martAddress;

    // location / time
    private Double currentLongitude;
    private Double currentLatitude;
    private LocalDateTime scannedAt;

    // AI assessment
    private Double score;
    private String priceComparison;
    private Integer priceDiffPercent;
    private Integer unitPricePer100g;
    private Integer unitPricePerUse;
    private String qualityComment;
    private String priceComment;
    private String conclusionComment;
    private List<AlternativeItemDto> alternatives;
}
