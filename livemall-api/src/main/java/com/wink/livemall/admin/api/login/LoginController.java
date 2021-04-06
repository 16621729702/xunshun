package com.wink.livemall.admin.api.login;

import com.google.gson.Gson;
import com.wink.livemall.admin.util.IMUtil;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.Md5Util;
import com.wink.livemall.admin.util.TokenUtil;
import com.wink.livemall.admin.util.httpclient.HttpClient;
import com.wink.livemall.coupon.service.LmCouponsService;
import com.wink.livemall.member.dto.LmMember;
import com.wink.livemall.member.dto.LmMemberInvite;
import com.wink.livemall.member.dto.LmMemberLevel;
import com.wink.livemall.member.service.LmMemberInviteService;
import com.wink.livemall.member.service.LmMemberLevelService;
import com.wink.livemall.member.service.LmMemberService;
import com.wink.livemall.sys.code.dto.LmSmsVcode;
import com.wink.livemall.sys.code.service.LmSmsVcodeService;
import com.wink.livemall.sys.setting.dto.Configs;
import com.wink.livemall.sys.setting.service.ConfigsService;
import com.wink.livemall.utils.cache.redis.RedisUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Api(tags = "登录模块")
@RestController
public class LoginController {
    @Autowired
    private LmMemberLevelService lmMemberLevelService;
    @Autowired
    private LmMemberService lmMemberService;
    @Autowired
    private LmSmsVcodeService lmSmsVcodeService;
    @Autowired
    private RedisUtil redisUtils;
    @Autowired
    private ConfigsService configsService;
    @Autowired
    private LmMemberInviteService lmMemberInviteService;
    /**
     * 登录验证
     */
    @ApiOperation(value = "登录验证")
    @PostMapping("login")
    public JsonResult login(HttpServletRequest request,
                            @ApiParam(name = "mobile", value = "用户名",defaultValue = "13812117597", required = true)@RequestParam(required = true) String mobile,
                            @ApiParam(name = "password", value = "密码",defaultValue = "13812117597", required = true)@RequestParam (required = true) String password){
       JsonResult jsonResult = new JsonResult();
        String msg="";
        if(!StringUtils.isEmpty(mobile)&&!StringUtils.isEmpty(password)){
            LmMember lmMember = lmMemberService.findByMobile(mobile);
            if(lmMember!=null){
                if(lmMember.getPassword().equals(Md5Util.MD5(password))){
                    int levelid = lmMember.getLevel_id();
                    lmMember.setPassword(password);
                    lmMemberService.updateService(lmMember);
                    LmMemberLevel lmMemberLevel = lmMemberLevelService.findById(levelid+"");
                    Map<String,Object> map = new HashMap<>();
                    map.put("userid",lmMember.getId());
                    map.put("growth_value",lmMember.getGrowth_value());
                    map.put("nickname",lmMember.getNickname());
                    map.put("avatar",lmMember.getAvatar());
                    if(lmMemberLevel!=null){
                        map.put("levelname",lmMemberLevel.getName());
                        map.put("levelcode",lmMemberLevel.getCode());
                    }else{
                        LmMemberLevel lmMemberLevels = lmMemberLevelService.findById("1");
                        map.put("levelname",lmMemberLevels.getName());
                        map.put("levelcode",lmMemberLevels.getCode());
                    }
                    String  uuid = UUID.randomUUID().toString();
                    Configs configs =configsService.findByTypeId(Configs.type_chat);
                    Map chat =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
                    long appid = Long.parseLong(chat.get("sdk_appid")+"");
                    String privateinfo = chat.get("privateinfo")+"";
                    String sigstring = IMUtil.genSig(appid,privateinfo,lmMember.getId()+"");
                    map.put("usersig",sigstring);
                   /* Set<String> set = redisUtils.mhget(lmMember.getId()+":"+"*");
                    redisUtils.deletekeys(set);
                    redisUtils.set(lmMember.getId()+":"+uuid,lmMember.getId(),3600*24*7);*/
                    String token = TokenUtil.Token(String.valueOf(lmMember.getId()));
                    map.put("accessToken",token);
                    HttpClient httpClient = new HttpClient();
                    Map<String,Object> memberinfo= new HashMap<>();
                    memberinfo.put("nickname",lmMember.getNickname());
                    httpClient.login(lmMember.getAvatar(),lmMember.getId()+"",new Gson().toJson(memberinfo));
                    jsonResult.setCode(JsonResult.SUCCESS);
                    jsonResult.setData(map);
                } else  if(lmMember.getPassword().equals(password)){
                    int levelid = lmMember.getLevel_id();
                    LmMemberLevel lmMemberLevel = lmMemberLevelService.findById(levelid+"");
                    Map<String,Object> map = new HashMap<>();
                    map.put("userid",lmMember.getId());
                    map.put("growth_value",lmMember.getGrowth_value());
                    map.put("nickname",lmMember.getNickname());
                    map.put("avatar",lmMember.getAvatar());
                    if(lmMemberLevel!=null){
                    	 map.put("levelname",lmMemberLevel.getName());
                         map.put("levelcode",lmMemberLevel.getCode());
                    }else{
                        LmMemberLevel lmMemberLevels = lmMemberLevelService.findById("1");
                        map.put("levelname",lmMemberLevels.getName());
                        map.put("levelcode",lmMemberLevels.getCode());
                    }
                    String  uuid = UUID.randomUUID().toString();
                    Configs configs =configsService.findByTypeId(Configs.type_chat);
                    Map chat =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
                    long appid = Long.parseLong(chat.get("sdk_appid")+"");
                    String privateinfo = chat.get("privateinfo")+"";
                    String sigstring = IMUtil.genSig(appid,privateinfo,lmMember.getId()+"");
                    map.put("usersig",sigstring);
                   /* Set<String> set = redisUtils.mhget(lmMember.getId()+":"+"*");
                    redisUtils.deletekeys(set);
                    redisUtils.set(lmMember.getId()+":"+uuid,lmMember.getId(),3600*24*7);*/
                    String token = TokenUtil.Token(String.valueOf(lmMember.getId()));
                    map.put("accessToken",token);
                    HttpClient httpClient = new HttpClient();
                    Map<String,Object> memberinfo= new HashMap<>();
                    memberinfo.put("nickname",lmMember.getNickname());
                    httpClient.login(lmMember.getAvatar(),lmMember.getId()+"",new Gson().toJson(memberinfo));
                    jsonResult.setCode(JsonResult.SUCCESS);
                    jsonResult.setData(map);
                } else {
                    jsonResult.setCode(JsonResult.ERROR);
                    msg="密码错误";
                }
            }else{
                jsonResult.setCode(JsonResult.ERROR);
                msg="该手机号码不存在";
            }
        }else{
            jsonResult.setCode(JsonResult.ERROR);
            msg="手机号码密码不能为空";
        }
        jsonResult.setMsg(msg);
        return jsonResult;
    }
    /**
     * 注册验证
     */
    @ApiOperation(value = "注册验证")
    @PostMapping("register")
    public JsonResult register(HttpServletRequest request,
                               @ApiParam(name = "mobile", value = "用户名",defaultValue = "13812117597", required = true)@RequestParam(required = true) String mobile,
                               @ApiParam(name = "password", value = "密码",defaultValue = "13812117597", required = true)@RequestParam(required = true) String password,
                               @ApiParam(name = "vcode", value = "验证码",defaultValue = "123456", required = true)@RequestParam(required = true) String vcode,
                               @ApiParam(name = "inviteCode", value = "分享的邀请码",defaultValue = "0", required = false)@RequestParam(required = false) String inviteCode){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        String msg="";
        if(!StringUtils.isEmpty(mobile)&&!StringUtils.isEmpty(password)){
            List<LmSmsVcode> list = lmSmsVcodeService.findByMobile(mobile);
            if(list==null&&list.size()<=0){
                msg="验证码错误";
                jsonResult.setCode(JsonResult.ERROR);
                jsonResult.setMsg(msg);
                return jsonResult;
            }
            //手机号码格式验证
            Pattern pattern = Pattern.compile("^[1]\\d{10}$");
            Matcher m = pattern.matcher(mobile);
            boolean isMatch = m.matches();
            if (!isMatch) {
                msg="手机号格式不正确";
                jsonResult.setCode(JsonResult.ERROR);
                jsonResult.setMsg(msg);
                return jsonResult;
            }
            LmMember old = lmMemberService.findByMobile(mobile);
            if(old==null){
                if(vcode.equals(list.get(0).getVcode())){
                    LmMember lmMember = new LmMember();
                    lmMember.setNickname(mobile);
                    lmMember.setUsername(mobile);
                    lmMember.setMobile(mobile);
                    lmMember.setUsertype(0);
                    lmMember.setCredit2(new BigDecimal(0));
                    lmMember.setState(0);
                    lmMember.setPassword(password);
                    lmMember.setGrowth_value(0);
                    lmMember.setLevel_id(0);
                    lmMember.setCreated_at(new Date());
                    lmMember.setUpdated_at(new Date());
                    lmMemberService.insertService(lmMember);
                    HttpClient httpClient = new HttpClient();
                    Map<String,Object> memberinfo= new HashMap<>();
                    memberinfo.put("nickname",lmMember.getNickname());
                    memberinfo.put("levelname","");
                    memberinfo.put("levelcode","");
                    httpClient.login(lmMember.getAvatar(),lmMember.getId()+"", new Gson().toJson(memberinfo));
                    LmMemberInvite lmMemberInvite = lmMemberInviteService.getInviteCode(String.valueOf(lmMember.getId()));
                    if(!StringUtils.isEmpty(inviteCode)){
                    lmMemberInviteService.addShareCoupon(inviteCode,lmMemberInvite);
                    }
                }else{
                    msg="验证码错误";
                    jsonResult.setCode(JsonResult.ERROR);
                    jsonResult.setMsg(msg);
                    return jsonResult;
                }
            }else{
                msg="用户已存在";
                jsonResult.setCode(JsonResult.ERROR);
                jsonResult.setMsg(msg);
                return jsonResult;
            }
        }else{
            msg="手机号码密码不能为空";
            jsonResult.setCode(JsonResult.ERROR);
            jsonResult.setMsg(msg);
            return jsonResult;
        }
        return jsonResult;
    }


