package com.wink.livemall.admin.api.good;

import com.google.gson.Gson;
import com.wink.livemall.admin.util.DateUtils;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.PageUtil;
import com.wink.livemall.coupon.dto.LmCoupons;
import com.wink.livemall.coupon.service.LmCouponsService;
import com.wink.livemall.goods.dto.Good;
import com.wink.livemall.goods.dto.GoodCategory;
import com.wink.livemall.goods.dto.LmGoodAuction;
import com.wink.livemall.goods.dto.LmGoodMaterial;
import com.wink.livemall.goods.service.GoodCategoryService;
import com.wink.livemall.goods.service.GoodService;
import com.wink.livemall.goods.service.LmGoodAuctionService;
import com.wink.livemall.live.dto.LmLive;
import com.wink.livemall.live.service.LmLiveService;
import com.wink.livemall.member.dto.LmMember;
import com.wink.livemall.member.dto.LmMemberFav;
import com.wink.livemall.member.dto.LmMemberFollow;
import com.wink.livemall.member.dto.LmMemberTrace;
import com.wink.livemall.member.service.LmMemberFavService;
import com.wink.livemall.member.service.LmMemberFollowService;
import com.wink.livemall.member.service.LmMemberTraceService;
import com.wink.livemall.merch.dto.LmMerchInfo;
import com.wink.livemall.merch.service.LmMerchInfoService;
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

import static org.springframework.util.StringUtils.isEmpty;

