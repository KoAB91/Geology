package com.KoAB91.geology.service;

import com.KoAB91.geology.tasks.FileExportingTask;
import com.KoAB91.geology.tasks.FileImportingTask;
import com.KoAB91.geology.enums.FileType;
import com.KoAB91.geology.enums.ProcessStatus;
import com.KoAB91.geology.entity.ExcelFile;
import com.KoAB91.geology.repository.ExcelFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class ExcelFileService {

    @Autowired
    ExcelFileRepository excelFileRepository;
    @Autowired
    GeoClassService geoClassService;
    @Autowired
    SectionService sectionService;

    @Autowired
    private TaskExecutor taskExecutor;

    public ExcelFile get(Integer fileId) {
        return getExcelFile(fileId);
    }

    public String getStatus(Integer fileId) {
        ExcelFile excelFile = getExcelFile(fileId);
        if (excelFile == null) {
            return "File not found.";
        }
        return convertStatusToResponse(excelFile.getProcessingStatus());
    }

    public ExcelFile save(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        ExcelFile excelFile = new ExcelFile(fileName, file.getContentType(), file.getBytes(), FileType.IMPORT);
        excelFile = excelFileRepository.save(excelFile);

        FileImportingTask importTaskAsync = new FileImportingTask(excelFile, excelFileRepository, sectionService, geoClassService);
        taskExecutor.execute(importTaskAsync);

        return excelFile;
    }

    public Integer initExport() {
        ExcelFile excelFile = new ExcelFile("all-sections.xls", "application/vnd.ms-excel", null, FileType.EXPORT);
        excelFile = excelFileRepository.save(excelFile);

        FileExportingTask exportTaskAsync = new FileExportingTask(excelFile, excelFileRepository, sectionService);
        taskExecutor.execute(exportTaskAsync);

        return excelFile.getId();
    }

    private ExcelFile getExcelFile(Integer fileId) {
        Optional<ExcelFile> excelFile = excelFileRepository.findById(fileId);
        return excelFile.orElse(null);
    }

    private String convertStatusToResponse(ProcessStatus status) {
        String statusToResponse = "";
        if (status == null) statusToResponse = "File is not being processed";
        else if (status == ProcessStatus.DONE) statusToResponse = "DONE";
        else if (status == ProcessStatus.IN_PROGRESS) statusToResponse = "IN PROGRESS";
        else if (status == ProcessStatus.ERROR) statusToResponse = "ERROR";
        return statusToResponse;
    }

}