    /**
     * 忘记密码
     */
    @ApiOperation(value = "忘记密码")
    @PostMapping("forgotpassword")
    public JsonResult forgotpassword(HttpServletRequest request,
                               @ApiParam(name = "mobile", value = "用户名",defaultValue = "13812117597", required = true)@RequestParam String mobile,
                               @ApiParam(name = "password", value = "密码",defaultValue = "13812117597", required = true)@RequestParam String password,
                               @ApiParam(name = "vcode", value = "验证码",defaultValue = "123456", required = true)@RequestParam String vcode){
        JsonResult jsonResult = new JsonResult();
        String msg="";
        if(!StringUtils.isEmpty(mobile)&&!StringUtils.isEmpty(password)){
            LmMember lmMember = lmMemberService.findByMobile(mobile);
            if(lmMember!=null){
                List<LmSmsVcode> list = lmSmsVcodeService.findByMobile(mobile);
                if(list==null&&list.size()<=0){
                    msg="验证码错误";
                    jsonResult.setCode(JsonResult.ERROR);
                    jsonResult.setMsg(msg);
                    return jsonResult;
                }
                //手机号码格式验证
                Pattern pattern = Pattern.compile("^[1]\\d{10}$");
                Matcher m = pattern.matcher(mobile);
                boolean isMatch = m.matches();
                if (!isMatch) {
                    msg="手机号格式不正确";
                    jsonResult.setCode(JsonResult.ERROR);
                    jsonResult.setMsg(msg);
                    return jsonResult;
                }
                if(vcode.equals(list.get(0).getVcode())){
                    if((password).equals(lmMember.getPassword())){
                        msg="原密码和新密码的不可重复";
                        jsonResult.setCode(JsonResult.ERROR);
                        jsonResult.setMsg(msg);
                        return jsonResult;
                    }
                    lmMember.setNickname(mobile);
                    lmMember.setUsername(mobile);
                    lmMember.setMobile(mobile);
                    lmMember.setUsertype(0);
                    lmMember.setState(0);
                    lmMember.setPassword(password);
                    lmMember.setCreated_at(new Date());
                    lmMember.setUpdated_at(new Date());
                    lmMemberService.updateService(lmMember);
                }else{
                    msg="验证码错误";
                    jsonResult.setCode(JsonResult.ERROR);
                }
            }else{
                msg="用户不存在";
                jsonResult.setCode(JsonResult.ERROR);
            }
        }else{
            jsonResult.setCode(JsonResult.ERROR);
            msg="手机号码密码不能为空";
        }
        jsonResult.setMsg(msg);
        return jsonResult;
    }





}
