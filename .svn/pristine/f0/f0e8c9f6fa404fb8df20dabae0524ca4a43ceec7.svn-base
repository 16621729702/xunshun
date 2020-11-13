package com.wink.livemall.admin.controller.ExcelController;

import com.wink.livemall.admin.controller.live.LiveController;
import com.wink.livemall.admin.easypoidto.OrderPoiVo;
import com.wink.livemall.admin.util.ExcelUtils;
import com.wink.livemall.order.dto.LmOrder;
import com.wink.livemall.order.service.LmOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("export")
public class ExportController {
    @Autowired
    private LmOrderService lmorderService;
    private Logger LOG= LoggerFactory.getLogger(ExportController.class);
    /**
     * 导出
     *
     * @param response
     */
    @RequestMapping("orderexport")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
        String orderid = StringUtils.isEmpty(request.getParameter("orderid"))?"":request.getParameter("orderid");
        String status = StringUtils.isEmpty(request.getParameter("status"))?"":request.getParameter("status");
        String paystatus = StringUtils.isEmpty(request.getParameter("paystatus"))?"":request.getParameter("paystatus");
        String startdate = StringUtils.isEmpty(request.getParameter("startdate"))?"":request.getParameter("startdate");
        String enddate = StringUtils.isEmpty(request.getParameter("enddate"))?"":request.getParameter("enddate");
        String timetype = StringUtils.isEmpty(request.getParameter("timetype"))?"":request.getParameter("timetype");
        List<OrderPoiVo> orderlist = new ArrayList<>();
        Map<String,String> condient = new HashMap<String, String>(16);
        condient.put("orderid",orderid);
        condient.put("status",status);
        condient.put("paystatus",paystatus);
        condient.put("startdate",startdate);
        condient.put("enddate",enddate);
        condient.put("timetype",timetype);
        List<Map<String,Object>> lmordersList =lmorderService.findByCondient(condient);
        for(Map<String,Object>map:lmordersList){
            OrderPoiVo orderPoiVo = new OrderPoiVo();
            if(!StringUtils.isEmpty(map.get("createtime"))){
                orderPoiVo.setCreattime(map.get("createtime")+"");
            }
            if(!StringUtils.isEmpty(map.get("paytime"))){
                orderPoiVo.setPaytime(map.get("paytime")+"");
            }
            orderPoiVo.setOrderid(map.get("orderid")+"");
            orderPoiVo.setPaystatus(LmOrder.paystatuschangetochinese(map.get("paystatus")));
            orderPoiVo.setStatus(LmOrder.statuschangetochinese(map.get("status")));
            orderPoiVo.setGoodinfo(map.get("goodname")+" |￥"+map.get("goodprice")+" x "+map.get("goodnum"));
            orderPoiVo.setUserinfo(map.get("chargename")+"|"+map.get("chargephone"));
            orderPoiVo.setRealpayprice(map.get("realpayprice")+"");
            orderlist.add(orderPoiVo);
        }
        try {
            ExcelUtils.exportExcel(orderlist, "订单信息表", "订单信息", OrderPoiVo.class, "订单信息", response);
        } catch (IOException e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
