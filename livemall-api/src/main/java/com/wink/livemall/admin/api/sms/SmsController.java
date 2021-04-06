package com.wink.livemall.admin.api.sms;

import com.alibaba.fastjson.JSONObject;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.Md5Util;
import com.wink.livemall.member.dto.LmMember;
import com.wink.livemall.member.service.LmMemberService;
import com.wink.livemall.sys.basic.dto.LmBasicConfig;
import com.wink.livemall.sys.basic.service.LmBasicConfigService;
import com.wink.livemall.sys.code.dto.LmSmsVcode;
import com.wink.livemall.sys.code.service.LmSmsVcodeService;
import com.wink.livemall.sys.setting.dto.Configs;
import com.wink.livemall.sys.setting.service.ConfigsService;
import com.wink.livemall.utils.sms.SmsUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Api(tags = "短信服务接口")
@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    private LmSmsVcodeService lmSmsVcodeService;

    @Autowired
    private ConfigsService configsService;
    @Autowired
    private LmMemberService lmMemberService;
    /**
     * 发送短信验证码
     */
    @ApiOperation(value = "发送短信验证码")
    @PostMapping("/send")
    public JsonResult login(HttpServletRequest request,@ApiParam(name = "mobile", value = "用户名",defaultValue = "13812117597", required = true)@RequestParam String mobile){
        JsonResult jsonResult = new JsonResult();
        String msg="";

        if(!StringUtils.isEmpty(mobile)){
            //判断手机号码格式是否正确
            //手机号码格式验证
            Pattern p = Pattern.compile("^[1]\\d{10}$");
            Matcher m = p.matcher(mobile);
            boolean isMatch = m.matches();
            if (!isMatch) {
                msg="手机号格式不正确";
                jsonResult.setCode(JsonResult.ERROR);
                jsonResult.setMsg(msg);
                return jsonResult;
            }
            LmMember old = lmMemberService.findByMobile(mobile);
            if(old!=null){
                msg="用户已存在";
                jsonResult.setCode(JsonResult.ERROR);
                jsonResult.setMsg(msg);
                return jsonResult;
            }
            List<LmSmsVcode> restrictMobile = lmSmsVcodeService.restrictMobile(mobile);
            if(restrictMobile.size()>5){
                msg="发送验证码已超过一天上限";
                jsonResult.setCode(JsonResult.ERROR);
                jsonResult.setMsg(msg);
                return jsonResult;
            }
            Configs configs = configsService.findByTypeId(Configs.type_sms);
            if(configs!=null) {
                String vcode = (int)((Math.random()*9+1)*100000)+"";
                String config = configs.getConfig();
                Map configmap = JSONObject.parseObject(config);
                String smstype = configmap.get("smstype")+"";
                if("3".equals(smstype)){
                    SmsUtils.sendValidCodeMsg(mobile,vcode,configmap.get("access_key_id")+"",configmap.get("access_key_secret")+"",configmap.get("aliaccsignname")+"",configmap.get("vercode_tpl")+"");
                }
                LmSmsVcode lmSmsVcode = new LmSmsVcode();
                lmSmsVcode.setCreatdate(new Date());
                lmSmsVcode.setMobile(mobile);
                lmSmsVcode.setVcode(vcode);
                lmSmsVcodeService.addservice(lmSmsVcode);
                jsonResult.setCode(JsonResult.SUCCESS);
            }
        }else{
            jsonResult.setCode(JsonResult.ERROR);
            msg="手机号码不能为空";
            jsonResult.setMsg(msg);
            return jsonResult;
        }
        return jsonResult;
    }


    /**
     * 发送短信验证码
     */
    @ApiOperation(value = "忘记密码发送密码")
    @PostMapping("/forgetLogin")
    public JsonResult forgetLogin(HttpServletRequest request,@ApiParam(name = "mobile", value = "用户名",defaultValue = "13812117597", required = true)@RequestParam String mobile){
        JsonResult jsonResult = new JsonResult();
        String msg="";

        if(!StringUtils.isEmpty(mobile)){
            //判断手机号码格式是否正确
            //手机号码格式验证
            Pattern p = Pattern.compile("^[1]\\d{10}$");
            Matcher m = p.matcher(mobile);
            boolean isMatch = m.matches();
            if (!isMatch) {
                msg="手机号格式不正确";
                jsonResult.setCode(JsonResult.ERROR);
                jsonResult.setMsg(msg);
                return jsonResult;
            }
            List<LmSmsVcode> restrictMobile = lmSmsVcodeService.restrictMobile(mobile);
            if(restrictMobile.size()>5){
                msg="发送验证码已超过一天上限";
                jsonResult.setCode(JsonResult.ERROR);
                jsonResult.setMsg(msg);
                return jsonResult;
            }
            Configs configs = configsService.findByTypeId(Configs.type_sms);
            if(configs!=null) {
                String vcode = (int)((Math.random()*9+1)*100000)+"";
                String config = configs.getConfig();
                Map configmap = JSONObject.parseObject(config);
                String smstype = configmap.get("smstype")+"";
                if("3".equals(smstype)){
                    SmsUtils.sendValidCodeMsg(mobile,vcode,configmap.get("access_key_id")+"",configmap.get("access_key_secret")+"",configmap.get("aliaccsignname")+"",configmap.get("vercode_tpl")+"");
                }
                LmSmsVcode lmSmsVcode = new LmSmsVcode();
                lmSmsVcode.setCreatdate(new Date());
                lmSmsVcode.setMobile(mobile);
                lmSmsVcode.setVcode(vcode);
                lmSmsVcodeService.addservice(lmSmsVcode);
                jsonResult.setCode(JsonResult.SUCCESS);
            }
        }else{
            jsonResult.setCode(JsonResult.ERROR);
            msg="手机号码不能为空";
            jsonResult.setMsg(msg);
            return jsonResult;
        }
        return jsonResult;
    }

}
