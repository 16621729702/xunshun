package com.wink.livemall.admin.api.member;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.wink.livemall.admin.util.DateUtils;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.PageUtil;
import com.wink.livemall.admin.util.WeixinpayUtil;
import com.wink.livemall.admin.util.payUtil.PayCommonUtil;
import com.wink.livemall.admin.util.payUtil.PayUtil;
import com.wink.livemall.admin.util.payUtil.StringUtil;
import com.wink.livemall.goods.dto.Good;
import com.wink.livemall.goods.dto.LivedGood;
import com.wink.livemall.goods.dto.LmGoodAuction;
import com.wink.livemall.goods.service.GoodService;
import com.wink.livemall.goods.service.LmGoodAuctionService;
import com.wink.livemall.live.dto.LmLive;
import com.wink.livemall.live.service.LmLiveGoodService;
import com.wink.livemall.live.service.LmLiveService;
import com.wink.livemall.member.dto.LmFalsify;
import com.wink.livemall.member.dto.LmMember;
import com.wink.livemall.member.service.LmFalsifyService;
import com.wink.livemall.member.service.LmMemberService;
import com.wink.livemall.merch.dto.LmMerchInfo;
import com.wink.livemall.merch.service.LmMerchInfoService;
import com.wink.livemall.order.dto.LmOrder;
import com.wink.livemall.order.dto.LmOrderGoods;
import com.wink.livemall.order.dto.LmPayLog;
import com.wink.livemall.order.service.LmOrderGoodsService;
import com.wink.livemall.order.service.LmOrderService;
import com.wink.livemall.order.service.LmPayLogService;
import com.wink.livemall.sys.setting.dto.Configs;
import com.wink.livemall.sys.setting.service.ConfigsService;
import com.wink.livemall.utils.cache.redis.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import net.sf.json.JSONObject;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
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

@Api(tags = "违约金列表")
@RestController
@RequestMapping("falsify")
public class LmFalsifyController {

    private static final Logger logger = LogManager.getLogger(LmFalsifyController.class);

    private static final String  SUCCESS_KEY= "SUCCESS";
    private static final String  FAILED_KEY= "FAILED";

    @Autowired
    private LmPayLogService lmPayLogService;
    @Autowired
    private ConfigsService configsService;
    @Autowired
    private LmFalsifyService lmFalsifyService;
    @Autowired
    private RedisUtil redisUtils;
    @Autowired
    private GoodService goodService;
    @Autowired
    private LmMerchInfoService lmMerchInfoService;
    @Autowired
    private LmLiveService lmLiveService;
    @Autowired
    private RedisUtil redisUtil;

    @ApiOperation(value = "获取违约金订单列表")
    @PostMapping("/list")
    public JsonResult getfalsifylist(HttpServletRequest request,
                    @ApiParam(name = "status", value = "-1已结束0未处理1审核中2退款完成3已违约", required = true)@RequestParam(value = "status",required = true,defaultValue = "0") String status,
                    @ApiParam(name = "page", value = "页码",defaultValue = "1",required=true) @RequestParam(value = "page",required = true,defaultValue = "1") int page,
                     @ApiParam(name = "pagesize", value = "每页个数",defaultValue = "10",required=true) @RequestParam(value = "pagesize",required = true,defaultValue = "10") int pagesize){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            String header = request.getHeader("Authorization");
            String userid = "";
            if (!StringUtils.isEmpty(header)) {
                if(!StringUtils.isEmpty(redisUtils.get(header))){
                    userid = redisUtils.get(header)+"";
                }else{
                    jsonResult.setMsg("用户未登陆,请重新登陆");
                    jsonResult.setCode(JsonResult.LOGIN);
                    return jsonResult;
                }
            }
            List<Map<String, Object>> findfalsify = lmFalsifyService.findFalsify(userid, status);
            for(Map<String,Object> map:findfalsify){
                if(map.get("goodstype")!=null&&(int)map.get("goodstype")==1){
                    //直播商品
                    LivedGood good = goodService.findLivedGood((int)map.get("good_id"));
                    map.put("goodname",good.getName());
                    map.put("thumb",good.getImg());
                    map.put("spec","");
                }else{
                    //普通订单
                    Good good = goodService.findById((int)map.get("good_id"));
                    if(good!=null){
                        map.put("goodname",good.getTitle());
                        map.put("thumb",good.getThumb());
                        map.put("spec",good.getSpec());
                    }
                }
            }
            jsonResult.setData(PageUtil.startPage(findfalsify,page,pagesize));
        } catch (Exception e) {
            e.printStackTrace();
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }

