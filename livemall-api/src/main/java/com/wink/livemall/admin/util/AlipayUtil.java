package com.wink.livemall.admin.util;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.wink.livemall.admin.api.order.OrderController;
import com.wink.livemall.sys.setting.service.ConfigsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 废弃
 */
public class AlipayUtil {
    Logger logger = LogManager.getLogger(AlipayUtil.class);


    /**
     * 应用号
     */
    private static String APP_ID = "2016101000652381";
    /**
     * 商户的私钥
     */
    private static String APP_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDZEWap4Uky56yoZYnBhQ7MuqL/+iPOI1s6CNF3ZEH3117fAjR5c1KgXPXryLHe7TNdZ7i3t4xlrs/DOVqDZ7jyJemeYkXWBfyuYIssS7WhtYG+xZhET1/gTaK7QZmEOGXhQccwfd8RAhjJzaRBE8jH/ePjVcVhhrz7VV8j2JIEsRYvqIg9J7pJ1yFbrnGXqZjx/7cY4mShYvbxiqFBmAR66tzfpKN1Zkvfb3G4OYQer1ly7x/o+0coOX3k1HiQ3F424Tt18jOgTeUuZ+7gby2OMOUKlfl+n4CWM5kM1+8qngKJJGSRaYh1NUqq7jRaQOE0Xe7GS76ner162V8WDKSXAgMBAAECggEBAIqwfIjUIWG99moNniugKSmToeutVc0yd+onkjYXP7Lrz3jrPr3Oeh9t5c6Fh2go4+WtDK+NPc4GGEbi0TsePWOx54dDwN0TLDP6CVH++YjdoI1UUIpssJRu5ieArWZ2Zlm7HlqUQb7rOQ1PL8BItMNbK4yn05oRbZOkI0qS6BDIKw19nweecQnxbkJBdxlIAzR+SnQg25zPk6GEfASuaScCeeGqXEeTKx3j/h4OWtPA6EXVSRjPoztC9GaKzbthkKWW9qZoEFa+Po6IAasWIoVt3riR69CFV6MuTMumzGMxF5NTqlkin+pPFy8QRT6wiXshb8uPmJwG3hfxmgAOxRkCgYEA95O7xH5QDUe8JUMXVUGyToWaWG+OLKFqxohh8HRR/BgLZCvjfxLhW+gk76/kjZ8iFnwuyKbpvJ8p2Lr5i78qaiw0qjVBgc7kZODfakXYbkMslhAKAgBeAFjNYWlXMXcZq++1BeLTJfsaVjksqCmOeh2XVIPmH3r0T3zvPnK8120CgYEA4HPy/1jCPLDNXqnXgq/twzceMbFDP6Zx72QHYTl/AF/qibpfyAmUh3z5HBkeBpIQ10vxUw6vLVtSvT+f+ZCq8NANTatYjGjIzNLYgKwTR7SDS7enDejV4UxSwetRM1Ev3coir/imNnoWQBnLjDu3FCJFJRiQgJ0c0Hd+c/IVFZMCgYB4bK3ayC4cJ5aG8Xl6CPi4ZRhMiAa6AtIzO8eX9JykaPWxhf+kogRodiTxSbd8g7lLAqCnDTmIfEsYluonZZc2CcacPEde4soJn7BMuyipiZc4bxjPHGqc9JUCGwFEmFAuZ+y/rjNSuC95XVoxefVJxPcFLxtSmLYZrAWbdKgSiQKBgA1EjV6uvnyb7ufjnkupXqKlhXFmixUeH6oI/a6vmWRKRzDxBWxrtI31wzv3+CBnTfuMD63bNlu2BEQPmMNF2/T0N3UZ5dgW8Ze59vZUNaeNch1ts8BMvSfePR4dbOVdrTitFDPWvOyHdv0/CRs3BHF6PSAePZx8rRFdnun3q2tlAoGAaSkXbFDOaZRyZKubhCqupd7nmRiSMY+njDXg81P8mBbqT8X9RMSZ2hBIAeCSOJIS2sjEXBwn1XwvKqf4wlDTzEUiKaHn8bRDa/7dsPLGwyY1OaRcIEAZFlX1tdopIYlCH3dQJv2ZU7re/GNcQ4AYNl/YGMueUd5BaKQhReB2QHY=";
    /**
     * 编码
     */
    private static String CHARSET = "UTF-8";
    /**
     * 支付宝公钥
     */
    private static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhkihZBvHVKIyY7wlNI3wgbyFepItaFszse9UvgJEIFFKSo2HgkjsDHJ3ByvdDaa91VpYz7adXW/YRekBDvOgnOTE77NYK/jEM7+eQ5Py01bwWi8H+wCOK2Xgf/h4RDSIUCAb1YR7Torqe2G88GKKEKsddr1pY+TgQaoXHFZBneOzuJCbrWQQs1YnuAyzg0sTzbC3wQ60yOWj42G7+zaHF3+doBBoMlOpVzp9Ju1Ul61HC7UTDjaHATP5gc6Ad71AUahl6sBuSG972mQTiS43SzaaKcLrIBk6ALZ00yeE+nnYNJW3PoQfs8Mbi/gsLb2t2AyqRz6G4ZhvVj0kbeUOEwIDAQAB";
    /**
     * 支付宝网关地址
     */
    private static String GATEWAY = "https://openapi.alipaydev.com/gateway.do";
    /**
     * 成功付款回调
     */
    private static String PAY_NOTIFY = "";
    /**
     * 支付成功回调
     */
    private static String REFUND_NOTIFY = "";
    /**
     * 前台通知地址
     */
    private static String RETURN_URL = "";
    /**
     * 参数类型
     */
    private static String PARAM_TYPE = "json";
    /**
     * 成功标识
     */
    private static final String SUCCESS_REQUEST = "TRADE_SUCCESS";
    /**
     * 交易关闭回调(当该笔订单全部退款完毕,则交易关闭)
            */
    private static final String TRADE_CLOSED = "TRADE_CLOSED";
    /**
     * 收款方账号
     */
    private static final String SELLER_ID = "";
    /**
     * 支付宝请求客户端入口
     */
    private volatile static AlipayClient alipayClient = null;

