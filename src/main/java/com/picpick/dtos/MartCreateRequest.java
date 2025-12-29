package com.picpick.dtos;

import lombok.Data;

@Data
public class MartCreateRequest {
    private String name;
    private String address;
    private Double longitude;
    private Double latitude;
    private String excelFile;
}
