package com.wink.livemall.admin.util.filterUtils;

import com.baidu.aip.util.Base64Util;
import com.baidu.aip.util.Util;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CheckPicAPI {
    private static final String URL = "https://aip.baidubce.com/rest/2.0/solution/v1/img_censor/v2/user_defined";
    public static  String appId = "23621403";
    public static  String apiKey = "TT92olsP2RzGXBTGi2l6mlOr";
    public static  String secretKey = "RbN1Nz73zoPYsZhlDL21UpttPl5Ptfhc";


    /**
     * @param imgUrl
     *
     * @return true 通过 false 失败
     */
    public boolean check(String imgUrl,String access_token){


        //获取access_token
        Map<String,String> parameters = new HashMap<>();
        //添加调用参数
        parameters.put("access_token",access_token);
        parameters.put("imgType","0");
        parameters.put("imgUrl",imgUrl);
        //调用文本审核接口
        String result = HttpUtil.doPost(URL, parameters);
        // JSON转换
        JSONObject jsonObj = JSONObject.fromObject(result);
        System.out.println(jsonObj);
        //根据API返回内容处理业务逻辑 （这里简单模拟,）
        System.out.println(imgUrl);
        if(jsonObj.get("conclusionType").toString().equals("1")){
            return true;
        }else if(jsonObj.get("conclusionType").toString().equals("2")){
            System.out.println("图片违规");
            return false;
        }
        return false;
    }

    public static void main(String[] args) {
      /* System.out.println( new CheckPicAPI().check("http://oss.xunshun.net/comment/Mqtyqu1sKuNlBbzE6PNYCTuigy8ykGJk"));*/
    }
}
