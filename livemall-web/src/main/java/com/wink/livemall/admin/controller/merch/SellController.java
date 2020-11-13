package com.wink.livemall.admin.controller.merch;

import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.PageUtil;
import com.wink.livemall.merch.dto.LmSellCate;
import com.wink.livemall.merch.service.LmSellCateService;
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
import java.util.List;

/**
 * 商品材质
 */
@Controller
@RequestMapping("sell")
public class SellController {

    @Autowired
    private LmSellCateService lmSellCateService;

    private Logger LOG=LoggerFactory.getLogger(SellController.class);
    

    @RequestMapping("/list")
    public ModelAndView list(HttpServletRequest request, Model model){
        String page = StringUtils.isEmpty(request.getParameter("page"))?"1":request.getParameter("page");
        String pagesize = StringUtils.isEmpty(request.getParameter("pagesize"))?"20":request.getParameter("pagesize");
        model.addAttribute("page",page);
        model.addAttribute("pagesize",pagesize);
        List<LmSellCate> returnlist =  lmSellCateService.findlist();
        model.addAttribute("totalsize",returnlist.size());
        model.addAttribute("returninfo", PageUtil.startPage(returnlist,Integer.parseInt(page),Integer.parseInt(pagesize)));
        model.addAttribute("totalpage",returnlist.size()/Integer.parseInt(pagesize)+1);
        return new ModelAndView("/merch/catelist");
    }



    @RequestMapping("addpage")
    public ModelAndView categoryaddpage(HttpServletRequest request, Model model){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("merch/cateaddpage");
        return modelAndView;
    }

    /**
     * 修改分类页面
     * @return
     */
    @RequestMapping("editpage")
    public ModelAndView categoryeditpage(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?"0":request.getParameter("id");
        LmSellCate sellCate = lmSellCateService.findById(Integer.parseInt(id));
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("merch/cateeditpage");
        model.addAttribute("sellCate",sellCate);

        return modelAndView;
    }


    @RequestMapping("insertthis")
    @ResponseBody
    public JsonResult addgoodcategory(HttpServletRequest request){
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        try {
            if(name==null){
                return  new JsonResult(JsonResult.ERROR,"参数异常");
            }
            LmSellCate sellCate = new LmSellCate();
            sellCate.setName(name);
            sellCate.setStatus(1);
            lmSellCateService.insertServuce(sellCate);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }


    @RequestMapping("deletethis")
    @ResponseBody
    public JsonResult deletegoodcategory(HttpServletRequest request){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        if(id!=null){
            try {
                lmSellCateService.deletethis(Integer.parseInt(id));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return new JsonResult(e);
            }
        }else{
            return new JsonResult(JsonResult.ERROR,"参数异常");
        }
        return new JsonResult();
    }

    @RequestMapping("editthis")
    @ResponseBody
    public JsonResult editgoodcategory(HttpServletRequest request){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        try {
            if(id==null){
                return  new JsonResult(JsonResult.ERROR,"参数异常");
            }
            LmSellCate sellCate =lmSellCateService.findById(Integer.parseInt(id));
            sellCate.setName(name);
            lmSellCateService.updateService(sellCate);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }

}
