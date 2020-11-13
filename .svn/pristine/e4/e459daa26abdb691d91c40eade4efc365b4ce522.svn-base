package com.wink.livemall.admin.controller.live;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.pili.PiliException;
import com.qiniu.pili.Stream;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.PageUtil;
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
import com.wink.livemall.video.dto.LmVideoCategoary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
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
 * 直播管理
 */
@Controller
@RequestMapping("live")
public class LiveController {

    @Autowired
    private LmLiveCategoryService lmLiveCategoryService;
    @Autowired
    private LmLiveService lmLiveService;
    @Autowired
    private LmLiveGoodService lmLiveGoodService;
    @Autowired
    private GoodService goodService;
    @Autowired
    private ConfigsService configsService;
    private Logger LOG=LoggerFactory.getLogger(LiveController.class);
    
    /**
     * 直播分类查询
     * @return
     */
    @RequestMapping("/categoryquery")
    public ModelAndView categoryquery(HttpServletRequest request, Model model){
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String page = StringUtils.isEmpty(request.getParameter("page"))?"1":request.getParameter("page");
        String pagesize = StringUtils.isEmpty(request.getParameter("pagesize"))?"20":request.getParameter("pagesize");
        Map<String,String> condient = new HashMap<>();
        condient.put("name",name);
        model.addAttribute("name",name);
        model.addAttribute("page",page);
        model.addAttribute("pagesize",pagesize);
        List<LmLiveCategory> returnlist =  lmLiveCategoryService.findByCondient(condient);
        model.addAttribute("totalsize",returnlist.size());
        model.addAttribute("returninfo", PageUtil.startPage(returnlist,Integer.parseInt(page),Integer.parseInt(pagesize)));
        model.addAttribute("totalpage",returnlist.size()/Integer.parseInt(pagesize)+1);
        return new ModelAndView("/live/categorylist");
    }


    /**
     * 添加分类页面
     * @return
     */
    @RequestMapping("categoryaddpage")
    public ModelAndView categoryaddpage(HttpServletRequest request, Model model){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("live/categoryaddpage");
        return modelAndView;
    }

