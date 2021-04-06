package com.wink.livemall.admin.controller.goods;

import com.wink.livemall.admin.controller.live.LiveController;
import com.wink.livemall.admin.util.DateUtils;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.PageUtil;
import com.wink.livemall.goods.dto.GoodCategory;
import com.wink.livemall.goods.service.GoodCategoryService;
import freemarker.template.utility.DateUtil;
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
import java.util.*;

/**
 * 商品分类
 */
@RequestMapping("goodcategory")
@Controller
public class GoodCategoryController {

    private Logger LOG= LoggerFactory.getLogger(GoodCategoryController.class);

    @Autowired
    private GoodCategoryService goodCategoryService;
    /**
     * 商品分类列表
     * name 商品分类名称
     * pid 商品分类父id
     * @return
     */
    @RequestMapping("query")
    public ModelAndView goodcategory(HttpServletRequest request, Model model){
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String pid = StringUtils.isEmpty(request.getParameter("pid"))?"0":request.getParameter("pid");
        pid = pid.replaceAll(",","");
        String page = StringUtils.isEmpty(request.getParameter("page"))?"1":request.getParameter("page");
        String pagesize = StringUtils.isEmpty(request.getParameter("pagesize"))?"20":request.getParameter("pagesize");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("good/goods_category");
        Map<String,String> condient = new HashMap<>();
        condient.put("pid",pid);
        condient.put("name",name);
        model.addAttribute("name",name);
        model.addAttribute("pid",pid);
        model.addAttribute("page",page);
        model.addAttribute("pagesize",pagesize);
        if("0".equals(pid)){
            model.addAttribute("isshow",true);
        }else{
            model.addAttribute("isshow",false);
        }

        List<Map> returnlist =  goodCategoryService.findGoodCategoryByCondient(condient);
        model.addAttribute("totalsize",returnlist.size());
        model.addAttribute("returninfo",PageUtil.startPage(returnlist,Integer.parseInt(page),Integer.parseInt(pagesize)));
        model.addAttribute("totalpage",returnlist.size()/Integer.parseInt(pagesize)+1);
        return modelAndView;
    }
    /**
     * 添加分类页面
     * @return
     */
    @RequestMapping("addpage")
    public ModelAndView addpage(HttpServletRequest request, Model model){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("good/addgoodscategory");
        model.addAttribute("topcategory",goodCategoryService.findtopgoodscategory());
        return modelAndView;
    }

