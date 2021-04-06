package com.wink.livemall.admin.util.filterUtils;

import com.alibaba.fastjson.JSON;
import com.wink.livemall.admin.util.Errors;
import com.wink.livemall.admin.util.idCardUtils.Bean.DataBean;
import com.wink.livemall.admin.util.idCardUtils.Bean.Hits;
import com.wink.livemall.admin.util.idCardUtils.Bean.JsonRootBean;
import com.wink.livemall.admin.util.idCardUtils.IDCardResult;
import com.wink.livemall.goods.utils.HttpJsonResult;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckTextAPI {
    private static final String URL = "https://aip.baidubce.com/rest/2.0/solution/v1/text_censor/v2/user_defined";

    public static  String appId = "20679407";
    public static  String apiKey = "wdr8XNBEcg53gIqx0ZK4lkyj";
    public static  String secretKey = "X1EsNdGThKDkze9pY6sD9qGSkaT0ym1o";


    /**
     * @param
     *
     * @return true 通过 false 失败
     */
    public HttpJsonResult check(String text, String access_token){
        HttpJsonResult httpJsonResult =new HttpJsonResult();
        httpJsonResult.setCode(Errors.SUCCESS.getCode());
        //获取access_token
        Map<String,String> parameters = new HashMap<>();
        //添加调用参数
        parameters.put("access_token",access_token);
        parameters.put("text",text);
        //调用文本审核接口
        String result = HttpUtil.doPost(URL, parameters);
        // JSON转换
        JSONObject jsonObj = JSONObject.fromObject(result);
        System.out.println(jsonObj);
        //根据API返回内容处理业务逻辑 （这里简单模拟,）
        System.out.println(text);
        if(jsonObj.get("conclusionType").toString().equals("1")){
            httpJsonResult.setCode(Errors.SUCCESS.getCode());
            httpJsonResult.setMsg(Errors.SUCCESS.getMsg());
            return httpJsonResult;
        }else if(jsonObj.get("conclusionType").toString().equals("2")){
            JsonRootBean  jsonRootBean = JSON.parseObject( result, JsonRootBean .class);
            List<DataBean> data = jsonRootBean.getData();
            DataBean dataBean = data.get(0);
            List<Hits> hits = dataBean.getHits();
            Hits hits1 = hits.get(0);
            List<String> words = hits1.getWords();
            if(words!=null&&words.size()>0){
                httpJsonResult.setCode(Errors.ERROR.getCode());
                httpJsonResult.setMsg(words.get(0)+"-涉及违规，无法发出");
                return httpJsonResult;
            }else {
                httpJsonResult.setCode(Errors.SUCCESS.getCode());
                httpJsonResult.setMsg("存在恶意推广");
                return httpJsonResult;
            }
        }
        return httpJsonResult;
    }

    public static void main(String[] args) {
      String ASS="习大大空间空间就看见毛泽东";
        String access_token = GetAuthService.getAuth(CheckTextAPI.apiKey,CheckTextAPI.secretKey);
        HttpJsonResult check = new CheckTextAPI().check(ASS, access_token);
        if(check.getCode()!=200){
            System.out.println(check.getMsg());
        }else {
            System.out.println(2222);
        }

    }
}
