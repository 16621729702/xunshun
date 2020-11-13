package com.wink.livemall.admin.util;

import com.wink.livemall.admin.exception.CustomException;

import java.util.HashMap;
import java.util.Map;

/**
 * 废弃
 */
public class TokenUtil {

    public static Map<String,Object> getToken(Map<String,Object> basicinfo){
        Map<String, Object> claims = new HashMap<>(10);
        basicinfo.put("create_time",System.currentTimeMillis());
        claims.put("basicinfo",basicinfo);
        JwtUtil jwtUtil = new JwtUtil();
        try {
            //创建accessToken
            String accessToken = jwtUtil.createJWT(claims);
            basicinfo.put("accessToken",accessToken);
        } catch (Exception e) {
           e.printStackTrace();
           throw new CustomException("创建token错误");
        }
        return basicinfo;
    }



}
