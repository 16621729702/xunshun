package com.wink.livemall.admin.api.order;

import com.google.gson.Gson;
import com.wink.livemall.admin.api.help.CommentService;
import com.wink.livemall.admin.util.*;
import com.wink.livemall.admin.util.httpclient.HttpClient;
import com.wink.livemall.coupon.dto.LmConpouMember;
import com.wink.livemall.coupon.dto.LmCouponLog;
import com.wink.livemall.coupon.dto.LmCoupons;
import com.wink.livemall.coupon.service.LmCouponMemberService;
import com.wink.livemall.coupon.service.LmCouponsService;
import com.wink.livemall.goods.dto.Good;
import com.wink.livemall.goods.dto.LivedGood;

import com.wink.livemall.goods.dto.LmShareGood;
import com.wink.livemall.goods.service.GoodService;
import com.wink.livemall.member.dto.*;
import com.wink.livemall.member.service.*;
import com.wink.livemall.merch.dto.LmMerchInfo;
import com.wink.livemall.merch.service.LmMerchInfoService;
import com.wink.livemall.order.dto.*;
import com.wink.livemall.order.service.*;
import com.wink.livemall.sys.dict.dto.LmSysDictItem;
import com.wink.livemall.sys.dict.service.Dict_itemService;
import com.wink.livemall.sys.msg.service.PushmsgService;
import com.wink.livemall.sys.setting.dto.Configs;
import com.wink.livemall.sys.setting.service.ConfigsService;
import com.wink.livemall.utils.cache.redis.RedisUtil;
import com.wink.livemall.utils.sms.SmsUtils;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.util.StringUtils.isEmpty;

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
    private LmCouponMemberService lmCouponMemberService;
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
    private LmFalsifyService lmFalsifyService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CommissionLogService commissionLogService;
    @Autowired
    private AgencyInfoService agencyInfoService;
    @Autowired
    private ForwardUserService forwardUserService;


    @ApiOperation(value = "获取订单列表")
    @PostMapping("/list")
    public JsonResult getorderlist(HttpServletRequest request,
                                   @ApiParam(name = "status", value = "订单状态0未支付1待发货2待收货3待评价4已完成5已关闭-1交易失败-2全部6售后", required = true)@RequestParam String status,
                                   @ApiParam(name = "page", value = "页码",defaultValue = "1",required=true) @RequestParam(value = "page",required = true,defaultValue = "1") int page,
                                   @ApiParam(name = "pagesize", value = "每页个数",defaultValue = "10",required=true) @RequestParam(value = "pagesize",required = true,defaultValue = "10") int pagesize){
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

            //获取订单基本信息
            List<Map<String,Object>> list = lmOrderService.findOrderList(status,Integer.parseInt(userid));
            for(Map<String,Object> map:list){
                String address = StringUtils.isEmpty(map.get("chargephone"))?null:(String) map.get("chargeaddress");
                String phone = StringUtils.isEmpty(map.get("chargephone"))?null:(String) map.get("chargephone");
                map.put("address",address);
                map.put("phone",phone);
                BigDecimal totalprice = StringUtils.isEmpty(map.get("totalprice"))?new BigDecimal(0.00):(BigDecimal) map.get("totalprice");
                BigDecimal realpayprice = StringUtils.isEmpty(map.get("realpayprice"))?new BigDecimal(0.00):(BigDecimal) map.get("realpayprice");
                BigDecimal couponprice=(totalprice.subtract(realpayprice)).setScale(2,BigDecimal.ROUND_HALF_UP);
                map.put("couponprice",couponprice);
                map.put("realpayprice",realpayprice);
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
                            map.put("goodstype",good.getType());
                            map.put("goodname",good.getName());
                            map.put("thumb",good.getImg());
                            map.put("spec","");
                        }else{
                            //普通订单
                            Good good = goodService.findById((int)map.get("goodid"));
                            if(good!=null){
                                map.put("goodstype",good.getType());
                                map.put("goodname",good.getTitle());
                                map.put("thumb",good.getThumb());
                                map.put("spec",good.getSpec());
                            }
                        }
                    }
                }
                if(map.get("backstatus")!=null){
                    if((int)map.get("backstatus")!=0){
                        int refundid = StringUtils.isEmpty(map.get("refundid"))?0:(int) map.get("refundid");
                        LmOrderRefundLog lmOrderRefundLog = lmOrderService.findRefundLogById(refundid);
                        if(lmOrderRefundLog!=null){
                            map.put("reason",lmOrderRefundLog.getReason());
                            map.put("refusal_instructions",lmOrderRefundLog.getRefusal_instructions());
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

    @ApiOperation(value = "获取买家订单列表长度")
    @PostMapping("/listsize")
    public JsonResult getorderlistsize(HttpServletRequest request){
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
            //获取订单基本信息
            Map<String, Object> map = new ConcurrentHashMap<>();
            for(int i=-2;i<7;i++) {
                String size=null;
                if(i==-2){
                    size="count" ;
                }else if(i==-1){
                    size="false";
                }else {
                    size=""+i;
                }
                Integer ordersize = lmOrderService.ordersize(i, Integer.parseInt(userid));
                map.put("ordersize"+size,ordersize);
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
        System.out.print("用户优惠券id+++++++++++=="+couponid);
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
            LmMemberAddress address = lmMemberAddressService.findById(addressid+"");
            Integer maxId = lmOrderService.findMaxId();
            if(maxId==null){
                maxId = 0;
            }
            String datetime = DateUtils.sdfyMdHm.format(new Date());
            Configs configs =configsService.findByTypeId(Configs.type_pay);
            Map map =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
            String prfix=map.get("msgSrcId")+"";
            LmConpouMember lmConpouMember =null;
            if(!StringUtils.isEmpty(couponid)){
                if(merchid==363){
                    lmConpouMember =null;
                }else {
                    lmConpouMember = lmCouponMemberService.findById(Integer.parseInt(couponid));
                }
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
            LmCoupons lmCoupons =null;
            if(lmConpouMember!=null){
                if(1==lmConpouMember.getCanUse()){
                    lmCoupons = lmCouponsService.findById(String.valueOf(lmConpouMember.getCouponId()));
                }else {
                    lmCoupons=null;
                }
            }
            if(lmCoupons!=null){

                       // 扣除优惠券数量
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
                        lmOrder.setCoupon_id(lmCoupons.getId());
                        lmOrder.setCoupon_price(lmCoupons.getCouponValue());
                        BigDecimal min=new BigDecimal(0);
                        if(good.getFreeshipping()==1){
                            lmOrder.setRealexpressprice(new BigDecimal(0));
                            BigDecimal  realPayPrice= new BigDecimal(price).subtract(lmCoupons.getCouponValue()).setScale(2,BigDecimal.ROUND_HALF_UP);
                            if(realPayPrice.compareTo(min)>0){
                                lmOrder.setRealpayprice(realPayPrice);
                            }else {
                                lmOrder.setRealpayprice(new BigDecimal(0.01).setScale(2,BigDecimal.ROUND_HALF_UP));
                            }
                        }else{
                            lmOrder.setRealexpressprice(good.getExpressprice());
                            BigDecimal total = new BigDecimal(price).add(good.getExpressprice()).setScale(2,BigDecimal.ROUND_HALF_UP);
                            BigDecimal  realPayPrice= total.subtract(lmCoupons.getCouponValue());
                            if(realPayPrice.compareTo(min)>0){
                                lmOrder.setRealpayprice(realPayPrice);
                            }else {
                                lmOrder.setRealpayprice(good.getExpressprice());
                            }
                        }
                        lmOrder.setTotalprice(new BigDecimal(price));
                        lmOrderService.insertService(lmOrder);
                        //设置库存
                        if(good.getStock()>0){
                            int newstock = good.getStock()-Integer.parseInt(num);
                            good.setStock(newstock);
                            if(newstock==0){
                                good.setState(0);
                                good.setWarehouse("3");
                            }
                        }
                        goodService.updateGoods(good);
                        //添加关联表信息
                        LmOrderGoods lmOrderGoods = new LmOrderGoods();
                        lmOrderGoods.setGoodid(goodid);
                        lmOrderGoods.setGoodnum(Integer.parseInt(num));
                        lmOrderGoods.setGoodstype(0);
                        lmOrderGoods.setGoodprice(good.getProductprice());
                        lmOrderGoods.setOrderid(lmOrder.getId());
                        lmOrderGoodsService.insertService(lmOrderGoods);
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
                        lmCouponLog.setMemberid(Integer.parseInt(userid));
                        lmCouponLog.setOrderid(lmOrder.getId());
                        lmCouponLog.setPrice(lmCoupons.getCouponValue());
                        lmCouponsService.insertLogService(lmCouponLog);
                        jsonResult.setData(lmOrder.getOrderid());
                        //添加订单记录
                        LmOrderLog lmOrderLog = new LmOrderLog();
                        lmOrderLog.setOrderid(lmOrder.getOrderid());
                        lmOrderLog.setOperatedate(new Date());
                        lmOrderLog.setOperate("订单创建");
                        lmOrderLogService.insert(lmOrderLog);

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
                //设置库存
                if(good.getStock()>0){
                    int newstock = good.getStock()-Integer.parseInt(num);
                    good.setStock(newstock);
                    if(newstock==0){
                        good.setState(0);
                        good.setWarehouse("3");
                    }

                }
                goodService.updateGoods(good);
                LmOrderGoods lmOrderGoods = new LmOrderGoods();
                lmOrderGoods.setGoodid(goodid);
                lmOrderGoods.setGoodstype(0);
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
        String header = request.getHeader("Authorization");
        String userid="";
        if (!StringUtils.isEmpty(header)) {
            if(!StringUtils.isEmpty(TokenUtil.getUserId(header))){
                userid = TokenUtil.getUserId(header);
            }else{
                jsonResult.setMsg("用户未登录");
                jsonResult.setCode(JsonResult.LOGIN);
                return jsonResult;
            }
        }
        try {

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
        String header = request.getHeader("Authorization");
        String userid="";
        if (!StringUtils.isEmpty(header)) {
            if(!StringUtils.isEmpty(TokenUtil.getUserId(header))){
                userid = TokenUtil.getUserId(header);
            }else{
                jsonResult.setMsg("用户未登录");
                jsonResult.setCode(JsonResult.LOGIN);
                return jsonResult;
            }
        }
        try {
            LmOrder lmOrder = lmOrderService.findById(id);
            if(null!=lmOrder&& null!=lmOrder.getStatus() && lmOrder.getStatus().equals("4")) {
            	 return jsonResult;
            }
            if(lmOrder!=null){
            	
            	logger.info("开始确认收货............更改订单状态:"+id);
                lmOrder.setStatus("3");
                lmOrder.setFinishtime(new Date());
                lmOrderService.updateService(lmOrder);
                //合买订单处理
                if(lmOrder.getType()==3){
                    boolean flag = true;
                    List<LmOrder> childlist = lmOrderService.findChildOrder(lmOrder.getPorderid());
                    for(LmOrder order:childlist){
                        if(!"3".equals(order.getStatus())){
                            flag = false;
                        }
                    }
                    if(flag){
                        LmOrder porder = lmOrderService.findById(lmOrder.getPorderid()+"");
                        porder.setStatus("3");
                        porder.setFinishtime(new Date());
                        lmOrderService.updateService(porder);
                    }
                }
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
                //将订单金额 打到商户余额上
                BigDecimal realPrice = lmOrder.getRealpayprice();
                if(lmOrder.getPaystatus().equals("3")){
                    realPrice = realPrice.multiply(new BigDecimal(94.4)).divide(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_DOWN);
                }else {
                    realPrice = realPrice.multiply(new BigDecimal(94.7)).divide(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_DOWN);
                }
                LmOrderGoods lmOrderGoods = lmOrderGoodsService.findByOrderid(lmOrder.getId());
                if(lmOrderGoods.getGoodstype()==0){
                    ForwardUser forwardUser = forwardUserService.findListByUserId(lmOrder.getMemberid());
                    if(forwardUser!=null){
                        AgencyInfo agencyInfo = agencyInfoService.findListByUserId(forwardUser.getForward_id());
                        if(agencyInfo!=null){
                            Good byId = goodService.findById(lmOrderGoods.getGoodid());
                            BigDecimal min=new BigDecimal(0);
                            if(byId.getCommission().compareTo(min)>0){
                                realPrice = realPrice.subtract(byId.getCommission()).setScale(2, BigDecimal.ROUND_DOWN);
                                CommissionLog commissionLog=new CommissionLog();
                                commissionLog.setGiver(lmMerchInfo.getMember_id());
                                commissionLog.setGainer(forwardUser.getForward_id());
                                commissionLog.setOrder_id(lmOrder.getId());
                                commissionLog.setCommission(byId.getCommission());
                                commissionLogService.insertCommissionLog(commissionLog);
                                LmMember Forward = lmMemberService.findById(String.valueOf(lmOrder.getForward()));
                                Forward.setBlance(Forward.getBlance().subtract(byId.getCommission()));
                                Forward.setCredit2(Forward.getCredit2().add(byId.getCommission()));
                                lmMemberService.updateService(Forward);
                            }
                        }
                    }
                }
                logger.info("开始确认收货............商户新增总资产:"+realPrice);
                lmMerchInfo.setCredit((null==lmMerchInfo.getCredit()?BigDecimal.ZERO:lmMerchInfo.getCredit()).add(realPrice).setScale(2,BigDecimal.ROUND_DOWN));
                lmMerchInfoService.updateService(lmMerchInfo);
                System.out.println(new Gson().toJson(lmMerchInfo));
                LmOrderLog lmOrderLog = new LmOrderLog();
                lmOrderLog.setOrderid(lmOrder.getOrderid());
                lmOrderLog.setOperatedate(new Date());
                lmOrderLog.setOperate("订单确认收货");
                lmOrderLogService.insert(lmOrderLog);
                //给他退款
                LmFalsify lmFalsify = lmFalsifyService.isFalsify(String.valueOf(lmOrder.getMemberid()), String.valueOf(lmOrderGoods.getGoodid()), String.valueOf(lmOrderGoods.getGoodstype()));
                if(!isEmpty(lmFalsify)){
                  String falsifyId=lmFalsify.getFalsify_id();
                  BigDecimal falsify =lmFalsify.getFalsify();
                    falsify=falsify.multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP);
                    String falsifyP=String.valueOf(falsify);
                    commentService.autoRefundFalsify(falsifyId,falsifyP);
                    HttpClient httpClient = new HttpClient();
                    httpClient.send("交易消息",lmFalsify.getMember_id()+"","违约金退款已完成，请确认是否收到退款");
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
        String header = request.getHeader("Authorization");
        String userid="";
        if (!StringUtils.isEmpty(header)) {
            if(!StringUtils.isEmpty(TokenUtil.getUserId(header))){
                userid = TokenUtil.getUserId(header);
            }else{
                jsonResult.setMsg("用户未登录");
                jsonResult.setCode(JsonResult.LOGIN);
                return jsonResult;
            }
        }
        try {
            if(id==null||reason==null||StringUtils.isEmpty(type)){
                jsonResult.setCode(JsonResult.ERROR);
                jsonResult.setMsg("参数异常");
            }
            LmOrder lmOrder = lmOrderService.findById(id);
            if(lmOrder!=null){
                lmOrder.setSend_type(5);
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
                    String msg ="您的店铺"+lmMerchInfo.getStore_name()+":\n"+"有售后服务,请尽快处理";
                    httpClient.send("交易消息",lmMerchInfo.getMember_id()+"",msg);
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
                    String msg ="您的店铺"+lmMerchInfo.getStore_name()+":\n"+"有售后服务,请尽快处理";
                    httpClient.send("交易消息",lmMerchInfo.getMember_id()+"",msg);
                }
            }
            LmMerchInfo info = lmMerchInfoService.findById(lmOrder.getMerchid() + "");
            LmMember merMember = lmMemberService.findById(info.getMember_id() + "");
            SmsUtils.sendValidCodeMsgs(merMember.getMobile(), info.getStore_name(),  "SMS_213281260");
            String msg="亲爱的商家，您的"+info.getStore_name()+"店铺中收到一笔退款请求，请在72小时内处理，避免逾期，请尽快处理，谢谢配合";
            PropellingUtil.IOSPropellingMessage("系统消息",msg,merMember.getId()+"");
            PropellingUtil.AndroidPropellingMessage("系统消息",msg,merMember.getId()+"");
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
        String header = request.getHeader("Authorization");
        String userid="";
        if (!StringUtils.isEmpty(header)) {
            if(!StringUtils.isEmpty(TokenUtil.getUserId(header))){
                userid = TokenUtil.getUserId(header);
            }else{
                jsonResult.setMsg("用户未登录");
                jsonResult.setCode(JsonResult.LOGIN);
                return jsonResult;
            }
        }
        try {
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
        String header = request.getHeader("Authorization");
        String userid="";
        if (!StringUtils.isEmpty(header)) {
            if(!StringUtils.isEmpty(TokenUtil.getUserId(header))){
                userid = TokenUtil.getUserId(header);
            }else{
                jsonResult.setMsg("用户未登录");
                jsonResult.setCode(JsonResult.LOGIN);
                return jsonResult;
            }
        }
        Map<String,Object> returnmap = new HashMap<>();
        try {
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
        String header = request.getHeader("Authorization");
        String userid="";
        if (!StringUtils.isEmpty(header)) {
            if(!StringUtils.isEmpty(TokenUtil.getUserId(header))){
                userid = TokenUtil.getUserId(header);
            }else{
                jsonResult.setMsg("用户未登录");
                jsonResult.setCode(JsonResult.LOGIN);
                return jsonResult;
            }
        }
        try {
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
        String header = request.getHeader("Authorization");
        String userid="";
        if (!StringUtils.isEmpty(header)) {
            if(!StringUtils.isEmpty(TokenUtil.getUserId(header))){
                userid = TokenUtil.getUserId(header);
            }else{
                jsonResult.setMsg("用户未登录");
                jsonResult.setCode(JsonResult.LOGIN);
                return jsonResult;
            }
        }
        try {
            LmOrder lmOrder = lmOrderService.findById(id+"");
            LmOrderGoods lmOrderGoods = lmOrderGoodsService.findByOrderid(lmOrder.getId());
            lmOrder.setStatus("4");
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
        String header = request.getHeader("Authorization");
        String userid="";
        if (!StringUtils.isEmpty(header)) {
            if(!StringUtils.isEmpty(TokenUtil.getUserId(header))){
                userid = TokenUtil.getUserId(header);
            }else{
                jsonResult.setMsg("用户未登录");
                jsonResult.setCode(JsonResult.LOGIN);
                return jsonResult;
            }
        }
        try {
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
        String header = request.getHeader("Authorization");
        String userid="";
        if (!StringUtils.isEmpty(header)) {
            if(!StringUtils.isEmpty(TokenUtil.getUserId(header))){
                userid = TokenUtil.getUserId(header);
            }else{
                jsonResult.setMsg("用户未登录");
                jsonResult.setCode(JsonResult.LOGIN);
                return jsonResult;
            }
        }
        Map<String,Object> returnmap = new HashMap<>();
        try {
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
    public JsonResult undo(HttpServletRequest request){
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
        String header = request.getHeader("Authorization");
        if (!StringUtils.isEmpty(header)) {
            if(!StringUtils.isEmpty(TokenUtil.getUserId(header))){
            }else{
                jsonResult.setMsg("用户未登录");
                jsonResult.setCode(JsonResult.LOGIN);
                return jsonResult;
            }
        }
        Map<String,Object> returnmap = new HashMap<>();
        try {
            LmOrder lmOrder = lmOrderService.findByOrderId(orderno);
            if(lmOrder!=null){
                LmMerchInfo byId = lmMerchInfoService.findById(String.valueOf(lmOrder.getMerchid()));
                LmOrderGoods lmOrderGoods = lmOrderGoodsService.findByOrderid(lmOrder.getId());
                returnmap.put("delay",lmOrder.getDelay());
                returnmap.put("freeze",byId.getFreeze());
                returnmap.put("num",lmOrderGoods.getGoodnum());
                returnmap.put("commentstatus",lmOrder.getCommentstatus());
                returnmap.put("couponPrice",lmOrder.getCoupon_price());
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
                                returnmap.put("coupons",lmOrder.getTotalprice().subtract(lmOrder.getRealpayprice()).subtract(lmOrder.getRealexpressprice()));
                            }else{
                                //一口价
                                returnmap.put("spec","");
                                returnmap.put("goodname",good.getName());
                                returnmap.put("thumb",good.getImg());
                                returnmap.put("price",good.getPrice());
                                returnmap.put("coupons",lmOrder.getTotalprice().subtract(lmOrder.getRealpayprice()).subtract(lmOrder.getRealexpressprice()));
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
                        returnmap.put("coupons",lmOrder.getTotalprice().subtract(lmOrder.getRealpayprice()).subtract(lmOrder.getRealexpressprice()));
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
                returnmap.put("violate",lmOrder.getViolate());
                returnmap.put("imid", lmOrder.getMemberid());
                returnmap.put("imname",lmOrder.getPaynickname());
                returnmap.put("immemberid", lmMerchInfo.getMember_id());
                returnmap.put("immembername",lmMerchInfo.getStore_name());
                returnmap.put("merId",lmMerchInfo.getId());
                returnmap.put("orderid",lmOrder.getOrderid());
                returnmap.put("type",lmOrder.getType());
                returnmap.put("id",lmOrder.getId());
                returnmap.put("createtime",DateUtils.sdf_yMdHms.format(lmOrder.getCreatetime()));
                LmMember lmMember = lmMemberService.findById(String.valueOf(lmMerchInfo.getMember_id()));
                if(lmMember!=null){
                    returnmap.put("userName",lmMember.getNickname());
                    returnmap.put("userImg",lmMember.getAvatar());
                }else {
                    returnmap.put("userName",null);
                    returnmap.put("userImg",null);
                }
                ExpressUtil expressUtil = new ExpressUtil();
                String result = expressUtil.synQueryData(lmOrder.getExpress(), lmOrder.getExpresssn(),"","","",0);
                returnmap.put("expressinfo",result);
                LmOrderRefundLog refund= lmMerchOrderService.getRefundLog(lmOrder.getRefundid());
                if(refund!=null){
                    returnmap.put("refundtype",refund.getType());
                    returnmap.put("refundstatus",refund.getStatus());
                    returnmap.put("refusal_instructions",refund.getRefusal_instructions());
                    returnmap.put("refundtext",refund.getText());
                }
                if(!StringUtils.isEmpty(lmOrder.getStatus())){
                    if(lmOrder.getStatus().equals("0")){
                        Long aLong = timeLong(lmOrder.getType(), lmOrder.getIslivegood(), lmOrder.getCreatetime(),lmOrder.getMerchid(),lmOrder.getDelay());
                        returnmap.put("countdown",aLong);
                    }else {
                        Long aLong = 0l;
                        returnmap.put("countdown",aLong);
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

    public Long  timeLong(Integer type,Integer isLiveGood,Date times,int merId,int delay) {
        Long longTime=0l;
        Date date=new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(times);
        if(delay==1){
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) +3);
        }
        if(isLiveGood==0){
            if(merId==363){
                calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + 5);
                Date time = calendar.getTime();
                longTime =time.getTime()/1000-date.getTime()/1000;
            }else {
                if(type==1){
                    calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + 30);
                    Date time = calendar.getTime();
                    longTime =time.getTime()/1000-date.getTime()/1000;
                }else  if(type==2){
                    calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 2);
                    Date time = calendar.getTime();
                    longTime =time.getTime()/1000-date.getTime()/1000;
                }
            }
        }else if(isLiveGood==1){
            if(type==1){
                calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + 5);
                Date time = calendar.getTime();
                longTime =time.getTime()/1000-date.getTime()/1000;
            }else  if(type==2){
                calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 2);
                Date time = calendar.getTime();
                longTime =time.getTime()/1000-date.getTime()/1000;
            }else if(type==3){
                calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + 5);
                Date time = calendar.getTime();
                longTime =time.getTime()/1000-date.getTime()/1000;
            }
        }
        Long min = 0l;
        if(longTime.longValue()<min.longValue()){
            longTime=0l;
        }
        return longTime;
    }


    /**
     * 订单取消
     *
     * @param id
     * @return
     */
    @RequestMapping("/cancelTheOrder")
    @ResponseBody
    @ApiOperation(value = "订单取消",notes = "订单取消接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单Id", dataType = "String",paramType = "query")
    })
    public JsonResult deleteorder(HttpServletRequest request, String id){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            LmOrder lmOrder = lmOrderService.findById(id);
            if(!("0").equals(lmOrder.getStatus())){
                jsonResult.setMsg("订单不可取消");
                jsonResult.setCode(JsonResult.ERROR);
                return jsonResult;
            }
            lmOrder.setStatus("7");
            lmOrderService.updateService(lmOrder);
            LmConpouMember lmConpouMember = lmCouponMemberService.findByOrderId(lmOrder.getId());
            if(lmConpouMember!=null){
                lmConpouMember.setCanUse(1);
                lmConpouMember.setOrderId(0);
                lmCouponMemberService.updateService(lmConpouMember);
            }
            LmOrderGoods lmOrderGoods = lmOrderGoodsService.findByOrderid(Integer.parseInt(id));
            if(lmOrderGoods.getGoodstype()==0){
                Good good = goodService.findById(lmOrderGoods.getGoodid());
                if(good!=null){
                    good.setStock(good.getStock()+lmOrderGoods.getGoodnum());
                    good.setState(1);
                    goodService.updateGoods(good);
                }
            }else {
                LivedGood livedGood = goodService.findLivedGood(lmOrderGoods.getGoodid());
                if(livedGood!=null){
                    livedGood.setStatus(0);
                    goodService.updateLivedGood(livedGood);
                }
            }
            LmFalsify falsify = lmFalsifyService.isFalsify(String.valueOf(lmOrder.getMemberid()), String.valueOf(lmOrderGoods.getGoodid()), String.valueOf(lmOrderGoods.getGoodstype()));
            if(falsify!=null){
                falsify.setType(1);
                falsify.setStatus(3);
                lmFalsifyService.updateService(falsify);
            }
            jsonResult.setMsg("订单取消成功，期待您的再次购物");
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }

}
