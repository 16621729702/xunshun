package com.wink.livemall.admin.util;


import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import com.google.gson.Gson;
import com.wink.livemall.admin.config.OurWxPayConfig;
import com.wink.livemall.member.service.LmFalsifyService;
import com.wink.livemall.order.dto.LmPayLog;
import com.wink.livemall.order.service.LmPayLogService;
import com.wink.livemall.sys.setting.dao.ConfigsDao;
import com.wink.livemall.sys.setting.dto.Configs;
import com.wink.livemall.sys.setting.service.ConfigsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 微信支付工具类
 */

public class WeixinpayUtil {

    Logger logger = LogManager.getLogger(WeixinpayUtil.class);

    private static final String  notifyUrlWxPay= "http://api.xunshun.net/api/";

    //private static final String  notifyUrlWxPay= "http://116.231.0.77:8989/api/";


    public Map<String,String> wxPayFunction(String price,String orderid,String body,String ip) throws Exception{
        String notifyUrl = notifyUrlWxPay+"pay/notifyWeiXinPay";
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
        System.out.println("notifyUrl：+++++++++" +  notifyUrl);
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

    public Map<String,String> wxPayFalsify(String price,String falsifyId,String body,String ip,String goodstype,String goodid,String userid) throws Exception{
        String notifyUrl = notifyUrlWxPay+"falsify/falsifyWeiXinPay";
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
        data.put("out_trade_no",falsifyId);   //交易号
        data.put("spbill_create_ip",ip);             //终端ip
        data.put("total_fee",price);       //订单总金额
        String s = WXPayUtil.generateSignature(data, ourWxPayConfig.getKey());  //签名
        data.put("sign",s);
        Map<String,String> scene_info = new HashMap<>();
        scene_info.put("goodstype",goodstype);
        scene_info.put("goodid",goodid);
        scene_info.put("userid",userid);
        data.put("scene_info",new Gson().toJson(scene_info));
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
            respData.put("respData",new Gson().toJson(respData));
            respData.put("sign",sign);
            respData.put("timestamp",repData.get("timestamp"));
            return respData;
        }
        throw new Exception(respData.get("return_msg"));
    }



    public Map<String,String> wxLivePayFunction(String price,String livePaySn,String body,String ip) throws Exception{
        String notifyUrl = notifyUrlWxPay+"payLive/liveWeiXinPay";
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
        data.put("out_trade_no",livePaySn);   //交易号
        data.put("spbill_create_ip",ip);             //终端ip
        data.put("total_fee",price);       //订单总金额
        String s = WXPayUtil.generateSignature(data, ourWxPayConfig.getKey());  //签名
        data.put("sign",s);
        System.out.println("notifyUrl：+++++++++" +  notifyUrl);
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
            respData.put("merOrderId",livePaySn);
            respData.put("sign",sign);
            respData.put("timestamp",repData.get("timestamp"));
            return respData;
        }
        throw new Exception(respData.get("return_msg"));
    }

    public Map<String,String> marginRenewFunction(String price,String livePaySn,String body,String ip) throws Exception{
        String notifyUrl = notifyUrlWxPay+"merMargin/marginRenewWeiXin";
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
        data.put("out_trade_no",livePaySn);   //交易号
        data.put("spbill_create_ip",ip);             //终端ip
        data.put("total_fee",price);       //订单总金额
        String s = WXPayUtil.generateSignature(data, ourWxPayConfig.getKey());  //签名
        data.put("sign",s);
        System.out.println("notifyUrl：+++++++++" +  notifyUrl);
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
            respData.put("merOrderId",livePaySn);
            respData.put("sign",sign);
            respData.put("timestamp",repData.get("timestamp"));
            return respData;
        }
        throw new Exception(respData.get("return_msg"));
    }


    //正常退款流程
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


  /*  public static boolean isOdd(int number)
    {
        return number % 2 == 1;
    }*/
    public static void main(String[] args) throws Exception{
      /*  BigDecimal bigDecimal=new BigDecimal(11111);
        BigDecimal bigDecimaal=new BigDecimal(111111);
        BigDecimal divide = bigDecimal.subtract(bigDecimaal);
        System.out.println(divide);*/

    }
}
