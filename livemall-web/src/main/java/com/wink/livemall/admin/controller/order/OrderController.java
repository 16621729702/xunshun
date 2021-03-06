package com.wink.livemall.admin.controller.order;

import com.wink.livemall.admin.controller.marketing.MarketController;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.PageUtil;
import com.wink.livemall.goods.dto.Good;
import com.wink.livemall.goods.dto.LmGoodComment;
import com.wink.livemall.member.dto.LmMember;
import com.wink.livemall.member.dto.LmMemberLevel;
import com.wink.livemall.member.service.LmMemberLevelService;
import com.wink.livemall.member.service.LmMemberService;
import com.wink.livemall.merch.dto.LmMerchInfo;
import com.wink.livemall.merch.service.LmMerchInfoService;
import com.wink.livemall.order.dao.LmExpressDao;
import com.wink.livemall.order.dao.LmOrderExpressDao;
import com.wink.livemall.order.dto.LmExpress;
import com.wink.livemall.order.dto.LmOrder;
import com.wink.livemall.order.dto.LmOrderExpress;
import com.wink.livemall.order.dto.LmOrderLog;
import com.wink.livemall.order.service.LmExpressService;
import com.wink.livemall.order.service.LmOrderExpressService;
import com.wink.livemall.order.service.LmOrderLogService;
import com.wink.livemall.order.service.LmOrderService;
import com.wink.livemall.sys.setting.dto.Express;
import com.wink.livemall.sys.setting.service.ExpressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("orders")
public class OrderController {
    private Logger LOG= LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private LmOrderLogService lmOrderLogService;
    @Autowired
    private LmOrderService lmorderService;
    @Autowired
    private LmMerchInfoService lmMerchInfoService;
    @Autowired
    private LmMemberLevelService lmMemberLevelService;
    @Autowired
    private LmOrderExpressService lmOrderExpressService;
    @Autowired
    private LmExpressService lmExpressService;
    @Autowired
    private LmMemberService lmMemberService;

    @RequestMapping("orderquery")
    public ModelAndView orderquery(HttpServletRequest request, Model model){
        String page = StringUtils.isEmpty(request.getParameter("page"))?"1":request.getParameter("page");
        String pagesize = StringUtils.isEmpty(request.getParameter("pagesize"))?"20":request.getParameter("pagesize");
        String orderid = StringUtils.isEmpty(request.getParameter("orderid"))?"":request.getParameter("orderid");
        String status = StringUtils.isEmpty(request.getParameter("status"))?"":request.getParameter("status");
        String paystatus = StringUtils.isEmpty(request.getParameter("paystatus"))?"":request.getParameter("paystatus");
        String startdate = StringUtils.isEmpty(request.getParameter("startdate"))?"":request.getParameter("startdate");
        String enddate = StringUtils.isEmpty(request.getParameter("enddate"))?"":request.getParameter("enddate");
        String timetype = StringUtils.isEmpty(request.getParameter("timetype"))?"":request.getParameter("timetype");
        String merchid = StringUtils.isEmpty(request.getParameter("merchid"))?"":request.getParameter("merchid");
        Map<String,String> condient = new HashMap<String, String>(16);
        condient.put("orderid",orderid);
        condient.put("status",status);
        condient.put("paystatus",paystatus);
        condient.put("startdate",startdate);
        condient.put("enddate",enddate);
        condient.put("timetype",timetype);
        if(!StringUtils.isEmpty(merchid)){
            condient.put("merchid",merchid);
        }
        model.addAttribute("merchid",merchid);
        model.addAttribute("orderid",orderid);
        model.addAttribute("status",status);
        model.addAttribute("paystatus",paystatus);
        model.addAttribute("startdate",startdate);
        model.addAttribute("enddate",enddate);
        model.addAttribute("timetype",timetype);
        model.addAttribute("page",page);
        model.addAttribute("pagesize",pagesize);
        List<Map<String,Object>> lmordersList =lmorderService.findByCondient(condient);
        List<Map<String,Object>> lmordersList2 =lmorderService.findByCondient2(condient);
        lmordersList.addAll(lmordersList2);
        BigDecimal totalprice = new BigDecimal("0");
        int totalnum = 0;
        for(Map<String,Object> map:lmordersList){
            totalprice = totalprice.add(new BigDecimal(map.get("realpayprice")+""));
            totalnum += Integer.parseInt(map.get("goodnum")+"");
        }
        model.addAttribute("totalnum",totalnum);
        model.addAttribute("totalprice",totalprice);
        List<Map<String,Object>> returnlist =  PageUtil.startPage(lmordersList,Integer.parseInt(page),Integer.parseInt(pagesize));
        for(Map map:returnlist){
            map.put("paystatus",LmOrder.paystatuschangetochinese(map.get("paystatus")));
            map.put("status",LmOrder.statuschangetochinese(map.get("status")));
        }
        Map<String, List<Map<String,Object>>> returninfo = change(returnlist,"merchid");
        List<LmMerchInfo> activeMerch = lmMerchInfoService.findActiveMerch();
        model.addAttribute("returninfo",returninfo);
        model.addAttribute("activeMerch",activeMerch);
        model.addAttribute("totalpage",lmordersList.size()/Integer.parseInt(pagesize)+1);
        model.addAttribute("totalsize",lmordersList.size());
        model.addAttribute("thissize",returnlist.size());
        return new ModelAndView("order/orderlist");
    }

