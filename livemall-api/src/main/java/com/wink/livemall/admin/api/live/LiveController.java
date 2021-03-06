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

@Api(tags = "直播功能")
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
     * 获取所有顶级分类
     * @return
     */
    @ApiOperation(value = "获取所有顶级分类")
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
    @ApiOperation(value = "直播列表")
    @PostMapping("list")
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "String", name = "Authorization", value = "token标记", required = false) })
    public JsonResult toplive(HttpServletRequest request,
                              @ApiParam(name = "type", value = "关注1/热门2/普通3",defaultValue = "1",required=true) @RequestParam(value = "type",required = false) String type,
                              @ApiParam(name = "page", value = "页码",defaultValue = "1",required=false) @RequestParam(value = "page",required = false,defaultValue = "1") int page,
                              @ApiParam(name = "pagesize", value = "每页个数",defaultValue = "10",required=false) @RequestParam(value = "pagesize",required = false,defaultValue = "10") int pagesize,
                              @ApiParam(name = "categoryid", value = "分类id", required = false,defaultValue = "1")@RequestParam(value = "categoryid",required = false,defaultValue = "10") String categoryid){
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
            //获取顶部推荐直播
            Map<String,String> map = lmLiveService.findRecommendLiveByapi();
            returnmap.put("top",map);
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //查询关注列表
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
                //查询热门直播
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
     * 直播间详细信息
     * @return
     */
    @ApiOperation(value = "直播间详细信息")
    @PostMapping("detail")
    public JsonResult detail(
            @ApiParam(name = "liveid", value = "直播间id",defaultValue = "0",required=true) @RequestParam(value = "liveid") String liveid,HttpServletRequest request
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
            //读取推流配置
            Configs configs = configsService.findByTypeId(Configs.type_flow);
            //获取直播间信息
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
                    httpClient.sendgroup(lmLive.getLivegroupid(),"欢迎【"+member.getNickname()+"】来到直播间",99);
                }
                */
                //设置直播播放地址
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

                    //rtmp播放地址
                    String readurl = qiniuUtil.getreadurl(hubname);
                    //添加防盗链sign
                    String t = Long.toHexString(System.currentTimeMillis()/1000+5 * 60).toLowerCase();
                    String s = key+"/"+hubname+"/"+streamKeyPrefix+t;
                    String sgin = md5(s).toLowerCase();
                    readurl = readurl+"?sign="+sgin+"&t="+t;
                    returnmap.put("rtmpurl",readurl);
                    //hls播放地址
                    readurl = qiniuUtil.getreadurl2(hubname);
                    //添加防盗链sign
                    t = Long.toHexString(System.currentTimeMillis()/1000+5 * 60).toLowerCase();
                    s = key+"/"+hubname+"/"+streamKeyPrefix+".m3u8"+t;
                    sgin = md5(s).toLowerCase();
                    readurl = readurl+"?sign="+sgin+"&t="+t;
                    returnmap.put("hlsurl",readurl);
                    //hdl播放地址
                    readurl = qiniuUtil.getreadurl3(hubname);
                    //添加防盗链sign
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
                //获取直播间用户基础信息
                LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(lmLive.getMerch_id()+"");
                if(lmMerchInfo!=null){
                    returnmap.put("name",lmMerchInfo.getStore_name());
                    returnmap.put("avatar",lmMerchInfo.getAvatar());
                    //returnmap.put("focusnum",lmMerchInfo.getFocusnum());
                }
                if(!StringUtils.isEmpty(userid)){
                    //直播间是否关注
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
                    //商户关注长度
                    /*List<LmMemberFollow> merchfollowlist = lmMemberFollowService.findByMerchidAndType(0,lmLive.getId());
                    if(merchfollowlist!=null&&merchfollowlist.size()>0){
                        returnmap.put("focusnum", merchfollowlist.size());
                    }else{
                        returnmap.put("focusnum",0);
                    }*/
                    returnmap.put("focusnum",0);
                    //判断是否为该店铺管理员
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
                    //添加足迹
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
                //获取直播间即时商品信息
                List<Map<String,Object>> livegoodlist = new ArrayList<>();
                //获取一口价
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
                //获取拍卖
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
     * 获取直播拍卖商品
     * @return
     */
    @ApiOperation(value = "获取直播拍卖商品购物车里面")
    @PostMapping("livegoodlist")
    public JsonResult livegoodlist(HttpServletRequest request,
                                   @ApiParam(name = "liveid", value = "直播间id",defaultValue = "0",required=true) @RequestParam(value = "liveid") String liveid,
                                   @ApiParam(name = "type", value = "类型0一口价1拍卖2私价",defaultValue = "0",required=true) @RequestParam(value = "type") String type
    ){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
                //店铺商品
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
     * 获取直播拍卖商品
     * @return
     */
    @ApiOperation(value = "获取直播拍卖商品购物车里面")
    @PostMapping("newlivegoodlist")
    public JsonResult newlivegoodlist(HttpServletRequest request,
            @ApiParam(name = "liveid", value = "直播间id",defaultValue = "0",required=true) @RequestParam(value = "liveid") String liveid,
            @ApiParam(name = "type", value = "类型0一口价1拍卖2私价",defaultValue = "0",required=true) @RequestParam(value = "type") String type
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
                //直播商品
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
                //店铺商品
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
     * 获取直播拍卖商品
     * @return
     */
    @ApiOperation(value = "获取现成直播商品")
    @PostMapping("getlivegood")
    public JsonResult getlivegood(
            @ApiParam(name = "liveid", value = "直播间id",defaultValue = "0",required=true) @RequestParam(value = "liveid") String liveid,
            @ApiParam(name = "type", value = "类型0一口价1拍卖2私价",defaultValue = "0",required=true) @RequestParam(value = "type") String type
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
     * 获取最新价格
     * @return
     */
    @ApiOperation(value = "获取最新价格")
    @PostMapping("nowgoodprice")
    public JsonResult nowgoodprice(
            @ApiParam(name = "goodid", value = "商品id",defaultValue = "0",required=true) @RequestParam(value = "goodid") int goodid,
            @ApiParam(name = "type", value = "类别",defaultValue = "0",required=true) @RequestParam(value = "type") int type
    ){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            if(1==type){
             //直播商品的最新价格
            LivedGood good = goodService.findLivedGood(goodid);
            LmGoodAuction lmGoodAuction = lmGoodAuctionService.findnowPriceByGoodidByApi(goodid,1);
            if(lmGoodAuction!=null){
                jsonResult.setData(lmGoodAuction.getPrice());
            }else{
                jsonResult.setData(good.getPrice());
            }
            }else {
                //店铺商品的最新价格
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
     * 拍卖出价接口
     * @param request
     * @return
     */
    @ApiOperation(value = "拍卖出价接口")
    @PostMapping("/offer")
    @Transactional
    public JsonResult offer(HttpServletRequest request,
                            @ApiParam(name = "price", value = "价格",defaultValue = "1",required=true) @RequestParam(value = "price",defaultValue = "1") String price,
                            @ApiParam(name = "goodid", value = "商品id",defaultValue = "1",required=true) @RequestParam(value = "goodid",defaultValue = "1") int goodid){
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
            //查看商品是否下架
            LivedGood good = goodService.findLivedGood(goodid);
            if(good.getStatus()==1 || (good.getEndtime().getTime())<System.currentTimeMillis()){
                jsonResult.setMsg("商品竞拍结束无法出价");
                jsonResult.setCode(JsonResult.ERROR);
                return jsonResult;
            }
            String redisKey = "paycountlive:" + goodid;
            boolean flag = redisUtils.getLock(redisKey);
            if (flag) {
                // 执行逻辑操作
                List<LmGoodAuction> list = goodService.findAuctionlistByGoodid2(goodid,1);
                if(list!=null&&list.size()>0){
                    LmGoodAuction lmGoodAuction = list.get(0);
                    int compare = lmGoodAuction.getPrice().compareTo(new BigDecimal(price));
                    if(compare > -1){
                        jsonResult.setData(lmGoodAuction.getPrice());
                        jsonResult.setCode(JsonResult.ERROR);
                        jsonResult.setMsg("已有人出价更高");
                        return jsonResult;
                    }
                    logger.info("a---------------开始出价-----------------1");
                    System.out.println("开始出价");
                    LmGoodAuction newauction =new LmGoodAuction();
                    newauction.setCreatetime(new Date());
                    newauction.setGoodid(goodid);
                    newauction.setType(1);
                    newauction.setMemberid(Integer.parseInt(userid));
                    newauction.setPrice(new BigDecimal(price));
                    newauction.setStatus("1");
                    logger.info("a---------------开始出价-----------------2");
                    logger.info("a---------------开始出价-----------------3"+newauction);
                    goodService.insertAuctionService(newauction);
                    for(LmGoodAuction old:list){
                        old.setStatus("0");
                        goodService.updateAuctionService(old);
                    }
                }else{
                	  logger.info("b---------------开始出价-----------------1");
                    System.out.println("开始出价");
                    LmGoodAuction lmGoodAuction =new LmGoodAuction();
                    lmGoodAuction.setCreatetime(new Date());
                    lmGoodAuction.setGoodid(goodid);
                    lmGoodAuction.setMemberid(Integer.parseInt(userid));
                    lmGoodAuction.setPrice(new BigDecimal(price));
                    lmGoodAuction.setStatus("1");
                    lmGoodAuction.setType(1);
                    logger.info("b---------------开始出价-----------------2"+lmGoodAuction);
                    goodService.insertAuctionService(lmGoodAuction);
                }
                if((good.getEndtime().getTime()-System.currentTimeMillis())<=good.getDelaytime()*1000){
                    Date date = new Date(good.getEndtime().getTime()+good.getDelaytime()*1000);
                    good.setEndtime(date);
                }
                
                logger.info("c---------------开始出价-----------------1");
                LmLive lmLive = lmLiveService.findbyId(good.getLiveid()+"");
                HttpClient httpClient = new HttpClient();
                goodService.updateLivedGood(good);
                httpClient.sendgroup(lmLive.getLivegroupid(),"直播拍卖商品发布",6);
                jsonResult.setData(list);
                logger.info("c---------------开始出价-----------------2");
                redisUtils.delete(redisKey);
                logger.info("c---------------开始出价-----------------3");
                
            }else{
                jsonResult.setMsg("当前出价人数过多，请稍后再试");
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
     * 直播生产订单
     * @return
     */
    @ApiOperation(value = "直播拍卖生产订单")
    @PostMapping("/generorder")
    @Transactional
    public JsonResult generorder(HttpServletRequest request,
                                 @ApiParam(name = "goodid", value = "商品id", required = true)@RequestParam int goodid,
                                 @ApiParam(name = "liveid", value = "直播间id", required = true)@RequestParam int liveid
    ){
        JsonResult jsonResult = new JsonResult();
        LmLive live = lmLiveService.findbyId(liveid+"");
        if(live==null){
            jsonResult.setMsg("直播间不存在");
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
            //根据商品 查询商品出价最高的用户
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
                    //判断是否小于0
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
                    //修改用户优惠券信息
                    lmConpouMember.setUseTime(new Date());
                    lmConpouMember.setCanUse(0);
                    lmConpouMember.setOrderId(lmOrder.getId());
                    lmCouponMemberService.updateService(lmConpouMember);
                    //添加优惠券使用记录
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
                    //发送通知 同时用户竞拍成功
                    String msg = "您拍卖的商品："+good.getName()+"已竞拍成功，请尽快支付订单";
//                    pushmsgService.send(0,msg,"2",memberid,0);
                    HttpClient httpClient = new HttpClient();
                    httpClient.send("拍品消息",memberid+"",msg);
                    returnmap.put("orderid",lmOrder.getId());
                    returnmap.put("orderno",lmOrder.getOrderid());
                    Configs tradconfigs =configsService.findByTypeId(Configs.type_trading);
                    Map tradmap =  com.alibaba.fastjson.JSONObject.parseObject(tradconfigs.getConfig());
                    //获取支付限时（小时数）
                    String paytime = tradmap.get("order_cancel_time")+"";
                    returnmap.put("times",Integer.parseInt(paytime)*3600*1000);
                    LmOrderLog lmOrderLog = new LmOrderLog();
                    lmOrderLog.setOrderid(lmOrder.getOrderid());
                    lmOrderLog.setOperatedate(new Date());
                    lmOrderLog.setOperate("订单生产");
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
                //给其他人退款
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
                            String msgs = "您缴纳该:"+good.getName()+"\n商品的违约金已自动退回";
                            httpClient.send("拍品消息",memberId+"",msgs);
                        }
                    }
                }

            }else{
                //设置流拍
                good.setStatus(1);
                //给其他人退款
                List<Map<String, Object>> autoRefundFalsify = lmFalsifyService.autoRefundFalsify(good.getId(),1);
                if(autoRefundFalsify!=null&&autoRefundFalsify.size()>0){
                    for(Map<String,Object> falsifyLists:autoRefundFalsify){
                        int memberId=(int) falsifyLists.get("member_id");
                        String falsifyId=(String) falsifyLists.get("falsify_id");
                        BigDecimal falsify=(BigDecimal)falsifyLists.get("falsify");
                        falsify=falsify.multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP);
                        String falsifyP=String.valueOf(falsify);
                        commentService.autoRefundFalsify(falsifyId,falsifyP);
                        String msg = "您缴纳该:"+good.getName()+"\n商品的违约金已自动退回";
                        HttpClient httpClient = new HttpClient();
                        httpClient.send("拍品消息",memberId+"",msg);
                    }
                }
            }
            }
            //商品设置下架
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
     * 直播生产订单
     * @return
     */
    @ApiOperation(value = "直播一口价私买订单提交")
    @PostMapping("/generorder2")
    @Transactional
    public JsonResult generorder2(HttpServletRequest request,
                                 @ApiParam(name = "goodid", value = "商品id", required = true)@RequestParam int goodid,
                                 @ApiParam(name = "liveid", value = "直播间id", required = true)@RequestParam int liveid,
                                  @ApiParam(name = "memberid", value = "用户id", required = true)@RequestParam int memberid,
                                  @ApiParam(name = "couponid", value = "优惠券id", required = false)@RequestParam(required = false) String couponid
    ){
    	System.out.println("直播一口价/私买订单提交");
        JsonResult jsonResult = new JsonResult();
        LmLive live = lmLiveService.findbyId(liveid+"");
        if(live==null){
            jsonResult.setMsg("直播间不存在");
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
                        //判断是否小于0
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
                        //发送通知 同时用户竞拍成功
                        String msg = "您购买的商品：" + good.getName() + "已下单成功，请尽快支付订单";
                        HttpClient httpClient = new HttpClient();
                        httpClient.send("拍品消息", memberid + "", msg);
                        returnmap.put("orderid", lmOrder.getId());
                        returnmap.put("orderno", lmOrder.getOrderid());
                        Configs tradconfigs = configsService.findByTypeId(Configs.type_trading);
                        Map tradmap = com.alibaba.fastjson.JSONObject.parseObject(tradconfigs.getConfig());
                        //修改用户优惠券信息
                        lmConpouMember.setUseTime(new Date());
                        lmConpouMember.setCanUse(0);
                        lmConpouMember.setOrderId(lmOrder.getId());
                        lmCouponMemberService.updateService(lmConpouMember);
                        //添加优惠券使用记录
                        LmCouponLog lmCouponLog = new LmCouponLog();
                        lmCouponLog.setCouponid(Integer.parseInt(couponid));
                        lmCouponLog.setCreatetime(new Date());
                        lmCouponLog.setMerchid(lmOrder.getMerchid());
                        lmCouponLog.setMemberid(memberid);
                        lmCouponLog.setOrderid(lmOrder.getId());
                        lmCouponLog.setPrice(lmCoupons.getCouponValue());
                        lmCouponsService.insertLogService(lmCouponLog);
                        //获取支付限时（小时数）
                        String paytime = tradmap.get("order_cancel_time") + "";
                        returnmap.put("times", Integer.parseInt(paytime) * 3600 * 1000);
                        LmOrderLog lmOrderLog = new LmOrderLog();
                        lmOrderLog.setOrderid(lmOrder.getOrderid());
                        lmOrderLog.setOperatedate(new Date());
                        lmOrderLog.setOperate("订单生产");
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
                        //发送通知 同时用户竞拍成功
                        String msg = "您购买的商品：" + good.getName() + "已下单成功，请尽快支付订单";
//                    pushmsgService.send(0,msg,"2",memberid,0);
                        HttpClient httpClient = new HttpClient();
                        httpClient.send("拍品消息", memberid + "", msg);
                        returnmap.put("price", lmOrder.getTotalprice());
                        returnmap.put("orderid", lmOrder.getId());
                        returnmap.put("orderno", lmOrder.getOrderid());
                        Configs tradconfigs = configsService.findByTypeId(Configs.type_trading);
                        Map tradmap = com.alibaba.fastjson.JSONObject.parseObject(tradconfigs.getConfig());
                        //获取支付限时（小时数）
                        String paytime = tradmap.get("order_cancel_time") + "";
                        returnmap.put("times", Integer.parseInt(paytime) * 3600 * 1000);
                        LmOrderLog lmOrderLog = new LmOrderLog();
                        lmOrderLog.setOrderid(lmOrder.getOrderid());
                        lmOrderLog.setOperatedate(new Date());
                        lmOrderLog.setOperate("订单生产");
                        lmOrderLogService.insert(lmOrderLog);
                        System.out.println("发送购买信息");
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
              /*//商品设置下架
                System.out.println("下架直播商品");
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
     * 设置直播人数 废弃
     * @return
     */
    @ApiOperation(value = "设置直播人数")
    @PostMapping("setwatchnum")
    public JsonResult setwatchnum(
            @ApiParam(name = "liveid", value = "直播ld",defaultValue = "0",required=true) @RequestParam(value = "liveid") int liveid,
            @ApiParam(name = "watchnum", value = "观看人数",defaultValue = "0",required=true) @RequestParam(value = "watchnum") int watchnum
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
     * 获取直播间人数
     * @return
     */
    @ApiOperation(value = "获取直播间人数和粉丝数")
    @PostMapping("getlivenum")
    public JsonResult getlivenum( @ApiParam(name = "liveid", value = "直播ld",defaultValue = "0",required=true) @RequestParam(value = "liveid") int liveid){
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
     * 获取直播间人数
     * @return
     */
    @ApiOperation(value = "获取直播间粉丝数")
    @PostMapping("getlivefanes")
    public JsonResult getlivefanes( @ApiParam(name = "liveid", value = "直播ld",defaultValue = "0",required=true) @RequestParam(value = "liveid") int liveid){
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
     * 设置禁言接口
     * @return
     */
    @ApiOperation(value = "设置禁言接口")
    @PostMapping("setlivesend")
    public JsonResult setlivesend(@ApiParam(name = "liveid", value = "直播ld",defaultValue = "0",required=true) @RequestParam(value = "liveid") String liveid,
                                  @ApiParam(name = "times", value = "禁言时长0代表解除禁言",defaultValue = "0",required=true) @RequestParam(value = "times") int times,
                                  @ApiParam(name = "memberid", value = "设置用户id",defaultValue = "0",required=true) @RequestParam(value = "memberid") String memberid){
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
     * 推送主播状态
     * @return
     */
    @ApiOperation(value = "设置禁言接口")
    @PostMapping("getLiveState")
    public JsonResult getLiveState(@ApiParam(name = "liveId", value = "直播ld",defaultValue = "0",required=true) @RequestParam(value = "liveId") String liveId,
                                  @ApiParam(name = "state", value = "状态吗",defaultValue = "0",required=true) @RequestParam(value = "state") int state){
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
