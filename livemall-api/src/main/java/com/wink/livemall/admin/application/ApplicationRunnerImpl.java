package com.wink.livemall.admin.application;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.wink.livemall.admin.util.DateUtils;
import com.wink.livemall.admin.util.httpclient.HttpClient;
import com.wink.livemall.member.dto.LmMember;
import com.wink.livemall.member.service.LmMemberService;
import com.wink.livemall.merch.dto.LmMerchInfo;
import com.wink.livemall.merch.service.LmMerchInfoService;
import com.wink.livemall.sys.setting.dto.Configs;
import com.wink.livemall.sys.setting.service.ConfigsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ApplicationRunnerImpl implements ApplicationRunner {

    @Autowired
    private LmMerchInfoService lmMerchInfoService;

    @Autowired
    private LmMemberService lmMemberService;
    @Autowired
    private ConfigsService configsService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("查看是否生成后台机器人");
        HttpClient httpClient = new HttpClient();
        LmMember root = lmMemberService.findById("0");
        Configs configs = configsService.findByTypeId(Configs.type_basic);
        String config = configs.getConfig();
        Map stringToMap  =  JSONObject.parseObject(config);
        Map<String,Object> memberinfo= new HashMap<>();
        memberinfo.put("levelname","");
        memberinfo.put("levelcode","");
        if(root==null){
            LmMember lmMember = new LmMember();
            lmMember.setNickname(stringToMap.get("site_name")+"");
            lmMember.setId(0);
            lmMember.setState(0);
            lmMember.setCreated_at(new Date());
            lmMember.setUpdated_at(new Date());
            lmMemberService.insertService(lmMember);
            memberinfo.put("nickname","滴雨轩官方");
            httpClient.login(stringToMap.get("logo")+"","滴雨轩官方",new Gson().toJson(memberinfo));
        }else{
            memberinfo.put("nickname","滴雨轩官方");
            httpClient.login(stringToMap.get("logo")+"","滴雨轩官方",new Gson().toJson(memberinfo));
        }
        //生产拍品消息机器人
        memberinfo.put("nickname","拍品消息");
        httpClient.login("http://oss.xunshun.net/head/wVEdm2znH51AgggW2LDveUAMZYDVpu9u","拍品消息",new Gson().toJson(memberinfo));
        //生产交易消息消息机器人
        memberinfo.put("nickname","交易消息");
        httpClient.login("http://oss.xunshun.net/head/7qk3tXYrR7AbLRmIhG4jIM9HTUGEu3rg","交易消息",new Gson().toJson(memberinfo));
        //生产物流消息机器人
        memberinfo.put("nickname","物流消息");
        httpClient.login("http://oss.xunshun.net/head/PToWmdrGyUiEXUIZNmZ0PwUq9re2ifgZ","物流消息",new Gson().toJson(memberinfo));


       /*List<LmMember> list= lmMemberService.findAll();
       for(LmMember lmMember:list){
           String msg = "【直播预告 ：12月30日  晚 19:30-22:30】\n" +
                   "尊敬的"+lmMember.getNickname()+"用户：\n" +
                   "元旦焕新季，翡翠专场来袭，顶尖好货冰点价尽在——滴雨轩拍卖行直播间，与您不见不散！";
           httpClient.send("拍品消息",lmMember.getId()+"",msg);
       }*/


    }
}
