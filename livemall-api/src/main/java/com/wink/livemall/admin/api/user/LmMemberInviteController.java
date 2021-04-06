package com.wink.livemall.admin.api.user;

import com.wink.livemall.admin.util.Errors;
import com.wink.livemall.admin.util.HttpJsonResult;
import com.wink.livemall.admin.util.TokenUtil;
import com.wink.livemall.member.dto.LmMemberInvite;
import com.wink.livemall.member.service.LmMemberInviteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "分享接口")
@RestController
@RequestMapping("/invite")
public class LmMemberInviteController {

    Logger logger = LogManager.getLogger(LmMemberInviteController.class);

    @Autowired
    private LmMemberInviteService lmMemberInviteService;


    @RequestMapping("/getInviteCode")
    @ResponseBody
    @ApiOperation(value = "获取邀请码",notes = "获取邀请码接口")
    @ApiImplicitParams({})
    private HttpJsonResult merchCouponList(HttpServletRequest request) throws Exception {
        HttpJsonResult jsonResult = new HttpJsonResult();
        String header = request.getHeader("Authorization");
        String userId="";
        if (!StringUtils.isEmpty(header)) {
            if(!StringUtils.isEmpty(TokenUtil.getUserId(header))){
                userId = TokenUtil.getUserId(header);
            }else{
                jsonResult.setCode(Errors.TOKEN_PAST.getCode());
                jsonResult.setMsg(Errors.TOKEN_PAST.getMsg());
                return jsonResult;
            }
        }
        try {
            LmMemberInvite lmMemberInvite = lmMemberInviteService.getInviteCode(userId);
            jsonResult.setData(lmMemberInvite.getInvite_code());
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

    @RequestMapping("/getInviteURL")
    @ApiOperation(value = "获取邀请码URL",notes = "获取邀请码接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "inviteCode", value = "邀请码", dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型", dataType = "String",paramType = "query")
    })
    private void  getInviteURL(HttpServletResponse response, String inviteCode, String type) throws Exception {

        Map map = new HashMap();
        map.put("inviteCode",inviteCode);
        map.put("type",type);
        String url="http://xiyi.xunshun.net:8989/invite.html?inviteCode="+inviteCode+"&type="+type;
        response.sendRedirect(url);
    }






}