    /**
     * 修改分类页面
     * @return
     */
    @RequestMapping("editpage")
    public ModelAndView editpage(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        id = id.replaceAll(",","");
        GoodCategory goodCategory = goodCategoryService.findgoodscategoryById(id);
        List<GoodCategory> list = goodCategoryService.findtopgoodscategory();
        List<Map> returnlist = new ArrayList<>();
        for(GoodCategory g:list){
            Map<String,String> map = new HashMap<>();
            if(g.getId()==goodCategory.getParent_id()){
                map.put("isselected","selected");
            }else{
                map.put("isselected","");
            }
            map.put("name",g.getName());
            map.put("isshow",g.getIsshow()+"");
            map.put("sort",g.getSort()+"");
            map.put("id",g.getId()+"");
            map.put("home_pic",g.getHome_pic());
            map.put("pic",g.getPic());
            map.put("merchshow",g.getMerchshow()+"");
            returnlist.add(map);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("good/editgoodscategory");
        model.addAttribute("goodcategory",goodCategory);
        model.addAttribute("topcategory",returnlist);
        return modelAndView;
    }
    /**
     * 添加商品
     * pid 父id
     * name 分类名称
     * pic 分类图标
     * home_pic 首页图标
     * order 排序号
     * status 状态 显示/隐藏
     * @param request
     * @return
     */
    @RequestMapping("insert")
    @ResponseBody
    public JsonResult addgoodcategory(HttpServletRequest request){
        String pid = StringUtils.isEmpty(request.getParameter("pid"))?"0":request.getParameter("pid");
        pid = pid.replaceAll(",","");
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String pic = StringUtils.isEmpty(request.getParameter("pic"))?"":request.getParameter("pic");
        String home_pic = StringUtils.isEmpty(request.getParameter("home_pic"))?"":request.getParameter("home_pic");
        String order = StringUtils.isEmpty(request.getParameter("order"))?"0":request.getParameter("order");
        String status = StringUtils.isEmpty(request.getParameter("status"))?null:request.getParameter("status");
        String merchshow = StringUtils.isEmpty(request.getParameter("merchshow"))?"0":request.getParameter("merchshow");
        try {
            if(name==null||order==null||status==null){
                return  new JsonResult(JsonResult.ERROR,"参数异常");
            }
            GoodCategory goodCategory = new GoodCategory();
            goodCategory.setIntro("");
            goodCategory.setName(name);
            if(!"0".equals(pid)){
                goodCategory.setMerchshow(Integer.parseInt(merchshow));
                goodCategory.setPic(pic);
                goodCategory.setHome_pic(home_pic);
            }
            goodCategory.setParent_id(Integer.parseInt(pid));
            goodCategory.setUpdated_at(new Date());
            goodCategory.setCreated_at(new Date());
            goodCategoryService.addgoodcategory(goodCategory);
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
    @RequestMapping("delete")
    @ResponseBody
    public JsonResult deletegoodcategory(HttpServletRequest request){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        id = id.replaceAll(",","");
        if(id!=null){
            try {
                goodCategoryService.deleteService(Integer.parseInt(id));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return new JsonResult(e);
            }
        }else{
            return new JsonResult(JsonResult.ERROR,"参数异常");
        }
        return new JsonResult();
    }

    /**
     * 修改商品
     * id 商品分类id
     * pid 父id
     * name 分类名称
     * pic 分类图标
     * home_pic 首页图标
     * order 排序号
     * status 状态 显示/隐藏
     * @param request
     * @return
     */
    @RequestMapping("edit")
    @ResponseBody
    public JsonResult editgoodcategory(HttpServletRequest request){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        String pid = StringUtils.isEmpty(request.getParameter("pid"))?null:request.getParameter("pid");
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String pic = StringUtils.isEmpty(request.getParameter("pic"))?null:request.getParameter("pic");
        String home_pic = StringUtils.isEmpty(request.getParameter("home_pic"))?null:request.getParameter("home_pic");
        String order = StringUtils.isEmpty(request.getParameter("order"))?null:request.getParameter("order");
        String status = StringUtils.isEmpty(request.getParameter("status"))?null:request.getParameter("status");
        String merchshow = StringUtils.isEmpty(request.getParameter("merchshow"))?null:request.getParameter("merchshow");
        String isrecommend = StringUtils.isEmpty(request.getParameter("isrecommend"))?"0":request.getParameter("isrecommend");
        try {
            if(id==null){
                return  new JsonResult(JsonResult.ERROR,"参数异常");
            }
            GoodCategory goodCategory =goodCategoryService.findgoodscategoryById(id);
            if(!StringUtils.isEmpty(home_pic)){
                goodCategory.setHome_pic(home_pic);
            }
            if(!StringUtils.isEmpty(name)){
                goodCategory.setName(name);
            }
            if(!StringUtils.isEmpty(merchshow)){
                goodCategory.setMerchshow(Integer.parseInt(merchshow));
            }
            if(!StringUtils.isEmpty(pid)){
                goodCategory.setParent_id(Integer.parseInt(pid));
            }
            if(!StringUtils.isEmpty(pic)){
                goodCategory.setPic(pic);
            }
            if(!StringUtils.isEmpty(order)){
                goodCategory.setSort(Integer.parseInt(order));
            }
            if(!StringUtils.isEmpty(status)){
                goodCategory.setIsshow(Integer.parseInt(status));
            }
            if(!StringUtils.isEmpty(isrecommend)){
                goodCategory.setIsrecommend(Integer.parseInt(isrecommend));
            }
            goodCategory.setUpdated_at(new Date());
            goodCategoryService.editgoodcategory(goodCategory);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }

}
