package com.wink.livemall.admin.api.pay;


import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.WeixinpayUtil;
import com.wink.livemall.admin.util.payUtil.PayCommonUtil;
import com.wink.livemall.admin.util.payUtil.PayUtil;
import com.wink.livemall.admin.util.payUtil.StringUtil;
import com.wink.livemall.live.dto.LmLive;
import com.wink.livemall.live.dto.LmLiveRenew;
import com.wink.livemall.live.service.LmLiveRenewService;
import com.wink.livemall.live.service.LmLiveService;
import com.wink.livemall.merch.dto.LmMerchInfo;
import com.wink.livemall.merch.service.LmMerchInfoService;
import com.wink.livemall.order.dto.LmPayLog;
import com.wink.livemall.order.service.LmPayLogService;
import com.wink.livemall.sys.setting.dto.Configs;
import com.wink.livemall.sys.setting.service.ConfigsService;
import io.swagger.annotations.*;
import net.sf.json.JSONObject;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;

@Api(tags = "直播间支付接口")
@RestController
@RequestMapping("/payLive")
public class PayLiveController {

    private static final Logger logger = LogManager.getLogger(PayLiveController.class);

    @Autowired
    private ConfigsService configsService;
    @Autowired
    private LmPayLogService lmPayLogService;
    @Autowired
    private LmLiveRenewService lmLiveRenewService;
    @Autowired
    private LmLiveService lmLiveService;
    @Autowired
    private LmMerchInfoService lmMerchInfoService;

    private static final String  SUCCESS_KEY= "SUCCESS";
    private static final String  FAILED_KEY= "FAILED";

