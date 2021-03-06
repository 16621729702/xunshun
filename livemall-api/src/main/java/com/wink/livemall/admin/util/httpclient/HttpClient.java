package com.wink.livemall.admin.util.httpclient;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.wink.livemall.admin.util.IMUtil;
import com.wink.livemall.admin.util.SpringUtil;
import com.wink.livemall.sys.setting.dto.Configs;
import com.wink.livemall.sys.setting.service.ConfigsService;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


public class HttpClient {



    /**
     * 腾讯im的接口
     */
    public void login(String headurl,String id,String nickname) {
        ApplicationContext context = SpringUtil.getApplicationContext();
        ConfigsService configsService = context.getBean(ConfigsService.class);
        Configs configs =configsService.findByTypeId(Configs.type_chat);
        Map chat =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
        long appid = Long.parseLong(chat.get("sdk_appid")+"");
        String privateinfo = chat.get("privateinfo")+"";
        String identifier = chat.get("identifier")+"";
        String sign = IMUtil.genSig(appid,privateinfo,identifier);
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        Random ran=new Random();
        String url = "https://console.tim.qq.com/v4/im_open_login_svc/account_import?sdkappid="+appid+"&identifier=administrator&usersig="+sign+"&random="+ran.nextInt(1000000)+"&contenttype=json";
        HttpPost httpPost = new HttpPost(url);
        Map<String,String> map = new HashMap<>();
        map.put("Identifier",id);
        map.put("Nick",nickname);
        map.put("FaceUrl",headurl);
        // 响应模型
        CloseableHttpResponse response = null;
        String body = JSONUtils.toJSONString(map);
        try {
            httpPost.setEntity(new StringEntity(body,"UTF-8"));
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();

            System.out.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    /**
     * 发送单聊信息
     */
    public void send(String fromid,String touserid,String text) {
        ApplicationContext context = SpringUtil.getApplicationContext();
        ConfigsService configsService = context.getBean(ConfigsService.class);
        Configs configs =configsService.findByTypeId(Configs.type_chat);
        Map chat =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
        long appid = Long.parseLong(chat.get("sdk_appid")+"");
        String privateinfo = chat.get("privateinfo")+"";
        String identifier = chat.get("identifier")+"";
        String sign = IMUtil.genSig(appid,privateinfo,identifier);
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        Random ran=new Random();
        String url = "https://console.tim.qq.com/v4/openim/sendmsg?sdkappid="+appid+"&identifier=administrator&usersig="+sign+"&random="+ran.nextInt(1000000)+"&contenttype=json";
        HttpPost httpPost = new HttpPost(url);
        Map<String,Object> map = new HashMap<>();
        map.put("SyncOtherMachine",2);
        map.put("From_Account",fromid);
        map.put("To_Account",touserid);
        map.put("MsgRandom",ran.nextInt(1000000));
        map.put("MsgTimeStamp",System.currentTimeMillis()/1000);
        List<Map> array = new ArrayList<>();
        Map<String,Object> childmap = new HashMap<>();
        childmap.put("MsgType","TIMTextElem");
        Map<String,Object> cchildmap = new HashMap<>();
        cchildmap.put("Text",text);
        childmap.put("MsgContent",cchildmap);
        array.add(childmap);
        map.put("MsgBody",array);
        // 响应模型
        CloseableHttpResponse response = null;
        String body = JSONUtils.toJSONString(map);
        try {
            httpPost.setEntity(new StringEntity(body,"UTF-8"));
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            System.out.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 发送群组信息
     */
    public void sendgroup(String groupid,String text,int type) {
        ApplicationContext context = SpringUtil.getApplicationContext();
        ConfigsService configsService = context.getBean(ConfigsService.class);
        Configs configs =configsService.findByTypeId(Configs.type_chat);
        Map chat =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
        long appid = Long.parseLong(chat.get("sdk_appid")+"");
        String privateinfo = chat.get("privateinfo")+"";
        String identifier = chat.get("identifier")+"";
        String sign = IMUtil.genSig(appid,privateinfo,identifier);
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        Random ran=new Random();
        String url = "https://console.tim.qq.com/v4/group_open_http_svc/send_group_msg?sdkappid="+appid+"&identifier=administrator&usersig="+sign+"&random="+ran.nextInt(1000000)+"&contenttype=json";
        HttpPost httpPost = new HttpPost(url);
        Map<String,Object> map = new HashMap<>();
        map.put("GroupId",groupid);
        map.put("Random",ran.nextInt(1000000));
        List<Map> array = new ArrayList<>();
        Map<String,Object> childmap = new HashMap<>();
        childmap.put("MsgType","TIMCustomElem");
        Map<String,Object> datainfo = new HashMap<>();
        Map<String,Object> cchildmap = new HashMap<>();
        cchildmap.put("data",text);
        cchildmap.put("type",type);
        datainfo.put("Data",new Gson().toJson(cchildmap));
        childmap.put("MsgContent",datainfo);
        array.add(childmap);
        map.put("MsgBody",array);
        // 响应模型
        CloseableHttpResponse response = null;
        String body = JSONUtils.toJSONString(map);
        try {
            httpPost.setEntity(new StringEntity(body,"UTF-8"));
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();

            System.out.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送群组信息
     */
    public void sendgroup(String groupid,String text,int type,int fromsendid) {
        ApplicationContext context = SpringUtil.getApplicationContext();
        ConfigsService configsService = context.getBean(ConfigsService.class);
        Configs configs =configsService.findByTypeId(Configs.type_chat);
        Map chat =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
        long appid = Long.parseLong(chat.get("sdk_appid")+"");
        String privateinfo = chat.get("privateinfo")+"";
        String identifier = chat.get("identifier")+"";
        String sign = IMUtil.genSig(appid,privateinfo,identifier);
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        Random ran=new Random();
        String url = "https://console.tim.qq.com/v4/group_open_http_svc/send_group_msg?sdkappid="+appid+"&identifier=administrator&usersig="+sign+"&random="+ran.nextInt(1000000)+"&contenttype=json";
        HttpPost httpPost = new HttpPost(url);
        Map<String,Object> map = new HashMap<>();
        map.put("GroupId",groupid);
        map.put("From_Account",fromsendid+"");
        map.put("Random",ran.nextInt(1000000));
        List<Map> array = new ArrayList<>();
        Map<String,Object> childmap = new HashMap<>();
        childmap.put("MsgType","TIMCustomElem");
        Map<String,Object> datainfo = new HashMap<>();
        Map<String,Object> cchildmap = new HashMap<>();
        cchildmap.put("data",text);
        cchildmap.put("type",type);
        datainfo.put("Data",new Gson().toJson(cchildmap));
        childmap.put("MsgContent",datainfo);
        array.add(childmap);
        map.put("MsgBody",array);
        // 响应模型
        CloseableHttpResponse response = null;
        String body = JSONUtils.toJSONString(map);
        try {
            httpPost.setEntity(new StringEntity(body,"UTF-8"));
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();

            System.out.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 禁言
     */
    public void forbid(String groupid,String memberaccount,int times) {
        ApplicationContext context = SpringUtil.getApplicationContext();
        ConfigsService configsService = context.getBean(ConfigsService.class);
        Configs configs =configsService.findByTypeId(Configs.type_chat);
        Map chat =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
        long appid = Long.parseLong(chat.get("sdk_appid")+"");
        String privateinfo = chat.get("privateinfo")+"";
        String identifier = chat.get("identifier")+"";
        String sign = IMUtil.genSig(appid,privateinfo,identifier);
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        Random ran=new Random();
        String url = "https://console.tim.qq.com/v4/group_open_http_svc/forbid_send_msg?sdkappid="+appid+"&identifier=administrator&usersig="+sign+"&random="+ran.nextInt(1000000)+"&contenttype=json";
        HttpPost httpPost = new HttpPost(url);
        Map<String,Object> map = new HashMap<>();
        map.put("GroupId",groupid);
        List<String> array = new ArrayList<>();
        array.add(memberaccount);
        map.put("Members_Account",array);
        map.put("ShutUpTime",times);
        // 响应模型
        CloseableHttpResponse response = null;
        String body = JSONUtils.toJSONString(map);
        try {
            httpPost.setEntity(new StringEntity(body,"UTF-8"));
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();

            System.out.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    /**
     * 创建zu
     */
    public Map creatgroup(String groupname) {
        ApplicationContext context = SpringUtil.getApplicationContext();
        ConfigsService configsService = context.getBean(ConfigsService.class);
        Configs configs =configsService.findByTypeId(Configs.type_chat);
        Map chat =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
        long appid = Long.parseLong(chat.get("sdk_appid")+"");
        String privateinfo = chat.get("privateinfo")+"";
        String identifier = chat.get("identifier")+"";
        String sign = IMUtil.genSig(appid,privateinfo,identifier);
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        Random ran=new Random();
        String url = "https://console.tim.qq.com/v4/group_open_http_svc/create_group?sdkappid="+appid+"&identifier=administrator&usersig="+sign+"&random="+ran.nextInt(1000000)+"&contenttype=json";
        HttpPost httpPost = new HttpPost(url);
        Map<String,Object> map = new HashMap<>();
        map.put("Type","AVChatRoom");
        map.put("Name",groupname);
        Map<String,Object> returnmap = new HashMap<>();
        // 响应模型
        CloseableHttpResponse response = null;
        String body = JSONUtils.toJSONString(map);
        try {
            httpPost.setEntity(new StringEntity(body));
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();

            System.out.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                String result = EntityUtils.toString(responseEntity);
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                System.out.println("响应内容为:" + result);
                returnmap =  JSONObject.parseObject(result);
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
            return returnmap;
    }



    /**
     * 删除群组
     */
    public void deleteGroup(String groupid) {
        ApplicationContext context = SpringUtil.getApplicationContext();
        ConfigsService configsService = context.getBean(ConfigsService.class);
        Configs configs =configsService.findByTypeId(Configs.type_chat);
        Map chat =  com.alibaba.fastjson.JSONObject.parseObject(configs.getConfig());
        long appid = Long.parseLong(chat.get("sdk_appid")+"");
        String privateinfo = chat.get("privateinfo")+"";
        String identifier = chat.get("identifier")+"";
        String sign = IMUtil.genSig(appid,privateinfo,identifier);
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        Random ran=new Random();
        String url = "https://console.tim.qq.com/v4/group_open_http_svc/destroy_group?sdkappid="+appid+"&identifier=administrator&usersig="+sign+"&random="+ran.nextInt(1000000)+"&contenttype=json";
        HttpPost httpPost = new HttpPost(url);
        Map<String,Object> map = new HashMap<>();
        map.put("GroupId",groupid);

        // 响应模型
        CloseableHttpResponse response = null;
        String body = JSONUtils.toJSONString(map);
        try {
            httpPost.setEntity(new StringEntity(body));
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();

            System.out.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 获取组消息
     */
    public static Map getGroupNews() {
        long appid = Long.parseLong("1400404340");
        String privateinfo ="d99a57a24b9dd20a47f628b2cfbaba0569041feeec88fd651e87aa3641ec6288";
        String identifier ="administrator";
        String sign = IMUtil.genSig(appid,privateinfo,identifier);
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        Random ran=new Random();
        String url = "https://console.tim.qq.com/v4/open_msg_svc/get_history?sdkappid="+appid+"&identifier=administrator&usersig="+sign+"&random="+ran.nextInt(1000000)+"&contenttype=json";
        HttpPost httpPost = new HttpPost(url);
        Map<String,Object> map = new HashMap<>();
        map.put("ChatType","Group");
        map.put("MsgTime","2021031012");
        Map<String,Object> returnmap = new HashMap<>();
        // 响应模型
        CloseableHttpResponse response = null;
        String body = JSONUtils.toJSONString(map);
        try {
            httpPost.setEntity(new StringEntity(body));
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();

            System.out.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                String result = EntityUtils.toString(responseEntity);
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                System.out.println("响应内容为:" + result);
                returnmap =  JSONObject.parseObject(result);
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return returnmap;
    }

    public static void main(String[] args) throws java.text.ParseException {
      /*  Map map = HttpClient.getGroupNews();
        System.out.println(map);*/
   /*   String   msg = "系统提示:您的店铺青田国石居中的‘老封门竹叶且’商品产品图上有微拍堂水印，请及时修改";
        HttpClient httpClient = new HttpClient();
        httpClient.send("交易消息","533",msg);*/
    }
}
