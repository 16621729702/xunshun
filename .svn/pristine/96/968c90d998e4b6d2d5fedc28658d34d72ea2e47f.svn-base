package com.wink.livemall.job.utils;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;

import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

public class HttpClient {




    /**
     * 腾讯im的接口
     */
    public void login(String headurl,String id,String nickname) {
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        Random ran=new Random();
        String url = "https://console.tim.qq.com/v4/im_open_login_svc/account_import?sdkappid="+IMUtil.sakappid+"&identifier=administrator&usersig="+IMUtil.genSig()+"&random="+ran.nextInt(1000000)+"&contenttype=json";
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
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        Random ran=new Random();
        String url = "https://console.tim.qq.com/v4/openim/sendmsg?sdkappid="+IMUtil.sakappid+"&identifier=administrator&usersig="+IMUtil.genSig()+"&random="+ran.nextInt(1000000)+"&contenttype=json";
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
     * 创建zu
     */
    public Map creatgroup(String groupname) {
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        Random ran=new Random();
        String url = "https://console.tim.qq.com/v4/group_open_http_svc/create_group?sdkappid="+IMUtil.sakappid+"&identifier=administrator&usersig="+IMUtil.genSig()+"&random="+ran.nextInt(1000000)+"&contenttype=json";
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
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        Random ran=new Random();
        String url = "https://console.tim.qq.com/v4/group_open_http_svc/destroy_group?sdkappid="+IMUtil.sakappid+"&identifier=administrator&usersig="+IMUtil.genSig()+"&random="+ran.nextInt(1000000)+"&contenttype=json";
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


    public static void main(String[] args) {
        HttpClient client = new HttpClient();
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }

//        client.deleteGroup("@TGS#aQ2CKCUGA");
    }
}
