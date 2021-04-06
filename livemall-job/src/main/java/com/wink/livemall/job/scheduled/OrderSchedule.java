package com.wink.livemall.job.scheduled;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.wink.livemall.coupon.dto.LmConpouMember;
import com.wink.livemall.coupon.dto.LmCouponLog;
import com.wink.livemall.coupon.dto.LmCoupons;
import com.wink.livemall.coupon.service.LmCouponMemberService;
import com.wink.livemall.coupon.service.LmCouponsService;
import com.wink.livemall.goods.dto.Good;
import com.wink.livemall.goods.dto.LivedGood;
import com.wink.livemall.goods.dto.LmGoodAuction;
import com.wink.livemall.goods.dto.LmShareGood;
import com.wink.livemall.goods.service.GoodService;
import com.wink.livemall.job.comment.CommentService;
import com.wink.livemall.job.utils.DateUtils;
import com.wink.livemall.job.utils.HttpClient;
import com.wink.livemall.job.utils.PayUtil;
import com.wink.livemall.job.utils.WeixinpayUtil;
import com.wink.livemall.live.dto.LmLive;
import com.wink.livemall.live.service.LmLiveGoodService;
import com.wink.livemall.live.service.LmLiveService;
import com.wink.livemall.live.util.QiniuUtil;
import com.wink.livemall.member.dto.*;
import com.wink.livemall.member.service.*;
import com.wink.livemall.merch.dto.LmMerchInfo;
import com.wink.livemall.merch.dto.LmMerchMarginLog;
import com.wink.livemall.merch.service.LmMerchInfoService;
import com.wink.livemall.merch.service.LmMerchMarginLogService;
import com.wink.livemall.order.dto.*;
import com.wink.livemall.order.service.*;

import com.wink.livemall.sys.setting.dto.Configs;
import com.wink.livemall.sys.setting.service.ConfigsService;
import com.wink.livemall.utils.sms.SmsUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import static org.springframework.util.StringUtils.isEmpty;

@Component
public class OrderSchedule {
    @Autowired
    private GoodService goodService;
    @Autowired
    private LmOrderService orderService;
    @Autowired
    private LmOrderGoodsService lmOrderGoodsService;
    @Autowired
    private LmMemberService lmMemberService;
    @Autowired
    private LmMemberFollowService lmMemberFollowService;
    @Autowired
    private ConfigsService configsService;
    @Autowired
    private LmMerchInfoService lmMerchInfoService;
    @Autowired
    private LmMemberLevelService lmMemberLevelService;
    @Autowired
    private LmMemberAddressService lmMemberAddressService;
    @Autowired
    private LmOrderLogService lmOrderLogService;
    @Autowired
    private LmLiveService lmLiveService;
    @Autowired
    private LmPayLogService lmPayLogService;
    @Autowired
    private LmFalsifyService lmFalsifyService;
    @Autowired
    private LmFalsifyRefundReasonService  lmFalsifyRefundReasonService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private LmCouponMemberService lmCouponMemberService;
    @Autowired
    private LmCouponsService lmCouponsService;
    @Autowired
    private LmMerchOrderService lmMerchOrderService;
    @Autowired
    private LmMerchMarginLogService lmMerchMarginLogService;
    @Autowired
    private CommissionLogService commissionLogService;
    @Autowired
    private AgencyInfoService agencyInfoService;
    @Autowired
    private ForwardUserService forwardUserService;
    /**
     * 开始寻找拍卖结束未生成订单的拍品,生产订单或者流拍
     */
    @Scheduled(fixedDelay = 1000*30)
    public void findwaitgoodgenerOrder() throws Exception {
        System.out.println("开始寻找拍卖结束未生成订单的拍品");
        List<Good> list = goodService.findwaitGoodInfo(new Date());
        Integer maxId = orderService.findMaxId();
        String datetime = DateUtils.sdfyMdHm.format(new Date());
        Configs configs =configsService.findByTypeId(Configs.type_pay);
        Map map =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());

