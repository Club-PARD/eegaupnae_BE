package com.picpick.controllers;

import com.picpick.dtos.ScanLogRequest;
import com.picpick.dtos.ScanLogResponse;
import com.picpick.entities.ScanLog;
import com.picpick.mappers.ScanLogMapper;
import com.picpick.services.ScanLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/scanlogs")
public class ScanLogController {

    private final ScanLogService scanLogService;
    private final ScanLogMapper scanLogMapper;

    @PostMapping
    public ResponseEntity<ScanLogResponse> createScanLog(@RequestBody ScanLogRequest request) {
        ScanLog savedLog = scanLogService.saveScanLog(request);
        ScanLogResponse response = scanLogMapper.toDto(savedLog);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ScanLogResponse>> getScanLogs() {
        List<ScanLogResponse> responses = scanLogService.getAllScanLogs().stream()
                .map(scanLogMapper::toDto)
                .toList();
        return ResponseEntity.ok(responses);
    }
}