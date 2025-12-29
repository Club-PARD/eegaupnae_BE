package com.picpick.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OnlineItemResponse {
    private Long id;
    private String naverProductId;
    private String name;
    private Integer price;
    private LocalDateTime lastUpdated;
}
