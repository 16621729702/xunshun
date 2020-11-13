package com.wink.livemall.admin.util;

import java.util.UUID;

/**
 * @author:lq 废弃
 * @date: 2019/10/16
 * @time: 15:15
 */
public class Constant {
    public static final String JWT_ID = UUID.randomUUID().toString();

    /**
     * 加密密文
     */
    public static final String JWT_SECRET = "c2VjcmV0";

    /**
     * accessToken有效期
     */
    public static final long ACCESS_TOKEN_JWT_TTL = 30*60*1000;

    /**
     * refreshToken有效期
     */
    public static final long REFRESH_TOKEN_JWT_TTL = 7*24*60*60*1000;

    /**
     * 提前token刷新时间
     */
    public static final long PRE_TOKEN_JWT_TTL = 25*60*1000;
}
