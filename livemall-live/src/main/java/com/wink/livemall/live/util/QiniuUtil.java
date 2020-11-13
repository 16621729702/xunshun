package com.wink.livemall.live.util;

import com.qiniu.pili.Client;
import com.qiniu.pili.Hub;
import com.qiniu.pili.PiliException;
import com.qiniu.pili.Stream;
import com.qiniu.pili.utils.Base64;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

public class QiniuUtil {
    private String keyA ;
    private static Hub hub;
    private String livepushurl;
    private String rtmpurl;
    private String hlsurl;
    private String hdlurl;
    private static Client cli;

    public QiniuUtil(String accessKey, String secretKey,String prefix,String pushurl,String readurl,String readurl2,String readurl3,String hubname) {
        //初始化client
        cli = new Client(accessKey,secretKey);
        //初始化Hub
        hub = cli.newHub(hubname);
        keyA = prefix;
        livepushurl = pushurl;
        rtmpurl = readurl;
        hlsurl = readurl2;
        hdlurl = readurl3;
    }

    public String getpushurl(String hubName){
        // RTMP推流地址
        String url = cli.RTMPPublishURL(livepushurl, hubName, keyA, 3600);
        System.out.printf("keyA=%s RTMP推流地址=%s\n", keyA, url);
        return url;
    }   

    public String getreadurl(String hubName){
        //RTMP直播地址
        String url = cli.RTMPPlayURL(rtmpurl, hubName, keyA);
        System.out.printf("keyA=%s RTMP直播地址=%s\n", keyA, url);
        return url;
    }
    public String getreadurl2(String hubName){
        //HLS直播地址
        String url = cli.HLSPlayURL(hlsurl, hubName, keyA);
        System.out.printf("keyA=%s HLS直播地址=%s\n", keyA, url);
        return url;
    }
    public String getreadurl3(String hubName){
        //HDL直播地址
        String url = cli.HDLPlayURL(hdlurl, hubName, keyA);
        System.out.printf("keyA=%s HDL直播地址=%s\n", keyA, url);
        return url;
    }

    public Stream create(String hubName){
        //创建流
        Stream streamA = null;
        try {
            streamA= hub.create(keyA);
        }catch (PiliException e){
            e.printStackTrace();
        }
        System.out.printf("keyA=%s 创建\n", keyA);
        return streamA;
    }
    public Stream getStream(String hubName){
        //获得流
        Stream streamA = null;
        try{
            streamA = hub.get(keyA);
        }catch (PiliException e){
            e.printStackTrace();
        }
        System.out.printf("keyA=%s 查询: %s\n", keyA, streamA.toJson());
        return streamA;
    }

    public boolean LiveStatus(){
        //获得流
        Stream streamA = null;
        //查询直播状态
        try{
            streamA = hub.get(keyA);
            Stream.LiveStatus status = streamA.liveStatus();
            System.out.printf("keyA=%s 直播状态: status=%s\n", keyA, status.toJson());
        }catch (PiliException e){
            if (!e.isNotInLive()) {
                e.printStackTrace();
                return true;
            }else{
                System.out.printf("keyA=%s 不在直播\n",keyA);
                return false;
            }
        }
        return true;
    }

