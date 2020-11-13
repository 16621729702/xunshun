package com.wink.livemall.admin.api.agreement;

import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.sys.basic.dto.LmBasicConfig;
import com.wink.livemall.sys.basic.service.LmBasicConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 获取隐私政策 和 用户协议
 */
@RestController
@Api(tags = "政策协议接口")
public class AgreementController {
    @Autowired
    private LmBasicConfigService lmBasicConfigService;
    /**
     * 获取用户协议
     */
    @PostMapping("agreement")
    @ApiOperation(value = "获取用户协议")
    public JsonResult useragreement(HttpServletRequest request){
        JsonResult jsonResult = new JsonResult();
        LmBasicConfig lmBasicConfig = lmBasicConfigService.findByType(LmBasicConfig.YHXY);
        jsonResult.setCode(jsonResult.SUCCESS);
        jsonResult.setMsg("");
        jsonResult.setData(lmBasicConfig);
        return jsonResult;
    }

    /**
     * 获取隐私政策
     */
    @PostMapping("privacypolicy")
    @ApiOperation(value = "获取隐私政策")
    public JsonResult privacypolicy(HttpServletRequest request){
        JsonResult jsonResult = new JsonResult();
        LmBasicConfig lmBasicConfig = lmBasicConfigService.findByType(LmBasicConfig.YSZC);
        jsonResult.setCode(jsonResult.SUCCESS);
        jsonResult.setMsg("");
        jsonResult.setData(lmBasicConfig);
        return jsonResult;
    }

}
