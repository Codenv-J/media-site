package com.coden.entity;

import com.coden.service.impl.DocLogServiceImpl;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * TODO
 **/
@Data
public class DocLog {

    @Id
    private String id;

    private String userId;

    private String userName;

    private DocLogServiceImpl.Action action;

    private String docId;

    private String docName;

    private Date createDate;

    private Date updateDate;
}
