package com.picpick.dtos;

import lombok.Data;

@Data
public class OnlineItemRequest {
    private String naverProductId;
    private String itemBrand;
    private String name;
    private Integer price;
}