@Api(tags = "商品信息接口")
@RestController
@RequestMapping("good")
public class GoodController {
    Logger logger = LogManager.getLogger(GoodController.class);
    @Autowired
    private RedisUtil redisUtils;
    @Autowired
    private GoodCategoryService goodCategoryService;
    @Autowired
    private GoodService goodService;
    @Autowired
    private LmMemberFollowService lmMemberFollowService;
    @Autowired
    private LmMemberFavService lmMemberFavService;
    @Autowired
    private LmMerchInfoService lmMerchInfoService;
    @Autowired
    private LmLiveService lmLiveService;
    @Autowired
    private LmCouponsService lmCouponsService;
    @Autowired
    private LmMemberTraceService lmMemberTraceService;
    @Autowired
    private LmGoodAuctionService lmGoodAuctionService;

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
            List<Map<String,String>> list = goodCategoryService.findtopgoodscategorybyapi();
            jsonResult.setData(list);
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }

    /**
     * 根据顶级分类获取子分类
     * @param request
     * @return
     */
    @ApiOperation(value = "根据父分类获取子分类")
    @PostMapping("/childcategory/{pid}")
    public JsonResult getcategorygoodlist(@ApiParam(name = "pid", value = "分类id", required = true,defaultValue = "2")@PathVariable String pid, HttpServletRequest request){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            //如果pid=0 那么就是查询推荐子分类
            if("0".equals(pid)){
                List<Map<String,String>> list = goodCategoryService.findrecommendByapi();
                jsonResult.setData(list);
            }else{
                List<Map<String,String>> list = goodCategoryService.findByPidByapi(pid);
                jsonResult.setData(list);
            }
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }

    /**
     * 根据分类id获取商品信息
     * @param request
     * @return
     */
    @ApiOperation(value = "根据分类id获取商品信息")
    @PostMapping("/list/{categoryid}")
    public JsonResult getgoodlist(@ApiParam(name = "categoryid", value = "分类id",defaultValue = "30")@PathVariable String categoryid,HttpServletRequest request,
                                  @ApiParam(name = "pid", value = "父类id",defaultValue = "",required=false) @RequestParam(value = "pid",required = false) String pid,
                                  @ApiParam(name = "goodname", value = "商品名称",defaultValue = "",required=false) @RequestParam(value = "goodname",required = false) String goodname,
                                  @ApiParam(name = "topprice", value = "最高价格",defaultValue = "",required=false ) @RequestParam(value = "topprice",required = false) String topprice,
                                  @ApiParam(name = "lowprice", value = "最低价格" ,defaultValue = "",required=false) @RequestParam(value = "lowprice",required = false) String lowprice,
                                  @ApiParam(name = "type", value = "列表区分一口价拍卖",defaultValue = "" ,required=false) @RequestParam(value = "type",required = false) String type,
                                  @ApiParam(name = "isauction", value = "滴雨轩拍卖行",defaultValue = "",required=false) @RequestParam(value = "isauction",required = false) String isauction,
                                  @ApiParam(name = "isdirect", value = "直营店",defaultValue = "",required=false) @RequestParam(value = "isdirect",required = false) String isdirect,
                                  @ApiParam(name = "isquality", value = "优选好店",defaultValue = "",required=false) @RequestParam(value = "isquality",required = false) String isquality,
                                  @ApiParam(name = "postage", value = "包邮",defaultValue = "",required=false) @RequestParam(value = "postage",required = false) String postage,
                                  @ApiParam(name = "refund", value = "包退",defaultValue = "",required=false) @RequestParam(value = "refund",required = false) String refund,
                                  @ApiParam(name = "isoem", value = "代工工作室",defaultValue = "",required=false) @RequestParam(value = "isoem",required = false) String isoem,
                                  @ApiParam(name = "material", value = "材质",defaultValue = "",required=false) @RequestParam(value = "material",required = false) String material,
                                  @ApiParam(name = "sorttype", value = "排序类型1价格2时间",defaultValue = "1",required=false) @RequestParam(value = "sorttype",required = false) String sorttype,
                                  @ApiParam(name = "page", value = "页码",defaultValue = "1",required=false) @RequestParam(value = "page",required = false,defaultValue = "1") int page,
                                  @ApiParam(name = "pagesize", value = "每页个数",defaultValue = "10",required=false) @RequestParam(value = "pagesize",required = false,defaultValue = "10") int pagesize,
                                  @ApiParam(name = "sortway", value = "排序方式up升/down 降",defaultValue = "up",required=false) @RequestParam(value = "sortway",required = false) String sortway
    ){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        List<Map> list = new ArrayList<>();
        try {
            Map<String,Object> map =lmLiveService.finddirectlyinfoByApi();
            if(map!=null){
                map.put("showtype","live");
                list.add(map);
            }
            List<Map> goodlist =  goodService.findInfoByApi(categoryid,goodname,topprice,lowprice,isauction,isdirect,isquality,postage,refund,isoem,material,sorttype,sortway,type,pid);
            for(Map mapinfo:goodlist) {
                Integer id = (int) mapinfo.get("goodid");
                Integer ordertype = (int) mapinfo.get("type");
                if (1 == ordertype) {
                    int types = 0;
                    LmGoodAuction lmGoodAuction = lmGoodAuctionService.findnowPriceByGoodidByApi(id, types);
                    if (!isEmpty(lmGoodAuction)) {
                        mapinfo.put("price", lmGoodAuction.getPrice());
                    } else {
                        mapinfo.put("price", 0);
                    }
                    mapinfo.put("showtype", "good");
                }
            }
            if(goodlist!=null){
                list.addAll(goodlist);
            }
            if(!StringUtils.isEmpty(page)&&!StringUtils.isEmpty(pagesize)){
                jsonResult.setData(PageUtil.startPage(list,page,pagesize));
            }else{
                jsonResult.setData(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }


    /**
     * 获取商品详细信息
     * @param request
     * @return
     */
    @ApiOperation(value = "获取商品详细信息")
    @PostMapping("/detail/{goodid}")
    @Transactional
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "String", name = "Authorization", value = "token标记", required = false) })
    public JsonResult detail(@ApiParam(name = "goodid", value = "商品id",defaultValue = "3", required = true)@PathVariable String goodid,HttpServletRequest request){
        String header = request.getHeader("Authorization");
        String userid = "";
        if (StringUtils.isEmpty(header)) {

        }else{
            userid = redisUtils.get(header)+"";
        }
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            Map<String,Object> map = new HashMap<>();
            Good good = goodService.findById(Integer.parseInt(goodid));

            if(good!=null){
                map.put("type",good.getType());
                map.put("name",good.getTitle());
                map.put("price",good.getProductprice());
                map.put("bidsnum",good.getBidsnum());
                map.put("spec",good.getSpec());
                map.put("weight",good.getWeight());
                map.put("material",good.getMaterial());
                if(good.getAuction_end_time()!=null){
                    map.put("endtime",DateUtils.sdf_yMdHms.format(good.getAuction_end_time()));
                }
                if(good.getAuction_start_time()!=null){
                    map.put("starttime",DateUtils.sdf_yMdHms.format(good.getAuction_start_time()));
                }
                GoodCategory goodCategory = goodCategoryService.findgoodscategoryById(good.getCategory_id()+"");
                if(goodCategory!=null){
                    map.put("category",goodCategory.getName());
                }
               /*if(good.getThumbs().contains(",")){
                    String[] imgs = good.getThumbs().split(",");
                    *//*map.put("goodimg",imgs[0]);*//*
                    map.put("goodimg",imgs);
                }else{
                    map.put("goodimg",good.getThumbs());
                }*/
                map.put("thumbimg",good.getThumb());
                map.put("goodimg",good.getThumbs());
                map.put("state",good.getState());
                map.put("productprice",good.getProductprice());
                map.put("description",good.getDescription());
                LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(good.getMer_id()+"");
                List<Map<String,Object>> list = goodService.findAuctionlistByGoodid(good.getId(),0);

                if(good.getType()==1){
                    for(Map<String,Object> mapinfo:list){
                        mapinfo.put("createtime",DateUtils.sdf_yMdHms.format(mapinfo.get("createtime")));
                    }
                    map.put("startprice",good.getStartprice());
                    map.put("stepprice",good.getStepprice());
                    map.put("marketprice",good.getMarketprice());
                    map.put("auctionlist",list);

                    long newdatetime = System.currentTimeMillis();
                    if(good.getAuction_start_time()!=null&&good.getAuction_end_time()!=null){
                        if(newdatetime>=good.getAuction_start_time().getTime()&&newdatetime<=(good.getAuction_end_time().getTime())){
                            long nd = 1000 * 24 * 60 * 60;
                            long nh = 1000 * 60 * 60;
                            long nm = 1000 * 60;
                            // long ns = 1000;
                            // 获得两个时间的毫秒时间差异
                            long diff = good.getAuction_end_time().getTime()-newdatetime;
                            // 计算差多少天
                            long day = diff / nd;
                            // 计算差多少小时
                            long hour = diff % nd / nh;
                            // 计算差多少分钟
                            long min = diff % nd % nh / nm;
                            // 计算差多少秒//输出结果
                            // long sec = diff % nd % nh % nm / ns;
                            map.put("resttime",  day + "天" + hour + "小时" + min + "分钟");
                            map.put("resttimes",diff);
                        }
                    }

                }
                if(!StringUtils.isEmpty(userid)&&!"null".equals(userid)){

                    LmMemberFav lmMemberFav = lmMemberFavService.findByMemberidAndGoodid(userid,goodid);

                    if(lmMemberFav!=null){
                        map.put("favid",lmMemberFav.getId());
                        map.put("isfav","yes");
                    }else{
                        map.put("isfav","no");
                    }
                }
                if(lmMerchInfo!=null){
                    map.put("imid",lmMerchInfo.getMember_id());
                    map.put("merchid",lmMerchInfo.getId());
                    map.put("avatar",lmMerchInfo.getAvatar());
                    map.put("isauction",lmMerchInfo.getIsauction());
                    map.put("isdirect",lmMerchInfo.getIsdirect());
                    map.put("isquality",lmMerchInfo.getIsquality());
                    map.put("postage",lmMerchInfo.getPostage());
                    map.put("refund",lmMerchInfo.getRefund());
                    map.put("isoem",lmMerchInfo.getIsoem());
                    map.put("storename",lmMerchInfo.getStore_name());
                    Map<String,Object> condient = new HashMap<>();
                    condient.put("datetime",DateUtils.sdf_yMdHms.format(new Date()));
                    condient.put("merchid",lmMerchInfo.getId());
                    /*List<Map<String, Object> > lmCouponsList = lmCouponsService.findByCondient(condient);
                    map.put("lmCouponsList", new Gson().toJson(lmCouponsList));*/
                    if(!StringUtils.isEmpty(userid)&&!"null".equals(userid)) {
                        //添加浏览记录
                        LmMemberTrace lmMemberTrace = lmMemberTraceService.findByMemberidAndGoodid(Integer.parseInt(userid),good.getId());
                        if(lmMemberTrace==null){
                            lmMemberTrace = new LmMemberTrace();
                            lmMemberTrace.setMember_id(Integer.parseInt(userid));
                            lmMemberTrace.setTrace_id(good.getId());
                            lmMemberTrace.setTrace_time(new Date());
                            lmMemberTrace.setTrace_type(2);
                            lmMemberTraceService.insertService(lmMemberTrace);
                        }else{
                            lmMemberTrace.setMember_id(Integer.parseInt(userid));
                            lmMemberTrace.setTrace_id(good.getId());
                            lmMemberTrace.setTrace_time(new Date());
                            lmMemberTrace.setTrace_type(2);
                            lmMemberTraceService.updateService(lmMemberTrace);
                        }
                        LmMemberFollow lmMemberFollow = lmMemberFollowService.findByMemberidAndMerchId(userid, lmMerchInfo.getId());
                        if (lmMemberFollow != null) {
                            map.put("followid",lmMemberFollow.getId());
                            map.put("isfollow", "yes");
                        } else {
                            map.put("isfollow", "no");
                        }
                    }
                }
            }
            jsonResult.setData(map);
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
     * 商户精选商品
     * @return
     */
    @ApiOperation(value = "商户精选商品")
    @PostMapping("/merchants/{merchid}")
    public JsonResult merchants(@ApiParam(name = "merchid", value = "商户id",defaultValue = "37", required = true) @PathVariable int merchid,
                                @ApiParam(name = "page", value = "页码",defaultValue = "1",required=false) @RequestParam(value = "page",required = false,defaultValue = "1") int page,
                                @ApiParam(name = "pagesize", value = "每页个数",defaultValue = "10",required=false) @RequestParam(value = "pagesize",required = false,defaultValue = "10") int pagesize,
                                HttpServletRequest request){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            List<Map<String,Object>> returnlist = goodService.findhotByMerchIdByApi(merchid);
            for(Map<String,Object> returnlist1:returnlist){
                Integer id=(int)returnlist1.get("goodid");
                Integer goodtype =(int)returnlist1.get("type");
                int type =0;
                if(1==goodtype) {
                    LmGoodAuction lmGoodAuction = lmGoodAuctionService.findnowPriceByGoodidByApi(id, type);
                    if (!isEmpty(lmGoodAuction)) {
                        returnlist1.put("goodprice", lmGoodAuction.getPrice());
                    } else {
                        returnlist1.put("goodprice", 0);
                    }
                }
            }
            jsonResult.setData(PageUtil.startPage(returnlist,page,pagesize));
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }


    /**
     * 获取商品材质
     * @param request
     * @return
     */
    @ApiOperation(value = "获取商品材质")
    @PostMapping("/material")
    public JsonResult material(HttpServletRequest request){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            List<LmGoodMaterial> list = goodService.getGoodMaterialByApi();
            jsonResult.setData(list);
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }

    /**
     * 获取最新价格
     * @param request
     * @return
     */
    @ApiOperation(value = "获取最新价格")
    @PostMapping("/newprice")
    public JsonResult newprice(HttpServletRequest request,
                               @ApiParam(name = "goodid", value = "商品id",defaultValue = "1",required=true) @RequestParam(value = "goodid",defaultValue = "1") int goodid){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        Map<String,Object> map = new HashMap<>();
        try {
            Good good = goodService.findById(goodid);
            List<Map<String,Object>> list = goodService.findAuctionlistByGoodid(goodid,0);
            if(list!=null&&list.size()>0){
                for(Map<String,Object> mapinfo:list){
                    mapinfo.put("createtime",DateUtils.sdf_yMdHms.format(mapinfo.get("createtime")));
                }
                Map<String,Object>lmGoodAuction = list.get(0);
                map.put("newprice",lmGoodAuction.get("price"));
                map.put("auctionlist",list);


            }else{
                map.put("newprice",good.getStartprice());
                map.put("auctionlist",new ArrayList<>());
            }
            if(good.getAuction_end_time().getTime()-System.currentTimeMillis()>0){
                map.put("resttimes",good.getAuction_end_time().getTime()-System.currentTimeMillis());
            }else{
                map.put("resttimes",0);
            }
            jsonResult.setData(map);
        } catch (Exception e) {
            e.printStackTrace();
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
            if(!StringUtils.isEmpty(redisUtils.get(header))){
                userid = redisUtils.get(header)+"";
            }else{
                jsonResult.setCode(JsonResult.LOGIN);
                return jsonResult;
            }
        }
        try {
            //查看商品是否下架
            Good good = goodService.findById(goodid);
            if(good.getState()==0&&(good.getAuction_end_time().getTime())<System.currentTimeMillis()){
                
                jsonResult.setCode(JsonResult.ERROR);
                jsonResult.setMsg("商品竞拍结束无法出价");
                return jsonResult;
            }
            String redisKey = "paycount:" + goodid;
            boolean flag = redisUtils.getLock(redisKey);
            if (flag) {
                // 执行逻辑操作
                   List<LmGoodAuction> list = goodService.findAuctionlistByGoodid2(goodid,0);
                   if(list!=null&&list.size()>0){
                       LmGoodAuction lmGoodAuction = list.get(0);
                       int compare = lmGoodAuction.getPrice().compareTo(new BigDecimal(price));
                       if(compare > -1){
                           jsonResult.setData(lmGoodAuction.getPrice());
                           jsonResult.setCode(JsonResult.ERROR);
                           jsonResult.setMsg("已有人出价更高");
                           return jsonResult;
                       }
                       LmGoodAuction newauction =new LmGoodAuction();
                       newauction.setCreatetime(new Date());
                       newauction.setGoodid(goodid);
                       newauction.setType(0);
                       newauction.setMemberid(Integer.parseInt(userid));
                       newauction.setPrice(new BigDecimal(price));
                       newauction.setStatus("1");
                       goodService.insertAuctionService(newauction);
                       for(LmGoodAuction old:list){
                           old.setStatus("0");
                           goodService.updateAuctionService(old);
                       }
                   }else{
                       LmGoodAuction lmGoodAuction =new LmGoodAuction();
                       lmGoodAuction.setCreatetime(new Date());
                       lmGoodAuction.setGoodid(goodid);
                       lmGoodAuction.setMemberid(Integer.parseInt(userid));
                       lmGoodAuction.setPrice(new BigDecimal(price));
                       lmGoodAuction.setStatus("1");
                       lmGoodAuction.setType(0);
                       goodService.insertAuctionService(lmGoodAuction);
                   }
                   good.setBidsnum(good.getBidsnum()+1);
                   if((good.getAuction_end_time().getTime()-System.currentTimeMillis())<=good.getDelaytime()*1000){
                       Date endtime = new Date(good.getAuction_end_time().getTime()+good.getDelaytime()*1000);
                       good.setAuction_end_time(endtime);
                   }
                   goodService.updateGoods(good);
                   jsonResult.setData(list);
                   redisUtils.delete(redisKey);
            }else{
            	jsonResult.setCode(JsonResult.ERROR);
                jsonResult.setMsg("当前出价人数过多，请稍后再试");
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
}
