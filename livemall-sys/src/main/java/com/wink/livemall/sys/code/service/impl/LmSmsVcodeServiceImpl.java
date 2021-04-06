package com.wink.livemall.sys.code.service.impl;

import com.wink.livemall.sys.code.dao.LmSmsVcodeDao;
import com.wink.livemall.sys.code.dto.LmSmsVcode;
import com.wink.livemall.sys.code.service.LmSmsVcodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LmSmsVcodeServiceImpl implements LmSmsVcodeService {

    @Resource
    private LmSmsVcodeDao lmSmsVcodeDao;
    @Override
    public void addservice(LmSmsVcode lmSmsVcode) {
        lmSmsVcodeDao.insert(lmSmsVcode);
    }

    @Override
    public List<LmSmsVcode> findByMobile(String mobile) {
        return lmSmsVcodeDao.findByMobile(mobile);
    }

    @Override
    public List<LmSmsVcode> restrictMobile(String mobile) {
        return lmSmsVcodeDao.restrictMobile(mobile);
    }

}
