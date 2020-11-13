package com.wink.livemall.video.service;

import com.wink.livemall.video.dto.LmVideoCategoary;

import java.util.List;
import java.util.Map;

public interface LmVideoCategoaryService {
    LmVideoCategoary findById(String id);

    void addService(LmVideoCategoary lmVideoCategoary);

    void deleteService(int parseInt);

    void updateService(LmVideoCategoary lmVideoCategoary);

    List<Map> findByCondient(Map<String, String> condient);

    List<LmVideoCategoary> findAll();

    List<LmVideoCategoary> findtopcategory();

    List<Map<String, String>> findtopcategoryByApi();
}
