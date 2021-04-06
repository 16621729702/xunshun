package com.wink.livemall.job.utils;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import com.wink.livemall.job.utils.payUtil.OurWxPayConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WeixinpayUtil {
    Logger logger = LogManager.getLogger(WeixinpayUtil.class);

    public Map<String,String> refund(String price,String orderid) throws Exception{
        OurWxPayConfig ourWxPayConfig = new OurWxPayConfig();
        WXPay wxPay = new WXPay(ourWxPayConfig);
        //根据微信支付api来设置
        Map<String,String> data = new HashMap<>();
        data.put("appid","wx21ffcd2a0e5145eb");
        data.put("mch_id",ourWxPayConfig.getMchID());
        data.put("nonce_str", UUID.randomUUID().toString());   // 随机字符串小于32位//商户号
        data.put("out_trade_no",orderid);   //交易号
        data.put("out_refund_no",orderid);
        data.put("total_fee",price);
        data.put("refund_fee",price);
        String s = WXPayUtil.generateSignature(data, ourWxPayConfig.getKey());  //签名
        data.put("sign",s);
        System.out.println(data);
        Map<String, String> respData = wxPay.refund(data);
        if (respData.get("return_code").equals("SUCCESS")){
            return respData;
        }
        throw new Exception(respData.get("return_msg"));
    }

    //违约金退款流程
    public Map<String,String> falsifyRefund(String price,String orderid) throws Exception{
        OurWxPayConfig ourWxPayConfig = new OurWxPayConfig();
        WXPay wxPay = new WXPay(ourWxPayConfig);
        //根据微信支付api来设置
        Map<String,String> data = new HashMap<>();
        data.put("appid","wx21ffcd2a0e5145eb");
        data.put("mch_id",ourWxPayConfig.getMchID());
        data.put("nonce_str", UUID.randomUUID().toString());   // 随机字符串小于32位//商户号
        data.put("out_trade_no",orderid);   //交易号
        data.put("out_refund_no",orderid);
        data.put("total_fee",price);
        data.put("refund_fee",price);
        String s = WXPayUtil.generateSignature(data, ourWxPayConfig.getKey());  //签名
        data.put("sign",s);
        System.out.println(data);
        Map<String, String> respData = wxPay.refund(data);
        if (respData.get("return_code").equals("SUCCESS")){

            return respData;
        }
        throw new Exception(respData.get("return_msg"));
    }

    public static void main(String[] args) throws Exception{
       /* WeixinpayUtil weixinpayUtil= new WeixinpayUtil();
        Map<String,String> map = weixinpayUtil.refund("5","10G920201102164121012");
        System.out.println(map);*/
    }
}
