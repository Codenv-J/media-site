package com.coden.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.coden.auth.PermissionEnum;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * TODO
 **/
@Data
public class UserVO {

    @Id
    private String id;

    @NotBlank(message = "非空")
    private String username;

    private String phone;

    private String mail;

    private Boolean male = false;

    private String description;

    private String avatar;

    // 封禁状态
    private Boolean banning = false;

    private PermissionEnum permissionEnum;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateDate;

}
