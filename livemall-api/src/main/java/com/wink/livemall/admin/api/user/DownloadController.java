package com.wink.livemall.admin.api.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;



@Api(tags = "分享接口")
@RestController
@RequestMapping("/download")
public class DownloadController {



    @RequestMapping("/downloadURL")
    @ApiOperation(value = "获取邀请码URL",notes = "获取邀请码接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "inviteCode", value = "邀请码", dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型", dataType = "String",paramType = "query")
    })
    private void  getInviteURL(HttpServletResponse response, String inviteCode, String type) throws Exception {

        Map map = new HashMap();
        map.put("inviteCode",inviteCode);
        map.put("type",type);
        String url="http://xiyi.xunshun.net:8989/download.html?inviteCode="+inviteCode+"&type="+type;
        response.sendRedirect(url);
    }

}
