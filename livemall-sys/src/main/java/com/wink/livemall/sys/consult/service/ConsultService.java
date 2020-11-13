package com.wink.livemall.sys.consult.service;

import com.wink.livemall.sys.consult.dto.Consult;

import java.util.List;

public interface ConsultService {
    List<String> findActiveList();

    String findInfoByTitle(String title);

    List<Consult> findAll();

    Consult findById(String id);

    void addService(Consult consult);

    void updateservice(Consult consult);

    void deleteService(String id);
}
