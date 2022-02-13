package com.zzy.wiki.util;

import java.io.Serializable;

/**
 * @author zzy
 * @date 2022/2/7
 */
public class RequestContext implements Serializable {

    private static ThreadLocal<String> remoteAddr = new ThreadLocal<>();

    public static String getRemoteAddr() {
        return remoteAddr.get();
    }

    public static void setRemoteAddr(String remoteAddr){
        RequestContext.remoteAddr.set(remoteAddr);
    }
}
