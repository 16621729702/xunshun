package com.wink.livemall.admin.api.help;

import com.wink.livemall.admin.api.good.GoodController;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.help.dto.LmFeedback;
import com.wink.livemall.help.dto.LmHelpInfo;
import com.wink.livemall.help.service.LmFeedbackService;
import com.wink.livemall.help.service.LmHelpCategoryService;
import com.wink.livemall.help.service.LmHelpInfoService;
import com.wink.livemall.sys.basic.dto.LmBasicConfig;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Api(tags = "帮助信息模块")
@RestController
@RequestMapping("help")
public class HelpController {
    Logger logger = LogManager.getLogger(HelpController.class);

    @Autowired
    private LmHelpCategoryService lmHelpCategoryService;
    @Autowired
    private LmHelpInfoService lmHelpInfoService;
    @Autowired
    private LmFeedbackService lmFeedbackService;
    /**
     * 获取帮助顶级分类
     */
    @ApiOperation(value = "获取帮助顶级分类")
    @PostMapping("top")
    public JsonResult top(HttpServletRequest request){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            //获取顶级选项
            List<Map<String,String>> list = lmHelpCategoryService.findTopByApi();
            jsonResult.setData(list);
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }

    /**
     * 根据顶级分类获取子分类
     */
    @ApiOperation(value = "根据顶级分类获取子分类")
    @PostMapping("/list/{categoryid}")
    public JsonResult list(@ApiParam(name = "categoryid", value = "分类id",defaultValue = "0", required = true)@PathVariable String categoryid, HttpServletRequest request){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            //获取顶级选项
            List<Map<String,String>> list = lmHelpInfoService.findBycategoryid(categoryid);
            jsonResult.setData(list);
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }

    /**
     * 获取帮助详细信息
     */
    @ApiOperation(value = "获取帮助详细信息")
    @PostMapping("/detail/{helpid}")
    public JsonResult detail(@ApiParam(name = "helpid", value = "帮助id",defaultValue = "0", required = true)@PathVariable String helpid,HttpServletRequest request){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            //获取顶级选项
            LmHelpInfo lmHelpInfo = lmHelpInfoService.findById(helpid);
            jsonResult.setData(lmHelpInfo);
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }

    /**
     * 反馈提交
     */
    @ApiOperation(value = "反馈提交")
    @PostMapping("/commit")
    public JsonResult commit(HttpServletRequest request,
                             @ApiParam(name = "type", value = "返回类型",defaultValue = "0", required = true)@RequestParam int type,
                             @ApiParam(name = "content", value = "内容",defaultValue = "0", required = true)@RequestParam String content,
                             @ApiParam(name = "imgs", value = "图片，隔开",defaultValue = "0", required = true)@RequestParam String imgs,
                             @ApiParam(name = "state", value = "'0待处理 1已处理';",defaultValue = "0", required = true)@RequestParam int state,
                             @ApiParam(name = "userid", value = "用户id",defaultValue = "0", required = true)@RequestParam int userid
                             ){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            LmFeedback lmFeedback = new LmFeedback();
            lmFeedback.setCreate_at(new Date());
            lmFeedback.setType(type);
            lmFeedback.setContent(content);
            lmFeedback.setImages(imgs);
            lmFeedback.setState(state);
            lmFeedback.setUpdate_at(new Date());
            lmFeedback.setMember_id(userid);
            lmFeedbackService.insertService(lmFeedback);
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }


}
