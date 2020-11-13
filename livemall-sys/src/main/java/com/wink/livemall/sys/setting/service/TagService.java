package com.wink.livemall.sys.setting.service;

import com.wink.livemall.sys.setting.dto.Tag;

import java.util.List;
import java.util.Map;

public interface TagService {
    Tag findById(String id);

    void deleteById(String id);

    void addService(Tag tag);

    void updateService(Tag tag);

    List<Tag> findByCondient(Map<String, String> condient);
}
