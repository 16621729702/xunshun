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
            map.put("state",lmCoupons.getState()+"");
            map.put("name",lmCoupons.getName());
            map.put("num",lmCoupons.getNum()+"");
            map.put("left_num",lmCoupons.getLeft_num()+"");
            map.put("start_date", DateUtils.sdf_yMdHms.format(lmCoupons.getStart_date()));
            map.put("end_date",DateUtils.sdf_yMdHms.format(lmCoupons.getEnd_date()));
            map.put("use_num",lmCoupons.getUse_num()+"");
            LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(lmCoupons.getMerch_id()+"");
            if(lmMerchInfo!=null){
                map.put("merchname",lmMerchInfo.getStore_name());
            }
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
        String state = StringUtils.isEmpty(request.getParameter("state"))?"0":request.getParameter("state");
        String name = StringUtils.isEmpty(request.getParameter("name"))?"":request.getParameter("name");
        String num = StringUtils.isEmpty(request.getParameter("num"))?"0":request.getParameter("num");
        String left_num = StringUtils.isEmpty(request.getParameter("left_num"))?"0":request.getParameter("left_num");
        String rate = StringUtils.isEmpty(request.getParameter("rate"))?"0":request.getParameter("rate");
        String useprice = StringUtils.isEmpty(request.getParameter("useprice"))?"0":request.getParameter("useprice");
        String start_date = StringUtils.isEmpty(request.getParameter("start_date"))?null:request.getParameter("start_date");
        String end_date = StringUtils.isEmpty(request.getParameter("end_date"))?null:request.getParameter("end_date");
        String description = StringUtils.isEmpty(request.getParameter("description"))?"":request.getParameter("description");
        String use_num = StringUtils.isEmpty(request.getParameter("use_num"))?"0":request.getParameter("use_num");
        String merch_id =  StringUtils.isEmpty(request.getParameter("merch_id"))?"0":request.getParameter("merch_id");

        try {
            LmCoupons lmCoupons =  new LmCoupons();
            lmCoupons.setCreated_at(new Date());
            lmCoupons.setDescription(description);
            lmCoupons.setEnd_date(DateUtils.sdf_yMdHms.parse(end_date));
            lmCoupons.setUseprice(new BigDecimal(useprice));
            lmCoupons.setLeft_num(Integer.parseInt(left_num));
            lmCoupons.setMerch_id(Integer.parseInt(merch_id));
            lmCoupons.setName(name);
            lmCoupons.setNum(Integer.parseInt(num));
            lmCoupons.setStart_date(DateUtils.sdf_yMdHms.parse(start_date));
            lmCoupons.setState(Integer.parseInt(state));
            lmCoupons.setType(Integer.parseInt(type));
            lmCoupons.setRate(new BigDecimal(rate));
            lmCoupons.setUse_num(Integer.parseInt(use_num));
            lmCouponsService.insertService(lmCoupons);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }


    /**
     * 优惠券添加
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("couponsedit")
    @ResponseBody
    public JsonResult couponsedit(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        String type = StringUtils.isEmpty(request.getParameter("type"))?"0":request.getParameter("type");
        String state = StringUtils.isEmpty(request.getParameter("state"))?"0":request.getParameter("state");
        String name = StringUtils.isEmpty(request.getParameter("name"))?"":request.getParameter("name");
        String num = StringUtils.isEmpty(request.getParameter("num"))?"0":request.getParameter("num");
        String left_num = StringUtils.isEmpty(request.getParameter("left_num"))?"0":request.getParameter("left_num");
        String rate = StringUtils.isEmpty(request.getParameter("rate"))?"0":request.getParameter("rate");
        String useprice = StringUtils.isEmpty(request.getParameter("useprice"))?"0":request.getParameter("useprice");
        String start_date = StringUtils.isEmpty(request.getParameter("start_date"))?null:request.getParameter("start_date");
        String end_date = StringUtils.isEmpty(request.getParameter("end_date"))?null:request.getParameter("end_date");
        String description = StringUtils.isEmpty(request.getParameter("description"))?"":request.getParameter("description");
        String use_num = StringUtils.isEmpty(request.getParameter("use_num"))?"0":request.getParameter("use_num");
        String merch_id =  StringUtils.isEmpty(request.getParameter("merch_id"))?"0":request.getParameter("merch_id");

        try {
            LmCoupons lmCoupons =  lmCouponsService.findById(id);
            lmCoupons.setDescription(description);
            lmCoupons.setEnd_date(DateUtils.sdf_yMdHms.parse(end_date));
            lmCoupons.setRate(new BigDecimal(rate));
            lmCoupons.setLeft_num(Integer.parseInt(left_num));
            lmCoupons.setMerch_id(Integer.parseInt(merch_id));
            lmCoupons.setName(name);
            lmCoupons.setNum(Integer.parseInt(num));
            lmCoupons.setStart_date(DateUtils.sdf_yMdHms.parse(start_date));
            lmCoupons.setState(Integer.parseInt(state));
            lmCoupons.setType(Integer.parseInt(type));
            lmCoupons.setUseprice(new BigDecimal(useprice));
            lmCoupons.setUse_num(Integer.parseInt(use_num));
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
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        try {
            lmCouponsService.deleteService(id);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
            return new JsonResult(e);
        }
        return new JsonResult();
    }


}