    /**
     * 修改分类页面
     * @return
     */
    @RequestMapping("categoryeditpage")
    public ModelAndView categoryeditpage(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        LmLiveCategory lmLiveCategory = lmLiveCategoryService.findbyId(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("live/categoryeditpage");
        model.addAttribute("lmLiveCategory",lmLiveCategory);

        return modelAndView;
    }


    @RequestMapping("categoryinsert")
    @ResponseBody
    public JsonResult addgoodcategory(HttpServletRequest request){
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String orderno = StringUtils.isEmpty(request.getParameter("orderno"))?"0":request.getParameter("orderno");
        String status = StringUtils.isEmpty(request.getParameter("status"))?"0":request.getParameter("status");
        try {
            if(name==null||orderno==null||status==null){
                return  new JsonResult(JsonResult.ERROR,"参数异常");
            }
            LmLiveCategory lmLiveCategory = new LmLiveCategory();
            lmLiveCategory.setPid(0);
            lmLiveCategory.setStatus(Integer.parseInt(status));
            lmLiveCategory.setName(name);
            lmLiveCategory.setOrderno(Integer.parseInt(orderno));
            lmLiveCategoryService.insertService(lmLiveCategory);
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
    @RequestMapping("categorydelete")
    @ResponseBody
    public JsonResult deletegoodcategory(HttpServletRequest request){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        if(id!=null){
            try {
                lmLiveCategoryService.deleteService(Integer.parseInt(id));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return new JsonResult(e);
            }
        }else{
            return new JsonResult(JsonResult.ERROR,"参数异常");
        }
        return new JsonResult();
    }

    @RequestMapping("categoryedit")
    @ResponseBody
    public JsonResult editgoodcategory(HttpServletRequest request){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String orderno = StringUtils.isEmpty(request.getParameter("orderno"))?"0":request.getParameter("orderno");
        String status = StringUtils.isEmpty(request.getParameter("status"))?null:request.getParameter("status");

        try {
            if(id==null){
                return  new JsonResult(JsonResult.ERROR,"参数异常");
            }
            LmLiveCategory lmLiveCategory =lmLiveCategoryService.findbyId(id);
            if(!StringUtils.isEmpty(status)){
                lmLiveCategory.setStatus(Integer.parseInt(status));
            }
            if(!StringUtils.isEmpty(orderno)){
                lmLiveCategory.setOrderno(Integer.parseInt(orderno));
            }
            if(!StringUtils.isEmpty(name)){
                lmLiveCategory.setName(name);
            }
            lmLiveCategoryService.updateService(lmLiveCategory);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }
    /**
     * 直播间管理
     * @return
     */
    @RequestMapping("query")
    public ModelAndView query(HttpServletRequest request, Model model){
        String merchid = StringUtils.isEmpty(request.getParameter("merchid"))?null:request.getParameter("merchid");
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String page = StringUtils.isEmpty(request.getParameter("page"))?"1":request.getParameter("page");
        String pagesize = StringUtils.isEmpty(request.getParameter("pagesize"))?"20":request.getParameter("pagesize");
        Map<String,String> condient = new HashMap<>();
        condient.put("name",name);
        condient.put("merchid",merchid);
        model.addAttribute("merchid",merchid);
        model.addAttribute("name",name);
        model.addAttribute("page",page);
        model.addAttribute("pagesize",pagesize);
        List<Map<String,Object>> returnlist =  lmLiveService.findByCondient(condient);
        model.addAttribute("totalsize",returnlist.size());
        model.addAttribute("returninfo", PageUtil.startPage(returnlist,Integer.parseInt(page),Integer.parseInt(pagesize)));
        model.addAttribute("totalpage",returnlist.size()/Integer.parseInt(pagesize)+1);
        return new ModelAndView("live/query");
    }
    /**
     * 直播间管理
     * @return
     */
    @RequestMapping("merchquery")
    public ModelAndView merchquery(HttpServletRequest request, Model model){
        String merchid = StringUtils.isEmpty(request.getParameter("merchid"))?null:request.getParameter("merchid");
        String page = StringUtils.isEmpty(request.getParameter("page"))?"1":request.getParameter("page");
        String pagesize = StringUtils.isEmpty(request.getParameter("pagesize"))?"20":request.getParameter("pagesize");
        Map<String,String> condient = new HashMap<>();
        condient.put("merchid",merchid);
        model.addAttribute("merchid",merchid);
        model.addAttribute("page",page);
        model.addAttribute("pagesize",pagesize);
        List<Map<String,Object>> returnlist =  lmLiveService.findByCondient(condient);
        model.addAttribute("totalsize",returnlist.size());
        model.addAttribute("returninfo", PageUtil.startPage(returnlist,Integer.parseInt(page),Integer.parseInt(pagesize)));
        model.addAttribute("totalpage",returnlist.size()/Integer.parseInt(pagesize)+1);
        return new ModelAndView("merch/merchlivelist");
    }


    @RequestMapping("liveinsert")
    @ResponseBody
    public JsonResult liveinsert(HttpServletRequest request){
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String isstart = StringUtils.isEmpty(request.getParameter("isstart"))?"0":request.getParameter("isstart");
        String status = StringUtils.isEmpty(request.getParameter("status"))?"0":request.getParameter("status");
        String isrecommend = StringUtils.isEmpty(request.getParameter("isrecommend"))?"0":request.getParameter("isrecommend");
        String img = StringUtils.isEmpty(request.getParameter("img"))?"0":request.getParameter("img");
        String watchnum = StringUtils.isEmpty(request.getParameter("watchnum"))?"0":request.getParameter("watchnum");
        String merch_id = StringUtils.isEmpty(request.getParameter("merch_id"))?"0":request.getParameter("merch_id");
        String categoryid = StringUtils.isEmpty(request.getParameter("categoryid"))?"0":request.getParameter("categoryid");
        String livegroupid = StringUtils.isEmpty(request.getParameter("livegroupid"))?"0":request.getParameter("livegroupid");

        try {
            if(isstart==null||isrecommend==null||img==null||watchnum==null||merch_id==null||categoryid==null||name==null||isstart==null||status==null){
                return  new JsonResult(JsonResult.ERROR,"参数异常");
            }
            LmLive lmLive = new LmLive();
            lmLive.setIsstart(Integer.parseInt(isstart));
            lmLive.setName(name);
            lmLive.setImg(img);
            lmLive.setLivegroupid(livegroupid);
            lmLive.setWatchnum(Integer.parseInt(watchnum));
            lmLive.setMerch_id(Integer.parseInt(merch_id));
            lmLive.setCategoryid(Integer.parseInt(categoryid));
            lmLive.setIsrecommend(Integer.parseInt(isrecommend));
            lmLive.setStatus(Integer.parseInt(status));
            lmLiveService.insertService(lmLive);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult(merch_id);
    }

    /**
     * 删除分类
     * id 商品分类id
     * @param request
     * @return
     */
    @RequestMapping("livedelete")
    @ResponseBody
    public JsonResult livedelete(HttpServletRequest request){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        if(id!=null){
            try {
                lmLiveService.deleteService(Integer.parseInt(id));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return new JsonResult(e);
            }
        }else{
            return new JsonResult(JsonResult.ERROR,"参数异常");
        }
        return new JsonResult();
    }

    @RequestMapping("liveedit")
    @ResponseBody
    public JsonResult liveedit(HttpServletRequest request){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String isstart = StringUtils.isEmpty(request.getParameter("isstart"))?"":request.getParameter("isstart");
        String status = StringUtils.isEmpty(request.getParameter("status"))?"":request.getParameter("status");
        String isrecommend = StringUtils.isEmpty(request.getParameter("isrecommend"))?"":request.getParameter("isrecommend");
        String img = StringUtils.isEmpty(request.getParameter("img"))?"":request.getParameter("img");
        String watchnum = StringUtils.isEmpty(request.getParameter("watchnum"))?"":request.getParameter("watchnum");
        String merch_id = StringUtils.isEmpty(request.getParameter("merch_id"))?"":request.getParameter("merch_id");
        String categoryid = StringUtils.isEmpty(request.getParameter("categoryid"))?"":request.getParameter("categoryid");
        String livegroupid = StringUtils.isEmpty(request.getParameter("livegroupid"))?"":request.getParameter("livegroupid");
        try {
            if(id==null){
                return  new JsonResult(JsonResult.ERROR,"参数异常");
            }
            LmLive lmLive =lmLiveService.findbyId(id);
            if(!StringUtils.isEmpty(status)){
                lmLive.setStatus(Integer.parseInt(status));
            }
            if(!StringUtils.isEmpty(isstart)){
                lmLive.setIsstart(Integer.parseInt(isstart));
            }
            if(!StringUtils.isEmpty(merch_id)){
                lmLive.setMerch_id(Integer.parseInt(merch_id));
            }
            if(!StringUtils.isEmpty(categoryid)){
                lmLive.setCategoryid(Integer.parseInt(categoryid));
            }
            if(!StringUtils.isEmpty(watchnum)){
                lmLive.setWatchnum(Integer.parseInt(watchnum));
            }
            if(!StringUtils.isEmpty(isrecommend)){
                lmLive.setIsrecommend(Integer.parseInt(isrecommend));
            }
            if(!StringUtils.isEmpty(img)){
                lmLive.setImg(img);
            }
            if(!StringUtils.isEmpty(livegroupid)){
                lmLive.setLivegroupid(livegroupid);
            }
            if(!StringUtils.isEmpty(name)){
                lmLive.setName(name);
            }
            lmLiveService.updateService(lmLive);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }




    /**
     * 添加分类页面
     * @return
     */
    @RequestMapping("addpage")
    public ModelAndView addpage(HttpServletRequest request, Model model){
        String merchid = StringUtils.isEmpty(request.getParameter("merchid"))?null:request.getParameter("merchid");
        List<LmLiveCategory> list = lmLiveCategoryService.findActiveList();
        model.addAttribute("categorylist",list);
        model.addAttribute("merchid",merchid);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("live/addpage");
        return modelAndView;
    }

    /**
     * 修改分类页面
     * @return
     */
    @RequestMapping("editpage")
    public ModelAndView editpage(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        LmLive lmLive = lmLiveService.findbyId(id);
        List<LmLiveCategory> list = lmLiveCategoryService.findActiveList();
        List<Map> CategoaryList = new ArrayList<>();
        for(LmLiveCategory lmLiveCategory:list){
            Map map = new HashMap();
            map.put("id",lmLiveCategory.getId());
            map.put("name",lmLiveCategory.getName());
            if(lmLiveCategory.getId()==lmLive.getCategoryid()){
                map.put("selected","true");
            }else{
                map.put("selected","false");
            }
            CategoaryList.add(map);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("live/editpage");
        model.addAttribute("categorylist",CategoaryList);
        model.addAttribute("lmLive",lmLive);
        return modelAndView;
    }

    /**
     * 查看直播
     * @return
     */
    @RequestMapping("livevideo")
    public ModelAndView livevideo(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        LmLive lmLive = lmLiveService.findbyId(id);
        Configs configs = configsService.findByTypeId(Configs.type_flow);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("live/livevideo");
      if(configs!=null){
          String config = configs.getConfig();
          Map configmap = JSONObject.parseObject(config);
          String access_key = configmap.get("access_key_id")+"";
          String access_secret = configmap.get("access_secret")+"";
          String livepushurl = configmap.get("pushurl")+"";
          String key = configmap.get("key")+"";
          String livereadurl = configmap.get("readurl")+"";
          String livereadurl2 = configmap.get("readurl2")+"";
          String livereadurl3 = configmap.get("readurl3")+"";
          String streamKeyPrefix = lmLive.getId()+"";
          String hubname = configmap.get("hubname")+"";
          QiniuUtil qiniuUtil = new QiniuUtil(access_key,access_secret,streamKeyPrefix,livepushurl,livereadurl,livereadurl2,livereadurl3,hubname);
          String readurl = qiniuUtil.getreadurl(hubname);
          //添加防盗链sign
          String t = Long.toHexString(System.currentTimeMillis()/1000+5 * 60).toLowerCase();
          String s = key+"/"+hubname+"/"+streamKeyPrefix+t;
          String sgin = md5(s).toLowerCase();
          readurl = readurl+"?sign="+sgin+"&t="+t;
          System.out.println(readurl);

          model.addAttribute("replayurl",readurl);
          String readurlreadurl = qiniuUtil.getreadurl3(hubname);
          //添加防盗链sign
          String ss = key+"/"+hubname+"/"+streamKeyPrefix+".flv"+t;
          String sginsgin = md5(ss).toLowerCase();
          readurlreadurl = readurlreadurl+"?sign="+sginsgin+"&t="+t;
          System.out.println(readurlreadurl);
      }
        return modelAndView;
    }



    /**
     * 管理页面首页
     * @return
     */
    @RequestMapping("managerlist")
    public ModelAndView managerlist(HttpServletRequest request, Model model){
        String liveid = StringUtils.isEmpty(request.getParameter("liveid"))?null:request.getParameter("liveid");
        model.addAttribute("liveid",liveid);
        return new ModelAndView("live/setting");
    }


    /**
     * 直播间商品管理
     * @return
     */
    @RequestMapping("goodlist")
    public ModelAndView goodlist(HttpServletRequest request,Model model){
        String liveid = StringUtils.isEmpty(request.getParameter("liveid"))?null:request.getParameter("liveid");
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String pagesize = StringUtils.isEmpty(request.getParameter("pagesize"))?"10":request.getParameter("pagesize");
        String page = StringUtils.isEmpty(request.getParameter("page"))?"1":request.getParameter("page");
        Map<String,String> condient = new HashMap<>();
        condient.put("name",name);
        condient.put("liveid",liveid);
        LmLive lmLive = lmLiveService.findbyId(liveid);
        List<Map<String,Object>> list = lmLiveGoodService.findInfoByLiveidAndName(condient);
        model.addAttribute("list",list);
        model.addAttribute("liveid",liveid);
        model.addAttribute("merchid",lmLive.getMerch_id());
        model.addAttribute("name",name);
        model.addAttribute("pagesize",pagesize);
        model.addAttribute("page",page);
        model.addAttribute("totalsize",list.size());
        model.addAttribute("returninfo", PageUtil.startPage(list,Integer.parseInt(page),Integer.parseInt(pagesize)));
        model.addAttribute("totalpage",list.size()/Integer.parseInt(pagesize)+1);
        return new ModelAndView("live/goodlist");
    }

    /**
     * 商品查询页面
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("selectgood")
    public ModelAndView selectgood(HttpServletRequest request, Model model){
        String liveid = StringUtils.isEmpty(request.getParameter("liveid"))?null:request.getParameter("liveid");
        String merchid = StringUtils.isEmpty(request.getParameter("merchid"))?null:request.getParameter("merchid");
        String title = StringUtils.isEmpty(request.getParameter("title"))?null:request.getParameter("title");
        String page = StringUtils.isEmpty(request.getParameter("page"))?"1":request.getParameter("page");
        String pagesize = StringUtils.isEmpty(request.getParameter("pagesize"))?"20":request.getParameter("pagesize");
        Map<String,String> condient = new HashMap<>(16);
        condient.put("title",title);
        condient.put("page",page);
        condient.put("merchid",merchid);
        condient.put("pagesize",pagesize);
        condient.put("liveid",liveid);
        List<LmLiveGood> liveGoods = lmLiveGoodService.findByLiveid(liveid);
        List<Map> list = goodService.findListByCondient(condient);
        for(Map map:list){
           if(liveGoods!=null&&liveGoods.size()>0){
               for(LmLiveGood lmLiveGood:liveGoods){
                   if((int)map.get("id")==lmLiveGood.getGood_id()){
                       map.put("islivegood",1);
                       break;
                   }else{
                       map.put("islivegood",0);
                   }
               }
           }else{
               map.put("islivegood",0);
           }
        }
        //参数
        model.addAttribute("condient",condient);
        //总数
        model.addAttribute("totalsize",list.size());
        //商品信息
        model.addAttribute("returninfo", PageUtil.startPage(list,Integer.parseInt(page),Integer.parseInt(pagesize)));
        //总页数
        model.addAttribute("totalpage",list.size()/Integer.parseInt(pagesize)+1);
        return new ModelAndView("/live/selectgoods");
    }


    @RequestMapping("changelivegood")
    @ResponseBody
    public JsonResult changelivegood(HttpServletRequest request){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        String liveid = StringUtils.isEmpty(request.getParameter("liveid"))?null:request.getParameter("liveid");
        try {
            if(id==null){
                return  new JsonResult(JsonResult.ERROR,"参数异常");
            }
           List<LmLiveGood> list = lmLiveGoodService.findByLiveid(liveid);
            boolean flag = true;
            for(LmLiveGood lmLiveGood:list){
                if(lmLiveGood.getGood_id()==Integer.parseInt(id)){
                    flag = false;
                    lmLiveGoodService.delGood(lmLiveGood.getId());
                    break;
                }
            }
            if(flag){
                LmLiveGood lmLiveGood = new LmLiveGood();
                lmLiveGood.setGood_id(Integer.parseInt(id));
                lmLiveGood.setLiveid(Integer.parseInt(liveid));
                lmLiveGoodService.addGood(lmLiveGood);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }

    /**
     * 直播间回访记录
     * @return
     */
    @RequestMapping("playback")
    public ModelAndView playback(HttpServletRequest request,Model model)   {
        String liveid = StringUtils.isEmpty(request.getParameter("liveid"))?null:request.getParameter("liveid");
        Configs configs = configsService.findByTypeId(Configs.type_flow);
        try {
            if(configs!=null) {
                LmLive lmLive = lmLiveService.findbyId(liveid);
                if(lmLive!=null) {
                    String config = configs.getConfig();
                    Map configmap = JSONObject.parseObject(config);
                    String access_key = configmap.get("access_key_id")+"";
                    String access_secret = configmap.get("access_secret")+"";
                    String livepushurl = configmap.get("pushurl")+"";
                    String key = configmap.get("key")+"";
                    String livereadurl = configmap.get("readurl")+"";
                    String livereadurl2 = configmap.get("readurl2")+"";
                    String livereadurl3 = configmap.get("readurl3")+"";
                    String streamKeyPrefix = lmLive.getId()+"";
                    String hubname = configmap.get("hubname")+"";
                    QiniuUtil qiniuUtil = new QiniuUtil(access_key,access_secret,streamKeyPrefix,livepushurl,livereadurl,livereadurl2,livereadurl3,hubname);
                    Stream stream = qiniuUtil.getStream(lmLive.getName());
                    Stream.Record[] records= stream.historyRecord(0,0);
                    System.out.println(records);
                }
            }
        } catch (PiliException e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
        }
        return new ModelAndView("live/playback");
    }
}
