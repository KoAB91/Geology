package com.KoAB91.geology.tasks;

import com.KoAB91.geology.entity.ExcelFile;
import com.KoAB91.geology.enums.ProcessStatus;
import com.KoAB91.geology.repository.ExcelFileRepository;
import com.KoAB91.geology.service.GeoClassService;
import com.KoAB91.geology.service.SectionService;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class FileImportingTask implements Runnable {

    private SectionService sectionService;
    private GeoClassService geoClassService;
    private ExcelFileRepository excelFileRepository;
    private ExcelFile excelFile;


    public FileImportingTask(ExcelFile excelFile, ExcelFileRepository excelFileRepository, SectionService sectionService, GeoClassService geoClassService) {
        this.excelFile = excelFile;
        this.excelFileRepository = excelFileRepository;
        this.sectionService = sectionService;
        this.geoClassService = geoClassService;
    }

    @Override
    public void run() {
        excelFile.setProcessingStatus(ProcessStatus.IN_PROGRESS);
        excelFileRepository.save(excelFile);

        ProcessStatus status = importFile();
        excelFile.setProcessingStatus(status);

        excelFileRepository.save(excelFile);
    }

    private ProcessStatus importFile() {
        ProcessStatus status;

        MultipartFile file = new MockMultipartFile(excelFile.getFileName(), excelFile.getFileName(), excelFile.getFileFormat(), excelFile.getData());
        try (HSSFWorkbook myExcelBook = new HSSFWorkbook(file.getInputStream())) {

            HSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);
            Iterator<Row> rowIterator = myExcelSheet.iterator();

            // Skip table header
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                Iterator<Cell> cellIterator = row.cellIterator();
                if (!cellIterator.hasNext()) {
                    continue;
                }

                Cell cell = cellIterator.next();

                String sectionName = cell.getStringCellValue();
                if (sectionName.isEmpty()) {
                    continue;
                }

                Map<String, String> geoClassNameCodePair = new HashMap<>();
                // Take name & code at once
                while (cellIterator.hasNext()) {
                    cell = cellIterator.next();

                    String className = cell.getStringCellValue();
                    if (className.isEmpty()) {
                        break;
                    }

                    if (!cellIterator.hasNext()) {
                        break;
                    }

                    cell = cellIterator.next();
                    String classCode = cell.getStringCellValue();
                    if (classCode.isEmpty()) {
                        break;
                    }

                    geoClassNameCodePair.put(className, classCode);
                }

                sectionService.create(sectionName);
                for (Map.Entry<String, String> geoClassPair : geoClassNameCodePair.entrySet()) {
                    String geoClassName = geoClassPair.getKey();
                    String geoClassCode = geoClassPair.getValue();
                    geoClassService.create(geoClassName, geoClassCode);
                    sectionService.addGeoClass(sectionName, geoClassName);
                }
            }

            status = ProcessStatus.DONE;

        } catch (Exception e) {
            status = ProcessStatus.ERROR;
        }

        return status;
    }
}
