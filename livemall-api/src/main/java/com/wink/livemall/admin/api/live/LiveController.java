package com.wink.livemall.admin.api.live;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.qiniu.pili.Stream;
import com.wink.livemall.admin.api.help.CommentService;
import com.wink.livemall.admin.util.DateUtils;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.PageUtil;
import com.wink.livemall.admin.util.TokenUtil;
import com.wink.livemall.admin.util.httpclient.HttpClient;
import com.wink.livemall.coupon.dto.LmConpouMember;
import com.wink.livemall.coupon.dto.LmCouponLog;
import com.wink.livemall.coupon.dto.LmCoupons;
import com.wink.livemall.coupon.service.LmCouponMemberService;
import com.wink.livemall.coupon.service.LmCouponsService;
import com.wink.livemall.goods.dto.Good;
import com.wink.livemall.goods.dto.LivedGood;
import com.wink.livemall.goods.dto.LmGoodAuction;
import com.wink.livemall.goods.service.GoodService;
import com.wink.livemall.goods.service.LmGoodAuctionService;
import com.wink.livemall.live.dto.LmLive;
import com.wink.livemall.live.dto.LmLiveCategory;
import com.wink.livemall.live.dto.LmLiveInfo;
import com.wink.livemall.live.service.LmLiveCategoryService;
import com.wink.livemall.live.service.LmLiveGoodService;
import com.wink.livemall.live.service.LmLiveInfoService;
import com.wink.livemall.live.service.LmLiveService;
import com.wink.livemall.live.util.QiniuUtil;
import com.wink.livemall.member.dto.LmMember;
import com.wink.livemall.member.dto.LmMemberAddress;
import com.wink.livemall.member.dto.LmMemberFollow;
import com.wink.livemall.member.dto.LmMemberTrace;
import com.wink.livemall.member.service.*;
import com.wink.livemall.merch.dto.LmMerchAdmin;
import com.wink.livemall.merch.dto.LmMerchInfo;
import com.wink.livemall.merch.service.LmMerchInfoService;
import com.wink.livemall.order.dto.LmOrder;
import com.wink.livemall.order.dto.LmOrderGoods;
import com.wink.livemall.order.dto.LmOrderLog;
import com.wink.livemall.order.service.LmOrderGoodsService;
import com.wink.livemall.order.service.LmOrderLogService;
import com.wink.livemall.order.service.LmOrderService;
import com.wink.livemall.sys.code.dto.LmSmsVcode;
import com.wink.livemall.sys.setting.dto.Configs;
import com.wink.livemall.sys.setting.service.ConfigsService;
import com.wink.livemall.utils.cache.redis.RedisUtil;
import com.wink.livemall.utils.sms.SmsUtils;
import io.swagger.annotations.*;
import io.swagger.models.auth.In;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.alibaba.druid.util.Utils.md5;

