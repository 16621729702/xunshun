package com.wink.livemall.admin.api.pay;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPayUtil;
import com.google.gson.Gson;
import com.qiniu.util.Json;
import com.wink.livemall.admin.util.*;
import com.wink.livemall.admin.util.httpclient.HttpClient;
import com.wink.livemall.admin.util.payUtil.PayCommonUtil;
import com.wink.livemall.admin.util.payUtil.PayUtil;
import com.wink.livemall.admin.util.payUtil.StringUtil;
import com.wink.livemall.goods.dto.Good;
import com.wink.livemall.goods.dto.LivedGood;
import com.wink.livemall.goods.dto.LmGoodAuction;
import com.wink.livemall.goods.dto.LmShareGood;
import com.wink.livemall.goods.service.GoodService;
import com.wink.livemall.live.dto.LmLive;
import com.wink.livemall.live.service.LmLiveService;
import com.wink.livemall.member.dto.LmMember;
import com.wink.livemall.member.dto.LmMemberAddress;
import com.wink.livemall.member.dto.LmMemberLevel;
import com.wink.livemall.member.service.LmMemberLevelService;
import com.wink.livemall.member.service.LmMemberService;
import com.wink.livemall.merch.dto.LmMerchInfo;
import com.wink.livemall.merch.dto.LmMerchMarginLog;
import com.wink.livemall.merch.service.LmMerchInfoService;
import com.wink.livemall.merch.service.LmMerchMarginLogService;
import com.wink.livemall.order.dto.LmOrder;
import com.wink.livemall.order.dto.LmOrderGoods;
import com.wink.livemall.order.dto.LmPayLog;
import com.wink.livemall.order.service.LmMerchOrderService;
import com.wink.livemall.order.service.LmOrderGoodsService;
import com.wink.livemall.order.service.LmOrderService;
import com.wink.livemall.order.service.LmPayLogService;
import com.wink.livemall.sys.setting.dto.Configs;
import com.wink.livemall.sys.setting.dto.Version;
import com.wink.livemall.sys.setting.service.ConfigsService;
import com.wink.livemall.utils.cache.redis.RedisUtil;
import com.wink.livemall.utils.sms.SmsUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Description;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;

@Api(tags = "????????????")
@RestController
@RequestMapping("/pay")
public class PayController {
    private static final Logger logger = LogManager.getLogger(PayController.class);


   @Autowired
   private RedisUtil redisUtils;
    @Autowired
    private ConfigsService configsService;
    @Autowired
    private LmPayLogService lmPayLogService;
    @Autowired
    private GoodService goodService;
    @Autowired
    private LmOrderService lmOrderService;
    @Autowired
    private LmOrderGoodsService lmOrderGoodsService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private LmMemberService lmMemberService;
    @Autowired
    private LmMerchInfoService lmMerchInfoService;
    @Autowired
    private LmLiveService lmLiveService;
    @Autowired
    private LmMemberLevelService lmMemberLevelService;
    @Autowired
    private LmMerchOrderService lmMerchOrderService;
    @Autowired
    private LmMerchMarginLogService lmMerchMarginLogService;



    private Long getorderprice(LmOrder lmOrder, Long totalAmount) {
        if (lmOrder.getType() == 3) {
            LmOrderGoods orderGood = lmOrderGoodsService.findByOrderid(lmOrder.getId());
            LmShareGood shareGood = goodService.findshareById(orderGood.getGoodid());
            //????????????
            BigDecimal price = shareGood.getChipped_price().multiply(new BigDecimal("1")).multiply(new BigDecimal("100"));
            //????????????????????? ??????????????????
            if (lmOrder.getType() == 3 && lmOrder.getIsprepay() == 1) {
                //????????????
                BigDecimal payprice;
                //?????????????????????
                if (lmOrder.getDeposit_type() == 0) {
                    payprice = price.multiply(new BigDecimal("0.3"));
                    if (shareGood.getChipped_num() == 1) {
                        BigDecimal s = price.multiply(new BigDecimal("0.05"));
                        payprice = payprice.add(s);
                    }
                } else {
                    payprice = price.multiply(new BigDecimal("0.3"));
                    if (shareGood.getChipped_num() == 1) {
                        BigDecimal s = price.multiply(new BigDecimal("0.05"));
                        payprice = payprice.add(s);
                    }
                    payprice = lmOrder.getRealpayprice().multiply(new BigDecimal("100")).subtract(payprice);
                }
                System.out.println(payprice.longValue());
                totalAmount = payprice.longValue();
            } else {
                if (shareGood.getChipped_num() == 1) {
                    totalAmount = price.multiply(new BigDecimal("1.05")).longValue();
                }
            }
        }
        return totalAmount;
    }

