package com.wink.livemall.admin.controller.member;

import com.wink.livemall.admin.controller.marketing.MarketController;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.Md5Util;
import com.wink.livemall.admin.util.PageUtil;
import com.wink.livemall.coupon.dto.LmCoupons;
import com.wink.livemall.member.dto.*;
import com.wink.livemall.member.service.*;
import com.wink.livemall.merch.dto.LmMerchInfo;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员 用户列表
 */
@Controller
@RequestMapping("member")
public class MemberController {
    private Logger LOG= LoggerFactory.getLogger(MemberController.class);

    @Autowired
    private LmMemberService lmMemberService;
    @Autowired
    private LmMemberLevelService lmMemberLevelService;
    @Autowired
    private LmMemberTraceService lmMemberTraceService;
    @Autowired
    private LmMemberCouponService lmMemberCouponService;
    @Autowired
    private LmMemberFavService lmMemberFavService;
    @Autowired
    private LmMemberFollowService lmMemberFollowService;
    @Autowired
    private LmMemberAddressService lmMemberAddressService;

    /**
     * 会员列表页面
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("query")
    public ModelAndView query(HttpServletRequest request, Model model) {
        String realname = StringUtils.isEmpty(request.getParameter("realname")) ? null : request.getParameter("realname");
        String page = StringUtils.isEmpty(request.getParameter("page")) ? "1" : request.getParameter("page");
        String pagesize = StringUtils.isEmpty(request.getParameter("pagesize")) ? "20" : request.getParameter("pagesize");
        model.addAttribute("realname", realname);
        model.addAttribute("page", page);
        model.addAttribute("pagesize", pagesize);
        Map<String, String> condient = new HashMap<>(16);
        condient.put("realname", realname);
        List<LmMember> lmMemberList = lmMemberService.findByCondient(condient);
        //商品信息
        model.addAttribute("returninfo", PageUtil.startPage(lmMemberList, Integer.parseInt(page), Integer.parseInt(pagesize)));
        //总页数
        model.addAttribute("totalpage", lmMemberList.size() / Integer.parseInt(pagesize) + 1);
        model.addAttribute("totalsize", lmMemberList.size());
        return new ModelAndView("member/memberlist");
    }

    /**
     * 我的足迹信息
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("editpage7")
    public ModelAndView editpage7(HttpServletRequest request, Model model) {
        String id = StringUtils.isEmpty(request.getParameter("id")) ? null : request.getParameter("id");
        if(id!=null){
            //查询用户足迹信息
            List<LmMemberTrace> lmMemberTraceList =lmMemberTraceService.findByMemberid(Integer.parseInt(id));
            model.addAttribute("lmMemberTraceList",lmMemberTraceList);
            model.addAttribute("id",id);
            return new ModelAndView("member/memberdetail7");
        }else{
            return new ModelAndView("error");
        }
    }

    /**
     * 我的关注信息
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("editpage5")
    public ModelAndView editpage5(HttpServletRequest request, Model model) {
        String id = StringUtils.isEmpty(request.getParameter("id")) ? null : request.getParameter("id");
        if(id!=null){
            List<LmMemberFollow> lmMemberFollowList = lmMemberFollowService.findByMemberid(Integer.parseInt(id));
            model.addAttribute("lmMemberFollowList",lmMemberFollowList);
            model.addAttribute("id",id);
            return new ModelAndView("member/memberdetail5");
        }else{
            return new ModelAndView("error");
        }
    }

    /**
     * 我的收藏信息
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("editpage4")
    public ModelAndView editpage4(HttpServletRequest request, Model model) {
        String id = StringUtils.isEmpty(request.getParameter("id")) ? null : request.getParameter("id");
        if(id!=null){
            List<LmMemberFav> lmMemberFavList = lmMemberFavService.findByMemberid(Integer.parseInt(id));
            model.addAttribute("lmMemberFavList",lmMemberFavList);
            model.addAttribute("id",id);
            return new ModelAndView("member/memberdetail4");
        }else{
            return new ModelAndView("error");
        }
    }
    /**
     * 我的优惠券信息
     */
    @RequestMapping("editpage3")
    public ModelAndView editpage3(HttpServletRequest request, Model model) {
        String id = StringUtils.isEmpty(request.getParameter("id")) ? null : request.getParameter("id");
        if(id!=null){

            List<LmMemberCoupon> lmMemberCouponList = lmMemberCouponService.findByMemberid(Integer.parseInt(id));
            model.addAttribute("lmMemberCouponList",lmMemberCouponList);
            model.addAttribute("id",id);
            return new ModelAndView("member/memberdetail3");
        }else{
            return new ModelAndView("error");
        }
    }

