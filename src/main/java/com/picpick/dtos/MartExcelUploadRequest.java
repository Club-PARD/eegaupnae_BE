package com.picpick.dtos;

import lombok.Data;

@Data
public class MartExcelUploadRequest {
    private Long martId;
    private String excelFileUrl; // S3나 서버에 업로드된 엑셀 파일 경로
}
