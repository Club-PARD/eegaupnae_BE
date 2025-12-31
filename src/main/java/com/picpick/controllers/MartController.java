package com.picpick.controllers;

import com.picpick.entities.Mart;
import com.picpick.services.MartRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/marts")
public class MartController {

    private final MartRegisterService martRegisterService;

    @PostMapping("/register")
    public ResponseEntity<Long> registerMart(
            @RequestPart("name") String name,
            @RequestPart("address") String address,
            @RequestPart("brn") String brn,
            @RequestPart("file") MultipartFile file
    ) throws Exception {

        Mart mart = martRegisterService.registerMartWithFile(name, address, brn, file);
        return ResponseEntity.ok(mart.getId());
    }
}
