package com.wink.livemall.admin.util;

import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import com.google.gson.Gson;
import com.wink.livemall.admin.config.OurWxPayConfig;
import com.wink.livemall.member.dto.LmFalsify;
import com.wink.livemall.member.service.LmFalsifyService;
import com.wink.livemall.order.dto.LmPayLog;
import com.wink.livemall.order.service.LmPayLogService;
import com.wink.livemall.sys.setting.dto.Configs;
import com.wink.livemall.sys.setting.service.ConfigsService;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.SSLContexts;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.KeyStore;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 微信支付工具类
 */
public class WeixinpayUtil {
    Logger logger = LogManager.getLogger(WeixinpayUtil.class);

    //private static String NotifyUrl = "http://192.168.1.6:8989/api/";
    //private static String NotifyUrl = "http://api.xunshun.net/";
    @Autowired
    private LmPayLogService lmPayLogService;
    @Autowired
    private LmFalsifyService lmFalsifyService;
    @Autowired
    private ConfigsService configsService;


    public Map<String,String> wxPayFunction(String price,String orderid,String body,String ip) throws Exception{

        Configs configs =configsService.findByTypeId(Configs.vx_pay);
        Map map =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
        String notifyUrl = map.get("notifyUrl")+"pay/notifyWeiXinPay";
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

    public Map<String,String> wxPayFalsify(String price,String orderid,String body,String ip,String goodstype,String goodid,String userid) throws Exception{
        Configs configs =configsService.findByTypeId(Configs.vx_pay);
        Map map =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
        String notifyUrl = map.get("notifyUrl")+"falsify/falsifyWeiXinPay";
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
            //记录保存
            LmPayLog lmPayLog = new LmPayLog();
            lmPayLog.setCreatetime(new Date());
            lmPayLog.setOrderno(orderid);
            lmPayLog.setType("falsify");
            lmPayLog.setSysmsg(new Gson().toJson(respData));
            lmPayLogService.insertService(lmPayLog);
            respData.put("goodstype",goodstype);
            respData.put("goodid",goodid);
            respData.put("userid",userid);
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

    public static void main(String[] args) throws Exception{
       /* WeixinpayUtil weixinpayUtil= new WeixinpayUtil();
        Map<String,String> map = weixinpayUtil.refund("0.05","10G920201102164121012");
        System.out.println(map);*/
    }
}
