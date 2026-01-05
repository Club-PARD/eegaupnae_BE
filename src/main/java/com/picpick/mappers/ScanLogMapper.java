package com.picpick.mappers;

import com.picpick.dtos.ScanLogResponse;
import com.picpick.entities.ScanLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.picpick.api.gemini.AnalysisRequest;

@Mapper(componentModel = "spring")
public interface ScanLogMapper {

    @Mapping(target = "productName", source = "name")
    @Mapping(target = "martId", source = "mart.id")
    @Mapping(target = "martName", source = "mart.name")
    @Mapping(target = "onlinePrice", source = "onlineItem.itemPrice")
    ScanLogResponse toDto(ScanLog scanLog);

    @Mapping(target = "productName", source = "name")
    @Mapping(target = "martPrice", source = "price")
    @Mapping(target = "onlinePrice", source = "onlineItem.itemPrice", defaultValue = "0")
    AnalysisRequest toAnalysisRequest(ScanLog scanLog);
}
