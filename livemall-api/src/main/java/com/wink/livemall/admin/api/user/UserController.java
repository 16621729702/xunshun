package com.wink.livemall.admin.api.user;

import com.google.gson.Gson;
import com.wink.livemall.admin.dtovo.LevelVo;
import com.wink.livemall.admin.dtovo.UserVo;
import com.wink.livemall.admin.util.DateUtils;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.Md5Util;
import com.wink.livemall.admin.util.PageUtil;
import com.wink.livemall.admin.util.httpclient.HttpClient;
import com.wink.livemall.goods.dto.Good;
import com.wink.livemall.goods.dto.LmGoodAuction;
import com.wink.livemall.goods.service.GoodService;
import com.wink.livemall.goods.service.LmGoodAuctionService;
import com.wink.livemall.member.dto.*;
import com.wink.livemall.member.service.*;
import com.wink.livemall.merch.dto.LmMerchAdmin;
import com.wink.livemall.merch.dto.LmMerchInfo;
import com.wink.livemall.merch.service.LmMerchInfoService;
import com.wink.livemall.sys.code.dto.LmSmsVcode;
import com.wink.livemall.sys.consult.service.ConsultService;
import com.wink.livemall.video.dto.LmVideoCategoary;
import com.wink.livemall.video.dto.LmVideoCore;
import com.wink.livemall.video.service.LmVideoCategoaryService;
import com.wink.livemall.video.service.LmVideoCoreService;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

import static org.springframework.util.StringUtils.isEmpty;

@Api(tags = "用户信息接口")
@RestController
@RequestMapping("user")
public class UserController {
    Logger logger = LogManager.getLogger(UserController.class);
    @Autowired
    private ConsultService consultService;
    @Autowired
    private LmMerchInfoService lmMerchInfoService;
    @Autowired
    private LmMemberTraceService lmMemberTraceService;
    @Autowired
    private LmMemberService lmMemberService;
    @Autowired
    private LmMemberAddressService lmMemberAddressService;
    @Autowired
    private LmMemberFavService lmMemberFavService;
    @Autowired
    private LmMemberFollowService lmMemberFollowService;
    @Autowired
    private GoodService goodService;
    @Autowired
    private LmMemberLevelService lmMemberLevelService;
    @Autowired
    private LmMemberCouponService lmMemberCouponService;

    @Autowired
    private LmGoodAuctionService lmGoodAuctionService;
    /**
     * 修改密码接口
     */
    @ApiOperation(value = "修改密码接口")

