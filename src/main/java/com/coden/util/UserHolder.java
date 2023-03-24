package com.coden.util;

import com.coden.entity.dto.UserDTO;

/**
 * @Author Codenv-j
 * @Date 2023/3/24 14:18
 * @Version 1.0
 */
public class UserHolder {
    private static final ThreadLocal<UserDTO> tl = new ThreadLocal<>();

    public static void saveUser(UserDTO user){
        tl.set(user);
    }

    public static UserDTO getUser(){
        return tl.get();
    }

    public static void removeUser(){
        tl.remove();
    }
}
