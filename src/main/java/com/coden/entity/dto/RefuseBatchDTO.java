package com.coden.entity.dto;

import com.coden.common.MessageConstant;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 TODO
 **/
@Data
public class RefuseBatchDTO extends BatchIdDTO{

    @NotNull(message = MessageConstant.PARAMS_IS_NOT_NULL)
    @Size(min = 1, max = 128, message = MessageConstant.PARAMS_LENGTH_REQUIRED)
    protected String reason;
}
