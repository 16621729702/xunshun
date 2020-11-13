package com.wink.livemall.admin.controller.setting;

import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.PageUtil;
import com.wink.livemall.merch.dto.LmMerchInfo;
import com.wink.livemall.merch.service.LmMerchInfoService;
import com.wink.livemall.sys.setting.dto.Lideshow;
import com.wink.livemall.sys.setting.service.LideshowService;
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
 * 轮播图
 */
@Controller
@RequestMapping("lideshow")
public class LideshowController {
    private Logger LOG= LoggerFactory.getLogger(LideshowController.class);

    @Autowired
    private LideshowService lideshowService;

    @Autowired
    private LmMerchInfoService lmMerchInfoService;

    @RequestMapping("query")
    public ModelAndView query(HttpServletRequest request, Model model){
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String type = StringUtils.isEmpty(request.getParameter("type"))?"":request.getParameter("type");
        String page = StringUtils.isEmpty(request.getParameter("page"))?"1":request.getParameter("page");
        String pagesize = StringUtils.isEmpty(request.getParameter("pagesize"))?"20":request.getParameter("pagesize");
        model.addAttribute("name",name);
        model.addAttribute("type",type);
        model.addAttribute("page",page);
        model.addAttribute("pagesize",pagesize);
        Map<String,String> condient = new HashMap<>();
        condient.put("name",name);
        condient.put("type",type);
        List<Lideshow> lideshowList = lideshowService.findByCondient(condient);
        model.addAttribute("returninfo", PageUtil.startPage(lideshowList,Integer.parseInt(page),Integer.parseInt(pagesize)));
        //总页数
        model.addAttribute("totalpage",lideshowList.size()/Integer.parseInt(pagesize)+1);
        model.addAttribute("totalsize",lideshowList.size());

        return new ModelAndView("setting/lideshowlist");
    }

    @RequestMapping("addpage")
    public ModelAndView addpage(HttpServletRequest request, Model model){
        //查询所有active商户
        List<LmMerchInfo> list = lmMerchInfoService.findActiveMerch();
        model.addAttribute("merchlist",list);
        return new ModelAndView("setting/lideshowaddpage");
    }

    @RequestMapping("editpage")
    public ModelAndView editpage(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        Lideshow lideshow = lideshowService.findById(id);
        model.addAttribute("lideshow",lideshow);
        List<LmMerchInfo> list = lmMerchInfoService.findActiveMerch();
        model.addAttribute("merchlist",list);
        return new ModelAndView("setting/lideshoweditpage");
    }

    @RequestMapping("insert")
    @ResponseBody
    public JsonResult insert(HttpServletRequest request, Model model){
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String type = StringUtils.isEmpty(request.getParameter("type"))?null:request.getParameter("type");
        String pic = StringUtils.isEmpty(request.getParameter("pic"))?null:request.getParameter("pic");
        String wxappurl = StringUtils.isEmpty(request.getParameter("wxappurl"))?null:request.getParameter("wxappurl");
        String sort = StringUtils.isEmpty(request.getParameter("sort"))?"0":request.getParameter("sort");
        String merchid = StringUtils.isEmpty(request.getParameter("merchid"))?"0":request.getParameter("merchid");
        try {
            Lideshow lideshow = new Lideshow();
            lideshow.setCreated_at(new Date());
            lideshow.setType(Integer.parseInt(type));
            lideshow.setName(name);
            lideshow.setPic(pic);
            lideshow.setMerchid(Integer.parseInt(merchid));
            lideshow.setSort(Integer.parseInt(sort));
            lideshow.setWxappurl(wxappurl);
            lideshow.setUpdated_at(new Date());
            lideshow.setLink_data("");
            lideshow.setLink_type(0);
            lideshowService.insertService(lideshow);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }

    @RequestMapping("edit")
    @ResponseBody
    public JsonResult edit(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String type = StringUtils.isEmpty(request.getParameter("type"))?null:request.getParameter("type");
        String pic = StringUtils.isEmpty(request.getParameter("pic"))?null:request.getParameter("pic");
        String wxappurl = StringUtils.isEmpty(request.getParameter("wxappurl"))?null:request.getParameter("wxappurl");
        String sort = StringUtils.isEmpty(request.getParameter("sort"))?"0":request.getParameter("sort");
        String merchid = StringUtils.isEmpty(request.getParameter("merchid"))?"0":request.getParameter("merchid");

        try {
            Lideshow lideshow = lideshowService.findById(id);
            lideshow.setCreated_at(new Date());
            lideshow.setType(Integer.parseInt(type));
            lideshow.setName(name);
            lideshow.setPic(pic);
            lideshow.setMerchid(Integer.parseInt(merchid));
            lideshow.setSort(Integer.parseInt(sort));
            lideshow.setWxappurl(wxappurl);
            lideshow.setUpdated_at(new Date());
            lideshowService.updateService(lideshow);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }


    @RequestMapping("delete")
    @ResponseBody
    public JsonResult delete(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        try {
            lideshowService.deleteService(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }
}
