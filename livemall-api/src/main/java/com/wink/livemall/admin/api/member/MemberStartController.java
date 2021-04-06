package com.wink.livemall.admin.api.member;


import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.member.dto.LmMemberStart;
import com.wink.livemall.member.service.LmMemberStartService;
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
    public JsonResult memberstart(HttpServletRequest request){
        JsonResult jsonResult = new JsonResult();
        String mobile=request.getParameter("mobile");
        Integer type=Integer.parseInt(request.getParameter("type"));
        Integer businessid=Integer.parseInt(request.getParameter("businessid"));
        try {
            List<LmMemberStart> list = lmMemberStartService.findByMobile(mobile,type,businessid);
            Map<String, Object> resdata=new HashMap<String, Object>();
            if(list!=null&&list.size()>0) {
                LmMemberStart lmMemberStart = list.get(0);
                resdata.put("margin", lmMemberStart.getMargin());
                resdata.put("coupon_price", lmMemberStart.getCoupon_price());
                jsonResult.setData(resdata);
            }else {
                resdata.put("margin","0");
                resdata.put("coupon_price", "-1");
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

    @ApiOperation(value = "一次优惠")
    @PostMapping("end")
    public JsonResult memberend(HttpServletRequest request){
        JsonResult jsonResult = new JsonResult();
        String mobile=request.getParameter("mobile");
        Integer type=Integer.parseInt(request.getParameter("type"));
        Integer businessid=Integer.parseInt(request.getParameter("businessid"));
        try {
            List<LmMemberStart> list = lmMemberStartService.findByMobile(mobile,type,businessid);
            for(LmMemberStart end:list){
                end.setIsstart(0);
                lmMemberStartService.updateMobile(end);
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