    @RequestMapping("merchorderquery")
    public ModelAndView merchorderquery(HttpServletRequest request, Model model){
        String page = StringUtils.isEmpty(request.getParameter("page"))?"1":request.getParameter("page");
        String pagesize = StringUtils.isEmpty(request.getParameter("pagesize"))?"20":request.getParameter("pagesize");
        String merchid = StringUtils.isEmpty(request.getParameter("merchid"))?"":request.getParameter("merchid");
        Map<String,String> condient = new HashMap<String, String>(16);
        if(!StringUtils.isEmpty(merchid)){
            condient.put("merchid",merchid);
        }
        model.addAttribute("merchid",merchid);
        model.addAttribute("page",page);
        model.addAttribute("pagesize",pagesize);
        List<Map<String,Object>> lmordersList =lmorderService.findByCondient(condient);
        BigDecimal totalprice = new BigDecimal("0");
        int totalnum = 0;
        for(Map<String,Object> map:lmordersList){
            totalprice = totalprice.add(new BigDecimal(map.get("realpayprice")+""));
            totalnum += Integer.parseInt(map.get("goodnum")+"");
        }
        model.addAttribute("totalnum",totalnum);
        model.addAttribute("totalprice",totalprice);
        List<Map<String,Object>> returnlist =  PageUtil.startPage(lmordersList,Integer.parseInt(page),Integer.parseInt(pagesize));
        for(Map map:returnlist){
            map.put("paystatus",LmOrder.paystatuschangetochinese(map.get("paystatus")));
            map.put("status",LmOrder.statuschangetochinese(map.get("status")));
        }
        Map<String, List<Map<String,Object>>> returninfo = change(returnlist,"merchid");
        model.addAttribute("returninfo",returninfo);
        model.addAttribute("totalpage",lmordersList.size()/Integer.parseInt(pagesize)+1);
        model.addAttribute("totalsize",lmordersList.size());
        model.addAttribute("thissize",returnlist.size());
        return new ModelAndView("merch/merchorderlist");
    }

    @RequestMapping("waitsendlist")
    public ModelAndView waitsendlist(HttpServletRequest request, Model model){
        String page = StringUtils.isEmpty(request.getParameter("page"))?"1":request.getParameter("page");
        String pagesize = StringUtils.isEmpty(request.getParameter("pagesize"))?"20":request.getParameter("pagesize");
        String orderid = StringUtils.isEmpty(request.getParameter("orderid"))?"":request.getParameter("orderid");
        String paystatus = StringUtils.isEmpty(request.getParameter("paystatus"))?"":request.getParameter("paystatus");
        String startdate = StringUtils.isEmpty(request.getParameter("startdate"))?"":request.getParameter("startdate");
        String enddate = StringUtils.isEmpty(request.getParameter("enddate"))?"":request.getParameter("enddate");
        String timetype = StringUtils.isEmpty(request.getParameter("timetype"))?"":request.getParameter("timetype");
        Map<String,String> condient = new HashMap<String, String>(16);
        condient.put("orderid",orderid);
        condient.put("status","1");
        condient.put("paystatus",paystatus);
        condient.put("startdate",startdate);
        condient.put("enddate",enddate);
        condient.put("timetype",timetype);
        model.addAttribute("orderid",orderid);
        model.addAttribute("status","1");
        model.addAttribute("paystatus",paystatus);
        model.addAttribute("startdate",startdate);
        model.addAttribute("enddate",enddate);
        model.addAttribute("timetype",timetype);
        model.addAttribute("page",page);
        model.addAttribute("pagesize",pagesize);
        List<Map<String,Object>> lmordersList =lmorderService.findByCondient(condient);
        BigDecimal totalprice = new BigDecimal("0");
        int totalnum = 0;
        for(Map<String,Object> map:lmordersList){
            totalprice = totalprice.add(new BigDecimal(map.get("realpayprice")+""));
            totalnum += Integer.parseInt(map.get("goodnum")+"");
        }
        model.addAttribute("totalnum",totalnum);
        model.addAttribute("totalprice",totalprice);
        List<Map<String,Object>> returnlist =  PageUtil.startPage(lmordersList,Integer.parseInt(page),Integer.parseInt(pagesize));
        for(Map map:returnlist){
            map.put("paystatus",LmOrder.paystatuschangetochinese(map.get("paystatus")));
            map.put("status",LmOrder.statuschangetochinese(map.get("status")));
        }
        Map<String, List<Map<String,Object>>> returninfo = change(returnlist,"merchid");
        model.addAttribute("returninfo",returninfo);
        model.addAttribute("totalpage",lmordersList.size()/Integer.parseInt(pagesize)+1);
        model.addAttribute("totalsize",lmordersList.size());
        model.addAttribute("thissize",returnlist.size());
        return new ModelAndView("order/statusorderlist");
    }

