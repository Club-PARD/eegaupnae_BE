package com.picpick.mart;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MartMapper {
    MartDto toDto(Mart mart);

    MartMapInfo toMapInfo(Mart mart);

    @org.mapstruct.Mapping(target = "id", ignore = true)
    @org.mapstruct.Mapping(target = "documentFile", ignore = true)
    @org.mapstruct.Mapping(target = "longitude", ignore = true)
    @org.mapstruct.Mapping(target = "latitude", ignore = true)
    @org.mapstruct.Mapping(target = "martItems", ignore = true)
    Mart toEntity(SignupRequest request);
}
