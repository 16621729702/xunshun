package com.wink.livemall.admin.controller.setting;

import com.wink.livemall.admin.controller.order.CommentsController;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.sys.basic.dto.LmBasicConfig;
import com.wink.livemall.sys.basic.service.LmBasicConfigService;
import com.wink.livemall.sys.dict.dto.LmSysDict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 隐私协议和用户协议
 */
@Controller
@RequestMapping("agreement")
public class AgreementController {
    private Logger LOG= LoggerFactory.getLogger(AgreementController.class);

    @Autowired
    private LmBasicConfigService lmBasicConfigService;

    /**
     * 协议列表
     * @return
     */
    @RequestMapping("query")
    public ModelAndView query(Model model, HttpServletRequest request){
        List<LmBasicConfig> list = lmBasicConfigService.findAll();
        List<Map> agreelist = new ArrayList<>();
        for(LmBasicConfig lmBasicConfig:list){
            Map map = new HashMap();
            map.put("id",lmBasicConfig.getId());
            map.put("comment",lmBasicConfig.getComment());
            map.put("status",lmBasicConfig.getStatus());
            map.put("name",lmBasicConfig.getName());
            map.put("type",LmBasicConfig.parsetochinesetype(lmBasicConfig.getType()));
            agreelist.add(map);
        }
        model.addAttribute("agreelist",agreelist);
        model.addAttribute("totalsize",agreelist.size());
        return new ModelAndView("agreement/agreementlist");
    }

    @RequestMapping("addpage")
    public ModelAndView addpage(Model model){
        return new ModelAndView("agreement/agreementaddpage");
    }

    @RequestMapping("add")
    @ResponseBody
    public JsonResult add(HttpServletRequest request, Model model){
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String type = StringUtils.isEmpty(request.getParameter("type"))?"1":request.getParameter("type");
        String comment = StringUtils.isEmpty(request.getParameter("comment"))?null:request.getParameter("comment");
        String status = StringUtils.isEmpty(request.getParameter("status"))?"1":request.getParameter("status");
        try {
            LmBasicConfig lmBasicConfig = new LmBasicConfig();
            lmBasicConfig.setComment(comment);
            lmBasicConfig.setName(name);
            lmBasicConfig.setStatus(status);
            lmBasicConfig.setType(type);
            lmBasicConfigService.addService(lmBasicConfig);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }

        return new JsonResult();
    }

    @RequestMapping("editpage")
    public ModelAndView editpage(HttpServletRequest request,Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        id = id.replaceAll(",","");
        LmBasicConfig lmBasicConfig = lmBasicConfigService.findById(id);
        model.addAttribute("lmBasicConfig",lmBasicConfig);
        return new ModelAndView("agreement/agreementeditpage");
    }
    @RequestMapping("edit")
    @ResponseBody
    public JsonResult edit(HttpServletRequest request,Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        id = id.replaceAll(",","");
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String type = StringUtils.isEmpty(request.getParameter("type"))?"1":request.getParameter("type");
        String comment = StringUtils.isEmpty(request.getParameter("comment"))?null:request.getParameter("comment");
        String status = StringUtils.isEmpty(request.getParameter("status"))?"1":request.getParameter("status");
        try {
            LmBasicConfig lmBasicConfig = lmBasicConfigService.findById(id);
            lmBasicConfig.setComment(comment);
            lmBasicConfig.setName(name);
            lmBasicConfig.setStatus(status);
            lmBasicConfig.setType(type);
            lmBasicConfigService.updateService(lmBasicConfig);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }
    @RequestMapping("changestatus")
    @ResponseBody
    public JsonResult changestatus(HttpServletRequest request,Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        id = id.replaceAll(",","");
        try {
            LmBasicConfig lmBasicConfig = lmBasicConfigService.findById(id);
            if(lmBasicConfig.ACTIVE.equals(lmBasicConfig.getStatus())){
                lmBasicConfig.setStatus(lmBasicConfig.INACTIVE);
            }else{
                lmBasicConfig.setStatus(lmBasicConfig.ACTIVE);
            }
            lmBasicConfigService.updateService(lmBasicConfig);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }


}
