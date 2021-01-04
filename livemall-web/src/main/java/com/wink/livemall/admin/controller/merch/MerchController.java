package com.wink.livemall.admin.controller.merch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wink.livemall.admin.controller.marketing.MarketController;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.PageUtil;
import com.wink.livemall.member.dto.LmMember;
import com.wink.livemall.member.service.LmMemberService;
import com.wink.livemall.merch.dto.LmMerchApplyInfo;
import com.wink.livemall.merch.dto.LmMerchConfigs;
import com.wink.livemall.merch.dto.LmMerchInfo;
import com.wink.livemall.merch.service.LmMerchApplyInfoService;
import com.wink.livemall.merch.service.LmMerchConfigsService;
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
import java.util.*;

/**
 * 后台商户管理列表和验证
 */
@Controller
@RequestMapping("merch")
public class MerchController {
    private Logger LOG= LoggerFactory.getLogger(MerchController.class);

    @Autowired
    private LmMerchInfoService lmMerchInfoService;
    @Autowired
    private LmMerchApplyInfoService lmMerchApplyInfoService;
    @Autowired
    private LmMemberService lmMemberService;
    @Autowired
    private LmMerchConfigsService lmMerchConfigsService;
    /**
     * 商户列表信息
     * @return
     */
    @RequestMapping("query")
    public ModelAndView queryinfo(HttpServletRequest request, Model model){
        String store_name = StringUtils.isEmpty(request.getParameter("store_name"))?null:request.getParameter("store_name");
        String page = StringUtils.isEmpty(request.getParameter("page"))?"1":request.getParameter("page");
        String pagesize = StringUtils.isEmpty(request.getParameter("pagesize"))?"20":request.getParameter("pagesize");
        model.addAttribute("store_name",store_name);
        model.addAttribute("page",page);
        model.addAttribute("pagesize",pagesize);
        Map<String,String> condient = new HashMap<>(16);
        condient.put("store_name",store_name);
        List<LmMerchInfo> lmMerchInfoList = lmMerchInfoService.findByCondient(condient);
        //商品信息
        model.addAttribute("returninfo", PageUtil.startPage(lmMerchInfoList,Integer.parseInt(page),Integer.parseInt(pagesize)));
        //总页数
        model.addAttribute("totalpage",lmMerchInfoList.size()/Integer.parseInt(pagesize)+1);
        model.addAttribute("totalsize",lmMerchInfoList.size());

        return new ModelAndView("merch/merchlist");
    }


