package com.wink.livemall.admin.controller.marketing;

import com.wink.livemall.admin.controller.sysadmin.PermissionController;
import com.wink.livemall.admin.util.DateUtils;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.PageUtil;
import com.wink.livemall.coupon.dto.LmCoupons;
import com.wink.livemall.coupon.service.LmCouponsService;
import com.wink.livemall.help.dto.LmHelpCategory;
import com.wink.livemall.help.dto.LmHelpInfo;
import com.wink.livemall.merch.dto.LmMerchInfo;
import com.wink.livemall.merch.service.LmMerchInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 营销
 */
@Controller
@RequestMapping("/marker")
public class MarketController {
    private Logger LOG= LoggerFactory.getLogger(MarketController.class);

    @Autowired
    private LmCouponsService lmCouponsService;
    @Autowired
    private LmMerchInfoService lmMerchInfoService;


    @RequestMapping("couponsquery")
    public ModelAndView couponsquery(Model model, HttpServletRequest request){
        String page = StringUtils.isEmpty(request.getParameter("page"))?"1":request.getParameter("page");
        String pagesize = StringUtils.isEmpty(request.getParameter("pagesize"))?"20":request.getParameter("pagesize");
        model.addAttribute("page",page);
        model.addAttribute("pagesize",pagesize);
        List<LmCoupons> lmCouponsList = lmCouponsService.findAll();
        List<Map<String,String>> returninfo = new ArrayList<>();
        for(LmCoupons lmCoupons:lmCouponsList){
            Map<String,String> map = new HashMap<>();
            map.put("id",lmCoupons.getId()+"");
            map.put("state",lmCoupons.getStatus()+"");
            map.put("name",lmCoupons.getCouponName());
            map.put("num",lmCoupons.getTotalLimitNum()+"");
            map.put("left_num",lmCoupons.getPersonLimitNum()+"");
            map.put("type",lmCoupons.getType()+"");
            map.put("couponName",lmCoupons.getCouponName());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String start_date=sdf.format(lmCoupons.getSendStartTime());
            String end_date=sdf.format(lmCoupons.getSendEndTime());
            map.put("start_date", start_date);
            map.put("end_date",end_date);
            map.put("merchname","滴雨轩");
            returninfo.add(map);
        }
        model.addAttribute("totalsize",lmCouponsList.size());

        model.addAttribute("returninfo", PageUtil.startPage(returninfo, Integer.parseInt(page), Integer.parseInt(pagesize)));
        model.addAttribute("totalpage",lmCouponsList.size()/Integer.parseInt(pagesize)+1);
        return new ModelAndView("/market/couponslist");
    }

