package com.wink.livemall.admin.scheduled;

import com.wink.livemall.admin.util.DateUtils;
import com.wink.livemall.goods.dto.Good;
import com.wink.livemall.goods.dto.LmGoodAuction;
import com.wink.livemall.goods.service.GoodService;
import com.wink.livemall.live.dto.LmLiveGood;
import com.wink.livemall.live.service.LmLiveGoodService;
import com.wink.livemall.member.dto.LmMember;
import com.wink.livemall.member.dto.LmMemberAddress;
import com.wink.livemall.member.service.LmMemberAddressService;
import com.wink.livemall.member.service.LmMemberService;
import com.wink.livemall.order.dto.LmOrder;
import com.wink.livemall.order.dto.LmOrderGoods;
import com.wink.livemall.order.service.LmOrderGoodsService;
import com.wink.livemall.order.service.LmOrderService;
import com.wink.livemall.sys.setting.dto.Configs;
import com.wink.livemall.sys.setting.service.ConfigsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderSchedule {/*
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
    private LmMemberAddressService lmMemberAddressService;
    //没五分钟执行一次
    @Scheduled(fixedDelay = 1000*60*5)
    public void scheduledTask1(){
        System.out.println("开始寻找拍卖结束未生成订单的拍品");
        List<Good> list = goodService.findwaitGoodInfo(new Date());
        Integer maxId = orderService.findMaxId();
        String datetime = DateUtils.sdfyMdHm.format(new Date());
        Configs configs =configsService.findByTypeId(Configs.type_pay);
        Map map =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
        String prfix=map.get("msgSrcId")+"";
        for(Good good:list){
            //根据商品 查询商品出价最高的用户
            List<LmGoodAuction> lmGoodAuctionList =goodService.findAuctionlistByGoodid2(good.getId());
            if(lmGoodAuctionList!=null&&lmGoodAuctionList.size()>0){
                LmGoodAuction auction = lmGoodAuctionList.get(0);
                auction.setStatus("2");
                goodService.updateAuctionService(auction);
                int memberid = auction.getMemberid();
                List<LmMemberAddress> addresses = lmMemberAddressService.findByMemberid(memberid);
                LmMemberAddress address = addresses.get(0);
                LmMember lmMember = lmMemberService.findById(memberid+"");
                good.setAuction_status(1);
                goodService.updateGoods(good);
                LmOrder lmOrder = new LmOrder();
                lmOrder.setOrderid(prfix+datetime+maxId);
                lmOrder.setStatus("0");
                lmOrder.setType(1);
                lmOrder.setPaynickname(lmMember.getNickname());
                lmOrder.setCreatetime(new Date());
                lmOrder.setMerchid(good.getMer_id());
                lmOrder.setChargeaddress(address.getProvince()+address.getCity()+address.getDistrict()+address.getAddress_info());
                lmOrder.setChargename(address.getRealname());
                lmOrder.setChargephone(address.getMobile());
                lmOrder.setMemberid(memberid);
                lmOrder.setTotalprice(auction.getPrice());
                if(good.getFreeshipping()==1){
                    lmOrder.setRealexpressprice(new BigDecimal(0));
                    lmOrder.setRealpayprice(auction.getPrice());
                }else{
                    lmOrder.setRealexpressprice(good.getExpressprice());
                    lmOrder.setRealpayprice(auction.getPrice().add(good.getExpressprice()));
                }
                orderService.insertService(lmOrder);
                LmOrderGoods lmOrderGoods = new LmOrderGoods();
                lmOrderGoods.setGoodid(good.getId());
                lmOrderGoods.setGoodnum(1);
                lmOrderGoods.setGoodprice(good.getExpressprice());
                lmOrderGoods.setOrderid(lmOrder.getId());
                lmOrderGoodsService.insertService(lmOrderGoods);
            }else{
                //设置流拍

                good.setAuction_status(2);
                goodService.updateGoods(good);
            }

        }
    }

    //定时下架直播拍卖到时商品
    @Scheduled(fixedDelay = 1000*30)
    public void scheduledTask2(){
        System.out.println("寻找拍卖时间到时的直播商品");
        List<Map<String,Object>>list = lmLiveGoodService.findAllInfo();
        for(Map<String,Object> map:list){
            int id = (int)map.get("id");
            int delaytime = (int)map.get("delaytime");
            Date date = (Date) map.get("auction_end_time");
            if(new Date().getTime()<(date.getTime()+delaytime)){
                lmLiveGoodService.delGood(id);
            }
        }
    }


    //收货超时订单 设置收货
    @Scheduled(fixedDelay = 1000*60*5)
    public void scheduledTask3(){

    }*/
}
