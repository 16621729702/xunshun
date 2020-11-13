package com.wink.livemall.sys.setting.service;

import com.wink.livemall.sys.setting.dto.Templates;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TemplateService {

    Templates findById(String id);

    void deleteById(String id);

    void addService(Templates templates);

    void updateService(Templates templates);

    List<Templates> findByCondient(Map<String, String> condient);
}
