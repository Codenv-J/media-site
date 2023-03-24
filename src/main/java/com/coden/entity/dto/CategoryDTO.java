package com.coden.entity.dto;

import com.coden.common.MessageConstant;
import com.coden.enums.FilterTypeEnum;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;




@Data
public class CategoryDTO {

    @NotNull(message = MessageConstant.PARAMS_IS_NOT_NULL)
    @Length(max = 64, message = MessageConstant.PARAMS_LENGTH_REQUIRED)
    private String name;

    @NotNull(message = MessageConstant.PARAMS_IS_NOT_NULL)
    private FilterTypeEnum type;


    private String id;

}