    @PostMapping("editpassword")
    @Transactional
    public JsonResult editpassword(HttpServletRequest request,
                                   @ApiParam(name = "mobile", value = "手机号码", required = true)@RequestParam String mobile,
                                   @ApiParam(name = "password", value = "旧密码", required = true)@RequestParam String password,
                                   @ApiParam(name = "newpassword", value = "新密码", required = true)@RequestParam String newpassword){

        JsonResult jsonResult = new JsonResult();
        String msg="";
        try {
            if(!StringUtils.isEmpty(mobile)&&!StringUtils.isEmpty(password)&&!StringUtils.isEmpty(newpassword)){
                LmMember lmMember = lmMemberService.findByMobile(mobile);
                if(lmMember!=null){
                    if(lmMember.getPassword().equals(Md5Util.MD5(password))){
                        lmMember.setPassword(Md5Util.MD5(newpassword));
                        lmMemberService.updateService(lmMember);
                        jsonResult.setCode(JsonResult.SUCCESS);
                    }else{
                        jsonResult.setCode(JsonResult.ERROR);
                        msg="密码错误";
                    }
                }else{
                    jsonResult.setCode(JsonResult.ERROR);
                    msg="该手机号码不存在";
                }
            }else{
                jsonResult.setCode(JsonResult.ERROR);
                msg="手机号码密码不能为空";
            }
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        jsonResult.setMsg(msg);
        return jsonResult;
    }

    /**
     * 获取用户详细信息
     * @return
     */
    @ApiOperation(value = "获取用户详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户id", defaultValue = "0", required = true)
    })
    @PostMapping("detail/{userid}")
    @Transactional
    public JsonResult detail(@PathVariable int userid){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        Map<String,Object> map = new HashMap<>();
        try {
            LmMember lmMember = lmMemberService.findById(userid+"");
           if(lmMember!=null){
        	   if(!StringUtils.isEmpty(lmMember.getLevel_id())){
                   LmMemberLevel lmMemberLevel = lmMemberLevelService.findById(lmMember.getLevel_id()+"");
                   if(lmMemberLevel!=null){
                	   map.put("levelname",lmMemberLevel.getName());
                       map.put("levelcode",lmMemberLevel.getCode());
                   }else{
                	   map.put("levelname","");
                       map.put("levelcode","");
                   }
               }
               List<Map<String,String>> lmMemberAddress = lmMemberAddressService.findByMemberidByapi(lmMember.getId());
               map.put("headimg",lmMember.getAvatar());
               map.put("growth_value",lmMember.getGrowth_value());
               map.put("nickname",lmMember.getNickname());
               map.put("gender",lmMember.getGender());
               map.put("mobile",lmMember.getMobile());
               map.put("address",new Gson().toJson(lmMemberAddress));
               //获取正在直播的关注中的直播间
               List<Map<String, Object>> followList = lmMemberFollowService.findByMemberidAndType(lmMember.getId(),"0");
               for(Map<String, Object> liveinfo:followList){
                   if(1==(int)liveinfo.get("isstart")){
                       map.put("livename",liveinfo.get("livename"));
                       map.put("liveid",liveinfo.get("followid"));
                       if((int)liveinfo.get("type")==1){
                           map.put("type","share");
                       }else{
                           map.put("type","normal");
                       }
                   }
               }
               //查询是否是商户
               List<LmMerchInfo> lmMerchInfoList = lmMerchInfoService.findByMemberid(lmMember.getId());
               List<Map<String,Object>> merchinfo = new ArrayList<>();
               if(lmMerchInfoList!=null&&lmMerchInfoList.size()>0){
                   for(LmMerchInfo lmMerchInfo:lmMerchInfoList){
                      if(lmMerchInfo.getState()==1){
                          Map<String,Object> merchmap = new HashMap<>();
                          merchmap.put("id",lmMerchInfo.getId());
                          merchmap.put("store_name",lmMerchInfo.getStore_name());
                          merchmap.put("type",LmMerchInfo.parsetochinesetype(lmMerchInfo.getType()));
                          merchinfo.add(merchmap);
                      }
                   }
                   //查询是否有挂靠商户
                   List<LmMerchAdmin> adminlist = lmMerchInfoService.findAdminByMember(lmMember.getId());
                   if(adminlist!=null&&adminlist.size()>0){
                       for(LmMerchAdmin lmMerchAdmin:adminlist){
                           LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(lmMerchAdmin.getMerchid()+"");
                           Map<String,Object> merchmap = new HashMap<>();
                           merchmap.put("id",lmMerchAdmin.getMerchid());
                           merchmap.put("store_name",lmMerchInfo.getStore_name());
                           merchmap.put("type",LmMerchInfo.parsetochinesetype(lmMerchInfo.getType()));
                           merchinfo.add(merchmap);
                       }
                   }
                   map.put("merchlist",merchinfo);
               }else{
                   //查询是否有挂靠在商户下
                   List<LmMerchAdmin> adminlist = lmMerchInfoService.findAdminByMember(lmMember.getId());
                   if(adminlist!=null&&adminlist.size()>0){
                       for(LmMerchAdmin lmMerchAdmin:adminlist){
                           LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(lmMerchAdmin.getMerchid()+"");
                           Map<String,Object> merchmap = new HashMap<>();
                           merchmap.put("id",lmMerchAdmin.getMerchid());
                           merchmap.put("store_name",lmMerchInfo.getStore_name());
                           merchmap.put("type",LmMerchInfo.parsetochinesetype(lmMerchInfo.getType()));
                           merchinfo.add(merchmap);
                       }
                       map.put("merchlist",merchinfo);
                   }else{
                       map.put("merchlist",new ArrayList<>());
                   }
               }
           }
            jsonResult.setData(map);
        } catch (Exception e) {
            e.printStackTrace();
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return jsonResult;
    }


    /**
     * 用户基础信息修改
     * @return
     */
    @ApiOperation(value = "用户基础信息修改")
    @PostMapping("edit")
    public JsonResult edit(@ApiParam(name = "id", value = "用户id" , required = true )@RequestParam int id,
                           @ApiParam(name = "nickname", value = "昵称" )@RequestParam(required = false) String nickname,
                           @ApiParam(name = "gender", value = "性别1男2女")@RequestParam(required = false) String gender,
                           @ApiParam(name = "avatar", value = "头像") @RequestParam(required = false) String avatar,
                           @ApiParam(name = "mobile", value = "手机号码") @RequestParam(required = false) String mobile
                           ){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            LmMember lmMember = lmMemberService.findById(id+"");
            if(lmMember!=null){
                LmMemberLevel lmMemberLevel = lmMemberLevelService.findById(lmMember.getLevel_id()+"");
                if(!StringUtils.isEmpty(mobile)){
                    lmMember.setMobile(mobile);
                }
                if(!StringUtils.isEmpty(avatar)){
                    lmMember.setAvatar(avatar);
                }
                if(!StringUtils.isEmpty(gender)){
                    lmMember.setGender(gender);
                }
                if(!StringUtils.isEmpty(nickname)){
                    lmMember.setNickname(nickname);
                }
                lmMemberService.updateService(lmMember);
                HttpClient httpClient = new HttpClient();
                Map<String,Object> memberinfo= new HashMap<>();
                memberinfo.put("nickname",lmMember.getNickname());
                if(lmMemberLevel!=null){
                    memberinfo.put("levelname",lmMemberLevel.getName());
                    memberinfo.put("levelcode",lmMemberLevel.getCode());
                }else {
                    memberinfo.put("levelname","");
                    memberinfo.put("levelcode","");
                }
                httpClient.login(lmMember.getAvatar(),lmMember.getId()+"",new Gson().toJson(memberinfo));
            }
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }


    /**
     * 设置支付密码接口
     * @return
     */
    @ApiOperation(value = "设置支付密码接口")
    @PostMapping("/pay/{userid}")
    @Transactional
    public JsonResult pay(@ApiParam(name = "userid", value = "用户id", required = true)@PathVariable int userid,
                          HttpServletRequest request,
                          @ApiParam(name = "paypassword", value = "密码", required = true)@RequestParam(required = true) String paypassword,
                          @ApiParam(name = "name", value = "真实姓名", required = true)@RequestParam(required = true) String name,
                          @ApiParam(name = "idcard", value = "证件号码", required = true)@RequestParam(required = true) String idcard){

        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            LmMember lmMember = lmMemberService.findById(userid+"");
            if(lmMember!=null){
                lmMember.setPaypassword(paypassword);
                lmMember.setRealname(name);
                lmMember.setIdcard(idcard);
                lmMemberService.updateService(lmMember);
            }
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return jsonResult;
    }


    /**
     * 获取足迹列表
     * @return
     */
    @ApiOperation(value = "获取足迹列表")
    @PostMapping("/trace/{userid}")
    @Transactional
    public JsonResult trace( @ApiParam(name = "userid", value = "用户id", defaultValue = "0", required = true)@PathVariable int userid,HttpServletRequest request,
                             @ApiParam(name = "type", value = "类别1直播2商品",defaultValue = "1",required=true) @RequestParam(value = "type",required = true,defaultValue = "1") int type,
                             @ApiParam(name = "page", value = "页码",defaultValue = "1",required=true) @RequestParam(value = "page",required = true,defaultValue = "1") int page,
                            @ApiParam(name = "pagesize", value = "每页个数",defaultValue = "10",required=true) @RequestParam(value = "pagesize",required = true,defaultValue = "10") int pagesize
                            ){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            List<Map<String,Object>> list = lmMemberTraceService.findByMemberidAndType(userid,type);
            for(Map<String,Object> map :list){
               if(type==1){
                   String livetype  = map.get("type")+"";
                   if("0".equals(livetype)){
                       map.put("type","normal");
                   }
                   if("1".equals(livetype)){
                       map.put("type","share");
                   }
               }
            }
            jsonResult.setData(PageUtil.startPage(list,page,pagesize));
        } catch (Exception e) {
            e.printStackTrace();
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return jsonResult;
    }


    /**
     * 获取我的收藏信息列表
     * @return
     */
    @ApiOperation(value = "获取我的收藏信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户id", defaultValue = "0", required = true)
    })
    @PostMapping("/fav/{userid}")
    @Transactional
    public JsonResult fav(@PathVariable int userid,HttpServletRequest request,
                          @ApiParam(name = "type", value = "类别1商品2视频",defaultValue = "1",required=true) @RequestParam(value = "type",defaultValue = "1") int type,
                          @ApiParam(name = "page", value = "页码",defaultValue = "1",required=true) @RequestParam(value = "page",defaultValue = "1") int page,
                          @ApiParam(name = "pagesize", value = "每页个数",defaultValue = "10",required=true) @RequestParam(value = "pagesize",defaultValue = "10") int pagesize
    ){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        List<Map<String,Object>> list = new ArrayList<>();
        try {
            if(type==1){
                list = lmMemberFavService.findInfoByMemberidByApi(userid,type);
                for(Map<String,Object> returnlist1:list){
                    Integer id=(int)returnlist1.get("id");
                    Integer goodtype =(int)returnlist1.get("type");
                    int types =0;
                    if(1==goodtype) {
                        LmGoodAuction lmGoodAuction = lmGoodAuctionService.findnowPriceByGoodidByApi(id, types);
                        if (!isEmpty(lmGoodAuction)) {
                            returnlist1.put("goodprice", lmGoodAuction.getPrice());
                        } else {
                            returnlist1.put("goodprice", 0);
                        }
                    }
                }
            }else{
                list = lmMemberFavService.findInfoByMemberidByApi(userid,type);
            }
            jsonResult.setData(PageUtil.startPage(list,page,pagesize));
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return jsonResult;
    }

    /**
     * 收藏操作
     * @return
     */
    @ApiOperation(value = "收藏操作")
    @PostMapping("/fav/operate")
    @Transactional
    public JsonResult favoperation(
                            @ApiParam(name = "type", value = "收藏类型0是商品1是视频", required = true)@RequestParam int type,
                                 @ApiParam(name = "id", value = "商品/视频id", required = true)@RequestParam int id,
                                 @ApiParam(name = "userid", value = "用户id", required = true)@RequestParam int userid,
                                 HttpServletRequest request){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            LmMemberFav lmMemberFav =lmMemberFavService.findByMemberidAndTypeAndId(userid,type,id);
            if(lmMemberFav==null){
                lmMemberFav = new LmMemberFav();
                lmMemberFav.setCreate_time(new Date());
                lmMemberFav.setMember_id(userid);
                lmMemberFav.setState(0);
                if(type==0){
                    lmMemberFav.setGoods_id(id);
                   Good good = goodService.findById(id);
                    LmMemberFollow lmMemberFollow =lmMemberFollowService.findByMemberidAndTypeAndId(userid,1,good.getMer_id());
                    if(lmMemberFollow==null){
                        lmMemberFollow = new LmMemberFollow();
                        lmMemberFollow.setFollow_id(good.getMer_id());
                        lmMemberFollow.setFollow_type(1);
                        lmMemberFollow.setFollow_time(new Date());
                        lmMemberFollow.setMember_id(userid);
                        lmMemberFollow.setState(0);
                        lmMemberFollowService.addService(lmMemberFollow);
                    }
                }else{
                    lmMemberFav.setVideo_id(id);
                }
                lmMemberFavService.addService(lmMemberFav);
            }else{
                lmMemberFavService.deleteService(lmMemberFav.getId());
            }

        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return jsonResult;
    }

    /**
     * 获取关注列表
     * @return
     */
    @ApiOperation(value = "获取关注列表")
    @PostMapping("/follow/{userid}")
    @Transactional
    public JsonResult follow( @ApiParam(name = "userid", value = "用户id", required = true)@PathVariable int userid,HttpServletRequest request,
                             @ApiParam(name = "type", value = "关注类型0是直播1是店铺", required = true)@RequestParam String type,
                              @ApiParam(name = "page", value = "页码",defaultValue = "1",required=true) @RequestParam(value = "page",defaultValue = "1") int page,
                              @ApiParam(name = "pagesize", value = "每页个数",defaultValue = "10",required=true) @RequestParam(value = "pagesize",defaultValue = "10") int pagesize
    ){
        //0是直播 1是店铺
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            List<Map<String,Object>> returnlist = new ArrayList<>();
            List<Map<String,Object>> list = lmMemberFollowService.findByMemberidAndType(userid,type);
            if("1".equals(type)){
                for(Map<String,Object> map :list){
                    int merchid  = (int)map.get("id");
                    //根据商户id查询商品
                    List<Map<String,Object>> goodlist = goodService.findByMerchIdByApi(merchid);
                    map.put("goodlist",new Gson().toJson(goodlist));
                    returnlist.add(map);
                    returnlist = PageUtil.startPage(returnlist,page,pagesize);
                }
            }
            if("0".equals(type)){
                for(Map<String,Object> map :list){
                    int livetype  = (int)map.get("type");
                    if(livetype==0){
                        map.put("type","normal");
                    }
                    if(livetype==1){
                        map.put("type","share");
                    }
                }
                returnlist = PageUtil.startPage(list,page,pagesize);
            }
            jsonResult.setData(returnlist);
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return jsonResult;
    }

    /**
     * 关注操作
     * @return
     */
    @ApiOperation(value = "关注操作")
    @PostMapping("/follow/operate")
    @Transactional
    public JsonResult operation( @ApiParam(name = "type", value = "关注类型0是直播1是店铺", required = true)@RequestParam int type,
                                 @ApiParam(name = "id", value = "直播/店铺id", required = true)@RequestParam int id,
                                 @ApiParam(name = "userid", value = "用户id", required = true)@RequestParam int userid,
                                 HttpServletRequest request){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            LmMemberFollow lmMemberFollow =lmMemberFollowService.findByMemberidAndTypeAndId(userid,type,id);
            if(lmMemberFollow==null){
                if(type==1){
                    LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(id+"");
                    //获取店铺关注人数
                    int count= lmMerchInfo.getFocusnum();
                    lmMerchInfo.setFocusnum(count+1);
                    lmMerchInfoService.updateService(lmMerchInfo);
                }
                lmMemberFollow = new LmMemberFollow();
                lmMemberFollow.setFollow_id(id);
                lmMemberFollow.setFollow_type(type);
                lmMemberFollow.setFollow_time(new Date());
                lmMemberFollow.setMember_id(userid);
                lmMemberFollow.setState(0);
                lmMemberFollowService.addService(lmMemberFollow);
            }else{
                if(type==1){
                    LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(id+"");
                    //获取店铺关注人数
                    int count= lmMerchInfo.getFocusnum();
                    lmMerchInfo.setFocusnum(count-1);
                    lmMerchInfoService.updateService(lmMerchInfo);
                }
                lmMemberFollowService.deleteService(lmMemberFollow.getId());
            }

        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return jsonResult;
    }

    /**
     * 获取我的优惠券列表
     * @return
     */
    @ApiOperation(value = "获取我的优惠券列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户id", defaultValue = "0", required = true)
    })
    @PostMapping("/coupon/{userid}")
    public JsonResult coupon(@PathVariable int userid,HttpServletRequest request){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            List<Map<String,String>> list = lmMemberCouponService.findByMemberidByApi(userid);
            List<Map<String,Object>> returnlist = new ArrayList<>();
            String merchname ="";
            for(Map<String,String> map:list){
                Map<String,Object> returnmap = new HashMap<>();
                if(!merchname.equals(map.get("merchname"))){
                    List<Map<String,String>> infolist = new ArrayList<>();
                    for(Map<String,String> infomap:list){
                        if(infomap.get("merchname").equals(map.get("merchname"))){
                            infolist.add(infomap);
                        }
                    }
                    returnmap.put("merchname",map.get("merchname"));
                    returnmap.put("goodlist",infolist);
                    returnlist.add(returnmap);
                }
                merchname=map.get("merchname");
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
     * 参拍记录
     * @param request
     * @return
     */
    @ApiOperation(value = "参拍记录")
    @PostMapping("/auctionlog")
    public JsonResult auctionlog(HttpServletRequest request,
                               @ApiParam(name = "userid", value = "买家id",defaultValue = "1",required=true) @RequestParam(value = "userid",defaultValue = "1") int userid,
                                 @ApiParam(name = "type", value = "状态2中拍0被超越1领先",defaultValue = "2",required=true) @RequestParam(value = "type",defaultValue = "2") int type){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            //普通商品参拍记录
            List<Map<String,Object>> acutioninfolist=goodService.findAuctionlistByUseridAndType(userid,type);
            for(Map<String,Object> map:acutioninfolist){
                map.put("createtime",DateUtils.sdf_yMdHms.format(map.get("createtime")));
            }
            //直播商品参拍记录
            List<Map<String,Object>> acutioninfolist2=goodService.findAuctionlistByUseridAndType2(userid,type);
            for(Map<String,Object> map:acutioninfolist2){
                map.put("createtime",DateUtils.sdf_yMdHms.format(map.get("createtime")));
            }
            acutioninfolist.addAll(acutioninfolist2);
            Collections.sort(acutioninfolist, new Comparator<Map>() {
                public int compare(Map o1, Map o2) {
                    if ((int)o1.get("lgaid") > (int)o2.get("lgaid")) {
                        return -1;
                    }
                    if (o1.get("lgaid") == o2.get("lgaid")) {
                        return 0;
                    }
                    return 1;
                }
            });
            jsonResult.setData(acutioninfolist);
        } catch (Exception e) {
            e.printStackTrace();
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }

    /**
     * 猜你喜欢
     * @return
     */
    @ApiOperation(value = "猜你喜欢")
    @PostMapping("/like")
    @Transactional
    public JsonResult favoperation(HttpServletRequest request){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        List<Map> returnlist = new ArrayList<>();
        try {
            List<Map> goodlist = goodService.findHotList();
            for(Map hotLists:goodlist){
                Integer id =(int)hotLists.get("goodid");
                Integer ordertype =(int)hotLists.get("type");
                if(1==ordertype) {
                    int type = 0;
                    LmGoodAuction lmGoodAuction = lmGoodAuctionService.findnowPriceByGoodidByApi(id, type);
                    if (!isEmpty(lmGoodAuction)) {
                        hotLists.put("goodprice", lmGoodAuction.getPrice());
                    } else {
                        hotLists.put("goodprice", 0);
                    }
                }
            }
            if(goodlist!=null&&goodlist.size()>2){
                returnlist = getRandomList(goodlist,2);
            }
            jsonResult.setData(returnlist);
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return jsonResult;
    }

    public static List getRandomList(List paramList,int count){
        if(paramList.size()==0){
            return paramList;
        }
        Random random=new Random();
        List tempList=new ArrayList();
        List newList=new ArrayList();
        int temp=0;
        for(int i=0;i<count;i++){
            temp=random.nextInt(paramList.size());//将产生的随机数作为被抽list的索引
            if(!tempList.contains(temp)){
                tempList.add(temp);
                newList.add(paramList.get(temp));
            }
            else{
                i--;
            }

        }
        return newList;
    }

    /**
     * 我的钱包
     * @return
     */
    @ApiOperation(value = "我的钱包")
    @PostMapping("/mypackage")
    public JsonResult mypackage(HttpServletRequest request,
                                @ApiParam(name = "userid", value = "用户id", required = true)@RequestParam String userid){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        Map<String,Object> returnmap = new HashMap<>();
        try {
            LmMember lmMember = lmMemberService.findById(userid);
            returnmap.put("price",lmMember.getCredit2());
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }

    /**
     * 热门咨询列表
     * @return
     */
    @ApiOperation(value = "热门咨询列表")
    @PostMapping("/consultlist")
    public JsonResult consultlist(HttpServletRequest request){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        List<String> list ;
        try {
            list = consultService.findActiveList();
            jsonResult.setData(list);
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }

    /**
     * 热门咨询
     * @return
     */
    @ApiOperation(value = "热门咨询")
    @PostMapping("/consult")
    public JsonResult consult(HttpServletRequest request,
                                @ApiParam(name = "title", value = "标题", required = true)@RequestParam String title){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        String content = "";
        try {
            content = consultService.findInfoByTitle(title);
            jsonResult.setData(content);
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }



}


