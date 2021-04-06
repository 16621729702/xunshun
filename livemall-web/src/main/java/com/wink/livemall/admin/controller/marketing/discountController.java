package com.wink.livemall.admin.controller.marketing;

import com.wink.livemall.admin.util.DateUtils;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.PageUtil;
import com.wink.livemall.coupon.dto.LmCoupons;
import com.wink.livemall.coupon.service.LmCouponsService;
import com.wink.livemall.member.dto.LmMemberStart;
import com.wink.livemall.member.service.LmMemberStartService;
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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 开店优惠
 */
@Controller
@RequestMapping("/discount")
public class discountController {
    private Logger LOG= LoggerFactory.getLogger(discountController.class);

    @Autowired
    private LmCouponsService lmCouponsService;
    @Autowired
    private LmMerchInfoService lmMerchInfoService;
    @Autowired
    private LmMemberStartService lmMemberStartService;


    @RequestMapping("discountquery")
    public ModelAndView couponsquery(Model model, HttpServletRequest request){
        String page = StringUtils.isEmpty(request.getParameter("page"))?"1":request.getParameter("page");
        String pagesize = StringUtils.isEmpty(request.getParameter("pagesize"))?"20":request.getParameter("pagesize");
        model.addAttribute("page",page);
        model.addAttribute("pagesize",pagesize);
        List<LmMemberStart> memberStarts = lmMemberStartService.findAll();
        List<Map<String,String>> returninfo = new ArrayList<>();
        for(LmMemberStart lmMemberStart:memberStarts){
            Map<String,String> map = new HashMap<>();
            map.put("id",lmMemberStart.getId()+"");
            map.put("mobile",lmMemberStart.getMobile()+"");
            map.put("margin",lmMemberStart.getMargin());
            map.put("couponPrice",lmMemberStart.getCoupon_price());
            map.put("type",lmMemberStart.getType()+"");
            map.put("isstart",lmMemberStart.getIsstart()+"");
            map.put("businessid",lmMemberStart.getBusinessid()+"");
            returninfo.add(map);
        }
        model.addAttribute("totalsize",memberStarts.size());

        model.addAttribute("returninfo", PageUtil.startPage(returninfo, Integer.parseInt(page), Integer.parseInt(pagesize)));
        model.addAttribute("totalpage",memberStarts.size()/Integer.parseInt(pagesize)+1);
        return new ModelAndView("/market/memberdiscountlist");
    }

    /**
     * 开店优惠添加
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("discountaddpage")
    public ModelAndView couponsaddpage(HttpServletRequest request, Model model){
        List<LmMemberStart> memberStartList = lmMemberStartService.findAll();
        model.addAttribute("memberStartList",memberStartList);
        return new ModelAndView("/market/discountaddpage");
    }

    /**
     * 开店优惠修改
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("discounteditpage")
    public ModelAndView couponseditpage(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?"":request.getParameter("id");
        id = id.replaceAll(",","");
        LmMemberStart lmMemberStart = lmMemberStartService.findById(id);
        List<LmMemberStart> memberStartList = lmMemberStartService.findAll();
        model.addAttribute("memberStartList",memberStartList);
        model.addAttribute("Discount",lmMemberStart);
        return new ModelAndView("/market/discounteditpage");
    }


    /**
     * 开店优惠添加
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("discountadd")
    @ResponseBody
    public JsonResult couponsadd(HttpServletRequest request, Model model){
        String mobile = StringUtils.isEmpty(request.getParameter("mobile"))?"0":request.getParameter("mobile");
        String margin = StringUtils.isEmpty(request.getParameter("margin"))?"0":request.getParameter("margin");
        String couponPrice = StringUtils.isEmpty(request.getParameter("couponPrice"))?"":request.getParameter("couponPrice");
        String type = StringUtils.isEmpty(request.getParameter("type"))?"0":request.getParameter("type");
        String isstart =  StringUtils.isEmpty(request.getParameter("isstart"))?"0":request.getParameter("isstart");
        String businessid =  StringUtils.isEmpty(request.getParameter("businessid"))?"1":request.getParameter("businessid");
        try {
            if(isstart.equals("1")) {
                List<LmMemberStart> byMobile = lmMemberStartService.findByMobile(mobile, Integer.parseInt(type), Integer.parseInt(businessid));
                if (byMobile != null && byMobile.size() > 0) {
                    return new JsonResult(JsonResult.ERROR, "已设置过无需重复设置");
                }
            }
            LmMemberStart lmMemberStart =  new LmMemberStart();
            lmMemberStart.setMobile(mobile);
            lmMemberStart.setMargin(margin);
            lmMemberStart.setCoupon_price(couponPrice);
            lmMemberStart.setType(Integer.parseInt(type));
            lmMemberStart.setIsstart(Integer.parseInt(isstart));
            lmMemberStart.setBusinessid(Integer.parseInt(businessid));
            lmMemberStartService.insert(lmMemberStart);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }


    /**
     * 开店优惠修改
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("discountedit")
    @ResponseBody
    public JsonResult couponsedit(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        id = id.replaceAll(",","");
        String mobile = StringUtils.isEmpty(request.getParameter("mobile"))?"0":request.getParameter("mobile");
        String margin = StringUtils.isEmpty(request.getParameter("margin"))?"0":request.getParameter("margin");
        String couponPrice = StringUtils.isEmpty(request.getParameter("couponPrice"))?"":request.getParameter("couponPrice");
        String type = StringUtils.isEmpty(request.getParameter("type"))?"0":request.getParameter("type");
        String isstart =  StringUtils.isEmpty(request.getParameter("isstart"))?"0":request.getParameter("isstart");
        String businessid =  StringUtils.isEmpty(request.getParameter("businessid"))?"1":request.getParameter("businessid");
        try {
            if(isstart.equals("1")){
                List<LmMemberStart> byMobile = lmMemberStartService.findByMobile(mobile, Integer.parseInt(type), Integer.parseInt(businessid));
                if(byMobile!=null&&byMobile.size()>0){
                    return new JsonResult(JsonResult.ERROR,"已设置过无需重复设置");
                }
            }
            LmMemberStart lmMemberStart =  lmMemberStartService.findById(id);
            lmMemberStart.setMobile(mobile);
            lmMemberStart.setMargin(margin);
            lmMemberStart.setCoupon_price(couponPrice);
            lmMemberStart.setType(Integer.parseInt(type));
            lmMemberStart.setIsstart(Integer.parseInt(isstart));
            lmMemberStart.setBusinessid(Integer.parseInt(businessid));
            lmMemberStartService.updateMobile(lmMemberStart);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }


    /**
     * 开店优惠删除方法
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("discountdelete")
    @ResponseBody
    public JsonResult couponsdelete(HttpServletRequest request, Model model){
        JsonResult jsonResult = new JsonResult();
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        id = id.replaceAll(",","");
        try {
            lmMemberStartService.delete(Integer.parseInt(id));
                jsonResult.setCode(jsonResult.SUCCESS);
                jsonResult.setMsg("删除成功");
        } catch (Exception e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
            return new JsonResult(e);
        }
        return jsonResult;
    }


}
