package com.wink.livemall.job.scheduled;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.wink.livemall.goods.dto.Good;
import com.wink.livemall.goods.dto.LivedGood;
import com.wink.livemall.goods.dto.LmGoodAuction;
import com.wink.livemall.goods.dto.LmShareGood;
import com.wink.livemall.goods.service.GoodService;
import com.wink.livemall.job.utils.DateUtils;
import com.wink.livemall.job.utils.HttpClient;
import com.wink.livemall.job.utils.PayUtil;
import com.wink.livemall.job.utils.WeixinpayUtil;
import com.wink.livemall.live.dto.LmLive;
import com.wink.livemall.live.dto.LmLiveGood;
import com.wink.livemall.live.service.LmLiveGoodService;
import com.wink.livemall.live.service.LmLiveService;
import com.wink.livemall.live.util.QiniuUtil;
import com.wink.livemall.member.dto.*;
import com.wink.livemall.member.service.LmFalsifyService;
import com.wink.livemall.member.service.LmMemberAddressService;
import com.wink.livemall.member.service.LmMemberLevelService;
import com.wink.livemall.member.service.LmMemberService;
import com.wink.livemall.merch.dto.LmMerchInfo;
import com.wink.livemall.merch.service.LmMerchInfoService;
import com.wink.livemall.order.dto.LmOrder;
import com.wink.livemall.order.dto.LmOrderGoods;
import com.wink.livemall.order.dto.LmOrderLog;
import com.wink.livemall.order.dto.LmPayLog;
import com.wink.livemall.order.service.*;
import com.wink.livemall.sys.msg.service.PushmsgService;
import com.wink.livemall.sys.setting.dto.Configs;
import com.wink.livemall.sys.setting.service.ConfigsService;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Component
public class OrderSchedule {
    @Autowired
    private LmLiveGoodService lmLiveGoodService;
    @Autowired
    private GoodService goodService;
    @Autowired
    private LmOrderService orderService;
    @Autowired
    private LmOrderGoodsService lmOrderGoodsService;
    @Autowired
    private LmMemberService lmMemberService;
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
    /**
     * 开始寻找拍卖结束未生成订单的拍品,生产订单或者流拍
     */
    @Scheduled(fixedDelay = 1000*30)
    public void findwaitgoodgenerOrder(){
        System.out.println("开始寻找拍卖结束未生成订单的拍品");
        List<Good> list = goodService.findwaitGoodInfo(new Date());
        Integer maxId = orderService.findMaxId();
        String datetime = DateUtils.sdfyMdHm.format(new Date());
        Configs configs =configsService.findByTypeId(Configs.type_pay);
        Map map =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());

