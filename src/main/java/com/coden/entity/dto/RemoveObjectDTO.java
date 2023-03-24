package com.coden.entity.dto;

import lombok.Data;


@Data
public class RemoveObjectDTO {

    /**
     * request body 请求对象中只具有单一参数id
     */
    private String id;

}
