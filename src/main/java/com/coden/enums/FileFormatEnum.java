package com.coden.enums;

import com.coden.util.converter.BaseEnum;


public enum FileFormatEnum implements BaseEnum {

    /**
     * PDF document
     */
    PDF(1, "pdf"),

    /**
     * doc
     */
    DOC(4, "doc"),

    /**
     * docx
     */
    DOCX(5, "docx"),


    /**
     * png
     */
    PNG(9, "png", "png_", "image/png"),

    /**
     * jpeg
     */
    JPEG(10, "JPEG"),

    TEXT(11,"txt","txt_","text/plain");


    private Integer code;

    private String description;

    private String filePrefix;

    private String contentType;

    FileFormatEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    FileFormatEnum(int code, String description, String filePrefix, String contentType) {
        this(code, description);
        this.filePrefix = filePrefix;
        this.contentType = contentType;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }

    public String getFilePrefix() {
        return this.filePrefix;
    }

    public String getContentType() {
        return this.contentType;
    }
}
