package com.wink.livemall.admin.api.user;


import com.wink.livemall.admin.util.Errors;
import com.wink.livemall.admin.util.HttpJsonResult;
import com.wink.livemall.admin.util.TokenUtil;
import com.wink.livemall.goods.service.GoodService;
import com.wink.livemall.member.dto.LmMemberInvite;
import com.wink.livemall.order.dto.LmOrder;
import com.wink.livemall.order.dto.LmOrderGoods;
import com.wink.livemall.order.service.LmOrderGoodsService;
import com.wink.livemall.order.service.LmOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "活动信息接口")
@RestController
@RequestMapping("event")
public class EventController {

    Logger logger = LogManager.getLogger(EventController.class);


    @Autowired
    private GoodService goodService;

    @Autowired
    private LmOrderService lmOrderService;
    @Autowired
    private LmOrderGoodsService lmOrderGoodsService;

    @RequestMapping("/getEventGood")
    @ResponseBody
    @ApiOperation(value = "获取活动商品",notes = "获取活动商品接口")
    @ApiImplicitParams({
    })
    private HttpJsonResult getEventGood(HttpServletRequest request) throws Exception {
        HttpJsonResult jsonResult = new HttpJsonResult();
        try {
            List<Map<String, Object>> byMerIdByApi = goodService.findByMerchIdByApi(363);
            jsonResult.setData(byMerIdByApi);
            jsonResult.setCode(Errors.SUCCESS.getCode());
            jsonResult.setMsg(Errors.SUCCESS.getMsg());
            return jsonResult;
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(Errors.ERROR.getCode());
            logger.error(e.getMessage());
        }
        return jsonResult;
    }

    @RequestMapping("/isBuy")
    @ResponseBody
    @ApiOperation(value = "是否买过活动商品",notes = "是否买过活动商品接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goodId", value = "商品ID", dataType = "Integer",paramType = "query")
    })
    private HttpJsonResult getInviteGood(HttpServletRequest request,int goodId) throws Exception {
        HttpJsonResult jsonResult = new HttpJsonResult();
        String header = request.getHeader("Authorization");
        String userId="";
        if (!StringUtils.isEmpty(header)) {
            if(!StringUtils.isEmpty(TokenUtil.getUserId(header))){
                userId = TokenUtil.getUserId(header);
            }
        }
        try {
            int isBuyState=0;
            if(!StringUtils.isEmpty(userId)){
                List<LmOrder> isBuy = lmOrderService.isBuy(363,Integer.parseInt(userId));
                if(isBuy.size()>0){
                  /*for(LmOrder lmOrder:isBuy){
                      if(lmOrder.getIslivegood()==0){
                      LmOrderGoods byOrderId = lmOrderGoodsService.findByOrderid(lmOrder.getId());
                          if(byOrderId.getGoodstype()==0){
                            if(byOrderId.getGoodid()==goodId){
                                isBuyState=1;
                                break;
                            }
                          }
                      }
                  }*/
                    isBuyState=1;
                }
            }
            jsonResult.setData(isBuyState);
            jsonResult.setCode(Errors.SUCCESS.getCode());
            jsonResult.setMsg(Errors.SUCCESS.getMsg());
            return jsonResult;
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(Errors.ERROR.getCode());
            logger.error(e.getMessage());
        }
        return jsonResult;
    }


}
