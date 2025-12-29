package com.picpick.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MartItemResponse {
    private Long id;
    private String name;
    private Integer price;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer discountPercentage;
}
