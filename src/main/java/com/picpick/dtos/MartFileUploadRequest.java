package com.picpick.dtos;

import lombok.Data;

@Data
public class MartFileUploadRequest {
    private Long martId;
    private String fileUrl; // S3 업로드된 엑셀 파일 경로
}
