package com.wink.livemall.admin.controller.setting;

import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.PageUtil;
import com.wink.livemall.order.dao.LmExpressDao;
import com.wink.livemall.order.dto.LmExpress;
import com.wink.livemall.sys.setting.dto.Express;
import com.wink.livemall.sys.setting.service.ExpressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("express")
public class ExpressController {
    private Logger LOG= LoggerFactory.getLogger(ExpressController.class);

    @Autowired
    private ExpressService expressService;
    @Resource
    private LmExpressDao lmExpressDao;
    /**
     * 列表页面
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("query")
    public ModelAndView query(HttpServletRequest request, Model model){
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String page = StringUtils.isEmpty(request.getParameter("page"))?"1":request.getParameter("page");
        String pagesize = StringUtils.isEmpty(request.getParameter("pagesize"))?"20":request.getParameter("pagesize");
        model.addAttribute("name",name);
        model.addAttribute("page",page);
        model.addAttribute("pagesize",pagesize);
        Map<String,String> condient = new HashMap<>(16);
        condient.put("name",name);
        List<LmExpress> expressList = lmExpressDao.findByCondient(condient);
        //商品信息
        model.addAttribute("returninfo", PageUtil.startPage(expressList,Integer.parseInt(page),Integer.parseInt(pagesize)));
        //总页数
        model.addAttribute("totalpage",expressList.size()/Integer.parseInt(pagesize)+1);
        model.addAttribute("totalsize",expressList.size());

        return new ModelAndView("setting/expresslist");
    }

    /**
     * 添加页面
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("addpage")
    public ModelAndView addpage(HttpServletRequest request, Model model){
        return new ModelAndView("setting/expressaddpage");
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
        LmExpress express = lmExpressDao.selectByPrimaryKey(Integer.parseInt(id));
        model.addAttribute("express",express);
        return new ModelAndView("setting/expresseditpage");
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
            lmExpressDao.deleteByPrimaryKey(Integer.parseInt(id));
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
        String express = StringUtils.isEmpty(request.getParameter("express"))?null:request.getParameter("express");
        String status = StringUtils.isEmpty(request.getParameter("status"))?"1":request.getParameter("status");
        try {
           LmExpress lmExpress = new LmExpress();
            lmExpress.setExpress(express);
            lmExpress.setName(name);
            lmExpress.setStatus(Integer.parseInt(status));
            lmExpressDao.insert(lmExpress);
            model.addAttribute("express",express);
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
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String express = StringUtils.isEmpty(request.getParameter("express"))?null:request.getParameter("express");
        String status = StringUtils.isEmpty(request.getParameter("status"))?"1":request.getParameter("status");
        try {
            LmExpress lmExpress = lmExpressDao.selectByPrimaryKey(Integer.parseInt(id));
            lmExpress.setExpress(express);
            lmExpress.setName(name);
            lmExpress.setStatus(Integer.parseInt(status));
            lmExpressDao.updateByPrimaryKey(lmExpress);
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
            LmExpress express = lmExpressDao.selectByPrimaryKey(Integer.parseInt(id));
            if(Express.active==express.getStatus()){
                express.setStatus(Express.inactive);
            }else{
                express.setStatus(Express.active);
            }
            lmExpressDao.updateByPrimaryKey(express);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }

}
