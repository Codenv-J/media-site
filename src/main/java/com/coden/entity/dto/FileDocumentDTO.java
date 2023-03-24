package com.coden.entity.dto;

import lombok.Data;

import java.util.Date;

/**
 * TODO
 **/
@Data
public class FileDocumentDTO {

    private String id;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件大小
     */
    private long size;

    /**
     * 上传时间
     */
    private Date uploadDate;

    /**
     * 预览图的GridFS的ID
     */
    private String thumbId;


    // true 正在审核；false 审核完毕
    private boolean reviewing = true;

}
