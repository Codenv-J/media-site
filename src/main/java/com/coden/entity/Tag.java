package com.coden.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import java.util.Date;



@Data
public class Tag {

    @Id
    private String id;

    @NotBlank(message = "")
    private String name;

    private Date createDate;

    private Date updateDate;

}
