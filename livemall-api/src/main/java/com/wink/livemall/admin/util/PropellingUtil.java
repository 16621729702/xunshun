package com.wink.livemall.admin.util;


import com.tencent.xinge.XingeApp;
import com.tencent.xinge.bean.*;
import com.tencent.xinge.bean.ios.Alert;
import com.tencent.xinge.bean.ios.Aps;
import com.tencent.xinge.push.app.PushAppRequest;
import com.wink.livemall.admin.util.idCardUtils.HttpUtil;
import org.json.JSONObject;

import java.util.ArrayList;

public class PropellingUtil {
    public static  String AndroidAppId = "1580002226";
    public static  String AndroidSecretKey = "3662b78c2a159191ff221027cbb7d3fc";
    public static  String IOSAppId = "1680002227";
    public static  String IOSSecretKey = "29e5d1e299f2e73c1115fb23653134c0";
    public static  String  DomainUrl="https://api.tpns.sh.tencent.com/";

    public static void AndroidPropellingMessage(String title,String content,String ID) {
        XingeApp xingeApp = new XingeApp.Builder()
                .appId(AndroidAppId)
                .secretKey(AndroidSecretKey)
                .domainUrl(DomainUrl)
                 .build();
        PushAppRequest pushAppRequest = new PushAppRequest();
        pushAppRequest.setAudience_type(AudienceType.account);
        pushAppRequest.setPlatform(Platform.android);
        pushAppRequest.setMessage_type(MessageType.notify);
        Message message = new Message();
        message.setTitle(title);
        message.setContent(content);
        MessageAndroid messageAndroid = new MessageAndroid();
        message.setAndroid(messageAndroid);
        ArrayList<String> tagList = new ArrayList();
        tagList.add(ID);
        pushAppRequest.setAccount_list(tagList);
        pushAppRequest.setMessage(message);
        JSONObject jsonObject = xingeApp.pushApp(pushAppRequest);
        System.out.println(jsonObject);
    }

    public static  void IOSPropellingMessage(String title,String content,String ID) {
        XingeApp IOSApp = new XingeApp.Builder()
                .appId(IOSAppId)
                .secretKey(IOSSecretKey)
                .domainUrl(DomainUrl)
                .build();
        PushAppRequest pushAppRequest = new PushAppRequest();
        pushAppRequest.setAudience_type(AudienceType.account);
        pushAppRequest.setPlatform(Platform.ios);
        pushAppRequest.setMessage_type(MessageType.notify);
        pushAppRequest.setEnvironment(Environment.dev);
        Message message = new Message();
        message.setTitle(title);
        message.setContent(content);
        MessageIOS messageIOS = new MessageIOS();
        Alert alert = new Alert();
        Aps aps=new Aps();
        aps.setAlert(alert);
        messageIOS.setAps(aps);
        message.setIos(messageIOS);
        pushAppRequest.setMessage(message);
        ArrayList<String> accountList = new ArrayList();
        accountList.add(ID);
        pushAppRequest.setAccount_list(accountList);
        JSONObject jsonObject = IOSApp.pushApp(pushAppRequest);
        System.out.println(jsonObject);
    }


    public static void main(String[] args) {
      /*  PropellingUtil.IOSPropellingMessage();
        PropellingUtil.AndroidPropellingMessage();*/
        /*PropellingUtil.IOSPropellingMessage();*/
        /*PropellingUtil.AndroidPropellingMessage("交易消息","交易消息","15733408690");*/
    }

}
