package com.wink.livemall.admin.util;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @desc 使用token验证用户是否登录
 **/
public class TokenUtil{
    //设置过期时间
    private static final int EXPIRE_DATE = 1000*60*60*24*14;
    //    private static final long EXPIRE_DATE = 60 * 1000;//一分钟
    //token秘钥
    private static final String TOKEN_SECRET = "Axmk89Li3Aji9M";//密钥自己设定一个

    /**
     * 生成签名,
     *
     * @param userId
     * @return
     */
    public static String Token(String userId) {
        String token = "";
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            Map<String, Object> header = new HashMap<String, Object>();
            header.put("alg", "HS256");
            header.put("typ", "JWT");
            token = JWT.create().withHeader(header)
                    .withClaim("userId", userId)
                    .withIssuer("xunshun")
                    .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRE_DATE))
                    .sign(algorithm);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return token;
    }



    public static HttpJsonResult<JSONObject> verify(String token ) {
        HttpJsonResult<JSONObject> jsonResult=new HttpJsonResult<>();
        /**
         * @desc 验证token，通过返回true
         * @params [token]需要校验的串
         **/
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer("xunshun").build();
            verifier.verify(token);
            jsonResult.setCode(Errors.SUCCESS.getCode());
            jsonResult.setMsg(Errors.SUCCESS.getMsg());
            return jsonResult;
        } catch (TokenExpiredException e1){
            //TokenExpiredException 该异常 为token 过期
//            e1.printStackTrace();
            //可以返回自己设定的token过期的code
            jsonResult.setCode(Errors.TOKEN_PAST.getCode());
            jsonResult.setMsg(Errors.TOKEN_PAST.getMsg());
            return jsonResult;
        }catch (Exception e) {
            //token 验证失败
//            e.printStackTrace();
            jsonResult.setCode(Errors.TOKEN_ERROR.getCode());
            jsonResult.setMsg(Errors.TOKEN_ERROR.getMsg());
            return jsonResult;
        }
    }
    /**
     * 我们可以将一些常用的信息放入token中,比如用户登陆信息,方便我们的使用
     * 获得token中的信息无需secret解密也能获得
     */
    public  static String getUserId(String token){
        HttpJsonResult<JSONObject> jsonResult=new HttpJsonResult<>();
        try {
            if (token == null || "".equals(token.trim())) {
//                throw new JWTAuthException(String.valueOf(Errors.jwt_token_missing.getCode()), Errors.jwt_token_missing.getMsg());
            }
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userId").asString();
        } catch (Exception e) {
            jsonResult.setCode(Errors.TOKEN_ERROR.getCode());
            jsonResult.setMsg(Errors.TOKEN_ERROR.getMsg());
            return null;
        }
    }

    public static void main(String[] args) throws Exception{
     String token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ4dW5zaHVuIiwiZXhwIjoxNjEyNzY0NjcxLCJ1c2VySWQiOiIyNjMifQ.f56edbu5eUzNavR-e96RBvTqHXthZn2wStFkGAS_8sY";
        String userId = getUserId(token);
        System.out.print(userId);


    }

}