package com.wink.livemall.admin.api.pay;


import com.alibaba.fastjson.JSONObject;
import com.wink.livemall.admin.util.Errors;
import com.wink.livemall.admin.util.HttpJsonResult;
import com.wink.livemall.admin.util.payUtil.ConstantsEJS;
import com.wink.livemall.admin.util.payUtil.VXappPayUtil;
import com.wink.livemall.coupon.dto.LmConpouMember;
import com.wink.livemall.coupon.dto.LmCoupons;
import com.wink.livemall.coupon.service.LmCouponMemberService;
import com.wink.livemall.coupon.service.LmCouponsService;
import com.wink.livemall.order.dto.LmOrder;
import com.wink.livemall.order.dto.LmOrderLog;
import com.wink.livemall.order.dto.LmOrderRefundLog;
import com.wink.livemall.order.dto.LmPayLog;
import com.wink.livemall.order.service.LmMerchOrderService;
import com.wink.livemall.order.service.LmOrderLogService;
import com.wink.livemall.order.service.LmOrderService;
import com.wink.livemall.order.service.LmPayLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.KeyStore;
import java.util.*;

@Api(tags = "WXapp接口")
@Controller
@RequestMapping("/WXapp")
public class VXRefoud {
    Logger log= LogManager.getLogger(VXRefoud.class);
    private int socketTimeout = 10000;// 连接超时时间，默认10秒
    private int connectTimeout = 30000;// 传输超时时间，默认30秒
    private static RequestConfig requestConfig;// 请求器的配置
    private static CloseableHttpClient httpClient;// HTTP请求器


    @Autowired
    private LmPayLogService lmPayLogService;
    @Autowired
    private LmOrderService lmOrderService;
    @Autowired
    private LmOrderLogService lmOrderLogService;

    @Autowired
    private LmCouponMemberService lmCouponMemberService;
    @Autowired
    private LmMerchOrderService LmMerchOrderService;
    @Autowired
    private LmCouponsService lmCouponService;

