package com.coden.entity.dto;

import com.coden.common.MessageConstant;
import com.coden.enums.FilterTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class RelationDTO {

    @NotNull(message = MessageConstant.PARAMS_IS_NOT_NULL)
    private String docId;

    @NotNull(message = MessageConstant.PARAMS_IS_NOT_NULL)
    private FilterTypeEnum type;

    private String id;


}