    /**
     * 我的地址信息
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("editpage2")
    public ModelAndView editpage2(HttpServletRequest request, Model model) {
        String id = StringUtils.isEmpty(request.getParameter("id")) ? null : request.getParameter("id");
        if(id!=null){
            List<LmMemberAddress> lmMemberAddressList = lmMemberAddressService.findByMemberid(Integer.parseInt(id));
            model.addAttribute("lmMemberAddressList",lmMemberAddressList);
            model.addAttribute("id",id);
            return new ModelAndView("member/memberdetail2");

        }else{
            return new ModelAndView("error");
        }
    }

    /**
     * 会员信息详情
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("editpage")
    public ModelAndView editpage(HttpServletRequest request, Model model) {
        String id = StringUtils.isEmpty(request.getParameter("id")) ? null : request.getParameter("id");
        if(id!=null){
            LmMember lmMember = lmMemberService.findById(id);
            model.addAttribute("lmMember",lmMember);
            model.addAttribute("id",id);
            return new ModelAndView("member/memberdetail");
        }else{
            return new ModelAndView("error");
        }
    }

    @RequestMapping("addpage")
    public ModelAndView addpage(HttpServletRequest request, Model model) {
        return new ModelAndView("/member/memberaddpage");
    }

    /**
     * 会员等级列表
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("levelquery")
    public ModelAndView levelquery(HttpServletRequest request, Model model) {
        String name = StringUtils.isEmpty(request.getParameter("name")) ? null : request.getParameter("name");
        String page = StringUtils.isEmpty(request.getParameter("page")) ? "1" : request.getParameter("page");
        String pagesize = StringUtils.isEmpty(request.getParameter("pagesize")) ? "20" : request.getParameter("pagesize");
        model.addAttribute("name", name);
        model.addAttribute("page", page);
        model.addAttribute("pagesize", pagesize);
        Map<String, String> condient = new HashMap<>(16);
        condient.put("name", name);
        List<LmMemberLevel> lmMemberLevelList = lmMemberLevelService.findByCondient(condient);
        //商品信息
        model.addAttribute("returninfo", PageUtil.startPage(lmMemberLevelList, Integer.parseInt(page), Integer.parseInt(pagesize)));
        //总页数
        model.addAttribute("totalpage", lmMemberLevelList.size() / Integer.parseInt(pagesize) + 1);
        model.addAttribute("totalsize", lmMemberLevelList.size());
        return new ModelAndView("member/memberlevellist");
    }

    /**
     * 会员等级添加页面
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("leveladdpage")
    public ModelAndView leveladdpage(HttpServletRequest request, Model model){
        return new ModelAndView("/member/leveladdpage");
    }

    /**
     * 会员等级修改页面
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("leveleditpage")
    public ModelAndView leveleditpage(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?"":request.getParameter("id");
        LmMemberLevel lmMemberLevel = lmMemberLevelService.findById(id);
        model.addAttribute("lmMemberLevel",lmMemberLevel);
        return new ModelAndView("/member/leveleditpage");
    }

    /**
     * 等级添加
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("leveladd")
    @ResponseBody
    public JsonResult leveladd(HttpServletRequest request, Model model){
        String name = StringUtils.isEmpty(request.getParameter("name"))?"":request.getParameter("name");
        String growth_value = StringUtils.isEmpty(request.getParameter("growth_value"))?"0":request.getParameter("growth_value");
//        String days = StringUtils.isEmpty(request.getParameter("days"))?"0":request.getParameter("days");
        String remark = StringUtils.isEmpty(request.getParameter("remark"))?"":request.getParameter("remark");
        String state = StringUtils.isEmpty(request.getParameter("state"))?"0":request.getParameter("state");
        String code = StringUtils.isEmpty(request.getParameter("code"))?"0":request.getParameter("code");

        try {
            LmMemberLevel lmMemberLevel = new LmMemberLevel();
//            lmMemberLevel.setDays(Integer.parseInt(days));
            lmMemberLevel.setGrowth_value(Integer.parseInt(growth_value));
            lmMemberLevel.setName(name);
            lmMemberLevel.setCode(code);
            lmMemberLevel.setRemark(remark);
            lmMemberLevel.setState(Integer.parseInt(state));
            lmMemberLevelService.insertService(lmMemberLevel);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
            return new JsonResult(e);
        }
        return new JsonResult();
    }

    /**
     * 等级修改
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("leveledit")
    @ResponseBody
    public JsonResult leveledit(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?"":request.getParameter("id");
        String name = StringUtils.isEmpty(request.getParameter("name"))?"":request.getParameter("name");
        String growth_value = StringUtils.isEmpty(request.getParameter("growth_value"))?"0":request.getParameter("growth_value");
//        String days = StringUtils.isEmpty(request.getParameter("days"))?"0":request.getParameter("days");
        String remark = StringUtils.isEmpty(request.getParameter("remark"))?"":request.getParameter("remark");
        String state = StringUtils.isEmpty(request.getParameter("state"))?"0":request.getParameter("state");
        String code = StringUtils.isEmpty(request.getParameter("code"))?"0":request.getParameter("code");

        try {
            LmMemberLevel lmMemberLevel = lmMemberLevelService.findById(id);
//            lmMemberLevel.setDays(Integer.parseInt(days));
            lmMemberLevel.setGrowth_value(Integer.parseInt(growth_value));
            lmMemberLevel.setName(name);
            lmMemberLevel.setCode(code);
            lmMemberLevel.setRemark(remark);
            lmMemberLevel.setState(Integer.parseInt(state));
            lmMemberLevelService.updateService(lmMemberLevel);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }

    /**
     * 等级删除
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("leveldelete")
    @ResponseBody
    public JsonResult leveldelete(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?"":request.getParameter("id");
        try {
            lmMemberLevelService.deleteService(id);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }

    /**
     *
     * 添加会员
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("memberadd")
    @ResponseBody
    public JsonResult memberadd(HttpServletRequest request, Model model){
        String nickname = StringUtils.isEmpty(request.getParameter("nickname"))?"":request.getParameter("nickname");
        String avatar = StringUtils.isEmpty(request.getParameter("avatar"))?"":request.getParameter("avatar");
        String realname = StringUtils.isEmpty(request.getParameter("realname"))?"":request.getParameter("realname");
        String mobile = StringUtils.isEmpty(request.getParameter("mobile"))?"":request.getParameter("mobile");
        String idcard = StringUtils.isEmpty(request.getParameter("idcard"))?"":request.getParameter("idcard");
        String gender = StringUtils.isEmpty(request.getParameter("gender"))?"":request.getParameter("gender");
        String country = StringUtils.isEmpty(request.getParameter("country"))?"":request.getParameter("country");
        String ip = StringUtils.isEmpty(request.getParameter("ip"))?"0":request.getParameter("ip");
        String province = StringUtils.isEmpty(request.getParameter("province"))?"":request.getParameter("province");
        String city = StringUtils.isEmpty(request.getParameter("city"))?"":request.getParameter("city");
        String credit = StringUtils.isEmpty(request.getParameter("credit"))?"0":request.getParameter("credit");
        String usertype = StringUtils.isEmpty(request.getParameter("usertype"))?"0":request.getParameter("usertype");
        String username = StringUtils.isEmpty(request.getParameter("username"))?"":request.getParameter("username");
        String password = StringUtils.isEmpty(request.getParameter("password"))?"":request.getParameter("password");
        String state = StringUtils.isEmpty(request.getParameter("state"))?"0":request.getParameter("state");

        try {
            LmMember lmMember = new LmMember();
            lmMember.setProvince(province);
            lmMember.setAvatar(avatar);
            lmMember.setCity(city);
            lmMember.setState(Integer.parseInt(state));
            lmMember.setCountry(country);
            lmMember.setCreated_at(new Date());
            lmMember.setCredit(Integer.parseInt(credit));
            lmMember.setGender(gender);
            lmMember.setIdcard(idcard);
            lmMember.setMobile(mobile);
            lmMember.setIp(ip);
            lmMember.setUsertype(Integer.parseInt(usertype));
            lmMember.setUpdated_at(new Date());
            lmMember.setRealname(realname);
            lmMember.setPassword(Md5Util.MD5(password));
            lmMember.setUsername(username);
            lmMember.setNickname(nickname);
            lmMemberService.insertService(lmMember);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }

    /**
     * 会员修改基本信息
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("memberedit")
    @ResponseBody
    public JsonResult memberedit(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?"":request.getParameter("id");
        String nickname = StringUtils.isEmpty(request.getParameter("nickname"))?"":request.getParameter("nickname");
        String avatar = StringUtils.isEmpty(request.getParameter("avatar"))?"":request.getParameter("avatar");
        String realname = StringUtils.isEmpty(request.getParameter("realname"))?"":request.getParameter("realname");
        String mobile = StringUtils.isEmpty(request.getParameter("mobile"))?"":request.getParameter("mobile");
        String idcard = StringUtils.isEmpty(request.getParameter("idcard"))?"":request.getParameter("idcard");
        String gender = StringUtils.isEmpty(request.getParameter("gender"))?"":request.getParameter("gender");
        String country = StringUtils.isEmpty(request.getParameter("country"))?"":request.getParameter("country");
        String ip = StringUtils.isEmpty(request.getParameter("ip"))?"0":request.getParameter("ip");
        String province = StringUtils.isEmpty(request.getParameter("province"))?"":request.getParameter("province");
        String city = StringUtils.isEmpty(request.getParameter("city"))?"":request.getParameter("city");
        String credit = StringUtils.isEmpty(request.getParameter("credit"))?"0":request.getParameter("credit");
        String usertype = StringUtils.isEmpty(request.getParameter("usertype"))?"0":request.getParameter("usertype");
        String username = StringUtils.isEmpty(request.getParameter("username"))?"":request.getParameter("username");
        String password = StringUtils.isEmpty(request.getParameter("password"))?"":request.getParameter("password");
        String state = StringUtils.isEmpty(request.getParameter("state"))?"0":request.getParameter("state");

        try {
            LmMember lmMember = lmMemberService.findById(id);
            lmMember.setProvince(province);
            lmMember.setAvatar(avatar);
            lmMember.setCity(city);
            lmMember.setCountry(country);
            lmMember.setCreated_at(new Date());
            lmMember.setCredit(Integer.parseInt(credit));
            lmMember.setGender(gender);
            lmMember.setIdcard(idcard);
            lmMember.setMobile(mobile);
            lmMember.setIp(ip);
            lmMember.setState(Integer.parseInt(state));
            lmMember.setUsertype(Integer.parseInt(usertype));
            lmMember.setUpdated_at(new Date());
            lmMember.setRealname(realname);
            lmMember.setPassword(Md5Util.MD5(password));
            lmMember.setUsername(username);
            lmMember.setNickname(nickname);
            lmMemberService.updateService(lmMember);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }



    @RequestMapping("memberdelete")
    @ResponseBody
    public JsonResult memberdelete(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?"":request.getParameter("id");
        try {
            lmMemberService.deleteService(id);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }
}