    @RequestMapping("/wxFefund")
    @ResponseBody
    //具体的调⽤微信的退款接⼝
    @ApiImplicitParam(name = "merOrderId", value = "订单ID", dataType =
            "String",paramType = "query")
    public HttpJsonResult<JSONObject> wxFefund(HttpServletRequest
                                                       request, String merOrderId) throws Exception{
        HttpJsonResult<JSONObject> jsonResult=new HttpJsonResult<>();
        Map<String,Object> returnmap = new HashMap<>();
        //查询是否有相同订单下单请求
        LmPayLog oldlmPayLog = lmPayLogService.findBymerOrderIdAndType(merOrderId,"refund");
        if(oldlmPayLog!=null) {
            returnmap.put("returninfo", oldlmPayLog.getSysmsg());
            Map msgmap =  com.alibaba.fastjson.JSONObject.parseObject(oldlmPayLog.getSysmsg());
            if(msgmap!=null){
                String code = msgmap.get("errCode")+"";
                if(code.equals("SUCCESS")){
                    jsonResult.setData(null);
                    jsonResult.setCode(999);
                    jsonResult.setMsg("已经发生过退款请求");
                    return jsonResult;
                }
            }
        }
        try {
            //具体的调⽤微信的退款接⼝
            String code = ConstantsEJS.CODE_SUCCESS;//状态码
            String msg = ConstantsEJS.REFUND_SUCCESS;//提示信息
            Map<String,String> data = new HashMap<String,String>();
            LmOrder order = lmOrderService.findByOrderId(merOrderId);
            String orderSn = order.getOrderid();
            System.out.println("orderSn:------------------"+orderSn);
            try {
                if (StringUtils.isEmpty(orderSn)){
                    msg = ConstantsEJS.MSG_NULL;
                    jsonResult.setCode(999);
                    jsonResult.setMsg(msg);
                    return jsonResult;
                }else {
                    //退款到⽤户微信
                    BigDecimal payAmount = (order.getRealpayprice().multiply(new
                            BigDecimal(100)));
                    String txnAmt = payAmount.toString().split("\\.")[0];
                    System.out.println("order+++++++++++++++"+order);
                    LmOrderLog lmOrdersLog =
                            lmOrderLogService.findByOrderids(String.valueOf(order.getId())).get(0);
                    String nonce_str = getRandomStringByLength(32);
                    // data.put("userId", String.valueOf(order.getUserId()));
                    data.put("appid", ConstantsEJS.MINI_APPID);
                    data.put("mch_id", ConstantsEJS.WXPAY_PARTNER);
                    data.put("nonce_str", nonce_str);
                    data.put("sign_type", "MD5");
                    data.put("out_trade_no", lmOrdersLog.getOrderid());//商户订单号
                    data.put("out_refund_no", UUID.randomUUID().toString().replaceAll("-", ""));//商户退款单号
                    data.put("total_fee",txnAmt);//⽀付⾦额，微信⽀付提交的⾦额是不能带⼩数点的，且是以分为单位,这边需要转成字符串类型，否则后⾯的签名会失败
                    data.put("refund_fee",txnAmt);//退款总⾦额,订单总⾦额,单位为分,只能为整数
                    data.put("notify_url", ConstantsEJS.NOTIFY_URL_REFUND);//退款成功后的回调地址
                    String preStr = VXappPayUtil.createLinkString(data); // 把数组所有元素，按照“参数=参数值”的模式⽤“&”字符拼接成字符串
                    //MD5运算⽣成签名，这⾥是第⼀次签名，⽤于调⽤统⼀下单接⼝
                    String mySign = VXappPayUtil.sign(preStr,
                            ConstantsEJS.WXPAY_PAY_API_KEY, "utf-8").toUpperCase();
                    data.put("sign", mySign);
                    //拼接统⼀下单接⼝使⽤的xml数据，要将上⼀步⽣成的签名⼀起拼接进去
                    String xmlStr = postData(ConstantsEJS.REFUND_PATH,
                            VXappPayUtil.GetMapToXML(data)); //⽀付结果通知的xml格式数据
                    System.out.println(xmlStr);
                    Map notifyMap = VXappPayUtil.doXMLParse(xmlStr);
                    System.out.println("notifyMap--------"+notifyMap);
                    if ("SUCCESS".equals(notifyMap.get("return_code"))) {
                        if("SUCCESS".equals(notifyMap.get("result_code"))) {
                            data.put("errCode","SUCCESS");
                            data.put("respStr", net.sf.json.JSONObject.fromObject(notifyMap).toString());
                            String resultStr = net.sf.json.JSONObject.fromObject(data).toString();
                            //退款成功的操作
                            String prepay_id = (String) notifyMap.get("prepay_id");//返回的预付单信息
                            System.out.println(prepay_id);
                            Long timeStamp = System.currentTimeMillis() / 1000;
                            //拼接签名需要的参数
                            String stringSignTemp = "appId=" + ConstantsEJS.MINI_APPID +
                                    "&nonceStr=" + nonce_str + "&package=prepay_id=" + prepay_id +
                                    "&signType=MD5&timeStamp=" + timeStamp;
                            //签名算法⽣成签名
                            String paySign = VXappPayUtil.sign(stringSignTemp,
                                    ConstantsEJS.WXPAY_PAY_API_KEY, "utf-8").toUpperCase();
                            data.put("package", "prepay_id=" + prepay_id);
                            data.put("timeStamp", String.valueOf(timeStamp));
                            data.put("paySign", paySign);
                            order.setBackstatus(2);
                           lmOrderService.updateService(order);
                                log.error("⽤户" + order.getMemberid() + "退款成功");
                                //如有需要可以添加退款后业务逻辑
                            LmOrderRefundLog lmOrderRefundLog = LmMerchOrderService.getRefundLogByorderid(String.valueOf(order.getId()));
                            if(lmOrderRefundLog !=null){
                                    lmOrderRefundLog.setType(2);
                                    lmOrderRefundLog.setStatus(2);
                                    LmMerchOrderService.updRefundLog(lmOrderRefundLog);
                                    log.error("⽤户" + order.getMemberid() + "退款⽇志数据已 修改");
                                    order.setRefundid(lmOrderRefundLog.getId());
                                    lmOrderService.updateService(order);
                                    log.error("⽤户" + order.getMemberid() + "退款⽇志ID已存⼊订单表");
                                }
                            //添加记录
                            LmPayLog lmPayLog = new LmPayLog();
                            lmPayLog.setCreatetime(new Date());
                            lmPayLog.setOrderno(merOrderId);
                            lmPayLog.setType("refund");
                            lmPayLog.setSysmsg(resultStr);
                            lmPayLogService.insertService(lmPayLog);
                        }else{
                            System.out.println("退款失败:原 因"+notifyMap.get("err_code_des"));
                            msg = (String)notifyMap.get("err_code_des");
                            jsonResult.setData(null);
                            jsonResult.setCode(999);
                            jsonResult.setMsg(msg);
                            return jsonResult;
                        }
                    }else{
                        System.out.println("退款失败:原因"+notifyMap.get("err_code_des"));
                        msg = (String)notifyMap.get("err_code_des");
                        jsonResult.setData(null);
                        jsonResult.setCode(999);
                        jsonResult.setMsg(msg);
                        return jsonResult;
                    }
                }
            }catch (Exception e) {
                log.error(e.toString(), e);
            }
            jsonResult.setData(null);
            jsonResult.setCode(Errors.SUCCESS.getCode());
            jsonResult.setMsg(Errors.SUCCESS.getMsg());
            return jsonResult;
        }catch (Exception e){
            log.error("修改失败"+e.getMessage());
        }
        return null;
    }


