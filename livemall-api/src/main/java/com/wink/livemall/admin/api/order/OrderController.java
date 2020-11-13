package com.wink.livemall.admin.api.order;

import com.google.gson.Gson;
import com.wink.livemall.admin.api.help.HelpController;
import com.wink.livemall.admin.api.shop.MerchController;
import com.wink.livemall.admin.util.*;
import com.wink.livemall.admin.util.httpclient.HttpClient;
import com.wink.livemall.coupon.dto.LmCouponLog;
import com.wink.livemall.coupon.dto.LmCoupons;
import com.wink.livemall.coupon.service.LmCouponsService;
import com.wink.livemall.goods.dto.Good;
import com.wink.livemall.goods.dto.LivedGood;
import com.wink.livemall.goods.dto.LmGoodAuction;
import com.wink.livemall.goods.dto.LmShareGood;
import com.wink.livemall.goods.service.GoodService;
import com.wink.livemall.live.dto.LmLiveGood;
import com.wink.livemall.live.service.LmLiveGoodService;
import com.wink.livemall.member.dto.LmMember;
import com.wink.livemall.member.dto.LmMemberAddress;
import com.wink.livemall.member.dto.LmMemberLevel;
import com.wink.livemall.member.service.LmMemberAddressService;
import com.wink.livemall.member.service.LmMemberLevelService;
import com.wink.livemall.member.service.LmMemberService;
import com.wink.livemall.merch.dto.LmMerchInfo;
import com.wink.livemall.merch.service.LmMerchInfoService;
import com.wink.livemall.order.dao.LmOrderRefundLogDao;
import com.wink.livemall.order.dto.*;
import com.wink.livemall.order.service.*;
import com.wink.livemall.sys.dict.dto.LmSysDict;
import com.wink.livemall.sys.dict.dto.LmSysDictItem;
import com.wink.livemall.sys.dict.service.DictService;
import com.wink.livemall.sys.dict.service.Dict_itemService;
import com.wink.livemall.sys.msg.service.PushmsgService;
import com.wink.livemall.sys.setting.dto.Configs;
import com.wink.livemall.sys.setting.service.ConfigsService;
import com.wink.livemall.utils.cache.redis.RedisUtil;
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

@Api(tags = "订单信息接口")
@RestController
@RequestMapping("/order")
public class OrderController {
	private static final Logger logger = LogManager.getLogger(OrderController.class);
    @Autowired
    private LmOrderCommentService lmOrderCommentService;
    @Autowired
    private PushmsgService pushmsgService;
    @Autowired
    private GoodService goodService;
    @Autowired
    private LmMemberService lmMemberService;
    @Autowired
    private LmOrderService lmOrderService;
    @Autowired
    private LmOrderGoodsService lmOrderGoodsService;
    @Autowired
    private LmCouponsService lmCouponsService;
    @Autowired
    private LmMemberAddressService lmMemberAddressService;
    @Autowired
    private LmMerchInfoService lmMerchInfoService;
    @Autowired
    private ConfigsService configsService;
    @Autowired
    private RedisUtil redisUtils;
    @Autowired
    private Dict_itemService dict_itemService;
    @Autowired
    private LmMemberLevelService lmMemberLevelService;
    @Autowired
    private LmOrderLogService lmOrderLogService;
    @Autowired
    private LmMerchOrderService lmMerchOrderService;
    @Autowired
    private LmLiveGoodService lmLiveGoodService;

