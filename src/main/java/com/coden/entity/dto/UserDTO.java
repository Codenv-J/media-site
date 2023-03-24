package com.coden.entity.dto;

import lombok.Data;

/**
 * TODO
 **/
@Data
public class UserDTO {

    private  String id;

    private String password;

    private String phone;

    private String mail;

    private boolean male = false;

    private String description;

}
