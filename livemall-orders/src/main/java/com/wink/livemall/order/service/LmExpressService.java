package com.wink.livemall.order.service;

import com.wink.livemall.order.dto.LmExpress;

import java.util.List;

public interface LmExpressService {
    List<LmExpress> findActiveList();
}
