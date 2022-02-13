package com.zzy.wiki.util;

import com.zzy.wiki.resp.UserLoginResp;

import java.io.Serializable;

/**
 * @author zzy
 * @date 2022/1/28
 */
public class LoginUserContext  implements Serializable {

    private static ThreadLocal<UserLoginResp> user = new ThreadLocal<>();

    public static UserLoginResp getUser() {
        return user.get();
    }

    public static void setUser(UserLoginResp user) {
        LoginUserContext.user.set(user);
    }
}
