package com.coden.entity.dto;

import com.coden.entity.FileDocument;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CommentWithUserDTO extends CommentDTO {

    private String id;

    private String userName;

    private Date createDate;

    private String userId;

    private List<FileDocument> abc;

}