    /**
     * 违约金订单详情
     * @param id
     * @return
     */
    @ApiOperation(value = "违约金订单详情")
    @PostMapping("/detail")
    @Transactional
    public JsonResult detail(HttpServletRequest request,
                             @ApiParam(name = "id", value = "违约金id", required = true)@RequestParam(value = "id",required = true,defaultValue = "0") String id
    ){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        Map<String,Object> lmFalsify = new HashMap<>();
        try {
            String header = request.getHeader("Authorization");
            if (!StringUtils.isEmpty(header)) {
                if(StringUtils.isEmpty(redisUtils.get(header))){
                    jsonResult.setMsg("用户未登陆,请重新登陆");
                    jsonResult.setCode(JsonResult.LOGIN);
                    return jsonResult;
                }
            }
            LmFalsify byId = lmFalsifyService.findById(id);
            if(null!=byId){
                LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(String.valueOf(byId.getMerch_id()));
                lmFalsify.put("falsify_id",byId.getFalsify_id());
                lmFalsify.put("type",byId.getType());
                lmFalsify.put("merchname",lmMerchInfo.getStore_name());
                lmFalsify.put("paystatus",byId.getPaystatus());
                lmFalsify.put("goodstype",byId.getGoodstype());
                lmFalsify.put("falsify",byId.getFalsify());
                lmFalsify.put("member_id",byId.getMember_id());
                lmFalsify.put("merch_id",byId.getMerch_id());
                lmFalsify.put("status",byId.getStatus());
                lmFalsify.put("good_id",byId.getGood_id());
                lmFalsify.put("create_time", DateUtils.sdf_yMdHms.format(byId.getCreate_time()));
                if(1==byId.getGoodstype()){
                    //直播商品
                    LivedGood good = goodService.findLivedGood(byId.getGood_id());
                    lmFalsify.put("goodname",good.getName());
                    lmFalsify.put("thumb",good.getImg());
                    lmFalsify.put("spec","");
                }else{
                    //普通订单
                    Good good = goodService.findById(byId.getGood_id());
                    if(good!=null){
                        lmFalsify.put("goodname",good.getTitle());
                        lmFalsify.put("thumb",good.getThumb());
                        lmFalsify.put("spec",good.getSpec());
                    }
                }
            }
            jsonResult.setData(lmFalsify);

        } catch (Exception e) {
            e.printStackTrace();
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }



    @ApiOperation(value = "判断是否可以出价，是否需要缴纳违约金")
    @PostMapping("/break")
    public  JsonResult falsify(HttpServletRequest request,
                               @ApiParam(name = "goodstype", value = "0是店铺1是直播", required = true)@RequestParam(value = "goodstype",required = true,defaultValue = "0") String goodstype,
                               @ApiParam(name = "goodid", value = "商品id", required = true)@RequestParam(value = "goodid",required = true,defaultValue = "0") String goodid){
        JsonResult jsonResult =new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            String header = request.getHeader("Authorization");
            String userid = "";
            if (!StringUtils.isEmpty(header)) {
                if(StringUtils.isEmpty(redisUtils.get(header))){
                    jsonResult.setMsg("用户未登陆,请重新登陆");
                    jsonResult.setCode(JsonResult.LOGIN);
                    return jsonResult;
                }else {
                    userid = redisUtils.get(header)+"";
                }
            }
            LmFalsify isfalsify = lmFalsifyService.isFalsify(userid, goodid, goodstype);
            if(null!=isfalsify){
                jsonResult.setMsg("用户已缴纳过保证金");
                jsonResult.setData(0);
                return jsonResult;
            }else {
                int merchId;
                if(("0").equals(goodstype)){
                    Good good = goodService.findById(Integer.parseInt(goodid));
                    merchId= good.getMer_id();
                }else {
                    LivedGood livedGood = goodService.findLivedGood(Integer.parseInt(goodid));
                    int liveid = livedGood.getLiveid();
                    LmLive lmLive = lmLiveService.findbyId(String.valueOf(liveid));
                    merchId=lmLive.getMerch_id();
                }
                LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(String.valueOf(merchId));
                if(("0").equals(goodstype)){
                    if(1==lmMerchInfo.getAutodeduct()){
                        Good good = goodService.findById(Integer.parseInt(goodid));
                        BigDecimal falsify=good.getMarketprice();
                        falsify=(falsify.multiply(new BigDecimal(30))).divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP);
                        jsonResult.setMsg("用户需要缴纳违约金");
                        jsonResult.setData(falsify);
                    }else {
                        jsonResult.setData(0);
                        jsonResult.setMsg("该店铺无需缴纳保证金");
                        return jsonResult;
                    }
                }else {
                    if(1==lmMerchInfo.getRefund_open()){
                        LivedGood livedGood = goodService.findLivedGood(Integer.parseInt(goodid));
                        BigDecimal falsify=livedGood.getStepprice();
                        jsonResult.setMsg("用户需要缴纳违约金");
                        jsonResult.setData(falsify);
                    }else {
                        jsonResult.setData(0);
                        jsonResult.setMsg("该店铺无需缴纳保证金");
                        return jsonResult;
                    }
                }
            }

        }catch (Exception e){
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }



