package com.wink.livemall.admin.util;


import com.wink.livemall.admin.exception.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

/**
 * 废弃
 */
public class JwtUtil {
    private final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    /**
     * 由字符串生成加密key
     *
     * @return 密钥
     */
    private SecretKey generalKey() {
        String stringKey = Constant.JWT_SECRET;

        // 本地的密码解码
        byte[] encodedKey = Base64.decodeBase64(stringKey);

        // 根据给定的字节数组使用AES加密算法构造一个密钥
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }


    /**
     * 创建Jwt
     * @param claims 创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
     * @return String
     * @throws Exception
     */
    public String createJWT(Map<String, Object> claims) throws Exception {
        // 指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // 生成JWT的时间
        long nowMillis = System.currentTimeMillis();

        // 生成签名的时候使用的秘钥secret，切记这个秘钥不能外露哦。它就是你服务端的私钥，在任何场景都不应该流露出去。
        // 一旦客户端得知这个secret, 那就意味着客户端是可以自我签发jwt了。
        SecretKey key = generalKey();

        // 下面就是在为payload添加各种标准声明和私有声明了
        // 这里其实就是new一个JwtBuilder，设置jwt的body
        JwtBuilder builder = Jwts.builder()
                // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setClaims(claims)
                // 设置签名使用的签名算法和签名使用的秘钥
                .signWith(signatureAlgorithm, key);

        // 设置过期时间
//        if (ttlMillis >= 0) {
//            long expMillis = nowMillis + ttlMillis;
//            Date exp = new Date(expMillis);
//            builder.setExpiration(exp);
//        }
        return builder.compact();
    }

    /**
     * 解密jwt
     * @param jwt token
     * @return
     * @throws Exception
     */
    public Claims parseJWT(String jwt) throws CustomException {
        //签名秘钥，和生成的签名的秘钥一模一样
        SecretKey key = generalKey();
        //得到DefaultJwtParser
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    //设置签名的秘钥
                    .setSigningKey(key)
                    //设置需要解析的jwt
                    .parseClaimsJws(jwt).getBody();
        }catch (Exception e){
            e.printStackTrace();
            throw new CustomException("Token已过期");
        }
        return claims;
    }

    /**
     * 解密jwt
     * @param token token
     * @return userid
     */
    public String getUseridFromparseJWT(String token) {
        String userid = null;
        try {
            Claims claims = parseJWT(token);
            Map<String, Object> basicinfo = claims.get("basicinfo", Map.class);
            if(basicinfo!=null&&basicinfo.get("userid")!=null){
                userid = basicinfo.get("userid").toString();
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return userid;
    }


}
