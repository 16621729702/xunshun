package com.wink.livemall.admin.api.member;


import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.member.dto.LmMemberStart;
import com.wink.livemall.member.service.LmMemberStartService;
import com.wink.livemall.merch.dto.LmMerchCategory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "店铺优惠接口")
@RestController
@RequestMapping("member")
public class MemberStartController {

    private static final Logger logger = LogManager.getLogger(MemberStartController.class);

    @Autowired
    private LmMemberStartService lmMemberStartService;


    /**
     * 获取所有顶级分类
     * @return
     */
    @ApiOperation(value = "获取该手机号码的优惠")
    @PostMapping("start")
    public JsonResult category(HttpServletRequest request){
        JsonResult jsonResult = new JsonResult();
        String mobile=request.getParameter("mobile");
        Integer type=Integer.parseInt(request.getParameter("type"));

        try {
            List<LmMemberStart> list = lmMemberStartService.findByMobile(mobile,type);
            Map<String, Object> resdata=new HashMap<String, Object>();
            if(null!=list) {
                resdata.put("list", list);
                jsonResult.setData(resdata);
            }else {
                resdata.put("list", null);
                jsonResult.setData(resdata);
            }
            jsonResult.setCode(JsonResult.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }
}