    @RequestMapping("waitgetlist")
    public ModelAndView waitgetlist(HttpServletRequest request, Model model){
        String page = StringUtils.isEmpty(request.getParameter("page"))?"1":request.getParameter("page");
        String pagesize = StringUtils.isEmpty(request.getParameter("pagesize"))?"20":request.getParameter("pagesize");
        String orderid = StringUtils.isEmpty(request.getParameter("orderid"))?"":request.getParameter("orderid");
        String paystatus = StringUtils.isEmpty(request.getParameter("paystatus"))?"":request.getParameter("paystatus");
        String startdate = StringUtils.isEmpty(request.getParameter("startdate"))?"":request.getParameter("startdate");
        String enddate = StringUtils.isEmpty(request.getParameter("enddate"))?"":request.getParameter("enddate");
        String timetype = StringUtils.isEmpty(request.getParameter("timetype"))?"":request.getParameter("timetype");
        Map<String,String> condient = new HashMap<String, String>(16);
        condient.put("orderid",orderid);
        condient.put("status","2");
        condient.put("paystatus",paystatus);
        condient.put("startdate",startdate);
        condient.put("enddate",enddate);
        condient.put("timetype",timetype);
        model.addAttribute("orderid",orderid);
        model.addAttribute("status","2");
        model.addAttribute("paystatus",paystatus);
        model.addAttribute("startdate",startdate);
        model.addAttribute("enddate",enddate);
        model.addAttribute("timetype",timetype);
        model.addAttribute("page",page);
        model.addAttribute("pagesize",pagesize);
        List<Map<String,Object>> lmordersList =lmorderService.findByCondient(condient);
        BigDecimal totalprice = new BigDecimal("0");
        int totalnum = 0;
        for(Map<String,Object> map:lmordersList){
            totalprice = totalprice.add(new BigDecimal(map.get("realpayprice")+""));
            totalnum += Integer.parseInt(map.get("goodnum")+"");
        }
        model.addAttribute("totalnum",totalnum);
        model.addAttribute("totalprice",totalprice);
        List<Map<String,Object>> returnlist =  PageUtil.startPage(lmordersList,Integer.parseInt(page),Integer.parseInt(pagesize));
        for(Map map:returnlist){
            map.put("paystatus",LmOrder.paystatuschangetochinese(map.get("paystatus")));
            map.put("status",LmOrder.statuschangetochinese(map.get("status")));
        }
        Map<String, List<Map<String,Object>>> returninfo = change(returnlist,"merchid");
        model.addAttribute("returninfo",returninfo);
        model.addAttribute("totalpage",lmordersList.size()/Integer.parseInt(pagesize)+1);
        model.addAttribute("totalsize",lmordersList.size());
        model.addAttribute("thissize",returnlist.size());
        return new ModelAndView("order/statusorderlist");
    }

    @RequestMapping("waitpaylist")
    public ModelAndView waitpaylist(HttpServletRequest request, Model model){
        String page = StringUtils.isEmpty(request.getParameter("page"))?"1":request.getParameter("page");
        String pagesize = StringUtils.isEmpty(request.getParameter("pagesize"))?"20":request.getParameter("pagesize");
        String orderid = StringUtils.isEmpty(request.getParameter("orderid"))?"":request.getParameter("orderid");
        String paystatus = StringUtils.isEmpty(request.getParameter("paystatus"))?"":request.getParameter("paystatus");
        String startdate = StringUtils.isEmpty(request.getParameter("startdate"))?"":request.getParameter("startdate");
        String enddate = StringUtils.isEmpty(request.getParameter("enddate"))?"":request.getParameter("enddate");
        String timetype = StringUtils.isEmpty(request.getParameter("timetype"))?"":request.getParameter("timetype");
        Map<String,String> condient = new HashMap<String, String>(16);
        condient.put("orderid",orderid);
        condient.put("status","0");
        condient.put("paystatus",paystatus);
        condient.put("startdate",startdate);
        condient.put("enddate",enddate);
        condient.put("timetype",timetype);
        model.addAttribute("orderid",orderid);
        model.addAttribute("status","0");
        model.addAttribute("paystatus",paystatus);
        model.addAttribute("startdate",startdate);
        model.addAttribute("enddate",enddate);
        model.addAttribute("timetype",timetype);
        model.addAttribute("page",page);
        model.addAttribute("pagesize",pagesize);
        List<Map<String,Object>> lmordersList =lmorderService.findByCondient(condient);
        BigDecimal totalprice = new BigDecimal("0");
        int totalnum = 0;
        for(Map<String,Object> map:lmordersList){
            totalprice = totalprice.add(new BigDecimal(map.get("realpayprice")+""));
            totalnum += Integer.parseInt(map.get("goodnum")+"");
        }
        model.addAttribute("totalnum",totalnum);
        model.addAttribute("totalprice",totalprice);
        List<Map<String,Object>> returnlist =  PageUtil.startPage(lmordersList,Integer.parseInt(page),Integer.parseInt(pagesize));
        for(Map map:returnlist){
            map.put("paystatus",LmOrder.paystatuschangetochinese(map.get("paystatus")));
            map.put("status",LmOrder.statuschangetochinese(map.get("status")));
        }
        Map<String, List<Map<String,Object>>> returninfo = change(returnlist,"merchid");
        model.addAttribute("returninfo",returninfo);
        model.addAttribute("totalpage",lmordersList.size()/Integer.parseInt(pagesize)+1);
        model.addAttribute("totalsize",lmordersList.size());
        model.addAttribute("thissize",returnlist.size());
        return new ModelAndView("order/statusorderlist");
    }