    private  String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
    /**
     * 加载证书
     *
     */
    private static void initCert() throws Exception {
        // 证书密码，默认为商户ID
        String key = ConstantsEJS.WXPAY_PARTNER;
        // 商户证书的路径
        ClassPathResource classPathResource = new ClassPathResource("apiclient_cert.p12");
        // 指定读取证书格式为PKCS12
        KeyStore keyStore = KeyStore.getInstance("PKCS12");

        InputStream inputStream = classPathResource.getInputStream();
//        String path = RefundController.class.getClassLoader().getResource("/apiclient_cert.p12").getPath();

//
//        // 读取本机存放的PKCS12证书文件
//        FileInputStream instream = new FileInputStream(inputStream);
        try {
            // 指定PKCS12的密码(商户ID)
            keyStore.load(inputStream, key.toCharArray());
        } finally {
            inputStream.close();
        }

        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, key.toCharArray()).build();

        // 指定TLS版本
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        // 设置httpclient的SSLSocketFactory
        httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
    }


    /**
     * 通过Https往API post xml数据
     * @param url  API地址
     * @param xmlObj   要提交的XML数据对象
     * @return
     */
    public  String postData(String url, String xmlObj) {
        // 加载证书
        try {
            initCert();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String result = null;
        HttpPost httpPost = new HttpPost(url);
        // 得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
        StringEntity postEntity = new StringEntity(xmlObj, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.setEntity(postEntity);
        // 根据默认超时限制初始化requestConfig
        requestConfig = RequestConfig.custom()
                .setSocketTimeout(socketTimeout)
                .setConnectTimeout(connectTimeout)
                .build();
        // 设置请求器的配置
        httpPost.setConfig(requestConfig);
        try {
            HttpResponse response = null;
            try {
                response = httpClient.execute(httpPost);
            }  catch (IOException e) {
                e.printStackTrace();
            }
            HttpEntity entity = response.getEntity();
            try {
                result = EntityUtils.toString(entity, "UTF-8");
            }  catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            httpPost.abort();
        }
        return result;
    }

}
