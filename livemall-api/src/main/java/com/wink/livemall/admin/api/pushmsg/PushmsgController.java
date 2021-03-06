package com.wink.livemall.admin.api.pushmsg;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.wink.livemall.admin.api.user.UserController;
import com.wink.livemall.admin.util.DateUtils;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.httpclient.HttpClient;
import com.wink.livemall.goods.dto.Good;
import com.wink.livemall.goods.dto.LivedGood;
import com.wink.livemall.goods.dto.LmShareGood;
import com.wink.livemall.goods.service.GoodService;
import com.wink.livemall.merch.dto.LmMerchInfo;
import com.wink.livemall.merch.service.LmMerchInfoService;
import com.wink.livemall.order.dto.LmOrder;
import com.wink.livemall.order.dto.LmOrderGoods;
import com.wink.livemall.order.service.LmOrderGoodsService;
import com.wink.livemall.order.service.LmOrderService;
import com.wink.livemall.sys.msg.service.PushmsgService;
import com.wink.livemall.sys.setting.dto.Configs;
import com.wink.livemall.sys.setting.service.ConfigsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Api(tags = "消息接口")
@RestController
@RequestMapping("pushmsg")
public class PushmsgController {
    Logger logger = LogManager.getLogger(PushmsgController.class);
    @Autowired
    private PushmsgService pushmsgService;
    @Autowired
    private GoodService goodService;
    @Autowired
    private LmOrderService lmOrderService;
    @Autowired
    private ConfigsService configsService;
    @Autowired
    private LmMerchInfoService lmMerchInfoService;
    @Autowired
    private LmOrderGoodsService lmOrderGoodsService;
    /**
     * 发送消息接口
     * @return
     */
    @ApiOperation(value = "提醒发货接口")
    @PostMapping("/remindsend")
    public JsonResult remindsend(HttpServletRequest request,
                              @ApiParam(name = "id", value = "订单id", required = true)@RequestParam String id){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            LmOrder order = lmOrderService.findById(id);
            if(order==null){
                return new JsonResult(JsonResult.ERROR,"订单id不存在");
            }
            LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(order.getMerchid()+"");
            LmOrderGoods lmOrderGoods = lmOrderGoodsService.findByOrderid(order.getId());
            if(lmMerchInfo==null||lmOrderGoods==null){
                return new JsonResult(JsonResult.ERROR,"商户信息或订单商品信息异常");
            }
            String msg = "";
            if(order.getRemind()>=3){
                return new JsonResult(JsonResult.ERROR,"提醒次数已超过三次，请耐心等待");
            }
            if(order.getType()==3){
            	LmShareGood good = goodService.findshareById(lmOrderGoods.getGoodid());
            	if(good==null){
                    return new JsonResult(JsonResult.ERROR,"商品信息异常");
                }
                 msg = "提醒发货:您的店铺"+lmMerchInfo.getStore_name()+"\n订单号"+order.getOrderid()+"-商品："+good.getName()+"----------买家提醒发货啦！";

            }else{
                if(order.getIslivegood()==1){
                    LivedGood good = goodService.findLivedGood(lmOrderGoods.getGoodid());
                    if (good == null) {
                        return new JsonResult(JsonResult.ERROR, "商品信息异常");
                    }
                    msg = "提醒发货:您的店铺"+lmMerchInfo.getStore_name()+"\n订单号"+order.getOrderid()+"-商品："+good.getName()+"----------买家提醒发货啦！";

                }else {
                    Good good = goodService.findById(lmOrderGoods.getGoodid());
                    if (good == null) {
                        return new JsonResult(JsonResult.ERROR, "商品信息异常");
                    }
                    msg = "提醒发货:您的店铺"+lmMerchInfo.getStore_name()+"\n订单号" + order.getOrderid() + "-商品：" + good.getTitle() + "----------买家提醒发货啦！";
                }
            }
            order.setRemind(order.getRemind()+1);
            lmOrderService.updateService(order);
            HttpClient httpClient = new HttpClient();
            httpClient.send("交易消息",lmMerchInfo.getMember_id()+"",msg);
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }


    /**
     * 根据接收者获取信息列表
     * @return
     */
    @ApiOperation(value = "根据接收者获取信息列表type=1 是平台信息 有logo和名称 type=2是系统信息 没有头像和名称（头像可以定死，名称也可以定死系统消息）")
    @PostMapping("/list")
    public JsonResult list(HttpServletRequest request,
                           @ApiParam(name = "receiveid", value = "接收人id", required = true)@RequestParam int receiveid){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            Configs configs = configsService.findByTypeId(Configs.type_basic);
            List<Map<String,Object>> list = pushmsgService.getlistByreceiveid(receiveid);
            String config = configs.getConfig();
            Map stringToMap  =  JSONObject.parseObject(config);
            for(Map<String,Object> map:list){
                map.put("createtime",DateUtils.sdf_yMdHms.format(map.get("createtime")));
                if((int)map.get("type")==1){
                    map.put("logo",stringToMap.get("logo"));
                    map.put("site_name",stringToMap.get("site_name"));
                }
            }
            jsonResult.setData(list);
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }




    /**
     * 消息详情
     * @return
     */
    @ApiOperation(value = "消息详情")
    @PostMapping("/detail")
    public JsonResult detail(HttpServletRequest request,
                           @ApiParam(name = "sendid", value = "发送者id", required = true)@RequestParam int sendid,
                             @ApiParam(name = "receiveid", value = "接收人id", required = true)@RequestParam int receiveid){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            Configs configs = configsService.findByTypeId(Configs.type_basic);

            List<Map<String,Object>> list = pushmsgService.getlistBysendidAndreceiveid(sendid,receiveid);
            String config = configs.getConfig();
            Map stringToMap  =  JSONObject.parseObject(config);
            for(Map<String,Object> map:list){
                map.put("createtime",DateUtils.sdf_yMdHms.format(map.get("createtime")));
                if((int)map.get("type")==1){
                    map.put("logo",stringToMap.get("logo"));
                    map.put("site_name",stringToMap.get("site_name"));
                }
            }
            jsonResult.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }


    /**
     * 删除系统消息
     * @return
     */
    @ApiOperation(value = "删除系统消息")
    @PostMapping("/deletethis")
    public JsonResult deletethis(HttpServletRequest request,
                             @ApiParam(name = "sendid", value = "发送者id", required = true)@RequestParam int sendid,
                             @ApiParam(name = "receiveid", value = "接收人id", required = true)@RequestParam int receiveid){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            List<Map<String,Object>> list = pushmsgService.getlistBysendidAndreceiveid(sendid,receiveid);
            for(Map<String,Object> map:list){
                int id = (int) map.get("id");
                pushmsgService.deleteservice(id);
            }
            jsonResult.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }



}