    @RequestMapping("successlist")
    public ModelAndView successlist(HttpServletRequest request, Model model){
        String page = StringUtils.isEmpty(request.getParameter("page"))?"1":request.getParameter("page");
        String pagesize = StringUtils.isEmpty(request.getParameter("pagesize"))?"20":request.getParameter("pagesize");
        String orderid = StringUtils.isEmpty(request.getParameter("orderid"))?"":request.getParameter("orderid");
        String paystatus = StringUtils.isEmpty(request.getParameter("paystatus"))?"":request.getParameter("paystatus");
        String startdate = StringUtils.isEmpty(request.getParameter("startdate"))?"":request.getParameter("startdate");
        String enddate = StringUtils.isEmpty(request.getParameter("enddate"))?"":request.getParameter("enddate");
        String timetype = StringUtils.isEmpty(request.getParameter("timetype"))?"":request.getParameter("timetype");
        Map<String,String> condient = new HashMap<String, String>(16);
        condient.put("orderid",orderid);
        condient.put("status","4");
        condient.put("paystatus",paystatus);
        condient.put("startdate",startdate);
        condient.put("enddate",enddate);
        condient.put("timetype",timetype);
        model.addAttribute("orderid",orderid);
        model.addAttribute("status","4");
        model.addAttribute("paystatus",paystatus);
        model.addAttribute("startdate",startdate);
        model.addAttribute("enddate",enddate);
        model.addAttribute("timetype",timetype);
        model.addAttribute("page",page);
        model.addAttribute("pagesize",pagesize);
        List<Map<String,Object>> lmordersList =lmorderService.findByCondient(condient);
        BigDecimal totalprice = new BigDecimal("0");
        int totalnum = 0;
        for(Map<String,Object> map:lmordersList){
            totalprice = totalprice.add(new BigDecimal(map.get("realpayprice")+""));
            totalnum += Integer.parseInt(map.get("goodnum")+"");
        }
        model.addAttribute("totalnum",totalnum);
        model.addAttribute("totalprice",totalprice);
        List<Map<String,Object>> returnlist =  PageUtil.startPage(lmordersList,Integer.parseInt(page),Integer.parseInt(pagesize));
        for(Map map:returnlist){
            map.put("paystatus",LmOrder.paystatuschangetochinese(map.get("paystatus")));
            map.put("status",LmOrder.statuschangetochinese(map.get("status")));
        }
        Map<String, List<Map<String,Object>>> returninfo = change(returnlist,"merchid");
        model.addAttribute("returninfo",returninfo);
        model.addAttribute("totalpage",lmordersList.size()/Integer.parseInt(pagesize)+1);
        model.addAttribute("totalsize",lmordersList.size());
        model.addAttribute("thissize",returnlist.size());
        return new ModelAndView("order/statusorderlist");
    }

    @RequestMapping("refundlist")
    public ModelAndView refundlist(HttpServletRequest request, Model model){
        String page = StringUtils.isEmpty(request.getParameter("page"))?"1":request.getParameter("page");
        String pagesize = StringUtils.isEmpty(request.getParameter("pagesize"))?"20":request.getParameter("pagesize");
        String orderid = StringUtils.isEmpty(request.getParameter("orderid"))?"":request.getParameter("orderid");
        String paystatus = StringUtils.isEmpty(request.getParameter("paystatus"))?"":request.getParameter("paystatus");
        String startdate = StringUtils.isEmpty(request.getParameter("startdate"))?"":request.getParameter("startdate");
        String enddate = StringUtils.isEmpty(request.getParameter("enddate"))?"":request.getParameter("enddate");
        String timetype = StringUtils.isEmpty(request.getParameter("timetype"))?"":request.getParameter("timetype");
        Map<String,String> condient = new HashMap<String, String>(16);
        condient.put("orderid",orderid);
        condient.put("backstatus","1");
        condient.put("paystatus",paystatus);
        condient.put("startdate",startdate);
        condient.put("enddate",enddate);
        condient.put("timetype",timetype);
        model.addAttribute("orderid",orderid);
        model.addAttribute("backstatus","1");
        model.addAttribute("paystatus",paystatus);
        model.addAttribute("startdate",startdate);
        model.addAttribute("enddate",enddate);
        model.addAttribute("timetype",timetype);
        model.addAttribute("page",page);
        model.addAttribute("pagesize",pagesize);
        List<Map<String,Object>> lmordersList =lmorderService.findByCondient(condient);
        BigDecimal totalprice = new BigDecimal("0");
        int totalnum = 0;
        for(Map<String,Object> map:lmordersList){
            totalprice = totalprice.add(new BigDecimal(map.get("realpayprice")+""));
            totalnum += Integer.parseInt(map.get("goodnum")+"");
        }
        model.addAttribute("totalnum",totalnum);
        model.addAttribute("totalprice",totalprice);
        List<Map<String,Object>> returnlist =  PageUtil.startPage(lmordersList,Integer.parseInt(page),Integer.parseInt(pagesize));
        for(Map map:returnlist){
            map.put("paystatus",LmOrder.paystatuschangetochinese(map.get("paystatus")));
            map.put("status",LmOrder.statuschangetochinese(map.get("status")));
        }
        Map<String, List<Map<String,Object>>> returninfo = change(returnlist,"merchid");
        model.addAttribute("returninfo",returninfo);
        model.addAttribute("totalpage",lmordersList.size()/Integer.parseInt(pagesize)+1);
        model.addAttribute("totalsize",lmordersList.size());
        model.addAttribute("thissize",returnlist.size());
        return new ModelAndView("order/statusorderlist");
    }

