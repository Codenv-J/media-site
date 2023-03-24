package com.coden.entity.dto;

import com.coden.auth.PermissionEnum;
import com.coden.common.MessageConstant;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * TODO
 **/
@Data
public class UserRoleDTO {

    @NotNull(message = MessageConstant.PARAMS_IS_NOT_NULL)
    private String userId;

    @NotNull(message = MessageConstant.PARAMS_IS_NOT_NULL)
    private PermissionEnum role;
}
