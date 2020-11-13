package com.wink.livemall.video.service;

import com.wink.livemall.video.dto.LmVideoCore;

import java.util.List;
import java.util.Map;

public interface LmVideoCoreService {

    LmVideoCore findById(String id);

    void addService(LmVideoCore lmVideoCore);

    void deleteService(int parseInt);

    void updateService(LmVideoCore lmVideoCore);

    List<Map> findByCondient(Map<String, String> condient);

    Map<String,String> findTopVideo();

    List<Map<String,String>> findHotVideolist();

    List<LmVideoCore> findByCategoryId(String category);

    List<LmVideoCore> findByTag(String tag);

    List<Map<String, String>> findByCategoryIdByApi(String categoryid);
    
    void updateByFields(List<Map<String, String>> params, String id);
    
    List<LmVideoCore> findPage(Map<String, String> params);
    
    void deleteById(int id);
}
