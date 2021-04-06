package com.wink.livemall.live.service;

import com.wink.livemall.live.dto.LmLive;

import java.util.List;
import java.util.Map;

public interface LmLiveService {

    List<Map<String,Object>> findListByCategoryIdByApi(int pid);

    List<Map<String,Object>> findfollewLiveByApi(int id);

    Map<String, Object> finddirectlyinfoByApi();

    List<Map<String,Object>> findhotlive();

    List<Map<String, String>> findsharefollewLiveByApi(int pid);

    List<Map<String, String>> findshareListByCategoryIdByApi(int pid);

    Map<String, Object> findsharehotlive();

    Map<String, String> findRecommendLiveByapi();

    List<Map<String,Object>> findHotLiveByApi();

    Map<String, String> findShareRecommendLiveByapi();

    List<Map<String, String>> findShareHotLiveByApi();

    List<Map<String, Object>> findByCondient(Map<String, String> condient);

    void deleteService(int id);

    void insertService(LmLive lmLive);

    LmLive findbyId(String id);

    void updateService(LmLive lmLive);

    List<Map<String, String>> findListByNameByApi(String name);

    List<LmLive> findLiveedLive();

    //查找所有正常直播间
    List<LmLive> findLiveList();

    //查找所有预播的直播间
    List<LmLive> findLivePreview();

    //查找热门资讯的直播间
    List<LmLive> findHotLive();

    LmLive findByMerchid(int merchid);
}