    /**
     * 微信支付违约金
     */
    @ApiOperation(value = "微信支付违约金")
    @RequestMapping(value = "/wxpayfalsify",method = RequestMethod.POST)
    public JsonResult order(HttpServletRequest request, HttpServletResponse response,
                            @ApiParam(name = "goodstype", value = "0是店铺1是直播", required = true)@RequestParam(value = "goodstype",required = true,defaultValue = "0") String goodstype,
                            @ApiParam(name = "goodid", value = "商品id", required = true)@RequestParam(value = "goodid",defaultValue = "0") String goodid,
                            @ApiParam(name = "falsify", value = "违约金", required = true)@RequestParam(value = "falsify",defaultValue = "0") long falsify
    ){
        JsonResult jsonResult = new JsonResult();
        Map<String,Object> returnmap = new HashMap<>();
        String header = request.getHeader("Authorization");
        String userid = "";
        if (!org.springframework.util.StringUtils.isEmpty(header)) {
            if(!org.springframework.util.StringUtils.isEmpty(redisUtil.get(header))){
                userid = redisUtil.get(header)+"";
            }else{
                jsonResult.setMsg("用户未登录,请重新登陆");
                jsonResult.setCode(JsonResult.LOGIN);
                return jsonResult;
            }
        }
        String prfix="10F9";
        String falsifyId = prfix+System.currentTimeMillis()+ new Random().nextInt(10);
        System.out.println("请求参数对象merOrderId{}totalAmount{}："+falsifyId+"  "+falsify);
        returnmap.put("falsifyId",falsifyId);
        LmPayLog oldlmPayLog = lmPayLogService.findBymerOrderIdAndType(falsifyId,"falsify");
        if(oldlmPayLog!=null){
            returnmap.put("returninfo",oldlmPayLog.getSysmsg());
            jsonResult.setCode(JsonResult.ERROR);
            jsonResult.setMsg("违约金已支付成功");
            jsonResult.setData(returnmap);
            return jsonResult;
        }
        try {
            WeixinpayUtil weixinpayUtil = new WeixinpayUtil();
            Map<String,String> returninfo = weixinpayUtil.wxPayFalsify(falsify+"",falsifyId,"微信下单",getIpAddr(request),goodstype,goodid,userid);
            returninfo.put("falsifyId",falsifyId);
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
     * 微信支付成功回调接口
     * 违约金
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws JDOMException
     */
    @RequestMapping(value = "/falsifyWeiXinPay", produces = MediaType.APPLICATION_JSON_VALUE)
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
            String goodstype = String.valueOf(params.get("goodstype"));
            String goodid = String.valueOf(params.get("goodid"));
            String userid = String.valueOf(params.get("userid"));
            Long price = Long.parseLong(total_fee)/100;
            logger.info("订单号："+out_trade_no);
            logger.info("金额："+total_fee);
            int merch_id=0;
            if(("0").equals(goodstype)){
                Good good = goodService.findById(Integer.parseInt(goodid));
                merch_id=good.getMer_id();
            }else {
                LivedGood livedGood = goodService.findLivedGood(Integer.parseInt(goodid));
               int liveid=livedGood.getLiveid();
                LmLive lmLive = lmLiveService.findbyId(String.valueOf(liveid));
                merch_id= lmLive.getMerch_id();
            }
            //添加违约金订单
            LmFalsify lmFalsify =new LmFalsify();
            lmFalsify.setFalsify_id(out_trade_no);
            lmFalsify.setPaystatus(1);
            lmFalsify.setType(0);
            lmFalsify.setGoodstype(Integer.parseInt(goodstype));
            lmFalsify.setFalsify(new BigDecimal(price));
            lmFalsify.setMember_id(Integer.parseInt(userid));
            lmFalsify.setMerch_id(merch_id);
            lmFalsify.setStatus(0);
            lmFalsify.setGood_id(Integer.parseInt(goodid));
            lmFalsify.setCreate_time(new Date());
            lmFalsifyService.insertService(lmFalsify);
            return_data.put("return_code", "SUCCESS");
            return_data.put("return_msg", "OK");
        }
        return StringUtil.GetMapToXML(return_data);
    }



