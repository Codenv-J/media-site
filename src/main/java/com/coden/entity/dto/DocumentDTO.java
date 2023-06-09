package com.coden.entity.dto;

import com.coden.common.MessageConstant;
import com.coden.enums.FilterTypeEnum;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Data
public class DocumentDTO {

    @NotNull(message = MessageConstant.PARAMS_IS_NOT_NULL)
    private FilterTypeEnum type;

    @NotNull(message = MessageConstant.PARAMS_IS_NOT_NULL)
    private String filterWord;

    @NotNull(message = MessageConstant.PARAMS_IS_NOT_NULL)
    @Min(value = 1, message = MessageConstant.PARAMS_LENGTH_REQUIRED)
    private Integer page;

    @NotNull(message = MessageConstant.PARAMS_IS_NOT_NULL)
    @Min(value = 1, message = MessageConstant.PARAMS_LENGTH_REQUIRED)
    private Integer rows;

    @NotNull(message = MessageConstant.PARAMS_IS_NOT_NULL)
    private String categoryId;

    @NotNull(message = MessageConstant.PARAMS_IS_NOT_NULL)
    private String tagId;

    private String userId;
}
