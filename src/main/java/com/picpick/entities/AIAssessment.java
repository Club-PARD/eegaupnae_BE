package com.picpick.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "ai_assessment")
public class AIAssessment {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scan_log_id", nullable = false)
    private ScanLog scanLog;

    // "마트 3,500원 / 온라인 3,200원, 12% 비쌈" 같은 한 줄 요약
    @Column(name = "price_compare")
    private String priceCompare;

    @Column(name = "unit_price_per_unit")
    private Integer unitPricePerUnit;

    @Column(name = "unit_price_per_use")
    private Integer unitPricePerUse;

    // 0.0 ~ 5.0 (0.5 step)
    @Column(precision = 2, scale = 1)
    private Double score;

    @Column(name = "price_diff_percent")
    private Integer priceDiffPercent;

    @Column(name = "quality_comment")
    private String qualityComment;

    @Column(name = "price_comment")
    private String priceComment;

    @Column(name = "conclusion_comment")
    private String conclusionComment;

    // JSON 문자열로 저장 (DB 타입은 TEXT/JSON)
    @Column(columnDefinition = "TEXT")
    private String alternatives;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
