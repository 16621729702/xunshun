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

    public Map<String,String> wxPayFunction(String price,String orderid,String body,String ip) throws Exception{

        String notifyUrl = "http://api.xunshun.net/api/pay/notifyWeiXinPay";
        OurWxPayConfig ourWxPayConfig = new OurWxPayConfig();
        WXPay wxPay = new WXPay(ourWxPayConfig);
        //根据微信支付api来设置
        Map<String,String> data = new HashMap<>();
        data.put("appid",ourWxPayConfig.getAppID());
        data.put("mch_id",ourWxPayConfig.getMchID());
        data.put("nonce_str", UUID.randomUUID().toString());   // 随机字符串小于32位//商户号
        data.put("body",body);
        data.put("trade_type","APP");                         //支付场景 APP 微信app支付 JSAPI 公众号支付  NATIVE 扫码支付
        data.put("notify_url",notifyUrl);                     //回调地址
        data.put("out_trade_no",orderid);   //交易号
        data.put("spbill_create_ip",ip);             //终端ip
        data.put("total_fee",price);       //订单总金额
        String s = WXPayUtil.generateSignature(data, ourWxPayConfig.getKey());  //签名
        data.put("sign",s);
        System.out.println(data);
        /** wxPay.unifiedOrder 这个方法中调用微信统一下单接口 */
        Map<String, String> respData = wxPay.unifiedOrder(data);
        if (respData.get("return_code").equals("SUCCESS")){

            //返回给APP端的参数，APP端再调起支付接口
            Map<String,String> repData = new HashMap<>();
            repData.put("appid",ourWxPayConfig.getAppID());
            repData.put("partnerid",ourWxPayConfig.getMchID());
            repData.put("prepayid",respData.get("prepay_id"));
            repData.put("package","Sign=WXPay");
            repData.put("noncestr",respData.get("nonce_str"));
            repData.put("timestamp",System.currentTimeMillis()/1000+"");
            System.out.println(repData);
            String sign = WXPayUtil.generateSignature(repData,ourWxPayConfig.getKey()); //签名
            respData.put("sign",sign);
            respData.put("timestamp",repData.get("timestamp"));
            return respData;
        }
        throw new Exception(respData.get("return_msg"));
    }


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



    public static void main(String[] args) throws Exception{
        WeixinpayUtil weixinpayUtil= new WeixinpayUtil();
        Map<String,String> map = weixinpayUtil.refund("5","10G920201102164121012");
        System.out.println(map);
    }
}