    /**
     * 添加商户
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("addpage")
    public ModelAndView addpage(HttpServletRequest request, Model model){

        List<LmMember> lmMemberList = lmMemberService.findAll();
        model.addAttribute("lmMemberList",lmMemberList);
        return new ModelAndView("/merch/merchaddpage");
    }


    /**
     * 添加商户
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("insert")
    @ResponseBody
    public JsonResult insertmerchinfo(HttpServletRequest request, Model model){
        String avatar = StringUtils.isEmpty(request.getParameter("avatar"))?"":request.getParameter("avatar");
        String store_name = StringUtils.isEmpty(request.getParameter("store_name"))?"":request.getParameter("store_name");
        String description = StringUtils.isEmpty(request.getParameter("description"))?"":request.getParameter("description");
        String bg_image = StringUtils.isEmpty(request.getParameter("bg_image"))?"":request.getParameter("bg_image");
        String mobile = StringUtils.isEmpty(request.getParameter("mobile"))?"":request.getParameter("mobile");
        String state = StringUtils.isEmpty(request.getParameter("state"))?"0":request.getParameter("state");
        String member_id = StringUtils.isEmpty(request.getParameter("member_id"))?"0":request.getParameter("member_id");
        String weixin = StringUtils.isEmpty(request.getParameter("weixin"))?"":request.getParameter("weixin");
        try {
            LmMerchInfo lmMerchInfo= new LmMerchInfo();
            lmMerchInfo.setState(Integer.parseInt(state));
            lmMerchInfo.setAvatar(avatar);
            lmMerchInfo.setMobile(mobile);
            lmMerchInfo.setStore_name(store_name);
            lmMerchInfo.setBg_image(bg_image);
            lmMerchInfo.setCreate_at(new Date());
            lmMerchInfo.setDescription(description);
            lmMerchInfo.setWeixin(weixin);
            lmMerchInfo.setMember_id(Integer.parseInt(member_id));
            lmMerchInfo.setUpdate_at(new Date());
            lmMerchInfoService.insertService(lmMerchInfo);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }


    /**
     * 修改商户
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("editpage")
    public ModelAndView editpage(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(id);
        List<LmMember> lmMemberList = lmMemberService.findAll();
        model.addAttribute("lmMerchInfo",lmMerchInfo);
        model.addAttribute("lmMemberList",lmMemberList);
        return new ModelAndView("/merch/mercheditpage");
    }

    /**
     * 修改商户信息
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("edit")
    @ResponseBody
    public JsonResult edit(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        String avatar = StringUtils.isEmpty(request.getParameter("avatar"))?"":request.getParameter("avatar");
        String store_name = StringUtils.isEmpty(request.getParameter("store_name"))?"":request.getParameter("store_name");
        String description = StringUtils.isEmpty(request.getParameter("description"))?"":request.getParameter("description");
        String bg_image = StringUtils.isEmpty(request.getParameter("bg_image"))?"":request.getParameter("bg_image");
        String mobile = StringUtils.isEmpty(request.getParameter("mobile"))?"":request.getParameter("mobile");
        String state = StringUtils.isEmpty(request.getParameter("state"))?"0":request.getParameter("state");
        String weixin = StringUtils.isEmpty(request.getParameter("weixin"))?"":request.getParameter("weixin");

        try {
            LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(id);
            lmMerchInfo.setState(Integer.parseInt(state));
            lmMerchInfo.setAvatar(avatar);
            lmMerchInfo.setMobile(mobile);
            lmMerchInfo.setStore_name(store_name);
            lmMerchInfo.setWeixin(weixin);
            lmMerchInfo.setBg_image(bg_image);
            lmMerchInfo.setUpdate_at(new Date());
            lmMerchInfo.setDescription(description);
            lmMerchInfoService.updateService(lmMerchInfo);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }
    /**
     * 商户认证列表
     * @return
     */
    @RequestMapping("applyquery")
    public ModelAndView applyquery(HttpServletRequest request, Model model){
        String realname = StringUtils.isEmpty(request.getParameter("true_name"))?null:request.getParameter("true_name");
        String page = StringUtils.isEmpty(request.getParameter("page"))?"1":request.getParameter("page");
        String pagesize = StringUtils.isEmpty(request.getParameter("pagesize"))?"20":request.getParameter("pagesize");

        model.addAttribute("true_name",realname);
        model.addAttribute("page",page);
        model.addAttribute("pagesize",pagesize);
        Map<String,String> condient = new HashMap<>(16);
        condient.put("realname",realname);
        List<Map<String,Object>> lmMerchInfoList = lmMerchInfoService.findByCondient2(condient);
        //商品信息
        model.addAttribute("returninfo", PageUtil.startPage(lmMerchInfoList,Integer.parseInt(page),Integer.parseInt(pagesize)));
        //总页数
        model.addAttribute("totalpage",lmMerchInfoList.size()/Integer.parseInt(pagesize)+1);
        model.addAttribute("totalsize",lmMerchInfoList.size());

        return new ModelAndView("merch/applymerchlist");
    }

