package com.picpick.api.aws;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/s3")
public class S3Controller {
    private final S3Service s3Service;

    // 단일 파일 업로드 (excel, pdf 다 가능)
    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestPart("file") MultipartFile file) throws Exception {
        String key = s3Service.uploadFile(file, "uploads");
        return ResponseEntity.ok(key);
    }

    // 파일 삭제
    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam("key") String key) {
        s3Service.deleteFile(key);
        return ResponseEntity.noContent().build();
    }
}
