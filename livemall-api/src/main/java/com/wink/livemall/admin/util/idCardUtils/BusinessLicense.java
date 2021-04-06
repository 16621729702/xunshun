package com.wink.livemall.admin.util.idCardUtils;



import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 营业执照识别
 */
public class BusinessLicense {

    public static  String apiKey = "xPHV0ibbcqtawIgeSYhlm67x";
    public static  String secretKey = "VB3RZU8NVnocEEb9wG6Fuzut0yhzVEuz";

    /**
     * 巡顺文化审核企业营业执照
     * @return
     */
    public static String businessLicense(String filePath) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/business_license";
        try {

            /*byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");*/
            String param = "url=" + filePath;
            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = AuthUtils.getAuth(apiKey,secretKey);
            String result = HttpUtil.post(url, accessToken, param);
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        //String filePath = "http://oss.xunshun.net/head/fXbH9iG7VsafM5awlZ7IuAGQc6bENJDp";
        String filePath = "http://oss.xunshun.net/%E4%B8%8A%E6%B5%B7%E5%B7%A1%E9%A1%BA%E6%96%87%E5%8C%96.jpg";
        String license = BusinessLicense.businessLicense(filePath);
        Object words = JSONObject.parseObject(license).get("words_result");
        Object uniform_social_credit_code =JSONObject.parseObject(words.toString()).get("社会信用代码");
        Object artificial_person =JSONObject.parseObject(words.toString()).get("法人");
        Object term_of_validity =JSONObject.parseObject(words.toString()).get("有效期");
        String ss= JSONObject.parseObject(artificial_person.toString()).get("words").toString();
        String daimass= JSONObject.parseObject(uniform_social_credit_code.toString()).get("words").toString();
        String validity= JSONObject.parseObject(term_of_validity.toString()).get("words").toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        Date validitys =new Date();
        if(("年月日").equals(validity)){
            System.out.print(validity);
        }else {
            validitys= sdf.parse(validity);
        Date date = new Date();
            if(validitys.getTime()<date.getTime()){
                validitys=null;
            }
        }

        System.out.print(ss+"+++++++++++++++++++="+daimass+"+++++++++++"+validitys);

    }
}