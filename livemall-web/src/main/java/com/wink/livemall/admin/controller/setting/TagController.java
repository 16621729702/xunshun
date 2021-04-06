package com.wink.livemall.admin.controller.setting;

import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.PageUtil;
import com.wink.livemall.sys.setting.dto.Express;
import com.wink.livemall.sys.setting.dto.Tag;
import com.wink.livemall.sys.setting.service.TagService;
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
@RequestMapping("tag")
public class TagController {
    private Logger LOG= LoggerFactory.getLogger(TagController.class);

    @Autowired
    private TagService tagService;

    /**
     * 列表页面
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("query")
    public ModelAndView query(HttpServletRequest request, Model model){
        String type = StringUtils.isEmpty(request.getParameter("type"))?"":request.getParameter("type");
        String style = StringUtils.isEmpty(request.getParameter("style"))?"":request.getParameter("style");
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String page = StringUtils.isEmpty(request.getParameter("page"))?"1":request.getParameter("page");
        String pagesize = StringUtils.isEmpty(request.getParameter("pagesize"))?"20":request.getParameter("pagesize");
        model.addAttribute("name",name);
        model.addAttribute("type",type);
        model.addAttribute("page",page);
        model.addAttribute("style",style);
        model.addAttribute("pagesize",pagesize);
        Map<String,String> condient = new HashMap<>(16);
        condient.put("name",name);
        condient.put("type",type);
        condient.put("style",style);
        List<Tag> tagList = tagService.findByCondient(condient);
        //商品信息
        model.addAttribute("returninfo", PageUtil.startPage(tagList,Integer.parseInt(page),Integer.parseInt(pagesize)));
        //总页数
        model.addAttribute("totalpage",tagList.size()/Integer.parseInt(pagesize)+1);
        model.addAttribute("totalsize",tagList.size());

        return new ModelAndView("setting/taglist");
    }

    /**
     * 添加页面
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("addpage")
    public ModelAndView addpage(HttpServletRequest request, Model model){
        return new ModelAndView("setting/tagaddpage");
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
        id = id.replaceAll(",","");
        Tag tag = tagService.findById(id);
        model.addAttribute("tag",tag);
        return new ModelAndView("setting/tageditpage");
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
        id = id.replaceAll(",","");
        try {
            tagService.deleteById(id);
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
        String type = StringUtils.isEmpty(request.getParameter("type"))?"1":request.getParameter("type");
        String title = StringUtils.isEmpty(request.getParameter("title"))?null:request.getParameter("title");
        String isshow = StringUtils.isEmpty(request.getParameter("isshow"))?"0":request.getParameter("isshow");
        String style = StringUtils.isEmpty(request.getParameter("style"))?"1":request.getParameter("style");
        String sort = StringUtils.isEmpty(request.getParameter("sort"))?"0":request.getParameter("sort");

        try {
            Tag tag = new Tag();
            tag.setIsshow(Integer.parseInt(isshow));
            tag.setCreated_at(new Date());
            tag.setSort(Integer.parseInt(sort));
            tag.setName(name);
            tag.setStyle(Integer.parseInt(style));
            tag.setTitle(title);
            tag.setType(Integer.parseInt(type));
            tag.setUpdated_at(new Date());
            tag.setSort(Integer.parseInt(sort));
            tagService.addService(tag);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
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
        id = id.replaceAll(",","");
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String type = StringUtils.isEmpty(request.getParameter("type"))?"1":request.getParameter("type");
        String title = StringUtils.isEmpty(request.getParameter("title"))?null:request.getParameter("title");
        String isshow = StringUtils.isEmpty(request.getParameter("isshow"))?"0":request.getParameter("isshow");
        String style = StringUtils.isEmpty(request.getParameter("style"))?"1":request.getParameter("style");
        String sort = StringUtils.isEmpty(request.getParameter("sort"))?"0":request.getParameter("sort");

        try {
            Tag tag = tagService.findById(id);
            if(!StringUtils.isEmpty(name)){
                tag.setName(name);
            }
            if(!StringUtils.isEmpty(title)){
                tag.setTitle(title);
            }
            if(!StringUtils.isEmpty(type)){
                tag.setType(Integer.parseInt(type));
            }
            if(!StringUtils.isEmpty(isshow)){
                tag.setIsshow(Integer.parseInt(isshow));
            }
            if(!StringUtils.isEmpty(style)){
                tag.setStyle(Integer.parseInt(style));
            }
            if(!StringUtils.isEmpty(sort)){
                tag.setSort(Integer.parseInt(sort));
            }
            tag.setUpdated_at(new Date());
            tagService.updateService(tag);
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
        id = id.replaceAll(",","");
        try {
            Tag tag = tagService.findById(id);
            if(Tag.active==tag.getIsshow()){
                tag.setIsshow(Tag.inactive);
            }else{
                tag.setIsshow(Tag.active);
            }
            tagService.updateService(tag);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }
}
