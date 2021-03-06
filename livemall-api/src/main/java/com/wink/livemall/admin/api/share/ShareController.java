package com.wink.livemall.admin.api.share;

import com.alibaba.fastjson.JSONObject;
import com.wink.livemall.admin.api.live.LiveController;
import com.wink.livemall.admin.util.DateUtils;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.PageUtil;
import com.wink.livemall.admin.util.TokenUtil;
import com.wink.livemall.coupon.dto.LmCouponLog;
import com.wink.livemall.coupon.dto.LmCoupons;
import com.wink.livemall.goods.dto.Good;
import com.wink.livemall.goods.dto.LmLotsInfo;
import com.wink.livemall.goods.dto.LmShareGood;
import com.wink.livemall.goods.service.GoodService;
import com.wink.livemall.goods.service.LotsService;
import com.wink.livemall.live.dto.LmLive;
import com.wink.livemall.live.dto.LmLiveCategory;
import com.wink.livemall.live.service.LmLiveCategoryService;
import com.wink.livemall.live.service.LmLiveGoodService;
import com.wink.livemall.live.service.LmLiveService;
import com.wink.livemall.live.util.QiniuUtil;
import com.wink.livemall.member.dto.LmMember;
import com.wink.livemall.member.dto.LmMemberAddress;
import com.wink.livemall.member.dto.LmMemberFollow;
import com.wink.livemall.member.dto.LmMemberTrace;
import com.wink.livemall.member.service.LmMemberAddressService;
import com.wink.livemall.member.service.LmMemberFollowService;
import com.wink.livemall.member.service.LmMemberService;
import com.wink.livemall.member.service.LmMemberTraceService;
import com.wink.livemall.merch.dto.LmMerchInfo;
import com.wink.livemall.merch.service.LmMerchInfoService;
import com.wink.livemall.order.dto.LmOrder;
import com.wink.livemall.order.dto.LmOrderGoods;
import com.wink.livemall.order.service.LmOrderGoodsService;
import com.wink.livemall.order.service.LmOrderService;
import com.wink.livemall.sys.dict.dto.LmSysDictItem;
import com.wink.livemall.sys.dict.service.Dict_itemService;
import com.wink.livemall.sys.setting.dto.Configs;
import com.wink.livemall.sys.setting.service.ConfigsService;
import com.wink.livemall.utils.cache.redis.RedisUtil;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

import static com.alibaba.druid.util.Utils.md5;

@Api(tags = "????????????")
@RestController
@RequestMapping("/share")
public class ShareController {
    @Autowired
    private RedisUtil redisUtils;
    Logger logger = LogManager.getLogger(LiveController.class);
    @Autowired
    private LmLiveCategoryService lmLiveCategoryService;
    @Autowired
    private LmLiveService lmLiveService;
    @Autowired
    private LmLiveGoodService lmLiveGoodService;
    @Autowired
    private LmMemberAddressService lmMemberAddressService;
    @Autowired
    private LmMemberService lmMemberService;
    @Autowired
    private GoodService goodService;
    @Autowired
    private LmOrderService lmOrderService;
    @Autowired
    private LmOrderGoodsService lmOrderGoodsService;
    @Autowired
    private LmMerchInfoService lmMerchInfoService;
    @Autowired
    private LotsService lotsService;
    @Autowired
    private Dict_itemService dict_itemService;
    @Autowired
    private ConfigsService configsService;
    @Autowired
    private LmMemberTraceService lmMemberTraceService;
    @Autowired
    private LmMemberFollowService lmMemberFollowService;

