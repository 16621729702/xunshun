package com.wink.livemall.admin.util.idCardUtils;

import com.alibaba.fastjson.JSONObject;
import com.wink.livemall.admin.util.idCardUtils.Bean.HttpVideoUtil;
import java.util.HashMap;
import java.util.Map;

public class PersonVerifyUtil {
    /**
     * 重要提示代码中所需工具类
     * 下载
     */
    public static String personVerify(String image , String realName, String merchIdCard) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/face/v3/person/verify";
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("image", image);
            map.put("liveness_control", "NONE");
            map.put("name", realName);
            map.put("id_card_number", merchIdCard);
            map.put("image_type", "URL");
            map.put("quality_control", "LOW");

            String param = GsonUtils.toJson(map);

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = AuthUtils.getAuth(MerchHoldApi.apiKey,MerchHoldApi.secretKey);

            String result = HttpUtil.post(url, accessToken,"application/json", param);
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 重要提示代码中所需工具类
     * 下载
     */
    public static String personVi(String image , String realName, String merchIdCard) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/solution/v1/video_censor/v2/user_defined";
        try {
            Map<String, String> map = new HashMap<>();
            map.put("name", realName);
            map.put("videoUrl", image);
            map.put("extId", merchIdCard);

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = AuthUtils.getAuth(MerchHoldApi.apiKeyvideo,MerchHoldApi.secretKeyvideo);
            map.put("access_token",accessToken);
            String result = HttpVideoUtil.doPost(image,map);
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void main(String[] args) {
        String image ="http://oss.xunshun.net/62697881c13566cf4f83bc7c0592e467.mp4";
        String realName = "沈晨宇";
        String merchIdCard = "310230199206204950";

        String s = PersonVerifyUtil.personVi(image, realName, merchIdCard);
        System.out.print(s);
    }
}
