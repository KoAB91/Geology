package com.KoAB91.geology.controller;

import com.KoAB91.geology.entity.ExcelFile;
import com.KoAB91.geology.enums.ProcessStatus;
import com.KoAB91.geology.service.ExcelFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/files")
public class ExcelFileController {

    @Autowired
    private ExcelFileService excelFileService;

    // import

    @PostMapping("/import")
    @ResponseBody
    public ResponseEntity<Integer> uploadFile(@RequestParam("file") MultipartFile file) {
        ExcelFile excelFile = excelFileService.save(file);
        if (excelFile == null) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
        return new ResponseEntity<>(excelFile.getId(), HttpStatus.OK);
    }

    @GetMapping("/import/{id}")
    @ResponseBody
    public ResponseEntity<String> getImportStatus(@PathVariable Integer id) {
        String status = excelFileService.getStatus(id);
        if (status == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    // export

    @GetMapping("/export")
    @ResponseBody
    public Integer getExportedFileId() {
        Integer fileId = excelFileService.initExport();
        return fileId;
    }

    @GetMapping("/export/{id}")
    @ResponseBody
    public ResponseEntity<String> getExportStatus(@PathVariable Integer id) {
        String status = excelFileService.getStatus(id);
        if (status == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
        return new ResponseEntity<>(status, HttpStatus.OK);
    }


    @GetMapping("/export/{id}/file")
    public ResponseEntity<Resource> downloadFile(@PathVariable Integer id) {
        ExcelFile excelFile = excelFileService.get(id);
        if (excelFile == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        ProcessStatus processingStatus = excelFile.getProcessingStatus();
        if (processingStatus == ProcessStatus.IN_PROGRESS) {
            return ResponseEntity
                    .status(HttpStatus.PROCESSING)
                    .build();
        }

        if (processingStatus == ProcessStatus.ERROR) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }

        MediaType contentType = MediaType.parseMediaType(excelFile.getFileFormat());
        String contentAttachment = "attachment; filename=\"" + excelFile.getFileName() + "\"";
        ByteArrayResource contentBody = new ByteArrayResource(excelFile.getData());

        return ResponseEntity.ok()
                .contentType(contentType)
                .header(HttpHeaders.CONTENT_DISPOSITION, contentAttachment)
                .body(contentBody);
    }

}
