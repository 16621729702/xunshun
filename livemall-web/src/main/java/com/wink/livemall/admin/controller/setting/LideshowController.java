package com.wink.livemall.admin.controller.setting;

import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.PageUtil;
import com.wink.livemall.goods.dto.Good;
import com.wink.livemall.goods.service.GoodService;
import com.wink.livemall.live.dto.LmLive;
import com.wink.livemall.live.service.LmLiveService;
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
import java.util.*;

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

    @Autowired
    private LmLiveService lmLiveService;

    @Autowired
    private GoodService goodService;

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
        list(model);
        return new ModelAndView("setting/lideshowaddpage");
    }

    @RequestMapping("editpage")
    public ModelAndView editpage(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        Lideshow lideshow = lideshowService.findById(id);
        list(model);
        model.addAttribute("lideshow",lideshow);
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
        String link_type = StringUtils.isEmpty(request.getParameter("link_type"))?"0":request.getParameter("link_type");
        String mer_category = StringUtils.isEmpty(request.getParameter("mer_category"))?"0":request.getParameter("mer_category");
        String live_category = StringUtils.isEmpty(request.getParameter("live_category"))?"0":request.getParameter("live_category");
        try {
            Lideshow lideshow = new Lideshow();
            lideshow.setCreated_at(new Date());
            lideshow.setType(Integer.parseInt(type));
            lideshow.setName(name);
            lideshow.setPic(pic);
            lideshow.setLink_type(Integer.parseInt(link_type));
            lideshow.setMerchid(Integer.parseInt(merchid));
            lideshow.setSort(Integer.parseInt(sort));
            lideshow.setWxappurl(wxappurl);
            lideshow.setUpdated_at(new Date());
            lideshow.setLink_data("");
            lideshow.setLink_type(0);
            lideshow.setMer_category(Integer.parseInt(mer_category));
            lideshow.setLive_category(Integer.parseInt(live_category));
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
        id = id.replaceAll(",","");
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String type = StringUtils.isEmpty(request.getParameter("type"))?null:request.getParameter("type");
        String pic = StringUtils.isEmpty(request.getParameter("pic"))?null:request.getParameter("pic");
        String wxappurl = StringUtils.isEmpty(request.getParameter("wxappurl"))?null:request.getParameter("wxappurl");
        String sort = StringUtils.isEmpty(request.getParameter("sort"))?"0":request.getParameter("sort");
        String merchid = StringUtils.isEmpty(request.getParameter("merchid"))?"0":request.getParameter("merchid");
        String link_type = StringUtils.isEmpty(request.getParameter("link_type"))?"0":request.getParameter("link_type");
        String mer_category = StringUtils.isEmpty(request.getParameter("mer_category"))?"0":request.getParameter("mer_category");
        String live_category = StringUtils.isEmpty(request.getParameter("live_category"))?"0":request.getParameter("live_category");
        try {
            Lideshow lideshow = lideshowService.findById(id);
            lideshow.setCreated_at(new Date());
            lideshow.setType(Integer.parseInt(type));
            lideshow.setName(name);
            lideshow.setPic(pic);
            lideshow.setLink_type(Integer.parseInt(link_type));
            lideshow.setMerchid(Integer.parseInt(merchid));
            lideshow.setSort(Integer.parseInt(sort));
            lideshow.setWxappurl(wxappurl);
            lideshow.setUpdated_at(new Date());
            lideshow.setMer_category(Integer.parseInt(mer_category));
            lideshow.setLive_category(Integer.parseInt(live_category));
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
        id = id.replaceAll(",","");
        try {
            lideshowService.deleteService(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }



    private void list(Model model){

          List<Map> merchList =new LinkedList<>();
               List<LmMerchInfo> list = lmMerchInfoService.findActiveMerch();

               for(LmMerchInfo lmMerchInfo:list){
                   Map map1 =new HashMap();
                   map1.put("id",lmMerchInfo.getId());
                   map1.put("typeName",lmMerchInfo.getStore_name());
                   if(lmMerchInfo.getCategoryid()==1){
                       map1.put("name","普通店铺");
                   }else  if(lmMerchInfo.getCategoryid()==2){
                       map1.put("name","优选店铺");
                   }else  if(lmMerchInfo.getCategoryid()==3){
                       map1.put("name","直播店铺");
                   }else  if(lmMerchInfo.getCategoryid()==4){
                       map1.put("name","合买店铺");
                   }
                   merchList.add(map1);
               }
        List<Map> goodsList =new LinkedList<>();
               List<Good> goodList = goodService.findGoodList();
               for(Good good :goodList){
                   Map map2 =new HashMap();
                   map2.put("id",good.getId());
                   map2.put("typeName",good.getTitle());
                   if(good.getType()==0){
                       map2.put("name","一口价商品");
                   }else  if(good.getType()==1){
                       map2.put("name","拍卖商品");
                   }
                   goodsList.add(map2);
               }
        List<Map> livesList =new LinkedList<>();
               List<LmLive> liveList = lmLiveService.findLiveList();
               for(LmLive lmLive : liveList){
                   Map map =new HashMap();
                   map.put("id",lmLive.getId());
                   map.put("typeName",lmLive.getName());
                   if(lmLive.getType()==0){
                       map.put("name","普通直播");
                   }else  if(lmLive.getType()==1){
                       map.put("name","合买直播");
                   }
                   livesList.add(map);
               }
        model.addAttribute("merchList",merchList);
        model.addAttribute("goodList",goodsList);
        model.addAttribute("liveList",livesList);
    }

}
