package com.coden.entity.dto;

import com.coden.common.MessageConstant;
import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class CollectDTO {

    @NotNull(message = MessageConstant.PARAMS_IS_NOT_NULL)
    private String docId;
}
