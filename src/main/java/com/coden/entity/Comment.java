package com.coden.entity;

import com.coden.common.MessageConstant;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 用户针对某一个文档的评论
 **/
@Data
public class Comment {

    @Id
    private String id;

    @NotNull
    private Long createUser;

    private String userId;

    private String userName;

    @NotBlank(message = "content" + MessageConstant.PARAMS_IS_NOT_NULL)
    @Size(min = 1, max = 140, message = MessageConstant.PARAMS_LENGTH_REQUIRED)
    private String content;

    @NotNull
    private String docId;

    private Date createDate;

    private Date updateDate;
}
