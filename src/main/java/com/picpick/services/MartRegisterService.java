package com.picpick.services;

import com.picpick.api.aws.S3Service;
import com.picpick.entities.Mart;
import com.picpick.entities.MartItem;
import com.picpick.repositories.MartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MartRegisterService {
    private final S3Service s3Service;
    private final MartRepository martRepository;

    @Transactional
    public Mart registerMartWithFile(String name, String address, String brn, MultipartFile file) throws Exception {
        log.info("Starting mart registration for: {}, BRN: {}", name, brn);

        // 1. Extract file data to match S3Service.uploadFile signature
        byte[] fileBytes = file.getBytes();

        // 2. Call S3Service with the correct arguments
        String s3Key = s3Service.uploadFile(file, "mart-documents");

        // 3. Create Mart Entity
        Mart mart = Mart.builder()
                .name(name)
                .address(address)
                .brn(brn)
                .documentFile(s3Key)
                .build();

        // 4. Parse Excel using the same bytes (prevents stream closed error)
        try (InputStream is = new ByteArrayInputStream(fileBytes)) {
            List<MartItem> items = parseExcel(is, mart);
            mart.setMartItems(items);
            log.info("Parsed {} items from Excel file", items.size());
        }

        // 5. Save (Cascade saves items automatically)
        Mart savedMart = martRepository.save(mart);
        log.info("Successfully registered mart with ID: {}", savedMart.getId());
        return savedMart;
    }

    private List<MartItem> parseExcel(InputStream is, Mart mart) throws Exception {
        List<MartItem> items = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();

        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || row.getCell(0) == null)
                    continue;

                String itemName = formatter.formatCellValue(row.getCell(0)).trim();
                if (itemName.isEmpty())
                    continue;

                // Price (Column B)
                int price = 0;
                Cell priceCell = row.getCell(1);
                if (priceCell != null && priceCell.getCellType() == CellType.NUMERIC) {
                    price = (int) priceCell.getNumericCellValue();
                }

                // Start Date (Column C) & End Date (Column D)
                LocalDate startDate = LocalDate.now();
                LocalDate endDate = LocalDate.now().plusMonths(1);

                Cell startCell = row.getCell(2);
                if (startCell != null && startCell.getCellType() == CellType.NUMERIC
                        && DateUtil.isCellDateFormatted(startCell)) {
                    startDate = startCell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }

                Cell endCell = row.getCell(3);
                if (endCell != null && endCell.getCellType() == CellType.NUMERIC
                        && DateUtil.isCellDateFormatted(endCell)) {
                    endDate = endCell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }

                items.add(MartItem.builder()
                        .itemName(itemName)
                        .itemPrice(price)
                        .startDate(startDate)
                        .endDate(endDate)
                        .mart(mart)
                        .build());
            }
        }
        return items;
    }
}