    /**
     * 下单请求模块
     * wx支付宝云闪付下单
     */
    @ApiOperation(value = "下单请求模块")
    @RequestMapping(value = "/live", method = RequestMethod.POST)
    @Transactional(propagation = Propagation.REQUIRED)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型0支付宝1微信2云支付",required =true,  dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "merId", value = "店铺Id", dataType = "Integer",paramType = "query",required =false),
            @ApiImplicitParam(name = "totalAmount", value = "总价 ", dataType = "Integer",paramType = "query",required =false)
    })
    public JsonResult order(HttpServletRequest request,Integer type, Integer merId, long totalAmount) {
        JsonResult jsonResult = new JsonResult();
        Configs configs =configsService.findByTypeId(Configs.live_pay);
        Map map =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
        String msgSrcId=map.get("msgSrcId")+"";
        String livePaySn = msgSrcId+System.currentTimeMillis()+ new Random().nextInt(10);
        LmLiveRenew lmLiveRenew=new LmLiveRenew();
        lmLiveRenew.setPaystatus(type);
        lmLiveRenew.setLive_pay_sn(livePaySn);
        lmLiveRenew.setMerch_id(merId);
        lmLiveRenewService.addLivePay(lmLiveRenew);
        try {
            if(type==1){
                WeixinpayUtil weixinpayUtil = new WeixinpayUtil();
                Map<String,String> returnLive = weixinpayUtil.wxLivePayFunction(totalAmount+"",livePaySn,"微信下单",getIpAddr(request));
                jsonResult.setCode(JsonResult.SUCCESS);
                jsonResult.setData(returnLive);
                return jsonResult;
            }else {
                String  payStatus="";
                if (type==0) {
                    payStatus ="trade.precreate";
                }else if (type==1) {
                    payStatus ="wx.unifiedOrder";
                }else if (type==2) {
                    payStatus ="uac.appOrder";
                }
                //组织请求报文
                JSONObject json = new JSONObject();
                json.put("instMid", map.get("instMid"));
                json.put("mid", map.get("mid"));
                json.put("merOrderId", livePaySn);
                json.put("msgSrc", map.get("msgSrc"));
                json.put("msgType", payStatus);
                json.put("notifyUrl", map.get("notifyUrl"));
                //是否要在商户系统下单，看商户需求  createBill()
                json.put("requestTimestamp", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                json.put("signType", "SHA256");
                json.put("tid", map.get("tid"));
                json.put("totalAmount", totalAmount);
                Map<String, String> paramsMap = PayUtil.jsonToMap(json);
                paramsMap.put("sign", PayUtil.makeSign(map.get("MD5Key")+"", paramsMap));
                System.out.println("paramsMap："+paramsMap);
                String strReqJsonStr = JSON.toJSONString(paramsMap);
                System.out.println("strReqJsonStr:"+strReqJsonStr);
                //调用银商平台获取二维码接口
                HttpURLConnection httpURLConnection = null;
                BufferedReader in = null;
                PrintWriter out = null;
                String resultStr = null;
                Map<String,String> resultMap = new HashMap<String,String>();
                URL url = new URL(map.get("APIurl") + "");
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Content_Type", "application/json");
                httpURLConnection.setRequestProperty("Accept_Charset", "UTF-8");
                httpURLConnection.setRequestProperty("contentType", "UTF-8");
                //发送POST请求参数
                out = new PrintWriter(httpURLConnection.getOutputStream());
                out.write(strReqJsonStr);
                out.flush();
                //读取响应
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    StringBuffer content = new StringBuffer();
                    String tempStr = null;
                    in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
                    while ((tempStr = in.readLine()) != null) {
                        content.append(tempStr);
                    }
                    System.out.println("content:" + content.toString());
                    //转换成json对象
                    com.alibaba.fastjson.JSONObject respJson = JSON.parseObject(content.toString());
                    String resultCode = respJson.getString("errCode");
                    resultMap.put("errCode", resultCode);
                    resultMap.put("respStr", respJson.toString());
                    resultStr = JSONObject.fromObject(resultMap).toString();
                    jsonResult.setCode(JsonResult.SUCCESS);
                    jsonResult.setData(resultStr);
                    if (out != null) {
                        out.close();
                    }
                    httpURLConnection.disconnect();
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            jsonResult.setCode(JsonResult.ERROR);
            jsonResult.setMsg("调用银商接口出现异常：" + e.toString());
            return jsonResult;
        }
        return jsonResult;
    }

    /**
     * 查询订单
     * @param request
     * @param livePaySn
     * @return
     */
    @ApiOperation(value = "查询订单")
    @RequestMapping(value = "/isOrder", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "livePaySn", value = "订单号", dataType = "String",paramType = "query",required =false)
    })
    public JsonResult isOrder(HttpServletRequest request, String livePaySn) {
        JsonResult jsonResult = new JsonResult();
        try {
            LmLiveRenew lmLiveRenew = lmLiveRenewService.findLmLiveRenew(livePaySn);
            if(lmLiveRenew.getState()==1){
                jsonResult.setCode(JsonResult.SUCCESS);
                jsonResult.setMsg("支付成功");
            }else {
                jsonResult.setCode(JsonResult.ERROR);
                jsonResult.setMsg("支付失败");
            }
        }catch (Exception e) {
            jsonResult.setCode(JsonResult.ERROR);
            jsonResult.setMsg(e.toString());
            return jsonResult;
        }
        return jsonResult;
    }



    /**
     * 微信支付成功回调接口
     * 直播间续费
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws JDOMException
     */
    @RequestMapping(value = "/liveWeiXinPay", produces = MediaType.APPLICATION_JSON_VALUE)
    public String notifyWeiXinPay(HttpServletRequest request, HttpServletResponse response) throws IOException, JDOMException {
        logger.info("微信支付回调");
        InputStream inStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        String resultxml = new String(outSteam.toByteArray(), "utf-8");
        Map<String, String> params = PayCommonUtil.doXMLParse(resultxml);
        outSteam.close();
        inStream.close();
        Map<String,String> return_data = new HashMap<String,String>();
        String livePaySn = String.valueOf(params.get("out_trade_no"));
        LmPayLog PayLog = lmPayLogService.findBymerOrderIdAndType(livePaySn,"livePay");
        if(PayLog!=null){
            return_data.put("return_code", "FAIL");
            return_data.put("return_msg", "已支付成功");
            return StringUtil.GetMapToXML(return_data);
        }
        if (!PayCommonUtil.isTenpaySign(params)) {
            logger.info("微信验签不通过");
            // 支付失败
            return_data.put("return_code", "FAIL");
            return_data.put("return_msg", "return_code不正确");
            return StringUtil.GetMapToXML(return_data);
        } else {
            String total_fee = String.valueOf(params.get("total_fee"));
            logger.info("付款成功，执行回调操作"+livePaySn+total_fee);
            return_data.put("return_code", "SUCCESS");
            return_data.put("return_msg", "OK");
            LmLiveRenew lmLiveRenew = lmLiveRenewService.findLmLiveRenew(livePaySn);
            if(lmLiveRenew.getState()==1){
                return StringUtil.GetMapToXML(return_data);
            }
            BigDecimal livePayPrice=new  BigDecimal(total_fee);
            livePayPrice=livePayPrice.divide(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP);
            lmLiveRenew.setState(1);
            lmLiveRenew.setPrice(livePayPrice);
            lmLiveRenewService.updLivePay(lmLiveRenew);
            LmLive lmLive = lmLiveService.findByMerchid(lmLiveRenew.getMerch_id());
            Calendar calendar = new GregorianCalendar();
            if(lmLive!=null){
                lmLive.setStatus(0);
                if(lmLive.getEnd_time()==null){
                    calendar.setTime(new Date());
                    calendar.add(Calendar.YEAR, +1);
                    lmLive.setEnd_time(calendar.getTime());
                }else {
                    if(System.currentTimeMillis()>lmLive.getEnd_time().getTime()){
                        calendar.setTime(new Date());
                        calendar.add(Calendar.YEAR, +1);
                        lmLive.setEnd_time(calendar.getTime());
                    }else {
                        calendar.setTime(lmLive.getEnd_time());
                        calendar.add(Calendar.YEAR, +1);
                        lmLive.setEnd_time(calendar.getTime());
                    }
                }
                lmLiveService.updateService(lmLive);
            }else {
                LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(String.valueOf(lmLiveRenew.getMerch_id()));
                lmMerchInfo.setIslive(1);
                lmMerchInfoService.updateService(lmMerchInfo);
            }
            //记录保存
            LmPayLog lmPayLog = new LmPayLog();
            lmPayLog.setCreatetime(new Date());
            lmPayLog.setOrderno(livePaySn);
            lmPayLog.setType("livePay");
            lmPayLog.setSysmsg(new Gson().toJson(params));
            lmPayLogService.insertService(lmPayLog);
        }
        return StringUtil.GetMapToXML(return_data);
    }



    /**
     * 支付宝 云闪付交易成功回调接口
     * @param request
     * @return
     */
    @ApiOperation(value = "交易成功反馈信息")
    @RequestMapping(value = "/liveSuccess",method = RequestMethod.POST)
    public String orderQuery(HttpServletRequest request){
        String livePaySn = request.getParameter("merOrderId");
        String totalAmount = request.getParameter("totalAmount");
        Configs configs =configsService.findByTypeId(Configs.live_pay);
        Map map =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
        BigDecimal livePayPrice=new  BigDecimal(totalAmount);
        livePayPrice=livePayPrice.divide(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP);
        Map<String, String> params = PayUtil.getRequestParams(request);
        System.out.println("params："+params);
        LmPayLog oldlmPayLog = lmPayLogService.findBymerOrderIdAndType(livePaySn,"livePay");
        if(oldlmPayLog!=null) {
            Map msgmap =  com.alibaba.fastjson.JSONObject.parseObject(oldlmPayLog.getSysmsg());
            if(msgmap!=null){
                String code = msgmap.get("errCode")+"";
                if(code.equals("SUCCESS")){
                    logger.info("已经发生过退款请求。。。。。。。。。livePaySn:"+livePaySn);
                    return SUCCESS_KEY;
                }
            }
        }
        // 验签
        boolean checkRet = PayUtil.checkSign(map.get("MD5Key")+"", params);
        try {
            if(checkRet){
                //添加记录
                LmLiveRenew lmLiveRenew = lmLiveRenewService.findLmLiveRenew(livePaySn);
                if(lmLiveRenew.getState()==1){
                    return SUCCESS_KEY;
                }
                lmLiveRenew.setState(1);
                lmLiveRenew.setPrice(livePayPrice);
                lmLiveRenewService.updLivePay(lmLiveRenew);
                LmLive lmLive = lmLiveService.findByMerchid(lmLiveRenew.getMerch_id());
                Calendar calendar = new GregorianCalendar();
                if(lmLive!=null){
                    lmLive.setStatus(0);
                    if(lmLive.getEnd_time()==null){
                        calendar.setTime(new Date());
                        calendar.add(Calendar.YEAR, +1);
                        lmLive.setEnd_time(calendar.getTime());
                    }else {
                        if(System.currentTimeMillis()>lmLive.getEnd_time().getTime()){
                            calendar.setTime(new Date());
                            calendar.add(Calendar.YEAR, +1);
                            lmLive.setEnd_time(calendar.getTime());
                        }else {
                            calendar.setTime(lmLive.getEnd_time());
                            calendar.add(Calendar.YEAR, +1);
                            lmLive.setEnd_time(calendar.getTime());
                        }
                    }
                    lmLiveService.updateService(lmLive);
                }else {
                    LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(String.valueOf(lmLiveRenew.getMerch_id()));
                    lmMerchInfo.setIslive(1);
                    lmMerchInfoService.updateService(lmMerchInfo);
                }
                //记录保存
                LmPayLog lmPayLog = new LmPayLog();
                lmPayLog.setCreatetime(new Date());
                lmPayLog.setOrderno(livePaySn);
                lmPayLog.setType("livePay");
                lmPayLog.setSysmsg(new Gson().toJson(params));
                lmPayLogService.insertService(lmPayLog);
                return SUCCESS_KEY;
            }else{
                return FAILED_KEY;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FAILED_KEY;
    }


    /**
     * 获取当前网络ip
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request){
        String ipAddress = request.getHeader("x-forwarded-for");
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if(ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")){
                //根据网卡取本机配置的IP
                InetAddress inet=null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress= inet.getHostAddress();
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15
            if(ipAddress.indexOf(",")>0){
                ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }
}
