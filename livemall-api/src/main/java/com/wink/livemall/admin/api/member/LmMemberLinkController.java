package com.wink.livemall.admin.api.member;


import com.wink.livemall.admin.util.*;
import com.wink.livemall.coupon.dto.LmCoupons;
import com.wink.livemall.member.dto.LmMember;
import com.wink.livemall.member.dto.LmMemberLink;
import com.wink.livemall.member.service.LmMemberLinkService;
import com.wink.livemall.member.service.LmMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Api(tags = "多账号登录接口")
@RestController
@RequestMapping("/lmMemberLink")
public class LmMemberLinkController {

    Logger logger = LogManager.getLogger(LmMemberLinkController.class);

    @Autowired
    private LmMemberLinkService lmMemberLinkService;
    @Autowired
    private LmMemberService lmMemberService;

    @RequestMapping("/memberLinkList")
    @ResponseBody
    @ApiOperation(value = "关联账号列表",notes = "关联账号列表接口")
    private HttpJsonResult merchCouponList(HttpServletRequest request) throws Exception {
        HttpJsonResult jsonResult=new HttpJsonResult<>();
        String header = request.getHeader("Authorization");
        String userid = "";
        if (!StringUtils.isEmpty(header)) {
            if(!StringUtils.isEmpty(TokenUtil.getUserId(header))){
                userid = TokenUtil.getUserId(header);
            }else{
                jsonResult.setMsg("用户未登陆,请重新登陆");
                jsonResult.setCode(Errors.LOGIN.getCode());
                return jsonResult;
            }
        }
        try {
            List< Map<String, Object>> list =new LinkedList<>();
            Map<String, Object> user = lmMemberService.findByIdList(Integer.parseInt(userid));
            user.put("linkId",0);
            list.add(user);
            List<LmMemberLink> memberIdOrLinkId = lmMemberLinkService.findMemberIdOrLinkId(Integer.parseInt(userid));
            if(memberIdOrLinkId!=null&&memberIdOrLinkId.size()>0){
                for(LmMemberLink lmMemberLink:memberIdOrLinkId ){
                    Map<String, Object> byIdList = lmMemberService.findByIdList(lmMemberLink.getLink_id());
                    byIdList.put("linkId",lmMemberLink.getId());
                    list.add(byIdList);
                }
            }
            jsonResult.setData(list);
            jsonResult.setCode(Errors.SUCCESS.getCode());
            jsonResult.setMsg(Errors.SUCCESS.getMsg());
            return jsonResult;
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(Errors.ERROR.getCode());
            logger.error(e.getMessage());
        }
        return jsonResult;
    }

    @RequestMapping("/delMemberLink")
    @ResponseBody
    @ApiOperation(value = "删除关联账号列表",notes = "关联账号列表接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "linkId", value = "linkId", dataType = "Integer",paramType = "query")
    })
    private HttpJsonResult delMemberLink(HttpServletRequest request,Integer linkId) throws Exception {
        HttpJsonResult jsonResult=new HttpJsonResult<>();
        try {
            lmMemberLinkService.delMemberLink(linkId);
            jsonResult.setCode(Errors.SUCCESS.getCode());
            jsonResult.setMsg(Errors.SUCCESS.getMsg());
            return jsonResult;
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(Errors.ERROR.getCode());
            logger.error(e.getMessage());
        }
        return jsonResult;
    }


    @RequestMapping("/addMemberLink")
    @ResponseBody
    @ApiOperation(value = "添加关联账号列表",notes = "关联账号列表接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "mobile", dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "password", value = "password", dataType = "String",paramType = "query"),
    })
    private HttpJsonResult addMemberLink(HttpServletRequest request,String mobile,String password) throws Exception {
        HttpJsonResult jsonResult=new HttpJsonResult<>();
        String header = request.getHeader("Authorization");
        String userid = "";
        if (!StringUtils.isEmpty(header)) {
            if(!StringUtils.isEmpty(TokenUtil.getUserId(header))){
                userid = TokenUtil.getUserId(header);
            }else{
                jsonResult.setMsg("用户未登陆,请重新登陆");
                jsonResult.setCode(Errors.LOGIN.getCode());
                return jsonResult;
            }
        }
        try {
            LmMember lmMember = lmMemberService.findByMobile(mobile);
            if(lmMember!=null){
                if(lmMember.getPassword().equals(password)){
                    List<LmMemberLink> link = lmMemberLinkService.findLink(lmMember.getId(), Integer.parseInt(userid));
                    if(link!=null&&link.size()>0){
                        jsonResult.setCode(Errors.ERROR.getCode());
                        jsonResult.setMsg("已添加过，不需要再次添加");
                        return jsonResult;
                    }else {
                        LmMemberLink lmMemberLink = new LmMemberLink();
                        lmMemberLink.setMember_id(Integer.parseInt(userid));
                        lmMemberLink.setLink_id(lmMember.getId());
                        lmMemberLink.setCreate_time(new Date());
                        lmMemberLinkService.insert(lmMemberLink);
                    }
                }else {
                    jsonResult.setCode(Errors.ERROR.getCode());
                    jsonResult.setMsg("密码不正确");
                    return jsonResult;
                }
            }else {
                jsonResult.setCode(Errors.ERROR.getCode());
                jsonResult.setMsg("账号不存在");
                return jsonResult;
            }
            jsonResult.setCode(Errors.SUCCESS.getCode());
            jsonResult.setMsg(Errors.SUCCESS.getMsg());
            return jsonResult;
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(Errors.ERROR.getCode());
            logger.error(e.getMessage());
        }
        return jsonResult;
    }

}
