package com.picpick.user;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

    @org.mapstruct.Mapping(target = "scans", ignore = true)
    @org.mapstruct.Mapping(target = "mart", ignore = true)
    User toEntity(UserDto userDto);
}
