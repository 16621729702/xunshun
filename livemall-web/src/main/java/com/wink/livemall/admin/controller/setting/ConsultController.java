package com.wink.livemall.admin.controller.setting;

import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.sys.consult.dto.Consult;
import com.wink.livemall.sys.consult.service.ConsultService;
import com.wink.livemall.sys.dict.dto.LmSysDict;
import com.wink.livemall.sys.dict.service.DictService;
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
import java.util.List;

@Controller
@RequestMapping("consult")
public class ConsultController {
    private Logger LOG= LoggerFactory.getLogger(ConsultController.class);

    @Autowired
    private ConsultService consultService;

    @RequestMapping("query")
    public ModelAndView query(Model model){
        List<Consult> consults = consultService.findAll();
        model.addAttribute("consults",consults);
        model.addAttribute("totalsize",consults.size());
        return new ModelAndView("setting/consultlist");
    }

    @RequestMapping("addpage")
    public ModelAndView addpage(Model model){
        return new ModelAndView("setting/consultaddpage");
    }

    @RequestMapping("editpage")
    public ModelAndView editpage(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        Consult consult = consultService.findById(id);
        model.addAttribute("consult",consult);
        return new ModelAndView("setting/consulteditpage");
    }


    @RequestMapping("add")
    @ResponseBody
    public JsonResult add(HttpServletRequest request, Model model){
        String title = StringUtils.isEmpty(request.getParameter("title"))?null:request.getParameter("title");
        String content = StringUtils.isEmpty(request.getParameter("content"))?null:request.getParameter("content");
        String status = StringUtils.isEmpty(request.getParameter("status"))?null:request.getParameter("status");
        try {
            Consult consult = new Consult();
            consult.setContent(content);
            consult.setStatus(Integer.parseInt(status));
            consult.setTitle(title);
            consult.setCreatetime(new Date());
            consultService.addService(consult);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }

        return new JsonResult();
    }

    @RequestMapping("edit")
    @ResponseBody
    public JsonResult edit(HttpServletRequest request,Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        id = id.replaceAll(",","");
        String title = StringUtils.isEmpty(request.getParameter("title"))?null:request.getParameter("title");
        String content = StringUtils.isEmpty(request.getParameter("content"))?null:request.getParameter("content");
        String status = StringUtils.isEmpty(request.getParameter("status"))?null:request.getParameter("status");
        try {
            Consult consult = consultService.findById(id);
            consult.setContent(content);
            consult.setStatus(Integer.parseInt(status));
            consult.setTitle(title);
            consultService.updateservice(consult);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }


    @RequestMapping("delete")
    @ResponseBody
    public JsonResult delete(HttpServletRequest request,Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        try {
            consultService.deleteService(id);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }
}
