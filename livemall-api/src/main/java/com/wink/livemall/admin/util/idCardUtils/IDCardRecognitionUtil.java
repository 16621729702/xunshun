package com.wink.livemall.admin.util.idCardUtils;

import com.alibaba.fastjson.JSON;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.util.HashMap;

/**
 * 身份证识别的工具类
 */
@Component
public class IDCardRecognitionUtil {
    //设置相关参数 https://console.bce.baidu.com/
    public static  String appId = "23509909";
    public static  String apiKey = "6svHS9OFGWgAwsQ2V2oH2YK7";
    public static  String secretKey = "r4hGan7AsS3wnu9jtVIzLWVs2TVF1HyZ";

    public void setAppId(String id){
        IDCardRecognitionUtil.appId=id;
    }
    public void setApiKey(String aKey){
        IDCardRecognitionUtil.apiKey=aKey;
    }
    public void setSecretKey(String sKey){
        IDCardRecognitionUtil.secretKey=sKey;
    }

    public static IDCardResult readIDCard(boolean detectDirectionFlag, boolean detectRiskFlag, String idCardSide, String imagePath){
        System.out.println("**********APP_ID_IDCARD:"+appId);
        System.out.println("**********API_KEY_IDCARD:"+apiKey);
        System.out.println("**********SECRET_KEY_IDCARD:"+secretKey);
        // 初始化一个AipOcr
        APIUrl client = new APIUrl(appId, apiKey, secretKey);
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("detect_direction", ""+detectDirectionFlag);
        options.put("detect_risk", ""+detectRiskFlag);
        if(StringUtils.isEmpty(idCardSide) || (!"front".equals(idCardSide) && !"back".equals(idCardSide)) ){
            System.out.println("身份证正反面参数idCardSide传入不正确!");
            return null;
        }
        JSONObject res = client.idcard(imagePath, idCardSide, options);
        IDCardResult idCardResult = JSON.parseObject(res.toString(), IDCardResult.class);
        return idCardResult;
    }

    public static String getMessage(IDCardResult idCardResult){
        StringBuilder sb = new StringBuilder();
        //image_status
        if("normal".equals(idCardResult.getImage_status())){
            sb.append("识别正常;");
        }else if("reversed_side".equals(idCardResult.getImage_status())){
            sb.append("未摆正身份证;");
        }else if("non_idcard".equals(idCardResult.getImage_status())){
            sb.append("上传的图片中不包含身份证;");
        }else if("blurred".equals(idCardResult.getImage_status())){
            sb.append("身份证模糊;");
        }else if("over_exposure".equals(idCardResult.getImage_status())){
            sb.append("身份证关键字段反光或过曝;");
        }else if("unknown".equals(idCardResult.getImage_status())){
            sb.append("未知状态;");
        }
        //risk_type
        if(!StringUtils.isEmpty(idCardResult.getRisk_type())){
            if("normal".equals(idCardResult.getRisk_type())){
                sb.append("正常身份证;");
            }else if("copy".equals(idCardResult.getRisk_type())){
                sb.append("复印件;");
            }else if("temporary".equals(idCardResult.getRisk_type())){
                sb.append("临时身份证;");
            }else if("screen".equals(idCardResult.getRisk_type())){
                sb.append("翻拍;");
            }else if("unknown".equals(idCardResult.getRisk_type())){
                sb.append("其他未知情况;");
            }
        }
        if(!StringUtils.isEmpty(idCardResult.getEdit_tool())){
            sb.append("该身份证被图片处理软件"+idCardResult.getEdit_tool()+"编辑处理过;");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
       /* String imagePath ="http://oss.xunshun.net/head/Ah6it1Ax6AbUYGrv6mM4h8jEkH2KBKGX";
        String imagePaths ="http://oss.xunshun.net/head/wpMvwIKKOICcYtgBesjqR6Bs5AUYFkcO";
        IDCardResult idCardResult = IDCardRecognitionUtil.readIDCard(true, true, "front", imagePath);
        IDCardResult idCardResults = IDCardRecognitionUtil.readIDCard(true, true, "back", imagePaths);
        System.out.println("***********: "+idCardResult);
        System.out.println("***********: "+IDCardRecognitionUtil.getMessage(idCardResult));
        System.out.println("***********: "+idCardResults);
        System.out.println("***********: "+IDCardRecognitionUtil.getMessage(idCardResults));*/
    }

}