    /*public static void main(String[] args) {

        //获得不存在的流
        String keyA = streamKeyPrefix + "A";
        try{
            hub.get(keyA);
        }catch (PiliException e){
            if (e.isNotFound()) {
                System.out.printf("Stream %s doesn't exist\n", keyA);
            }else {
                System.out.println(keyA + " should not exist");
                e.printStackTrace();
                return;
            }
        }
        System.out.printf("keyA=%s 不存在\n",keyA);





        //创建重复的流
        try {
            hub.create(keyA);
        } catch (PiliException e) {
            if (!e.isDuplicate()){
                e.printStackTrace();
                return;
            }
        }
        System.out.printf("keyA=%s 已存在\n", keyA);

        //创建另一路流
        String keyB = streamKeyPrefix + "B";
        Stream streamB;
        try{
            streamB = hub.create(keyB);
        }catch(PiliException e){
            e.printStackTrace();
            return;
        }
        System.out.printf("keyB=%s 创建: %s\n", keyB, streamB.toJson());

        //列出所有流
        try{
            Hub.ListRet listRet = hub.list(streamKeyPrefix, 0, "");
            System.out.printf("hub=%s 列出流: keys=%s marker=%s\n", hubName,printArrary(listRet.keys) , listRet.omarker);
        }catch (PiliException e){
            e.printStackTrace();
            return;
        }

        //列出正在直播的流
        try{
            Hub.ListRet listRet = hub.listLive(streamKeyPrefix, 0, "");
            System.out.printf("hub=%s 列出正在直播的流: keys=%s marker=%s\n", hubName, printArrary(listRet.keys), listRet.omarker);
        }catch (PiliException e){
            e.printStackTrace();
            return;
        }

        //禁用流
        try{
            streamA.disable();
            streamA = hub.get(keyA);
        }catch (PiliException e ){
            e.printStackTrace();
            return;
        }
        System.out.printf("keyA=%s 禁用: %s\n", keyA, streamA.toJson());

        //启用流
        try{
            streamA.enable();
            streamA = hub.get(keyA);
        }catch (PiliException e ){
            e.printStackTrace();
            return;
        }
        System.out.printf("keyA=%s 启用: %s\n", keyA, streamA.toJson());

        //查询直播状态
        try{
            Stream.LiveStatus status = streamA.liveStatus();
            System.out.printf("keyA=%s 直播状态: status=%s\n", keyA, status.toJson());
        }catch (PiliException e){
            if (!e.isNotInLive()) {
                e.printStackTrace();
                return;
            }else{
                System.out.printf("keyA=%s 不在直播\n",keyA);
            }
        }

        //查询推流历史
        Stream.Record[] records;
        try{
            records = streamA.historyRecord(0, 0);
        }catch (PiliException e){
            e.printStackTrace();
            return;
        }
        System.out.printf("keyA=%s 推流历史: records=%s\n", keyA, printArrary(records));

        //保存直播数据
        String fname = null;
        try {
            fname = streamA.save(0,0);
        }catch (PiliException e){
            if (!e.isNotInLive()) {
                e.printStackTrace();
                return;
            }else{
                System.out.printf("keyA=%s 不在直播\n",keyA);
            }
        }
        System.out.printf("keyA=%s 保存直播数据: fname=%s\n", keyA, fname);

        //保存直播数据并获取作业id
        String fname2 = null;
        try {
            Stream.SaveOptions options = new Stream.SaveOptions();
            options.start = 0;
            options.end = 0;
            options.format = "mp4";

            Map<String, String> ret = streamA.saveReturn(options);
            System.out.println( "fname:" + ret.get("fname") );
            System.out.println( "persistentID:" + ret.get("persistentID") );
        }catch (PiliException e){
            if (!e.isNotInLive()) {
                e.printStackTrace();
                return;
            }else{
                System.out.printf("keyA=%s 不在直播\n","hutext");
            }
        }

    }*/

    private static String printArrary(Object[] arr){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Object a : arr){
            sb.append(a.toString()+" ");
        }
        sb.append("]");
        return sb.toString();
    }

    public  String doQiNiuGet(String hub){

        String url = "http://pili.qiniuapi.com/v2/hubs/"+ hub +"/stat/play";
        System.out.println(url);

        // header是七牛管理凭证中的signingStr
        String header = "GET /v2/hubs/"+hub+"/stat/play"+
                "\nHost: pili.qiniuapi.com" +
                "\nContent-Type: application/json" +
                "\n\n";

        System.out.println(header);
        //hmacSha1加密需要引入commons-codec（1.10版本）
        // sign secret key
        byte[] sign = HmacUtils.hmacSha1("Zt-mh_ogeLzbczhQZCGk82YWuwJ_-xvcGAIaKs5z",header);
        //base64加密，用七牛直播SDK中给的base64就行
        String encodedSign = Base64.encodeToString(sign,0).replace('+','-').replace('/','_');
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("Host","pili.qiniuapi.com");
        // Authorization: Qiniu access key:+sign,
        //注意：base64 加密后字符超过76个以后会在最后加/n，要去除这个/n
        String authorization = ("Qiniu KHCzbpIL_mUAG8Dh5MZxFb9ahViYoMiSnVbTqwvx:" + encodedSign).replaceAll("\n","");
        httpHeaders.add("Authorization",authorization);
        System.out.println(httpHeaders);
        //使用RestTemplate放出get请求
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> requestEntity = new HttpEntity<String>(null, httpHeaders);
        ResponseEntity<String> results = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        String responseBody = results.getBody();
        System.out.println("get body:" + responseBody.toString());
        return responseBody;
    }

}

