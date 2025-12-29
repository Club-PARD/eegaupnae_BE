package com.picpick.dtos;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String uuid; // 프론트에서 받은 UUID
}
