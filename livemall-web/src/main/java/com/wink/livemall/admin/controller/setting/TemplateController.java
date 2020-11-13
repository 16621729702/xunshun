package com.wink.livemall.admin.controller.setting;

import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.PageUtil;
import com.wink.livemall.sys.setting.dto.Tag;
import com.wink.livemall.sys.setting.dto.Templates;
import com.wink.livemall.sys.setting.service.TagService;
import com.wink.livemall.sys.setting.service.TemplateService;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设置-专题页管理
 */
@Controller
@RequestMapping("template")
public class TemplateController {
    private Logger LOG= LoggerFactory.getLogger(TemplateController.class);

    @Autowired
    private TemplateService templateService;

    /**
     * 列表页面
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("query")
    public ModelAndView query(HttpServletRequest request, Model model){
        String status = StringUtils.isEmpty(request.getParameter("status"))?"":request.getParameter("status");
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String page = StringUtils.isEmpty(request.getParameter("page"))?"1":request.getParameter("page");
        String pagesize = StringUtils.isEmpty(request.getParameter("pagesize"))?"20":request.getParameter("pagesize");
        model.addAttribute("name",name);
        model.addAttribute("status",status);
        model.addAttribute("page",page);
        model.addAttribute("pagesize",pagesize);
        Map<String,String> condient = new HashMap<>(16);
        condient.put("name",name);
        condient.put("status",status);
        List<Templates> templates = templateService.findByCondient(condient);
        //商品信息
        model.addAttribute("returninfo", PageUtil.startPage(templates,Integer.parseInt(page),Integer.parseInt(pagesize)));
        //总页数
        model.addAttribute("totalpage",templates.size()/Integer.parseInt(pagesize)+1);
        model.addAttribute("totalsize",templates.size());
        return new ModelAndView("setting/templatelist");
    }

    /**
     * 添加页面
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("addpage")
    public ModelAndView addpage(HttpServletRequest request, Model model){
        return new ModelAndView("setting/templateaddpage");
    }

    /**
     * 编辑页面
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("editpage")
    public ModelAndView editpage(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        Templates templates = templateService.findById(id);
        model.addAttribute("templates",templates);
        return new ModelAndView("setting/templateeditpage");
    }


    /**
     * 删除快递公司
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public JsonResult delete(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        try {
            templateService.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }


    /**
     * 添加快递公司
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public JsonResult add(HttpServletRequest request, Model model){
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String tempkey = StringUtils.isEmpty(request.getParameter("tempkey"))?null:request.getParameter("tempkey");
        String tempid = StringUtils.isEmpty(request.getParameter("tempid"))?null:request.getParameter("tempid");
        String content = StringUtils.isEmpty(request.getParameter("content"))?null:request.getParameter("content");
        String status = StringUtils.isEmpty(request.getParameter("status"))?"0":request.getParameter("status");
        try {
           Templates templates = new Templates();
            templates.setCreated_at(new Date());
            templates.setUpdated_at(new Date());
            templates.setContent(content);
            templates.setName(name);
            templates.setTempid(tempid);
            templates.setTempkey(tempkey);
            templates.setStatus(Integer.parseInt(status));
            templateService.addService(templates);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
            return new JsonResult(e);
        }
        return new JsonResult();
    }


    /**
     * 编辑快递公司
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("edit")
    @ResponseBody
    public JsonResult edit(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String tempkey = StringUtils.isEmpty(request.getParameter("tempkey"))?null:request.getParameter("tempkey");
        String tempid = StringUtils.isEmpty(request.getParameter("tempid"))?null:request.getParameter("tempid");
        String content = StringUtils.isEmpty(request.getParameter("content"))?null:request.getParameter("content");
        String status = StringUtils.isEmpty(request.getParameter("status"))?"0":request.getParameter("status");

        try {
            Templates templates = templateService.findById(id);
            if(!StringUtils.isEmpty(name)){
                templates.setName(name);
            }
            if(!StringUtils.isEmpty(tempkey)){
                templates.setTempkey(tempkey);
            }
            if(!StringUtils.isEmpty(tempid)){
                templates.setTempid(tempid);
            }
            if(!StringUtils.isEmpty(status)){
                templates.setStatus(Integer.parseInt(status));
            }
            if(!StringUtils.isEmpty(content)){
                templates.setContent(content);
            }
            templates.setUpdated_at(new Date());
            templateService.updateService(templates);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }

    /**
     * 修改状态
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("changestatus")
    @ResponseBody
    public JsonResult changestatus(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        try {
            Templates templates = templateService.findById(id);
            if(templates.active==templates.getStatus()){
                templates.setStatus(Tag.inactive);
            }else{
                templates.setStatus(Tag.active);
            }
            templateService.updateService(templates);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
            return new JsonResult(e);
        }
        return new JsonResult();
    }
}
