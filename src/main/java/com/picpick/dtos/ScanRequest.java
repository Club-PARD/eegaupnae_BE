package com.picpick.dtos;

import lombok.Data;
/**
 * Client → Backend
 * 사용자가 상품을 찍었을 떄.
 */
@Data
public class ScanRequest {
    private String userUuid;         // maps to User.uuid
    private String name;             // OCR product name
    private Integer price;           // offline price
    private String description;      // optional
    private String pictureFileUrl;   // uploaded image URL or file path
    private Double currentLongitude;
    private Double currentLatitude;
}