    @RequestMapping("refundedlist")
    public ModelAndView refundedlist(HttpServletRequest request, Model model){
        String page = StringUtils.isEmpty(request.getParameter("page"))?"1":request.getParameter("page");
        String pagesize = StringUtils.isEmpty(request.getParameter("pagesize"))?"20":request.getParameter("pagesize");
        String orderid = StringUtils.isEmpty(request.getParameter("orderid"))?"":request.getParameter("orderid");
        String paystatus = StringUtils.isEmpty(request.getParameter("paystatus"))?"":request.getParameter("paystatus");
        String startdate = StringUtils.isEmpty(request.getParameter("startdate"))?"":request.getParameter("startdate");
        String enddate = StringUtils.isEmpty(request.getParameter("enddate"))?"":request.getParameter("enddate");
        String timetype = StringUtils.isEmpty(request.getParameter("timetype"))?"":request.getParameter("timetype");
        Map<String,String> condient = new HashMap<String, String>(16);
        condient.put("orderid",orderid);
        condient.put("backstatus","2");
        condient.put("paystatus",paystatus);
        condient.put("startdate",startdate);
        condient.put("enddate",enddate);
        condient.put("timetype",timetype);
        model.addAttribute("orderid",orderid);
        model.addAttribute("backstatus","2");
        model.addAttribute("paystatus",paystatus);
        model.addAttribute("startdate",startdate);
        model.addAttribute("enddate",enddate);
        model.addAttribute("timetype",timetype);
        model.addAttribute("page",page);
        model.addAttribute("pagesize",pagesize);
        List<Map<String,Object>> lmordersList =lmorderService.findByCondient(condient);
        BigDecimal totalprice = new BigDecimal("0");
        int totalnum = 0;
        for(Map<String,Object> map:lmordersList){
            totalprice = totalprice.add(new BigDecimal(map.get("realpayprice")+""));
            totalnum += Integer.parseInt(map.get("goodnum")+"");
        }
        model.addAttribute("totalnum",totalnum);
        model.addAttribute("totalprice",totalprice);
        List<Map<String,Object>> returnlist =  PageUtil.startPage(lmordersList,Integer.parseInt(page),Integer.parseInt(pagesize));
        for(Map map:returnlist){
            map.put("paystatus",LmOrder.paystatuschangetochinese(map.get("paystatus")));
            map.put("status",LmOrder.statuschangetochinese(map.get("status")));
        }
        Map<String, List<Map<String,Object>>> returninfo = change(returnlist,"merchid");
        model.addAttribute("returninfo",returninfo);
        model.addAttribute("totalpage",lmordersList.size()/Integer.parseInt(pagesize)+1);
        model.addAttribute("totalsize",lmordersList.size());
        model.addAttribute("thissize",returnlist.size());
        return new ModelAndView("order/statusorderlist");
    }

