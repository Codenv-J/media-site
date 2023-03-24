package com.coden.entity.dto;

import com.coden.common.MessageConstant;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class BasePageDTO {

    /**
     * 页数
     */
    @NotNull(message = MessageConstant.PARAMS_IS_NOT_NULL)
    @Min(value = 1, message = MessageConstant.PARAMS_FORMAT_ERROR)
    protected Integer page;

    /**
     * 每页条数
     */
    @NotNull(message = MessageConstant.PARAMS_IS_NOT_NULL)
    @Min(value = 1, message = MessageConstant.PARAMS_FORMAT_ERROR)
    @Max(value = 100, message = MessageConstant.PARAMS_FORMAT_ERROR)
    protected Integer rows;
}
