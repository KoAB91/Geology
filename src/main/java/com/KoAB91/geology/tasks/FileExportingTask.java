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

        ProcessStatus status = exportFile();
        excelFile.setProcessingStatus(status);
        excelFileRepository.save(excelFile);
    }

    private ProcessStatus exportFile() {
        ProcessStatus status;

        ByteArrayOutputStream outputStream = null;

        HSSFWorkbook book = new HSSFWorkbook();
        Sheet sheet = book.createSheet("Sections");

        // create table header
        int rowIndex = 0;
        Row header = sheet.createRow(rowIndex++);
        String[] cellNames = { "Section name", "Class 1 name", "Class 1 code", "Class 2 name", "Class 2 code" };
        for (int i = 0; i < cellNames.length; i++) {
            String cellName = cellNames[i];
            header.createCell(i).setCellValue(cellName);
        }

        try {
            List<SectionDTO> sectionsDTO = sectionService.get();
            if (sectionsDTO == null || sectionsDTO.isEmpty()) {
                status = ProcessStatus.DONE;
                return status;
            }

            for (SectionDTO sectionDTO : sectionsDTO) {
                Row row = sheet.createRow(rowIndex++);

                int cellIndex = 0;
                Cell cell = row.createCell(cellIndex++);
                cell.setCellValue(sectionDTO.getName());

                List<GeoClassDTO> geoClassesDTO = sectionDTO.getGeologicalClasses();
                if (geoClassesDTO == null || geoClassesDTO.isEmpty()) {
                    continue;
                }

                for (GeoClassDTO geoClassDTO : geoClassesDTO) {
                    cell = row.createCell(cellIndex++);
                    cell.setCellValue(geoClassDTO.getName());
                    cell = row.createCell(cellIndex++);
                    cell.setCellValue(geoClassDTO.getCode());
                }
            }

            outputStream = new ByteArrayOutputStream();
            book.write(outputStream);
            byte[] fileData = outputStream.toByteArray();

            excelFile.setData(fileData);
            excelFileRepository.save(excelFile);

            status = ProcessStatus.DONE;
        } catch (Exception e) {
            status = ProcessStatus.ERROR;
        } finally {
            try {
                book.close();
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return status;
    }
}