    /**
     * 修改商户
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("applyeditpage")
    public ModelAndView applyeditpage(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(id);
        model.addAttribute("lmMerchInfo",lmMerchInfo);
        return new ModelAndView("/merch/applyeditpage");
    }
    /**
     * 修改商户信息
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("applyqueryedit")
    @ResponseBody
    public JsonResult applyqueryedit(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        String categoryid = StringUtils.isEmpty(request.getParameter("categoryid"))?"0":request.getParameter("categoryid");
        String state = StringUtils.isEmpty(request.getParameter("state"))?"":request.getParameter("state");
        String ischipped = StringUtils.isEmpty(request.getParameter("ischipped"))?"":request.getParameter("ischipped");
        String islive = StringUtils.isEmpty(request.getParameter("islive"))?"":request.getParameter("islive");
        String isoem = StringUtils.isEmpty(request.getParameter("isoem"))?"":request.getParameter("isoem");
        String refund = StringUtils.isEmpty(request.getParameter("refund"))?"":request.getParameter("refund");
        String postage = StringUtils.isEmpty(request.getParameter("postage"))?"":request.getParameter("postage");
        String isquality = StringUtils.isEmpty(request.getParameter("isquality"))?"":request.getParameter("isquality");
        String isdirect = StringUtils.isEmpty(request.getParameter("isdirect"))?"":request.getParameter("isdirect");
        String isauction = StringUtils.isEmpty(request.getParameter("isauction"))?"":request.getParameter("isauction");
        try {
            LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(id);
            lmMerchInfo.setState(Integer.parseInt(state));
            lmMerchInfo.setType(Integer.parseInt(categoryid));
            if(!StringUtils.isEmpty(isauction)){
                lmMerchInfo.setIsauction(Integer.parseInt(isauction));
            }
            if(!StringUtils.isEmpty(isdirect)){
                lmMerchInfo.setIsdirect(Integer.parseInt(isdirect));
            }
            if(!StringUtils.isEmpty(isquality)){
                lmMerchInfo.setIsquality(Integer.parseInt(isquality));
            }
            if(!StringUtils.isEmpty(postage)){
                lmMerchInfo.setPostage(Integer.parseInt(postage));
            }
            if(!StringUtils.isEmpty(refund)){
                lmMerchInfo.setRefund(Integer.parseInt(refund));
            }
            if(!StringUtils.isEmpty(isoem)){
                lmMerchInfo.setIsoem(Integer.parseInt(isoem));
            }
            if(!StringUtils.isEmpty(islive)){
                lmMerchInfo.setIslive(Integer.parseInt(islive));
            }
            if(!StringUtils.isEmpty(ischipped)){
                lmMerchInfo.setIschipped(Integer.parseInt(ischipped));
            }
            lmMerchInfoService.updateService(lmMerchInfo);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }

    @RequestMapping("baseSetting")
    public ModelAndView baseSetting(HttpServletRequest request,Model model){
        LmMerchConfigs lmMerchConfigs = lmMerchConfigsService.findByType("基础配置");
        Map<String,String> config = new HashMap<>();
        if(lmMerchConfigs!=null){
            String configs = lmMerchConfigs.getConfig();
            if(!StringUtils.isEmpty(configs)){
                config = JSONObject.parseObject(configs,Map.class);
            }else{
                config = null;
            }
            model.addAttribute("id",lmMerchConfigs.getId());
        }else{
            lmMerchConfigs = new LmMerchConfigs();
            lmMerchConfigs.setType("基础配置");
            lmMerchConfigs.setCreated_at(new Date());
            lmMerchConfigs.setConfig("");
            lmMerchConfigsService.insertService(lmMerchConfigs);
            model.addAttribute("id",lmMerchConfigs.getId());
            config = null;
        }
        model.addAttribute("config",config);
        return new ModelAndView("merch/baseSetting");
    }

    @ResponseBody
    @RequestMapping("baseSettingsave")
    public JsonResult baseSettingsave(HttpServletRequest request){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        String enable = StringUtils.isEmpty(request.getParameter("enable"))?null:request.getParameter("enable");
        String is_deduct_commission = StringUtils.isEmpty(request.getParameter("is_deduct_commission"))?null:request.getParameter("is_deduct_commission");
        String settlement_rate = StringUtils.isEmpty(request.getParameter("settlement_rate"))?null:request.getParameter("settlement_rate");
        String withdraw_weixin = StringUtils.isEmpty(request.getParameter("withdraw_weixin"))?null:request.getParameter("withdraw_weixin");
        String withdraw_alipay = StringUtils.isEmpty(request.getParameter("withdraw_alipay"))?null:request.getParameter("withdraw_alipay");
        String withdraw_bank = StringUtils.isEmpty(request.getParameter("withdraw_bank"))?null:request.getParameter("withdraw_bank");
        String withdraw_limit = StringUtils.isEmpty(request.getParameter("withdraw_limit"))?null:request.getParameter("withdraw_limit");
        String integrity_text = StringUtils.isEmpty(request.getParameter("integrity_text"))?null:request.getParameter("integrity_text");
        String integrity_money = StringUtils.isEmpty(request.getParameter("integrity_money"))?null:request.getParameter("integrity_money");
        String integrity_approve = StringUtils.isEmpty(request.getParameter("integrity_approve"))?null:request.getParameter("integrity_approve");
        String integrity_service = StringUtils.isEmpty(request.getParameter("integrity_service"))?null:request.getParameter("integrity_service");
        try {
            if(!StringUtils.isEmpty(id)){
                LmMerchConfigs lmMerchConfigs = lmMerchConfigsService.findById(id);
                Map<String,String> map = new HashMap<>();
                map.put("enable",enable);
                map.put("is_deduct_commission",is_deduct_commission);
                map.put("settlement_rate",settlement_rate);
                map.put("withdraw_weixin",withdraw_weixin);
                map.put("withdraw_alipay",withdraw_alipay);
                map.put("withdraw_bank",withdraw_bank);
                map.put("withdraw_limit",withdraw_limit);
                map.put("integrity_text",integrity_text);
                map.put("integrity_money",integrity_money);
                map.put("integrity_approve",integrity_approve);
                map.put("integrity_service",integrity_service);
                String config = JSONObject.toJSONString(map);
                lmMerchConfigs.setConfig(config);
                lmMerchConfigs.setUpdated_at(new Date());
                lmMerchConfigsService.updateService(lmMerchConfigs);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
            return new JsonResult(e);
        }
        return new JsonResult();
    }


    @RequestMapping("intoSetting")
    public ModelAndView intoSetting(HttpServletRequest request,Model model)
    {
        LmMerchConfigs lmMerchConfigs = lmMerchConfigsService.findByType("入驻配置");
        Map<String,String> config = new HashMap<>();
        if(lmMerchConfigs!=null){
            String configs = lmMerchConfigs.getConfig();
            if(!StringUtils.isEmpty(configs)){
                config = JSONObject.parseObject(configs,Map.class);
            }else{
                config = null;
            }
            model.addAttribute("id",lmMerchConfigs.getId());
        }else{
            lmMerchConfigs = new LmMerchConfigs();
            lmMerchConfigs.setType("入驻配置");
            lmMerchConfigs.setCreated_at(new Date());
            lmMerchConfigs.setConfig("");
            lmMerchConfigsService.insertService(lmMerchConfigs);
            model.addAttribute("id",lmMerchConfigs.getId());
            config = null;
        }
        model.addAttribute("config",config);
        return new ModelAndView("merch/into");
    }


    @RequestMapping("pageSetting")
    public ModelAndView pageSetting(HttpServletRequest request,Model model)
    {
        LmMerchConfigs lmMerchConfigs = lmMerchConfigsService.findByType("页面设置");
        Map<String,String> config = new HashMap<>();
        if(lmMerchConfigs!=null){
            String configs = lmMerchConfigs.getConfig();
            if(!StringUtils.isEmpty(configs)){
                config = JSONObject.parseObject(configs,Map.class);
            }else{
                config = null;
            }
            model.addAttribute("id",lmMerchConfigs.getId());
        }else{
            lmMerchConfigs = new LmMerchConfigs();
            lmMerchConfigs.setType("页面设置");
            lmMerchConfigs.setCreated_at(new Date());
            lmMerchConfigs.setUpdated_at(new Date());
            lmMerchConfigs.setConfig("");
            lmMerchConfigsService.insertService(lmMerchConfigs);
            model.addAttribute("id",lmMerchConfigs.getId());
            config = null;
        }
        model.addAttribute("config",config);
        return new ModelAndView("merch/pagesetting");
    }

    @ResponseBody
    @RequestMapping("pageSettingsave")
    public JsonResult pageSettingsave(HttpServletRequest request){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        String title = StringUtils.isEmpty(request.getParameter("title"))?null:request.getParameter("title");
        String subtitle = StringUtils.isEmpty(request.getParameter("subtitle"))?null:request.getParameter("subtitle");
        String content = StringUtils.isEmpty(request.getParameter("content"))?null:request.getParameter("content");
        String modeltitle1 = StringUtils.isEmpty(request.getParameter("modeltitle1"))?null:request.getParameter("modeltitle1");
        String modelcontent1 = StringUtils.isEmpty(request.getParameter("modelcontent1"))?null:request.getParameter("modelcontent1");
        String modeltitle2 = StringUtils.isEmpty(request.getParameter("modeltitle2"))?null:request.getParameter("modeltitle2");
        String modellabel1 = StringUtils.isEmpty(request.getParameter("modellabel1"))?null:request.getParameter("modellabel1");
        String modellabel2 = StringUtils.isEmpty(request.getParameter("modellabel2"))?null:request.getParameter("modellabel2");
        String modellabel3 = StringUtils.isEmpty(request.getParameter("modellabel3"))?null:request.getParameter("modellabel3");
        String modelsublabel1 = StringUtils.isEmpty(request.getParameter("modelsublabel1"))?null:request.getParameter("modelsublabel1");
        String modelsublabel2 = StringUtils.isEmpty(request.getParameter("modelsublabel2"))?null:request.getParameter("modelsublabel2");
        String modelsublabel3 = StringUtils.isEmpty(request.getParameter("modelsublabel3"))?null:request.getParameter("modelsublabel3");
        String modellabelpic1 = StringUtils.isEmpty(request.getParameter("modellabelpic1"))?null:request.getParameter("modellabelpic1");
        String modellabelpic2 = StringUtils.isEmpty(request.getParameter("modellabelpic2"))?null:request.getParameter("modellabelpic2");
        String modellabelpic3 = StringUtils.isEmpty(request.getParameter("modellabelpic3"))?null:request.getParameter("modellabelpic3");
        String label1 = StringUtils.isEmpty(request.getParameter("label1"))?null:request.getParameter("label1");
        String label2 = StringUtils.isEmpty(request.getParameter("label2"))?null:request.getParameter("label2");
        String label3 = StringUtils.isEmpty(request.getParameter("label3"))?null:request.getParameter("label3");
        String label4 = StringUtils.isEmpty(request.getParameter("label4"))?null:request.getParameter("label4");
        String label5 = StringUtils.isEmpty(request.getParameter("label5"))?null:request.getParameter("label5");
        String label6 = StringUtils.isEmpty(request.getParameter("label6"))?null:request.getParameter("label6");
        String sublabel1 = StringUtils.isEmpty(request.getParameter("sublabel1"))?null:request.getParameter("sublabel1");
        String sublabel2 = StringUtils.isEmpty(request.getParameter("sublabel2"))?null:request.getParameter("sublabel2");
        String sublabel3 = StringUtils.isEmpty(request.getParameter("sublabel3"))?null:request.getParameter("sublabel3");
        String sublabel4 = StringUtils.isEmpty(request.getParameter("sublabel4"))?null:request.getParameter("sublabel4");
        String sublabel5 = StringUtils.isEmpty(request.getParameter("sublabel5"))?null:request.getParameter("sublabel5");
        String sublabel6 = StringUtils.isEmpty(request.getParameter("sublabel6"))?null:request.getParameter("sublabel6");
        String labelpic1 = StringUtils.isEmpty(request.getParameter("labelpic1"))?null:request.getParameter("labelpic1");
        String labelpic2 = StringUtils.isEmpty(request.getParameter("labelpic2"))?null:request.getParameter("labelpic2");
        String labelpic3 = StringUtils.isEmpty(request.getParameter("labelpic3"))?null:request.getParameter("labelpic3");
        String labelpic4 = StringUtils.isEmpty(request.getParameter("labelpic4"))?null:request.getParameter("labelpic4");
        String labelpic5 = StringUtils.isEmpty(request.getParameter("labelpic5"))?null:request.getParameter("labelpic5");
        String labelpic6 = StringUtils.isEmpty(request.getParameter("labelpic6"))?null:request.getParameter("labelpic6");
        try {
            if(!StringUtils.isEmpty(id)){
                LmMerchConfigs lmMerchConfigs = lmMerchConfigsService.findById(id);
                Map<String,String> map = new HashMap<>();
                map.put("title",title);
                map.put("subtitle",subtitle);
                map.put("content",content);
                map.put("modeltitle1",modeltitle1);
                map.put("modelcontent1",modelcontent1);
                map.put("modeltitle2",modeltitle2);
                map.put("modellabel1",modellabel1);
                map.put("modellabel2",modellabel2);
                map.put("modellabel3",modellabel3);
                map.put("modelsublabel1",modelsublabel1);
                map.put("modelsublabel2",modelsublabel2);
                map.put("modelsublabel3",modelsublabel3);
                map.put("modellabelpic1",modellabelpic1);
                map.put("modellabelpic2",modellabelpic2);
                map.put("modellabelpic3",modellabelpic3);
                map.put("label1",label1);
                map.put("label2",label2);
                map.put("label3",label3);
                map.put("label4",label4);
                map.put("label5",label5);
                map.put("label6",label6);
                map.put("sublabel1",sublabel1);
                map.put("sublabel2",sublabel2);
                map.put("sublabel3",sublabel3);
                map.put("sublabel4",sublabel4);
                map.put("sublabel5",sublabel5);
                map.put("sublabel6",sublabel6);
                map.put("labelpic1",labelpic1);
                map.put("labelpic2",labelpic2);
                map.put("labelpic3",labelpic3);
                map.put("labelpic4",labelpic4);
                map.put("labelpic5",labelpic5);
                map.put("labelpic6",labelpic6);
                String config = JSONObject.toJSONString(map);
                lmMerchConfigs.setConfig(config);
                lmMerchConfigs.setUpdated_at(new Date());
                lmMerchConfigsService.updateService(lmMerchConfigs);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }


    @ResponseBody
    @RequestMapping("intoSettingsave")
    public JsonResult intoSettingsave(HttpServletRequest request){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        String open_apply = StringUtils.isEmpty(request.getParameter("open_apply"))?null:request.getParameter("open_apply");
        String apply_limit = StringUtils.isEmpty(request.getParameter("apply_limit"))?null:request.getParameter("apply_limit");
        String apply_limit_pay_money = StringUtils.isEmpty(request.getParameter("apply_limit_pay_money"))?null:request.getParameter("apply_limit_pay_money");
        String apply_limit_buy_monty = StringUtils.isEmpty(request.getParameter("apply_limit_buy_monty"))?null:request.getParameter("apply_limit_buy_monty");
        String need_check = StringUtils.isEmpty(request.getParameter("need_check"))?null:request.getParameter("need_check");
        String valid_type = StringUtils.isEmpty(request.getParameter("valid_type"))?null:request.getParameter("valid_type");
        String valid_month = StringUtils.isEmpty(request.getParameter("valid_month"))?null:request.getParameter("valid_month");
        String intro = StringUtils.isEmpty(request.getParameter("intro"))?null:request.getParameter("intro");
        String is_apply_diyform = StringUtils.isEmpty(request.getParameter("is_apply_diyform"))?null:request.getParameter("is_apply_diyform");
        String apply_diyform = StringUtils.isEmpty(request.getParameter("apply_diyform"))?null:request.getParameter("apply_diyform");
        String open_protocol = StringUtils.isEmpty(request.getParameter("open_protocol"))?null:request.getParameter("open_protocol");
        String apply_protocol_title = StringUtils.isEmpty(request.getParameter("apply_protocol_title"))?null:request.getParameter("apply_protocol_title");
        String apply_protocol_content = StringUtils.isEmpty(request.getParameter("apply_protocol_content"))?null:request.getParameter("apply_protocol_content");

        try {
            if(!StringUtils.isEmpty(id)){
                LmMerchConfigs lmMerchConfigs = lmMerchConfigsService.findById(id);
                Map<String,String> map = new HashMap<>();
                map.put("open_apply",open_apply);
                map.put("apply_limit",apply_limit);
                map.put("apply_limit_pay_money",apply_limit_pay_money);
                map.put("apply_limit_buy_monty",apply_limit_buy_monty);
                map.put("need_check",need_check);
                map.put("valid_type",valid_type);
                map.put("valid_month",valid_month);
                map.put("intro",intro);
                map.put("is_apply_diyform",is_apply_diyform);
                map.put("apply_diyform",apply_diyform);
                map.put("open_protocol",open_protocol);
                map.put("apply_protocol_title",apply_protocol_title);
                map.put("apply_protocol_content",apply_protocol_content);
                String config = JSONObject.toJSONString(map);
                lmMerchConfigs.setConfig(config);
                lmMerchConfigs.setUpdated_at(new Date());
                lmMerchConfigsService.updateService(lmMerchConfigs);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }
    /**
     * 商户认证
     * @return
     */
    @RequestMapping("check")
    @ResponseBody
    public JsonResult check(HttpServletRequest request){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        String state = StringUtils.isEmpty(request.getParameter("state"))?null:request.getParameter("state");
        try {
            lmMerchInfoService.check(id,state);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult(e);
        }
        return new JsonResult();
    }

    /**
     * 商户删除
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public JsonResult delete(HttpServletRequest request){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        try {
            lmMerchInfoService.deleteMerch(id);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }

}
