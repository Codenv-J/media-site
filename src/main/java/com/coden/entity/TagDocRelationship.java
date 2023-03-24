package com.coden.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;


@Data
public class TagDocRelationship {

    /**
     * id
     */
    @Id
    private String id;

    /**
     * 分类id
     */
    private String tagId;

    /**
     * 文件id
     */
    private String fileId;

    /**
     * create date
     */
    private Date createDate;

    /**
     * update date
     */
    private Date updateDate;

}
