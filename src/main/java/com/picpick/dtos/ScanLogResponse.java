package com.picpick.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScanLogResponse {
    private Long id;
    private String productName;
    private Integer price;
    private String description;
    private String pictureFileUrl;
    private Double currentLongitude;
    private Double currentLatitude;
    private LocalDateTime scannedAt;
    private Long martId;
    private String martName;
}