    @RequestMapping("closedlist")
    public ModelAndView closedlist(HttpServletRequest request, Model model){
        String page = StringUtils.isEmpty(request.getParameter("page"))?"1":request.getParameter("page");
        String pagesize = StringUtils.isEmpty(request.getParameter("pagesize"))?"20":request.getParameter("pagesize");
        String orderid = StringUtils.isEmpty(request.getParameter("orderid"))?"":request.getParameter("orderid");
        String paystatus = StringUtils.isEmpty(request.getParameter("paystatus"))?"":request.getParameter("paystatus");
        String startdate = StringUtils.isEmpty(request.getParameter("startdate"))?"":request.getParameter("startdate");
        String enddate = StringUtils.isEmpty(request.getParameter("enddate"))?"":request.getParameter("enddate");
        String timetype = StringUtils.isEmpty(request.getParameter("timetype"))?"":request.getParameter("timetype");
        Map<String,String> condient = new HashMap<String, String>(16);
        condient.put("orderid",orderid);
        condient.put("status","5");
        condient.put("paystatus",paystatus);
        condient.put("startdate",startdate);
        condient.put("enddate",enddate);
        condient.put("timetype",timetype);
        model.addAttribute("orderid",orderid);
        model.addAttribute("status","5");
        model.addAttribute("paystatus",paystatus);
        model.addAttribute("startdate",startdate);
        model.addAttribute("enddate",enddate);
        model.addAttribute("timetype",timetype);
        model.addAttribute("page",page);
        model.addAttribute("pagesize",pagesize);
        List<Map<String,Object>> lmordersList =lmorderService.findByCondient(condient);
        BigDecimal totalprice = new BigDecimal("0");
        int totalnum = 0;
        for(Map<String,Object> map:lmordersList){
            totalprice = totalprice.add(new BigDecimal(map.get("realpayprice")+""));
            totalnum += Integer.parseInt(map.get("goodnum")+"");
        }
        model.addAttribute("totalnum",totalnum);
        model.addAttribute("totalprice",totalprice);
        List<Map<String,Object>> returnlist =  PageUtil.startPage(lmordersList,Integer.parseInt(page),Integer.parseInt(pagesize));
        for(Map map:returnlist){
            map.put("paystatus",LmOrder.paystatuschangetochinese(map.get("paystatus")));
            map.put("status",LmOrder.statuschangetochinese(map.get("status")));
        }
        Map<String, List<Map<String,Object>>> returninfo = change(returnlist,"merchid");
        model.addAttribute("returninfo",returninfo);
        model.addAttribute("totalpage",lmordersList.size()/Integer.parseInt(pagesize)+1);
        model.addAttribute("totalsize",lmordersList.size());
        model.addAttribute("thissize",returnlist.size());
        return new ModelAndView("order/statusorderlist");
    }

