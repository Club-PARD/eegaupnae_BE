package com.picpick.mappers;

import com.picpick.dtos.MartItemResponse;
import com.picpick.dtos.MartResponse;
import com.picpick.entities.Mart;
import com.picpick.entities.MartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MartMapper {
    @Mapping(target = "items", source = "martItems")
    MartResponse toDto(Mart mart);

    @Mapping(target = "name", source = "itemName")
    @Mapping(target = "price", source = "itemPrice")
    MartItemResponse toDto(MartItem martItem);
}
