package com.coden.controller;

import com.coden.auth.Permission;
import com.coden.auth.PermissionEnum;
import com.coden.common.ConfigConstant;
import com.coden.common.MessageConstant;
import com.coden.entity.User;
import com.coden.entity.dto.*;
import com.coden.service.IUserService;
import com.coden.util.BaseApiResult;
import com.mongodb.client.result.UpdateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Pattern;

@Api(tags = "用户模块")
@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    private static final String COLLECTION_NAME = "user";

    private static final String REQUEST_USER_ID = "id";

    @Resource
    IUserService userService;


    @Resource
    private MongoTemplate template;


    @ApiOperation(value = "模拟用户登录", notes = "模拟")
    @PostMapping("/login")
    public BaseApiResult login(@RequestBody RegistryUserDTO user) {
        return userService.login(user);
    }


    @ApiOperation(value = "新增单个用户", notes = "新增单个用户")
    @PostMapping(value = "/insert")
    public BaseApiResult insertObj(@RequestBody RegistryUserDTO userDTO) {
        return userService.registry(userDTO);
    }

    @ApiOperation(value = "批量新增用户", notes = "批量新增用户")
    @PostMapping(value = "/batchInsert")
    public BaseApiResult batchInsert(@RequestBody List<User> users) {
        log.info("批量新增用户入参=={}", users.toString());
        for (User item : users) {
            template.save(item, COLLECTION_NAME);
        }
        return BaseApiResult.success("批量新增成功");
    }

    @ApiOperation(value = "根据id查询", notes = "根据id查询")
    @PostMapping(value = "/getById")
    public BaseApiResult getById(@RequestBody User user) {
        Query query = new Query(Criteria.where("_id").is(user.getId()));
        User one = template.findOne(query, User.class, COLLECTION_NAME);
        return BaseApiResult.success(one);
    }

    @ApiOperation(value = "根据用户名称查询", notes = "根据用户名称查询")
    @PostMapping(value = "/getByUsername")
    public BaseApiResult getByUsername(@RequestBody User user) {
        Query query = new Query(Criteria.where("username").is(user.getUsername()));
        User one = template.findOne(query, User.class, COLLECTION_NAME);
        return BaseApiResult.success(one);
    }

    @ApiOperation(value = "更新用户", notes = "更新用户")
    @PutMapping(value = "/updateUser")
    public BaseApiResult updateUser(@RequestBody UserDTO userDTO) {
        System.out.println(" userId = " + userDTO.getId());
        if (StringUtils.hasText(userDTO.getPassword())) {
            if (!patternMatch(userDTO.getPassword(), fieldRegx.get("password"))) {
                return BaseApiResult.error(MessageConstant.PARAMS_ERROR_CODE, MessageConstant.PARAMS_FORMAT_ERROR);
            }
        }
        if (StringUtils.hasText(userDTO.getMail())) {
            if (!patternMatch(userDTO.getPassword(), fieldRegx.get("mail"))) {
                return BaseApiResult.error(MessageConstant.PARAMS_ERROR_CODE, MessageConstant.PARAMS_FORMAT_ERROR);
            }
        }

        if (StringUtils.hasText(userDTO.getPhone())) {
            if (!patternMatch(userDTO.getPassword(), fieldRegx.get("phone"))) {
                return BaseApiResult.error(MessageConstant.PARAMS_ERROR_CODE, MessageConstant.PARAMS_FORMAT_ERROR);
            }
        }
        if (StringUtils.hasText(userDTO.getDescription())) {
            if (!patternMatch(userDTO.getPassword(), fieldRegx.get("description"))) {
                return BaseApiResult.error(MessageConstant.PARAMS_ERROR_CODE, MessageConstant.PARAMS_FORMAT_ERROR);
            }
        }
        return userService.updateUserInfo(userDTO);
    }

    @Permission(PermissionEnum.ADMIN)
    @ApiOperation(value = "根据id删除用户", notes = "根据id删除用户，删除用户的时候必须要删除其头像信息")
    @DeleteMapping(value = "/auth/deleteByID")
    public BaseApiResult deleteById(@RequestBody User removeUser, HttpServletRequest request) {
        String userId = (String) request.getAttribute(REQUEST_USER_ID);
        // 不能删除自己的账号
        String removeUserId = removeUser.getId();
        if (userId == null || userId.equals(removeUserId)) {
            return BaseApiResult.error(MessageConstant.PARAMS_ERROR_CODE, MessageConstant.OPERATE_FAILED);
        }
        return userService.removeUser(removeUserId);
    }

    @ApiOperation(value = "批量删除用户", notes = "根据id删除用户，管理员批量删除， 注意删除用户的时候必须要删除其头像信息")
    @Permission(value = PermissionEnum.ADMIN)
    @DeleteMapping(value = "/auth/deleteByIDBatch")
    public BaseApiResult deleteByIdBatch(@RequestBody BatchIdDTO batchIdDTO, HttpServletRequest request) {
        // 用户只能删除自己，不能删除其他人的信息
        String adminUserId = (String) request.getAttribute(REQUEST_USER_ID);
        List<String> userIdList = Optional.ofNullable(batchIdDTO.getIds()).orElse(new ArrayList<>());
        if (userIdList.size() > ConfigConstant.MAX_DELETE_NUM) {
            return BaseApiResult.error(MessageConstant.PROCESS_ERROR_CODE, MessageConstant.OPERATE_FAILED);
        }
        return userService.deleteUserByIdBatch(userIdList, adminUserId);
    }


    @ApiOperation(value = "根据分页参数查询用户列表")
    @Permission(PermissionEnum.ADMIN)
    @GetMapping("/allUsers")
    public BaseApiResult allUsers(@ModelAttribute("pageDTO") BasePageDTO pageDTO) {
        return userService.getUserList(pageDTO);
    }


    @ApiOperation(value = "修改用户角色")
    @Permission(PermissionEnum.ADMIN)
    @PutMapping("changeUserRole")
    public BaseApiResult changeUserRole(@RequestBody UserRoleDTO userRoleDTO, HttpServletRequest request) {
        String adminUserId = (String) request.getAttribute(REQUEST_USER_ID);
        // 不能屏蔽自己的账号
        if (userRoleDTO.getUserId().equals(adminUserId)) {
            return BaseApiResult.error(MessageConstant.PARAMS_ERROR_CODE, MessageConstant.OPERATE_FAILED);
        }
        return userService.changeUserRole(userRoleDTO);
    }

    @ApiOperation(value = "屏蔽用户",notes = "使用户不可登录；再次调用此接口则取消屏蔽")
    @Permission(PermissionEnum.ADMIN)
    @GetMapping("blockUser")
    public BaseApiResult blockUser(@RequestParam("userId") String userId, HttpServletRequest request) {
        if (!StringUtils.hasText(userId)) {
            return BaseApiResult.error(MessageConstant.PARAMS_ERROR_CODE, MessageConstant.PARAMS_IS_NOT_NULL);
        }
        String adminUserId = (String) request.getAttribute(REQUEST_USER_ID);
        // 不能屏蔽自己的账号
        if (userId.equals(adminUserId)) {
            return BaseApiResult.error(MessageConstant.PARAMS_ERROR_CODE, MessageConstant.OPERATE_FAILED);
        }
        return userService.blockUser(userId);
    }


    @ApiOperation(value = "查询用户信息",notes = "登录后携带JWT才能访问")
    @RequestMapping("/secure/getUserInfo")
    public String login(HttpServletRequest request) {
        Integer id = (Integer) request.getAttribute("id");
        String userName = request.getAttribute("username").toString();
        String password = request.getAttribute("password").toString();
        return "当前用户信息id=" + id + ",userName=" + userName + ",password=" + password;
    }

    static final Map<String, String> fieldRegx = new HashMap<>(8);

    static {
        // 1-64个数字字母下划线
        fieldRegx.put("password", "^[0-9a-z_]{1,64}$");
        fieldRegx.put("phone", "/^1(3\\d|4[5-9]|5[0-35-9]|6[567]|7[0-8]|8\\d|9[0-35-9])\\d{8}$/");
        fieldRegx.put("mail", "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
        // 1-140个任意字符
        fieldRegx.put("description", "(.*){1,140}");
    }



    @ApiOperation(value = "更新用户的头像")
    @PostMapping("/auth/uploadUserAvatar")
    public BaseApiResult uploadUserAvatar(@RequestParam(value = "img") MultipartFile file, HttpServletRequest request) {
        String userId = (String) request.getAttribute("id");
        String type = file.getContentType();
        String[] availableTypes = new String[]{"image/png", "image/jpeg", "image/gif"};
        if (!Arrays.asList(availableTypes).contains(type)) {
            return BaseApiResult.error(MessageConstant.PARAMS_ERROR_CODE, MessageConstant.PARAMS_FORMAT_ERROR);
        }
        return userService.uploadUserAvatar(userId, file);
    }

    @ApiOperation(value = "删除用户的头像")
    @DeleteMapping("/auth/removeUserAvatar")
    public BaseApiResult removeUserAvatar(HttpServletRequest request) {
        return userService.removeUserAvatar((String) request.getAttribute("id"));
    }

    private boolean patternMatch(String s, String regex) {
        return Pattern.compile(regex).matcher(s).matches();
    }

}