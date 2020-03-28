package com.KoAB91.geology.tasks;

import com.KoAB91.geology.dto.GeoClassDTO;
import com.KoAB91.geology.dto.SectionDTO;
import com.KoAB91.geology.entity.ExcelFile;
import com.KoAB91.geology.enums.ProcessStatus;
import com.KoAB91.geology.repository.ExcelFileRepository;
import com.KoAB91.geology.service.SectionService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


public class FileExportingTask implements Runnable {

    private SectionService sectionService;
    private ExcelFileRepository excelFileRepository;
    private ExcelFile excelFile;


    public FileExportingTask(ExcelFile excelFile, ExcelFileRepository excelFileRepository, SectionService sectionService) {
        this.excelFile = excelFile;
        this.excelFileRepository = excelFileRepository;
        this.sectionService = sectionService;
    }

    @Override
    public void run() {
        excelFile.setProcessingStatus(ProcessStatus.IN_PROGRESS);
        excelFileRepository.save(excelFile);
        int exportResult = exportFile();
        if (exportResult == 1) {
            excelFile.setProcessingStatus(ProcessStatus.DONE);
        } else {
            excelFile.setProcessingStatus(ProcessStatus.ERROR);
        }
        excelFileRepository.save(excelFile);
    }

    private int exportFile() {
        int status;
        ByteArrayOutputStream bos = null;
        HSSFWorkbook book = new HSSFWorkbook();
        Sheet sheet = book.createSheet("Sections");

        // create table header
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Section name");
        header.createCell(1).setCellValue("Class 1 name");
        header.createCell(2).setCellValue("Class 1 code");
        header.createCell(3).setCellValue("Class 2 name");
        header.createCell(4).setCellValue("Class 2 code");

        try {
            List<SectionDTO> sectionsDTO = sectionService.get();
            if (sectionsDTO == null || sectionsDTO.isEmpty()) {
                status = 1;
                return status;
            }
            int rowCounter = 1;
            for (SectionDTO sectionDTO : sectionsDTO) {
                Row row = sheet.createRow(rowCounter);
                Cell cell = row.createCell(0);
                cell.setCellValue(sectionDTO.getName());

                List<GeoClassDTO> geoClassesDTO = sectionDTO.getGeologicalClasses();
                if (geoClassesDTO == null || geoClassesDTO.isEmpty()) {
                    continue;
                }
                int cellCounter = 1;
                for (GeoClassDTO geoClassDTO : geoClassesDTO) {
                    cell = row.createCell(cellCounter);
                    cell.setCellValue(geoClassDTO.getName());
                    cellCounter++;
                    cell = row.createCell(cellCounter);
                    cell.setCellValue(geoClassDTO.getCode());
                    cellCounter++;
                }

                rowCounter++;
            }
            bos = new ByteArrayOutputStream();
            book.write(bos);
            byte[] fileData = bos.toByteArray();
            excelFile.setData(fileData);
            excelFileRepository.save(excelFile);
            status = 1;
        } catch (Exception e) {
            status = 0;
        } finally {
            try {
                book.close();
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return status;
    }
}
