package com.coden.service;

import com.coden.auth.PermissionEnum;
import com.coden.entity.User;
import com.coden.entity.dto.BasePageDTO;
import com.coden.entity.dto.RegistryUserDTO;
import com.coden.entity.dto.UserDTO;
import com.coden.entity.dto.UserRoleDTO;
import com.coden.util.BaseApiResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface IUserService {

    void initFirstUser();

    BaseApiResult login(RegistryUserDTO userDTO);

    BaseApiResult registry(RegistryUserDTO userDTO);

    /**
     * @Description 查询用户列表
     * @Param [pageDTO]
     * @return BaseApiResult
     **/
    BaseApiResult getUserList(BasePageDTO pageDTO);

    BaseApiResult changeUserRole(UserRoleDTO userRoleDTO);

    /**
     * @Description 屏蔽掉某个用户
     * @Param [userId]
     * @return BaseApiResult
     **/
    BaseApiResult blockUser(String userId);

    User queryById(String userId);

    boolean checkPermissionForUser(User user, PermissionEnum[] permissionEnums);

    /**
     * @Description 上传用户的头像信息
     * @Param []
     * @return BaseApiResult
     **/
    BaseApiResult uploadUserAvatar(String userId, MultipartFile file);

    /**
     * Deleting a user profile picture
     * @param userId user index
     * @return BaseApiResult
     */
    BaseApiResult removeUserAvatar(String userId);

    /**
     * remove user entity
     * @param userId user index
     * @return BaseApiResult
     */
    BaseApiResult removeUser(String userId);

    /**
     * Remove user entities in batches
     * @param userIdList user index
     * @param adminUserId administrator index
     * @return BaseApiResult
     */
    BaseApiResult deleteUserByIdBatch(List<String> userIdList, String adminUserId);

    BaseApiResult updateUserInfo(UserDTO userDTO);
}