        String prfix=map.get("msgSrcId")+"";
        for(Good good:list){
            //查看延迟时间是否结束
            if(System.currentTimeMillis()>good.getDelaytime()+good.getAuction_end_time().getTime()){
                //根据商品 查询商品出价最高的用户
                List<LmGoodAuction> lmGoodAuctionList =goodService.findAuctionlistByGoodid2(good.getId(),0);
                LmOrderGoods aloneOrderGoods=lmOrderGoodsService.findByGoodsid0(good.getId());
                if(aloneOrderGoods==null) {
                    if (lmGoodAuctionList != null && lmGoodAuctionList.size() > 0) {
                        LmGoodAuction auction = lmGoodAuctionList.get(0);
                        auction.setStatus("2");
                        goodService.updateAuctionService(auction);
                        int memberid = auction.getMemberid();
                        List<LmMemberAddress> addresses = lmMemberAddressService.findByMemberid(memberid);
                        if (addresses != null && addresses.size() > 0) {
                            LmMemberAddress address = addresses.get(0);
                            LmMember lmMember = lmMemberService.findById(memberid + "");
                            good.setAuction_status(1);
                            goodService.updateGoods(good);
                            LmOrder lmOrder = new LmOrder();
                            lmOrder.setOrderid(prfix + datetime + (int) (Math.random() * (9999 - 1000) + 1000));
                            lmOrder.setStatus("0");
                            lmOrder.setType(2);
                            lmOrder.setPaynickname(lmMember.getNickname());
                            lmOrder.setCreatetime(new Date());
                            lmOrder.setMerchid(good.getMer_id());
                            lmOrder.setChargeaddress(address.getProvince() + address.getCity() + address.getDistrict() + address.getAddress_info());
                            lmOrder.setChargename(address.getRealname());
                            lmOrder.setChargephone(address.getMobile());
                            lmOrder.setMemberid(memberid);
                            lmOrder.setDeposit_type(0);
                            lmOrder.setTotalprice(auction.getPrice());
                            if (good.getFreeshipping() == 1) {
                                lmOrder.setRealexpressprice(new BigDecimal(0));
                                lmOrder.setRealpayprice(auction.getPrice());
                            } else {
                                lmOrder.setRealexpressprice(good.getExpressprice());
                                lmOrder.setRealpayprice(auction.getPrice().add(good.getExpressprice()));
                            }
                            orderService.insertService(lmOrder);
                            LmOrderGoods lmOrderGoods = new LmOrderGoods();
                            lmOrderGoods.setGoodid(good.getId());
                            lmOrderGoods.setGoodnum(1);
                            lmOrderGoods.setGoodstype(0);
                            lmOrderGoods.setGoodprice(auction.getPrice());
                            lmOrderGoods.setOrderid(lmOrder.getId());
                            lmOrderGoodsService.insertService(lmOrderGoods);
                          //发送通知 同时用户竞拍成功
                            LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(String.valueOf(lmOrder.getMerchid()));
                            Map<String, Object> memberinfo = new HashMap<>();
                            memberinfo.put("levelname", "");
                            memberinfo.put("levelcode", "");
                            String msg = "您拍卖的商品：-" + good.getTitle() + "-已竞拍成功,\n可以进入个人中心的待付款里进行付款操作,未支付会导致您产生违约记录";
//                    pushmsgService.send(0,msg,"2",memberid,0);
                            HttpClient httpClient = new HttpClient();
                            memberinfo.put("nickname", lmMerchInfo.getStore_name());
                            httpClient.login(lmMerchInfo.getAvatar(), lmMerchInfo.getStore_name(), new Gson().toJson(memberinfo));
                            httpClient.send(lmMerchInfo.getStore_name(), memberid + "", msg);
                            LmOrderLog lmOrderLog = new LmOrderLog();
                            lmOrderLog.setOrderid(lmOrder.getOrderid());
                            lmOrderLog.setOperatedate(new Date());
                            lmOrderLog.setOperate("订单生产");
                            lmOrderLogService.insert(lmOrderLog);
                        } else {
                            LmMember lmMember = lmMemberService.findById(memberid + "");
                            good.setAuction_status(1);
                            goodService.updateGoods(good);
                            LmOrder lmOrder = new LmOrder();
                            lmOrder.setOrderid(prfix + datetime + (int) (Math.random() * (9999 - 1000) + 1000));
                            lmOrder.setStatus("0");
                            lmOrder.setType(2);
                            lmOrder.setPaynickname(lmMember.getNickname());
                            lmOrder.setCreatetime(new Date());
                            lmOrder.setMerchid(good.getMer_id());
                            lmOrder.setMemberid(memberid);
                            lmOrder.setDeposit_type(0);
                            lmOrder.setTotalprice(auction.getPrice());
                            if (good.getFreeshipping() == 1) {
                                lmOrder.setRealexpressprice(new BigDecimal(0));
                                lmOrder.setRealpayprice(auction.getPrice());
                            } else {
                                lmOrder.setRealexpressprice(good.getExpressprice());
                                lmOrder.setRealpayprice(auction.getPrice().add(good.getExpressprice()));
                            }
                            orderService.insertService(lmOrder);
                            LmOrderGoods lmOrderGoods = new LmOrderGoods();
                            lmOrderGoods.setGoodid(good.getId());
                            lmOrderGoods.setGoodnum(1);
                            lmOrderGoods.setGoodprice(auction.getPrice());
                            lmOrderGoods.setOrderid(lmOrder.getId());
                            lmOrderGoods.setGoodstype(0);
                            lmOrderGoodsService.insertService(lmOrderGoods);
                            //发送通知 同时用户竞拍成功
                            LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(String.valueOf(lmOrder.getMerchid()));
                            LmMember lmMembers = lmMemberService.findById(String.valueOf(lmMerchInfo.getMember_id()));
                            Map<String, Object> memberinfo = new HashMap<>();
                            memberinfo.put("levelname", "");
                            memberinfo.put("levelcode", "");
                            String msg = "您拍卖的商品：-" + good.getTitle() + "-已竞拍成功,\n可以进入个人中心的待付款里进行付款操作,未支付会导致您产生违约记录";
//                    pushmsgService.send(0,msg,"2",memberid,0);
                            HttpClient httpClient = new HttpClient();
                            memberinfo.put("nickname", lmMerchInfo.getStore_name());
                            httpClient.login(lmMerchInfo.getAvatar(), lmMerchInfo.getStore_name(), new Gson().toJson(memberinfo));
                            httpClient.send(lmMerchInfo.getStore_name(), memberid + "", msg);
                            LmOrderLog lmOrderLog = new LmOrderLog();
                            lmOrderLog.setOrderid(lmOrder.getOrderid());
                            lmOrderLog.setOperatedate(new Date());
                            lmOrderLog.setOperate("订单生产");
                            lmOrderLogService.insert(lmOrderLog);
                        }
                    } else {
                        //设置流拍
                        good.setAuction_status(2);
                        goodService.updateGoods(good);
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
                if(lmGoodAuctionList!=null&&lmGoodAuctionList.size()>0){
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



    //定时下架直播拍卖到时商品
    @Scheduled(fixedDelay = 1000*30)
    public void closelivegood(){
        System.out.println("寻找拍卖时间到时的直播商品");
        List<Map<String,Object>>list = lmLiveGoodService.findAllInfo();
        for(Map<String,Object> map:list){
            int id = (int)map.get("id");
            int delaytime = (int)map.get("delaytime");
            Date date = (Date) map.get("auction_end_time");
            if(date!=null){
                if(new Date().getTime()>(date.getTime()+delaytime)){
                    lmLiveGoodService.delGood(id);
                }
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
            //商品设置下架
            good.setState(0);
            good.setWarehouse("3");
            good.setAuction_status(2);
            goodService.updateGoods(good);
        }
    }


    //设置自动收货
    @Scheduled(fixedDelay = 1000*30)
    public void autogetgood(){
        Configs tradconfigs =configsService.findByTypeId(Configs.type_trading);
        Map tradmap =  JSONObject.parseObject(tradconfigs.getConfig());
        //获取自动收货时间
        String gettime = tradmap.get("automatic_receiving_goods")+"";
        //当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - Integer.parseInt(gettime));
        //七天前时间
        Date olddate =calendar.getTime();
        //查询收货之间超过7天还没点击收货的订单设置已收货并添加积分
        List<LmOrder> list  = orderService.findListByDate(olddate);
        for(LmOrder order:list){
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
            lmMerchInfo.setCredit((null==lmMerchInfo.getCredit()?BigDecimal.ZERO:lmMerchInfo.getCredit()).add(order.getRealpayprice()));
            lmMerchInfoService.updateService(lmMerchInfo);

            LmOrderLog lmOrderLog = new LmOrderLog();
            lmOrderLog.setOrderid(order.getOrderid());
            lmOrderLog.setOperatedate(new Date());
            lmOrderLog.setOperate("订单确认收货");
            lmOrderLogService.insert(lmOrderLog);
        }
    }



    //自动关闭时间内未支付的订单
    @Scheduled(fixedDelay = 1000*30)
    public void autocloseOrder(){
        System.out.println("自动关闭时间内未支付的订单");
        //查询所有未支付订单
        List<LmOrder> lmordersList =orderService.findOrderListByStatus(0);
        Configs tradconfigs =configsService.findByTypeId(Configs.type_trading);
//        Map tradmap =  com.alibaba.fastjson.JSONObject.parseObject(tradconfigs.getConfig());
        //获取支付限时（小时数）
//        String paytime = tradmap.get("order_cancel_time")+"";
        //当前时间
        Calendar calendar = Calendar.getInstance();

        //获取n小时前的时间
        Date olddate =calendar.getTime();
        for(LmOrder order:lmordersList){
            if(order.getType()==3){
                if(order.getPorderid()!=0&&order.getDeposit_type()!=1){
                    String sharepaytime = "30";//30秒
                    //当前时间
                    calendar.setTime(new Date());
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
                calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - Integer.parseInt(paytime));
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
                        if(null!=isfalsify){
                            isfalsify.setType(1);
                            isfalsify.setStatus(3);
                            lmFalsifyService.updateService(isfalsify);
                        }
                    }
                }
            } else {
                //订单创建时间
                Date createtime = order.getCreatetime();
                String paytime = "30";//30分钟
                //当前时间
                calendar.setTime(new Date());
                calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - Integer.parseInt(paytime));
                //获取n小时前的时间
                olddate =calendar.getTime();
                if(createtime!=null){
                    if(olddate.getTime()>createtime.getTime()){
                        //订单失效
                        order.setStatus("-1");
                        orderService.updateService(order);
                        if(order.getType()==1){
                            //获取订单关联商品
                            LmOrderGoods lmOrderGoods = lmOrderGoodsService.findByOrderid(order.getId());
                            Good good = goodService.findById(lmOrderGoods.getGoodid());
                            if(good!=null){
                                good.setStock(good.getStock()+lmOrderGoods.getGoodnum());
                                goodService.updateGoods(good);
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
                    List<LivedGood> oldgood = goodService.findLivedGoodByLiveid(lmLive.getId());
                    for(LivedGood good:oldgood){
                        if(good.getType()!=1) {
                            good.setStatus(-1);
                            goodService.updateLivedGood(good);
                        }
                    }
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

    //关闭到期的直播商品（拍卖私卖一口价）
    @Scheduled(fixedDelay = 1000*30)
    public void closelivegood2() {
        List<LivedGood> goodlist = goodService.findLiveGood(new Date());
        for(LivedGood good:goodlist){
            //商品设置下架
            if(1==good.getType()) {
                good.setStatus(-1);
                goodService.updateLivedGood(good);
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
        BigDecimal bigDecimal = new BigDecimal("0.05");
        System.out.println((bigDecimal.multiply(new BigDecimal(100))).setScale(0).longValue());
    }



}