@Api(tags = "????????????")
@RestController
@RequestMapping("live")
public class LiveController {
    @Autowired
    private LmLiveGoodService lmLiveGoodService;
    @Autowired
    private RedisUtil redisUtils;
    private static final Logger logger = LogManager.getLogger(LiveController.class);
    @Autowired
    private LmLiveCategoryService lmLiveCategoryService;
    @Autowired
    private LmLiveService lmLiveService;
    @Autowired
    private LmMerchInfoService lmMerchInfoService;
    @Autowired
    private LmGoodAuctionService lmGoodAuctionService;
    @Autowired
    private GoodService goodService;
    @Autowired
    private LmMemberFollowService lmMemberFollowService;
    @Autowired
    private ConfigsService configsService;
    @Autowired
    private LmOrderService lmOrderService;
    @Autowired
    private LmMemberTraceService lmMemberTraceService;
    @Autowired
    private LmMemberService lmMemberService;
    @Autowired
    private LmMemberAddressService lmMemberAddressService;
    @Autowired
    private LmOrderGoodsService lmOrderGoodsService;
    @Autowired
    private LmOrderLogService lmOrderLogService;
    @Autowired
    private LmLiveInfoService lmLiveInfoService;
    @Autowired
    private LmFalsifyService lmFalsifyService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private LmCouponsService lmCouponsService;
    @Autowired
    private LmCouponMemberService lmCouponMemberService;
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
                              @ApiParam(name = "type", value = "??????1/??????2/??????3",defaultValue = "1",required=true) @RequestParam(value = "type",required = false) String type,
                              @ApiParam(name = "page", value = "??????",defaultValue = "1",required=false) @RequestParam(value = "page",required = false,defaultValue = "1") int page,
                              @ApiParam(name = "pagesize", value = "????????????",defaultValue = "10",required=false) @RequestParam(value = "pagesize",required = false,defaultValue = "10") int pagesize,
                              @ApiParam(name = "categoryid", value = "??????id", required = false,defaultValue = "1")@RequestParam(value = "categoryid",required = false,defaultValue = "10") String categoryid){
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
            }
            Map<String,Object> returnmap = new HashMap<>();
            //????????????????????????
            Map<String,String> map = lmLiveService.findRecommendLiveByapi();
            returnmap.put("top",map);
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //??????????????????
            if("1".equals(type)){
                if(!StringUtils.isEmpty(userid)){
                    List<Map<String,Object>> livelist = lmLiveService.findfollewLiveByApi(Integer.parseInt(userid));
                    for(Map<String,Object> maps:livelist){
                        if(maps.get("preview_time")!=null){
                             maps.put("preview_time",dateFormat.format((Date)maps.get("preview_time")));
                        }
                        int liveid=(int)maps.get("id");
                        LmLive lmLive = lmLiveService.findbyId(liveid+"");
                        LmLiveInfo lmLiveInfo =lmLiveInfoService.findLiveInfo(liveid);
                        maps.put("watchnum",lmLive.getWatchnum()+lmLiveInfo.getWatchnum()+lmLiveInfo.getAddnum());
                    }
                    returnmap.put("list",PageUtil.startPage(livelist,page,pagesize));
                }
            }else if("2".equals(type)){
                //??????????????????
                List<Map<String,Object>> livelist = lmLiveService.findHotLiveByApi();
                for(Map<String,Object> maps:livelist){
                    int liveid=(int)maps.get("id");
                    if(maps.get("preview_time")!=null){
                        maps.put("preview_time",dateFormat.format((Date)maps.get("preview_time")));
                    }
                    LmLive lmLive = lmLiveService.findbyId(liveid+"");
                    LmLiveInfo lmLiveInfo =lmLiveInfoService.findLiveInfo(liveid);
                    maps.put("watchnum",lmLive.getWatchnum()+lmLiveInfo.getWatchnum()+lmLiveInfo.getAddnum());
                }
                returnmap.put("list",PageUtil.startPage(livelist,page,pagesize));
            }else{
                List<Map<String,Object>> livelist = lmLiveService.findListByCategoryIdByApi(Integer.parseInt(categoryid));
                for(Map<String,Object> maps:livelist){
                    int liveid=(int)maps.get("id");
                    if(maps.get("preview_time")!=null){
                        maps.put("preview_time",dateFormat.format((Date)maps.get("preview_time")));
                    }
                    LmLive lmLive = lmLiveService.findbyId(liveid+"");
                    LmLiveInfo lmLiveInfo =lmLiveInfoService.findLiveInfo(liveid);
                    maps.put("watchnum",lmLive.getWatchnum()+lmLiveInfo.getWatchnum()+lmLiveInfo.getAddnum());
                }
                returnmap.put("list",PageUtil.startPage(livelist,page,pagesize));

            }
        jsonResult.setData(returnmap);
        } catch (Exception e) {
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
                
                /**
                LmMember member= this.lmMemberService.findById(userid);
                if(null!=member) {
                    HttpClient httpClient = new HttpClient();
                    httpClient.sendgroup(lmLive.getLivegroupid(),"?????????"+member.getNickname()+"??????????????????",99);
                }
                */
                //????????????????????????
                if(configs!=null)
                {
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
                    //returnmap.put("focusnum",lmMerchInfo.getFocusnum());
                }
                if(!StringUtils.isEmpty(userid)){
                    //?????????????????????
                    LmMemberFollow livefollow = lmMemberFollowService.findByMemberidAndTypeAndId(Integer.parseInt(userid), 0, Integer.parseInt(liveid));
                    if(livefollow!=null){
                        if(livefollow.getState()==0){
                            returnmap.put("livefollowid",livefollow.getId());
                            returnmap.put("liveisfollow","yes");
                        }else {
                            returnmap.put("liveisfollow","no");
                        }
                    }else{
                    	returnmap.put("liveisfollow","no");
                    }
                    //??????????????????
                    /*List<LmMemberFollow> merchfollowlist = lmMemberFollowService.findByMerchidAndType(0,lmLive.getId());
                    if(merchfollowlist!=null&&merchfollowlist.size()>0){
                        returnmap.put("focusnum", merchfollowlist.size());
                    }else{
                        returnmap.put("focusnum",0);
                    }*/
                    returnmap.put("focusnum",0);
                    //?????????????????????????????????
                    LmMerchAdmin lmMerchAdmin = lmMerchInfoService.findMerchAdmin(Integer.parseInt(userid),lmLive.getMerch_id());
                    List<Map<String,Object>> memberinfo = new ArrayList<>();
                    if(lmMerchAdmin!=null){
                        returnmap.put("isadmin",1);
                        returnmap.put("admininfo",null);
                    }else{
                        returnmap.put("isadmin",0);
                        List<LmMerchAdmin>  byadmins=lmMerchInfoService.findAdminByMerchid(lmLive.getMerch_id());
                        if(byadmins!=null){
                           for(LmMerchAdmin Admins:byadmins){
                            LmMember lmMember=lmMemberService.findById(String.valueOf(Admins.getMemberid()));
                            Map<String,Object> merchmap = new HashMap<>();
                            merchmap.put("adminid",lmMember.getId());
                            merchmap.put("adminname",lmMember.getNickname());
                            merchmap.put("adminavatar",lmMember.getAvatar());
                            memberinfo.add(merchmap);
                          }
                            LmLiveInfo lmLiveInfo =lmLiveInfoService.findLiveInfo(lmLive.getId());
                             int  addnum= lmLiveInfo.getAddnum();
                            lmLiveInfo.setAddnum(addnum+4);
                            lmLiveInfoService.updateService(lmLiveInfo);
                            returnmap.put("admininfo",memberinfo);
                        }else {
                            returnmap.put("admininfo",null);
                        }
                    }
                    LmMemberFollow merchfollow = lmMemberFollowService.findByMemberidAndTypeAndId(Integer.parseInt(userid), 1,lmMerchInfo.getId());
                    if(merchfollow!=null){
                        if(merchfollow.getState()==0){
                            returnmap.put("merchfollowid",merchfollow.getId());
                            returnmap.put("merchisfollow","yes");
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
                //?????????????????????????????????
                List<Map<String,Object>> livegoodlist = new ArrayList<>();
                //???????????????
                List<Map<String,Object>> goodlist = lmLiveGoodService.findLivegoodinfoById(liveid,"0");
                for(Map<String,Object> map:goodlist){
                    if(map.get("endtime")!=null){
                        Date endtime = (Date)map.get("endtime");
                        long resttime = endtime.getTime()-System.currentTimeMillis();
                        if(resttime>0){
                            map.put("resttime",resttime);
                        }else{
                            map.put("resttime",0);
                        }
                    }else{
                        map.put("resttime",0);
                    }
                }
                //????????????
                List<Map<String,Object>> goodlist2 = lmLiveGoodService.findLivegoodinfoById(liveid,"1");
                for(Map<String,Object> map:goodlist2){
                    LmGoodAuction lmGoodAuction = lmGoodAuctionService.findnowPriceByGoodidByApi((int)map.get("id"),1);
                    if(lmGoodAuction!=null){
                        map.put("bidsnum",1);
                        map.put("nowprice",lmGoodAuction.getPrice());
                    }else{
                        map.put("bidsnum",0);
                        map.put("nowprice",map.get("price"));
                    }
                    if(map.get("endtime")!=null){
                        Date endtime = (Date)map.get("endtime");
                        long resttime = endtime.getTime()-System.currentTimeMillis();
                        if(resttime>0){
                            map.put("resttime",resttime);
                        }else{
                            map.put("resttime",0);
                        }
                    }
                }
                if(goodlist!=null&&goodlist.size()>0){
                    livegoodlist.addAll(goodlist);
                }
                if(goodlist2!=null&&goodlist2.size()>0){
                    livegoodlist.addAll(goodlist2);
                }
                returnmap.put("livegoodlist",livegoodlist);
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
     * old
     * ????????????????????????
     * @return
     */
    @ApiOperation(value = "???????????????????????????????????????")
    @PostMapping("livegoodlist")
    public JsonResult livegoodlist(HttpServletRequest request,
                                   @ApiParam(name = "liveid", value = "?????????id",defaultValue = "0",required=true) @RequestParam(value = "liveid") String liveid,
                                   @ApiParam(name = "type", value = "??????0?????????1??????2??????",defaultValue = "0",required=true) @RequestParam(value = "type") String type
    ){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
                //????????????
                List<Map<String,Object>> goodlist = lmLiveGoodService.findByLiveIdByApi(liveid,type);
                for(Map<String,Object> map:goodlist){
                    if("1".equals(type)){
                        LmGoodAuction lmGoodAuction = lmGoodAuctionService.findnowPriceByGoodidByApi((int)map.get("id"),0);
                        if(lmGoodAuction!=null){
                            map.put("nowprice",lmGoodAuction.getPrice());
                        }else{
                            map.put("nowprice",map.get("productprice"));
                        }
                    }
                    map.put("producttype","0");
                }
                jsonResult.setData(goodlist);

        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }


    /**
     * NEW
     * ????????????????????????
     * @return
     */
    @ApiOperation(value = "???????????????????????????????????????")
    @PostMapping("newlivegoodlist")
    public JsonResult newlivegoodlist(HttpServletRequest request,
            @ApiParam(name = "liveid", value = "?????????id",defaultValue = "0",required=true) @RequestParam(value = "liveid") String liveid,
            @ApiParam(name = "type", value = "??????0?????????1??????2??????",defaultValue = "0",required=true) @RequestParam(value = "type") String type
    ){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            if("2".equals(type)){
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
                List<Map<String,Object>> goodlist = lmLiveGoodService.findLivegoodtomemberid(liveid,type,userid);
                jsonResult.setData(goodlist);
            }else {
                //????????????
                List<Map<String,Object>> goodlists = lmLiveGoodService.findLivegoodinfoById(liveid,type);
                for(Map<String,Object> map:goodlists){
                    if("1".equals(type)){
                        LmGoodAuction lmGoodAuction = lmGoodAuctionService.findnowPriceByGoodidByApi((int)map.get("id"),1);
                        if(lmGoodAuction!=null){
                            map.put("nowprice",lmGoodAuction.getPrice());
                        }else{
                            map.put("nowprice",map.get("price"));
                        }
                    }
                    if(map.get("endtime")!=null){
                        Date endtime = (Date)map.get("endtime");
                        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        long resttime = endtime.getTime()-System.currentTimeMillis();
                        String endtimes=sf.format(endtime);
                        map.put("endtime",endtimes);
                        if(resttime>0){
                            map.put("resttime",resttime);
                        }else{
                            map.put("resttime",0);
                        }
                    }
                    map.put("producttype","1");
                }
                //????????????
                List<Map<String,Object>> goodlist = lmLiveGoodService.findByLiveIdByApi(liveid,type);
                for(Map<String,Object> map:goodlist){
                    if("1".equals(type)){
                        LmGoodAuction lmGoodAuction = lmGoodAuctionService.findnowPriceByGoodidByApi((int)map.get("id"),0);
                        if(lmGoodAuction!=null){
                            map.put("nowprice",lmGoodAuction.getPrice());
                        }else{
                            map.put("nowprice",map.get("productprice"));
                        }
                    }
                    map.put("producttype","0");
                }
                goodlists.addAll(goodlist);
                jsonResult.setData(goodlists);
            }
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
    @PostMapping("getlivegood")
    public JsonResult getlivegood(
            @ApiParam(name = "liveid", value = "?????????id",defaultValue = "0",required=true) @RequestParam(value = "liveid") String liveid,
            @ApiParam(name = "type", value = "??????0?????????1??????2??????",defaultValue = "0",required=true) @RequestParam(value = "type") String type
    ){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            List<Map<String,Object>> goodlist = lmLiveGoodService.findLivegoodinfoById(liveid,type);
            for(Map<String,Object> map:goodlist){
                if("1".equals(type)){
                    LmGoodAuction lmGoodAuction = lmGoodAuctionService.findnowPriceByGoodidByApi((int)map.get("id"),1);
                    if(lmGoodAuction!=null){
                        map.put("bidsnum",1);
                        map.put("nowprice",lmGoodAuction.getPrice());
                    }else{
                        map.put("bidsnum",0);
                        map.put("nowprice",map.get("price"));
                    }
                }
                if(map.get("endtime")!=null){
                    Date endtime = (Date)map.get("endtime");
                    long resttime = endtime.getTime()-System.currentTimeMillis();
                    if(resttime>0){
                        map.put("resttime",resttime);
                    }else{
                        map.put("resttime",0);
                    }
                }
            }
            jsonResult.setData(goodlist);

        } catch (Exception e) {
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
    @PostMapping("nowgoodprice")
    public JsonResult nowgoodprice(
            @ApiParam(name = "goodid", value = "??????id",defaultValue = "0",required=true) @RequestParam(value = "goodid") int goodid,
            @ApiParam(name = "type", value = "??????",defaultValue = "0",required=true) @RequestParam(value = "type") int type
    ){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            if(1==type){
             //???????????????????????????
            LivedGood good = goodService.findLivedGood(goodid);
            LmGoodAuction lmGoodAuction = lmGoodAuctionService.findnowPriceByGoodidByApi(goodid,1);
            if(lmGoodAuction!=null){
                jsonResult.setData(lmGoodAuction.getPrice());
            }else{
                jsonResult.setData(good.getPrice());
            }
            }else {
                //???????????????????????????
                Good good=goodService.findById(goodid);
                LmGoodAuction lmGoodAuction = lmGoodAuctionService.findnowPriceByGoodidByApi(goodid,0);
                if(lmGoodAuction!=null){
                    jsonResult.setData(lmGoodAuction.getPrice());
                }else{
                    jsonResult.setData(good.getProductprice());
                }
            }
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }


    /**
     * ??????????????????
     * @param request
     * @return
     */
    @ApiOperation(value = "??????????????????")
    @PostMapping("/offer")
    @Transactional
    public JsonResult offer(HttpServletRequest request,
                            @ApiParam(name = "price", value = "??????",defaultValue = "1",required=true) @RequestParam(value = "price",defaultValue = "1") String price,
                            @ApiParam(name = "goodid", value = "??????id",defaultValue = "1",required=true) @RequestParam(value = "goodid",defaultValue = "1") int goodid){
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
            //????????????????????????
            LivedGood good = goodService.findLivedGood(goodid);
            if(good.getStatus()==1 || (good.getEndtime().getTime())<System.currentTimeMillis()){
                jsonResult.setMsg("??????????????????????????????");
                jsonResult.setCode(JsonResult.ERROR);
                return jsonResult;
            }
            String redisKey = "paycountlive:" + goodid;
            boolean flag = redisUtils.getLock(redisKey);
            if (flag) {
                // ??????????????????
                List<LmGoodAuction> list = goodService.findAuctionlistByGoodid2(goodid,1);
                if(list!=null&&list.size()>0){
                    LmGoodAuction lmGoodAuction = list.get(0);
                    int compare = lmGoodAuction.getPrice().compareTo(new BigDecimal(price));
                    if(compare > -1){
                        jsonResult.setData(lmGoodAuction.getPrice());
                        jsonResult.setCode(JsonResult.ERROR);
                        jsonResult.setMsg("?????????????????????");
                        return jsonResult;
                    }
                    logger.info("a---------------????????????-----------------1");
                    System.out.println("????????????");
                    LmGoodAuction newauction =new LmGoodAuction();
                    newauction.setCreatetime(new Date());
                    newauction.setGoodid(goodid);
                    newauction.setType(1);
                    newauction.setMemberid(Integer.parseInt(userid));
                    newauction.setPrice(new BigDecimal(price));
                    newauction.setStatus("1");
                    logger.info("a---------------????????????-----------------2");
                    logger.info("a---------------????????????-----------------3"+newauction);
                    goodService.insertAuctionService(newauction);
                    for(LmGoodAuction old:list){
                        old.setStatus("0");
                        goodService.updateAuctionService(old);
                    }
                }else{
                	  logger.info("b---------------????????????-----------------1");
                    System.out.println("????????????");
                    LmGoodAuction lmGoodAuction =new LmGoodAuction();
                    lmGoodAuction.setCreatetime(new Date());
                    lmGoodAuction.setGoodid(goodid);
                    lmGoodAuction.setMemberid(Integer.parseInt(userid));
                    lmGoodAuction.setPrice(new BigDecimal(price));
                    lmGoodAuction.setStatus("1");
                    lmGoodAuction.setType(1);
                    logger.info("b---------------????????????-----------------2"+lmGoodAuction);
                    goodService.insertAuctionService(lmGoodAuction);
                }
                if((good.getEndtime().getTime()-System.currentTimeMillis())<=good.getDelaytime()*1000){
                    Date date = new Date(good.getEndtime().getTime()+good.getDelaytime()*1000);
                    good.setEndtime(date);
                }
                
                logger.info("c---------------????????????-----------------1");
                LmLive lmLive = lmLiveService.findbyId(good.getLiveid()+"");
                HttpClient httpClient = new HttpClient();
                goodService.updateLivedGood(good);
                httpClient.sendgroup(lmLive.getLivegroupid(),"????????????????????????",6);
                jsonResult.setData(list);
                logger.info("c---------------????????????-----------------2");
                redisUtils.delete(redisKey);
                logger.info("c---------------????????????-----------------3");
                
            }else{
                jsonResult.setMsg("??????????????????????????????????????????");
                jsonResult.setCode(JsonResult.ERROR);
                return jsonResult;
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
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
    @ApiOperation(value = "????????????????????????")
    @PostMapping("/generorder")
    @Transactional
    public JsonResult generorder(HttpServletRequest request,
                                 @ApiParam(name = "goodid", value = "??????id", required = true)@RequestParam int goodid,
                                 @ApiParam(name = "liveid", value = "?????????id", required = true)@RequestParam int liveid
    ){
        JsonResult jsonResult = new JsonResult();
        LmLive live = lmLiveService.findbyId(liveid+"");
        if(live==null){
            jsonResult.setMsg("??????????????????");
            jsonResult.setCode(JsonResult.ERROR);
            return jsonResult;
        }
        jsonResult.setCode(JsonResult.SUCCESS);
        Map<String,Object> returnmap = new HashMap<>();
        Integer maxId = lmOrderService.findMaxId();
        String datetime = DateUtils.sdfyMdHm.format(new Date());
        Configs configs =configsService.findByTypeId(Configs.type_pay);
        Map map =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
        String prfix=map.get("msgSrcId")+"";
        try {
            LivedGood good = goodService.findLivedGood(goodid);
            //???????????? ?????????????????????????????????
            List<LmGoodAuction> lmGoodAuctionList =goodService.findAuctionlistByGoodid2(good.getId(),1);
            LmOrderGoods aloneOrderGoods=lmOrderGoodsService.findByGoodsid1(goodid);
            if(aloneOrderGoods==null){
            if(lmGoodAuctionList!=null&&lmGoodAuctionList.size()>0){
                LmGoodAuction auction = lmGoodAuctionList.get(0);
                auction.setStatus("2");
                goodService.updateAuctionService(auction);
                int memberid = auction.getMemberid();
                List<LmMemberAddress> addresses = lmMemberAddressService.findByMemberid(memberid);
                LmOrder lmOrder = new LmOrder();
                LmMember lmMember = lmMemberService.findById(memberid + "");
                if(addresses!=null&&addresses.size()>0) {
                    LmMemberAddress address = addresses.get(0);
                    lmOrder.setChargeaddress(address.getProvince() + address.getCity() + address.getDistrict() + address.getAddress_info());
                    lmOrder.setChargename(address.getRealname());
                    lmOrder.setChargephone(address.getMobile());
                }

                List<Map<String,Object>> lmCouponsList = lmCouponsService.orderCouponList(memberid,String.valueOf(auction.getPrice()),
                        "1","2", String.valueOf(live.getMerch_id()));
                LmConpouMember lmConpouMember  =null;
                BigDecimal couponValue=new BigDecimal(0);
                if(lmCouponsList!=null&&lmCouponsList.size()>0){
                    Map<String,Object> lists=lmCouponsList.get(0);
                    Number num=(Number)lists.get("id");
                    int couponid = num.intValue();
                    lmConpouMember  = lmCouponMemberService.findById(couponid);
                    couponValue=(BigDecimal)lists.get("couponValue");
                }
                    lmOrder.setOrderid(prfix+datetime+maxId);
                    lmOrder.setStatus("0");
                    lmOrder.setType(2);
                    lmOrder.setSend_type(1);
                    lmOrder.setPaynickname(lmMember.getNickname());
                    lmOrder.setCreatetime(new Date());
                    lmOrder.setMerchid(live.getMerch_id());
                    lmOrder.setMemberid(memberid);
                    lmOrder.setDeposit_type(0);
                    lmOrder.setTotalprice(auction.getPrice());
                if(lmCouponsList!=null&&lmCouponsList.size()>0) {
                    lmOrder.setCoupon_id(lmConpouMember.getCouponId());
                    lmOrder.setCoupon_price(couponValue);
                }
                    lmOrder.setRealexpressprice(new BigDecimal(0));
                    //??????????????????0
                    BigDecimal min=new BigDecimal(0);
                    if(couponValue.compareTo(min)>0){
                        BigDecimal price=auction.getPrice();
                        BigDecimal  realPayPrice= price.subtract(couponValue);
                        if(realPayPrice.compareTo(min)>0){
                            lmOrder.setRealpayprice(realPayPrice);
                            returnmap.put("price",realPayPrice);
                        }else {
                            lmOrder.setRealpayprice(new BigDecimal(0.01).setScale(2,BigDecimal.ROUND_HALF_UP));
                            returnmap.put("price",0.01);
                        }
                    }else {
                        lmOrder.setRealpayprice(auction.getPrice());
                        returnmap.put("price",auction.getPrice());
                    }
                    lmOrder.setIslivegood(1);
                    lmOrderService.insertService(lmOrder);
                 if(lmCouponsList!=null&&lmCouponsList.size()>0){
                    //???????????????????????????
                    lmConpouMember.setUseTime(new Date());
                    lmConpouMember.setCanUse(0);
                    lmConpouMember.setOrderId(lmOrder.getId());
                    lmCouponMemberService.updateService(lmConpouMember);
                    //???????????????????????????
                    LmCouponLog lmCouponLog = new LmCouponLog();
                    lmCouponLog.setCouponid(lmConpouMember.getId());
                    lmCouponLog.setCreatetime(new Date());
                    lmCouponLog.setMerchid(lmOrder.getMerchid());
                    lmCouponLog.setMemberid(memberid);
                    lmCouponLog.setOrderid(lmOrder.getId());
                    lmCouponLog.setPrice(couponValue);
                    lmCouponsService.insertLogService(lmCouponLog);
                 }
                    LmOrderGoods lmOrderGoods = new LmOrderGoods();
                    lmOrderGoods.setGoodid(good.getId());
                    lmOrderGoods.setGoodnum(1);
                    lmOrderGoods.setGoodprice(auction.getPrice());
                    lmOrderGoods.setOrderid(lmOrder.getId());
                     lmOrderGoods.setGoodstype(1);
                    lmOrderGoodsService.insertService(lmOrderGoods);
                    //???????????? ????????????????????????
                    String msg = "?????????????????????"+good.getName()+"???????????????????????????????????????";
//                    pushmsgService.send(0,msg,"2",memberid,0);
                    HttpClient httpClient = new HttpClient();
                    httpClient.send("????????????",memberid+"",msg);
                    returnmap.put("orderid",lmOrder.getId());
                    returnmap.put("orderno",lmOrder.getOrderid());
                    Configs tradconfigs =configsService.findByTypeId(Configs.type_trading);
                    Map tradmap =  com.alibaba.fastjson.JSONObject.parseObject(tradconfigs.getConfig());
                    //?????????????????????????????????
                    String paytime = tradmap.get("order_cancel_time")+"";
                    returnmap.put("times",Integer.parseInt(paytime)*3600*1000);
                    LmOrderLog lmOrderLog = new LmOrderLog();
                    lmOrderLog.setOrderid(lmOrder.getOrderid());
                    lmOrderLog.setOperatedate(new Date());
                    lmOrderLog.setOperate("????????????");
                    lmOrderLogService.insert(lmOrderLog);
                    Map<String,Object> msgmap = new HashMap<>();
                    msgmap.put("username",lmMember.getNickname());
                    msgmap.put("price",auction.getPrice());
                    msgmap.put("resttime",Integer.parseInt(paytime)*3600*1000);
                    msgmap.put("orderno",lmOrder.getOrderid());
                    msgmap.put("userid",lmMember.getId());
                    msgmap.put("liveGoodName",good.getName());
                    msgmap.put("liveImg",good.getImg());
                    httpClient.sendgroup(live.getLivegroupid(), new Gson().toJson(msgmap), 10);
                    Map<String,Object> immap = new HashMap<>();
                    immap.put("userName",lmMember.getNickname());
                if(lmMember.getLevel_id()==0){
                    immap.put("userLevel",1);
                }else {
                    immap.put("userLevel",lmMember.getLevel_id());
                }
                    immap.put("userImg",lmMember.getAvatar());
                    immap.put("liveGoodName",good.getName());
                    immap.put("userId",lmMember.getId());
                    httpClient.sendgroup(live.getLivegroupid(), new Gson().toJson(immap), 21);
                //??????????????????
                List<Map<String, Object>> autoRefundFalsify = lmFalsifyService.autoRefundFalsify(good.getId(),1);
                if(autoRefundFalsify!=null&&autoRefundFalsify.size()>0){
                    for(Map<String,Object> falsifyLists:autoRefundFalsify){
                        int memberId= (int)falsifyLists.get("member_id");
                        if(memberId==memberid){
                            falsifyLists.clear();
                        }else {
                            String falsifyId = (String)falsifyLists.get("falsify_id");
                            BigDecimal falsify = (BigDecimal) falsifyLists.get("falsify");
                            falsify = falsify.multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP);
                            String falsifyp = String.valueOf(falsify);
                            commentService.autoRefundFalsify(falsifyId, falsifyp);
                            String msgs = "????????????:"+good.getName()+"\n?????????????????????????????????";
                            httpClient.send("????????????",memberId+"",msgs);
                        }
                    }
                }

            }else{
                //????????????
                good.setStatus(1);
                //??????????????????
                List<Map<String, Object>> autoRefundFalsify = lmFalsifyService.autoRefundFalsify(good.getId(),1);
                if(autoRefundFalsify!=null&&autoRefundFalsify.size()>0){
                    for(Map<String,Object> falsifyLists:autoRefundFalsify){
                        int memberId=(int) falsifyLists.get("member_id");
                        String falsifyId=(String) falsifyLists.get("falsify_id");
                        BigDecimal falsify=(BigDecimal)falsifyLists.get("falsify");
                        falsify=falsify.multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP);
                        String falsifyP=String.valueOf(falsify);
                        commentService.autoRefundFalsify(falsifyId,falsifyP);
                        String msg = "????????????:"+good.getName()+"\n?????????????????????????????????";
                        HttpClient httpClient = new HttpClient();
                        httpClient.send("????????????",memberId+"",msg);
                    }
                }
            }
            }
            //??????????????????
            good.setStatus(1);
            goodService.updateLivedGood(good);
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
    @ApiOperation(value = "?????????????????????????????????")
    @PostMapping("/generorder2")
    @Transactional
    public JsonResult generorder2(HttpServletRequest request,
                                 @ApiParam(name = "goodid", value = "??????id", required = true)@RequestParam int goodid,
                                 @ApiParam(name = "liveid", value = "?????????id", required = true)@RequestParam int liveid,
                                  @ApiParam(name = "memberid", value = "??????id", required = true)@RequestParam int memberid,
                                  @ApiParam(name = "couponid", value = "?????????id", required = false)@RequestParam(required = false) String couponid
    ){
    	System.out.println("???????????????/??????????????????");
        JsonResult jsonResult = new JsonResult();
        LmLive live = lmLiveService.findbyId(liveid+"");
        if(live==null){
            jsonResult.setMsg("??????????????????");
            jsonResult.setCode(JsonResult.ERROR);
            return jsonResult;
        }
        jsonResult.setCode(JsonResult.SUCCESS);
        Map<String,Object> returnmap = new HashMap<>();
        Integer maxId = lmOrderService.findMaxId();
        String datetime = DateUtils.sdfyMdHm.format(new Date());
        Configs configs =configsService.findByTypeId(Configs.type_pay);
        Map map =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
        String prfix=map.get("msgSrcId")+"";
        try {
            LivedGood good = goodService.findLivedGood(goodid);
            if(good!=null){
                LmConpouMember lmConpouMember =null;
                if(!StringUtils.isEmpty(couponid)){
                    lmConpouMember = lmCouponMemberService.findById(Integer.parseInt(couponid));
                }
                LmCoupons lmCoupons =null;
                if(lmConpouMember!=null){
                    if(1==lmConpouMember.getCanUse()){
                        lmCoupons = lmCouponsService.findById(String.valueOf(lmConpouMember.getCouponId()));
                    }else {
                        lmCoupons=null;
                    }
                }
                if(lmCoupons!=null){
                    List<LmMemberAddress> addresses = lmMemberAddressService.findByMemberid(memberid);
                    if (addresses != null && addresses.size() > 0) {
                        LmMemberAddress address = addresses.get(0);
                        LmMember lmMember = lmMemberService.findById(memberid + "");
                        LmOrder lmOrder = new LmOrder();
                        lmOrder.setOrderid(prfix + datetime + maxId);
                        lmOrder.setStatus("0");
                        lmOrder.setType(1);
                        lmOrder.setPaynickname(lmMember.getNickname());
                        lmOrder.setCreatetime(new Date());
                        lmOrder.setMerchid(live.getMerch_id());
                        lmOrder.setChargeaddress(address.getProvince() + address.getCity() + address.getDistrict() + address.getAddress_info());
                        lmOrder.setChargename(address.getRealname());
                        lmOrder.setChargephone(address.getMobile());
                        lmOrder.setIslivegood(1);
                        lmOrder.setMemberid(memberid);
                        lmOrder.setDeposit_type(0);
                        lmOrder.setTotalprice(good.getPrice());
                        lmOrder.setCoupon_id(lmCoupons.getId());
                        lmOrder.setCoupon_price(lmCoupons.getCouponValue());
                        lmOrder.setRealexpressprice(new BigDecimal(0));
                        //??????????????????0
                        BigDecimal min=new BigDecimal(0);
                        BigDecimal price=good.getPrice();
                        BigDecimal  realPayPrice= price.subtract(lmCoupons.getCouponValue());
                        if(realPayPrice.compareTo(min)>0){
                            lmOrder.setRealpayprice(realPayPrice);
                            returnmap.put("price", realPayPrice);
                        }else {
                            returnmap.put("price", 0.01);
                            lmOrder.setRealpayprice(new BigDecimal(0.01).setScale(2,BigDecimal.ROUND_HALF_UP));
                        }
                        lmOrderService.insertService(lmOrder);
                        LmOrderGoods lmOrderGoods = new LmOrderGoods();
                        lmOrderGoods.setGoodid(good.getId());
                        lmOrderGoods.setGoodnum(1);
                        lmOrderGoods.setGoodstype(1);
                        lmOrderGoods.setGoodprice(good.getPrice());
                        lmOrderGoods.setOrderid(lmOrder.getId());
                        lmOrderGoodsService.insertService(lmOrderGoods);
                        //???????????? ????????????????????????
                        String msg = "?????????????????????" + good.getName() + "???????????????????????????????????????";
                        HttpClient httpClient = new HttpClient();
                        httpClient.send("????????????", memberid + "", msg);
                        returnmap.put("orderid", lmOrder.getId());
                        returnmap.put("orderno", lmOrder.getOrderid());
                        Configs tradconfigs = configsService.findByTypeId(Configs.type_trading);
                        Map tradmap = com.alibaba.fastjson.JSONObject.parseObject(tradconfigs.getConfig());
                        //???????????????????????????
                        lmConpouMember.setUseTime(new Date());
                        lmConpouMember.setCanUse(0);
                        lmConpouMember.setOrderId(lmOrder.getId());
                        lmCouponMemberService.updateService(lmConpouMember);
                        //???????????????????????????
                        LmCouponLog lmCouponLog = new LmCouponLog();
                        lmCouponLog.setCouponid(Integer.parseInt(couponid));
                        lmCouponLog.setCreatetime(new Date());
                        lmCouponLog.setMerchid(lmOrder.getMerchid());
                        lmCouponLog.setMemberid(memberid);
                        lmCouponLog.setOrderid(lmOrder.getId());
                        lmCouponLog.setPrice(lmCoupons.getCouponValue());
                        lmCouponsService.insertLogService(lmCouponLog);
                        //?????????????????????????????????
                        String paytime = tradmap.get("order_cancel_time") + "";
                        returnmap.put("times", Integer.parseInt(paytime) * 3600 * 1000);
                        LmOrderLog lmOrderLog = new LmOrderLog();
                        lmOrderLog.setOrderid(lmOrder.getOrderid());
                        lmOrderLog.setOperatedate(new Date());
                        lmOrderLog.setOperate("????????????");
                        lmOrderLogService.insert(lmOrderLog);
                        if (good.getType() == 0) {
                            httpClient.sendgroup(live.getLivegroupid(), "", 5);
                        }
                        if (good.getType() == 2) {
                            httpClient.sendgroup(live.getLivegroupid(), lmMember.getId() + "", 4);
                        }
                        Map<String,Object> immap = new HashMap<>();
                        immap.put("userName",lmMember.getNickname());
                        if(lmMember.getLevel_id()==0){
                            immap.put("userLevel",1);
                        }else {
                            immap.put("userLevel",lmMember.getLevel_id());
                        }
                        immap.put("userImg",lmMember.getAvatar());
                        immap.put("liveGoodName",good.getName());
                        immap.put("userId",lmMember.getId());
                        httpClient.sendgroup(live.getLivegroupid(), new Gson().toJson(immap), 21);
                    }
                }else {
                    List<LmMemberAddress> addresses = lmMemberAddressService.findByMemberid(memberid);
                    if (addresses != null && addresses.size() > 0) {
                        LmMemberAddress address = addresses.get(0);
                        LmMember lmMember = lmMemberService.findById(memberid + "");
                        LmOrder lmOrder = new LmOrder();
                        lmOrder.setOrderid(prfix + datetime + maxId);
                        lmOrder.setStatus("0");
                        lmOrder.setType(1);
                        lmOrder.setPaynickname(lmMember.getNickname());
                        lmOrder.setCreatetime(new Date());
                        lmOrder.setMerchid(live.getMerch_id());
                        lmOrder.setChargeaddress(address.getProvince() + address.getCity() + address.getDistrict() + address.getAddress_info());
                        lmOrder.setChargename(address.getRealname());
                        lmOrder.setChargephone(address.getMobile());
                        lmOrder.setIslivegood(1);
                        lmOrder.setMemberid(memberid);
                        lmOrder.setDeposit_type(0);
                        lmOrder.setTotalprice(good.getPrice());
                        lmOrder.setRealexpressprice(new BigDecimal(0));
                        lmOrder.setRealpayprice(good.getPrice());
                        lmOrderService.insertService(lmOrder);
                        LmOrderGoods lmOrderGoods = new LmOrderGoods();
                        lmOrderGoods.setGoodid(good.getId());
                        lmOrderGoods.setGoodnum(1);
                        lmOrderGoods.setGoodstype(1);
                        lmOrderGoods.setGoodprice(good.getPrice());
                        lmOrderGoods.setOrderid(lmOrder.getId());
                        lmOrderGoodsService.insertService(lmOrderGoods);
                        //???????????? ????????????????????????
                        String msg = "?????????????????????" + good.getName() + "???????????????????????????????????????";
//                    pushmsgService.send(0,msg,"2",memberid,0);
                        HttpClient httpClient = new HttpClient();
                        httpClient.send("????????????", memberid + "", msg);
                        returnmap.put("price", lmOrder.getTotalprice());
                        returnmap.put("orderid", lmOrder.getId());
                        returnmap.put("orderno", lmOrder.getOrderid());
                        Configs tradconfigs = configsService.findByTypeId(Configs.type_trading);
                        Map tradmap = com.alibaba.fastjson.JSONObject.parseObject(tradconfigs.getConfig());
                        //?????????????????????????????????
                        String paytime = tradmap.get("order_cancel_time") + "";
                        returnmap.put("times", Integer.parseInt(paytime) * 3600 * 1000);
                        LmOrderLog lmOrderLog = new LmOrderLog();
                        lmOrderLog.setOrderid(lmOrder.getOrderid());
                        lmOrderLog.setOperatedate(new Date());
                        lmOrderLog.setOperate("????????????");
                        lmOrderLogService.insert(lmOrderLog);
                        System.out.println("??????????????????");
                        if (good.getType() == 0) {
                            httpClient.sendgroup(live.getLivegroupid(), "", 5);
                        }
                        if (good.getType() == 2) {
                            httpClient.sendgroup(live.getLivegroupid(), lmMember.getId() + "", 4);
                        }
                        Map<String,Object> immap = new HashMap<>();
                        immap.put("userName",lmMember.getNickname());
                        if(lmMember.getLevel_id()==0){
                            immap.put("userLevel",1);
                        }else {
                            immap.put("userLevel",lmMember.getLevel_id());
                        }
                        immap.put("userImg",lmMember.getAvatar());
                        immap.put("liveGoodName",good.getName());
                        immap.put("userId",lmMember.getId());
                        httpClient.sendgroup(live.getLivegroupid(), new Gson().toJson(immap), 21);
                    }
                }
              /*//??????????????????
                System.out.println("??????????????????");
                good.setStatus(1);
                goodService.updateLivedGood(good);*/
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
     * ?????????????????? ??????
     * @return
     */
    @ApiOperation(value = "??????????????????")
    @PostMapping("setwatchnum")
    public JsonResult setwatchnum(
            @ApiParam(name = "liveid", value = "??????ld",defaultValue = "0",required=true) @RequestParam(value = "liveid") int liveid,
            @ApiParam(name = "watchnum", value = "????????????",defaultValue = "0",required=true) @RequestParam(value = "watchnum") int watchnum
    ){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            LmLive lmLive = lmLiveService.findbyId(liveid+"");
            if(lmLive!=null){
                lmLive.setWatchnum(watchnum);
            }
            lmLiveService.updateService(lmLive);
        } catch (Exception e) {
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
    @ApiOperation(value = "?????????????????????????????????")
    @PostMapping("getlivenum")
    public JsonResult getlivenum( @ApiParam(name = "liveid", value = "??????ld",defaultValue = "0",required=true) @RequestParam(value = "liveid") int liveid){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            LmLive lmLive = lmLiveService.findbyId(liveid+"");
            LmLiveInfo lmLiveInfo =lmLiveInfoService.findLiveInfo(liveid);

            List<LmMemberFollow> merchfollowlist = lmMemberFollowService.findByMerchidAndType(0,lmLive.getId());
            Map<String, Object> num = new HashMap<String, Object>();
            num.put("focusnum", merchfollowlist.size()+lmLiveInfo.getFocusnum());
            num.put("watchnum", lmLive.getWatchnum()+lmLiveInfo.getWatchnum()+lmLiveInfo.getAddnum());
            jsonResult.setData(num);
        } catch (Exception e) {
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
    @ApiOperation(value = "????????????????????????")
    @PostMapping("getlivefanes")
    public JsonResult getlivefanes( @ApiParam(name = "liveid", value = "??????ld",defaultValue = "0",required=true) @RequestParam(value = "liveid") int liveid){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            LmLive lmLive = lmLiveService.findbyId(liveid+"");
            List<LmMemberFollow> merchfollowlist = lmMemberFollowService.findByMerchidAndType(0,lmLive.getId());
            String livefanes=String.valueOf(merchfollowlist.size());
            jsonResult.setData(livefanes);
        } catch (Exception e) {
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
    @PostMapping("setlivesend")
    public JsonResult setlivesend(@ApiParam(name = "liveid", value = "??????ld",defaultValue = "0",required=true) @RequestParam(value = "liveid") String liveid,
                                  @ApiParam(name = "times", value = "????????????0??????????????????",defaultValue = "0",required=true) @RequestParam(value = "times") int times,
                                  @ApiParam(name = "memberid", value = "????????????id",defaultValue = "0",required=true) @RequestParam(value = "memberid") String memberid){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            LmLive lmLive = lmLiveService.findbyId(liveid);
            HttpClient httpClient = new HttpClient();
            httpClient.forbid(lmLive.getLivegroupid(),memberid,times);
        } catch (Exception e) {
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
    @PostMapping("getLiveState")
    public JsonResult getLiveState(@ApiParam(name = "liveId", value = "??????ld",defaultValue = "0",required=true) @RequestParam(value = "liveId") String liveId,
                                  @ApiParam(name = "state", value = "?????????",defaultValue = "0",required=true) @RequestParam(value = "state") int state){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            LmLive lmLive = lmLiveService.findbyId(liveId);
            HttpClient httpClient = new HttpClient();
            httpClient.sendgroup(lmLive.getLivegroupid(), "", state);
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }


}
