package com.wink.livemall.admin.api.member;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.wink.livemall.admin.api.help.CommentService;
import com.wink.livemall.admin.util.*;
import com.wink.livemall.admin.util.httpclient.HttpClient;
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
import com.wink.livemall.member.dto.LmFalsifyRefundReason;
import com.wink.livemall.member.dto.LmMember;
import com.wink.livemall.member.service.LmFalsifyRefundReasonService;
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
import io.swagger.annotations.*;
import io.swagger.models.auth.In;
import net.sf.json.JSONObject;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
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
import java.text.SimpleDateFormat;
import java.util.*;

@Api(tags = "???????????????")
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
    private LmFalsifyRefundReasonService lmFalsifyRefundReasonService;
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
    @Autowired
    private CommentService commentService;

    @ApiOperation(value = "?????????????????????????????????")
    @PostMapping("/list")
    public JsonResult getFalsifyList(HttpServletRequest request,
                    @ApiParam(name = "status", value = "-1?????????0?????????1?????????2????????????3?????????4?????????", required = true)@RequestParam(value = "status",required = true,defaultValue = "0") String status,
                    @ApiParam(name = "page", value = "??????",defaultValue = "1",required=true) @RequestParam(value = "page",required = true,defaultValue = "1") int page,
                     @ApiParam(name = "pagesize", value = "????????????",defaultValue = "10",required=true) @RequestParam(value = "pagesize",required = true,defaultValue = "10") int pagesize){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        String header = request.getHeader("Authorization");
        String userid = "";
        if (!StringUtils.isEmpty(header)) {
            if(!StringUtils.isEmpty(TokenUtil.getUserId(header))){
                userid = TokenUtil.getUserId(header);
            }else{
                jsonResult.setMsg("???????????????,???????????????");
                jsonResult.setCode(JsonResult.LOGIN);
                return jsonResult;
            }
        }
        try {
            List<Map<String, Object>> findfalsify = lmFalsifyService.findFalsify(userid, status);
            for(Map<String,Object> map:findfalsify){
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                map.put("create_time", format.format((Date)map.get("create_time")));
                if(map.get("goodstype")!=null&&(int)map.get("goodstype")==1){
                    //????????????
                    LivedGood good = goodService.findLivedGood((int)map.get("good_id"));
                    map.put("goodName",good.getName());
                    map.put("thumb",good.getImg());
                    map.put("spec","");
                }else{
                    //????????????
                    Good good = goodService.findById((int)map.get("good_id"));
                        map.put("goodName",good.getTitle());
                        map.put("thumb",good.getThumb());
                        map.put("spec",good.getSpec());
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






    @ApiOperation(value = "?????????????????????????????????")
    @PostMapping("/merchList")
    public JsonResult getMerchFalsifyList(HttpServletRequest request,
                                     @ApiParam(name = "status", value = "0????????????1?????????2????????????3?????????", required = true)@RequestParam(value = "status",required = true,defaultValue = "0") String status,
                                     @ApiParam(name = "merchId", value = "??????Id", required = true)@RequestParam(value = "merchId",required = true,defaultValue = "0") String merchId,
                                     @ApiParam(name = "page", value = "??????",defaultValue = "1",required=true) @RequestParam(value = "page",required = true,defaultValue = "1") int page,
                                     @ApiParam(name = "pagesize", value = "????????????",defaultValue = "10",required=true) @RequestParam(value = "pagesize",required = true,defaultValue = "10") int pagesize){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            List<Map<String, Object>> getMerchFalsifyList = lmFalsifyService.getMerchFalsifyList(merchId, status);
            for(Map<String,Object> map:getMerchFalsifyList){
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                map.put("create_time", format.format((Date)map.get("create_time")));
                if(map.get("goodstype")!=null&&(int)map.get("goodstype")==1){
                    //????????????
                    LivedGood good = goodService.findLivedGood((int)map.get("good_id"));
                    map.put("goodName",good.getName());
                    map.put("thumb",good.getImg());
                    map.put("spec","");
                }else{
                    //????????????
                    Good good = goodService.findById((int)map.get("good_id"));
                    map.put("goodName",good.getTitle());
                    map.put("thumb",good.getThumb());
                    map.put("spec",good.getSpec());
                }
            }
            jsonResult.setData(PageUtil.startPage(getMerchFalsifyList,page,pagesize));
        } catch (Exception e) {
            e.printStackTrace();
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }


    @ApiOperation(value = "????????????????????????????????????")
    @RequestMapping(value = "/merchRefund",method = RequestMethod.POST)
    public Map<String, Object> merchRefund(HttpServletResponse response,
                                     @ApiParam(name = "falsifyId", value = "?????????", required = true)@RequestParam(value = "falsifyId",defaultValue = "0") String falsifyId,
                                     @ApiParam(name = "status", value = "0??????1??????", required = true)@RequestParam(value = "status",required = true,defaultValue = "0") String status,
                                     @ApiParam(name = "refundAmount", value = "????????????", required = true)@RequestParam(value = "refundAmount",defaultValue = "0") String refundAmount,
                                     @ApiParam(name = "refusal_instructions", value = "????????????", required = false)@RequestParam(value = "refusal_instructions",defaultValue = "") String refusal_instructions) throws Exception {
        Map<String,Object> returnmap = new HashMap<>();
        try {
            LmFalsify lmFalsify = lmFalsifyService.findFalsifyId(falsifyId);
            if(("0").equals(status)){
                System.out.println(falsifyId+"+++++++++++++++++++="+refundAmount);
                BigDecimal realPrice = lmFalsify.getFalsify();
                if(lmFalsify.getPaystatus()==3){
                    realPrice = realPrice.multiply(new BigDecimal(94.4)).divide(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_DOWN);
                }else {
                    realPrice = realPrice.multiply(new BigDecimal(94.7)).divide(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_DOWN);
                }
                LmMerchInfo byId = lmMerchInfoService.findById(String.valueOf(lmFalsify.getMerch_id()));
                if(byId!=null){
                    byId.setCredit(byId.getCredit().subtract(realPrice).setScale(2,BigDecimal.ROUND_HALF_UP));
                    lmMerchInfoService.updateService(byId);
                }
                commentService.autoRefundFalsify(falsifyId,refundAmount);
                String msg = "???????????????????????????????????????";
                HttpClient httpClient = new HttpClient();
                httpClient.send("????????????",lmFalsify.getMember_id()+"",msg);
                returnmap.put("errCode","SUCCESS");
                returnmap.put("msg","???????????????");
            }else if(("1").equals(status)){
               if(!StringUtils.isEmpty(refusal_instructions)){
                   LmFalsifyRefundReason refundReason = lmFalsifyRefundReasonService.findRefundReason(lmFalsify.getMember_id(), lmFalsify.getGood_id(), lmFalsify.getGoodstype());
                   if(refundReason!=null){
                       refundReason.setRefusal_instructions(refusal_instructions);
                       lmFalsifyRefundReasonService.update(refundReason);
                   }
               }
                lmFalsify.setType(1);
                lmFalsify.setStatus(4);
                lmFalsifyService.updateService(lmFalsify);
                String msg = "?????????????????????????????????????????????,???????????????????????????";
                HttpClient httpClient = new HttpClient();
                httpClient.send("????????????",lmFalsify.getMember_id()+"",msg);
                returnmap.put("errCode","SUCCESS");
                returnmap.put("msg","???????????????");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnmap.put("errCode","FAIL");
            returnmap.put("msg",e.getMessage());
            logger.error(e.getMessage());
            return returnmap;
        }
        return returnmap;
    }

    /**
     * ?????????????????????
     * @param id
     * @return
     */
    @ApiOperation(value = "?????????????????????")
    @PostMapping("/detail")
    @Transactional
    public JsonResult detail(HttpServletRequest request,
                             @ApiParam(name = "id", value = "?????????id", required = true)@RequestParam(value = "id",required = true,defaultValue = "0") String id
    ){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        String header = request.getHeader("Authorization");
        String userid="";
        if (!StringUtils.isEmpty(header)) {
            if(!StringUtils.isEmpty(TokenUtil.getUserId(header))){
                userid = TokenUtil.getUserId(header);
            }else{
                jsonResult.setMsg("???????????????");
                jsonResult.setCode(JsonResult.LOGIN);
                return jsonResult;
            }
        }
        Map<String,Object> lmFalsify = new HashMap<>();
        try {
            LmFalsify byId = lmFalsifyService.findById(id);
            if(byId!=null){
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
                LmFalsifyRefundReason refundReason = lmFalsifyRefundReasonService.findRefundReason(byId.getMember_id(), byId.getGood_id(), byId.getGoodstype());
                if(refundReason!=null){
                    lmFalsify.put("imgs",refundReason.getImgs());
                    lmFalsify.put("description",refundReason.getDescription());
                    lmFalsify.put("refusal_instructions",refundReason.getRefusal_instructions());
                }
                if(1==byId.getGoodstype()){
                    //????????????
                    LivedGood good = goodService.findLivedGood(byId.getGood_id());
                    lmFalsify.put("goodname",good.getName());
                    lmFalsify.put("thumb",good.getImg());
                    lmFalsify.put("spec","");
                }else{
                    //????????????
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



    @ApiOperation(value = "??????????????????????????????????????????????????????")
    @PostMapping("/break")
    public  JsonResult falsify(HttpServletRequest request,
                               @ApiParam(name = "goodstype", value = "0?????????1?????????", required = true)@RequestParam(value = "goodstype",required = true,defaultValue = "0") String goodstype,
                               @ApiParam(name = "goodid", value = "??????id", required = true)@RequestParam(value = "goodid",required = true,defaultValue = "0") String goodid){
        JsonResult jsonResult =new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            String header = request.getHeader("Authorization");
            String userid = "";
            if (!StringUtils.isEmpty(header)) {
                if(StringUtils.isEmpty(TokenUtil.getUserId(header))){
                    jsonResult.setMsg("???????????????,???????????????");
                    jsonResult.setCode(JsonResult.LOGIN);
                    return jsonResult;
                }else {
                        userid = TokenUtil.getUserId(header);

                }
            }
            LmFalsify isfalsify = lmFalsifyService.isFalsify(userid, goodid, goodstype);
            if(isfalsify!=null){
                jsonResult.setMsg("???????????????????????????");
                jsonResult.setData(0);
                return jsonResult;
            }else {
                int buyway;
                if(("0").equals(goodstype)){
                    Good good = goodService.findById(Integer.parseInt(goodid));
                    buyway= good.getBuyway();
                }else {
                    LivedGood livedGood = goodService.findLivedGood(Integer.parseInt(goodid));
                    buyway=livedGood.getBuyway();
                    System.out.print("???????????????????????????+++++++++???"+buyway);
                }
                if(("0").equals(goodstype)){
                    if(1==buyway){
                        Good good = goodService.findById(Integer.parseInt(goodid));
                        BigDecimal falsify=good.getStepprice();
                        BigDecimal falsifyp=new BigDecimal(100).setScale(2,BigDecimal.ROUND_HALF_UP);
                        if(falsify.compareTo(falsifyp)>-1){
                            jsonResult.setData(good.getStepprice());
                        }else {
                            jsonResult.setData(100.00);
                        }
                        jsonResult.setData(good.getStepprice());
                        jsonResult.setMsg("???????????????????????????");

                    }else {
                        jsonResult.setData(0);
                        jsonResult.setMsg("??????????????????????????????");
                        return jsonResult;
                    }
                }else {
                    if(1==buyway){
                        LivedGood livedGood = goodService.findLivedGood(Integer.parseInt(goodid));
                        BigDecimal falsify=livedGood.getStepprice();
                        BigDecimal falsifyp=new BigDecimal(100).setScale(2,BigDecimal.ROUND_HALF_UP);
                        if(falsify.compareTo(falsifyp)>-1){
                            jsonResult.setData(falsify);
                        }else {
                            jsonResult.setData(100.00);
                        }
                        jsonResult.setData(falsify);
                        jsonResult.setMsg("???????????????????????????");

                    }else {
                        jsonResult.setData(0);
                        jsonResult.setMsg("??????????????????????????????");
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
     * ?????????????????????
     */
    @ApiOperation(value = "?????????????????????")
    @RequestMapping(value = "/wxpayfalsify",method = RequestMethod.POST)
    public JsonResult order(HttpServletRequest request, HttpServletResponse response,
                            @ApiParam(name = "goodstype", value = "0?????????1?????????", required = true)@RequestParam(value = "goodstype",required = true,defaultValue = "0") String goodstype,
                            @ApiParam(name = "goodid", value = "??????id", required = true)@RequestParam(value = "goodid",defaultValue = "0") String goodid,
                            @ApiParam(name = "falsify", value = "?????????", required = true)@RequestParam(value = "falsify",defaultValue = "0") long falsify
    ){
        JsonResult jsonResult = new JsonResult();
        Map<String,Object> returnmap = new HashMap<>();
        String header = request.getHeader("Authorization");
        String userid="";
        if (!StringUtils.isEmpty(header)) {
            if(!StringUtils.isEmpty(TokenUtil.getUserId(header))){
                userid = TokenUtil.getUserId(header);
            }else{
                jsonResult.setMsg("???????????????");
                jsonResult.setCode(JsonResult.LOGIN);
                return jsonResult;
            }
        }
        String prfix="10F9";
        String falsifyId = prfix+System.currentTimeMillis()+ new Random().nextInt(10);
        System.out.println("??????????????????merOrderId{}totalAmount{}???"+falsifyId+"  "+falsify);
        returnmap.put("falsifyId",falsifyId);
        try {
            WeixinpayUtil weixinpayUtil = new WeixinpayUtil();
            Map<String,String> returninfo = weixinpayUtil.wxPayFalsify(falsify+"",falsifyId,"????????????",getIpAddr(request),goodstype,goodid,userid);
            returninfo.put("falsifyId",falsifyId);
            int merch_id;
            if(("0").equals(goodstype)){
                Good good = goodService.findById(Integer.parseInt(goodid));
                merch_id=good.getMer_id();
            }else {
                LivedGood livedGood = goodService.findLivedGood(Integer.parseInt(goodid));
                int liveid=livedGood.getLiveid();
                LmLive lmLive = lmLiveService.findbyId(String.valueOf(liveid));
                merch_id= lmLive.getMerch_id();
            }
            Map<String ,Object> map=new HashMap<>();
            map.put("merchId",merch_id);
            map.put("falsifyId",falsifyId);
            String  falsifyP=String.valueOf(falsify);
            map.put("falsify",falsifyP);
            map.put("goodsType",goodstype);
            map.put("goodId",goodid);
            map.put("userId",userid);
            //??????redis ????????????10??????
            redisUtil.hmset(falsifyId,map,10*60*1000);
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
     * ??????????????????????????????
     * ?????????
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws JDOMException
     */
    @RequestMapping(value = "/falsifyWeiXinPay", produces = MediaType.APPLICATION_JSON_VALUE)
    public String notifyWeiXinPay(HttpServletRequest request, HttpServletResponse response) throws IOException, JDOMException {
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
        String falsifyIds = String.valueOf(params.get("out_trade_no"));
        LmPayLog oldlmPayLog = lmPayLogService.findBymerOrderIdAndType(falsifyIds,"falsify");
        if(oldlmPayLog!=null){
            return_data.put("return_code", "FAIL");
            return_data.put("return_msg", "???????????????");
            return StringUtil.GetMapToXML(return_data);
        }
        if (!PayCommonUtil.isTenpaySign(params)) {
            logger.info("?????????????????????");
            String out_trade_no = String.valueOf(params.get("out_trade_no"));
            // ????????????
            return_data.put("return_code", "FAIL");
            return_data.put("return_msg", "return_code?????????");
            return StringUtil.GetMapToXML(return_data);
        } else {
            logger.info("?????????????????????????????????");
            System.out.println("===============????????????==============");
            String out_trade_no = String.valueOf(params.get("out_trade_no"));
            String total_fee = String.valueOf(params.get("total_fee"));
            logger.info("????????????"+out_trade_no);
            logger.info("?????????"+total_fee);
            Map<Object, Object> lmFalsifyMap = redisUtil.hmget(out_trade_no);
            String merchId = String.valueOf(lmFalsifyMap.get("merchId"));
            String falsifyId = String.valueOf(lmFalsifyMap.get("falsifyId"));
            String falsify = String.valueOf(lmFalsifyMap.get("falsify"));
            String goodsType = String.valueOf(lmFalsifyMap.get("goodsType"));
            String goodId = String.valueOf(lmFalsifyMap.get("goodId"));
            String userId = String.valueOf(lmFalsifyMap.get("userId"));
            logger.info("??????+++++++++++++++++++++++"+lmFalsifyMap);
            //?????????????????????
            LmFalsify lmFalsify =new LmFalsify();
            lmFalsify.setFalsify_id(out_trade_no);
            lmFalsify.setPaystatus(1);
            lmFalsify.setType(0);
            lmFalsify.setGoodstype(Integer.parseInt(goodsType));
            BigDecimal falsifyP=new  BigDecimal(falsify);
            falsifyP=falsifyP.divide(new BigDecimal(100));
            lmFalsify.setFalsify(falsifyP);
            lmFalsify.setMember_id(Integer.parseInt(userId));
            lmFalsify.setMerch_id(Integer.parseInt(merchId));
            lmFalsify.setStatus(0);
            lmFalsify.setGood_id(Integer.parseInt(goodId));
            lmFalsify.setCreate_time(new Date());
            lmFalsifyService.insertService(lmFalsify);
            redisUtil.delete(falsifyId);
            //????????????
            LmPayLog lmPayLog = new LmPayLog();
            lmPayLog.setCreatetime(new Date());
            lmPayLog.setOrderno(out_trade_no);
            lmPayLog.setType("falsify");
            lmPayLog.setSysmsg(new Gson().toJson(params));
            lmPayLogService.insertService(lmPayLog);

            return_data.put("return_code", "SUCCESS");
            return_data.put("return_msg", "OK");
        }
        return StringUtil.GetMapToXML(return_data);
    }



    /**
     * ?????????????????????
     * ????????????????????????
     */
    @ApiOperation(value = "?????????????????????")
    @RequestMapping(value = "/zfbFalsify", method = RequestMethod.POST)
    public Map<String, Object> zfbFalsify(HttpServletRequest request, HttpServletResponse response,
                                          @ApiParam(name = "type", value = "????????????", required = true)@RequestParam(value = "type",defaultValue = "0") String type,
                                          @ApiParam(name = "goodstype", value = "0?????????1?????????", required = true)@RequestParam(value = "goodstype",required = true,defaultValue = "0") String goodstype,
                                          @ApiParam(name = "goodid", value = "??????id", required = true)@RequestParam(value = "goodid",defaultValue = "0") String goodid,
                                          @ApiParam(name = "falsify", value = "?????????", required = true)@RequestParam(value = "falsify",defaultValue = "0") long falsify){
        Map<String, Object> returnmap = new HashMap<>();
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
        Configs configs =configsService.findByTypeId(Configs.falsify_pay);
        Map map =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
        String prfix=map.get("msgSrcId")+"";
        String falsifyId = prfix+System.currentTimeMillis()+ new Random().nextInt(10);
        returnmap.put("falsifyId",falsifyId);
        //???????????????????????????????????????
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
        Map<String ,Object> maps=new HashMap<>();
        maps.put("goodsType",goodstype);
        maps.put("payStatus",paystatus);
        maps.put("goodId",goodid);
        maps.put("userId",userid);
        //??????redis ????????????10??????
        redisUtil.hmset(falsifyId,maps,10*60*1000);

        returnmap.put("config",configs.getConfig());
        //??????????????????
        JSONObject json = new JSONObject();
        json.put("instMid", map.get("instMid"));
        json.put("mid", map.get("mid"));
        json.put("merOrderId", falsifyId);
        json.put("msgSrc", map.get("msgSrc"));
        json.put("msgType", type);
        json.put("notifyUrl", map.get("notifyUrl"));
        //????????????????????????????????????????????????  createBill()
        json.put("requestTimestamp", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        json.put("signType", "SHA256");
        json.put("tid", map.get("tid"));
        json.put("totalAmount", falsify);
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
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnmap.put("errCode", "HttpURLException");
            returnmap.put("msg", "?????????????????????????????????" + e.toString());
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
     * ????????? ?????????????????????????????????
     * @param request
     * @return
     */
    @ApiOperation(value = "????????????????????????")
    @RequestMapping(value = "/falsifysuccess",method = RequestMethod.POST)
    public String orderQuery(HttpServletRequest request){


        String falsifyId = request.getParameter("merOrderId");
        String falsify = request.getParameter("totalAmount");
        Map<Object, Object> lmFalsifyMap = redisUtil.hmget(falsifyId);
        String payStatus = String.valueOf(lmFalsifyMap.get("payStatus"));
        String goodsType = String.valueOf(lmFalsifyMap.get("goodsType"));
        String goodId = String.valueOf(lmFalsifyMap.get("goodId"));
        String userId = String.valueOf(lmFalsifyMap.get("userId"));
        Configs configs =configsService.findByTypeId(Configs.type_pay);
        Map map =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
        BigDecimal falsifyP=new  BigDecimal(falsify);
        falsifyP=falsifyP.divide(new BigDecimal(100));
        Map<String, String> params = PayUtil.getRequestParams(request);
        System.out.println("params???"+params);
        LmPayLog oldlmPayLog = lmPayLogService.findBymerOrderIdAndType(falsifyId,"refund");
        if(oldlmPayLog!=null) {
            Map msgmap =  com.alibaba.fastjson.JSONObject.parseObject(oldlmPayLog.getSysmsg());
            if(msgmap!=null){
                String code = msgmap.get("errCode")+"";
                if(code.equals("SUCCESS")){
                    logger.info("??????????????????????????????????????????????????????falsifyId:"+falsifyId);
                    return SUCCESS_KEY;
                }
            }
        }
        // ??????
        boolean checkRet = PayUtil.checkSign(map.get("MD5Key")+"", params);
        try {
            if(checkRet){
                 //????????????
                LmPayLog lmPayLog = new LmPayLog();
                lmPayLog.setCreatetime(new Date());
                lmPayLog.setOrderno(falsifyId);
                lmPayLog.setType("falsify");
                lmPayLog.setSysmsg(new Gson().toJson(params));
                lmPayLogService.insertService(lmPayLog);
                int merchId;
                if(("0").equals(goodsType)){
                    Good good = goodService.findById(Integer.parseInt(goodId));
                    merchId=good.getMer_id();
                }else if(("1").equals(goodsType)){
                    LivedGood livedGood = goodService.findLivedGood(Integer.parseInt(goodId));
                    int liveId=livedGood.getLiveid();
                    LmLive lmLive = lmLiveService.findbyId(String.valueOf(liveId));
                    merchId= lmLive.getMerch_id();
                }else {
                    return SUCCESS_KEY;
                }
                //?????????????????????
                LmFalsify lmFalsify =new LmFalsify();
                lmFalsify.setFalsify_id(falsifyId);
                lmFalsify.setPaystatus(Integer.parseInt(payStatus));
                lmFalsify.setType(0);
                lmFalsify.setGoodstype(Integer.parseInt(goodsType));
                lmFalsify.setFalsify(falsifyP);
                lmFalsify.setMember_id(Integer.parseInt(userId));
                lmFalsify.setMerch_id(merchId);
                lmFalsify.setStatus(0);
                lmFalsify.setGood_id(Integer.parseInt(goodId));
                lmFalsify.setCreate_time(new Date());
                lmFalsifyService.insertService(lmFalsify);
                redisUtil.delete(falsifyId);
                    return SUCCESS_KEY;
                }else{
                    return FAILED_KEY;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return FAILED_KEY;
        }



    @ApiOperation(value = "????????????????????????")
    @RequestMapping(value = "/falsifyrefund",method = RequestMethod.POST)
    public Map<String,Object> refund(HttpServletResponse response,
                                     @ApiParam(name = "falsifyId", value = "?????????", required = true)@RequestParam(value = "falsifyId",defaultValue = "0") String falsifyId,
                                     @ApiParam(name = "refundAmount", value = "????????????", required = true)@RequestParam(value = "refundAmount",defaultValue = "0") String refundAmount,
                                     @ApiParam(name = "imgs", value = "??????", required = false)@RequestParam(value = "imgs",defaultValue = "") String imgs,
                                     @ApiParam(name = "description", value = "????????????", required = false)@RequestParam(value = "description",defaultValue = "") String description
    ) throws Exception {
        System.out.println("?????????????????????"+falsifyId+refundAmount);
        Map<String,Object> returnmap = new HashMap<>();
        //???????????????????????????????????????
        LmPayLog oldlmPayLog = lmPayLogService.findBymerOrderIdAndType(falsifyId,"refund");
        if(oldlmPayLog!=null) {
            returnmap.put("returninfo", oldlmPayLog.getSysmsg());
            Map msgmap =  com.alibaba.fastjson.JSONObject.parseObject(oldlmPayLog.getSysmsg());
            if(msgmap!=null){
                String code = msgmap.get("errCode")+"";
                if(code.equals("SUCCESS")){
                    logger.info("??????????????????????????????????????????????????????falsifyId:"+falsifyId);
                    returnmap.put("msg","???????????????????????????");
                    return returnmap;
                }
            }
        }
        LmFalsify lmFalsify = lmFalsifyService.findFalsifyId(falsifyId);
        lmFalsify.setStatus(1);
        lmFalsifyService.updateService(lmFalsify);
        LmFalsifyRefundReason refundReason = lmFalsifyRefundReasonService.findRefundReason(lmFalsify.getMember_id(), lmFalsify.getGood_id(), lmFalsify.getGoodstype());
        if(refundReason!=null){
            refundReason.setCreat_time(new Date());
            refundReason.setDescription(description);
            refundReason.setFalsify_price(lmFalsify.getFalsify());
            refundReason.setImgs(imgs);
            refundReason.setGood_id(lmFalsify.getGood_id());
            refundReason.setMember_id(lmFalsify.getMember_id());
            refundReason.setType(lmFalsify.getGoodstype());
            lmFalsifyRefundReasonService.update(refundReason);
        }else {
            refundReason=new LmFalsifyRefundReason();
            refundReason.setCreat_time(new Date());
            refundReason.setDescription(description);
            refundReason.setFalsify_price(lmFalsify.getFalsify());
            refundReason.setImgs(imgs);
            refundReason.setGood_id(lmFalsify.getGood_id());
            refundReason.setMember_id(lmFalsify.getMember_id());
            refundReason.setType(lmFalsify.getGoodstype());
            lmFalsifyRefundReasonService.insert(refundReason);
        }
        returnmap.put("errCode","SUCCESS");
        returnmap.put("msg","???????????????,????????????????????????");
        return returnmap;
    }


    /**
     * ????????????
     * @param request
     * @param falsifyId
     * @return
     */
    @ApiOperation(value = "????????????")
    @RequestMapping(value = "/falsifyQuery",method = RequestMethod.POST)
    public HttpJsonResult falsifyQuery(HttpServletRequest request,
                                       @ApiParam(name = "falsifyId", value = "?????????", required = true)@RequestParam(value = "falsifyId",defaultValue = "0") String falsifyId){
        HttpJsonResult   jsonResult=new HttpJsonResult<>();
        LmFalsify lmFalsify = lmFalsifyService.findFalsifyId(falsifyId);
        try {
            if(lmFalsify!=null){
                int status = lmFalsify.getStatus();
                if(0==status){
                    jsonResult.setCode(Errors.SUCCESS.getCode());
                    jsonResult.setMsg(Errors.SUCCESS.getMsg());
                    return jsonResult;
                }else{
                    jsonResult.setCode(Errors.ERROR.getCode());
                    jsonResult.setMsg(Errors.ERROR.getMsg());
                    return jsonResult;
                }
            }else{
                jsonResult.setCode(Errors.ERROR.getCode());
                jsonResult.setMsg("????????????????????????");
                return jsonResult;
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonResult.setCode(Errors.ERROR.getCode());
            jsonResult.setMsg(Errors.ERROR.getMsg());
            return jsonResult;
        }
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
