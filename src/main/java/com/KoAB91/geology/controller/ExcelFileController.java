package com.KoAB91.geology.controller;

import com.KoAB91.geology.entity.ExcelFile;
import com.KoAB91.geology.service.ExcelFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/files")
public class ExcelFileController {

    @Autowired
    private ExcelFileService excelFileService;

    // import

    @PostMapping("/import")
    @ResponseBody
    public Integer uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        ExcelFile excelFile = excelFileService.save(file);
        return excelFile.getId();
    }

    @GetMapping("/import/{id}")
    @ResponseBody
    public String getImportStatus(@PathVariable Integer id) {
        return excelFileService.getStatus(id);
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
    public String getExportStatus(@PathVariable Integer id) {
        return excelFileService.getStatus(id);
    }


    @GetMapping("/export/{id}/file")
    public ResponseEntity<Resource> downloadFile(@PathVariable Integer id) {
        ExcelFile excelFile = excelFileService.get(id);
        if (excelFile == null) {
            return ResponseEntity
                    .notFound()
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
