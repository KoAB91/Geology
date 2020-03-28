package com.KoAB91.geology.entity;

import com.KoAB91.geology.enums.FileType;
import com.KoAB91.geology.enums.ProcessStatus;

import javax.persistence.*;

@Entity
@Table(name = "excel_files")
public class ExcelFile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String fileName;

    @Column(name = "format")
    private String fileFormat;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private FileType fileType;

    @Lob
    private byte[] data;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ProcessStatus status;



    public ExcelFile(){}

    public ExcelFile(String fileName, String fileFormat, byte[] data, FileType fileType) {
        this.fileName = fileName;
        this.fileFormat = fileFormat;
        this.data = data;
        this.fileType = fileType;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileType) {
        this.fileFormat = fileType;
    }

    public ProcessStatus getProcessingStatus() {
        return status;
    }

    public void setProcessingStatus(ProcessStatus status) {
        this.status = status;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }
}