    /**
     * 违约金请求模块
     * 支付宝云闪付下单
     */
    @ApiOperation(value = "违约金下单模块")
    @RequestMapping(value = "/zfbFalsify", method = RequestMethod.POST)
    public Map<String, Object> zfbFalsify(HttpServletRequest request, HttpServletResponse response,
                                          @ApiParam(name = "type", value = "支付类型", required = true)@RequestParam(value = "type",defaultValue = "0") String type,
                                          @ApiParam(name = "goodstype", value = "0是店铺1是直播", required = true)@RequestParam(value = "goodstype",required = true,defaultValue = "0") String goodstype,
                                          @ApiParam(name = "goodid", value = "商品id", required = true)@RequestParam(value = "goodid",defaultValue = "0") String goodid,
                                          @ApiParam(name = "falsify", value = "违约金", required = true)@RequestParam(value = "falsify",defaultValue = "0") long falsify){
        Map<String, Object> returnmap = new HashMap<>();
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
        Configs configs =configsService.findByTypeId(Configs.falsify_pay);
        Map map =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
        String prfix="10F8";
        String falsifyId = prfix+System.currentTimeMillis()+ new Random().nextInt(10);
        returnmap.put("falsifyId",falsifyId);
        //查询是否有相同订单下单请求
        LmPayLog oldlmPayLog = lmPayLogService.findBymerOrderIdAndType(falsifyId, "falsify");
        if (oldlmPayLog != null) {
            returnmap.put("returninfo",oldlmPayLog.getSysmsg());
            returnmap.put("return_msg", "违约金已支付成功");
            return returnmap;
        }
        String  paystatus="";
        if ("trade.precreate".equals(type)) {
            paystatus ="0";
        }
        if ("wx.unifiedOrder".equals(type)) {
            paystatus ="1";
        }
        if ("uac.appOrder".equals(type)) {
            paystatus ="2";
        }

        returnmap.put("config",configs.getConfig());
        //组织请求报文
        JSONObject json = new JSONObject();
        json.put("instMid", map.get("instMid"));
        json.put("mid", map.get("mid"));
        json.put("falsifyId", falsifyId);
        json.put("msgSrc", map.get("msgSrc"));
        json.put("msgType", type);
        json.put("userId", userid);
        json.put("payStatus", paystatus);
        json.put("goodsType", goodstype);
        json.put("goodId", goodid);
        json.put("notifyUrl", map.get("notifyUrl"));
        //是否要在商户系统下单，看商户需求  createBill()
        json.put("requestTimestamp", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        json.put("signType", "SHA256");
        json.put("tid", map.get("tid"));
        json.put("falsify", falsify);
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
        if (!org.apache.commons.lang3.StringUtils.isNotBlank(map.get("APIurl")+"")) {
            resultMap.put("errCode","URLFailed");
            resultStr = JSONObject.fromObject(resultMap).toString();
            returnmap.put("returninfo",resultStr);
            return returnmap;
        }
        try{
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
                returnmap.put("returninfo", resultStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnmap.put("errCode", "HttpURLException");
            returnmap.put("msg", "调用银商接口出现异常：" + e.toString());
            returnmap.put("returninfo", returnmap);
            resultStr = JSONObject.fromObject(resultMap).toString();
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



    /**
     * 支付宝 云闪付交易成功回调接口
     * @param request
     * @return
     */
    @ApiOperation(value = "交易成功反馈信息")
    @RequestMapping(value = "/falsifysuccess",method = RequestMethod.POST)
    public String orderQuery(HttpServletRequest request){
        String falsifyId = request.getParameter("falsifyId");
        String falsify = request.getParameter("falsify");
        String userId = request.getParameter("userId");
        String payStatus = request.getParameter("payStatus");
        String goodsType = request.getParameter("goodsType");
        String goodId = request.getParameter("goodId");
        Configs configs =configsService.findByTypeId(Configs.type_pay);
        Map map =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
        Long price = Long.parseLong(falsify)/100;
        Map<String, String> params = PayUtil.getRequestParams(request);
        System.out.println("params："+params);
        // 验签
        boolean checkRet = PayUtil.checkSign(map.get("MD5Key")+"", params);
        try {
            if(checkRet){
                 //添加记录
                LmPayLog lmPayLog = new LmPayLog();
                lmPayLog.setCreatetime(new Date());
                lmPayLog.setOrderno(falsifyId);
                lmPayLog.setType("falsify");
                lmPayLog.setSysmsg(new Gson().toJson(params));
                lmPayLogService.insertService(lmPayLog);
                int merchId=0;
                if(("0").equals(goodsType)){
                    Good good = goodService.findById(Integer.parseInt(goodId));
                    merchId=good.getMer_id();
                }else {
                    LivedGood livedGood = goodService.findLivedGood(Integer.parseInt(goodId));
                    int liveId=livedGood.getLiveid();
                    LmLive lmLive = lmLiveService.findbyId(String.valueOf(liveId));
                    merchId= lmLive.getMerch_id();
                }
                //添加违约金订单
                LmFalsify lmFalsify =new LmFalsify();
                lmFalsify.setFalsify_id(falsifyId);
                lmFalsify.setPaystatus(Integer.parseInt(payStatus));
                lmFalsify.setType(0);
                lmFalsify.setGoodstype(Integer.parseInt(goodsType));
                lmFalsify.setFalsify(new BigDecimal(price));
                lmFalsify.setMember_id(Integer.parseInt(userId));
                lmFalsify.setMerch_id(merchId);
                lmFalsify.setStatus(0);
                lmFalsify.setGood_id(Integer.parseInt(goodId));
                lmFalsify.setCreate_time(new Date());
                lmFalsifyService.insertService(lmFalsify);
                    return SUCCESS_KEY;
                }else{
                    return FAILED_KEY;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return FAILED_KEY;
        }



    @ApiOperation(value = "退款要求接口")
    @RequestMapping(value = "/falsifyrefund",method = RequestMethod.POST)
    public Map<String,Object> refund(HttpServletResponse response,@ApiParam(name = "falsifyId", value = "订单号", required = true)@RequestParam(value = "falsifyId",defaultValue = "0") String falsifyId,
                                     @ApiParam(name = "refundAmount", value = "退款金额", required = true)@RequestParam(value = "refundAmount",defaultValue = "0") String refundAmount) throws Exception {
        System.out.println("请求参数对象："+falsifyId);
        Map<String,Object> returnmap = new HashMap<>();
        //查询是否有相同订单下单请求
        LmPayLog oldlmPayLog = lmPayLogService.findBymerOrderIdAndType(falsifyId,"refund");
        if(oldlmPayLog!=null) {
            returnmap.put("returninfo", oldlmPayLog.getSysmsg());
            Map msgmap =  com.alibaba.fastjson.JSONObject.parseObject(oldlmPayLog.getSysmsg());
            if(msgmap!=null){
                String code = msgmap.get("errCode")+"";
                if(code.equals("SUCCESS")){
                    logger.info("已经发生过退款请求。。。。。。。。。falsifyId:"+falsifyId);
                    return returnmap;
                }
            }
        }
        LmFalsify lmFalsify = lmFalsifyService.findFalsifyId(falsifyId);
        Map<String, String> refundFalsify = lmFalsifyService.isRefundFalsify(falsifyId);
        if(null!=refundFalsify){
            String resultStr = JSONObject.fromObject(refundFalsify).toString();
            returnmap.put("returninfo",resultStr);
            return returnmap;
        }
        // 支付宝 云闪付退款逻辑
        if(!"1".equals(lmFalsify.getPaystatus())){

            logger.info("发起支付宝退款。。。。。。。。。falsifyId:"+falsifyId);
            Configs configs =configsService.findByTypeId(Configs.falsify_pay);
            Map map =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
            returnmap.put("config",configs.getConfig());
            //组织请求报文
            JSONObject json = new JSONObject();
            json.put("mid",  map.get("mid"));
            json.put("tid",map.get("tid"));
            json.put("msgType", "refund");
            json.put("msgSrc", map.get("msgSrc"));
            json.put("instMid", map.get("instMid"));
            json.put("falsifyId", falsifyId);
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
            if (!org.apache.commons.lang3.StringUtils.isNotBlank(map.get("APIurl")+"")) {
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
                    lmPayLog.setOrderno(falsifyId);
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
                Map<String,String> weixinrefund=weixinpayUtil.falsifyRefund(refundAmount,falsifyId);
                weixinrefund.put("refundStatus","SUCCESS");
                resultMap.put("errCode","SUCCESS");
                resultMap.put("respStr",JSONObject.fromObject(weixinrefund).toString());
                String resultStr = JSONObject.fromObject(resultMap).toString();
                returnmap.put("returninfo",resultStr);
                logger.info("发起微信退款。。。。。。。。。content:"+resultStr.toString());
                //添加记录
                LmPayLog lmPayLog = new LmPayLog();
                lmPayLog.setCreatetime(new Date());
                lmPayLog.setOrderno(falsifyId);
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
