package com.wink.livemall.admin.controller.setting;

import com.alibaba.fastjson.JSONObject;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.sys.setting.dto.Configs;
import com.wink.livemall.sys.setting.service.ConfigsService;
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
import java.util.Map;

/**
 * 设置-系统配置
 */
@Controller
@RequestMapping("tosetting")
public class ToSettingController {
    private Logger LOG= LoggerFactory.getLogger(ToSettingController.class);

    @Autowired
    private ConfigsService configsService;
    /**
     * 基础配置
     * @return
     */
    @RequestMapping("basic")
    public ModelAndView basic(HttpServletRequest request, Model model){
        Configs configs = configsService.findByTypeId(Configs.type_basic);
        Map stringToMap = new HashMap();
        if(configs!=null){
            String config = configs.getConfig();
            stringToMap =  JSONObject.parseObject(config);
        }
        System.out.println(stringToMap);
        model.addAttribute("info",stringToMap);
        model.addAttribute("configs",configs);

        return new ModelAndView("setting/config_setting");
    }
    @RequestMapping("basicupdate")
    @ResponseBody
    public JsonResult basicupdate(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        String site_name = StringUtils.isEmpty(request.getParameter("site_name"))?null:request.getParameter("site_name");
        String logo = StringUtils.isEmpty(request.getParameter("logo"))?null:request.getParameter("logo");
        String consumer_hotline = StringUtils.isEmpty(request.getParameter("consumer_hotline"))?null:request.getParameter("consumer_hotline");
        String auth_mobile = StringUtils.isEmpty(request.getParameter("auth_mobile"))?"0":request.getParameter("auth_mobile");
        String copyright_text = StringUtils.isEmpty(request.getParameter("copyright_text"))?null:request.getParameter("copyright_text");
        String copyright_logo = StringUtils.isEmpty(request.getParameter("copyright_logo"))?null:request.getParameter("copyright_logo");
        String copyright_link = StringUtils.isEmpty(request.getParameter("copyright_link"))?null:request.getParameter("copyright_link");
        try {
            if(id==null){
                Configs configs = new Configs();
                configs.setCreated_at(new Date());
                configs.setUpdated_at(new Date());
                configs.setType(Configs.type_basic);
                Map<String,String> map = new HashMap<>();
                map.put("site_name",site_name);
                map.put("logo",logo);
                map.put("consumer_hotline",consumer_hotline);
                map.put("auth_mobile",auth_mobile);
                map.put("copyright_text",copyright_text);
                map.put("copyright_logo",copyright_logo);
                map.put("copyright_link",copyright_link);
                configs.setConfig(JSONObject.toJSONString(map));
                configsService.insertService(configs);
            }else{
                Configs configs = configsService.findById(Integer.parseInt(id));
                Map<String,String> map = new HashMap<>();
                map.put("site_name",site_name);
                map.put("logo",logo);
                map.put("consumer_hotline",consumer_hotline);
                map.put("auth_mobile",auth_mobile);
                map.put("copyright_text",copyright_text);
                map.put("copyright_logo",copyright_logo);
                map.put("copyright_link",copyright_link);
                configs.setConfig(JSONObject.toJSONString(map));
                configs.setUpdated_at(new Date());
                configsService.updateService(configs);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }


    @RequestMapping("flow")
    public ModelAndView flow(HttpServletRequest request, Model model){
        Configs configs = configsService.findByTypeId(Configs.type_flow);
        Map stringToMap = new HashMap();
        if(configs!=null){
            String config = configs.getConfig();
            stringToMap =  JSONObject.parseObject(config);
        }
        System.out.println(stringToMap);
        model.addAttribute("info",stringToMap);
        model.addAttribute("configs",configs);

        return new ModelAndView("setting/config_setting2");
    }

    @RequestMapping("flowupdate")
    @ResponseBody
    public JsonResult flowupdate(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        String access_key_id = StringUtils.isEmpty(request.getParameter("access_key_id"))?null:request.getParameter("access_key_id");
        String access_secret = StringUtils.isEmpty(request.getParameter("access_secret"))?null:request.getParameter("access_secret");
        String readurl = StringUtils.isEmpty(request.getParameter("readurl"))?null:request.getParameter("readurl");
        String readurl2 = StringUtils.isEmpty(request.getParameter("readurl2"))?null:request.getParameter("readurl2");
        String readurl3 = StringUtils.isEmpty(request.getParameter("readurl3"))?null:request.getParameter("readurl3");
        String pushurl = StringUtils.isEmpty(request.getParameter("pushurl"))?null:request.getParameter("pushurl");
        String hubname = StringUtils.isEmpty(request.getParameter("hubname"))?null:request.getParameter("hubname");
        String key = StringUtils.isEmpty(request.getParameter("key"))?null:request.getParameter("key");
        String vod = StringUtils.isEmpty(request.getParameter("vod"))?null:request.getParameter("vod");

        try {
            if(id==null){
                Configs configs = new Configs();
                configs.setCreated_at(new Date());
                configs.setUpdated_at(new Date());
                configs.setType(Configs.type_flow);
                Map<String,String> map = new HashMap<>();
                map.put("access_key_id",access_key_id);
                map.put("access_secret",access_secret);
                map.put("key",key);
                map.put("readurl",readurl);
                map.put("readurl2",readurl2);
                map.put("readurl3",readurl3);

                map.put("pushurl",pushurl);
                map.put("vod",vod);
                map.put("hubname",hubname);
                configs.setConfig(JSONObject.toJSONString(map));
                configsService.insertService(configs);
            }else{
                Configs configs = configsService.findById(Integer.parseInt(id));
                Map<String,String> map = new HashMap<>();
                map.put("access_key_id",access_key_id);
                map.put("access_secret",access_secret);
                map.put("readurl",readurl);
                map.put("key",key);
                map.put("vod",vod);
                map.put("pushurl",pushurl);
                map.put("hubname",hubname);
                map.put("readurl2",readurl2);
                map.put("readurl3",readurl3);
                configs.setConfig(JSONObject.toJSONString(map));
                configs.setUpdated_at(new Date());
                configsService.updateService(configs);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }


    @RequestMapping("chat")
    public ModelAndView chat(HttpServletRequest request, Model model){
        Configs configs = configsService.findByTypeId(Configs.type_chat);
        Map stringToMap = new HashMap();
        if(configs!=null){
            String config = configs.getConfig();
            stringToMap =  JSONObject.parseObject(config);
        }
        System.out.println(stringToMap);
        model.addAttribute("info",stringToMap);
        model.addAttribute("configs",configs);

        return new ModelAndView("setting/config_setting3");
    }

    @RequestMapping("chatupdate")
    @ResponseBody
    public JsonResult chatupdate(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        String type_value = StringUtils.isEmpty(request.getParameter("type_value"))?null:request.getParameter("type_value");
        String privateinfo = StringUtils.isEmpty(request.getParameter("privateinfo"))?null:request.getParameter("privateinfo");
        String sdk_appid = StringUtils.isEmpty(request.getParameter("sdk_appid"))?null:request.getParameter("sdk_appid");
        String identifier = StringUtils.isEmpty(request.getParameter("identifier"))?null:request.getParameter("identifier");
        String subscribe_key = StringUtils.isEmpty(request.getParameter("subscribe_key"))?null:request.getParameter("subscribe_key");
        String client_secret = StringUtils.isEmpty(request.getParameter("client_secret"))?null:request.getParameter("client_secret");
        String client_id = StringUtils.isEmpty(request.getParameter("client_id"))?null:request.getParameter("client_id");
        String app_name = StringUtils.isEmpty(request.getParameter("app_name"))?null:request.getParameter("app_name");
        String org_name = StringUtils.isEmpty(request.getParameter("org_name"))?null:request.getParameter("org_name");
        String host = StringUtils.isEmpty(request.getParameter("host"))?null:request.getParameter("host");
        String common_key = StringUtils.isEmpty(request.getParameter("common_key"))?null:request.getParameter("common_key");

        try {
            if(id==null){
                Configs configs = new Configs();
                configs.setCreated_at(new Date());
                configs.setUpdated_at(new Date());
                configs.setType(Configs.type_chat);
                Map<String,String> map = new HashMap<>();
                map.put("type_value",type_value);
                map.put("privateinfo",privateinfo);
                map.put("sdk_appid",sdk_appid);
                map.put("identifier",identifier);
                map.put("subscribe_key",subscribe_key);
                map.put("client_secret",client_secret);
                map.put("client_id",client_id);
                map.put("app_name",app_name);
                map.put("org_name",org_name);
                map.put("host",host);
                map.put("common_key",common_key);
                configs.setConfig(JSONObject.toJSONString(map));
                configsService.insertService(configs);
            }else{
                Configs configs = configsService.findById(Integer.parseInt(id));
                Map<String,String> map = new HashMap<>();
                map.put("type_value",type_value);
                map.put("sdk_appid",sdk_appid);
                map.put("privateinfo",privateinfo);
                map.put("identifier",identifier);
                map.put("subscribe_key",subscribe_key);
                map.put("client_secret",client_secret);
                map.put("client_id",client_id);
                map.put("app_name",app_name);
                map.put("org_name",org_name);
                map.put("host",host);
                map.put("common_key",common_key);
                configs.setConfig(JSONObject.toJSONString(map));
                configs.setUpdated_at(new Date());
                configsService.updateService(configs);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }


    @RequestMapping("sms")
    public ModelAndView sms(HttpServletRequest request, Model model){
        Configs configs = configsService.findByTypeId(Configs.type_sms);
        Map stringToMap = new HashMap();
        if(configs!=null){
            String config = configs.getConfig();
            stringToMap =  JSONObject.parseObject(config);
        }
        System.out.println(stringToMap);
        model.addAttribute("info",stringToMap);
        model.addAttribute("configs",configs);

        return new ModelAndView("setting/config_setting4");
    }

    @RequestMapping("smsupdate")
    @ResponseBody
    public JsonResult smsupdate(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        String appkey = StringUtils.isEmpty(request.getParameter("appkey"))?null:request.getParameter("appkey");
        String smstype = StringUtils.isEmpty(request.getParameter("smstype"))?null:request.getParameter("smstype");
        String app_code = StringUtils.isEmpty(request.getParameter("app_code"))?null:request.getParameter("app_code");
        String alisignname = StringUtils.isEmpty(request.getParameter("alisignname"))?null:request.getParameter("alisignname");
        String access_key_id = StringUtils.isEmpty(request.getParameter("access_key_id"))?null:request.getParameter("access_key_id");
        String access_key_secret = StringUtils.isEmpty(request.getParameter("access_key_secret"))?null:request.getParameter("access_key_secret");
        String aliaccsignname = StringUtils.isEmpty(request.getParameter("aliaccsignname"))?null:request.getParameter("aliaccsignname");
        String vercode_tpl = StringUtils.isEmpty(request.getParameter("vercode_tpl"))?null:request.getParameter("vercode_tpl");

        try {
            if(id==null){
                Configs configs = new Configs();
                configs.setCreated_at(new Date());
                configs.setUpdated_at(new Date());
                configs.setType(Configs.type_sms);
                Map<String,String> map = new HashMap<>();
                map.put("smstype",smstype);

                map.put("appkey",appkey);
                map.put("app_code",app_code);
                map.put("alisignname",alisignname);
                map.put("access_key_id",access_key_id);
                map.put("access_key_secret",access_key_secret);
                map.put("aliaccsignname",aliaccsignname);
                map.put("vercode_tpl",vercode_tpl);

                configs.setConfig(JSONObject.toJSONString(map));
                configsService.insertService(configs);
            }else{
                Configs configs = configsService.findById(Integer.parseInt(id));
                Map<String,String> map = new HashMap<>();
                map.put("smstype",smstype);
                map.put("app_code",app_code);
                map.put("appkey",appkey);
                map.put("alisignname",alisignname);
                map.put("access_key_id",access_key_id);
                map.put("access_key_secret",access_key_secret);
                map.put("aliaccsignname",aliaccsignname);
                map.put("vercode_tpl",vercode_tpl);
                configs.setConfig(JSONObject.toJSONString(map));
                configs.setUpdated_at(new Date());
                configsService.updateService(configs);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }

    @RequestMapping("pay")
    public ModelAndView pay(HttpServletRequest request, Model model){
        Configs configs = configsService.findByTypeId(Configs.type_pay);
        Map stringToMap = new HashMap();
        if(configs!=null){
            String config = configs.getConfig();
            stringToMap =  JSONObject.parseObject(config);
        }
        System.out.println(stringToMap);
        model.addAttribute("info",stringToMap);
        model.addAttribute("configs",configs);

        return new ModelAndView("setting/config_setting5");
    }

    @RequestMapping("payupdate")
    @ResponseBody
    public JsonResult payupdate(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        String mid = StringUtils.isEmpty(request.getParameter("mid"))?null:request.getParameter("mid");
        String tid = StringUtils.isEmpty(request.getParameter("tid"))?null:request.getParameter("tid");
        String instMid = StringUtils.isEmpty(request.getParameter("instMid"))?null:request.getParameter("instMid");
        String msgSrc = StringUtils.isEmpty(request.getParameter("msgSrc"))?null:request.getParameter("msgSrc");
        String msgSrcId = StringUtils.isEmpty(request.getParameter("msgSrcId"))?null:request.getParameter("msgSrcId");
        String APIurl = StringUtils.isEmpty(request.getParameter("APIurl"))?null:request.getParameter("APIurl");
        String MD5Key = StringUtils.isEmpty(request.getParameter("MD5Key"))?null:request.getParameter("MD5Key");
        String appid = StringUtils.isEmpty(request.getParameter("appid"))?null:request.getParameter("appid");
        String notifyUrl = StringUtils.isEmpty(request.getParameter("notifyUrl"))?null:request.getParameter("notifyUrl");

        try {
            if(id==null){
                Configs configs = new Configs();
                configs.setCreated_at(new Date());
                configs.setUpdated_at(new Date());
                configs.setType(Configs.type_pay);
                Map<String,String> map = new HashMap<>();
                map.put("mid",mid);
                map.put("notifyUrl",notifyUrl);
                map.put("APIurl",APIurl);
                map.put("MD5Key",MD5Key);
                map.put("tid",tid);
                map.put("instMid",instMid);
                map.put("msgSrc",msgSrc);
                map.put("appid",appid);

                map.put("msgSrcId",msgSrcId);
                configs.setConfig(JSONObject.toJSONString(map));
                configsService.insertService(configs);
            }else{
                Configs configs = configsService.findById(Integer.parseInt(id));
                Map<String,String> map = new HashMap<>();
                map.put("mid",mid);
                map.put("APIurl",APIurl);
                map.put("MD5Key",MD5Key);
                map.put("notifyUrl",notifyUrl);
                map.put("tid",tid);
                map.put("instMid",instMid);
                map.put("msgSrc",msgSrc);
                map.put("appid",appid);
                map.put("msgSrcId",msgSrcId);
                configs.setConfig(JSONObject.toJSONString(map));
                configs.setUpdated_at(new Date());
                configsService.updateService(configs);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }


    @RequestMapping("logistics")
    public ModelAndView logistics(HttpServletRequest request, Model model){
        Configs configs = configsService.findByTypeId(Configs.type_logistics);
        Map stringToMap = new HashMap();
        if(configs!=null){
            String config = configs.getConfig();
            stringToMap =  JSONObject.parseObject(config);
        }
        System.out.println(stringToMap);
        model.addAttribute("info",stringToMap);
        model.addAttribute("configs",configs);

        return new ModelAndView("setting/config_setting6");
    }

    @RequestMapping("logisticsupdate")
    @ResponseBody
    public JsonResult logisticsupdate(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        String store_postage = StringUtils.isEmpty(request.getParameter("store_postage"))?null:request.getParameter("store_postage");
        String store_free_postage = StringUtils.isEmpty(request.getParameter("store_free_postage"))?null:request.getParameter("store_free_postage");
        String express_appsecret = StringUtils.isEmpty(request.getParameter("express_appsecret"))?null:request.getParameter("express_appsecret");

        try {
            if(id==null){
                Configs configs = new Configs();
                configs.setCreated_at(new Date());
                configs.setUpdated_at(new Date());
                configs.setType(Configs.type_logistics);
                Map<String,String> map = new HashMap<>();
                map.put("store_postage",store_postage);
                map.put("store_free_postage",store_free_postage);
                map.put("express_appsecret",express_appsecret);
                configs.setConfig(JSONObject.toJSONString(map));
                configsService.insertService(configs);
            }else{
                Configs configs = configsService.findById(Integer.parseInt(id));
                Map<String,String> map = new HashMap<>();
                map.put("store_postage",store_postage);
                map.put("store_free_postage",store_free_postage);
                map.put("express_appsecret",express_appsecret);
                configs.setConfig(JSONObject.toJSONString(map));
                configs.setUpdated_at(new Date());
                configsService.updateService(configs);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }


    @RequestMapping("trading")
    public ModelAndView trading(HttpServletRequest request, Model model){
        Configs configs = configsService.findByTypeId(Configs.type_trading);
        Map stringToMap = new HashMap();
        if(configs!=null){
            String config = configs.getConfig();
            stringToMap =  JSONObject.parseObject(config);
        }
        System.out.println(stringToMap);
        model.addAttribute("info",stringToMap);
        model.addAttribute("configs",configs);

        return new ModelAndView("setting/config_setting7");
    }

    @RequestMapping("tradingupdate")
    @ResponseBody
    public JsonResult tradingupdate(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        String order_cancel_time = StringUtils.isEmpty(request.getParameter("order_cancel_time"))?null:request.getParameter("order_cancel_time");
        String automatic_receiving_goods = StringUtils.isEmpty(request.getParameter("automatic_receiving_goods"))?null:request.getParameter("automatic_receiving_goods");
        String automatic_close_goods = StringUtils.isEmpty(request.getParameter("automatic_close_goods"))?null:request.getParameter("automatic_close_goods");
        String switchinfo = StringUtils.isEmpty(request.getParameter("switchinfo"))?null:request.getParameter("switchinfo");
        String recharge = StringUtils.isEmpty(request.getParameter("recharge"))?null:request.getParameter("recharge");
        String min_recharge = StringUtils.isEmpty(request.getParameter("min_recharge"))?null:request.getParameter("min_recharge");
        String withdraw = StringUtils.isEmpty(request.getParameter("withdraw"))?null:request.getParameter("withdraw");
        String withdraw_limit = StringUtils.isEmpty(request.getParameter("withdraw_limit"))?null:request.getParameter("withdraw_limit");
        String withdraw_commission = StringUtils.isEmpty(request.getParameter("withdraw_commission"))?null:request.getParameter("withdraw_commission");
        String withdraw_commission_free_min = StringUtils.isEmpty(request.getParameter("withdraw_commission_free_min"))?null:request.getParameter("withdraw_commission_free_min");
        String withdraw_commission_free_max = StringUtils.isEmpty(request.getParameter("withdraw_commission_free_max"))?null:request.getParameter("withdraw_commission_free_max");
        try {
            if(id==null){
                Configs configs = new Configs();
                configs.setCreated_at(new Date());
                configs.setUpdated_at(new Date());
                configs.setType(Configs.type_trading);
                Map<String,String> map = new HashMap<>();
                map.put("order_cancel_time",order_cancel_time);
                map.put("automatic_receiving_goods",automatic_receiving_goods);
                map.put("switchinfo",switchinfo);
                map.put("recharge",recharge);
                map.put("automatic_close_goods",automatic_close_goods);
                map.put("min_recharge",min_recharge);
                map.put("withdraw",withdraw);
                map.put("withdraw_limit",withdraw_limit);
                map.put("withdraw_commission",withdraw_commission);
                map.put("withdraw_commission_free_min",withdraw_commission_free_min);
                map.put("withdraw_commission_free_max",withdraw_commission_free_max);
                configs.setConfig(JSONObject.toJSONString(map));
                configsService.insertService(configs);
            }else{

                Configs configs = configsService.findById(Integer.parseInt(id));
                Map<String,String> map = new HashMap<>();
                map.put("order_cancel_time",order_cancel_time);
                map.put("automatic_receiving_goods",automatic_receiving_goods);
                map.put("switchinfo",switchinfo);
                map.put("recharge",recharge);
                map.put("min_recharge",min_recharge);
                map.put("automatic_close_goods",automatic_close_goods);
                map.put("withdraw",withdraw);
                map.put("withdraw_limit",withdraw_limit);
                map.put("withdraw_commission",withdraw_commission);
                map.put("withdraw_commission_free_min",withdraw_commission_free_min);
                map.put("withdraw_commission_free_max",withdraw_commission_free_max);
                configs.setConfig(JSONObject.toJSONString(map));
                configs.setUpdated_at(new Date());
                configsService.updateService(configs);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return new JsonResult(e);
        }
        return new JsonResult();
    }



    @RequestMapping("live")
    public ModelAndView live(HttpServletRequest request, Model model){
        Configs configs = configsService.findByTypeId(Configs.type_live);
        Map stringToMap = new HashMap();
        if(configs!=null){
            String config = configs.getConfig();
            stringToMap =  JSONObject.parseObject(config);
        }
        System.out.println(stringToMap);
        model.addAttribute("info",stringToMap);
        model.addAttribute("configs",configs);

        return new ModelAndView("setting/config_setting8");
    }

    @RequestMapping("liveupdate")
    @ResponseBody
    public JsonResult liveupdate(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        String chat_nums = StringUtils.isEmpty(request.getParameter("chat_nums"))?null:request.getParameter("chat_nums");
        String open_deduct = StringUtils.isEmpty(request.getParameter("open_deduct"))?null:request.getParameter("open_deduct");
        String deduct_ratio = StringUtils.isEmpty(request.getParameter("deduct_ratio"))?null:request.getParameter("deduct_ratio");
        String open_location = StringUtils.isEmpty(request.getParameter("open_location"))?null:request.getParameter("open_location");
        String chat_notice = StringUtils.isEmpty(request.getParameter("chat_notice"))?null:request.getParameter("chat_notice");

        try {
            if(id==null){
                Configs configs = new Configs();
                configs.setCreated_at(new Date());
                configs.setUpdated_at(new Date());
                configs.setType(Configs.type_live);
                Map<String,String> map = new HashMap<>();
                map.put("chat_nums",chat_nums);
                map.put("open_deduct",open_deduct);
                map.put("deduct_ratio",deduct_ratio);
                map.put("open_location",open_location);
                map.put("chat_notice",chat_notice);
                configs.setConfig(JSONObject.toJSONString(map));
                configsService.insertService(configs);
            }else{
                Configs configs = configsService.findById(Integer.parseInt(id));
                Map<String,String> map = new HashMap<>();
                map.put("chat_nums",chat_nums);
                map.put("open_deduct",open_deduct);
                map.put("deduct_ratio",deduct_ratio);
                map.put("open_location",open_location);
                map.put("chat_notice",chat_notice);
                configs.setConfig(JSONObject.toJSONString(map));
                configs.setUpdated_at(new Date());
                configsService.updateService(configs);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return new JsonResult(e);
        }
        return new JsonResult();
    }


    /**
     * 上传服务配置
     * @return
     */
    @RequestMapping("uploadconfig")
    public ModelAndView uploadconfig(HttpServletRequest request, Model model){
        Configs configs = configsService.findByTypeId(Configs.type_upload);
        Map stringToMap = new HashMap();
        if(configs!=null){
            String config = configs.getConfig();
            stringToMap =  JSONObject.parseObject(config);
        }
        System.out.println(stringToMap);
        model.addAttribute("info",stringToMap);
        model.addAttribute("configs",configs);

        return new ModelAndView("setting/config_setting9");
    }


    @RequestMapping("uploadconfigupdate")
    @ResponseBody
    public JsonResult uploadconfigupdate(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        String localurl = StringUtils.isEmpty(request.getParameter("localurl"))?null:request.getParameter("localurl");
        String url = StringUtils.isEmpty(request.getParameter("url"))?null:request.getParameter("url");
        String accesskey = StringUtils.isEmpty(request.getParameter("accesskey"))?null:request.getParameter("accesskey");
        String secretkey = StringUtils.isEmpty(request.getParameter("secretkey"))?null:request.getParameter("secretkey");
        String bucket = StringUtils.isEmpty(request.getParameter("bucket"))?null:request.getParameter("bucket");
        String type_value = StringUtils.isEmpty(request.getParameter("type_value"))?null:request.getParameter("type_value");

        try {
            if(id==null){
                Configs configs = new Configs();
                configs.setCreated_at(new Date());
                configs.setUpdated_at(new Date());
                configs.setType(Configs.type_upload);
                Map<String,String> map = new HashMap<>();
                map.put("type_value",type_value);
                map.put("localurl",localurl);
                map.put("url",url);
                map.put("accesskey",accesskey);
                map.put("secretkey",secretkey);
                map.put("bucket",bucket);
                configs.setConfig(JSONObject.toJSONString(map));
                configsService.insertService(configs);
            }else{
                Configs configs = configsService.findById(Integer.parseInt(id));
                Map<String,String> map = new HashMap<>();
                map.put("localurl",localurl);
                map.put("type_value",type_value);
                map.put("url",url);
                map.put("accesskey",accesskey);
                map.put("secretkey",secretkey);
                map.put("bucket",bucket);
                configs.setConfig(JSONObject.toJSONString(map));
                configs.setUpdated_at(new Date());
                configsService.updateService(configs);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }
}
