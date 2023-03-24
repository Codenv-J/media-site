package com.coden.auth;

import com.auth0.jwt.interfaces.Claim;
import com.coden.entity.User;
import com.coden.entity.dto.UserDTO;
import com.coden.service.IUserService;
import com.coden.util.JwtUtil;
import com.coden.util.UserHolder;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

public class AuthenticationInterceptor implements HandlerInterceptor {

    private final IUserService userService;

    public AuthenticationInterceptor(IUserService userService) {
        this.userService = userService;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        // 获取方法中的注解
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        // 省略判断是否需要登录的方法.....

        // 获取类注解
        Permission permissionClass = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), Permission.class);
        // 获取方法注解
        Permission permissionMethod = AnnotationUtils.findAnnotation(method, Permission.class);

        // 判断是否需要权限校验
        if (permissionClass == null && permissionMethod == null) {
            // 不需要校验权限，直接放行
            return true;
        }
        // 省略Token解析的方法.....

        //获取 header里的token
        final String token = request.getHeader("authorization");
        if (token == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        Map<String, Claim> userData = JwtUtil.verifyToken(token);

        if (CollectionUtils.isEmpty(userData)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        // 此处根据自己的系统架构，通过Token或Cookie等获取用户信息。
        User userInfo = userService.queryById(userData.get("id").asString());
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userInfo.getId());
        userDTO.setMail(userInfo.getMail());
        userDTO.setPhone(userInfo.getPhone());
        UserHolder.saveUser(userDTO);
        // 获取该方法注解，优先级:方法注解>类注解
        PermissionEnum[] permissionEnums;
        if (permissionClass != null && permissionMethod == null) {
            // 类注解不为空，方法注解为空，使用类注解
            permissionEnums = permissionClass.name();
        } else if (permissionClass == null) {
            // 类注解为空，使用方法注解
            permissionEnums = permissionMethod.name();
        } else {
            // 都不为空，使用方法注解
            permissionEnums = permissionMethod.name();
        }
        // 校验该用户是否有改权限
        // 校验方法可自行实现，拿到permissionEnums中的参数进行比较
        if (userService.checkPermissionForUser(userInfo, permissionEnums)) {
            // 拥有权限
            return true;
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }

    /**
     * @Description 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
     * @Param [request, response, handler, modelAndView]
     **/
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {

    }

    /**
     * @Description 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
     * @Param [request, response, handler, ex]
     **/
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }
}
