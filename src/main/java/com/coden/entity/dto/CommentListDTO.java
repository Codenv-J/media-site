package com.coden.entity.dto;

import com.coden.common.MessageConstant;
import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class CommentListDTO {

    @NotNull(message = MessageConstant.PARAMS_IS_NOT_NULL)
    private String docId;

    @NotNull(message = MessageConstant.PARAMS_IS_NOT_NULL)
    private Integer page;

    @NotNull(message = MessageConstant.PARAMS_IS_NOT_NULL)
    private Integer rows;

}
