package com.wink.livemall.admin.api.help.impl;

import com.alibaba.fastjson.JSON;
import com.wink.livemall.admin.api.help.CommentService;
import com.wink.livemall.admin.util.WeixinpayUtil;
import com.wink.livemall.admin.util.payUtil.ConstantsEJS;
import com.wink.livemall.admin.util.payUtil.PayUtil;
import com.wink.livemall.admin.util.payUtil.VXappPayUtil;
import com.wink.livemall.member.dao.LmFalsifyDao;
import com.wink.livemall.member.dto.LmFalsify;
import com.wink.livemall.order.dao.LmPayLogDao;
import com.wink.livemall.order.dto.LmOrderLog;
import com.wink.livemall.order.dto.LmPayLog;
import com.wink.livemall.sys.setting.dao.ConfigsDao;
import com.wink.livemall.sys.setting.dto.Configs;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.util.*;


@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    private LmFalsifyDao lmFalsifyDao;
    @Resource
    private ConfigsDao configsDao;
    @Resource
    private LmPayLogDao lmPayLogDao;

    @Override
    public Map<String, Object> autoRefundFalsify(String falsifyId, String refundAmount) throws Exception {
        LmFalsify lmFalsify = lmFalsifyDao.findFalsifyId(falsifyId);
        Map<String,Object> returnmap = new HashMap<>();
        //???????????????????????????????????????
        LmPayLog oldlmPayLog = lmPayLogDao.findBymerOrderIdAndType(falsifyId,"refund");
        if(oldlmPayLog!=null) {
            returnmap.put("returninfo", oldlmPayLog.getSysmsg());
            Map msgmap =  com.alibaba.fastjson.JSONObject.parseObject(oldlmPayLog.getSysmsg());
            if(msgmap!=null){
                String code = msgmap.get("errCode")+"";
                if(code.equals("SUCCESS")){
                    return returnmap;
                }
            }
        }

        if(1==lmFalsify.getPaystatus()){
            //??????????????????
            Map<String,String> resultMap = new HashMap<String,String>();
            try {
                WeixinpayUtil weixinpayUtil = new WeixinpayUtil();
                Map<String,String> weixinrefund=weixinpayUtil.falsifyRefund(refundAmount,falsifyId);
                if (weixinrefund.get("return_code").equals("SUCCESS")){
                    lmFalsify.setStatus(2);
                    lmFalsifyDao.updateByPrimaryKeySelective(lmFalsify);
                    weixinrefund.put("refundStatus","SUCCESS");
                    resultMap.put("errCode","SUCCESS");
                }else {
                    weixinrefund.put("refundStatus","FAIL");
                    resultMap.put("errCode","FAIL");
                }
                resultMap.put("respStr",JSONObject.fromObject(weixinrefund).toString());
                String resultStr = JSONObject.fromObject(resultMap).toString();
                returnmap.put("returninfo",resultStr);
                //????????????
                LmPayLog lmPayLog = new LmPayLog();
                lmPayLog.setCreatetime(new Date());
                lmPayLog.setOrderno(falsifyId);
                lmPayLog.setType("refund");
                lmPayLog.setSysmsg(resultStr);
                lmPayLogDao.insertSelective(lmPayLog);
            } catch (Exception e) {
                e.printStackTrace();
                resultMap.put("errCode","HttpURLException");
                resultMap.put("msg","???????????????????????????"+e.toString());
                String resultStr = JSONObject.fromObject(resultMap).toString();
                returnmap.put("returninfo",resultStr);
            }
        }else if(3==lmFalsify.getPaystatus()){
                Map<String,String> resultMap = new HashMap<String,String>();
                String nonce_str = getRandomStringByLength(32);
                // data.put("userId", String.valueOf(order.getUserId()));
                resultMap.put("appid", ConstantsEJS.MINI_APPID);
                resultMap.put("mch_id", ConstantsEJS.WXPAY_PARTNER);
                resultMap.put("nonce_str", nonce_str);
                resultMap.put("sign_type", "MD5");
                resultMap.put("out_trade_no", falsifyId);//???????????????
                resultMap.put("out_refund_no", UUID.randomUUID().toString().replaceAll("-", ""));//??????????????????
                resultMap.put("total_fee",refundAmount);//??????????????????????????????????????????????????????????????????????????????????????????,??????????????????????????????????????????????????????????????????
                resultMap.put("refund_fee",refundAmount);//???????????????,???????????????,????????????,???????????????
                resultMap.put("notify_url", ConstantsEJS.NOTIFY_URL_REFUND);//??????????????????????????????
                String preStr = VXappPayUtil.createLinkString(resultMap); // ???????????????????????????????????????=???????????????????????????&???????????????????????????
                //MD5??????????????????????????????????????????????????????????????????????????????
                String mySign = VXappPayUtil.sign(preStr,
                        ConstantsEJS.WXPAY_PAY_API_KEY, "utf-8").toUpperCase();
                resultMap.put("sign", mySign);
                //?????????????????????????????????xml?????????????????????????????????????????????????????????
                String xmlStr = postData(ConstantsEJS.REFUND_PATH,
                        VXappPayUtil.GetMapToXML(resultMap)); //?????????????????????xml????????????
                System.out.println(xmlStr);
                Map notifyMap = VXappPayUtil.doXMLParse(xmlStr);
                System.out.println("notifyMap--------"+notifyMap);
                if ("SUCCESS".equals(notifyMap.get("return_code"))) {
                    if("SUCCESS".equals(notifyMap.get("result_code"))) {
                        resultMap.put("errCode","SUCCESS");
                        resultMap.put("respStr",JSONObject.fromObject(notifyMap).toString());
                        String resultStr = JSONObject.fromObject(resultMap).toString();
                        //?????????????????????
                        String prepay_id = (String) notifyMap.get("prepay_id");//????????????????????????
                        System.out.println(prepay_id);
                        Long timeStamp = System.currentTimeMillis() / 1000;
                        //???????????????????????????
                        String stringSignTemp = "appId=" + ConstantsEJS.MINI_APPID +
                                "&nonceStr=" + nonce_str + "&package=prepay_id=" + prepay_id +
                                "&signType=MD5&timeStamp=" + timeStamp;
                        //????????????????????????
                        String paySign = VXappPayUtil.sign(stringSignTemp,
                                ConstantsEJS.WXPAY_PAY_API_KEY, "utf-8").toUpperCase();
                        returnmap.put("package", "prepay_id=" + prepay_id);
                        returnmap.put("timeStamp", String.valueOf(timeStamp));
                        returnmap.put("paySign", paySign);
                        lmFalsify.setStatus(2);
                        lmFalsifyDao.updateByPrimaryKeySelective(lmFalsify);
                        //????????????
                        LmPayLog lmPayLog = new LmPayLog();
                        lmPayLog.setCreatetime(new Date());
                        lmPayLog.setOrderno(falsifyId);
                        lmPayLog.setType("refund");
                        lmPayLog.setSysmsg(resultStr);
                        lmPayLogDao.insertSelective(lmPayLog);
                    }else{
                        System.out.println("????????????:??? ???"+notifyMap.get("err_code_des"));
                    }
                }else{
                    System.out.println("????????????:??????"+notifyMap.get("err_code_des"));
                }


        }else{
            // ????????? ?????????????????????
            Configs configs =configsDao.findByTypeId(Configs.falsify_pay);
            Map map =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
            returnmap.put("config",configs.getConfig());
            //??????????????????
            JSONObject json = new JSONObject();
            json.put("mid",  map.get("mid"));
            json.put("tid",map.get("tid"));
            json.put("msgType", "refund");
            json.put("msgSrc", map.get("msgSrc"));
            json.put("instMid", map.get("instMid"));
            json.put("merOrderId", falsifyId);
            //????????????????????????????????????????????????  createBill()
            json.put("refundAmount",refundAmount);
            json.put("requestTimestamp", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            json.put("signType", "SHA256");
            Map<String, String> paramsMap = PayUtil.jsonToMap(json);
            paramsMap.put("sign", PayUtil.makeSign(map.get("MD5Key")+"", paramsMap));
            String strReqJsonStr = JSON.toJSONString(paramsMap);
            //???????????????????????????????????????
            HttpURLConnection httpURLConnection = null;
            BufferedReader in = null;
            PrintWriter out = null;
            String resultStr = null;
            Map<String,String> resultMap = new HashMap<String,String>();
            if (!StringUtils.isNotBlank(map.get("APIurl")+"")) {
                resultMap.put("errCode","URLFailed");
                resultStr = JSONObject.fromObject(resultMap).toString();
                returnmap.put("returninfo",resultStr);
            }
            try {
                URL url = new URL(map.get("APIurl")+"");
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Content_Type","application/json");
                httpURLConnection.setRequestProperty("Accept_Charset","UTF-8");
                httpURLConnection.setRequestProperty("contentType","UTF-8");
                //??????POST????????????
                out = new PrintWriter(httpURLConnection.getOutputStream());
                out.write(strReqJsonStr);
                out.flush();
                //????????????
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    StringBuffer content = new StringBuffer();
                    String tempStr = null;
                    in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"UTF-8"));
                    while ((tempStr=in.readLine()) != null){
                        content.append(tempStr);
                    }
                    //?????????json??????
                    com.alibaba.fastjson.JSONObject respJson = JSON.parseObject(content.toString());
                    String resultCode = respJson.getString("errCode");
                    if (resultCode.equals("SUCCESS")){
                        lmFalsify.setStatus(2);
                        lmFalsifyDao.updateByPrimaryKeySelective(lmFalsify);
                        resultMap.put("msg",respJson.getString("errMsg"));
                    }else {
                        resultMap.put("msg","?????????????????????????????????");
                    }
                    resultMap.put("errCode",resultCode);
                    resultMap.put("respStr",respJson.toString());
                    resultStr = JSONObject.fromObject(resultMap).toString();
                    returnmap.put("returninfo",resultStr);
                    //????????????
                    LmPayLog lmPayLog = new LmPayLog();
                    lmPayLog.setCreatetime(new Date());
                    lmPayLog.setOrderno(falsifyId);
                    lmPayLog.setType("refund");
                    lmPayLog.setSysmsg(resultStr);
                    lmPayLogDao.insertSelective(lmPayLog);
                }
            } catch (Exception e) {
                e.printStackTrace();
                resultMap.put("errCode","HttpURLException");
                resultMap.put("msg","?????????????????????????????????"+e.toString());
                resultStr = JSONObject.fromObject(resultMap).toString();
                returnmap.put("returninfo",resultStr);
            }finally {
                if (out != null) {
                    out.close();
                }
                httpURLConnection.disconnect();
            }
        }

        return returnmap;
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
     * ????????????
     *
     */
    private static void initCert() throws Exception {
        // ??????????????????????????????ID
        String key = ConstantsEJS.WXPAY_PARTNER;
        // ?????????????????????
        ClassPathResource classPathResource = new ClassPathResource("apiclient_cert.p12");
        // ???????????????????????????PKCS12
        KeyStore keyStore = KeyStore.getInstance("PKCS12");

        InputStream inputStream = classPathResource.getInputStream();
//        String path = RefundController.class.getClassLoader().getResource("/apiclient_cert.p12").getPath();

//
//        // ?????????????????????PKCS12????????????
//        FileInputStream instream = new FileInputStream(inputStream);
        try {
            // ??????PKCS12?????????(??????ID)
            keyStore.load(inputStream, key.toCharArray());
        } finally {
            inputStream.close();
        }

        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, key.toCharArray()).build();

        // ??????TLS??????
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        // ??????httpclient???SSLSocketFactory
        httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
    }

    private int socketTimeout = 10000;// ???????????????????????????10???
    private int connectTimeout = 30000;// ???????????????????????????30???
    private static RequestConfig requestConfig;// ??????????????????
    private static CloseableHttpClient httpClient;// HTTP?????????

    /**
     * ??????Https???API post xml??????
     * @param url  API??????
     * @param xmlObj   ????????????XML????????????
     * @return
     */
    public  String postData(String url, String xmlObj) {
        // ????????????
        try {
            initCert();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String result = null;
        HttpPost httpPost = new HttpPost(url);
        // ???????????????UTF-8??????????????????API?????????XML??????????????????????????????
        StringEntity postEntity = new StringEntity(xmlObj, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.setEntity(postEntity);
        // ?????????????????????????????????requestConfig
        requestConfig = RequestConfig.custom()
                .setSocketTimeout(socketTimeout)
                .setConnectTimeout(connectTimeout)
                .build();
        // ????????????????????????
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
