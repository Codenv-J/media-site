package com.coden.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * 用户收藏文档的关系表
 **/
@Data
public class CollectDocRelationship {

    @Id
    private String id;

    private String userId;

    private String docId;

    private Date createDate;

    private Date updateDate;

}
