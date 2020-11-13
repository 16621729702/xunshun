package com.wink.livemall.admin.api.pay;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPayUtil;
import com.google.gson.Gson;
import com.qiniu.util.Json;
import com.wink.livemall.admin.util.DateUtils;
import com.wink.livemall.admin.util.IMUtil;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.WeixinpayUtil;
import com.wink.livemall.admin.util.httpclient.HttpClient;
import com.wink.livemall.admin.util.payUtil.PayCommonUtil;
import com.wink.livemall.admin.util.payUtil.PayUtil;
import com.wink.livemall.admin.util.payUtil.StringUtil;
import com.wink.livemall.goods.dto.Good;
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
import com.wink.livemall.merch.service.LmMerchInfoService;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;

@Api(tags = "支付接口")
@RestController
@RequestMapping("/pay")
public class PayController {
    private static final Logger logger = LogManager.getLogger(PayController.class);

    private static String NotifyUrl = "http://api.xunshun.net/api/pay/ordersuccess";

    @Value("${server.servlet.context-path}")
    private String path;

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


    private Long getorderprice(LmOrder lmOrder,Long totalAmount){
        if(lmOrder.getType()==3){
            LmOrderGoods orderGood = lmOrderGoodsService.findByOrderid(lmOrder.getId());
            LmShareGood shareGood = goodService.findshareById(orderGood.getGoodid());
            //商品金额
            BigDecimal price = shareGood.getChipped_price().multiply(new BigDecimal("1")).multiply(new BigDecimal("100"));
            //如果是合买订单 则先支付定金
            if(lmOrder.getType()==3&&lmOrder.getIsprepay()==1){
                //订金金额
                BigDecimal payprice;
                //是否支付过定金
                if(lmOrder.getDeposit_type()==0){
                    payprice = price.multiply(new BigDecimal("0.3"));
                    if(shareGood.getChipped_num()==1){
                        BigDecimal s = price.multiply(new BigDecimal("0.05"));
                        payprice =payprice.add(s);
                    }
                }else{
                    payprice = price.multiply(new BigDecimal("0.3"));
                    if(shareGood.getChipped_num()==1){
                        BigDecimal s = price.multiply(new BigDecimal("0.05"));
                        payprice =payprice.add(s);
                    }
                    payprice = lmOrder.getRealpayprice().multiply(new BigDecimal("100")).subtract(payprice);
                }
                System.out.println(payprice.longValue());
                totalAmount = payprice.longValue();
            }else{
                if(shareGood.getChipped_num()==1){
                    totalAmount = price.multiply(new BigDecimal("1.05")).longValue();
                }
            }
        }
        return totalAmount;
    }
    /**
     * 下单请求模块
     * 支付宝云闪付下单
     */
    @ApiOperation(value = "下单请求模块")
    @RequestMapping(value = "/order",method = RequestMethod.POST)
    public Map<String,Object> order(HttpServletRequest request,HttpServletResponse response, @ApiParam(name = "merOrderId", value = "订单号", required = true)@RequestParam(value = "merOrderId",defaultValue = "0") String merOrderId,
                                    @ApiParam(name = "type", value = "支付类型", required = true)@RequestParam(value = "type",defaultValue = "0") String type,
                                    @ApiParam(name = "totalAmount", value = "金额", required = true)@RequestParam(value = "totalAmount",defaultValue = "0") long totalAmount
    ){
        System.out.println("请求参数对象merOrderId{}totalAmount{}："+merOrderId+"  "+totalAmount);
        Map<String,Object> returnmap = new HashMap<>();
        LmOrder lmOrder = lmOrderService.findByOrderId(merOrderId);
        if(lmOrder==null){
            return returnmap;
        }
//        //如果是合买订单
//        if(lmOrder.getType()==3){
//            Map<String,String> resultMap = new HashMap<String,String>();
//            LmOrder porder =lmOrderService.findById(lmOrder.getPorderid()+"");
//            String redisKey = "pay:" + porder;
//            boolean flag = redisUtil.getLock(redisKey);
//            if (flag) {
//                //查询总订单
//                //查询总订单下所有子订单
//                List<LmOrder>  orders = lmOrderService.findOrderListByPid(porder.getId());
//                LmOrderGoods lmOrderGoods = lmOrderGoodsService.findByOrderid(lmOrder.getId());
//                LmShareGood shareGood = goodService.findshareById(lmOrderGoods.getGoodid());
//                if(shareGood.getChipped_num()!=1&&shareGood.getChipped_num()<=orders.size()){
//                    resultMap.put("errCode","ERROR");
//                    resultMap.put("msg","合买人数太多无法合买");
//                    String resultStr = JSONObject.fromObject(resultMap).toString();
//                    returnmap.put("returninfo",resultStr);
//                    return returnmap;
//                }
//                redisUtil.delete(redisKey);
//            }else{
//                resultMap.put("errCode","ERROR");
//                resultMap.put("msg","当前支付人数过多，请稍后重试");
//                String resultStr = JSONObject.fromObject(resultMap).toString();
//                returnmap.put("returninfo",resultStr);
//                return returnmap;
//            }
//        }
        totalAmount = getorderprice(lmOrder,lmOrder.getRealpayprice().multiply(new BigDecimal("100")).longValue());
        //查询是否有相同订单下单请求
        LmPayLog oldlmPayLog = lmPayLogService.findBymerOrderIdAndType(merOrderId,"placeorder");
        if(oldlmPayLog!=null){
            merOrderId = getneworderno();
            lmOrder.setNeworderid(merOrderId);
        }
        {
            Configs configs =configsService.findByTypeId(Configs.type_pay);
            Map map =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
            returnmap.put("config",configs.getConfig());
            //组织请求报文
            JSONObject json = new JSONObject();
            json.put("instMid", map.get("instMid"));
            json.put("mid", map.get("mid"));
            json.put("merOrderId", merOrderId);
            json.put("msgSrc", map.get("msgSrc"));
            json.put("msgType", type);
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
                //发送POST请求参数
                out = new PrintWriter(httpURLConnection.getOutputStream());
                out.write(strReqJsonStr);
                out.flush();
                //读取响应
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    StringBuffer content = new StringBuffer();
                    String tempStr = null;
                    in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"UTF-8"));
                    while ((tempStr=in.readLine()) != null){
                        content.append(tempStr);
                    }
                    System.out.println("content:"+content.toString());
                    //转换成json对象
                    com.alibaba.fastjson.JSONObject respJson = JSON.parseObject(content.toString());
                    String resultCode = respJson.getString("errCode");
                    resultMap.put("errCode",resultCode);
                    resultMap.put("respStr",respJson.toString());
                    resultStr = JSONObject.fromObject(resultMap).toString();
                    returnmap.put("returninfo",resultStr);
                    //添加记录
                    LmPayLog lmPayLog = new LmPayLog();
                    lmPayLog.setCreatetime(new Date());
                    lmPayLog.setOrderno(merOrderId);
                    lmPayLog.setType("placeorder");
                    lmPayLog.setSysmsg(resultStr);
                    lmPayLogService.insertService(lmPayLog);
                    if("trade.precreate".equals(type)){
                        lmOrder.setPaystatus("0");
                    }
                    if("wx.unifiedOrder".equals(type)){
                        lmOrder.setPaystatus("1");
                    }
                    if("uac.appOrder".equals(type)){
                        lmOrder.setPaystatus("2");
                    }
                    lmOrderService.updateService(lmOrder);
                }
            } catch (Exception e) {
                e.printStackTrace();
                resultMap.put("errCode","HttpURLException");
                resultMap.put("msg","调用银商接口出现异常："+e.toString());
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
     * 获取下单金额
     */
    @ApiOperation(value = "获取下单金额")
    @RequestMapping(value = "/getorderprice",method = RequestMethod.POST)
    public JsonResult getorderprice( @ApiParam(name = "merOrderId", value = "订单号", required = true)@RequestParam(value = "merOrderId",defaultValue = "0") String merOrderId,
                            @ApiParam(name = "totalAmount", value = "金额", required = true)@RequestParam(value = "totalAmount",defaultValue = "0") long totalAmount
    ){
        JsonResult jsonResult = new JsonResult();
        System.out.println("请求参数对象merOrderId{}totalAmount{}："+merOrderId+"  "+totalAmount);
        LmOrder lmOrder = lmOrderService.findByOrderId(merOrderId);
        if(lmOrder==null){
            jsonResult.setCode(JsonResult.ERROR);
            jsonResult.setMsg("订单不存在");
            return jsonResult;
        }
        totalAmount = getorderprice(lmOrder,lmOrder.getRealpayprice().multiply(new BigDecimal("100")).longValue());
        jsonResult.setData(totalAmount);
        return jsonResult;
    }

    /**
     * 微信下单请求模块
     */
    @ApiOperation(value = "下单请求模块")
    @RequestMapping(value = "/wxorder",method = RequestMethod.POST)
    public JsonResult order(HttpServletRequest request,HttpServletResponse response, @ApiParam(name = "merOrderId", value = "订单号", required = true)@RequestParam(value = "merOrderId",defaultValue = "0") String merOrderId,
                            @ApiParam(name = "totalAmount", value = "金额", required = true)@RequestParam(value = "totalAmount",defaultValue = "0") long totalAmount
    ){
        JsonResult jsonResult = new JsonResult();
        System.out.println("请求参数对象merOrderId{}totalAmount{}："+merOrderId+"  "+totalAmount);
        LmOrder lmOrder = lmOrderService.findByOrderId(merOrderId);
        if(lmOrder==null){
            jsonResult.setCode(JsonResult.ERROR);
            jsonResult.setMsg("订单不存在");
            return jsonResult;
        }
//        //如果是合买订单
//        if(lmOrder.getType()==3){
//            LmOrder porder =lmOrderService.findById(lmOrder.getPorderid()+"");
//            String redisKey = "pay:" + porder;
//            boolean flag = redisUtil.getLock(redisKey);
//            if (flag) {
//                //查询总订单下所有子订单
//                List<LmOrder>  orders = lmOrderService.findOrderListByPid(porder.getId());
//                LmOrderGoods lmOrderGoods = lmOrderGoodsService.findByOrderid(lmOrder.getId());
//                LmShareGood shareGood = goodService.findshareById(lmOrderGoods.getGoodid());
//                if(shareGood.getChipped_num()!=1&&shareGood.getChipped_num()<=orders.size()){
//                    jsonResult.setCode(JsonResult.ERROR);
//                    jsonResult.setMsg("合买人数太多无法合买");
//                    return jsonResult;
//                }
//                redisUtil.delete(redisKey);
//            }else{
//                jsonResult.setCode(JsonResult.ERROR);
//                jsonResult.setMsg("当前支付人数过多，请稍后重试");
//                return jsonResult;
//            }
//        }
        totalAmount = getorderprice(lmOrder,lmOrder.getRealpayprice().multiply(new BigDecimal("100")).longValue());
        //查询是否有相同订单下单请求
        LmPayLog oldlmPayLog = lmPayLogService.findBymerOrderIdAndType(merOrderId,"placeorder");
        if(oldlmPayLog!=null){
            merOrderId = getneworderno();
            lmOrder.setNeworderid(merOrderId);
        }
        try {
            WeixinpayUtil weixinpayUtil = new WeixinpayUtil();
            Map<String,String> returninfo = weixinpayUtil.wxPayFunction(totalAmount+"",merOrderId,"微信下单",getIpAddr(request));
            returninfo.put("merOrderId",merOrderId);
            //添加记录
            LmPayLog lmPayLog = new LmPayLog();
            lmPayLog.setCreatetime(new Date());
            lmPayLog.setOrderno(merOrderId);
            lmPayLog.setType("placeorder");
            lmPayLog.setSysmsg(new Gson().toJson(returninfo));
            lmPayLogService.insertService(lmPayLog);
            lmOrder.setPaystatus("1");
            lmOrderService.updateService(lmOrder);
            jsonResult.setCode(JsonResult.SUCCESS);
            jsonResult.setMsg("下单成功");
            jsonResult.setData(returninfo);
        } catch (Exception e) {
            e.printStackTrace();
            jsonResult.setCode(JsonResult.ERROR);
            jsonResult.setMsg(e.getMessage());
            return jsonResult;
        }
        return jsonResult;
    }

    /**
     * 微信支付保障金
     */
    @ApiOperation(value = "微信支付保障金")
    @RequestMapping(value = "/wxpaymoney",method = RequestMethod.POST)
    public JsonResult order(HttpServletRequest request,HttpServletResponse response,
                            @ApiParam(name = "merchid", value = "商户id", required = true)@RequestParam(value = "merchid",defaultValue = "0") int merchid,
                            @ApiParam(name = "totalAmount", value = "金额", required = true)@RequestParam(value = "totalAmount",defaultValue = "0") long totalAmount
    ){
        JsonResult jsonResult = new JsonResult();
        totalAmount=1;
        Map<String,Object> returnmap = new HashMap<>();
        String header = request.getHeader("Authorization");
        String userid = "";
        if (!org.springframework.util.StringUtils.isEmpty(header)) {
            if(!org.springframework.util.StringUtils.isEmpty(redisUtil.get(header))){
                userid = redisUtil.get(header)+"";
            }else{
                jsonResult.setCode(JsonResult.LOGIN);
                return jsonResult;
            }
        }
        Configs configs =configsService.findByTypeId(Configs.type_pay);
        Map map =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
        String prfix=map.get("msgSrcId")+"";
        String merOrderId = prfix+System.currentTimeMillis()+ new Random().nextInt(10);
        System.out.println("请求参数对象merOrderId{}totalAmount{}："+merOrderId+"  "+totalAmount);
        returnmap.put("merOrderId",merOrderId);
        //查询是否有相同订单下单请求
        LmPayLog oldlmPayLog = lmPayLogService.findBymerOrderIdAndType(merOrderId,"placeorder");
        if(oldlmPayLog!=null){
            returnmap.put("returninfo",oldlmPayLog.getSysmsg());
            jsonResult.setCode(JsonResult.ERROR);
            jsonResult.setMsg("token失效");
            jsonResult.setData(returnmap);
            return jsonResult;
        }
        try {
            WeixinpayUtil weixinpayUtil = new WeixinpayUtil();
            Map<String,String> returninfo = weixinpayUtil.wxPayFunction(totalAmount+"",merOrderId,"微信下单",getIpAddr(request));
            returninfo.put("merOrderId",merOrderId);
            //添加记录
            LmPayLog lmPayLog = new LmPayLog();
            lmPayLog.setCreatetime(new Date());
            lmPayLog.setOrderno(merOrderId);
            lmPayLog.setType("margin");
            lmPayLog.setSysmsg(new Gson().toJson(returninfo));
            lmPayLogService.insertService(lmPayLog);
            //生成商户保证金订单
            LmOrder lmOrder = new LmOrder();
            lmOrder.setStatus("1");
            lmOrder.setOrderid(merOrderId);
            lmOrder.setCreatetime(new Date());
            lmOrder.setMerchid(merchid);
            lmOrderService.insertService(lmOrder);
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
     * 支付宝云闪付支付保障金
     * @param request
     * @param type
     * @param merchid
     * @param totalAmount
     * @return
     */
    @ApiOperation(value = "保证金支付接口")
    @RequestMapping(value = "/paymoney",method = RequestMethod.POST)
    public Map<String,Object> paymoney(HttpServletRequest request,
                                       @ApiParam(name = "type", value = "支付类型", required = true)@RequestParam(value = "type",defaultValue = "0") String type,
                                       @ApiParam(name = "merchid", value = "商户id", required = true)@RequestParam(value = "merchid",defaultValue = "0") int merchid,
                                       @ApiParam(name = "totalAmount", value = "金额", required = true)@RequestParam(value = "totalAmount",defaultValue = "0") long totalAmount
    ){
        totalAmount=1;
        Map<String,Object> returnmap = new HashMap<>();
        String header = request.getHeader("Authorization");
        String userid = "";
        if (!org.springframework.util.StringUtils.isEmpty(header)) {
            if(!org.springframework.util.StringUtils.isEmpty(redisUtil.get(header))){
                userid = redisUtil.get(header)+"";
            }else{
                returnmap.put("returninfo","token失效,请重新登录");
                return returnmap;
            }
        }
        Configs configs =configsService.findByTypeId(Configs.type_pay);
        Map map =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
        String prfix=map.get("msgSrcId")+"";
        String merOrderId = prfix+System.currentTimeMillis()+ new Random().nextInt(10);
        System.out.println("请求参数对象merOrderId{}totalAmount{}："+merOrderId+"  "+totalAmount);
        returnmap.put("merOrderId",merOrderId);
        //查询是否有相同订单下单请求
        LmPayLog oldlmPayLog = lmPayLogService.findBymerOrderIdAndType(merOrderId,"placeorder");
        if(oldlmPayLog!=null){
            returnmap.put("returninfo",oldlmPayLog.getSysmsg());
            return returnmap;
        }
        {
            returnmap.put("config",configs.getConfig());
            //组织请求报文
            JSONObject json = new JSONObject();
            json.put("instMid", map.get("instMid"));
            json.put("mid", map.get("mid"));
            json.put("merOrderId", merOrderId);
            json.put("msgSrc", map.get("msgSrc"));
            json.put("msgType", type);
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
                //发送POST请求参数
                out = new PrintWriter(httpURLConnection.getOutputStream());
                out.write(strReqJsonStr);
                out.flush();
                //读取响应
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    StringBuffer content = new StringBuffer();
                    String tempStr = null;
                    in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"UTF-8"));
                    while ((tempStr=in.readLine()) != null){
                        content.append(tempStr);
                    }
                    System.out.println("content:"+content.toString());
                    //转换成json对象
                    com.alibaba.fastjson.JSONObject respJson = JSON.parseObject(content.toString());
                    String resultCode = respJson.getString("errCode");
                    resultMap.put("errCode",resultCode);
                    resultMap.put("respStr",respJson.toString());
                    resultStr = JSONObject.fromObject(resultMap).toString();
                    returnmap.put("returninfo",resultStr);
                    //添加记录
                    LmPayLog lmPayLog = new LmPayLog();
                    lmPayLog.setCreatetime(new Date());
                    lmPayLog.setOrderno(merOrderId);
                    lmPayLog.setType("margin");
                    lmPayLog.setSysmsg(resultStr);
                    lmPayLogService.insertService(lmPayLog);
                    //生成商户保证金订单
                    LmOrder lmOrder = new LmOrder();
                    if("trade.precreate".equals(type)){
                        lmOrder.setPaystatus("0");
                    }
                    if("wx.unifiedOrder".equals(type)){
                        lmOrder.setPaystatus("1");
                    }
                    if("uac.appOrder".equals(type)){
                        lmOrder.setPaystatus("2");
                    }
                    lmOrder.setOrderid(merOrderId);
                    lmOrder.setCreatetime(new Date());
                    lmOrder.setMerchid(merchid);
                    lmOrderService.insertService(lmOrder);
                }
            } catch (Exception e) {
                e.printStackTrace();
                resultMap.put("errCode","HttpURLException");
                resultMap.put("msg","调用银商接口出现异常："+e.toString());
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
     * 微信支付成功回调接口
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws JDOMException
     */
    @RequestMapping(value = "notifyWeiXinPay", produces = MediaType.APPLICATION_JSON_VALUE)
    public String notifyWeiXinPay(HttpServletRequest request, HttpServletResponse response) throws IOException, JDOMException {
        System.out.println("微信支付回调");
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
        if (!PayCommonUtil.isTenpaySign(params)) {
            logger.info("微信验签不通过");

            // 支付失败
            return_data.put("return_code", "FAIL");
            return_data.put("return_msg", "return_code不正确");
            return StringUtil.GetMapToXML(return_data);
        } else {
            logger.info("付款成功，执行回调操作");
            System.out.println("===============付款成功==============");
            String out_trade_no = String.valueOf(params.get("out_trade_no"));
            String total_fee = String.valueOf(params.get("total_fee"));
            Long price = Long.parseLong(total_fee)/100;
            logger.info("订单号："+out_trade_no);
            logger.info("金额："+total_fee);

            // ------------------------------
            // 处理业务开始
            // ------------------------------
            // 此处处理订单状态，结合自己的订单数据完成订单状态的更新
            // ------------------------------
            //查询保证金记录
            LmPayLog marginlog = lmPayLogService.findBymerOrderIdAndType(out_trade_no,"margin");
            if(marginlog!=null){
                LmOrder lmOrder = lmOrderService.findByOrderId(out_trade_no);
                if(lmOrder!=null){
                    LmMerchInfo info=lmMerchInfoService.findById(lmOrder.getMerchid()+"");
                    info.setMargin(new BigDecimal(price));
                    info.setState(1);
                    lmOrder.setStatus("5");
                    lmOrderService.updateService(lmOrder);
                    int r=lmMerchInfoService.updateService(info);
                    if(r>0) {
                        return_data.put("return_code", "SUCCESS");
                        return_data.put("return_msg", "OK");
                    }else {
                        return_data.put("return_code", "FAIL");
                        return_data.put("return_msg", "OK");
                    }
                }
            }
            LmOrder lmOrder = lmOrderService.findByOrderId(out_trade_no);
            if(lmOrder==null){
                lmOrder = lmOrderService.findByNewOrderid(out_trade_no);
            }
            LmOrderGoods lmOrderGoods = null;
            if(lmOrder!=null){
                lmOrderGoods = lmOrderGoodsService.findByOrderid(lmOrder.getId());
            }
            if(lmOrder!=null&&lmOrderGoods!=null){
                if(!"1".equals(lmOrder.getStatus())){
                    LmPayLog lmPayLog = new LmPayLog();
                    lmPayLog.setCreatetime(new Date());
                    lmPayLog.setOrderno(out_trade_no);
                    lmPayLog.setSysmsg(new Gson().toJson(params));
                    lmPayLog.setType("payorder");
                    lmPayLogService.insertService(lmPayLog);
                    //合买订单
                    if(lmOrder.getType()==3){
                        if(lmOrder.getIsprepay()==1){
                            //合买订单
                            if(lmOrder.getDeposit_type()==0){
                                //设置定金
                                lmOrder.setPrepay_money(new BigDecimal(price));
                                lmOrder.setPrepay_time(new Date());
                                lmOrder.setDeposit_type(1);
                            }else{
                                //设置尾款
                                lmOrder.setRemain_money(new BigDecimal(price));
                                lmOrder.setStatus("1");
                                lmOrder.setPaytime(new Date());
                                lmOrder.setRemian_time(new Date());
                            }
                        }else{
                            //全款支付
                            lmOrder.setPrepay_money(new BigDecimal(0));
                            lmOrder.setPrepay_time(new Date());
                            lmOrder.setDeposit_type(1);
                            lmOrder.setRemain_money(new BigDecimal(price));
                            lmOrder.setStatus("1");
                            lmOrder.setPaytime(new Date());
                            lmOrder.setRemian_time(new Date());
                        }
                        lmOrderService.updateService(lmOrder);
                        List<LmOrder>  lmOrderlist = lmOrderService.findOrderListByPid(lmOrder.getPorderid());
                        LmShareGood good = goodService.findshareById(lmOrderGoods.getGoodid());
                        if(good!=null){
                            if(lmOrderlist.size()==good.getChipped_num()){
                                LmOrder porder = lmOrderService.findById(lmOrder.getPorderid()+"");
                                porder.setChippedtime(new Date());
                                lmOrderService.updateService(porder);
                                good.setStatus(-1);
                                goodService.updateShareGood(good);
                            }
                            LmLive lmLive = lmLiveService.findbyId(good.getLiveid()+"");
                            HttpClient httpClient = new HttpClient();
                            LmMember member = lmMemberService.findById(lmOrder.getMemberid()+"");
                            httpClient.sendgroup(lmLive.getLivegroupid(),"合买成功",9,member.getId());
                        }
                    }else{
                        //普通订单处理
                        lmOrder.setStatus("1");
                        lmOrder.setPaytime(new Date());
                        lmOrderService.updateService(lmOrder);
                        LmMember member = lmMemberService.findById(lmOrder.getMemberid()+"");
                        LmMerchInfo info=lmMerchInfoService.findById(lmOrder.getMerchid()+"");
                        HttpClient httpClient = new HttpClient();
                        httpClient.send("交易消息",info.getMember_id()+"","用户"+member.getNickname()+"付款成功尽快发货，订单号："+lmOrder.getOrderid());
                    }
                    return_data.put("return_code", "SUCCESS");
                    return_data.put("return_msg", "OK");
                }else{
                    return_data.put("return_code", "FAIL");
                    return_data.put("return_msg", "OK");
                }
            }else{
                return_data.put("return_code", "FAIL");
                return_data.put("return_msg", "OK");
            }
            return StringUtil.GetMapToXML(return_data);
        }
    }



//    /**
//     * 查询模块
//     */
//    //账单查询
//    @ApiOperation(value = "账单查询")
//    @RequestMapping(value = "/orderQuery",method = RequestMethod.POST)
//    public Map<String,Object>  orderQuery(HttpServletResponse response, @ApiParam(name = "merOrderId", value = "订单号", required = true)@RequestParam(value = "merOrderId",defaultValue = "0") String merOrderId){
//        System.out.println("请求参数对象："+merOrderId);
//        Map<String,Object> returnmap = new HashMap<>();
//        Configs configs =configsService.findByTypeId(Configs.type_pay);
//        Map map =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
//        returnmap.put("config",configs.getConfig());
//        //组织请求报文
//        JSONObject json = new JSONObject();
//        json.put("mid",  map.get("mid"));
//        json.put("tid",map.get("tid"));
//        json.put("msgType","query");
//        json.put("msgSrc", map.get("msgSrc"));
//        json.put("instMid", map.get("instMid"));
//        json.put("merOrderId", merOrderId);
//
//        //是否要在商户系统下单，看商户需求  createBill()
//        json.put("requestTimestamp", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
//        json.put("signType", "SHA256");
//
//        Map<String, String> paramsMap = PayUtil.jsonToMap(json);
//        paramsMap.put("sign", PayUtil.makeSign(map.get("MD5Key")+"", paramsMap));
//        System.out.println("paramsMap："+paramsMap);
//        String strReqJsonStr = JSON.toJSONString(paramsMap);
//        System.out.println("strReqJsonStr:"+strReqJsonStr);
//        //调用银商平台获取二维码接口
//        HttpURLConnection httpURLConnection = null;
//        BufferedReader in = null;
//        PrintWriter out = null;
////        OutputStreamWriter out = null;
//        String resultStr = null;
//        Map<String,String> resultMap = new HashMap<String,String>();
//        if (!StringUtils.isNotBlank(map.get("APIurl")+"")) {
//            resultMap.put("errCode","URLFailed");
//            resultStr = JSONObject.fromObject(resultMap).toString();
//            returnmap.put("returninfo",resultStr);
//            return returnmap;
//        }
//
//        try {
//            URL url = new URL(map.get("APIurl")+"");
//            httpURLConnection = (HttpURLConnection) url.openConnection();
//            httpURLConnection.setRequestMethod("POST");
//            httpURLConnection.setDoInput(true);
//            httpURLConnection.setDoOutput(true);
//            httpURLConnection.setRequestProperty("Content_Type","application/json");
//            httpURLConnection.setRequestProperty("Accept_Charset","UTF-8");
//            httpURLConnection.setRequestProperty("contentType","UTF-8");
//            //发送POST请求参数
//            out = new PrintWriter(httpURLConnection.getOutputStream());
//            out.write(strReqJsonStr);
//            out.flush();
//
//            //读取响应
//            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                StringBuffer content = new StringBuffer();
//                String tempStr = null;
//                in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"UTF-8"));
//                while ((tempStr=in.readLine()) != null){
//                    content.append(tempStr);
//                }
//                System.out.println("content:"+content.toString());
//
//                //转换成json对象
//                com.alibaba.fastjson.JSONObject respJson = JSON.parseObject(content.toString());
//                String resultCode = respJson.getString("errCode");
//                resultMap.put("errCode",resultCode);
//                resultMap.put("respStr",respJson.toString());
//                resultStr = JSONObject.fromObject(resultMap).toString();
//                returnmap.put("returninfo",resultStr);
//                //添加记录
//                LmPayLog lmPayLog = new LmPayLog();
//                lmPayLog.setCreatetime(new Date());
//                lmPayLog.setOrderno(merOrderId);
//                lmPayLog.setType("orderquery");
//                lmPayLog.setSysmsg(resultStr);
//                lmPayLogService.insertService(lmPayLog);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            resultMap.put("errCode","HttpURLException");
//            resultMap.put("msg","调用银商接口出现异常："+e.toString());
//            resultStr = JSONObject.fromObject(resultMap).toString();
//            returnmap.put("returninfo",resultStr);
//            return returnmap;
//        }finally {
//            if (out != null) {
//                out.close();
//            }
//            httpURLConnection.disconnect();
//        }
//
//        System.out.println("resultStr:"+resultStr);
//        return returnmap;
//    }

    /**
     * 订单查询
     * @param request
     * @param merOrderId
     * @return
     */
    @ApiOperation(value = "订单查询")
    @RequestMapping(value = "/orderquery",method = RequestMethod.POST)
    public JsonResult orderquery(HttpServletRequest request,@ApiParam(name = "merOrderId", value = "订单号", required = true)@RequestParam(value = "merOrderId",defaultValue = "0") String merOrderId){
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
     * 支付宝 云闪付交易成功回调接口
     * @param request
     * @return
     */
    @ApiOperation(value = "交易成功反馈信息")
    @RequestMapping(value = "/ordersuccess",method = RequestMethod.POST)
    public String orderQuery(HttpServletRequest request){
        String merOrderId = request.getParameter("merOrderId");
        String totalAmount = request.getParameter("totalAmount");
        Configs configs =configsService.findByTypeId(Configs.type_pay);
        Map map =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
        Long price = Long.parseLong(totalAmount)/100;
        Map<String, String> params = PayUtil.getRequestParams(request);
        System.out.println("params："+params);
        // 验签
        boolean checkRet = PayUtil.checkSign(map.get("MD5Key")+"", params);
        try {
            if(checkRet){
                //查询保证金记录
                LmPayLog marginlog = lmPayLogService.findBymerOrderIdAndType(merOrderId,"margin");
                if(marginlog!=null){
                    LmOrder lmOrder = lmOrderService.findByOrderId(merOrderId);
                    if(lmOrder!=null){
                        LmMerchInfo info=lmMerchInfoService.findById(lmOrder.getMerchid()+"");
                        info.setMargin(new BigDecimal(price));
                        info.setState(1);
                        lmOrder.setStatus("5");
                        lmOrderService.updateService(lmOrder);
                        int r=lmMerchInfoService.updateService(info);
                        if(r>0) {
                            return "SUCCESS";
                        }else {
                            return "FAILED";
                        }
                    }
                }
                LmOrder lmOrder = lmOrderService.findByOrderId(merOrderId);
                if(lmOrder==null){
                    lmOrder = lmOrderService.findByNewOrderid(merOrderId);
                }
                LmOrderGoods lmOrderGoods = null;
                if(lmOrder!=null){
                    lmOrderGoods = lmOrderGoodsService.findByOrderid(lmOrder.getId());
                }

                if(lmOrder!=null&&lmOrderGoods!=null){
                    if(!"1".equals(lmOrder.getStatus())){
                        LmPayLog lmPayLog = new LmPayLog();
                        lmPayLog.setCreatetime(new Date());
                        lmPayLog.setOrderno(merOrderId);
                        lmPayLog.setSysmsg(new Gson().toJson(params));
                        lmPayLog.setType("payorder");
                        lmPayLogService.insertService(lmPayLog);
                        //合买订单
                        if(lmOrder.getType()==3){
                            if(lmOrder.getIsprepay()==1){
                                //合买订单
                                if(lmOrder.getDeposit_type()==0){
                                    //设置定金
                                    lmOrder.setPrepay_money(new BigDecimal(price));
                                    lmOrder.setPrepay_time(new Date());
                                    lmOrder.setDeposit_type(1);
                                }else{
                                    //设置尾款
                                    lmOrder.setRemain_money(new BigDecimal(price));
                                    lmOrder.setStatus("1");
                                    lmOrder.setPaytime(new Date());
                                    lmOrder.setRemian_time(new Date());
                                }
                            }else{
                                //全款支付
                                lmOrder.setPrepay_money(new BigDecimal(0));
                                lmOrder.setPrepay_time(new Date());
                                lmOrder.setDeposit_type(1);
                                lmOrder.setRemain_money(new BigDecimal(price));
                                lmOrder.setStatus("1");
                                lmOrder.setPaytime(new Date());
                                lmOrder.setRemian_time(new Date());
                            }
                            lmOrderService.updateService(lmOrder);
                            List<LmOrder>  lmOrderlist = lmOrderService.findOrderListByPid(lmOrder.getPorderid());
                            LmShareGood good = goodService.findshareById(lmOrderGoods.getGoodid());
                            if(good!=null){
                                if(lmOrderlist.size()==good.getChipped_num()){
                                    LmOrder porder = lmOrderService.findById(lmOrder.getPorderid()+"");
                                    porder.setChippedtime(new Date());
                                    lmOrderService.updateService(porder);
                                    good.setStatus(-1);
                                    goodService.updateShareGood(good);
                                }
                                LmLive lmLive = lmLiveService.findbyId(good.getLiveid()+"");
                                HttpClient httpClient = new HttpClient();
                                LmMember member = lmMemberService.findById(lmOrder.getMemberid()+"");
                                httpClient.sendgroup(lmLive.getLivegroupid(),"合买成功",9,member.getId());
                            }
                        }else{
                            //普通订单处理
                            lmOrder.setStatus("1");
                            lmOrder.setPaytime(new Date());
                            lmOrderService.updateService(lmOrder);
                            LmMember member = lmMemberService.findById(lmOrder.getMemberid()+"");
                            LmMerchInfo info=lmMerchInfoService.findById(lmOrder.getMerchid()+"");
                            HttpClient httpClient = new HttpClient();
                            httpClient.send("交易消息",info.getMember_id()+"","用户"+member.getNickname()+"付款成功尽快发货，订单号："+lmOrder.getOrderid());

                        }
                        return "SUCCESS";
                    }else{
                        return "FAILED";
                    }
                }else{
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
     * 退款接口
     * @param response
     * @param merOrderId
     * @param refundAmount
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "退款要求接口")
    @RequestMapping(value = "/refund",method = RequestMethod.POST)
    public Map<String,Object> refund(HttpServletResponse response,@ApiParam(name = "merOrderId", value = "订单号", required = true)@RequestParam(value = "merOrderId",defaultValue = "0") String merOrderId,
                                     @ApiParam(name = "refundAmount", value = "退款金额", required = true)@RequestParam(value = "refundAmount",defaultValue = "0") String refundAmount) throws Exception {
        System.out.println("请求参数对象："+merOrderId);
        Map<String,Object> returnmap = new HashMap<>();
        //查询是否有相同订单下单请求
        LmPayLog oldlmPayLog = lmPayLogService.findBymerOrderIdAndType(merOrderId,"refund");
        if(oldlmPayLog!=null) {
            returnmap.put("returninfo", oldlmPayLog.getSysmsg());
            Map msgmap =  com.alibaba.fastjson.JSONObject.parseObject(oldlmPayLog.getSysmsg());
            if(msgmap!=null){
                String code = msgmap.get("errCode")+"";
                if(code.equals("SUCCESS")){
                	logger.info("已经发生过退款请求。。。。。。。。。merOrderId:"+merOrderId);
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
				_resultMap.put("msg","商戶不存在！");
	              String resultStr = JSONObject.fromObject(_resultMap).toString();
	              returnmap.put("returninfo",resultStr);
	              return returnmap;
			}
			if(null==lmMerchInfo.getCredit() || (lmMerchInfo.getCredit().multiply(new BigDecimal(100))).compareTo(new BigDecimal(refundAmount))==-1) 
			{
				_resultMap.put("errCode","FAIL");
				_resultMap.put("msg","商戶额度不足！");
	              String resultStr = JSONObject.fromObject(_resultMap).toString();
	              returnmap.put("returninfo",resultStr);
	              return returnmap;
			}
    	}
		
        // 支付宝 云闪付退款逻辑
        if(!"1".equals(lmOrder.getPaystatus())){
        	
        	logger.info("发起支付宝退款。。。。。。。。。merOrderId:"+merOrderId);
            Configs configs =configsService.findByTypeId(Configs.type_pay);
            Map map =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
            returnmap.put("config",configs.getConfig());
            //组织请求报文
            JSONObject json = new JSONObject();
            json.put("mid",  map.get("mid"));
            json.put("tid",map.get("tid"));
            json.put("msgType", "refund");
            json.put("msgSrc", map.get("msgSrc"));
            json.put("instMid", map.get("instMid"));
            json.put("merOrderId", merOrderId);
            //是否要在商户系统下单，看商户需求  createBill()
            json.put("refundAmount",refundAmount);
            json.put("requestTimestamp", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            json.put("signType", "SHA256");
            Map<String, String> paramsMap = PayUtil.jsonToMap(json);
            paramsMap.put("sign", PayUtil.makeSign(map.get("MD5Key")+"", paramsMap));
           
            String strReqJsonStr = JSON.toJSONString(paramsMap);
            
            //调用银商平台获取二维码接口
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
            try {

                URL url = new URL(map.get("APIurl")+"");
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Content_Type","application/json");
                httpURLConnection.setRequestProperty("Accept_Charset","UTF-8");
                httpURLConnection.setRequestProperty("contentType","UTF-8");
                //发送POST请求参数
                out = new PrintWriter(httpURLConnection.getOutputStream());
                out.write(strReqJsonStr);
                out.flush();
                //读取响应
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    StringBuffer content = new StringBuffer();
                    String tempStr = null;
                    in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"UTF-8"));
                    while ((tempStr=in.readLine()) != null){
                        content.append(tempStr);
                    }
                    logger.info("发起支付宝退款。。。。。。。。。content::"+content.toString());
                    
                    //转换成json对象
                    com.alibaba.fastjson.JSONObject respJson = JSON.parseObject(content.toString());
                    String resultCode = respJson.getString("errCode");
                    resultMap.put("errCode",resultCode);
                    resultMap.put("respStr",respJson.toString());
                    resultStr = JSONObject.fromObject(resultMap).toString();
                    returnmap.put("returninfo",resultStr);
                    //添加记录
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
                resultMap.put("msg","调用银商接口出现异常："+e.toString());
                resultStr = JSONObject.fromObject(resultMap).toString();
                returnmap.put("returninfo",resultStr);
            }finally {
                if (out != null) {
                    out.close();
                }
                httpURLConnection.disconnect();
            }
        }else{
            //微信退款逻辑
            Map<String,String> resultMap = new HashMap<String,String>();
            try {
                WeixinpayUtil weixinpayUtil = new WeixinpayUtil();
                Map<String,String> weixinrefund=weixinpayUtil.refund(refundAmount,merOrderId);
                weixinrefund.put("refundStatus","SUCCESS");
                resultMap.put("errCode","SUCCESS");
                resultMap.put("respStr",JSONObject.fromObject(weixinrefund).toString());
                String resultStr = JSONObject.fromObject(resultMap).toString();
                returnmap.put("returninfo",resultStr);
                
                logger.info("发起微信退款。。。。。。。。。content:"+resultStr.toString());
                //添加记录
                LmPayLog lmPayLog = new LmPayLog();
                lmPayLog.setCreatetime(new Date());
                lmPayLog.setOrderno(merOrderId);
                lmPayLog.setType("refund");
                lmPayLog.setSysmsg(resultStr);
                lmPayLogService.insertService(lmPayLog);
            } catch (Exception e) {
                e.printStackTrace();
                resultMap.put("errCode","HttpURLException");
                resultMap.put("msg","微信支付接口异常："+e.toString());
                String resultStr = JSONObject.fromObject(resultMap).toString();
                returnmap.put("returninfo",resultStr);
            }
        }
        logger.info("发起退款。。。。。。。。。服务器返回:"+returnmap);
        return returnmap;
    }



    /**
     * 支付宝云闪付退款账单查询
     */
    @ApiOperation(value = "退款账单查询")
    @RequestMapping(value = "/backorderQuery",method = RequestMethod.POST)
    public Map<String,Object>  backorderQuery(HttpServletResponse response, @ApiParam(name = "merOrderId", value = "订单号", required = true)@RequestParam(value = "merOrderId",defaultValue = "0") String merOrderId){
        System.out.println("请求参数对象："+merOrderId);
        Map<String,Object> returnmap = new HashMap<>();
        Configs configs =configsService.findByTypeId(Configs.type_pay);
        Map map =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
        returnmap.put("config",configs.getConfig());
        //组织请求报文
        JSONObject json = new JSONObject();
        json.put("mid",  map.get("mid"));
        json.put("tid",map.get("tid"));
        json.put("msgType","refundQuery");
        json.put("msgSrc", map.get("msgSrc"));
        json.put("instMid", map.get("instMid"));
        json.put("merOrderId", merOrderId);
        //是否要在商户系统下单，看商户需求  createBill()
        json.put("requestTimestamp", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        json.put("signType", "SHA256");
        Map<String, String> paramsMap = PayUtil.jsonToMap(json);
        paramsMap.put("sign", PayUtil.makeSign(map.get("MD5Key")+"", paramsMap));
        System.out.println("paramsMap："+paramsMap);
        String strReqJsonStr = JSON.toJSONString(paramsMap);
        System.out.println("strReqJsonStr:"+strReqJsonStr);
        //调用银商平台获取二维码接口
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
            //发送POST请求参数
            out = new PrintWriter(httpURLConnection.getOutputStream());
            out.write(strReqJsonStr);
            out.flush();

            //读取响应
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                StringBuffer content = new StringBuffer();
                String tempStr = null;
                in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"UTF-8"));
                while ((tempStr=in.readLine()) != null){
                    content.append(tempStr);
                }
                System.out.println("content:"+content.toString());

                //转换成json对象
                com.alibaba.fastjson.JSONObject respJson = JSON.parseObject(content.toString());
                String resultCode = respJson.getString("errCode");
                resultMap.put("errCode",resultCode);
                resultMap.put("respStr",respJson.toString());
                resultStr = JSONObject.fromObject(resultMap).toString();
                returnmap.put("returninfo",resultStr);
                //添加记录
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
            resultMap.put("msg","调用银商接口出现异常："+e.toString());
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
     * 获取微信openid
     * @param code
     * @return
     */
    private static String getopenid(String code){
        String openid  =  "";
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        Random ran=new Random();
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=wx8d3dd4f8ecbd3b11&secret=61df1215a760d6df205f4dce4d07dd70&grant_type=authorization_code&js_code="+code;
        HttpPost httpPost = new HttpPost(url);
        Map<String,String> map = new HashMap<>();
        // 响应模型
        CloseableHttpResponse response = null;
        String body = JSONUtils.toJSONString(map);
        try {
            httpPost.setEntity(new StringEntity(body,"UTF-8"));
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();

            System.out.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
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
                // 释放资源
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
     * 微信下单 废弃
     */
//    @ApiOperation(value = "微信下单")
//    @GetMapping(value ="/weixinorder" )
//    public Map<String,Object> weixinorder(HttpServletResponse response, @ApiParam(name = "merOrderId", value = "订单号", required = true)@RequestParam(value = "merOrderId",defaultValue = "0") String merOrderId,
//                                          @ApiParam(name = "totalAmount", value = "金额", required = true)@RequestParam(value = "totalAmount",defaultValue = "0") long totalAmount,
//                                          @ApiParam(name = "code", value = "code", required = true)@RequestParam(value = "code",defaultValue = "0") String code
//
//    ){
//        System.out.println("请求参数对象merOrderId{}totalAmount{}："+merOrderId+"  "+totalAmount);
//        Map<String,Object> returnmap = new HashMap<>();
//        LmOrder lmOrder = lmOrderService.findByOrderId(merOrderId);
//        if(lmOrder==null){
//            return returnmap;
//        }
//        //如果是合买订单 则先支付定金
//        if(lmOrder.getType()==3&&lmOrder.getIsprepay()==1){
//            BigDecimal price = lmOrder.getTotalprice().multiply(new BigDecimal("100"));
//            //是否支付过定金
//            if(lmOrder.getDeposit_type()==0){
//                price = price.multiply(new BigDecimal("0.3"));
//            }else{
//                price = price.multiply(new BigDecimal("0.7"));
//            }
//            System.out.println(price.longValue());
//            totalAmount = price.longValue();
//        }
//        //查询是否有相同订单下单请求
//        LmPayLog oldlmPayLog = lmPayLogService.findBymerOrderIdAndType(merOrderId,"placeorder");
//        if(oldlmPayLog!=null){
//            merOrderId = getneworderno();
//            lmOrder.setOrderid(merOrderId);
//        }
//
//        {
//            Configs configs =configsService.findByTypeId(Configs.type_pay);
//            Map map =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
//            returnmap.put("config",configs.getConfig());
//            //组织请求报文
//            JSONObject json = new JSONObject();
//            json.put("instMid", "MINIDEFAULT");
//            json.put("mid", map.get("mid"));
//            json.put("merOrderId", merOrderId);
//            json.put("msgSrc", map.get("msgSrc"));
//            json.put("msgType", "wx.unifiedOrder");
//
//            //是否要在商户系统下单，看商户需求  createBill()
//            json.put("requestTimestamp", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
//            json.put("signType", "SHA256");
//            json.put("subAppId",  map.get("appid"));
//            json.put("subOpenId", getopenid(code));
//            json.put("tradeType", "MINI");
//            json.put("tid", map.get("tid"));
//            json.put("totalAmount", totalAmount);
//            Map<String, String> paramsMap = PayUtil.jsonToMap(json);
//            paramsMap.put("sign", PayUtil.makeSign(map.get("MD5Key")+"", paramsMap));
//            System.out.println("paramsMap："+paramsMap);
//            String strReqJsonStr = JSON.toJSONString(paramsMap);
//            System.out.println("strReqJsonStr:"+strReqJsonStr);
//            //调用银商平台获取二维码接口
//            HttpURLConnection httpURLConnection = null;
//            BufferedReader in = null;
//            PrintWriter out = null;
////        OutputStreamWriter out = null;
//            String resultStr = null;
//            Map<String,Object> resultMap = new HashMap<String,Object>();
//            if (!StringUtils.isNotBlank(map.get("APIurl")+"")) {
//                resultMap.put("errCode","URLFailed");
//                resultMap.put("errorMsg","");
//                resultMap.put("data","");
//                return resultMap;
//            }
//            try {
//                URL url = new URL(map.get("APIurl")+"");
//                httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setRequestMethod("POST");
//                httpURLConnection.setDoInput(true);
//                httpURLConnection.setDoOutput(true);
//                httpURLConnection.setRequestProperty("Content_Type","application/json");
//                httpURLConnection.setRequestProperty("Accept_Charset","UTF-8");
//                httpURLConnection.setRequestProperty("contentType","UTF-8");
//                //发送POST请求参数
//                out = new PrintWriter(httpURLConnection.getOutputStream());
//                out.write(strReqJsonStr);
//                out.flush();
//                //读取响应
//                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                    StringBuffer content = new StringBuffer();
//                    String tempStr = null;
//                    in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"UTF-8"));
//                    while ((tempStr=in.readLine()) != null){
//                        content.append(tempStr);
//                    }
//                    System.out.println("content:"+content.toString());
//                    //转换成json对象
//                    com.alibaba.fastjson.JSONObject respJson = JSON.parseObject(content.toString());
//                    String resultCode = respJson.getString("errCode");
//                    String respstr = respJson.getString("miniPayRequest");
//                    resultMap.put("errCode",resultCode);
//                    resultMap.put("errorMsg","");
//                    resultMap.put("data",JSON.parseObject(respstr, HashMap.class));
//                    //添加记录
//                    LmPayLog lmPayLog = new LmPayLog();
//                    lmPayLog.setCreatetime(new Date());
//                    lmPayLog.setOrderno(merOrderId);
//                    lmPayLog.setType("placeorder");
//                    lmPayLog.setSysmsg(resultStr);
//                    lmPayLogService.insertService(lmPayLog);
//                    lmOrder.setPaystatus("1");
//                    lmOrderService.updateService(lmOrder);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                resultMap.put("errCode","HttpURLException");
//                resultMap.put("msg","调用银商接口出现异常："+e.toString());
//                return resultMap;
//            }finally {
//                if (out != null) {
//                    out.close();
//                }
//                httpURLConnection.disconnect();
//            }
//            System.out.println("resultStr:"+resultStr);
//            return resultMap;
//        }
//
//
//    }


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