    /**
     * ??????????????????
     * ????????????????????????
     */
    @ApiOperation(value = "??????????????????")
    @RequestMapping(value = "/order", method = RequestMethod.POST)
    @Transactional(propagation = Propagation.REQUIRED)
    public Map<String, Object> order(HttpServletRequest request, HttpServletResponse response, @ApiParam(name = "merOrderId", value = "?????????", required = true) @RequestParam(value = "merOrderId", defaultValue = "0") String merOrderId,
                                     @ApiParam(name = "type", value = "????????????", required = true) @RequestParam(value = "type", defaultValue = "0") String type,
                                     @ApiParam(name = "totalAmount", value = "??????", required = true) @RequestParam(value = "totalAmount", defaultValue = "0") long totalAmount
    ) {

        Map<String, Object> returnmap = new HashMap<>();
        LmOrder lmOrder = lmOrderService.findByOrderId(merOrderId);
        if (lmOrder == null) {
            return returnmap;
        }
        LmOrderGoods byOrderid = lmOrderGoodsService.findByOrderid(lmOrder.getId());
        String redisKey = "livepay" + byOrderid.getGoodid();

        System.out.println("??????????????????merOrderId{}totalAmount{}???" + merOrderId + "  " + totalAmount);
        totalAmount = getorderprice(lmOrder, lmOrder.getRealpayprice().multiply(new BigDecimal("100")).longValue());
        //???????????????????????????????????????
        LmPayLog oldlmPayLog = lmPayLogService.findBymerOrderIdAndType(merOrderId, "placeorder");
        if (oldlmPayLog != null) {
            merOrderId = getneworderno();
            lmOrder.setNeworderid(merOrderId);
        }

            {
            Configs configs = configsService.findByTypeId(Configs.type_pay);
            Map map = com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
            returnmap.put("config", configs.getConfig());
            //??????????????????
            JSONObject json = new JSONObject();
            json.put("instMid", map.get("instMid"));
            json.put("mid", map.get("mid"));
            json.put("merOrderId", merOrderId);
            json.put("msgSrc", map.get("msgSrc"));
            json.put("msgType", type);
            json.put("notifyUrl", map.get("notifyUrl"));

            System.out.println("notifyUrl???+++++++++" +  map.get("notifyUrl"));
            //????????????????????????????????????????????????  createBill()
            json.put("requestTimestamp", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            json.put("signType", "SHA256");

            json.put("tid", map.get("tid"));
            json.put("totalAmount", totalAmount);
            Map<String, String> paramsMap = PayUtil.jsonToMap(json);
            paramsMap.put("sign", PayUtil.makeSign(map.get("MD5Key") + "", paramsMap));
            System.out.println("paramsMap???" + paramsMap);
            String strReqJsonStr = JSON.toJSONString(paramsMap);
            System.out.println("strReqJsonStr:" + strReqJsonStr);
            //???????????????????????????????????????
            HttpURLConnection httpURLConnection = null;
            BufferedReader in = null;
            PrintWriter out = null;
            String resultStr = null;
            Map<String, String> resultMap = new HashMap<String, String>();
            if (!StringUtils.isNotBlank(map.get("APIurl") + "")) {
                resultMap.put("errCode", "URLFailed");
                resultStr = JSONObject.fromObject(resultMap).toString();
                returnmap.put("returninfo", resultStr);
                return returnmap;
            }
            try {
                URL url = new URL(map.get("APIurl") + "");
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Content_Type", "application/json");
                httpURLConnection.setRequestProperty("Accept_Charset", "UTF-8");
                httpURLConnection.setRequestProperty("contentType", "UTF-8");
                if(byOrderid.getGoodstype()==1){
                    if(lmOrder.getType()==1){
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) + 12);
                        Date calendarTime = calendar.getTime();
                        Number sellerId =(Number)calendarTime.getTime();
                        String intValue = sellerId.toString();
                        boolean flag = redisUtils.lock(redisKey,intValue);
                        System.out.println("redisKey+++++++++++++++++++++++="+redisKey+"user::::"+lmOrder.getMemberid());
                        System.out.println("flag++++++++++++++++++++++++++"+flag+"user::::"+lmOrder.getMemberid());
                        Map<String, String> resultLock = new HashMap<String, String>();
                        if(flag){
                            List<LmOrderGoods> byGoodRepeat = lmOrderGoodsService.findByGoodRepeat(byOrderid.getGoodid());
                            if(byGoodRepeat!=null&&byGoodRepeat.size()>0){
                                for (LmOrderGoods lmOrderGoods:byGoodRepeat){
                                    LmOrder lmOrderRepeat = lmOrderService.findById(String.valueOf(lmOrderGoods.getOrderid()));
                                    if(("1").equals(lmOrderRepeat.getStatus())){
                                        resultLock.put("errCode", "FIAT");
                                        resultLock.put("msg", "?????????????????????????????????" );
                                        String  resultStrs = JSONObject.fromObject(resultLock).toString();
                                        returnmap.put("returninfo", resultStrs);
                                        return returnmap;
                                    }else {
                                        payZFBMethod(httpURLConnection,map,out,strReqJsonStr,in,resultMap,resultStr,returnmap,merOrderId,type,lmOrder);
                                    }
                                }
                            }
                        }else {
                            resultLock.put("errCode", "FIAT");
                            resultLock.put("msg", "????????????????????????????????????" );
                            String  resultStrs = JSONObject.fromObject(resultLock).toString();
                            returnmap.put("returninfo", resultStrs);
                            return returnmap;
                        }
                    }else {
                        payZFBMethod(httpURLConnection,map,out,strReqJsonStr,in,resultMap,resultStr,returnmap,merOrderId,type,lmOrder);
                    }
                }else {
                    payZFBMethod(httpURLConnection,map,out,strReqJsonStr,in,resultMap,resultStr,returnmap,merOrderId,type,lmOrder);
                }

            } catch (Exception e) {
                e.printStackTrace();
                resultMap.put("errCode", "HttpURLException");
                resultMap.put("msg", "?????????????????????????????????" + e.toString());
                resultStr = JSONObject.fromObject(resultMap).toString();
                returnmap.put("returninfo", resultStr);
                return returnmap;
            } finally {
                if (out != null) {
                    out.close();
                }
                httpURLConnection.disconnect();
            }
            System.out.println("resultStr:" + resultStr);
            return returnmap;
        }
    }

    public  void payZFBMethod(HttpURLConnection httpURLConnection,Map map,PrintWriter out,
                              String strReqJsonStr,BufferedReader in,Map<String, String> resultMap,String resultStr,
                              Map<String, Object> returnmap ,String merOrderId,String type,LmOrder lmOrder) throws Exception {
        System.out.println("?????????++++++++="+lmOrder.getMemberid());
        //??????POST????????????
        out = new PrintWriter(httpURLConnection.getOutputStream());
        out.write(strReqJsonStr);
        out.flush();
        //????????????
        if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            StringBuffer content = new StringBuffer();
            String tempStr = null;
            in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
            while ((tempStr = in.readLine()) != null) {
                content.append(tempStr);
            }
            System.out.println("content:" + content.toString());
            //?????????json??????
            com.alibaba.fastjson.JSONObject respJson = JSON.parseObject(content.toString());
            String resultCode = respJson.getString("errCode");
            resultMap.put("errCode", resultCode);
            resultMap.put("respStr", respJson.toString());
            resultStr = JSONObject.fromObject(resultMap).toString();
            returnmap.put("returninfo", resultStr);
            //????????????
            LmPayLog lmPayLog = new LmPayLog();
            lmPayLog.setCreatetime(new Date());
            lmPayLog.setOrderno(merOrderId);
            lmPayLog.setType("placeorder");
            lmPayLog.setSysmsg(resultStr);
            lmPayLogService.insertService(lmPayLog);
            if ("trade.precreate".equals(type)) {
                lmOrder.setPaystatus("0");
            }
            if ("wx.unifiedOrder".equals(type)) {
                lmOrder.setPaystatus("1");
            }
            if ("uac.appOrder".equals(type)) {
                lmOrder.setPaystatus("2");
            }
            lmOrder.setPaytime(new Date());
            lmOrderService.updateService(lmOrder);
        }
    }

    /**
     * ??????????????????
     */
    @ApiOperation(value = "??????????????????")
    @RequestMapping(value = "/getorderprice", method = RequestMethod.POST)
    public JsonResult getorderprice(@ApiParam(name = "merOrderId", value = "?????????", required = true) @RequestParam(value = "merOrderId", defaultValue = "0") String merOrderId,
                                    @ApiParam(name = "totalAmount", value = "??????", required = true) @RequestParam(value = "totalAmount", defaultValue = "0") long totalAmount
    ) {
        JsonResult jsonResult = new JsonResult();
        System.out.println("??????????????????merOrderId{}totalAmount{}???" + merOrderId + "  " + totalAmount);
        LmOrder lmOrder = lmOrderService.findByOrderId(merOrderId);
        if (lmOrder == null) {
            jsonResult.setCode(JsonResult.ERROR);
            jsonResult.setMsg("???????????????");
            return jsonResult;
        }
        totalAmount = getorderprice(lmOrder, lmOrder.getRealpayprice().multiply(new BigDecimal("100")).longValue());
        jsonResult.setData(totalAmount);
        return jsonResult;
    }

    /**
     * ????????????????????????
     */
    @ApiOperation(value = "??????????????????")
    @RequestMapping(value = "/wxorder", method = RequestMethod.POST)
    @Transactional(propagation = Propagation.REQUIRED)
    public JsonResult order(HttpServletRequest request, HttpServletResponse response, @ApiParam(name = "merOrderId", value = "?????????", required = true) @RequestParam(value = "merOrderId", defaultValue = "0") String merOrderId,
                            @ApiParam(name = "totalAmount", value = "??????", required = true) @RequestParam(value = "totalAmount", defaultValue = "0") long totalAmount
    ) {
        JsonResult jsonResult = new JsonResult();
        System.out.println("??????????????????merOrderId{}totalAmount{}???" + merOrderId + "  " + totalAmount);
        LmOrder lmOrder = lmOrderService.findByOrderId(merOrderId);
        if (lmOrder == null) {
            jsonResult.setCode(JsonResult.ERROR);
            jsonResult.setMsg("???????????????");
            return jsonResult;
        }
        totalAmount = getorderprice(lmOrder, lmOrder.getRealpayprice().multiply(new BigDecimal("100")).longValue());
        System.out.println("??????????????????++++++++++++++totalAmount{}???" +  totalAmount);
        //???????????????????????????????????????
        LmPayLog oldlmPayLog = lmPayLogService.findBymerOrderIdAndType(merOrderId, "placeorder");
        if (oldlmPayLog != null) {
            merOrderId = getneworderno();
            lmOrder.setNeworderid(merOrderId);
        }
        LmOrderGoods byOrderid = lmOrderGoodsService.findByOrderid(lmOrder.getId());
        String redisKey = "livepay" + byOrderid.getGoodid();
        try {
        if(byOrderid.getGoodstype()==1){
            if(lmOrder.getType()==1) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) + 12);
                Date calendarTime = calendar.getTime();
                Number sellerId =(Number)calendarTime.getTime();
                String intValue = sellerId.toString();
                boolean flag = redisUtils.lock(redisKey,intValue);
                System.out.println("redisKey+++++++++++++++++++++++="+redisKey+"user::::"+lmOrder.getMemberid());
                System.out.println("flag++++++++++++++++++++++++++"+flag+"user::::"+lmOrder.getMemberid());
                if (flag) {
                    List<LmOrderGoods> byGoodRepeat = lmOrderGoodsService.findByGoodRepeat(byOrderid.getGoodid());
                    if (byGoodRepeat != null && byGoodRepeat.size() > 0) {
                        for (LmOrderGoods lmOrderGoods : byGoodRepeat) {
                            LmOrder lmOrderRepeat = lmOrderService.findById(String.valueOf(lmOrderGoods.getOrderid()));
                            if (("1").equals(lmOrderRepeat.getStatus())) {
                                jsonResult.setCode(JsonResult.ERROR);
                                jsonResult.setMsg("???????????????");
                                return jsonResult;
                            }else {
                                payVxMethod(request,totalAmount,merOrderId,lmOrder,jsonResult);
                            }
                        }
                    }
                } else {
                    jsonResult.setCode(JsonResult.ERROR);
                    jsonResult.setMsg("?????????????????????????????????");
                    return jsonResult;
                }
            }else {
                payVxMethod(request,totalAmount,merOrderId,lmOrder,jsonResult);
            }
        }else {
            payVxMethod(request,totalAmount,merOrderId,lmOrder,jsonResult);
        }
        } catch (Exception e) {
            e.printStackTrace();
            jsonResult.setCode(JsonResult.ERROR);
            jsonResult.setMsg(e.getMessage());
            return jsonResult;
        }
        return jsonResult;
    }

    public  void payVxMethod(HttpServletRequest request,long totalAmount,String merOrderId,LmOrder lmOrder, JsonResult jsonResult) throws Exception {
            WeixinpayUtil weixinpayUtil = new WeixinpayUtil();
        System.out.println("?????????++++++++="+lmOrder.getMemberid());
            Map<String, String> returninfo = weixinpayUtil.wxPayFunction(totalAmount + "", merOrderId, "????????????", getIpAddr(request));
            returninfo.put("merOrderId", merOrderId);
            //????????????
            LmPayLog lmPayLog = new LmPayLog();
            lmPayLog.setCreatetime(new Date());
            lmPayLog.setOrderno(merOrderId);
            lmPayLog.setType("placeorder");
            lmPayLog.setSysmsg(new Gson().toJson(returninfo));
            lmPayLogService.insertService(lmPayLog);
            lmOrder.setPaystatus("1");
            lmOrder.setPaytime(new Date());
            lmOrderService.updateService(lmOrder);
            jsonResult.setCode(JsonResult.SUCCESS);
            jsonResult.setMsg("????????????");
            jsonResult.setData(returninfo);
    }



    @ApiOperation(value = "???????????????")
    @RequestMapping(value = "/freemoney", method = RequestMethod.POST)
    public JsonResult freemoney(HttpServletRequest request, HttpServletResponse response,
                                @ApiParam(name = "id", value = "??????id", required = true) @RequestParam(value = "id", defaultValue = "0") int id
    ) {
        JsonResult jsonResult = new JsonResult();
        String header = request.getHeader("Authorization");
        String userid="";
        if (!org.springframework.util.StringUtils.isEmpty(header)) {
            if(!org.springframework.util.StringUtils.isEmpty(TokenUtil.getUserId(header))){
                userid = TokenUtil.getUserId(header);
            }else{
                jsonResult.setMsg("???????????????");
                jsonResult.setCode(JsonResult.LOGIN);
                return jsonResult;
            }
        }
        try {
            LmMerchInfo info=lmMerchInfoService.findById(id+"");
            info.setState(1);
            int r=lmMerchInfoService.updateService(info);
            if(r>0) {
                jsonResult.setCode(JsonResult.SUCCESS);
            }else {
                jsonResult.setCode(JsonResult.ERROR);
            }
            return jsonResult;
        } catch (Exception e) {
            e.printStackTrace();
            jsonResult.setCode(JsonResult.ERROR);
            jsonResult.setMsg(e.getMessage());
            return jsonResult;
        }
    }

    /**
     * ?????????????????????
     */
    @ApiOperation(value = "?????????????????????")
    @RequestMapping(value = "/wxpaymoney",method = RequestMethod.POST)
    @Transactional(propagation = Propagation.REQUIRED)
    public JsonResult order(HttpServletRequest request,HttpServletResponse response,
                            @ApiParam(name = "merchid", value = "??????id", required = true)@RequestParam(value = "merchid",defaultValue = "0") int merchid,
                            @ApiParam(name = "totalAmount", value = "??????", required = true)@RequestParam(value = "totalAmount",defaultValue = "0") long totalAmount
    ){
        JsonResult jsonResult = new JsonResult();
        Map<String,Object> returnmap = new HashMap<>();
        String header = request.getHeader("Authorization");
        String userid="";
        if (!org.springframework.util.StringUtils.isEmpty(header)) {
            if(!org.springframework.util.StringUtils.isEmpty(TokenUtil.getUserId(header))){
                userid = TokenUtil.getUserId(header);
            }else{
                jsonResult.setMsg("???????????????");
                jsonResult.setCode(JsonResult.LOGIN);
                return jsonResult;
            }
        }
        System.out.println("??????????????????merchid{}++++++++=???"+merchid);
        Configs configs =configsService.findByTypeId(Configs.type_pay);
        Map map =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
        String prfix=map.get("msgSrcId")+"";
        String merOrderId = prfix+System.currentTimeMillis()+ new Random().nextInt(10);
        System.out.println("??????????????????merOrderId{}totalAmount{}???"+merOrderId+"  "+totalAmount);
        returnmap.put("merOrderId",merOrderId);
        //???????????????????????????????????????
        LmPayLog oldlmPayLog = lmPayLogService.findBymerOrderIdAndType(merOrderId,"placeorder");
        if(oldlmPayLog!=null){
            returnmap.put("returninfo",oldlmPayLog.getSysmsg());
            jsonResult.setCode(JsonResult.ERROR);
            jsonResult.setMsg("token??????");
            jsonResult.setData(returnmap);
            return jsonResult;
        }
        try {
            WeixinpayUtil weixinpayUtil = new WeixinpayUtil();
            Map<String,String> returninfo = weixinpayUtil.wxPayFunction(totalAmount+"",merOrderId,"????????????",getIpAddr(request));
            returninfo.put("merOrderId",merOrderId);
            //????????????
            LmPayLog lmPayLog = new LmPayLog();
            lmPayLog.setCreatetime(new Date());
            lmPayLog.setOrderno(merOrderId);
            lmPayLog.setType("margin");
            lmPayLog.setSysmsg(new Gson().toJson(returninfo));
            lmPayLogService.insertService(lmPayLog);
            //???????????????????????????
            LmMerchMarginLog lmMerchMarginLog=new LmMerchMarginLog();
            lmMerchMarginLog.setCreate_time(new Date());
            lmMerchMarginLog.setMargin_sn(merOrderId);
            lmMerchMarginLog.setPaystatus(1);
            lmMerchMarginLog.setState(0);
            lmMerchMarginLog.setMer_id(merchid);
            lmMerchMarginLog.setType(0);
            lmMerchMarginLog.setDescription("?????????????????????");
            lmMerchMarginLogService.insert(lmMerchMarginLog);
            jsonResult.setCode(JsonResult.SUCCESS);
            jsonResult.setData(returninfo);
            return jsonResult;
        } catch (Exception e) {
            e.printStackTrace();
            jsonResult.setCode(JsonResult.ERROR);
            jsonResult.setMsg(e.getMessage());
            return jsonResult;
        }
    }

    /**
     * ?????????????????????????????????
     * @param request
     * @param type
     * @param merchid
     * @param totalAmount
     * @return
     */
    @ApiOperation(value = "?????????????????????")
    @RequestMapping(value = "/paymoney",method = RequestMethod.POST)
    @Transactional(propagation = Propagation.REQUIRED)
    public Map<String,Object> paymoney(HttpServletRequest request,
                                       @ApiParam(name = "type", value = "????????????", required = true)@RequestParam(value = "type",defaultValue = "0") String type,
                                       @ApiParam(name = "merchid", value = "??????id", required = true)@RequestParam(value = "merchid",defaultValue = "0") int merchid,
                                       @ApiParam(name = "totalAmount", value = "??????", required = true)@RequestParam(value = "totalAmount",defaultValue = "0") long totalAmount
    ){

        Map<String,Object> returnmap = new HashMap<>();
        String header = request.getHeader("Authorization");
        String userid="";
        if (!StringUtils.isEmpty(header)) {
            if(!StringUtils.isEmpty(TokenUtil.getUserId(header))){
                userid = TokenUtil.getUserId(header);
            }else{
                returnmap.put("returninfo","token??????,???????????????");
                return returnmap;
            }
        }
        System.out.println("??????????????????merchid{}++++++++=???"+merchid);
        Configs configs =configsService.findByTypeId(Configs.type_pay);
        Map map =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
        String prfix=map.get("msgSrcId")+"";
        String merOrderId = prfix+System.currentTimeMillis()+ new Random().nextInt(10);
        System.out.println("??????????????????merOrderId{}totalAmount{}???"+merOrderId+"  "+totalAmount);
        returnmap.put("merOrderId",merOrderId);
        //???????????????????????????????????????
        LmPayLog oldlmPayLog = lmPayLogService.findBymerOrderIdAndType(merOrderId,"placeorder");
        if(oldlmPayLog!=null){
            returnmap.put("returninfo",oldlmPayLog.getSysmsg());
            return returnmap;
        }
        {
            returnmap.put("config",configs.getConfig());
            //??????????????????
            JSONObject json = new JSONObject();
            json.put("instMid", map.get("instMid"));
            json.put("mid", map.get("mid"));
            json.put("merOrderId", merOrderId);
            json.put("msgSrc", map.get("msgSrc"));
            json.put("msgType", type);
            json.put("notifyUrl", map.get("notifyUrl"));
            //????????????????????????????????????????????????  createBill()
            json.put("requestTimestamp", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            json.put("signType", "SHA256");
            json.put("tid", map.get("tid"));
            json.put("totalAmount", totalAmount);
            Map<String, String> paramsMap = PayUtil.jsonToMap(json);
            paramsMap.put("sign", PayUtil.makeSign(map.get("MD5Key")+"", paramsMap));
            System.out.println("paramsMap???"+paramsMap);
            String strReqJsonStr = JSON.toJSONString(paramsMap);
            System.out.println("strReqJsonStr:"+strReqJsonStr);
            //???????????????????????????????????????
            HttpURLConnection httpURLConnection = null;
            BufferedReader in = null;
            PrintWriter out = null;
//        OutputStreamWriter out = null;
            String resultStr = null;
            Map<String,String> resultMap = new HashMap<String,String>();
            if (!StringUtils.isNotBlank(map.get("APIurl")+"")) {
                resultMap.put("errCode","URLFailed");
                resultStr = JSONObject.fromObject(resultMap).toString();
                returnmap.put("returninfo",resultStr);
                return returnmap;
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
                    System.out.println("content:"+content.toString());
                    //?????????json??????
                    com.alibaba.fastjson.JSONObject respJson = JSON.parseObject(content.toString());
                    String resultCode = respJson.getString("errCode");
                    resultMap.put("errCode",resultCode);
                    resultMap.put("respStr",respJson.toString());
                    resultStr = JSONObject.fromObject(resultMap).toString();
                    returnmap.put("returninfo",resultStr);
                    //????????????
                    LmPayLog lmPayLog = new LmPayLog();
                    lmPayLog.setCreatetime(new Date());
                    lmPayLog.setOrderno(merOrderId);
                    lmPayLog.setType("margin");
                    lmPayLog.setSysmsg(resultStr);
                    lmPayLogService.insertService(lmPayLog);
                    //???????????????????????????
                    LmMerchMarginLog lmMerchMarginLog=new LmMerchMarginLog();
                    lmMerchMarginLog.setCreate_time(new Date());
                    lmMerchMarginLog.setMargin_sn(merOrderId);
                    if("trade.precreate".equals(type)){
                        lmMerchMarginLog.setPaystatus(0);
                    }
                    if("wx.unifiedOrder".equals(type)){
                        lmMerchMarginLog.setPaystatus(1);
                    }
                    if("uac.appOrder".equals(type)){
                        lmMerchMarginLog.setPaystatus(2);
                    }
                    lmMerchMarginLog.setState(0);
                    lmMerchMarginLog.setMer_id(merchid);
                    lmMerchMarginLog.setType(0);
                    lmMerchMarginLog.setDescription("?????????????????????");
                    lmMerchMarginLogService.insert(lmMerchMarginLog);
                }
            } catch (Exception e) {
                e.printStackTrace();
                resultMap.put("errCode","HttpURLException");
                resultMap.put("msg","?????????????????????????????????"+e.toString());
                resultStr = JSONObject.fromObject(resultMap).toString();
                returnmap.put("returninfo",resultStr);
                return returnmap;
            }finally {
                if (out != null) {
                    out.close();
                }
                httpURLConnection.disconnect();
            }
            System.out.println("resultStr:"+resultStr);
            return returnmap;
        }
    }

    /**
     * ??????????????????????????????
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws JDOMException
     */
    @RequestMapping(value = "notifyWeiXinPay", produces = MediaType.APPLICATION_JSON_VALUE)
    public String notifyWeiXinPay(HttpServletRequest request, HttpServletResponse response) throws IOException, JDOMException {
        System.out.println("??????????????????");
        logger.info("??????????????????");
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
        if (!PayCommonUtil.isTenpaySign(params)) {
            logger.info("?????????????????????");
            // ????????????
            return_data.put("return_code", "FAIL");
            return_data.put("return_msg", "return_code?????????");
            return StringUtil.GetMapToXML(return_data);
        } else {
            logger.info("?????????????????????????????????");
            System.out.println("===============????????????==============");
            String out_trade_no = String.valueOf(params.get("out_trade_no"));
            String total_fee = String.valueOf(params.get("total_fee"));
            BigDecimal price=new  BigDecimal(total_fee);
            price=price.divide(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP);
            logger.info("????????????"+out_trade_no);
            logger.info("?????????"+price);
            // ------------------------------
            // ??????????????????
            // ------------------------------
            // ?????????????????????????????????????????????????????????????????????????????????
            // ------------------------------
            //?????????????????????
            LmPayLog marginlog = lmPayLogService.findBymerOrderIdAndType(out_trade_no,"margin");
            if(marginlog!=null){
                LmMerchMarginLog lmMerchMarginLog = lmMerchMarginLogService.findByMarginSn(out_trade_no);
                if(lmMerchMarginLog!=null){
                    LmMerchInfo info=lmMerchInfoService.findById(lmMerchMarginLog.getMer_id()+"");
                    info.setState(1);
                    lmMerchMarginLog.setPrice(price);
                    lmMerchMarginLog.setState(1);
                    lmMerchMarginLogService.update(lmMerchMarginLog);
                    int r=lmMerchInfoService.updateService(info);
                    if(r>0) {
                        return_data.put("return_code", "SUCCESS");
                        return_data.put("return_msg", "OK");
                    }else {
                        return_data.put("return_code", "FAIL");
                        return_data.put("return_msg", "OK");
                    }
                }
                return StringUtil.GetMapToXML(return_data);
            }
                LmOrder lmOrder = lmOrderService.findByOrderId(out_trade_no);
                if (lmOrder == null) {
                    lmOrder = lmOrderService.findByNewOrderid(out_trade_no);
                }
                LmOrderGoods lmOrderGoods = null;
                if (lmOrder != null) {
                    lmOrderGoods = lmOrderGoodsService.findByOrderid(lmOrder.getId());
                }
                if (lmOrder != null && lmOrderGoods != null) {
                    LmMerchInfo info = lmMerchInfoService.findById(lmOrder.getMerchid() + "");
                    LmMember merMember = lmMemberService.findById(info.getMember_id() + "");
                    lmOrder.setSend_type(4);
                    if (("0").equals(lmOrder.getStatus())) {
                        LmPayLog lmPayLog = new LmPayLog();
                        lmPayLog.setCreatetime(new Date());
                        lmPayLog.setOrderno(out_trade_no);
                        lmPayLog.setSysmsg(new Gson().toJson(params));
                        lmPayLog.setType("payorder");
                        lmPayLogService.insertService(lmPayLog);
                        //????????????
                        if (lmOrder.getType() == 3) {
                            if (lmOrder.getIsprepay() == 1) {
                                //????????????
                                if (lmOrder.getDeposit_type() == 0) {
                                    //????????????
                                    lmOrder.setPrepay_money(price);
                                    lmOrder.setPrepay_time(new Date());
                                    lmOrder.setDeposit_type(1);
                                } else {
                                    //????????????
                                    lmOrder.setRemain_money(price);
                                    lmOrder.setStatus("1");
                                    lmOrder.setPaytime(new Date());
                                    lmOrder.setRemian_time(new Date());
                                }
                            } else {
                                //????????????
                                lmOrder.setPrepay_money(new BigDecimal(0));
                                lmOrder.setPrepay_time(new Date());
                                lmOrder.setDeposit_type(1);
                                lmOrder.setRemain_money(price);
                                lmOrder.setStatus("1");
                                lmOrder.setPaytime(new Date());
                                lmOrder.setRemian_time(new Date());
                            }
                            lmOrderService.updateService(lmOrder);
                            List<LmOrder> lmOrderlist = lmOrderService.findOrderListByPid(lmOrder.getPorderid());
                            LmShareGood good = goodService.findshareById(lmOrderGoods.getGoodid());
                            if (good != null) {
                                if (lmOrderlist.size() == good.getChipped_num()) {
                                    LmOrder porder = lmOrderService.findById(lmOrder.getPorderid() + "");
                                    porder.setChippedtime(new Date());
                                    lmOrderService.updateService(porder);
                                    good.setStatus(-1);
                                    goodService.updateShareGood(good);
                                }
                                LmLive lmLive = lmLiveService.findbyId(good.getLiveid() + "");
                                HttpClient httpClient = new HttpClient();
                                LmMember member = lmMemberService.findById(lmOrder.getMemberid() + "");
                                httpClient.sendgroup(lmLive.getLivegroupid(), "????????????", 9, member.getId());
                                if (lmOrder.getIslivegood() == 1) {
                                    if (lmLive.getIsstart() == 1) {
                                        System.out.println("????????????+++++++++++++++++++++++++++++++++" + 22);
                                        Map<String, Object> immap = new HashMap<>();
                                        immap.put("userName", member.getNickname());
                                        if (member.getLevel_id() == 0) {
                                            immap.put("userLevel", 1);
                                        } else {
                                            immap.put("userLevel", member.getLevel_id());
                                        }
                                        immap.put("userImg", member.getAvatar());
                                        immap.put("liveGoodId", good.getId());
                                        immap.put("liveGoodName", good.getName());
                                        immap.put("userId", member.getId());
                                        httpClient.sendgroup(lmLive.getLivegroupid(), new Gson().toJson(immap), 22);
                                    }
                                }
                            }
                        } else {
                            //??????????????????
                            lmOrder.setStatus("1");
                            lmOrder.setPaytime(new Date());
                            lmOrderService.updateService(lmOrder);
                            LmMember member = lmMemberService.findById(lmOrder.getMemberid() + "");
                            HttpClient httpClient = new HttpClient();
                            httpClient.send("????????????", info.getMember_id() + "", "??????" + member.getNickname() + "??????????????????????????????????????????" + lmOrder.getOrderid());
                            LmOrderGoods orderGoods = lmOrderGoodsService.findByOrderid(lmOrder.getId());
                            if (lmOrder.getIslivegood() == 1) {
                                LivedGood livedGood = goodService.findLivedGood(orderGoods.getGoodid());
                                livedGood.setStatus(1);
                                goodService.updateLivedGood(livedGood);
                                LmLive lmLive = lmLiveService.findbyId(String.valueOf(livedGood.getLiveid()));
                                if (lmLive.getIsstart() == 1) {
                                    System.out.println("????????????+++++++++++++++++++++++++++++++++" + 22);
                                    Map<String, Object> immap = new HashMap<>();
                                    immap.put("userName", member.getNickname());
                                    if (member.getLevel_id() == 0) {
                                        immap.put("userLevel", 1);
                                    } else {
                                        immap.put("userLevel", member.getLevel_id());
                                    }
                                    immap.put("userImg", member.getAvatar());
                                    immap.put("liveGoodId", livedGood.getId());
                                    immap.put("liveGoodName", livedGood.getName());
                                    immap.put("userId", member.getId());
                                    httpClient.sendgroup(lmLive.getLivegroupid(), new Gson().toJson(immap), 22);
                                }
                            }

                        }
                        SmsUtils.sendValidCodeMsgs(merMember.getMobile(), info.getStore_name(), "SMS_213291171");
                        String msg="????????????????????????"+info.getStore_name()+"????????????????????????????????????????????????48??????????????????????????????????????????????????????????????????????????????????????????";
                        PropellingUtil.IOSPropellingMessage("????????????",msg,merMember.getId()+"");
                        PropellingUtil.AndroidPropellingMessage("????????????",msg,merMember.getId()+"");
                        return_data.put("return_code", "SUCCESS");
                        return_data.put("return_msg", "OK");
                    } else {
                        return_data.put("return_code", "FAIL");
                        return_data.put("return_msg", "OK");
                    }
                } else {
                    return_data.put("return_code", "FAIL");
                    return_data.put("return_msg", "OK");
                }
                LmOrderGoods byOrderid = lmOrderGoodsService.findByOrderid(lmOrder.getId());
                if (byOrderid != null) {
                    String redisKey = "livepay" + byOrderid.getGoodid();
                    redisUtils.delete(redisKey);
                }

            return StringUtil.GetMapToXML(return_data);
        }
    }


    /**
     * ????????????
     * @param request
     * @param merOrderId
     * @return
     */
    @ApiOperation(value = "????????????")
    @RequestMapping(value = "/orderquery",method = RequestMethod.POST)
    public JsonResult orderquery(HttpServletRequest request,@ApiParam(name = "merOrderId", value = "?????????", required = true)@RequestParam(value = "merOrderId",defaultValue = "0") String merOrderId){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        Map<String,Object> returnmap = new HashMap<>();
        LmOrder lmOrder = lmOrderService.findByOrderId(merOrderId);
        try {
            if(lmOrder!=null){
                int status = Integer.parseInt(lmOrder.getStatus());
                if(status>0){
                    returnmap.put("status","SUCCESS");
                }else{
                	if(lmOrder.getType()==3){
                		if(lmOrder.getDeposit_type()==1){
                            returnmap.put("status","SUCCESS");
                		}
                	}else{
                        returnmap.put("status","FAILED");
                    }
                }
            }else{
                returnmap.put("status","FAILED");
            }
            jsonResult.setData(returnmap);
        } catch (Exception e) {
            e.printStackTrace();
            jsonResult.setCode(JsonResult.ERROR);
            jsonResult.setMsg(e.getMessage());
        }
        return jsonResult;
    }


    /**
     * ????????? ?????????????????????????????????
     * @param request
     * @return
     */
    @ApiOperation(value = "????????????????????????")
    @RequestMapping(value = "/ordersuccess",method = RequestMethod.POST)
    public String orderQuery(HttpServletRequest request){
        String merOrderId = request.getParameter("merOrderId");
        String totalAmount = request.getParameter("totalAmount");
        Configs configs =configsService.findByTypeId(Configs.type_pay);
        Map map =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
        BigDecimal price=new  BigDecimal(totalAmount);
        price=price.divide(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP);
        Map<String, String> params = PayUtil.getRequestParams(request);
        System.out.println("params???"+params);
        logger.info("?????????"+price);
        // ??????
        boolean checkRet = PayUtil.checkSign(map.get("MD5Key")+"", params);
        try {
            if(checkRet) {
                //?????????????????????
                LmPayLog marginlog = lmPayLogService.findBymerOrderIdAndType(merOrderId, "margin");
                if (marginlog != null) {
                    LmMerchMarginLog lmMerchMarginLog = lmMerchMarginLogService.findByMarginSn(merOrderId);
                    if (lmMerchMarginLog != null) {
                        LmMerchInfo info = lmMerchInfoService.findById(lmMerchMarginLog.getMer_id() + "");
                        info.setState(1);
                        lmMerchMarginLog.setPrice(price);
                        lmMerchMarginLog.setState(1);
                        lmMerchMarginLogService.update(lmMerchMarginLog);
                        int r = lmMerchInfoService.updateService(info);
                        if (r > 0) {
                            return "SUCCESS";
                        } else {
                            return "FAILED";
                        }
                    }
                }
                    LmOrder lmOrder = lmOrderService.findByOrderId(merOrderId);
                    if (lmOrder == null) {
                        lmOrder = lmOrderService.findByNewOrderid(merOrderId);
                    }
                    LmOrderGoods lmOrderGoods = null;
                    if (lmOrder != null) {
                        lmOrderGoods = lmOrderGoodsService.findByOrderid(lmOrder.getId());
                    }

                    if (lmOrder != null && lmOrderGoods != null) {
                        LmMerchInfo info = lmMerchInfoService.findById(lmOrder.getMerchid() + "");
                        LmMember merMember = lmMemberService.findById(info.getMember_id() + "");
                        lmOrder.setSend_type(4);
                        LmOrderGoods byOrderid = lmOrderGoodsService.findByOrderid(lmOrder.getId());
                        String redisKey = "livepay" + byOrderid.getGoodid();
                        redisUtils.delete(redisKey);
                        if (("0").equals(lmOrder.getStatus())) {
                            LmPayLog lmPayLog = new LmPayLog();
                            lmPayLog.setCreatetime(new Date());
                            lmPayLog.setOrderno(merOrderId);
                            lmPayLog.setSysmsg(new Gson().toJson(params));
                            lmPayLog.setType("payorder");
                            lmPayLogService.insertService(lmPayLog);
                            //????????????
                            if (lmOrder.getType() == 3) {
                                if (lmOrder.getIsprepay() == 1) {
                                    //????????????
                                    if (lmOrder.getDeposit_type() == 0) {
                                        //????????????
                                        lmOrder.setPrepay_money(price);
                                        lmOrder.setPrepay_time(new Date());
                                        lmOrder.setDeposit_type(1);
                                    } else {
                                        //????????????
                                        lmOrder.setRemain_money(price);
                                        lmOrder.setStatus("1");
                                        lmOrder.setPaytime(new Date());
                                        lmOrder.setRemian_time(new Date());
                                    }
                                } else {
                                    //????????????
                                    lmOrder.setPrepay_money(new BigDecimal(0));
                                    lmOrder.setPrepay_time(new Date());
                                    lmOrder.setDeposit_type(1);
                                    lmOrder.setRemain_money(price);
                                    lmOrder.setStatus("1");
                                    lmOrder.setPaytime(new Date());
                                    lmOrder.setRemian_time(new Date());
                                }
                                lmOrderService.updateService(lmOrder);
                                List<LmOrder> lmOrderlist = lmOrderService.findOrderListByPid(lmOrder.getPorderid());
                                LmShareGood good = goodService.findshareById(lmOrderGoods.getGoodid());
                                if (good != null) {
                                    if (lmOrderlist.size() == good.getChipped_num()) {
                                        LmOrder porder = lmOrderService.findById(lmOrder.getPorderid() + "");
                                        porder.setChippedtime(new Date());
                                        lmOrderService.updateService(porder);
                                        good.setStatus(-1);
                                        goodService.updateShareGood(good);
                                    }
                                    LmLive lmLive = lmLiveService.findbyId(good.getLiveid() + "");
                                    HttpClient httpClient = new HttpClient();
                                    LmMember member = lmMemberService.findById(lmOrder.getMemberid() + "");
                                    httpClient.sendgroup(lmLive.getLivegroupid(), "????????????", 9, member.getId());
                                    if (lmOrder.getIslivegood() == 1) {
                                        if (lmLive.getIsstart() == 1) {
                                            System.out.println("????????????+++++++++++++++++++++++++++++++++" + 22);
                                            Map<String, Object> immap = new HashMap<>();
                                            immap.put("userName", member.getNickname());
                                            if (member.getLevel_id() == 0) {
                                                immap.put("userLevel", 1);
                                            } else {
                                                immap.put("userLevel", member.getLevel_id());
                                            }
                                            immap.put("userImg", member.getAvatar());
                                            immap.put("liveGoodId", good.getId());
                                            immap.put("liveGoodName", good.getName());
                                            immap.put("userId", member.getId());
                                            httpClient.sendgroup(lmLive.getLivegroupid(), new Gson().toJson(immap), 22);
                                        }
                                    }
                                }
                            } else {
                                //??????????????????
                                lmOrder.setStatus("1");
                                lmOrder.setPaytime(new Date());
                                lmOrderService.updateService(lmOrder);
                                LmMember member = lmMemberService.findById(lmOrder.getMemberid() + "");

                                HttpClient httpClient = new HttpClient();
                                httpClient.send("????????????", info.getMember_id() + "", "??????" + member.getNickname() + "???????????????????????????????????????" + lmOrder.getOrderid());
                                LmOrderGoods orderGoods = lmOrderGoodsService.findByOrderid(lmOrder.getId());
                                if (lmOrder.getIslivegood() == 1) {
                                    LivedGood livedGood = goodService.findLivedGood(orderGoods.getGoodid());
                                    livedGood.setStatus(1);
                                    goodService.updateLivedGood(livedGood);
                                    LmLive lmLive = lmLiveService.findbyId(String.valueOf(livedGood.getLiveid()));
                                    if (lmLive.getIsstart() == 1) {
                                        System.out.println("????????????+++++++++++++++++++++++++++++++++" + 22);
                                        Map<String, Object> immap = new HashMap<>();
                                        immap.put("userName", member.getNickname());
                                        if (member.getLevel_id() == 0) {
                                            immap.put("userLevel", 1);
                                        } else {
                                            immap.put("userLevel", member.getLevel_id());
                                        }
                                        immap.put("userImg", member.getAvatar());
                                        immap.put("liveGoodId", livedGood.getId());
                                        immap.put("liveGoodName", livedGood.getName());
                                        immap.put("userId", member.getId());
                                        httpClient.sendgroup(lmLive.getLivegroupid(), new Gson().toJson(immap), 22);
                                    }
                                }
                            }
                            SmsUtils.sendValidCodeMsgs(merMember.getMobile(), info.getStore_name(), "SMS_213291171");
                            String msg="????????????????????????"+info.getStore_name()+"????????????????????????????????????????????????48??????????????????????????????????????????????????????????????????????????????????????????";
                            PropellingUtil.IOSPropellingMessage("????????????",msg,merMember.getId()+"");
                            PropellingUtil.AndroidPropellingMessage("????????????",msg,merMember.getId()+"");
                            return "SUCCESS";
                        } else {
                            return "FAILED";
                        }
                    } else {
                        return "FAILED";
                    }
                }else{
                    return "FAILED";
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "FAILED";
    }

    /**
     * ????????????
     * @param response
     * @param merOrderId
     * @param refundAmount
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "??????????????????")
    @RequestMapping(value = "/refund",method = RequestMethod.POST)
    public Map<String,Object> refund(HttpServletResponse response,@ApiParam(name = "merOrderId", value = "?????????", required = true)@RequestParam(value = "merOrderId",defaultValue = "0") String merOrderId,
                                     @ApiParam(name = "refundAmount", value = "????????????", required = true)@RequestParam(value = "refundAmount",defaultValue = "0") String refundAmount) throws Exception {
        System.out.println("?????????????????????"+merOrderId);
        Map<String,Object> returnmap = new HashMap<>();
        //???????????????????????????????????????
        LmPayLog oldlmPayLog = lmPayLogService.findBymerOrderIdAndType(merOrderId,"refund");
        if(oldlmPayLog!=null) {
            returnmap.put("returninfo", oldlmPayLog.getSysmsg());
            Map msgmap =  com.alibaba.fastjson.JSONObject.parseObject(oldlmPayLog.getSysmsg());
            if(msgmap!=null){
                String code = msgmap.get("errCode")+"";
                if(code.equals("SUCCESS")){
                	logger.info("??????????????????????????????????????????????????????merOrderId:"+merOrderId);
                    return returnmap;
                }
            }
        }
        LmOrder lmOrder = lmOrderService.findByOrderId(merOrderId);
        
        
    	if(lmOrder.getStatus().equals("4")) 
    	{
	        Map<String,String> _resultMap = new HashMap<String,String>();
	    	LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(lmOrder.getMerchid()+"");
			if (lmMerchInfo == null) {
				_resultMap.put("errCode","FAIL");
				_resultMap.put("msg","??????????????????");
	              String resultStr = JSONObject.fromObject(_resultMap).toString();
	              returnmap.put("returninfo",resultStr);
	              return returnmap;
			}
			/*if(null==lmMerchInfo.getCredit() || (lmMerchInfo.getCredit().multiply(new BigDecimal(100))).compareTo(new BigDecimal(refundAmount))==-1)
			{
				_resultMap.put("errCode","FAIL");
				_resultMap.put("msg","?????????????????????");
	              String resultStr = JSONObject.fromObject(_resultMap).toString();
	              returnmap.put("returninfo",resultStr);
	              return returnmap;
			}*/
    	}
		
        // ????????? ?????????????????????
        if(!"1".equals(lmOrder.getPaystatus())){
        	
        	logger.info("????????????????????????????????????????????????merOrderId:"+merOrderId);
            Configs configs =configsService.findByTypeId(Configs.type_pay);
            Map map =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
            returnmap.put("config",configs.getConfig());
            //??????????????????
            JSONObject json = new JSONObject();
            json.put("mid",  map.get("mid"));
            json.put("tid",map.get("tid"));
            json.put("msgType", "refund");
            json.put("msgSrc", map.get("msgSrc"));
            json.put("instMid", map.get("instMid"));
            json.put("merOrderId", merOrderId);
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
//        OutputStreamWriter out = null;
            String resultStr = null;
            Map<String,String> resultMap = new HashMap<String,String>();
            if (!StringUtils.isNotBlank(map.get("APIurl")+"")) {
                resultMap.put("errCode","URLFailed");
                resultStr = JSONObject.fromObject(resultMap).toString();
                returnmap.put("returninfo",resultStr);
            }
            try{
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
                    logger.info("????????????????????????????????????????????????content::"+content.toString());
                    
                    //?????????json??????
                    com.alibaba.fastjson.JSONObject respJson = JSON.parseObject(content.toString());
                    String resultCode = respJson.getString("errCode");
                    resultMap.put("errCode",resultCode);
                    resultMap.put("respStr",respJson.toString());
                    resultStr = JSONObject.fromObject(resultMap).toString();
                    returnmap.put("returninfo",resultStr);
                    //????????????
                    LmPayLog lmPayLog = new LmPayLog();
                    lmPayLog.setCreatetime(new Date());
                    lmPayLog.setOrderno(merOrderId);
                    lmPayLog.setType("refund");
                    lmPayLog.setSysmsg(resultStr);
                    lmPayLogService.insertService(lmPayLog);
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
        }else{
            //??????????????????
            Map<String,String> resultMap = new HashMap<String,String>();
            try {
                WeixinpayUtil weixinpayUtil = new WeixinpayUtil();
                Map<String,String> weixinrefund=weixinpayUtil.refund(refundAmount,merOrderId);
                String code = weixinrefund.get("return_code")+"";
                weixinrefund.put("refundStatus",code);
                resultMap.put("errCode",code);
                resultMap.put("respStr",JSONObject.fromObject(weixinrefund).toString());
                String resultStr = JSONObject.fromObject(resultMap).toString();
                returnmap.put("returninfo",resultStr);
                logger.info("?????????????????????????????????????????????content:"+resultStr.toString());
                //????????????
                LmPayLog lmPayLog = new LmPayLog();
                lmPayLog.setCreatetime(new Date());
                lmPayLog.setOrderno(merOrderId);
                lmPayLog.setType("refund");
                lmPayLog.setSysmsg(resultStr);
                lmPayLogService.insertService(lmPayLog);
            } catch (Exception e) {
                e.printStackTrace();
                resultMap.put("errCode","HttpURLException");
                resultMap.put("msg","???????????????????????????"+e.toString());
                String resultStr = JSONObject.fromObject(resultMap).toString();
                returnmap.put("returninfo",resultStr);
            }
        }
        logger.info("??????????????????????????????????????????????????????:"+returnmap);
        return returnmap;
    }



    /**
     * ????????????????????????????????????
     */
    @ApiOperation(value = "??????????????????")
    @RequestMapping(value = "/backorderQuery",method = RequestMethod.POST)
    public Map<String,Object>  backorderQuery(HttpServletResponse response, @ApiParam(name = "merOrderId", value = "?????????", required = true)@RequestParam(value = "merOrderId",defaultValue = "0") String merOrderId){
        System.out.println("?????????????????????"+merOrderId);
        Map<String,Object> returnmap = new HashMap<>();
        Configs configs =configsService.findByTypeId(Configs.type_pay);
        Map map =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
        returnmap.put("config",configs.getConfig());
        //??????????????????
        JSONObject json = new JSONObject();
        json.put("mid",  map.get("mid"));
        json.put("tid",map.get("tid"));
        json.put("msgType","refundQuery");
        json.put("msgSrc", map.get("msgSrc"));
        json.put("instMid", map.get("instMid"));
        json.put("merOrderId", merOrderId);
        //????????????????????????????????????????????????  createBill()
        json.put("requestTimestamp", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        json.put("signType", "SHA256");
        Map<String, String> paramsMap = PayUtil.jsonToMap(json);
        paramsMap.put("sign", PayUtil.makeSign(map.get("MD5Key")+"", paramsMap));
        System.out.println("paramsMap???"+paramsMap);
        String strReqJsonStr = JSON.toJSONString(paramsMap);
        System.out.println("strReqJsonStr:"+strReqJsonStr);
        //???????????????????????????????????????
        HttpURLConnection httpURLConnection = null;
        BufferedReader in = null;
        PrintWriter out = null;
//        OutputStreamWriter out = null;
        String resultStr = null;
        Map<String,String> resultMap = new HashMap<String,String>();
        if (!StringUtils.isNotBlank(map.get("APIurl")+"")) {
            resultMap.put("errCode","URLFailed");
            resultStr = JSONObject.fromObject(resultMap).toString();
            returnmap.put("returninfo",resultStr);
            return returnmap;
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
                System.out.println("content:"+content.toString());

                //?????????json??????
                com.alibaba.fastjson.JSONObject respJson = JSON.parseObject(content.toString());
                String resultCode = respJson.getString("errCode");
                resultMap.put("errCode",resultCode);
                resultMap.put("respStr",respJson.toString());
                resultStr = JSONObject.fromObject(resultMap).toString();
                returnmap.put("returninfo",resultStr);
                //????????????
                LmPayLog lmPayLog = new LmPayLog();
                lmPayLog.setCreatetime(new Date());
                lmPayLog.setOrderno(merOrderId);
                lmPayLog.setType("orderquery");
                lmPayLog.setSysmsg(resultStr);
                lmPayLogService.insertService(lmPayLog);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("errCode","HttpURLException");
            resultMap.put("msg","?????????????????????????????????"+e.toString());
            resultStr = JSONObject.fromObject(resultMap).toString();
            returnmap.put("returninfo",resultStr);
            return returnmap;
        }finally {
            if (out != null) {
                out.close();
            }
            httpURLConnection.disconnect();
        }
        System.out.println("resultStr:"+resultStr);
        return returnmap;
    }

    private String getneworderno(){
        Integer maxId = lmOrderService.findMaxId();
        if(maxId==null){
            maxId = 0;
        }
        String datetime = DateUtils.sdfyMdHm.format(new Date());
        Configs configs =configsService.findByTypeId(Configs.type_pay);
        Map map =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
        String prfix=map.get("msgSrcId")+"";
        return prfix+datetime+maxId;
    }


    /**
     * ????????????openid
     * @param code
     * @return
     */
    private static String getopenid(String code){
        String openid  =  "";
        // ??????Http?????????(???????????????:???????????????????????????;??????:?????????HttpClient???????????????????????????)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        Random ran=new Random();
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=wx8d3dd4f8ecbd3b11&secret=61df1215a760d6df205f4dce4d07dd70&grant_type=authorization_code&js_code="+code;
        HttpPost httpPost = new HttpPost(url);
        Map<String,String> map = new HashMap<>();
        // ????????????
        CloseableHttpResponse response = null;
        String body = JSONUtils.toJSONString(map);
        try {
            httpPost.setEntity(new StringEntity(body,"UTF-8"));
            // ??????????????????(??????)Post??????
            response = httpClient.execute(httpPost);
            // ????????????????????????????????????
            HttpEntity responseEntity = response.getEntity();

            System.out.println("???????????????:" + response.getStatusLine());
            if (responseEntity != null) {
                System.out.println("?????????????????????:" + responseEntity.getContentLength());
                System.out.println("???????????????:" + EntityUtils.toString(responseEntity));
            }
            com.alibaba.fastjson.JSONObject respJson = JSON.parseObject(EntityUtils.toString(responseEntity));
            openid = respJson.getString("openid");
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                // ????????????
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return openid;
    }



    /**
     * ??????????????????ip
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
                //??????????????????????????????IP
                InetAddress inet=null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress= inet.getHostAddress();
            }
        }
        //?????????????????????????????????????????????IP??????????????????IP,??????IP??????','??????
        if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15
            if(ipAddress.indexOf(",")>0){
                ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }


}