    /**
     * ????????????????????????
     * @return
     */
    @ApiOperation(value = "????????????????????????")
    @PostMapping("topcategory")
    public JsonResult gettopcategory(){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            List<LmLiveCategory> list = lmLiveCategoryService.findTopListByApi();
            jsonResult.setData(list!=null?list:new ArrayList<>());
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }
    /**
     *
     * @return
     */
    @ApiOperation(value = "????????????")
    @PostMapping("list")
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "String", name = "Authorization", value = "token??????", required = false) })
    public JsonResult toplive(HttpServletRequest request,
                              @ApiParam(name = "type", value = "??????1/??????2/??????3",defaultValue = "2",required=true) @RequestParam(value = "type",required = false) String type,
                              @ApiParam(name = "page", value = "??????",defaultValue = "1",required=false) @RequestParam(value = "page",required = false,defaultValue = "1") int page,
                              @ApiParam(name = "pagesize", value = "????????????",defaultValue = "10",required=false) @RequestParam(value = "pagesize",required = false,defaultValue = "10") int pagesize,
                              @ApiParam(name = "categoryid", value = "??????id", required = false,defaultValue = "2")@RequestParam(value = "categoryid",required = false,defaultValue = "1")  String categoryid){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            String header = request.getHeader("Authorization");
            String userid = "";
            if (!StringUtils.isEmpty(header)) {
                if(!StringUtils.isEmpty(TokenUtil.getUserId(header))){
                    userid = TokenUtil.getUserId(header);
                }else{
                    jsonResult.setCode(JsonResult.LOGIN);
                    return jsonResult;
                }
            }else{
                jsonResult.setCode(JsonResult.LOGIN);
                return jsonResult;
            }
            Map<String,Object> returnmap = new HashMap<>();
            //????????????????????????
            Map<String,String> map = lmLiveService.findShareRecommendLiveByapi();
            returnmap.put("top",map);
            //??????????????????
            if("1".equals(type)){
                if(!StringUtils.isEmpty(userid)){
                    List<Map<String,String>> livelist = lmLiveService.findsharefollewLiveByApi(Integer.parseInt(userid));
                    returnmap.put("list",PageUtil.startPage(livelist,page,pagesize));
                }else{
                    jsonResult.setCode(JsonResult.LOGIN);
                    return jsonResult;
                }
            }else if("2".equals(type)){
                //??????????????????
                List<Map<String,String>> livelist = lmLiveService.findShareHotLiveByApi();
                returnmap.put("list",PageUtil.startPage(livelist,page,pagesize));
            }else{
                List<Map<String,String>> livelist = lmLiveService.findshareListByCategoryIdByApi(Integer.parseInt(categoryid));
                returnmap.put("list",PageUtil.startPage(livelist,page,pagesize));

            }
            jsonResult.setData(returnmap);
        } catch (Exception e) {
            e.printStackTrace();
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }


    /**
     * ?????????????????????
     * @return
     */
    @ApiOperation(value = "?????????????????????")
    @PostMapping("detail")
    public JsonResult detail(
            @ApiParam(name = "liveid", value = "?????????id",defaultValue = "0",required=true) @RequestParam(value = "liveid") String liveid,HttpServletRequest request
    ){
        String header = request.getHeader("Authorization");
        String userid = "";
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        if (!StringUtils.isEmpty(header)) {
            if(!StringUtils.isEmpty(TokenUtil.getUserId(header))){
                userid = TokenUtil.getUserId(header);
            }else{
                jsonResult.setCode(JsonResult.LOGIN);
                return jsonResult;
            }
        }else{
            jsonResult.setCode(JsonResult.LOGIN);
            return jsonResult;
        }

        Map<String,Object> returnmap = new HashMap<>();
        try {
            //??????????????????
            Configs configs = configsService.findByTypeId(Configs.type_flow);
            //?????????????????????
            LmLive lmLive = lmLiveService.findbyId(liveid);
            if(lmLive!=null){
                returnmap.put("livename",lmLive.getName());
                returnmap.put("merchid",lmLive.getMerch_id());
                returnmap.put("liveid",lmLive.getId());
                returnmap.put("watchnum",lmLive.getWatchnum());
                returnmap.put("img",lmLive.getImg());
                returnmap.put("isstart",lmLive.getIsstart());
                returnmap.put("livegroupid",lmLive.getLivegroupid());
                //????????????????????????
                if(configs!=null){
                    String config = configs.getConfig();
                    Map configmap = JSONObject.parseObject(config);
                    String access_key = configmap.get("access_key_id")+"";
                    String access_secret = configmap.get("access_secret")+"";
                    String livepushurl = configmap.get("pushurl")+"";
                    String livereadurl = configmap.get("readurl")+"";
                    String livereadurl2 = configmap.get("readurl2")+"";
                    String livereadurl3 = configmap.get("readurl3")+"";

                    String key = configmap.get("key")+"";
                    String streamKeyPrefix = lmLive.getId()+"";
                    String hubname = configmap.get("hubname")+"";
                    QiniuUtil qiniuUtil = new QiniuUtil(access_key,access_secret,streamKeyPrefix,livepushurl,livereadurl,livereadurl2,livereadurl3,hubname);

                    //rtmp????????????
                    String readurl = qiniuUtil.getreadurl(hubname);
                    //???????????????sign
                    String t = Long.toHexString(System.currentTimeMillis()/1000+5 * 60).toLowerCase();
                    String s = key+"/"+hubname+"/"+streamKeyPrefix+t;
                    String sgin = md5(s).toLowerCase();
                    readurl = readurl+"?sign="+sgin+"&t="+t;
                    returnmap.put("rtmpurl",readurl);
                    //hls????????????
                    readurl = qiniuUtil.getreadurl2(hubname);
                    //???????????????sign
                    t = Long.toHexString(System.currentTimeMillis()/1000+5 * 60).toLowerCase();
                    s = key+"/"+hubname+"/"+streamKeyPrefix+".m3u8"+t;
                    sgin = md5(s).toLowerCase();
                    readurl = readurl+"?sign="+sgin+"&t="+t;
                    returnmap.put("hlsurl",readurl);
                    //hdl????????????
                    readurl = qiniuUtil.getreadurl3(hubname);
                    //???????????????sign
                    t = Long.toHexString(System.currentTimeMillis()/1000+5 * 60).toLowerCase();
                    s = key+"/"+hubname+"/"+streamKeyPrefix+".flv"+t;
                    sgin = md5(s).toLowerCase();
                    readurl = readurl+"?sign="+sgin+"&t="+t;
                    returnmap.put("hdlurl",readurl);

                    String pushurl = qiniuUtil.getpushurl(hubname);
                    returnmap.put("pushurl",pushurl);
                    lmLive.setPushurl(pushurl);
                    lmLiveService.updateService(lmLive);
                }
                //?????????????????????????????????
                LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(lmLive.getMerch_id()+"");
                if(lmMerchInfo!=null){
                    returnmap.put("name",lmMerchInfo.getStore_name());
                    returnmap.put("avatar",lmMerchInfo.getAvatar());
                    returnmap.put("focusnum",lmMerchInfo.getFocusnum());
                }
                if(!StringUtils.isEmpty(userid)){
                    //?????????????????????
                    LmMemberFollow livefollow = lmMemberFollowService.findByMemberidAndTypeAndId(Integer.parseInt(userid), 0, Integer.parseInt(liveid));
                    if(livefollow!=null){
                        if(livefollow.getState()==0) {
                            returnmap.put("livefollowid", livefollow.getId());
                            returnmap.put("liveisfollow", "yes");
                        }else {
                            returnmap.put("liveisfollow","no");
                        }
                    }else{
                        returnmap.put("liveisfollow","no");
                    }
                    //??????????????????
                    List<LmMemberFollow> merchfollowlist = lmMemberFollowService.findByMerchidAndType(1,lmMerchInfo.getId());
                    if(merchfollowlist!=null&&merchfollowlist.size()>0){
                        returnmap.put("merchfollownum",merchfollowlist.size());
                    }
                    LmMemberFollow merchfollow = lmMemberFollowService.findByMemberidAndTypeAndId(Integer.parseInt(userid), 1,lmMerchInfo.getId());
                    if(merchfollow!=null){
                        if(merchfollow.getState()==0) {
                            returnmap.put("merchfollowid", merchfollow.getId());
                            returnmap.put("merchisfollow", "yes");
                        }else {
                            returnmap.put("merchisfollow","no");
                        }
                    }else{
                        returnmap.put("merchisfollow","no");
                    }
                    //????????????
                    LmMemberTrace lmMemberTrace = lmMemberTraceService.findByMemberidAndLiveid(Integer.parseInt(userid),Integer.parseInt(liveid));
                    if(lmMemberTrace==null){
                        lmMemberTrace = new LmMemberTrace();
                        lmMemberTrace.setTrace_time(new Date());
                        lmMemberTrace.setMember_id(Integer.parseInt(userid));
                        lmMemberTrace.setTrace_type(1);
                        lmMemberTrace.setTrace_id(Integer.parseInt(liveid));
                        lmMemberTraceService.insertService(lmMemberTrace);
                    }else{
                        lmMemberTrace.setTrace_time(new Date());
                        lmMemberTraceService.updateService(lmMemberTrace);
                    }
                }
            }
            jsonResult.setData(returnmap);
        } catch (Exception e) {
            e.printStackTrace();
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }
    /**
     * ??????????????????
     * @return
     */
    @ApiOperation(value = "??????????????????")
    @PostMapping("submit")
    @Transactional
    public JsonResult submit( @ApiParam(name = "merchid", value = "??????id", required = true)@RequestParam int merchid,
                              @ApiParam(name = "goodid", value = "??????id", required = true)@RequestParam int goodid,
                              @ApiParam(name = "userid", value = "??????id", required = true) @RequestParam int userid,
                              @ApiParam(name = "price", value = "??????", required = true)@RequestParam String price,
                              @ApiParam(name = "isprepay", value = "??????????????????0???1???", required = true) @RequestParam int isprepay,
                              @ApiParam(name = "addressid", value = "??????id", required = true)@RequestParam int addressid,
                              @ApiParam(name = "num", value = "??????", required = true)@RequestParam String num){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            Configs configs =configsService.findByTypeId(Configs.type_pay);
            Map map =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
            String prfix=map.get("msgSrcId")+"";
            //?????????????????????????????????
            LmOrder toporder = lmOrderService.findTopOrder(goodid,merchid);
            if(toporder==null){
                //???????????????
                Integer maxId = lmOrderService.findMaxId();
                String datetime = DateUtils.sdfyMdHm.format(new Date());
                toporder = new LmOrder();
                toporder.setOrderid(prfix+datetime+maxId);
                toporder.setStatus("0");
                toporder.setType(3);
                toporder.setCreatetime(new Date());
                toporder.setMerchid(merchid);
                toporder.setLots_status(0);
                lmOrderService.insertService(toporder);
                LmOrderGoods lmOrderGoods = new LmOrderGoods();
                lmOrderGoods.setGoodid(goodid);
                LmShareGood lmShareGood = goodService.findshareById(goodid);
                lmOrderGoods.setGoodnum(lmShareGood.getChipped_num());
                if(lmShareGood.getChipped_num()!=1){
                    lmOrderGoods.setGoodprice(lmShareGood.getPrice());
                }else{
                    lmOrderGoods.setGoodprice(lmShareGood.getPrice().multiply(new BigDecimal("1.05")));
                }
                lmOrderGoods.setGoodstype(2);
                lmOrderGoods.setOrderid(toporder.getId());
                lmOrderGoodsService.insertService(lmOrderGoods);
            }
            //???????????????
                LmMemberAddress address = lmMemberAddressService.findById(addressid+"");
                Integer maxId = lmOrderService.findMaxId();
                String datetime = DateUtils.sdfyMdHm.format(new Date());
                LmShareGood lmShareGood = goodService.findshareById(goodid);
                if(address==null){
                    return new JsonResult(JsonResult.ERROR,"????????????");
                }
                LmMember lmMember = lmMemberService.findById(userid+"");
                if(lmMember==null){
                    return new JsonResult(JsonResult.ERROR,"????????????");
                }
                if(lmShareGood==null){
                    return new JsonResult(JsonResult.ERROR,"????????????");
                }
                //???????????????????????????
                List<LmOrder>  lmOrderlist = lmOrderService.findOrderListByPid2(toporder.getId());
                if(lmOrderlist.size()>=lmShareGood.getChipped_num()){
                    return new JsonResult(JsonResult.ERROR,"??????????????????????????????????????????");
                }
                //????????????????????????
                for(LmOrder order:lmOrderlist){
                    if(order.getMemberid()==userid){
                        return new JsonResult(JsonResult.ERROR,"???????????????????????????");
                    }
                }
                    LmOrder lmOrder = new LmOrder();
                    lmOrder.setOrderid(prfix+datetime+maxId);
                    lmOrder.setIsprepay(isprepay);
                    lmOrder.setStatus("0");
                    lmOrder.setType(3);
                    lmOrder.setPaynickname(lmMember.getNickname());
                    lmOrder.setCreatetime(new Date());
                    lmOrder.setMerchid(merchid);
                    lmOrder.setChargeaddress(address.getProvince()+address.getCity()+address.getDistrict()+address.getAddress_info());
                    lmOrder.setChargename(address.getRealname());
                    lmOrder.setChargephone(address.getMobile());
                    lmOrder.setMemberid(userid);
                    lmOrder.setPorderid(toporder.getId());
                    lmOrder.setLots_log_no(0);
                    if(lmShareGood.getChipped_num()!=1){
                        lmOrder.setTotalprice(lmShareGood.getChipped_price().multiply(new BigDecimal(num)));
                        lmOrder.setRealpayprice(lmShareGood.getChipped_price().multiply(new BigDecimal(num)));
                    }else{
                        lmOrder.setTotalprice(lmShareGood.getChipped_price().multiply(new BigDecimal(num)).multiply(new BigDecimal("1.05")));
                        lmOrder.setRealpayprice(lmShareGood.getChipped_price().multiply(new BigDecimal(num)).multiply(new BigDecimal("1.05")));
                    }
                    lmOrder.setRealexpressprice(new BigDecimal(0));
                    lmOrderService.insertService(lmOrder);
                    LmOrderGoods lmOrderGoods = new LmOrderGoods();
                    lmOrderGoods.setGoodid(goodid);
                    lmOrderGoods.setGoodnum(Integer.parseInt(num));
                    if(lmShareGood.getChipped_num()!=1){
                        lmOrderGoods.setGoodprice(lmShareGood.getChipped_price());
                    }else{
                        lmOrderGoods.setGoodprice(lmShareGood.getChipped_price().multiply(new BigDecimal("1.05")));
                    }
                    lmOrderGoods.setOrderid(lmOrder.getId());
                    lmOrderGoods.setGoodstype(2);
                    lmOrderGoodsService.insertService(lmOrderGoods);
            jsonResult.setData(lmOrder.getOrderid());
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());

        }
        return jsonResult;
    }