        String prfix=map.get("msgSrcId")+"";
        for(Good good:list){
            //查看延迟时间是否结束
            if(System.currentTimeMillis()>1000+good.getAuction_end_time().getTime()){
                //根据商品 查询商品出价最高的用户
                List<LmGoodAuction> lmGoodAuctionList =goodService.findAuctionlistByGoodid2(good.getId(),0);
                LmOrderGoods aloneOrderGoods=lmOrderGoodsService.findByGoodsid0(good.getId());
                if(aloneOrderGoods==null) {
                    if (lmGoodAuctionList.size() > 0) {
                        LmGoodAuction auction = lmGoodAuctionList.get(0);
                        auction.setStatus("2");
                        goodService.updateAuctionService(auction);
                        int memberid = auction.getMemberid();
                        List<Map<String,Object>> lmCouponsList = lmCouponsService.orderCouponList(memberid,String.valueOf(auction.getPrice()),
                                "0","2", String.valueOf(good.getMer_id()));
                        LmConpouMember lmConpouMember  =null;
                        BigDecimal couponValue=new BigDecimal(0);
                        if(lmCouponsList!=null&&lmCouponsList.size()>0){
                            Map<String,Object> lists=lmCouponsList.get(0);
                            Number num=(Number)lists.get("id");
                            int couponid = num.intValue();
                            lmConpouMember  = lmCouponMemberService.findById(couponid);
                            couponValue=(BigDecimal)lists.get("couponValue");
                        }
                            LmMember lmMember = lmMemberService.findById(memberid + "");
                            good.setAuction_status(1);
                            LmOrder lmOrder = new LmOrder();
                            lmOrder.setOrderid(prfix + datetime + (int) (Math.random() * (9999 - 1000) + 1000));
                            lmOrder.setStatus("0");
                            lmOrder.setType(2);
                            lmOrder.setSend_type(1);
                            lmOrder.setPaynickname(lmMember.getNickname());
                            lmOrder.setCreatetime(new Date());
                            lmOrder.setMerchid(good.getMer_id());
                        if(lmCouponsList!=null&&lmCouponsList.size()>0){
                            lmOrder.setCoupon_id(lmConpouMember.getCouponId());
                            lmOrder.setCoupon_price(couponValue);
                        }
                        List<LmMemberAddress> addresses = lmMemberAddressService.findByMemberid(memberid);
                        if (addresses != null && addresses.size() > 0) {
                            LmMemberAddress address = addresses.get(0);
                            lmOrder.setChargeaddress(address.getProvince() + address.getCity() + address.getDistrict() + address.getAddress_info());
                            lmOrder.setChargename(address.getRealname());
                            lmOrder.setChargephone(address.getMobile());
                        }
                            lmOrder.setMemberid(memberid);
                            lmOrder.setDeposit_type(0);
                            lmOrder.setTotalprice(auction.getPrice());
                        //判断是否小于0
                        BigDecimal min=new BigDecimal(0);
                            if (good.getFreeshipping() == 1) {
                                lmOrder.setRealexpressprice(new BigDecimal(0));
                                if(couponValue.compareTo(min)>0){
                                    BigDecimal price=auction.getPrice();
                                    BigDecimal  realPayPrice= price.subtract(couponValue);
                                    if(realPayPrice.compareTo(min)>0){
                                        lmOrder.setRealpayprice(realPayPrice);
                                    }else {
                                        lmOrder.setRealpayprice(new BigDecimal(0.01).setScale(2,BigDecimal.ROUND_HALF_UP));
                                    }
                                }else {
                                    lmOrder.setRealpayprice(auction.getPrice());
                                }
                            } else {
                                lmOrder.setRealexpressprice(good.getExpressprice());
                                if(couponValue.compareTo(min)>0){
                                    BigDecimal price=auction.getPrice().add(good.getExpressprice());
                                    BigDecimal  realPayPrice= price.subtract(couponValue);
                                    if(realPayPrice.compareTo(min)>0){
                                        lmOrder.setRealpayprice(realPayPrice);
                                    }else {
                                        lmOrder.setRealpayprice(good.getExpressprice());
                                    }
                                }else {
                                    lmOrder.setRealpayprice(auction.getPrice().add(good.getExpressprice()));
                                }
                            }
                            orderService.insertService(lmOrder);
                            LmOrderGoods lmOrderGoods = new LmOrderGoods();
                            lmOrderGoods.setGoodid(good.getId());
                            lmOrderGoods.setGoodnum(1);
                            lmOrderGoods.setGoodstype(0);
                            lmOrderGoods.setGoodprice(auction.getPrice());
                            lmOrderGoods.setOrderid(lmOrder.getId());
                            lmOrderGoodsService.insertService(lmOrderGoods);
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
                          //发送通知 同时用户竞拍成功
                            LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(String.valueOf(lmOrder.getMerchid()));
                            Map<String, Object> memberinfo = new HashMap<>();
                            memberinfo.put("levelname", "");
                            memberinfo.put("levelcode", "");
                            String msg = "您拍卖的商品：-" + good.getTitle() + "-已竞拍成功,\n可以进入个人中心的待付款里进行付款操作,未支付会导致您产生违约记录";
                            HttpClient httpClient = new HttpClient();
                            memberinfo.put("nickname", lmMerchInfo.getStore_name());
                            httpClient.login(lmMerchInfo.getAvatar(), lmMerchInfo.getStore_name(), new Gson().toJson(memberinfo));
                            SmsUtils.sendValidCodeMsgs(lmMember.getMobile(),good.getTitle(),"SMS_213550266");
                            httpClient.send("拍品消息", memberid + "", msg);
                            LmOrderLog lmOrderLog = new LmOrderLog();
                            lmOrderLog.setOrderid(lmOrder.getOrderid());
                            lmOrderLog.setOperatedate(new Date());
                            lmOrderLog.setOperate("订单生产");
                            lmOrderLogService.insert(lmOrderLog);
                        //给其他人退款
                        List<Map<String, Object>> autoRefundFalsify = lmFalsifyService.autoRefundFalsify(good.getId(),0);
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
                                    String msgs = "您缴纳该:"+good.getTitle()+"\n商品的违约金已自动退回";
                                    httpClient.send("拍品消息",memberId+"",msgs);
                                }
                            }
                        }
                    } else {
                        //设置流拍
                        good.setAuction_status(2);
                        List<Map<String, Object>> autoRefundFalsify = lmFalsifyService.autoRefundFalsify(good.getId(),0);
                        if(autoRefundFalsify!=null&&autoRefundFalsify.size()>0){
                            for(Map<String,Object> falsifyLists:autoRefundFalsify){
                                int memberId=(int) falsifyLists.get("member_id");
                                String falsifyId=(String) falsifyLists.get("falsify_id");
                                BigDecimal falsify=(BigDecimal)falsifyLists.get("falsify");
                                falsify=falsify.multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP);
                               String falsifyP=String.valueOf(falsify);
                               commentService.autoRefundFalsify(falsifyId,falsifyP);
                                String msg = "您缴纳该:"+good.getTitle()+"\n商品的违约金已自动退回";
                                HttpClient httpClient = new HttpClient();
                                httpClient.send("拍品消息",memberId+"",msg);
                            }
                        }
                    }
                }
                //商品设置下架
                good.setState(0);
                good.setWarehouse("3");
                goodService.updateGoods(good);
            }
        }
    }


    //拍卖到期之前15分钟提示
   @Scheduled(fixedDelay = 1000*60)
    public void tishigood(){
        System.out.println("寻找前15分钟拍卖时间到时的商品");
        Calendar calendar = Calendar.getInstance();
        String tishigoodtime="14";
        calendar.setTime(new Date());
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + Integer.parseInt(tishigoodtime));
        //获取n小时前的时间
        Date nowdate =calendar.getTime();
        System.out.println(nowdate);
       Date nowdate1=new Date();

       long c=nowdate1.getTime()-1000*60*2;//1000*60*1 是1分钟
       Date olddate1=new Date(c);
       System.out.println(olddate1);


       String oldgoodtime="15";
       calendar.setTime(new Date());
       calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + Integer.parseInt(oldgoodtime));
       Date olddate =calendar.getTime();
       System.out.println(olddate);

        List<Good> list = goodService.findwaitGoodInfo15(nowdate,olddate);

        Configs configs =configsService.findByTypeId(Configs.type_pay);
        Map map =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
        String prfix=map.get("msgSrcId")+"";
        for(Good good:list){
                System.out.println(good.getAuction_end_time());
                //根据商品 查询商品出价最高的用户
                List<LmGoodAuction> lmGoodAuctionList =goodService.findAuctionlistByGoodid2(good.getId(),0);
                if(lmGoodAuctionList.size()>0){
                    LmGoodAuction auction = lmGoodAuctionList.get(0);
                    int memberid = auction.getMemberid();
                    List<LmMemberAddress> addresses = lmMemberAddressService.findByMemberid(memberid);
                        //发送通知 同时用户竞拍成功
                        String msg = "您拍卖的商品:"+good.getTitle()+"\n还有不到15分钟剩余时间就下架了，请及时关注,可在个人中心的我的竞拍中及时查看状态";
//                    pushmsgService.send(0,msg,"2",memberid,0);
                        HttpClient httpClient = new HttpClient();
                        httpClient.send("拍品消息",memberid+"",msg);
                    System.out.println(good.getTitle()+memberid);
                }

        }

    }


    /*0 0/10 * * * ?*/

    //定时下架拍卖到时商品
    @Scheduled(fixedDelay = 1000*30)
    public void closegood(){
        System.out.println("寻找拍卖时间到时的商品");
        List<Good>list = goodService.findAllwaitGoodInfo(new Date());
        for(Good good:list){
            if(System.currentTimeMillis()>good.getDelaytime()+good.getAuction_end_time().getTime()) {
                //商品设置下架
                good.setState(0);
                good.setWarehouse("3");
                goodService.updateGoods(good);
            }
        }
    }


    @Scheduled(cron = "0 1 0 */1 * ?")
    public void closeCoupon(){
        System.out.println("关闭时间到期的优惠券");
        List<LmCoupons> coupon = lmCouponsService.findMerchCouponBys();
        if(null!=coupon&&coupon.size()>0){
            for (LmCoupons coupons:coupon){

                Date useEndTime =coupons.getUseEndTime();
                Date date =new Date();
                if(date.getTime()>=useEndTime.getTime()){
                    coupons.setStatus(1);
                    lmCouponsService.updateService(coupons);
                }
            }
        }
    }


    //通知预展的关注直播的人
    @Scheduled(cron = "0 6 0/1 * * ?")
    public void livePreview(){
        System.out.println("通知预展的关注直播的人,并且检查到期没有开直播的直播间");
        Calendar calendar = Calendar.getInstance();
        HttpClient httpClient = new HttpClient();
        String newTime="1";
        calendar.setTime(new Date());
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.HOUR_OF_DAY) + Integer.parseInt(newTime));
        List<LmLive> livePreview = lmLiveService.findLivePreview();
        if(livePreview!=null&&livePreview.size()>0){
            for(LmLive lmLive:livePreview){
                Date preview_time = lmLive.getPreview_time();
                Date time = calendar.getTime();
                LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(String.valueOf(lmLive.getMerch_id()));
                LmMember lmMember = lmMemberService.findById(String.valueOf(lmMerchInfo.getMember_id()));
                if(System.currentTimeMillis()<preview_time.getTime()&&preview_time.getTime()<time.getTime()){
                    String msg = "您的店铺："+lmMerchInfo.getStore_name()+"\n还有一个小时不到的时间就到达您预设的直播时间，请及时开播，您的粉丝正在等你！！";
                    httpClient.send("拍品消息", lmMerchInfo.getMember_id()+"",msg);
                    List<LmMemberFollow> byMerchidAndType = lmMemberFollowService.findByMerchidAndType(0, lmLive.getMerch_id());
                    if(byMerchidAndType!=null&&byMerchidAndType.size()>0){
                        for(LmMemberFollow lmMemberFollow:byMerchidAndType){
                            String msgs = "您关注的直播"+lmLive.getName()+"\n还有一个小时不到的时间就要开播了，请及时关注，主播正在准备中····";
                            httpClient.send("拍品消息",lmMemberFollow.getMember_id()+"",msgs);
                        }
                    }
                }
                if(System.currentTimeMillis()>preview_time.getTime()){
                    String msg ="";
                    int violate = lmLive.getViolate();
                    lmLive.setIsstart(0);
                    if(violate==0){
                       msg = "您的店铺："+lmMerchInfo.getStore_name()+"\n由于未及时开播，并超过五分钟，再有一次超时扣除1000元保证金，请及时关注";
                        lmLive.setViolate(violate+1);
                        SmsUtils.sendValidCodeMsgs(lmMember.getMobile(),lmMerchInfo.getStore_name(),"SMS_213744401");
                    }else {
                        boolean odd = isOdd(violate);
                        if(odd){
                            lmLive.setViolate(violate+1);
                            msg = "您的店铺："+lmMerchInfo.getStore_name()+"\n由于两次未及时开播，并超过五分钟，导致扣除1000元保证金，如有疑问可联系客服";
                            LmMerchMarginLog lmMerchMarginLog=new LmMerchMarginLog();
                            lmMerchMarginLog.setMargin_sn(null);
                            lmMerchMarginLog.setPaystatus(0);
                            lmMerchMarginLog.setState(1);
                            lmMerchMarginLog.setMer_id(lmMerchInfo.getId());
                            lmMerchMarginLog.setType(1);
                            lmMerchMarginLog.setPrice(new BigDecimal(1000));
                            lmMerchMarginLog.setDescription("由于您未按时开启直播间，扣除保证金1000元");
                            lmMerchMarginLog.setCreate_time(new Date());
                            lmMerchMarginLogService.insert(lmMerchMarginLog);
                            SmsUtils.sendValidCodeMsgs(lmMember.getMobile(),lmMerchInfo.getStore_name(),"SMS_214525053");
                        }else {
                            lmLive.setViolate(violate+1);
                            msg = "您的店铺："+lmMerchInfo.getStore_name()+"\n由于未及时开播，并超过五分钟，再有一次超时扣除1000元保证金，请及时关注";
                            SmsUtils.sendValidCodeMsgs(lmMember.getMobile(),lmMerchInfo.getStore_name(),"SMS_213744401");
                        }
                    }
                    lmLiveService.updateService(lmLive);
                    httpClient.send("拍品消息", lmMerchInfo.getMember_id()+"",msg);
                }
            }
        }
    }

    public boolean isOdd(int number)
    {
        return number % 2 == 1;
    }

   //关闭到期的直播间，并通知续费
    @Scheduled(cron = "0 0 0 * * ?")
    public void liveExpire() {
        System.out.println("关闭到期的直播间，并通知续费");
        List<LmLive> liveExpire = lmLiveService.findLiveList();
        HttpClient httpClient = new HttpClient();
        if(liveExpire!=null&&liveExpire.size()>0){
            for (LmLive lmLive:liveExpire){
                LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(String.valueOf(lmLive.getMerch_id()));
                Date expireTime = lmLive.getEnd_time();
                if(lmMerchInfo.getType()==1){
                    if(System.currentTimeMillis()>expireTime.getTime()){
                        lmLive.setStatus(2);
                        lmLive.setIsstart(0);
                        lmLiveService.updateService(lmLive);
                        String msg  = "您的店铺："+lmMerchInfo.getStore_name()+"\n的直播间已到期,请及时续费";
                        httpClient.send("拍品消息", lmMerchInfo.getMember_id()+"",msg);
                    }
                }
            }
        }
    }



    //设置自动收货
    @Scheduled(fixedDelay = 1000*30)
    public void autogetgood() throws Exception {
        Configs tradconfigs =configsService.findByTypeId(Configs.type_trading);
        Map tradmap =  JSONObject.parseObject(tradconfigs.getConfig());
        //获取自动收货时间
        String gettime = tradmap.get("automatic_receiving_goods")+"";
        //当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - Integer.parseInt(gettime));
        //七天前时间
        Date olddate =calendar.getTime();
        //查询收货之间超过7天还没点击收货的订单设置已收货并添加积分
        List<LmOrder> list  = orderService.findListByDate(olddate);
        for(LmOrder order:list){
           // 判定用户是否点击延迟收货
            if(order.getDelay()==2){
                calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 7);
                Date  delayTime=calendar.getTime();
                Date deliveryTime = order.getDeliverytime();
                if(delayTime.getTime()<deliveryTime.getTime()){
                    continue;
                }
            }
            order.setStatus("4");
            order.setFinishtime(new Date());
            orderService.updateService(order);
            //成长值处理
            LmMember lmMember = lmMemberService.findById(order.getMemberid()+"");
            int price = order.getRealpayprice().intValue();
            if(price>=12){
                int growvalue = price/12;
                lmMember.setGrowth_value(lmMember.getGrowth_value()+growvalue);
                lmMemberService.updateService(lmMember);
            }
            //成长值
            List<LmMemberLevel> levels = lmMemberLevelService.findAll();
            for(LmMemberLevel lmMemberLevel:levels){
                if(lmMember.getGrowth_value()>=lmMemberLevel.getGrowth_value()){
                    lmMember.setLevel_id(lmMemberLevel.getId());
                }
            }
            //交易成功率
            LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(order.getMerchid()+"");
            //查询所有该商户的订单
            List<Map<String, String>> orderList = orderService.findListByMerchidByApi(order.getMerchid());
            Double successnum = 0.00;
            for(Map<String, String> map:orderList){
                String status = map.get("status");
                if("3".equals(status)||"4".equals(status)){
                    successnum++;
                }
            }
            lmMerchInfo.setSuccessper(successnum/orderList.size());
            lmMemberService.updateService(lmMember);
            //将订单金额 打到商户余额上
            BigDecimal realPrice = order.getRealpayprice();
            if(order.getPaystatus().equals("3")){
                realPrice = realPrice.multiply(new BigDecimal(94.4)).divide(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_DOWN);
            }else {
                realPrice = realPrice.multiply(new BigDecimal(94.7)).divide(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_DOWN);
            }
            LmOrderGoods lmOrderGoods = lmOrderGoodsService.findByOrderid(order.getId());
            ForwardUser forwardUser = forwardUserService.findListByUserId(order.getMemberid());
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
                        commissionLog.setOrder_id(order.getId());
                        commissionLog.setCommission(byId.getCommission());
                        commissionLogService.insertCommissionLog(commissionLog);
                        LmMember Forward = lmMemberService.findById(String.valueOf(order.getForward()));
                        Forward.setBlance(Forward.getBlance().subtract(byId.getCommission()));
                        Forward.setCredit2(Forward.getCredit2().add(byId.getCommission()));
                        lmMemberService.updateService(Forward);
                    }
                }
            }
            lmMerchInfo.setCredit((null==lmMerchInfo.getCredit()?BigDecimal.ZERO:lmMerchInfo.getCredit()).add(realPrice));
            lmMerchInfoService.updateService(lmMerchInfo);
            LmOrderLog lmOrderLog = new LmOrderLog();
            lmOrderLog.setOrderid(order.getOrderid());
            lmOrderLog.setOperatedate(new Date());
            lmOrderLog.setOperate("订单确认收货");
            lmOrderLogService.insert(lmOrderLog);
            //自动退款
            List<Map<String, Object>> autoRefundFalsify = lmFalsifyService.autoRefundFalsify(lmOrderGoods.getGoodid(),lmOrderGoods.getGoodstype());
              if( autoRefundFalsify!=null&&autoRefundFalsify.size()>0){
                 for(Map<String,Object> falsifyLists:autoRefundFalsify){
                     String falsifyId= (String)falsifyLists.get("falsify_id");
                     BigDecimal falsify= (BigDecimal) falsifyLists.get("falsify");
                     falsify=falsify.multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP);
                    String falsifyP=String.valueOf(falsify);
                    commentService.autoRefundFalsify(falsifyId,falsifyP);
                 }
            }
        }
    }



    //自动关闭时间内未支付的订单
    @Scheduled(fixedDelay = 1000*30)
    public void autocloseOrder(){
        System.out.println("自动关闭时间内未支付的订单");
        //查询所有未支付订单
        List<LmOrder> lmordersList =orderService.findOrderListByStatus(0);
        Configs tradconfigs =configsService.findByTypeId(Configs.type_trading);
        Calendar calendar = Calendar.getInstance();
        //获取n小时前的时间
        Date olddate =calendar.getTime();
        if(lmordersList.size()>0){
        for(LmOrder order:lmordersList){
            if(order.getType()==3){
                if(order.getPorderid()!=0&&order.getDeposit_type()!=1){
                    String sharepaytime = "30";//30秒
                    //当前时间
                    calendar.setTime(new Date());
                    if(order.getDelay()==1){
                        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 3);
                    }
                    calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) - Integer.parseInt(sharepaytime));
                    //获取n小时前的时间
                    olddate =calendar.getTime();
                    //订单创建时间
                    Date createtime = order.getCreatetime();
                    if(createtime!=null){
                        if(olddate.getTime()>createtime.getTime()){
                            //订单失效
                            order.setStatus("-1");
                            orderService.updateService(order);
                        }
                    }
                }
            }else if(order.getType()==2){
                //订单创建时间
                Date createtime = order.getCreatetime();
                String paytime = "2";//2天
                //当前时间
                calendar.setTime(new Date());
                if(order.getDelay()==1){
                    calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 3);
                }
               calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - Integer.parseInt(paytime));
               //分钟
                //calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - Integer.parseInt(paytime));
                //获取n小时前的时间
                olddate =calendar.getTime();
                if(createtime!=null){
                    if(olddate.getTime()>createtime.getTime()){
                        //订单失效
                        order.setStatus("-1");
                        orderService.updateService(order);
                        //违约金变不可以退
                        LmOrderGoods lmOrderGoods = lmOrderGoodsService.findByOrderid(order.getId());
                        LmFalsify isfalsify = lmFalsifyService.isFalsify(String.valueOf(order.getMemberid()), String.valueOf(lmOrderGoods.getGoodid()), String.valueOf(order.getIslivegood()));
                        if(isfalsify!=null){
                            isfalsify.setType(1);
                            isfalsify.setStatus(3);
                            lmFalsifyService.updateService(isfalsify);
                        }
                        BigDecimal realPrice = isfalsify.getFalsify();
                        if(isfalsify.getPaystatus()==3){
                            realPrice = realPrice.multiply(new BigDecimal(94.4)).divide(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_DOWN);
                        }else {
                            realPrice = realPrice.multiply(new BigDecimal(94.7)).divide(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_DOWN);
                        }
                        LmMerchInfo byId = lmMerchInfoService.findById(String.valueOf(isfalsify.getMerch_id()));
                        if(byId!=null){
                            if(byId.getCredit()==null){
                                byId.setCredit(realPrice);
                            }else {
                                byId.setCredit(byId.getCredit().add(realPrice).setScale(2,BigDecimal.ROUND_DOWN));
                            }
                            lmMerchInfoService.updateService(byId);
                        }
                        //恢复优惠券
                        LmConpouMember lmConpouMember = lmCouponMemberService.findByOrderId(order.getId());
                        if(lmConpouMember!=null){
                            lmConpouMember.setCanUse(1);
                            lmConpouMember.setOrderId(0);
                            lmCouponMemberService.updateService(lmConpouMember);
                        }
                    }
                }
            } else {
                //订单创建时间
                Date createtime = order.getCreatetime();
                String goodTime = "30";//30分钟
                String liveGoodTime = "5";//5分钟
                //当前时间
                calendar.setTime(new Date());
                if(order.getDelay()==1){
                    calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 3);
                }
                //获取n小时前的时间
                if(createtime!=null){
                    if(order.getIslivegood()==0){
                        if(order.getMerchid()==363){
                            calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - Integer.parseInt(liveGoodTime));
                            olddate =calendar.getTime();
                            if(olddate.getTime()>createtime.getTime()){
                                //订单失效
                                order.setStatus("-1");
                                orderService.updateService(order);
                                if(order.getType()==1){
                                    if(!order.getPaystatus().equals("3")){
                                        //获取订单关联商品
                                        LmOrderGoods lmOrderGoods = lmOrderGoodsService.findByOrderid(order.getId());
                                        Good good = goodService.findById(lmOrderGoods.getGoodid());
                                        if(good!=null){
                                            good.setStock(good.getStock()+lmOrderGoods.getGoodnum());
                                            good.setState(1);
                                            good.setWarehouse("2");
                                            goodService.updateGoods(good);
                                        }
                                    }
                                }
                            }
                        }else {
                        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - Integer.parseInt(goodTime));
                        olddate =calendar.getTime();
                        if(olddate.getTime()>createtime.getTime()){
                            //订单失效
                            order.setStatus("-1");
                            orderService.updateService(order);
                            if(order.getType()==1){
                                //获取订单关联商品
                                if(!order.getPaystatus().equals("3")){
                                LmOrderGoods lmOrderGoods = lmOrderGoodsService.findByOrderid(order.getId());
                                Good good = goodService.findById(lmOrderGoods.getGoodid());
                                if(good!=null){
                                    good.setStock(good.getStock()+lmOrderGoods.getGoodnum());
                                    good.setState(1);
                                    good.setWarehouse("2");
                                    goodService.updateGoods(good);
                                 }
                                }
                            }
                        //恢复优惠券
                            LmConpouMember lmConpouMember = lmCouponMemberService.findByOrderId(order.getId());
                            if(lmConpouMember!=null){
                                lmConpouMember.setCanUse(1);
                                lmConpouMember.setOrderId(0);
                                lmCouponMemberService.updateService(lmConpouMember);
                            }
                        }
                        }
                    }else {
                        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - Integer.parseInt(liveGoodTime));
                        olddate =calendar.getTime();
                        if(olddate.getTime()>createtime.getTime()){
                            //订单失效
                            order.setStatus("-1");
                            orderService.updateService(order);
                            //恢复优惠券
                            LmConpouMember lmConpouMember = lmCouponMemberService.findByOrderId(order.getId());
                            if(lmConpouMember!=null){
                                lmConpouMember.setCanUse(1);
                                lmConpouMember.setOrderId(0);
                                lmCouponMemberService.updateService(lmConpouMember);
                            }
                        }
                    }
                }
            }

        }
        }
    }


    //定时查看直播间的商品推送是否生成订单
    @Scheduled(fixedDelay = 1000*10)
    public void movementImLive(){
        List<LmLive> list = lmLiveService.findLiveedLive();
        System.out.println("定时查看直播间的商品推送是否生成订单");
        HttpClient httpClient = new HttpClient();
        if(list.size()>0){
            for(LmLive lmLive:list){
                List<LivedGood> livedGoods = goodService.movementImLive(lmLive.getId());
                if(livedGoods!=null&&livedGoods.size()>0){
                    for (int i = 0; i <livedGoods.size() ; i++) {
                        Date date=new Date();
                            LivedGood livedGood = livedGoods.get(i);
                        if(date.getTime()>livedGood.getEndtime().getTime()){
                            List<LmGoodAuction> lmGoodAuctionList =goodService.findAuctionlistByGoodid2(livedGood.getId(),1);
                            LmOrderGoods aloneOrderGoods=lmOrderGoodsService.findByGoodsid1(livedGood.getId());
                            if(aloneOrderGoods==null){
                                if(lmGoodAuctionList!=null&&lmGoodAuctionList.size()>0){
                                    System.out.println("发送群组+++++++++++++++++++++++++++++++++"+20);
                                    Map<String,Object> msgmap = new HashMap<>();
                                    msgmap.put("liveGoodId",livedGood.getId());
                                    msgmap.put("liveId",lmLive.getId());
                                    httpClient.sendgroup(lmLive.getLivegroupid(), new Gson().toJson(msgmap), 20);
                                }else {
                                    livedGood.setStatus(1);
                                    goodService.updateLivedGood(livedGood);
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    //定时设置直播人数
    @Scheduled(fixedDelay = 1000*30)
    public void setlivenum(){
        System.out.println("定时设置直播人数");
        //读取推流配置
        Configs configs = configsService.findByTypeId(Configs.type_flow);
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
            String streamKeyPrefix = "1";
            String hubname = configmap.get("hubname")+"";
            QiniuUtil qiniuUtil = new QiniuUtil(access_key,access_secret,streamKeyPrefix,livepushurl,livereadurl,livereadurl2,livereadurl3,hubname);
            String body = qiniuUtil.doQiNiuGet(hubname);
            System.out.println(body);
            Map livemap = JSONObject.parseObject(body);
            Map streams = (Map) livemap.get("streams");
            System.out.println(streams);
            List<LmLive> list = lmLiveService.findLiveedLive();
            for(LmLive lmLive:list){
                if(streams.get(lmLive.getId()+"")!=null){
                    Map countinfo = (Map) streams.get(lmLive.getId()+"");
                    System.out.println(countinfo);
                    if(countinfo!=null){
                        int count = (int) countinfo.get("count");
                        lmLive.setWatchnum(count);
                        lmLiveService.updateService(lmLive);
                    }
                }
            }
        }
    }


    //关闭直播状态不正常的直播间
    @Scheduled(fixedDelay = 1000*30)
    public void setlivestatus() {
        System.out.println("关闭直播状态不正常的直播间");
        //读取推流配置
        Configs configs = configsService.findByTypeId(Configs.type_flow);
        if (configs != null) {
            String config = configs.getConfig();
            Map configmap = JSONObject.parseObject(config);
            String access_key = configmap.get("access_key_id") + "";
            String access_secret = configmap.get("access_secret") + "";
            String hubname = configmap.get("hubname") + "";
            List<LmLive> list = lmLiveService.findLiveedLive();
            for(LmLive lmLive:list){
                QiniuUtil qiniuUtil = new QiniuUtil(access_key, access_secret, lmLive.getId()+"", "", "", "", "", hubname);
                boolean isliveed = qiniuUtil.LiveStatus();
                if(!isliveed){
                    HttpClient httpClient = new HttpClient();
                    httpClient.deleteGroup(lmLive.getLivegroupid());
                    lmLive.setLivegroupid("0");
                    lmLive.setIsstart(0);
                    lmLiveService.updateService(lmLive);
                }
            }
        }
    }


    //关闭合买过期商品
    @Scheduled(fixedDelay = 1000*30)
    public void closesharegood() throws Exception {
        List<LmShareGood> goodlist = goodService.findShareGood(new Date());
        for(LmShareGood good:goodlist){
            //商品设置下架
            good.setStatus(-1);
            goodService.updateShareGood(good);
            //查询合买过期商品的相关订单
            LmLive lmLive = lmLiveService.findbyId(good.getLiveid()+"");
            LmOrder porder =orderService.findTopOrder(good.getId(),lmLive.getMerch_id());
            List<LmOrder>  orders = orderService.findOrderListByPid(porder.getId());
            if(orders.size()!=good.getChipped_num()){
                for(LmOrder order:orders){
                    order.setStatus("-1");
                    orderService.updateService(order);
                    refund(order.getOrderid(),order.getTotalprice().multiply(new BigDecimal(100)).setScale(2).longValue()+"");
                }
                porder.setStatus("-1");
                orderService.updateService(porder);
            }
        }
    }

    //扫描未及时缴费的保证金，并冻结他的资产
    @Scheduled(cron = "0 0 * * * ?")
    public void LmMerMarginLog() throws Exception {
        System.out.println("扫描未及时缴费的保证金，并冻结他的资产");
        List<LmMerchMarginLog> violateAll = lmMerchMarginLogService.findViolateAll();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 3);
        Date time = calendar.getTime();
        HttpClient httpClient = new HttpClient();
        if(violateAll.size()>0){
            for(LmMerchMarginLog lmMerchMarginLog:violateAll){
                if(time.getTime()>lmMerchMarginLog.getCreate_time().getTime()){
                    LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(String.valueOf(lmMerchMarginLog.getMer_id()));
                     if(lmMerchInfo.getFreeze()==0){
                         LmMember lmMember = lmMemberService.findById(String.valueOf(lmMerchInfo.getMember_id()));
                         lmMerchInfo.setFreeze(1);
                         lmMerchInfoService.updateService(lmMerchInfo);
                         String msg="您的"+lmMerchInfo.getStore_name()+"店铺因未及时缴纳扣除的保证金，资产已被冻结，请及时缴纳，然后申请客服解冻";
                         httpClient.send("拍品消息",lmMerchInfo.getMember_id()+"",msg);
                         SmsUtils.sendValidCodeMsgs(lmMember.getMobile(),lmMerchInfo.getStore_name(),"SMS_214505130");
                     }
                }
            }
        }
    }



    //关闭未分料的总订单并且自动退款
    @Scheduled(fixedDelay = 1000*60*5)
    public void closeporder() {
        System.out.println("关闭未分料的总订单和子订单并且自动退款");
        List<LmOrder> lmOrders = orderService.findTopOrderList();
        for(LmOrder lmOrder:lmOrders){
            //判断是否超时
            long nowtiems = System.currentTimeMillis();
            if(lmOrder.getChippedtime()!=null){
                long cliptimes = lmOrder.getChippedtime().getTime();
                //超时3天
                if((nowtiems-cliptimes)>1000*3600*24*3){
                    lmOrder.setStatus("5");
                    List<LmOrder> childs = orderService.findOrderListByPid(lmOrder.getId());
                    for(LmOrder corder:childs){
                        corder.setStatus("5");
                        try {
                            refund(corder.getOrderid(),corder.getTotalprice().multiply(new BigDecimal(100)).setScale(2).longValue()+"");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        orderService.updateService(corder);
                    }
                    orderService.updateService(lmOrder);
                }
            }

        }
    }


    //未收货商户违约
    @Scheduled(cron = "0 0/30 * * * ?")
    public void violateOrder(){
        Calendar calendar = Calendar.getInstance();
        //未收货商户违约
        List<LmOrder> confiscateLmOrders = orderService.violateOrderOne(0);
        if(confiscateLmOrders.size()>0){
            for(LmOrder lmOrder:confiscateLmOrders){
                if(lmOrder.getPaytime()==null){
                    lmOrder.setPaytime(new Date());
                    orderService.updateService(lmOrder);
                }
                calendar.setTime(new Date());
                calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 3);
                Date calendarTime = calendar.getTime();
                if(calendarTime.getTime()>lmOrder.getPaytime().getTime()){
                    lmOrder.setViolate(1);
                    orderService.updateService(lmOrder);
                }
            }
        }
        List<LmOrder> LmOrders = orderService.violateOrderTwo(0);
        if(LmOrders.size()>0){
            for(LmOrder lmOrder:LmOrders){
                if(lmOrder.getPaytime()==null){
                    lmOrder.setPaytime(new Date());
                    orderService.updateService(lmOrder);
                }
                calendar.setTime(new Date());
                calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 10);
                Date calendarTime = calendar.getTime();
                if(calendarTime.getTime()>lmOrder.getPaytime().getTime()){
                    lmOrder.setViolate(3);
                    orderService.updateService(lmOrder);
                    LmMerchInfo byId = lmMerchInfoService.findById(String.valueOf(lmOrder.getMerchid()));
                    LmMember lmMember = lmMemberService.findById(String.valueOf(byId.getMember_id()));
                    if(byId.getFreeze()==0){
                        byId.setFreeze(1);
                        lmMerchInfoService.updateService(byId);
                        HttpClient httpClient=new HttpClient();
                        String msg = "您的"+byId.getStore_name()+"店铺\n总资产已被冻结";
                        httpClient.send("拍品消息",byId.getMember_id()+"",msg);
                        SmsUtils.sendValidCodeMsgs(lmMember.getMobile(),byId.getStore_name(),"SMS_213744405");
                    }
                }
            }
        }
        //退款中商户未处理
        List<LmOrder> backLmOrders = orderService.violateOrderOne(1);
        if(backLmOrders.size()>0){
            for(LmOrder lmOrder:backLmOrders){
                LmOrderRefundLog refundLogByorderid = lmMerchOrderService.getRefundLogByorderid(String.valueOf(lmOrder.getId()));
                calendar.setTime(new Date());
                calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 2);
                Date calendarTime = calendar.getTime();
                if(calendarTime.getTime()>refundLogByorderid.getCreatetime().getTime()){
                    lmOrder.setViolate(1);
                    orderService.updateService(lmOrder);
                }
            }
        }

        //违约金商家未处理
        List<LmFalsify> noFalsify = lmFalsifyService.findNoFalsify();
        if(noFalsify.size()>0){
            for(LmFalsify lmFalsify:noFalsify){
                LmFalsifyRefundReason refundReason = lmFalsifyRefundReasonService.findRefundReason(lmFalsify.getMember_id(), lmFalsify.getGood_id(), lmFalsify.getGoodstype());
                calendar.setTime(new Date());
                calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 2);
                Date calendarTime = calendar.getTime();
                if(calendarTime.getTime()>refundReason.getCreat_time().getTime()){
                    lmFalsify.setViolate(1);
                    lmFalsifyService.updateService(lmFalsify);
                }
            }
        }

    }



    public Map<String,Object> refund(String merOrderId,
                                    String refundAmount) throws Exception {
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
                    return returnmap;
                }
            }
        }
        LmOrder lmOrder = orderService.findByOrderId(merOrderId);
        if(!"1".equals(lmOrder.getPaystatus())){
            Configs configs =configsService.findByTypeId(Configs.type_pay);
            Map map =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
            returnmap.put("config",configs.getConfig());
            //组织请求报文
            net.sf.json.JSONObject json = new net.sf.json.JSONObject();
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
                resultStr = net.sf.json.JSONObject.fromObject(resultMap).toString();
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
                    System.out.println("content:"+content.toString());
                    //转换成json对象
                    com.alibaba.fastjson.JSONObject respJson = JSON.parseObject(content.toString());
                    String resultCode = respJson.getString("errCode");
                    resultMap.put("errCode",resultCode);
                    resultMap.put("respStr",respJson.toString());
                    resultStr = net.sf.json.JSONObject.fromObject(resultMap).toString();
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
                resultStr = net.sf.json.JSONObject.fromObject(resultMap).toString();
                returnmap.put("returninfo",resultStr);
            }finally {
                if (out != null) {
                    out.close();
                }
                httpURLConnection.disconnect();
            }
        }else{
            Map<String,String> resultMap = new HashMap<String,String>();
            try {
                WeixinpayUtil weixinpayUtil = new WeixinpayUtil();
                Map<String,String> weixinrefund=weixinpayUtil.refund(refundAmount,merOrderId);
                weixinrefund.put("refundStatus","SUCCESS");
                resultMap.put("errCode","SUCCESS");
                resultMap.put("respStr", net.sf.json.JSONObject.fromObject(weixinrefund).toString());
                String resultStr = net.sf.json.JSONObject.fromObject(resultMap).toString();
                returnmap.put("returninfo",resultStr);
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
                String resultStr = net.sf.json.JSONObject.fromObject(resultMap).toString();
                returnmap.put("returninfo",resultStr);
            }
        }
        return returnmap;
    }


    public static void main(String[] args) {
        /*BigDecimal bigDecimal = new BigDecimal("0.05");
        System.out.println((bigDecimal.multiply(new BigDecimal(100))).setScale(0).longValue());*/
    }



}
