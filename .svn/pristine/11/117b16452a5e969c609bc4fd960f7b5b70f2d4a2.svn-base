package com.wink.livemall.admin.controller.goods;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.pili.PiliException;
import com.qiniu.pili.Stream;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.PageUtil;
import com.wink.livemall.goods.dto.LmGoodMaterial;
import com.wink.livemall.goods.service.GoodService;
import com.wink.livemall.live.dto.LmLive;
import com.wink.livemall.live.dto.LmLiveCategory;
import com.wink.livemall.live.dto.LmLiveGood;
import com.wink.livemall.live.service.LmLiveCategoryService;
import com.wink.livemall.live.service.LmLiveGoodService;
import com.wink.livemall.live.service.LmLiveService;
import com.wink.livemall.live.util.QiniuUtil;
import com.wink.livemall.sys.setting.dto.Configs;
import com.wink.livemall.sys.setting.service.ConfigsService;
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

import static com.alibaba.druid.util.Utils.md5;

/**
 * 商品材质
 */
@Controller
@RequestMapping("mater")
public class MaterController {

    @Autowired
    private GoodService goodService;

    private Logger LOG=LoggerFactory.getLogger(MaterController.class);
    

    @RequestMapping("/list")
    public ModelAndView categoryquery(HttpServletRequest request, Model model){
        String page = StringUtils.isEmpty(request.getParameter("page"))?"1":request.getParameter("page");
        String pagesize = StringUtils.isEmpty(request.getParameter("pagesize"))?"20":request.getParameter("pagesize");
        model.addAttribute("page",page);
        model.addAttribute("pagesize",pagesize);
        List<LmGoodMaterial> returnlist =  goodService.findMaterlist();
        model.addAttribute("totalsize",returnlist.size());
        model.addAttribute("returninfo", PageUtil.startPage(returnlist,Integer.parseInt(page),Integer.parseInt(pagesize)));
        model.addAttribute("totalpage",returnlist.size()/Integer.parseInt(pagesize)+1);
        return new ModelAndView("/good/materlist");
    }



    @RequestMapping("addpage")
    public ModelAndView categoryaddpage(HttpServletRequest request, Model model){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("good/materaddpage");
        return modelAndView;
    }

    /**
     * 修改分类页面
     * @return
     */
    @RequestMapping("editpage")
    public ModelAndView categoryeditpage(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?"0":request.getParameter("id");
        LmGoodMaterial material = goodService.findMaterById(Integer.parseInt(id));
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("good/matereditpage");
        model.addAttribute("mater",material);

        return modelAndView;
    }


    @RequestMapping("insertthis")
    @ResponseBody
    public JsonResult addgoodcategory(HttpServletRequest request){
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String orderno = StringUtils.isEmpty(request.getParameter("orderno"))?"0":request.getParameter("orderno");
        String code = StringUtils.isEmpty(request.getParameter("code"))?"0":request.getParameter("code");
        try {
            if(name==null||orderno==null||code==null){
                return  new JsonResult(JsonResult.ERROR,"参数异常");
            }
            LmGoodMaterial material = new LmGoodMaterial();
            material.setCode(Integer.parseInt(code));
            material.setStatus("0");
            material.setName(name);
            material.setOrderno(Integer.parseInt(orderno));
            goodService.insertMater(material);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }

    /**
     * 删除分类
     * id 商品分类id
     * @param request
     * @return
     */
    @RequestMapping("deletethis")
    @ResponseBody
    public JsonResult deletegoodcategory(HttpServletRequest request){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        if(id!=null){
            try {
                goodService.deletemater(Integer.parseInt(id));
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
        String orderno = StringUtils.isEmpty(request.getParameter("orderno"))?"0":request.getParameter("orderno");
        String code = StringUtils.isEmpty(request.getParameter("code"))?"0":request.getParameter("code");
        try {
            if(id==null){
                return  new JsonResult(JsonResult.ERROR,"参数异常");
            }
            LmGoodMaterial material =goodService.findMaterById(Integer.parseInt(id));
            material.setCode(Integer.parseInt(code));
            material.setOrderno(Integer.parseInt(orderno));
            material.setName(name);
            goodService.updatemater(material);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }

}
