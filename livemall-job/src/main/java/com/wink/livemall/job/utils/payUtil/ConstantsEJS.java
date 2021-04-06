package com.wink.livemall.job.utils.payUtil;

public class ConstantsEJS {
    public static final String SERVICE_RESULT_CODE_SYSERROR            = "syserror";
    public static final String SERVICE_RESULT_EXCEPTION_SYSERROR       ="服务异常，请联系系统管理员。";

    /**
     * 官方userId
     */
    public static final int OFFICILA_USERID                         = 148;

    /**
     * 微信支付access-token
     */
    public static final String WX_ACCESSS_TOKEN                         = "wx_access_token";


    /** 验证码key */
    public static final String VERIFY_NUMBER_NAME                      = "verify_number";

    /**
     * 和商城的变量同步，存放商家上传图片内容，以seller/商家ID
     */
    public final static String SELLER_ID                               = "seller_id";

    /**
     * 商家申请上传图片
     */
    public final static String SELLER_APPLY                             = "seller_apply";
    
    /**
     * 用户头像上传图片
     */
    public final static String USER_HEAD                                = "user_head";
    
    /**
     * 和商城的变量同步，存放所有品牌信息，由平台上传
     */
    public final static String BRAND                                    = "brand";


    public final static String REGRIONS                                 = "{\"id\":\"654201\",\"regionName\":\"塔城市\"},{\"id\":\"654226\",\"regionName\":\"和布克赛尔蒙古自治县\"}]}]}]";


    public final static String WXPAY_CREATE_ORDER_URL                  ="https://api.mch.weixin.qq.com/pay/unifiedorder";

    //小程序首页微信支付 h5
    public final static String WXPAY_NOTIFY_URL                        ="https://shop.xunshun.net/notify/wxNotify";

    //小程序支付保证金URL
    public final static String WXPAY_CASH_NOTIFY_URL                   ="http://api.xunshun.net/notify/wxCashNotify";

    public final static String WXPAY_GET_JSAPI_TICKET                  ="https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";


    public final static String WXPAY_ACCESS_TOKEN                       ="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";

    //微信消息模板
    public final static String WXPAY_MESSAGE_TEMPLATE                   ="https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send";

    //微信内容安全检测
    public final static String WXPAY_CONTENT_CHECK                      ="https://api.weixin.qq.com/wxa/msg_sec_check";

    //微信图片和媒体安全检测
    public final static String WXPAY_MEDIA_CHECK                        ="https://api.weixin.qq.com/wxa/media_check_async";

    //发送微信客服文本消息
    public final static String WXPAY_SERVICE_MESSAGE                    ="https://api.weixin.qq.com/cgi-bin/message/custom/send";

    //重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节
    public final static String WXPAY_STATE                              ="123";

    public final static String WXPAY_SCOPE                              ="snsapi_base";

    public final static String WXPAY_USERINFO                           ="snsapi_userinfo";

    //映山红商户号
    public final static String WXPAY_PARTNER2                           ="1382171702";

    //微信商户的key  签名使用
    public static final String WXPAY_PAY_API_KEY2                       = "eYEDg3BDluzNVtKvj3y1GzkU4TVQ0vN5";

//    //百灵鸟商户号
//    public final static String MCH_ID                                   ="1504442641";
//
//    //微信商户的key  签名使用
//    public static final String API_SECRET                               = "j78s7cQ4VBVDhwpgimFFda1lO5VBvqV2";

    //商户号
    public final static String WXPAY_PARTNER                            ="1603650355";
    //商户号的wxkey
    public final static String WXPAY_PAY_API_KEY                        ="mqcatmz3io4qoinf0ed01wthr21h0yfg";
    //获取网页授权access-token地址,无需修改
    public final static String WXPAY_OAUTH_TOKEN                        ="https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

    public final  static String WXPAY_WEBSIT                            ="http://minishop.52mfarm.com";

    //网页授权地址，无需修改
    public final static String WXPAY_OAUTH2_URL                         ="https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";

    public final static String MINI_APPID                               = "wx8d3dd4f8ecbd3b11";

    public final static String MINI_APPSECRET                           = "f1e68b535b63f93fcfde488e37c8b5b4";

    public final static String JSCODE_2_SESSION                         = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";

    public final static String CODE_SUCCESS                             = "000";

    public final static String CODE_ERROR                               = "999";

    public final static String REFUND_SUCCESS                           = "退款成功";

    public final static String MSG_NULL                                 = "无效的订单";

    public final static String MSG_01                                   = "签名错误";

    public final static String NOTIFY_URL_RECHARGE                      ="http://minishop.52mfarm.com/wxpay/recharge";

    public final static String NOTIFY_URL_REFUND                        ="http://minishop.52mfarm.com/wxpay/refundnotify";

    public final static String REFUND_PATH                              = "https://api.mch.weixin.qq.com/secapi/pay/refund";

    public final static String MERCHANT_ID                              = "100000001305563";

    public final static String SIGN_TYPE                                = "RSA";

    public final static String CHARSET                                  = "UFT-8";

    public final static String KEY                                      = "0db93106c08b88659ea454babc88b5c98gagd7a4ad7aaa6g107a8af4e6e590bc";

}
