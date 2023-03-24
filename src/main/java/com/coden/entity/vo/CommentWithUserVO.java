package com.coden.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.coden.entity.dto.CommentDTO;
import lombok.Data;

import java.util.Date;

/**
 * TODO
 **/
@Data
public class CommentWithUserVO extends CommentDTO {

    private String id;

    private String userName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    private String userId;

    private String docId;

    private String docName;
}