//    /**
//     * ????????????????????????
//     * @param id
//     * @return
//     */
//    @ApiOperation(value = "????????????????????????")
//    @PostMapping("/pay")
//    public JsonResult pay(HttpServletRequest request,
//                          @ApiParam(name = "id", value = "??????id", required = true)@RequestParam String id,
//                          @ApiParam(name = "price", value = "????????????", required = true)@RequestParam String price
//    ){
//        JsonResult jsonResult = new JsonResult();
//        jsonResult.setCode(JsonResult.SUCCESS);
//        try {
//            LmOrder lmOrder = lmOrderService.findById(id);
//            LmOrderGoods lmOrderGoods = lmOrderGoodsService.findByOrderid(lmOrder.getId());
//            if(lmOrder!=null&&lmOrderGoods!=null){
//                LmShareGood good = goodService.findshareById(lmOrderGoods.getGoodid());
//                if(good!=null){
//                    if(good.getType()==1){
//                        if(lmOrder.getDeposit_type()==1){
//                            lmOrder.setRemain_money(new BigDecimal(price));
//                            lmOrder.setStatus("1");
//                            lmOrder.setPaytime(new Date());
//                            lmOrder.setRemian_time(new Date());
//                        }else{
//                            lmOrder.setPrepay_money(new BigDecimal(price));
//                            lmOrder.setPrepay_time(new Date());
//                            lmOrder.setDeposit_type(1);
//                        }
//                    }else{
//                        lmOrder.setStatus("1");
//                        lmOrder.setPaytime(new Date());
//                    }
//                    lmOrderService.updateService(lmOrder);
//                    List<LmOrder>  lmOrderlist = lmOrderService.findOrderListByPid(lmOrder.getPorderid());
//                    if(lmOrderlist.size()==good.getChipped_num()){
//                        LmOrder porder = lmOrderService.findById(lmOrder.getPorderid()+"");
//                        porder.setChippedtime(new Date());
//                        lmOrderService.updateService(porder);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            jsonResult.setMsg(e.getMessage());
//            jsonResult.setCode(JsonResult.ERROR);
//            logger.error(e.getMessage());
//        }
//        return jsonResult;
//    }

    /**
     * ?????????????????????????????????
     * @return
     */
    @ApiOperation(value = "?????????????????????????????????")
    @PostMapping("goodlist")
    public JsonResult goodlist(
            @ApiParam(name = "liveid", value = "?????????id",defaultValue = "1",required=true) @RequestParam(value = "liveid",required = false) int liveid
    ){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            LmLive lmLive = lmLiveService.findbyId(liveid+"");
            List<Map<String,Object>> returnlist = new ArrayList<>();
            List<Map<String,Object>> goodlist = lmLiveGoodService.findByLiveIdByApi(liveid);
            for(Map<String,Object> map:goodlist){
                Date starttime = (Date) map.get("starttime");
                Date endtime = (Date) map.get("endtime");
            	map.put("starttime", DateUtils.sdf_yMdHms.format(starttime));
            	map.put("endtime", DateUtils.sdf_yMdHms.format(endtime));
                map.put("resttimes", endtime.getTime()-System.currentTimeMillis());
            	//???????????????????????????
                LmOrder toporder = lmOrderService.findTopOrder((int)map.get("id"),lmLive.getMerch_id());
                if(toporder!=null){
                    List<LmOrder>  lmOrderlist = lmOrderService.findOrderListByPid(toporder.getId());
                    if(lmOrderlist!=null){
                        if(lmOrderlist.size()!=(int)map.get("num")){
                            map.put("isbuynum",lmOrderlist.size());
                            returnlist.add(map);
                        }
                    }else{
                        map.put("isbuynum",0);
                        returnlist.add(map);
                    }
                }else{
                    map.put("isbuynum",0);
                    returnlist.add(map);
                }

            }
            jsonResult.setData(returnlist);
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }


    /**
     * ????????????
     * ???????????? ??????????????????
     * ???????????? ??????????????????
     * @return
     */
    @ApiOperation(value = "????????????")
    @PostMapping("myshare")
    public JsonResult myshare(
            @ApiParam(name = "status", value = "??????1?????????2?????????",defaultValue = "1",required=true) @RequestParam(value = "status",required = false) int status,
            @ApiParam(name = "userid", value = "??????id",defaultValue = "1",required=true) @RequestParam(value = "userid") int userid
    ){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        System.out.println(status);
        try {
            List<LmSysDictItem> dictItemList = dict_itemService.findByDictCode("share");
            List<Map<String,Object>> orderlist = lmOrderService.findMyshare(status,userid);
            for(Map<String,Object> map:orderlist){
                map.put("sharedict",dictItemList);
                if(status==1){
                    map.put("lot_no",0);
                }
            }
            jsonResult.setData(orderlist);
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }


    /**
     * ????????????
     * ???????????? ?????????????????????????????????
     * ??????????????? ?????????????????????????????????
     * @return
     */
    @ApiOperation(value = "????????????")
    @PostMapping("sharelist")
    public JsonResult sharelist(HttpServletRequest request,
            @ApiParam(name = "status", value = "??????1?????????2?????????",defaultValue = "1",required=true) @RequestParam(value = "status",required = false) Integer status,
            @ApiParam(name = "merchid", value = "??????id",defaultValue = "1",required=false) @RequestParam(value = "merchid",required = false) Integer merchid,
            @ApiParam(name = "liveid", value = "?????????id",defaultValue = "1",required=false) @RequestParam(value = "liveid",required = false) Integer liveid

    ){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        String header = request.getHeader("Authorization");
        String userid = "";
        if (!StringUtils.isEmpty(header)) {
            if(!StringUtils.isEmpty(TokenUtil.getUserId(header))){
                userid = TokenUtil.getUserId(header);
            }else{
                jsonResult.setCode(JsonResult.LOGIN);
                return jsonResult;
            }
        }

        try {
            LmLive lmLive = lmLiveService.findbyId(liveid+"");
            List<Map<String,Object>> returnmap = new ArrayList<>();
            List<Map<String,Object>> orderlist = lmOrderService.findSharelist(status,liveid,Integer.parseInt(userid));
            List<LmSysDictItem> dictItemList = dict_itemService.findByDictCode("share");
            for(Map<String,Object> map:orderlist){
                //???????????????????????????
                map.put("lot_no","");
                map.put("sharedict",dictItemList);
                LmOrder toporder = lmOrderService.findTopOrder((int)map.get("id"),lmLive.getMerch_id());
                if(toporder!=null){
                    List<LmOrder>  lmOrderlist = lmOrderService.findOrderListByPid(toporder.getId());
                    if(lmOrderlist!=null){
                        map.put("isbuynum",lmOrderlist.size());
                        for(LmOrder order:lmOrderlist){
                            if(order.getMemberid()==Integer.parseInt(userid)){
                                if(status==1){
                                    if(order.getDeposit_type()>0){
                                        returnmap.add(map);
                                    }
                                }
                                if(status==2){
                                    if(order.getDeposit_type()==0){
                                        returnmap.add(map);
                                    }
                                }
                            }
                        }
                    }else{
                        map.put("isbuynum",0);
                    }
                }else{
                    map.put("isbuynum",0);
                }
            }
            jsonResult.setData(orderlist);
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }



    /**
     * ????????????????????????
     * @return
     */
    @ApiOperation(value = "????????????????????????")
    @PostMapping("orderdetail")
    public JsonResult orderdetail(
            @ApiParam(name = "orderid", value = "??????id",defaultValue = "1",required=true) @RequestParam(value = "orderid",required = false) int orderid
    ){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        Map<String,Object> returnmap = new HashMap<>();

        try {
            //??????????????????
            LmOrder lmOrder = lmOrderService.findById(orderid+"");

            if(lmOrder!=null){
                returnmap.put("lotsno",lmOrder.getLots_log_no());
                returnmap.put("id",lmOrder.getId());
                returnmap.put("finishtime",lmOrder.getFinishtime()!=null?DateUtils.sdf_yMdHms.format(lmOrder.getFinishtime()):"");
                returnmap.put("status",lmOrder.getStatus());

                LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(lmOrder.getMerchid()+"");
                if(lmMerchInfo!=null){
                    returnmap.put("merchname",lmMerchInfo.getStore_name());
                    returnmap.put("merchid",lmMerchInfo.getId());
                    returnmap.put("imid",lmMerchInfo.getMember_id());
                }
                LmOrderGoods lmOrderGoods=lmOrderGoodsService.findByOrderid(lmOrder.getId());
                if(lmOrderGoods!=null){
                    returnmap.put("price",lmOrderGoods.getGoodprice());
                    LmShareGood good = goodService.findshareById(lmOrderGoods.getGoodid());
                    if(good!=null){
                        returnmap.put("name",good.getName());
                        returnmap.put("thumb",good.getImg());
                        returnmap.put("material",good.getMaterial());
                    }
                    //??????????????????
                    List<Map<String,Object>> list = lotsService.findLotsInfo(lmOrderGoods.getGoodid());
                    for(Map map:list){
                        map.put("createtime",DateUtils.sdf_yMdHms.format(map.get("createtime")));
                    }
                    returnmap.put("lotinfo",list);
                }

            }
            jsonResult.setData(returnmap);
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }
}
