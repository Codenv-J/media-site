package com.coden.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Document
@Data
public class CateDocRelationship {

    /**
     * 主键ID
     */
    @Id
    String id;

    /**
     * 分类id
     */
    String categoryId;

    /**
     * 文件id
     */
    String fileId;

    /**
     * 创建时间
     */
    Date createDate;

    /**
     * 修改时间
     */
    Date updateDate;

}
