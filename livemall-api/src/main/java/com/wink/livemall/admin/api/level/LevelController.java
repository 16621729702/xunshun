package com.wink.livemall.admin.api.level;

import com.wink.livemall.admin.dtovo.LevelVo;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.member.service.LmMemberLevelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Api(tags = "会员成长值")
@RestController
@RequestMapping("level")
public class LevelController {

    @Autowired
    private LmMemberLevelService lmMemberLevelService;

    /**
     * 获取成长值分段列表
     */
    @ApiOperation(value = "获取成长值分段列表")
    @PostMapping("list")
    @ApiResponses({
            @ApiResponse(code = 200, message = "ok", response= LevelVo.class),
    })
    public JsonResult top(HttpServletRequest request){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            List<Map<String,String>> list = lmMemberLevelService.findInfoByApi();
            jsonResult.setData(list);
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
        }
        return jsonResult;
    }



}