    /**
     * 不可实例化
     */
    private AlipayUtil(){};

    public static AlipayClient getInstance(){
        if (alipayClient == null){
            synchronized (AlipayUtil.class){
                if (alipayClient == null){
                    alipayClient = new DefaultAlipayClient(GATEWAY,APP_ID,APP_PRIVATE_KEY,PARAM_TYPE,CHARSET,ALIPAY_PUBLIC_KEY);
                }
            }
        }
        return alipayClient;
    }

    /**
     * web支付下单并支付(web支付在安卓中是可以直接唤醒支付宝APP的)
     * url https://doc.open.alipay.com/doc2/detail.htm?treeId=203&articleId=105463&docType=1#s1
     * @return web支付的表单
     */
    public String TradeWapPayRequest(Map<String, String> sParaTemp){
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
        alipayRequest.setReturnUrl(AlipayUtil.RETURN_URL);
        alipayRequest.setNotifyUrl(AlipayUtil.PAY_NOTIFY);
        // 待请求参数数组
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody("我是测试数据");
        model.setSubject("App支付测试Java");
        model.setOutTradeNo("11");
        model.setTimeoutExpress("30m");
        model.setTotalAmount("0.01");
        model.setProductCode("QUICK_MSECURITY_PAY");
        alipayRequest.setBizModel(model);
        String orderstring = "";
        try {
            orderstring = AlipayUtil.getInstance().sdkExecute(alipayRequest).getBody();
        } catch (AlipayApiException e) {
            logger.error("支付宝构造表单失败",e);
        }
        logger.debug("支付宝:"+orderstring);
        return orderstring;
    }

    /**
     * 申请退款
     * @param sParaTemp 退款参数
     * @return true成功,回调中处理
     * 备注:https://doc.open.alipay.com/docs/api.htm?spm=a219a.7629065.0.0.3RjsEZ&apiId=759&docType=4
     */
    public boolean tradeRefundRequest(Map<String, ?> sParaTemp) throws AlipayApiException {
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setReturnUrl(AlipayUtil.RETURN_URL);
        request.setNotifyUrl(AlipayUtil.REFUND_NOTIFY);
        // 待请求参数数组
        request.setBizContent(JSON.toJSONString(sParaTemp));
        AlipayTradeRefundResponse response = AlipayUtil.getInstance().execute(request);
        logger.debug("支付宝退货结果:"+response.isSuccess());
        return response.isSuccess();
    }


    /**
     * 支付宝回调验签
     * @param request 回调请求
     * @return true成功
     * 备注:验签成功后，按照支付结果异步通知中的描述(二次验签接口,貌似称为历史接口了)
     */
    public boolean verifyNotify(HttpServletRequest request) throws AlipayApiException {
        Map<String,String> paranMap = SignUtil.request2Map(request);
        logger.debug("支付宝回调参数:"+paranMap.toString());
        boolean isVerify = false;
        if (AlipayUtil.SUCCESS_REQUEST.equals(paranMap.get("trade_status")) || AlipayUtil.TRADE_CLOSED.equals(paranMap.get("trade_status"))) {
            isVerify = AlipaySignature.rsaCheckV1(paranMap, AlipayUtil.ALIPAY_PUBLIC_KEY, AlipayUtil.CHARSET); //调用SDK验证签名
        }
        logger.debug("支付宝验签结果"+isVerify);
        return isVerify;
    }

    public static void main(String[] args) {
        AlipayUtil alipayUtil = new AlipayUtil();
        String id = alipayUtil.TradeWapPayRequest(new HashMap<>());
        System.out.println(id);
    }

}