    /**
     * ??????
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("comment")
    public ModelAndView comment(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        id = id.replaceAll(",","");
        String content = StringUtils.isEmpty(request.getParameter("content"))?null:request.getParameter("content");
        model.addAttribute("content",content);
        model.addAttribute("id",id);
        return new ModelAndView("order/ordercomment");
    }

    /**
     * ????????????
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("orderlog")
    public ModelAndView orderlog(HttpServletRequest request, Model model){
        String orderid = StringUtils.isEmpty(request.getParameter("orderid"))?null:request.getParameter("orderid");
        List<LmOrderLog> orderLogList = lmOrderLogService.findByOrderid(orderid);
        model.addAttribute("orderLogList",orderLogList);
        return new ModelAndView("order/orderlog");
    }

    /**
     *????????????
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("editpage")
    public ModelAndView editpage(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        id = id.replaceAll(",","");
        LmOrder order = lmorderService.findById(id);
        model.addAttribute("order",order);
        return new ModelAndView("order/ordereditpage");
    }

    /**
     * ????????????
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("detail")
    public ModelAndView detail(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        id = id.replaceAll(",","");
        Map<String,Object> orderinfo =lmorderService.findInfoById(id);
        orderinfo.put("paystatus",LmOrder.paystatuschangetochinese(orderinfo.get("paystatus")));
        orderinfo.put("status",LmOrder.statuschangetochinese(orderinfo.get("status")));
        model.addAttribute("order",orderinfo);
        return new ModelAndView("order/orderdetail");
    }


    @RequestMapping("saveedit")
    @ResponseBody
    public JsonResult saveedit(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        id = id.replaceAll(",","");
        String totalprice = StringUtils.isEmpty(request.getParameter("totalprice"))?null:request.getParameter("totalprice");
        String payexpressprice = StringUtils.isEmpty(request.getParameter("payexpressprice"))?null:request.getParameter("payexpressprice");
        String realpayprice = StringUtils.isEmpty(request.getParameter("realpayprice"))?null:request.getParameter("realpayprice");
        String realexpressprice = StringUtils.isEmpty(request.getParameter("realexpressprice"))?null:request.getParameter("realexpressprice");

        try {
            LmOrder lmOrder = lmorderService.findById(id);
            lmOrder.setTotalprice(new BigDecimal(totalprice));
            lmOrder.setPayexpressprice(new BigDecimal(payexpressprice));
            lmOrder.setRealexpressprice(new BigDecimal(realexpressprice));
            lmOrder.setRealpayprice(new BigDecimal(realpayprice));
            lmorderService.updateService(lmOrder);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }


    @RequestMapping("commentsave")
    @ResponseBody
    public JsonResult commentsave(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        id = id.replaceAll(",","");
        String comment = StringUtils.isEmpty(request.getParameter("comment"))?"":request.getParameter("comment");
        try {
            LmOrder lmOrder = lmorderService.findById(id);
            lmOrder.setOrdercomment(comment);
            lmorderService.updateService(lmOrder);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }


    /**
     * ??????????????????
     * @param request
     * @return
     */
    @RequestMapping("batchoption")
    @ResponseBody
    @Transactional
    public JsonResult batchoption(HttpServletRequest request){
        String ids = StringUtils.isEmpty(request.getParameter("ids"))?null:request.getParameter("ids");
        String operate = StringUtils.isEmpty(request.getParameter("operate"))?null:request.getParameter("operate");
        try {
            //????????????
            if("delete".equals(operate)){
                String [] orderpkeys = ids.split(",");
                for(String id:orderpkeys){
                    lmorderService.delete(id);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }

    private static Map<String, List<Map<String,Object>>> change(List<Map<String, Object>> list, String oneMapKey) {
        Map<String, List<Map<String,Object>>> resultMap = new HashMap<String, List<Map<String,Object>>>();
        Set setTmp = new HashSet();
        for (Map<String, Object> tmp : list) {
            setTmp.add(tmp.get(oneMapKey));
        }
        Iterator it = setTmp.iterator();
        while (it.hasNext()) {
            String oneSetTmpStr = it.next()+"";
            List<Map<String, Object>> oneSetTmpList = new ArrayList<Map<String, Object>>();
            for (Map<String, Object> tmp : list) {
                String oneMapValueStr =  tmp.get(oneMapKey)+"";
                if (oneMapValueStr.equals(oneSetTmpStr)) {
                    oneSetTmpList.add(tmp);
                }
            }
            resultMap.put(oneSetTmpStr, oneSetTmpList);
        }
        return resultMap;
    }

    @RequestMapping("addexpressinfo")
    public ModelAndView addexpressinfo(HttpServletRequest request, Model model){
        String orderid = StringUtils.isEmpty(request.getParameter("orderid"))?null:request.getParameter("orderid");
        model.addAttribute("orderid",orderid);
        List<LmExpress> expressList = lmExpressService.findActiveList();
        model.addAttribute("expressList",expressList);
        return new ModelAndView("order/addexpressinfo");
    }

    @RequestMapping("editexpressinfo")
    public ModelAndView editexpressinfo(HttpServletRequest request, Model model){
        String orderid = StringUtils.isEmpty(request.getParameter("orderid"))?null:request.getParameter("orderid");
        Map<String, Object>  expressinfo = lmOrderExpressService.findByOrderid(orderid);
        model.addAttribute("expressinfo",expressinfo);
        List<LmExpress> expressList = lmExpressService.findActiveList();
        List<Map<String,Object>> list = new ArrayList<>();
        for(LmExpress express:expressList){
            Map<String,Object> map = new HashMap<>();
            map.put("id",express.getId());
            map.put("name",express.getName());
            if(expressinfo!=null){
                String expressid = expressinfo.get("expressid")+"";
                if(expressid.equals(express.getId()+"")){
                    map.put("isselected","selected");
                }else{
                    map.put("isselected","");
                }
            }else{
                map.put("isselected","");
            }

            list.add(map);
        }
        model.addAttribute("expressList",list);
        return new ModelAndView("order/editexpressinfo");
    }
    @RequestMapping("expressedit")
    @ResponseBody
    public JsonResult expressedit(HttpServletRequest request){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        id = id.replaceAll(",","");
        String deliverytype = StringUtils.isEmpty(request.getParameter("deliverytype"))?"0":request.getParameter("deliverytype");
        String sendphone = StringUtils.isEmpty(request.getParameter("sendphone"))?null:request.getParameter("sendphone");
        String sendname = StringUtils.isEmpty(request.getParameter("sendname"))?null:request.getParameter("sendname");
        String expressid = StringUtils.isEmpty(request.getParameter("expressid"))?null:request.getParameter("expressid");
        String expressorderid = StringUtils.isEmpty(request.getParameter("expressorderid"))?null:request.getParameter("expressorderid");
        try {
            LmOrderExpress lmOrderExpress = lmOrderExpressService.findByPKey(id);
            lmOrderExpress.setDeliverytype(Integer.parseInt(deliverytype));
            lmOrderExpress.setExpressid(Integer.parseInt(expressid));
            lmOrderExpress.setSendname(sendname);
            lmOrderExpress.setSendphone(sendphone);
            lmOrderExpress.setExpressorderid(expressorderid);
            lmOrderExpressService.updateService(lmOrderExpress);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }

    /**
     * ??????
     * @param request
     * @return
     */
    @RequestMapping("expressadd")
    @ResponseBody
    @Transactional
    public JsonResult expressadd(HttpServletRequest request ){
        String orderid = StringUtils.isEmpty(request.getParameter("orderid"))?"":request.getParameter("orderid");
        String deliverytype = StringUtils.isEmpty(request.getParameter("deliverytype"))?"":request.getParameter("deliverytype");
        String sendphone = StringUtils.isEmpty(request.getParameter("sendphone"))?"":request.getParameter("sendphone");
        String sendname = StringUtils.isEmpty(request.getParameter("sendname"))?"":request.getParameter("sendname");
        String expressid = StringUtils.isEmpty(request.getParameter("expressid"))?"":request.getParameter("expressid");
        String expressorderid = StringUtils.isEmpty(request.getParameter("expressorderid"))?"":request.getParameter("expressorderid");
        try {
            LmOrder lmOrder = lmorderService.findByOrderId(orderid);
            LmOrderExpress lmOrderExpress = new LmOrderExpress();
            lmOrderExpress.setDeliverytype(Integer.parseInt(deliverytype));
            if(!StringUtils.isEmpty(expressid)){
                lmOrderExpress.setExpressid(Integer.parseInt(expressid));
            }
            if(!StringUtils.isEmpty(expressorderid)){
                lmOrderExpress.setExpressorderid(expressorderid);
            }
            if(!StringUtils.isEmpty(sendname)){
                lmOrderExpress.setSendname(sendname);
            }
            if(!StringUtils.isEmpty(sendphone)){
                lmOrderExpress.setSendphone(sendphone);
            }
            lmOrderExpress.setOrderid(orderid);
            lmOrderExpressService.insertService(lmOrderExpress);
            //???????????????????????????
            lmOrder.setStatus("2");
            lmorderService.updateService(lmOrder);
            LmOrderLog lmOrderLog = new LmOrderLog();
            lmOrderLog.setOperate("????????????");
            lmOrderLog.setOperatedate(new Date());
            lmOrderLog.setOrderid(orderid);
            lmOrderLogService.insert(lmOrderLog);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new JsonResult(e);
        }
        return new JsonResult();
    }

    /**
     * ??????
     * @param request
     * @return
     */
    @RequestMapping("takegood")
    @ResponseBody
    @Transactional
    public JsonResult takegood(HttpServletRequest request ){
        String id = StringUtils.isEmpty(request.getParameter("id"))?"":request.getParameter("id");
        id = id.replaceAll(",","");
        try {
            LmOrder lmOrder = lmorderService.findById(id);
            if(lmOrder!=null){
                lmOrder.setStatus("4");
                lmOrder.setFinishtime(new Date());
                lmorderService.updateService(lmOrder);
                //???????????????
                LmMember lmMember = lmMemberService.findById(lmOrder.getMemberid()+"");
                int price = lmOrder.getRealpayprice().intValue();
                if(price>=12){
                    int growvalue = price/12;
                    lmMember.setGrowth_value(lmMember.getGrowth_value()+growvalue);
                    lmMemberService.updateService(lmMember);
                }
                //?????????
                List<LmMemberLevel> list = lmMemberLevelService.findAll();
                for(LmMemberLevel lmMemberLevel:list){
                    if(lmMember.getGrowth_value()>=lmMemberLevel.getGrowth_value()){
                        lmMember.setLevel_id(lmMemberLevel.getId());
                    }
                }
                //???????????????
                LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(lmOrder.getMerchid()+"");
                //??????????????????????????????
                List<Map<String, String>> orderList = lmorderService.findListByMerchidByApi(lmOrder.getMerchid());
                Double successnum = 0.00;
                for(Map<String, String> map:orderList){
                    String status = map.get("status");
                    if("3".equals(status)||"4".equals(status)){
                        successnum++;
                    }
                }
                lmMerchInfo.setSuccessper(successnum/orderList.size());
                lmMemberService.updateService(lmMember);
                //??????????????? ?????????????????????
                lmMerchInfo.setCredit( ( null==lmMerchInfo.getCredit()?BigDecimal.ZERO:lmMerchInfo.getCredit()).add(lmOrder.getRealpayprice()));
                lmMerchInfoService.updateService(lmMerchInfo);
            }

            LmOrderLog lmOrderLog = new LmOrderLog();
            lmOrderLog.setOperate("????????????");
            lmOrderLog.setOperatedate(new Date());
            lmOrderLog.setOrderid(lmOrder.getId()+"");
            lmOrderLogService.insert(lmOrderLog);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new JsonResult(e);
        }
        return new JsonResult();
    }

    /**
     * ??????
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("refund")
    public ModelAndView refund(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        id = id.replaceAll(",","");
        LmOrder lmOrder = lmorderService.findById(id);
        model.addAttribute("lmOrder",lmOrder);
        return new ModelAndView("order/refund");
    }


    /**
     * ??????
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("refundsave")
    @ResponseBody
    @Transactional
    public JsonResult refundsave(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        id = id.replaceAll(",","");
        String backprice = StringUtils.isEmpty(request.getParameter("backprice"))?null:request.getParameter("backprice");

        try {
            LmOrder lmOrder = lmorderService.findById(id);
            lmOrder.setBackprice(new BigDecimal(backprice));
            //???????????????
            lmOrder.setBackstatus(1);
            lmorderService.updateService(lmOrder);
            LmOrderLog lmOrderLog = new LmOrderLog();
            lmOrderLog.setOperate("????????????");
            lmOrderLog.setOperatedate(new Date());
            lmOrderLog.setOrderid(lmOrder.getId()+"");
            lmOrderLogService.insert(lmOrderLog);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new JsonResult(e);
        }
        return new JsonResult();
    }


    /**
     * ????????????
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("refundsuccess")
    @ResponseBody
    public JsonResult refundsuccess(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        id = id.replaceAll(",","");
        try {
            LmOrder lmOrder = lmorderService.findById(id);
            //???????????????
            lmOrder.setBackstatus(2);
            //??????????????????
            lmOrder.setStatus("5");
            lmorderService.updateService(lmOrder);
            lmorderService.updateService(lmOrder);
            LmOrderLog lmOrderLog = new LmOrderLog();
            lmOrderLog.setOperate("????????????");
            lmOrderLog.setOperatedate(new Date());
            lmOrderLog.setOrderid(lmOrder.getId()+"");
            lmOrderLogService.insert(lmOrderLog);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new JsonResult(e);
        }
        return new JsonResult();
    }
}
