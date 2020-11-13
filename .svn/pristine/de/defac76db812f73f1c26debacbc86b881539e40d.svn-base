package com.wink.livemall.help.service;

import com.wink.livemall.help.dto.LmFeedback;
import com.wink.livemall.help.dto.LmHelpCategory;

import java.util.List;
import java.util.Map;

public interface LmHelpCategoryService {

    //根据主键查询
    LmHelpCategory findById(String id);
    //查询所有
    List<LmHelpCategory> findAll();

    void insertService(LmHelpCategory lmHelpCategory);

    void updateService(LmHelpCategory lmHelpCategory);

    void deleteService(String id);

    List<Map<String, String>> findTopByApi();

    List<Map<String, String>> findByPidByApi(String categoryid);
}