    @ApiOperation(value = "获取订单列表")
    @PostMapping("/list")
    public JsonResult getorderlist(HttpServletRequest request,
                                   @ApiParam(name = "status", value = "订单状态0未支付1待发货2待收货3待评价4已完成5已关闭-1交易失败-2全部6售后", required = true)@RequestParam String status,
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
                    jsonResult.setCode(JsonResult.LOGIN);
                    return jsonResult;
                }
            }
            //获取订单基本信息
            List<Map<String,Object>> list = lmOrderService.findOrderList(status,Integer.parseInt(userid));
            for(Map<String,Object> map:list){
                System.out.println(map.toString());
                if(map.get("type")!=null){
                    if((int)map.get("type")==3){
                        //合买
                        LmShareGood lmShareGood = goodService.findshareById((int)map.get("goodid"));
                        map.put("goodname",lmShareGood.getName());
                        map.put("thumb",lmShareGood.getImg());
                        map.put("spec","");
                    }else{
                        if(map.get("islivegood")!=null&&(int)map.get("islivegood")==1){
                            //直播商品
                            LivedGood good = goodService.findLivedGood((int)map.get("goodid"));
                            map.put("goodname",good.getName());
                            map.put("thumb",good.getImg());
                            map.put("spec","");
                        }else{
                            //普通订单
                            Good good = goodService.findById((int)map.get("goodid"));
                            if(good!=null){
                                map.put("goodname",good.getTitle());
                                map.put("thumb",good.getThumb());
                                map.put("spec",good.getSpec());
                            }
                        }
                    }
                }
                if(map.get("backstatus")!=null){
                    if((int)map.get("backstatus")!=0){
                        LmOrderRefundLog lmOrderRefundLog = lmOrderService.findRefundLogById((int)map.get("refundid"));
                        if(lmOrderRefundLog!=null){
                            map.put("reason",lmOrderRefundLog.getReason());
                        }
                    }else{
                        map.put("reason","");
                    }
                }
            }

            jsonResult.setData(PageUtil.startPage(list,page,pagesize));
        } catch (Exception e) {
            e.printStackTrace();
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }

    /**
     * 提交订单
     * @return
     */
    @ApiOperation(value = "提交订单")
    @PostMapping("/submit")
    @Transactional
    public JsonResult submit(HttpServletRequest request,
                             @ApiParam(name = "merchid", value = "商户id", required = true)@RequestParam int merchid,
                             @ApiParam(name = "goodid", value = "商品id", required = true)@RequestParam int goodid,
                             @ApiParam(name = "price", value = "价格", required = true)@RequestParam String price,
                             @ApiParam(name = "addressid", value = "地址id", required = true)@RequestParam int addressid,
                             @ApiParam(name = "couponid", value = "优惠券id", required = false)@RequestParam(required = false) String couponid,
                             @ApiParam(name = "num", value = "数量", required = true)@RequestParam String num){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
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
            LmMemberAddress address = lmMemberAddressService.findById(addressid+"");
            Integer maxId = lmOrderService.findMaxId();
            if(maxId==null){
                maxId = 0;
            }
            String datetime = DateUtils.sdfyMdHm.format(new Date());
            Configs configs =configsService.findByTypeId(Configs.type_pay);
            Map map =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
            String prfix=map.get("msgSrcId")+"";
            LmCoupons lmCoupons= null;
            if(!StringUtils.isEmpty(couponid)){
                lmCoupons = lmCouponsService.findById(couponid);
            }
            Good good = goodService.findById(goodid);
            if(address==null){
                return new JsonResult(JsonResult.ERROR,"参数错误");
            }
            LmMember lmMember = lmMemberService.findById(userid+"");
            if(lmMember==null){
                return new JsonResult(JsonResult.ERROR,"参数错误");
            }
            if(good==null){
                return new JsonResult(JsonResult.ERROR,"参数错误");
            }
            if(good.getStock()<Integer.parseInt(num)){
                return new JsonResult(JsonResult.ERROR,"库存不足");
            }
            if(lmCoupons!=null){
                Date date = new Date();
                if(date.getTime()>=lmCoupons.getStart_date().getTime()&&date.getTime()<=lmCoupons.getEnd_date().getTime()){
                    if(lmCoupons.getLeft_num()>0){
                        //扣除优惠券数量
                        lmCoupons.setUse_num(lmCoupons.getUse_num()+1);
                        lmCoupons.setLeft_num(lmCoupons.getLeft_num()-1);
                        lmCouponsService.updateService(lmCoupons);
                        LmOrder lmOrder = new LmOrder();
                        lmOrder.setStatus("0");
                        lmOrder.setOrderid(prfix+datetime+maxId);
                        lmOrder.setChargeaddress(address.getProvince()+address.getCity()+address.getDistrict()+address.getAddress_info());
                        lmOrder.setPayexpressprice(good.getExpressprice());
                        lmOrder.setChargename(address.getRealname());
                        lmOrder.setChargephone(address.getMobile());
                        lmOrder.setType(1);
                        lmOrder.setPaynickname(lmMember.getNickname());
                        lmOrder.setCreatetime(new Date());
                        lmOrder.setMerchid(merchid);
                        lmOrder.setMemberid(Integer.parseInt(userid));
                        if(good.getFreeshipping()==1){
                            lmOrder.setRealexpressprice(new BigDecimal(0));
                            lmOrder.setRealpayprice(new BigDecimal(price));
                        }else{
                            lmOrder.setRealexpressprice(good.getExpressprice());
                            lmOrder.setRealpayprice(new BigDecimal(price).add(good.getExpressprice()));
                        }
                        lmOrder.setTotalprice(new BigDecimal(price));
                        lmOrderService.insertService(lmOrder);
                        LmOrderGoods lmOrderGoods = new LmOrderGoods();
                        lmOrderGoods.setGoodid(goodid);
                        lmOrderGoods.setGoodnum(Integer.parseInt(num));
                        lmOrderGoods.setGoodprice(good.getProductprice());
                        lmOrderGoods.setOrderid(lmOrder.getId());
                        lmOrderGoodsService.insertService(lmOrderGoods);

                        LmCouponLog lmCouponLog = new LmCouponLog();
                        lmCouponLog.setCouponid(Integer.parseInt(couponid));
                        lmCouponLog.setCreatetime(new Date());
                        lmCouponLog.setMemberid(Integer.parseInt(userid));
                        lmCouponLog.setOrderid(lmOrder.getId());
                        lmCouponLog.setPrice(lmOrder.getTotalprice());
                        lmCouponsService.insertLogService(lmCouponLog);
                        jsonResult.setData(lmOrder.getOrderid());

                        LmOrderLog lmOrderLog = new LmOrderLog();
                        lmOrderLog.setOrderid(lmOrder.getOrderid());
                        lmOrderLog.setOperatedate(new Date());
                        lmOrderLog.setOperate("订单创建");
                        lmOrderLogService.insert(lmOrderLog);
                        //设置库存
                        if(good.getStock()>0){
                            int newstock = good.getStock()-Integer.parseInt(num);
                            good.setStock(newstock);
                            if(newstock==0){
                                good.setState(0);
                            }
                        }
                        goodService.updateGoods(good);

                    }else{
                        jsonResult.setCode("401");
                        jsonResult.setMsg("优惠券失效请重新下单");
                    }
                }else{
                    jsonResult.setCode("401");
                    jsonResult.setMsg("优惠券失效请重新下单");
                }
            }else{
                LmOrder lmOrder = new LmOrder();
                lmOrder.setOrderid(prfix+datetime+maxId);
                lmOrder.setStatus("0");
                lmOrder.setType(1);
                lmOrder.setPaynickname(lmMember.getNickname());
                lmOrder.setCreatetime(new Date());
                lmOrder.setMerchid(merchid);
                lmOrder.setChargeaddress(address.getProvince()+address.getCity()+address.getDistrict()+address.getAddress_info());
                lmOrder.setChargename(address.getRealname());
                lmOrder.setChargephone(address.getMobile());
                lmOrder.setMemberid(Integer.parseInt(userid));
                lmOrder.setTotalprice(new BigDecimal(price));
                if(good.getFreeshipping()==1){
                    lmOrder.setRealexpressprice(new BigDecimal(0));
                    lmOrder.setRealpayprice(new BigDecimal(price));
                }else{
                    lmOrder.setRealexpressprice(good.getExpressprice());
                    lmOrder.setRealpayprice(new BigDecimal(price).add(good.getExpressprice()));
                }
                lmOrderService.insertService(lmOrder);
                LmOrderGoods lmOrderGoods = new LmOrderGoods();
                lmOrderGoods.setGoodid(goodid);
                lmOrderGoods.setGoodnum(Integer.parseInt(num));
                lmOrderGoods.setGoodprice(good.getProductprice());
                lmOrderGoods.setOrderid(lmOrder.getId());
                lmOrderGoodsService.insertService(lmOrderGoods);

                LmOrderLog lmOrderLog = new LmOrderLog();
                lmOrderLog.setOrderid(lmOrder.getOrderid());
                lmOrderLog.setOperatedate(new Date());
                lmOrderLog.setOperate("订单创建");
                lmOrderLogService.insert(lmOrderLog);
                jsonResult.setData(lmOrder.getOrderid());
                //设置库存
                if(good.getStock()>0){
                    int newstock = good.getStock()-Integer.parseInt(num);
                    good.setStock(newstock);
                    if(newstock==0){
                        good.setState(0);
                    }

                }
                goodService.updateGoods(good);
            }
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
     * 支付设置订单状态
     * 废弃
     * @param id
     * @return
     */
    @ApiOperation(value = "支付设置订单状态")
    @PostMapping("/pay")
    public JsonResult pay(HttpServletRequest request,
                          @ApiParam(name = "id", value = "订单id", required = true)@RequestParam String id,
                          @ApiParam(name = "price", value = "支付金额", required = true)@RequestParam String price
    ){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            String header = request.getHeader("Authorization");
            if (!StringUtils.isEmpty(header)) {
                if(StringUtils.isEmpty(redisUtils.get(header))){
                    jsonResult.setCode(JsonResult.LOGIN);
                    return jsonResult;
                }
            }
            LmOrder lmOrder = lmOrderService.findById(id);
            LmOrderGoods lmOrderGoods = lmOrderGoodsService.findByOrderid(lmOrder.getId());
            if(lmOrder!=null&&lmOrderGoods!=null){
                Good good = goodService.findById(lmOrderGoods.getGoodid());
                if(good!=null){
                    if(good.getType()==1){
                        if(lmOrder.getDeposit_type()==1){
                            lmOrder.setRemain_money(new BigDecimal(price));
                            lmOrder.setStatus("1");
                            lmOrder.setPaytime(new Date());
                            lmOrder.setRemian_time(new Date());

                            LmOrderLog lmOrderLog = new LmOrderLog();
                            lmOrderLog.setOrderid(lmOrder.getOrderid());
                            lmOrderLog.setOperatedate(new Date());
                            lmOrderLog.setOperate("订单支付尾款");
                            lmOrderLogService.insert(lmOrderLog);

                        }else{
                            lmOrder.setPrepay_money(new BigDecimal(price));
                            lmOrder.setPrepay_time(new Date());
                            lmOrder.setDeposit_type(1);

                            LmOrderLog lmOrderLog = new LmOrderLog();
                            lmOrderLog.setOrderid(lmOrder.getOrderid());
                            lmOrderLog.setOperatedate(new Date());
                            lmOrderLog.setOperate("订单支付定金");
                            lmOrderLogService.insert(lmOrderLog);

                        }
                    }else if(good.getType()==2){
                        lmOrder.setStatus("1");
                        lmOrder.setPaytime(new Date());

                        LmOrderLog lmOrderLog = new LmOrderLog();
                        lmOrderLog.setOrderid(lmOrder.getOrderid());
                        lmOrderLog.setOperatedate(new Date());
                        lmOrderLog.setOperate("订单支付");
                        lmOrderLogService.insert(lmOrderLog);

                    }
                    lmOrderService.updateService(lmOrder);
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
     * 确认收货
     * @param id
     * @return
     */
    @ApiOperation(value = "确认收货")
    @PostMapping("/delivery")
    public JsonResult delivery(HttpServletRequest request,
                               @ApiParam(name = "id", value = "订单id", required = true)@RequestParam String id
    ){
    	
    	logger.info("开始确认收货............orderId:"+id);
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            String header = request.getHeader("Authorization");
            if (!StringUtils.isEmpty(header)) {
                if(StringUtils.isEmpty(redisUtils.get(header))){
                    jsonResult.setCode(JsonResult.LOGIN);
                    return jsonResult;
                }
            }
            LmOrder lmOrder = lmOrderService.findById(id);
            if(null!=lmOrder&& null!=lmOrder.getStatus() && lmOrder.getStatus().equals("4")) {
            	 return jsonResult;
            }
            if(lmOrder!=null){
            	
            	logger.info("开始确认收货............更改订单状态:"+id);
                lmOrder.setStatus("4");
                lmOrder.setFinishtime(new Date());
                lmOrderService.updateService(lmOrder);
                //合买订单处理
                if(lmOrder.getType()==3){
                    boolean flag = true;
                    List<LmOrder> childlist = lmOrderService.findChildOrder(lmOrder.getPorderid());
                    for(LmOrder order:childlist){
                        if(!"4".equals(order.getStatus())){
                            flag = false;
                        }
                    }
                    if(flag){
                        LmOrder porder = lmOrderService.findById(lmOrder.getPorderid()+"");
                        porder.setStatus("4");
                        porder.setFinishtime(new Date());
                        lmOrderService.updateService(porder);
                    }
                }
                	
            	logger.info("开始确认收货............订单信息:"+lmOrder);
                //成长值处理
                LmMember lmMember = lmMemberService.findById(lmOrder.getMemberid()+"");
                int price = lmOrder.getRealpayprice().intValue();
                
            	logger.info("开始确认收货............订单金额:"+lmOrder.getRealpayprice());
                if(price>=12){
                    int growvalue = price/12;
                    lmMember.setGrowth_value(lmMember.getGrowth_value()+growvalue);
                    lmMemberService.updateService(lmMember);
                }
                //成长值
                List<LmMemberLevel> list = lmMemberLevelService.findAll();
                for(LmMemberLevel lmMemberLevel:list){
                    if(lmMember.getGrowth_value()>=lmMemberLevel.getGrowth_value()){
                        lmMember.setLevel_id(lmMemberLevel.getId());
                    }
                }
                //交易成功率
                LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(lmOrder.getMerchid()+"");
                //查询所有该商户的订单
                List<Map<String, String>> orderList = lmOrderService.findListByMerchidByApi(lmOrder.getMerchid());
                Double successnum = 0.00;
                for(Map<String, String> map:orderList){
                    String status = map.get("status");
                    if("3".equals(status)||"4".equals(status)){
                        successnum++;
                    }
                }
                lmMerchInfo.setSuccessper(successnum/orderList.size());
                lmMemberService.updateService(lmMember);
                
                logger.info("商户基本信息:"+lmMerchInfo);
                
                logger.info("开始确认收货............商户现有总资产:"+lmMerchInfo.getCredit());
                
                logger.info("开始确认收货............商户新增总资产:"+lmOrder.getRealpayprice());
                //将订单金额 打到商户余额上
                lmMerchInfo.setCredit((null==lmMerchInfo.getCredit()?BigDecimal.ZERO:lmMerchInfo.getCredit()).add(lmOrder.getRealpayprice()));
                lmMerchInfoService.updateService(lmMerchInfo);
                System.out.println(new Gson().toJson(lmMerchInfo));
                LmOrderLog lmOrderLog = new LmOrderLog();
                lmOrderLog.setOrderid(lmOrder.getOrderid());
                lmOrderLog.setOperatedate(new Date());
                lmOrderLog.setOperate("订单确认收货");
                lmOrderLogService.insert(lmOrderLog);

            }

        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;

    }

    /**
     * 退款申请
     * @param id
     * @return
     */
    @ApiOperation(value = "退款申请")
    @PostMapping("/back")
    @Transactional
    public JsonResult back(HttpServletRequest request,
                           @ApiParam(name = "id", value = "订单id", required = true)@RequestParam String id,
                           @ApiParam(name = "type", value = "1退款2退款并退货", required = true)@RequestParam int type,
                           @ApiParam(name = "imgs", value = "图片地址多个,隔开", required = false)@RequestParam(required = false) String imgs,
                           @ApiParam(name = "context", value = "内容", required = false)@RequestParam(required = false) String context,
                           @ApiParam(name = "reason", value = "原因", required = true)@RequestParam String reason
    ){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            if(id==null||reason==null||StringUtils.isEmpty(type)){
                jsonResult.setCode(JsonResult.ERROR);
                jsonResult.setMsg("参数异常");
            }
            String header = request.getHeader("Authorization");
            if (!StringUtils.isEmpty(header)) {
                if(StringUtils.isEmpty(redisUtils.get(header))){
                    jsonResult.setCode(JsonResult.LOGIN);
                    return jsonResult;
                }
            }
            LmOrder lmOrder = lmOrderService.findById(id);
            if(lmOrder!=null){
                if(lmOrder.getFinishtime()!=null){
                    Long finishtime =lmOrder.getFinishtime().getTime();
                    Long nowtime = new Date().getTime();
                    Long time = nowtime-finishtime;
                    if(time>1000*60*60*24*7){
                        return new JsonResult(JsonResult.ERROR,"已超过7天无法申请售后处理");
                    }
                    LmOrderRefundLog lmOrderRefundLog = new LmOrderRefundLog();
                    lmOrderRefundLog.setCreatetime(new Date());
                    lmOrderRefundLog.setReason(reason);
                    lmOrderRefundLog.setRefundmoney(lmOrder.getRealpayprice());
                    lmOrderRefundLog.setMemberid(lmOrder.getMemberid());
                    lmOrderRefundLog.setMerchid(lmOrder.getMerchid());
                    lmOrderRefundLog.setOrderid(lmOrder.getId());
                    lmOrderRefundLog.setStatus(0);
                    lmOrderRefundLog.setType(type);
                    if(!StringUtils.isEmpty(imgs)){
                        lmOrderRefundLog.setImgs(imgs);
                    }
                    if(!StringUtils.isEmpty(context)){
                        lmOrderRefundLog.setText(context);
                    }

                    lmOrderService.insertRefindLogService(lmOrderRefundLog);
                    lmOrder.setRefundid(lmOrderRefundLog.getId());
                    lmOrder.setBackprice(lmOrder.getRealpayprice());
                    lmOrder.setBackstatus(1);
                    lmOrder.setFinishtime(new Date());
                    lmOrderService.updateService(lmOrder);

                    LmOrderLog lmOrderLog = new LmOrderLog();
                    lmOrderLog.setOrderid(lmOrder.getOrderid());
                    lmOrderLog.setOperatedate(new Date());
                    lmOrderLog.setOperate("订单申请退款");
                    lmOrderLogService.insert(lmOrderLog);
                    LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(lmOrder.getMerchid()+"");
//                    pushmsgService.send(0,"有售后服务，请尽快处理","2",lmMerchInfo.getMember_id(),1);
                    HttpClient httpClient = new HttpClient();
                    httpClient.send("交易消息",lmMerchInfo.getMember_id()+"","有售后服务，请尽快处理");
                }else{
                    LmOrderRefundLog lmOrderRefundLog = new LmOrderRefundLog();
                    lmOrderRefundLog.setCreatetime(new Date());
                    lmOrderRefundLog.setReason(reason);
                    lmOrderRefundLog.setMemberid(lmOrder.getMemberid());
                    lmOrderRefundLog.setMerchid(lmOrder.getMerchid());
                    lmOrderRefundLog.setRefundmoney(lmOrder.getRealpayprice());
                    lmOrderRefundLog.setOrderid(lmOrder.getId());
                    lmOrderRefundLog.setStatus(0);
                    lmOrderRefundLog.setType(type);
                    if(!StringUtils.isEmpty(imgs)){
                        lmOrderRefundLog.setImgs(imgs);
                    }
                    if(!StringUtils.isEmpty(context)){
                        lmOrderRefundLog.setText(context);
                    }
                    lmOrderService.insertRefindLogService(lmOrderRefundLog);
                    lmOrder.setRefundid(lmOrderRefundLog.getId());
                    lmOrder.setBackprice(lmOrder.getRealpayprice());
                    lmOrder.setBackstatus(1);
                    lmOrder.setFinishtime(new Date());
                    lmOrderService.updateService(lmOrder);
                    LmOrderLog lmOrderLog = new LmOrderLog();
                    lmOrderLog.setOrderid(lmOrder.getOrderid());
                    lmOrderLog.setOperatedate(new Date());
                    lmOrderLog.setOperate("订单申请退款");
                    lmOrderLogService.insert(lmOrderLog);
                    LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(lmOrder.getMerchid()+"");
//                    pushmsgService.send(0,"有售后服务，请尽快处理","2",lmMerchInfo.getMember_id(),1);
                    HttpClient httpClient = new HttpClient();
                    httpClient.send("交易消息",lmMerchInfo.getMember_id()+"","有售后服务，请尽快处理");
                }
            }
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
     * 退款物流信息填写
     * @param id
     * @return
     */
    @ApiOperation(value = "退款物流信息填写")
    @PostMapping("/backexpress")
    @Transactional
    public JsonResult backexpress(HttpServletRequest request,
                                  @ApiParam(name = "id", value = "订单id", required = true)@RequestParam String id,
                                  @ApiParam(name = "sn", value = "快递单号", required = true)@RequestParam String sn,
                                  @ApiParam(name = "expressname", value = "快递公司", required = true)@RequestParam String expressname,
                                  @ApiParam(name = "express", value = "快递公司代号", required = true)@RequestParam String express,
                                  @ApiParam(name = "remark", value = "备注", required = true)@RequestParam String remark
    ){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            String header = request.getHeader("Authorization");
            if (!StringUtils.isEmpty(header)) {
                if(StringUtils.isEmpty(redisUtils.get(header))){
                    jsonResult.setCode(JsonResult.LOGIN);
                    return jsonResult;
                }
            }
            LmOrder lmOrder = lmOrderService.findById(id);
            if(lmOrder!=null)
            {
                LmOrderRefundLog lmOrderRefundLog = lmOrderService.findRefundLogById(lmOrder.getRefundid());
                if(lmOrderRefundLog.getStatus()!=1) 
                {
                	  return new JsonResult(JsonResult.ERROR,"请等待商家审核！");
                }
                lmOrderRefundLog.setSn(sn);
                lmOrderRefundLog.setExpress(express);
                lmOrderRefundLog.setRemark(remark);
                lmOrderRefundLog.setExpressname(expressname);
                if(lmOrderRefundLog.getChecktime()==null){
                    return new JsonResult(JsonResult.ERROR,"申请尚未审核，请稍后再试");
                }
                Long nowtime = new Date().getTime();
                Long checktime = lmOrderRefundLog.getChecktime().getTime();

             //   Long time = nowtime-checktime;
               // if(time>1000*60*60*24*7){
                //    lmOrderRefundLog.setStatus(-1);
                //    lmOrder.setStatus("4");
              //  }
                lmOrderService.updateRefundLogService(lmOrderRefundLog);
                lmOrderService.updateService(lmOrder);

                LmOrderLog lmOrderLog = new LmOrderLog();
                lmOrderLog.setOrderid(lmOrder.getOrderid());
                lmOrderLog.setOperatedate(new Date());
                lmOrderLog.setOperate("订单填写物流信息");
                lmOrderLogService.insert(lmOrderLog);

            }
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
     * 订单退货地址
     * @param id
     * @return
     */
    @ApiOperation(value = "订单退货地址")
    @PostMapping("/backaddress")
    @Transactional
    public JsonResult backaddress(HttpServletRequest request,
                                  @ApiParam(name = "id", value = "订单id", required = true)@RequestParam String id
    ){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        Map<String,Object> returnmap = new HashMap<>();
        try {
            String header = request.getHeader("Authorization");
            if (!StringUtils.isEmpty(header)) {
                if(StringUtils.isEmpty(redisUtils.get(header))){
                    jsonResult.setCode(JsonResult.LOGIN);
                    return jsonResult;
                }
            }
            LmOrder lmOrder = lmOrderService.findById(id);
            LmOrderRefundLog lmOrderRefundLog = lmOrderService.findRefundLogById(lmOrder.getRefundid());
            LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(lmOrder.getMerchid()+"");
            returnmap.put("name",lmMerchInfo.getStore_name());
            returnmap.put("mobile",lmMerchInfo.getMobile());
            returnmap.put("address",lmMerchInfo.getRefund_address());
            if(lmOrder.getBackstatus()>0&&lmOrderRefundLog.getStatus()>0&&lmOrderRefundLog.getType()==2){
                returnmap.put("isshowexpress","true");
                if(!StringUtils.isEmpty(lmOrderRefundLog.getExpress())){
                    returnmap.put("isedit","true");
                    returnmap.put("express",lmOrderRefundLog.getExpress());
                    returnmap.put("expressname",lmOrderRefundLog.getExpressname());
                    returnmap.put("sn",lmOrderRefundLog.getSn());
                    returnmap.put("remark",lmOrderRefundLog.getRemark());
                }else{
                    returnmap.put("isedit","true");
                }
            }else{
                returnmap.put("isshowexpress","false");
            }
            jsonResult.setData(returnmap);
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
     * 修改订单地址接口
     * @param id
     * @return
     */
    @ApiOperation(value = "修改订单地址接口")
    @PostMapping("/editaddress")
    public JsonResult comment(HttpServletRequest request,
                              @ApiParam(name = "id", value = "订单id", required = true)@RequestParam int id,
                              @ApiParam(name = "addressid", value = "地址id", required = true)@RequestParam int addressid
    ){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            String header = request.getHeader("Authorization");
            if (!StringUtils.isEmpty(header)) {
                if(StringUtils.isEmpty(redisUtils.get(header))){
                    jsonResult.setCode(JsonResult.LOGIN);
                    return jsonResult;
                }
            }
            LmOrder lmOrder = lmOrderService.findById(id+"");
            LmMemberAddress lmMemberAddress = lmMemberAddressService.findById(addressid+"");
            if(lmOrder!=null&&lmMemberAddress!=null){
                lmOrder.setChargephone(lmMemberAddress.getMobile());
                lmOrder.setChargeaddress(lmMemberAddress.getProvince()+lmMemberAddress.getCity()+lmMemberAddress.getDistrict()+lmMemberAddress.getAddress_info());
                lmOrder.setChargename(lmMemberAddress.getRealname());
                lmOrderService.updateService(lmOrder);
            }
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;

    }

    /**
     * 发表评论接口
     * @param id
     * @return
     */
    @ApiOperation(value = "发表评论接口")
    @PostMapping("/comment")
    @Transactional
    public JsonResult comment(HttpServletRequest request,
                              @ApiParam(name = "id", value = "订单id", required = true)@RequestParam int id,
                              @ApiParam(name = "score", value = "得分", required = true)@RequestParam(required = true) String score,
                              @ApiParam(name = "comment", value = "评价", required = true)@RequestParam(required = true) String comment,
                              @ApiParam(name = "imgurl", value = "图片地址", required = true)@RequestParam(required = true) String imgurl){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            String header = request.getHeader("Authorization");
            if (!StringUtils.isEmpty(header)) {
                if(StringUtils.isEmpty(redisUtils.get(header))){
                    jsonResult.setCode(JsonResult.LOGIN);
                    return jsonResult;
                }
            }
            LmOrder lmOrder = lmOrderService.findById(id+"");
            LmOrderGoods lmOrderGoods = lmOrderGoodsService.findByOrderid(lmOrder.getId());
            lmOrder.setCommentstatus(1);
            lmOrderService.updateService(lmOrder);
            LmOrderComment lmOrderComment  = new LmOrderComment();
            lmOrderComment.setComment(comment);
            lmOrderComment.setImg(imgurl);
            lmOrderComment.setScore(Double.parseDouble(score));
            lmOrderComment.setMerchid(lmOrder.getMerchid());
            lmOrderComment.setGoodid(lmOrderGoods.getGoodid());
            lmOrderComment.setOrder_id(id);
            if(Double.parseDouble(score)>=4.0){
                lmOrderComment.setIsgood(1);
            }else if(Double.parseDouble(score)<=2.0){
                lmOrderComment.setIsgood(3);
            }else{
                lmOrderComment.setIsgood(2);
            }
            lmOrderCommentService.insertService(lmOrderComment);

            //设置商户的评分
            LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(lmOrder.getMerchid()+"");
            List<LmOrderComment> list = lmOrderGoodsService.findByMerchid(lmOrder.getMerchid());
            Double allscore = 0.00;
            Double goodnum = 0.00;
            for(LmOrderComment orderComment:list){
                allscore+=orderComment.getScore();
                if(orderComment.getIsgood()==1){
                    goodnum++;
                }
            }
            Double goodper = (goodnum/list.size())*100;
            Double scoreper = allscore/list.size();
            lmMerchInfo.setScore(scoreper);
            lmMerchInfo.setSuccessper(goodper);
            lmMerchInfoService.updateService(lmMerchInfo);

            LmOrderLog lmOrderLog = new LmOrderLog();
            lmOrderLog.setOrderid(lmOrder.getOrderid());
            lmOrderLog.setOperatedate(new Date());
            lmOrderLog.setOperate("订单发表评论");
            lmOrderLogService.insert(lmOrderLog);

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
     * 申请撤回接口
     * @param id
     * @return
     */
    @ApiOperation(value = "申请撤回接口")
    @PostMapping("/undo")
    @Transactional
    public JsonResult undo(HttpServletRequest request,
                           @ApiParam(name = "id", value = "订单id", required = true)@RequestParam String id
    ){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            String header = request.getHeader("Authorization");
            if (!StringUtils.isEmpty(header)) {
                if(StringUtils.isEmpty(redisUtils.get(header))){
                    jsonResult.setCode(JsonResult.LOGIN);
                    return jsonResult;
                }
            }
            LmOrder lmOrder = lmOrderService.findById(id);
            if(lmOrder!=null){
                LmOrderRefundLog lmOrderRefundLog = lmOrderService.findRefundLogById(lmOrder.getRefundid());
                lmOrderRefundLog.setStatus(-2);
                lmOrderService.updateRefundLogService(lmOrderRefundLog);
                lmOrder.setBackstatus(0);
                lmOrderService.updateService(lmOrder);
            }
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
     * 撤回详情
     * @param id
     * @return
     */
    @ApiOperation(value = "撤回详情")
    @PostMapping("/backdetail")
    public JsonResult backdetail(HttpServletRequest request,
                                 @ApiParam(name = "id", value = "订单id", required = true)@RequestParam String id
    ){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        Map<String,Object> returnmap = new HashMap<>();
        try {
            String header = request.getHeader("Authorization");
            if (!StringUtils.isEmpty(header)) {
                if(StringUtils.isEmpty(redisUtils.get(header))){
                    jsonResult.setCode(JsonResult.LOGIN);
                    return jsonResult;
                }
            }
            LmOrder lmOrder = lmOrderService.findById(id);
            if(lmOrder!=null){
                LmOrderGoods lmOrderGoods = lmOrderGoodsService.findByOrderid(lmOrder.getId());
                returnmap.put("price",lmOrderGoods.getGoodprice());
                returnmap.put("num",lmOrderGoods.getGoodnum());
                Good good = goodService.findById(lmOrderGoods.getGoodid());
                if(good!=null){
                    returnmap.put("spec",good.getSpec());
                    returnmap.put("goodname",good.getTitle());
                    returnmap.put("thumb",good.getThumb());
                }
                LmOrderRefundLog lmOrderRefundLog = lmOrderService.findRefundLogById(lmOrder.getRefundid());
                if(lmOrderRefundLog!=null){
                    returnmap.put("text",lmOrderRefundLog.getText());
                    returnmap.put("reason",lmOrderRefundLog.getReason());
                    returnmap.put("imgs",lmOrderRefundLog.getImgs());
                    if(lmOrder.getBackstatus()>0&&lmOrderRefundLog.getStatus()>0&&lmOrderRefundLog.getType()==2){
                        returnmap.put("isshowexpress","true");
                        if(!StringUtils.isEmpty(lmOrderRefundLog.getExpress())){
                            returnmap.put("isedit","true");
                            returnmap.put("express",lmOrderRefundLog.getExpress());
                            returnmap.put("expressname",lmOrderRefundLog.getExpressname());
                            returnmap.put("sn",lmOrderRefundLog.getSn());
                            returnmap.put("remark",lmOrderRefundLog.getRemark());
                        }else{
                            returnmap.put("isedit","true");
                        }
                    }else{
                        returnmap.put("isshowexpress","false");
                    }
                }
                LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(lmOrder.getMerchid()+"");
                if(lmMerchInfo!=null){
                    returnmap.put("name",lmMerchInfo.getStore_name());
                    returnmap.put("mobile",lmMerchInfo.getMobile());
                    returnmap.put("address",lmMerchInfo.getRefund_address());
                    returnmap.put("backstatus",lmOrder.getBackstatus());
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
     * 订单字典接口
     * @param
     * @return
     */
    @ApiOperation(value = "订单字典接口")
    @PostMapping("/dict")
    @Transactional
    public JsonResult undo(HttpServletRequest request
    ){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            List<LmSysDictItem> lmSysDictItems = dict_itemService.findByDictCode("order");
            if(lmSysDictItems!=null){
                jsonResult.setData(lmSysDictItems);
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
     * 订单详情
     * @param orderno
     * @return
     */
    @ApiOperation(value = "订单详情")
    @PostMapping("/detail")
    @Transactional
    public JsonResult detail(HttpServletRequest request,
                             @ApiParam(name = "orderno", value = "订单号", required = true)@RequestParam String orderno
    ){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        Map<String,Object> returnmap = new HashMap<>();
        try {
            String header = request.getHeader("Authorization");
            if (!StringUtils.isEmpty(header)) {
                if(StringUtils.isEmpty(redisUtils.get(header))){
                    jsonResult.setCode(JsonResult.LOGIN);
                    return jsonResult;
                }
            }
            LmOrder lmOrder = lmOrderService.findByOrderId(orderno);
            if(lmOrder!=null){
                LmOrderGoods lmOrderGoods = lmOrderGoodsService.findByOrderid(lmOrder.getId());
                returnmap.put("num",lmOrderGoods.getGoodnum());
                returnmap.put("commentstatus",lmOrder.getCommentstatus());
                if(lmOrder.getType()!=3){
                    if(lmOrder.getIslivegood()==1){
                        LivedGood good = goodService.findLivedGood(lmOrderGoods.getGoodid());
                        if(good!=null){
                            if(good.getType()==1){
                                //拍卖
                                returnmap.put("spec","");
                                returnmap.put("goodname",good.getName());
                                returnmap.put("thumb",good.getImg());
                                returnmap.put("price",lmOrder.getTotalprice());
                                returnmap.put("coupons","");
                            }else{
                                //一口价
                                returnmap.put("spec","");
                                returnmap.put("goodname",good.getName());
                                returnmap.put("thumb",good.getImg());
                                returnmap.put("price",good.getPrice());
                                returnmap.put("coupons","");
                            }
                        }
                    }else{
                        Good good = goodService.findById(lmOrderGoods.getGoodid());
                        if(good!=null){
                            if(good.getType()==1){
                                //拍卖
                                returnmap.put("spec",good.getSpec());
                                returnmap.put("goodname",good.getTitle());
                                returnmap.put("thumb",good.getThumb());
                                returnmap.put("price",lmOrder.getTotalprice());
                                returnmap.put("coupons",lmOrder.getTotalprice().subtract(lmOrder.getRealpayprice()).subtract(lmOrder.getRealexpressprice()));
                            }else{
                                //一口价
                                returnmap.put("spec",good.getSpec());
                                returnmap.put("goodname",good.getTitle());
                                returnmap.put("thumb",good.getThumb());
                                returnmap.put("price",good.getProductprice());
                                returnmap.put("coupons",lmOrder.getTotalprice().subtract(lmOrder.getRealpayprice()).subtract(lmOrder.getRealexpressprice()));
                            }
                        }
                    }
                }else{
                    LmShareGood lmShareGood = goodService.findshareById(lmOrderGoods.getGoodid());
                    if(lmShareGood!=null){
                        returnmap.put("spec","");
                        returnmap.put("goodname",lmShareGood.getName());
                        returnmap.put("thumb",lmShareGood.getImg());
                        returnmap.put("price",lmOrder.getTotalprice());
                        returnmap.put("coupons","");
                    }
                }
                returnmap.put("address",lmOrder.getChargeaddress());
                returnmap.put("name",lmOrder.getChargename());
                returnmap.put("mobile",lmOrder.getChargephone());
                LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(lmOrder.getMerchid()+"");
                if(lmMerchInfo!=null){
                    returnmap.put("store_name",lmMerchInfo.getStore_name());
                }
                returnmap.put("backstatus",lmOrder.getBackstatus());
                returnmap.put("realexpressprice",lmOrder.getRealexpressprice());
                returnmap.put("realprice",lmOrder.getRealpayprice());
                returnmap.put("status",lmOrder.getStatus());
                returnmap.put("paystatus",lmOrder.getPaystatus());
                returnmap.put("orderid",lmOrder.getOrderid());
                returnmap.put("type",lmOrder.getType());
                returnmap.put("id",lmOrder.getId());
                returnmap.put("createtime",DateUtils.sdf_yMdHms.format(lmOrder.getCreatetime()));
                ExpressUtil expressUtil = new ExpressUtil();
                String result = expressUtil.synQueryData(lmOrder.getExpress(), lmOrder.getExpresssn(),"","","",0);
                returnmap.put("expressinfo",result);
                LmOrderRefundLog refund= lmMerchOrderService.getRefundLog(lmOrder.getRefundid());
                if(refund!=null){
                    returnmap.put("refundtype",refund.getType());
                    returnmap.put("refundstatus",refund.getStatus());
                }
                //发送通知给买家
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
     * 订单删除
     * 废弃
     * @param id
     * @return
     */
//    @ApiOperation(value = "订单删除")
//    @PostMapping("/deleteorder")
//    public JsonResult deleteorder(HttpServletRequest request,
//                                  @ApiParam(name = "orderno", value = "订单号", required = true)@RequestParam String orderno
//    ){
//        JsonResult jsonResult = new JsonResult();
//        jsonResult.setCode(JsonResult.SUCCESS);
//        try {
//            LmOrder order = lmOrderService.findByOrderId(orderno);
//            if(order!=null){
//                lmOrderService.deleteByOrderno(order);
//            }
//        } catch (Exception e) {
//            jsonResult.setMsg(e.getMessage());
//            jsonResult.setCode(JsonResult.ERROR);
//            logger.error(e.getMessage());
//        }
//        return jsonResult;
//
//    }

}