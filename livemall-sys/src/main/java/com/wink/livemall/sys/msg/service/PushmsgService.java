package com.wink.livemall.sys.msg.service;

import java.util.List;
import java.util.Map;


public interface PushmsgService {
    void send(int sendid, String content, String type, int receiveid,int ismerch);
    
    /**
     * 获取商户端消息类表分页
     * @return
     */
    List<Map<String, Object>> findPageMerch(Map<String, String> params);

    List<Map<String, Object>> getlistByreceiveid(int receiveid);

    List<Map<String, Object>> getlistBysendid(int sendid);

    List<Map<String, Object>> getlistBysendidAndreceiveid(int sendid, int receiveid);

    void deleteservice(int id);
}