    /**
     * 优惠券添加
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("couponsaddpage")
    public ModelAndView couponsaddpage(HttpServletRequest request, Model model){
        List<LmMerchInfo> lmMerchInfoList = lmMerchInfoService.findAll();
        model.addAttribute("lmMerchInfoList",lmMerchInfoList);
        return new ModelAndView("/market/couponsaddpage");
    }

    /**
     * 优惠券修改
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("couponseditpage")
    public ModelAndView couponseditpage(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?"":request.getParameter("id");
        id = id.replaceAll(",","");
        LmCoupons lmCoupons = lmCouponsService.findById(id);
        List<LmMerchInfo> lmMerchInfoList = lmMerchInfoService.findAll();
        model.addAttribute("lmMerchInfoList",lmMerchInfoList);
        model.addAttribute("lmCoupons",lmCoupons);
        return new ModelAndView("/market/couponseditpage");
    }


    /**
     * 优惠券添加
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("couponsadd")
    @ResponseBody
    public JsonResult couponsadd(HttpServletRequest request, Model model){
        String type = StringUtils.isEmpty(request.getParameter("type"))?"0":request.getParameter("type");
        String status = StringUtils.isEmpty(request.getParameter("status"))?"0":request.getParameter("status");
        String couponName = StringUtils.isEmpty(request.getParameter("couponName"))?"":request.getParameter("couponName");
        String totalLimitNum = StringUtils.isEmpty(request.getParameter("totalLimitNum"))?"0":request.getParameter("totalLimitNum");
        String couponValue = StringUtils.isEmpty(request.getParameter("couponValue"))?"0":request.getParameter("couponValue");
        String minAmount = StringUtils.isEmpty(request.getParameter("minAmount"))?"0":request.getParameter("minAmount");
        String personLimitNum = StringUtils.isEmpty(request.getParameter("personLimitNum"))?"0":request.getParameter("personLimitNum");
        String sendStartTime = StringUtils.isEmpty(request.getParameter("sendStartTime"))?null:request.getParameter("sendStartTime");
        String sendEndTime = StringUtils.isEmpty(request.getParameter("sendEndTime"))?null:request.getParameter("sendEndTime");
        String remark = StringUtils.isEmpty(request.getParameter("remark"))?"":request.getParameter("remark");
        String sellerId =  StringUtils.isEmpty(request.getParameter("sellerId"))?"0":request.getParameter("sellerId");

        try {
            LmCoupons lmCoupons =  new LmCoupons();
            lmCoupons.setCreateTime(new Date());
            lmCoupons.setRemark(remark);
            lmCoupons.setUseEndTime(DateUtils.sdf_yMdHms.parse(sendEndTime));
            lmCoupons.setSendEndTime(DateUtils.sdf_yMdHms.parse(sendEndTime));
            lmCoupons.setCouponValue(new BigDecimal(couponValue));
            lmCoupons.setMinAmount(new BigDecimal(minAmount));
            lmCoupons.setSellerId(Integer.parseInt(sellerId));
            lmCoupons.setCouponName(couponName);
            lmCoupons.setTotalLimitNum(Integer.parseInt(totalLimitNum));
            lmCoupons.setUseStartTime(DateUtils.sdf_yMdHms.parse(sendStartTime));
            lmCoupons.setSendStartTime(DateUtils.sdf_yMdHms.parse(sendStartTime));
            lmCoupons.setStatus(Integer.parseInt(status));
            lmCoupons.setType(Integer.parseInt(type));
            lmCoupons.setPersonLimitNum(Integer.parseInt(personLimitNum));
            lmCoupons.setReceivedNum(0);
            lmCoupons.setProductType(3);
            lmCoupons.setGoodsType(2);
            lmCoupons.setCouponType(0);
            lmCouponsService.insertService(lmCoupons);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }


    /**
     * 优惠券修改
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("couponsedit")
    @ResponseBody
    public JsonResult couponsedit(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        id = id.replaceAll(",","");
        String type = StringUtils.isEmpty(request.getParameter("type"))?"0":request.getParameter("type");
        String status = StringUtils.isEmpty(request.getParameter("status"))?"0":request.getParameter("status");
        String couponName = StringUtils.isEmpty(request.getParameter("couponName"))?"":request.getParameter("couponName");
        String totalLimitNum = StringUtils.isEmpty(request.getParameter("totalLimitNum"))?"0":request.getParameter("totalLimitNum");
        String couponValue = StringUtils.isEmpty(request.getParameter("couponValue"))?"0":request.getParameter("couponValue");
        String minAmount = StringUtils.isEmpty(request.getParameter("minAmount"))?"0":request.getParameter("minAmount");
        String personLimitNum = StringUtils.isEmpty(request.getParameter("personLimitNum"))?"0":request.getParameter("personLimitNum");
        String sendStartTime = StringUtils.isEmpty(request.getParameter("sendStartTime"))?null:request.getParameter("sendStartTime");
        String sendEndTime = StringUtils.isEmpty(request.getParameter("sendEndTime"))?null:request.getParameter("sendEndTime");
        String remark = StringUtils.isEmpty(request.getParameter("remark"))?"":request.getParameter("remark");

        try {
            LmCoupons lmCoupons =  lmCouponsService.findById(id);
            lmCoupons.setCreateTime(new Date());
            lmCoupons.setRemark(remark);
            lmCoupons.setSendEndTime(DateUtils.sdf_yMdHms.parse(sendEndTime));
            lmCoupons.setCouponValue(new BigDecimal(couponValue));
            lmCoupons.setMinAmount(new BigDecimal(minAmount));
            lmCoupons.setCouponName(couponName);
            lmCoupons.setTotalLimitNum(Integer.parseInt(totalLimitNum));
            lmCoupons.setSendStartTime(DateUtils.sdf_yMdHms.parse(sendStartTime));
            lmCoupons.setStatus(Integer.parseInt(status));
            lmCoupons.setType(Integer.parseInt(type));
            lmCoupons.setPersonLimitNum(Integer.parseInt(personLimitNum));
            lmCouponsService.updateService(lmCoupons);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }


    /**
     * 优惠券删除方法
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("couponsdelete")
    @ResponseBody
    public JsonResult couponsdelete(HttpServletRequest request, Model model){
        JsonResult jsonResult = new JsonResult();
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        id = id.replaceAll(",","");
        try {

            LmCoupons lmCoupons = lmCouponsService.findById(id);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date useEndTime = lmCoupons.getUseEndTime();
            long userEndTtime = useEndTime.getTime();

            Date nowDate=new Date();
            nowDate.setTime(nowDate.getTime()+60*60l*1000);
            long nowTime = nowDate.getTime();
                //优惠券已过期
                lmCouponsService.deleteService(id);

        } catch (Exception e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
            return new JsonResult(e);
        }
        return jsonResult;
    }


}
