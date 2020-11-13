package com.wink.livemall.admin.controller.video;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.util.Auth;
import com.wink.livemall.admin.exception.CustomException;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.PageUtil;
import com.wink.livemall.admin.util.QiniuUploadUtil;
import com.wink.livemall.goods.dto.Good;
import com.wink.livemall.goods.service.GoodService;
import com.wink.livemall.member.dto.LmMember;
import com.wink.livemall.member.service.LmMemberService;
import com.wink.livemall.sys.setting.dto.Configs;
import com.wink.livemall.sys.setting.service.ConfigsService;
import com.wink.livemall.video.dto.LmVideoCategoary;
import com.wink.livemall.video.dto.LmVideoCore;
import com.wink.livemall.video.service.LmVideoCategoaryService;
import com.wink.livemall.video.service.LmVideoCoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("video")
public class VideoController {

    @Autowired
    private LmVideoCategoaryService lmVideoCategoaryService;
    @Autowired
    private LmVideoCoreService lmVideoCoreService;
    @Autowired
    private GoodService goodService;
    @Autowired
    private ConfigsService configsService;
    @Autowired
    private LmMemberService lmMemberService;
    /**
     * 视频分类列表
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("categoryquery")
    public ModelAndView categoryquery(HttpServletRequest request, Model model){
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String pid = StringUtils.isEmpty(request.getParameter("pid"))?"0":request.getParameter("pid");
        String page = StringUtils.isEmpty(request.getParameter("page"))?"1":request.getParameter("page");
        String pagesize = StringUtils.isEmpty(request.getParameter("pagesize"))?"20":request.getParameter("pagesize");
        Map<String,String> condient = new HashMap<>();
        condient.put("name",name);
        condient.put("pid",pid);
        model.addAttribute("name",name);
        model.addAttribute("page",page);
        model.addAttribute("pagesize",pagesize);
        List<Map> returnlist =  lmVideoCategoaryService.findByCondient(condient);
        model.addAttribute("totalsize",returnlist.size());
        model.addAttribute("returninfo", PageUtil.startPage(returnlist,Integer.parseInt(page),Integer.parseInt(pagesize)));
        model.addAttribute("totalpage",returnlist.size()/Integer.parseInt(pagesize)+1);
        return new ModelAndView("/video/categorylist");
    }

    /**
     * 添加分类页面
     * @return
     */
    @RequestMapping("categoryaddpage")
    public ModelAndView categoryaddpage(HttpServletRequest request, Model model){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("video/categoryaddpage");
        model.addAttribute("topcategory",lmVideoCategoaryService.findtopcategory());
        return modelAndView;
    }

    /**
     * 修改分类页面
     * @return
     */
    @RequestMapping("categoryeditpage")
    public ModelAndView categoryeditpage(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        LmVideoCategoary lmVideoCategoary = lmVideoCategoaryService.findById(id);
        List<Map> list = new ArrayList<>();
        List<LmVideoCategoary> lmVideoCategoaryList = lmVideoCategoaryService.findtopcategory();
        for(LmVideoCategoary lmVideoCategoary1:lmVideoCategoaryList){
            Map map = new HashMap();
            map.put("id",lmVideoCategoary1.getId());
            map.put("name",lmVideoCategoary1.getName());
            if(lmVideoCategoary.getPid() ==lmVideoCategoary1.getId()){
                map.put("selected","selected");
            }else{
                map.put("selected","");
            }
            list.add(map);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("video/categoryeditpage");
        model.addAttribute("lmVideoCategoary",lmVideoCategoary);
        model.addAttribute("lmVideoCategoaryList",list);

        return modelAndView;
    }

    @RequestMapping("query")
    public ModelAndView query(HttpServletRequest request, Model model){
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String page = StringUtils.isEmpty(request.getParameter("page"))?"1":request.getParameter("page");
        String pagesize = StringUtils.isEmpty(request.getParameter("pagesize"))?"20":request.getParameter("pagesize");
        Map<String,String> condient = new HashMap<>();
        condient.put("name",name);
        model.addAttribute("name",name);
        model.addAttribute("page",page);
        model.addAttribute("pagesize",pagesize);
        List<Map> returnlist =  lmVideoCoreService.findByCondient(condient);
        List<Good> goodList =goodService.findAll();
        for(Map map:returnlist){
            String goodids = (String) map.get("goodids");
            String goodname = "";
            if(goodids.contains(",")){
                String [] ids = goodids.split(",");
                for(String id:ids){
                    for(Good good:goodList){
                        if(id.equals(good.getId()+"")){
                            goodname += good.getTitle()+",";
                            break;
                        }
                    }
                }
                goodname = goodname.substring(0,goodname.length()-2);
            }else{
                for(Good good:goodList){
                   if(goodids.equals(good.getId()+"")){
                       goodname += good.getTitle();
                       break;
                   }
                }
            }
            map.put("goodname",goodname);
        }
        model.addAttribute("totalsize",returnlist.size());
        model.addAttribute("returninfo", PageUtil.startPage(returnlist,Integer.parseInt(page),Integer.parseInt(pagesize)));
        model.addAttribute("totalpage",returnlist.size()/Integer.parseInt(pagesize)+1);
        return new ModelAndView("/video/videolist");
    }

    /**
     * 添加分类页面
     * @return
     */
    @RequestMapping("addpage")
    public ModelAndView addpage(HttpServletRequest request, Model model){
        List<LmVideoCategoary> lmVideoCategoaryList = lmVideoCategoaryService.findAll();
        List<Map> goods = goodService.findListByCondient(new HashMap<>());

        model.addAttribute("goods",goods);
        model.addAttribute("category",lmVideoCategoaryList);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("video/addpage");
        return modelAndView;
    }

    /**
     * 修改页面
     * @return
     */
    @RequestMapping("editpage")
    public ModelAndView editpage(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        LmVideoCore lmVideoCore = lmVideoCoreService.findById(id);
        List<LmVideoCategoary> lmVideoCategoaryList = lmVideoCategoaryService.findAll();
        List<Map> goodlist = new ArrayList<>();
        String goodids = lmVideoCore.getGood_id();
        if(goodids.contains(",")){
            String [] ids = goodids.split(",");
            for(String goodid:ids){
                Good good = goodService.findById(Integer.parseInt(goodid));
                Map map = new HashMap();
                map.put("name",good.getTitle());
                map.put("id",good.getId());
                goodlist.add(map);
            }
        }else{
            Good good = goodService.findById(Integer.parseInt(goodids));
            Map map = new HashMap();
            if(good!=null){
                map.put("name",good.getTitle());
                map.put("id",good.getId());
            }else{
                map.put("name","");
                map.put("id","");
            }
            goodlist.add(map);
        }
        model.addAttribute("goodlist",goodlist);
        List<Map> CategoaryList = new ArrayList<>();
        for(LmVideoCategoary lmVideoCategoary:lmVideoCategoaryList){
            Map map = new HashMap();
            map.put("id",lmVideoCategoary.getId());
            map.put("name",lmVideoCategoary.getName());
            if(lmVideoCategoary.getId()==lmVideoCore.getCategory()){
                map.put("selected","true");
            }else{
                map.put("selected","false");
            }
            CategoaryList.add(map);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("video/editpage");
        model.addAttribute("lmVideoCore",lmVideoCore);
        model.addAttribute("CategoaryList",CategoaryList);
        return modelAndView;
    }



    @RequestMapping("insert")
    @ResponseBody
    public JsonResult insert(HttpServletRequest request){
        String img = StringUtils.isEmpty(request.getParameter("img"))?"":request.getParameter("img");
        String video = StringUtils.isEmpty(request.getParameter("video"))?"":request.getParameter("video");
        String name = StringUtils.isEmpty(request.getParameter("name"))?"":request.getParameter("name");
        String content = StringUtils.isEmpty(request.getParameter("content"))?"":request.getParameter("content");
        String tag = StringUtils.isEmpty(request.getParameter("tag"))?"":request.getParameter("tag");
        String category = StringUtils.isEmpty(request.getParameter("category"))?"0":request.getParameter("category");
        String good_id = StringUtils.isEmpty(request.getParameter("good_id"))?"":request.getParameter("good_id");
        try {
            if(name==null||img==null||video==null||content==null||tag ==null||category==null||good_id==null){
                return  new JsonResult(JsonResult.ERROR,"参数异常");
            }
            //查询机器人用户是否存在
            LmMember root = lmMemberService.findById("0");
            LmVideoCore lmVideoCore = new LmVideoCore();
            lmVideoCore.setCategory(Integer.parseInt(category));
            lmVideoCore.setContent(content);
            lmVideoCore.setName(name);
            lmVideoCore.setVideo(video);
            lmVideoCore.setImg(img);
            lmVideoCore.setCreattime(new Date());
            lmVideoCore.setTag(tag);
            lmVideoCore.setGood_id(good_id);
            if(root!=null){
                lmVideoCore.setCreatuserid(root.getId());
            }
            lmVideoCoreService.addService(lmVideoCore);
        } catch (Exception e) {
            e.printStackTrace();
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
    public JsonResult delete(HttpServletRequest request){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        if(id!=null){
            try {
                lmVideoCoreService.deleteService(Integer.parseInt(id));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return new JsonResult(e);
            }
        }else{
            return new JsonResult(JsonResult.ERROR,"参数异常");
        }
        return new JsonResult();
    }

    @RequestMapping("edit")
    @ResponseBody
    public JsonResult edit(HttpServletRequest request){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        String img = StringUtils.isEmpty(request.getParameter("img"))?"":request.getParameter("img");
        String video = StringUtils.isEmpty(request.getParameter("video"))?"":request.getParameter("video");
        String name = StringUtils.isEmpty(request.getParameter("name"))?"":request.getParameter("name");
        String content = StringUtils.isEmpty(request.getParameter("content"))?"":request.getParameter("content");
        String tag = StringUtils.isEmpty(request.getParameter("tag"))?"":request.getParameter("tag");
        String category = StringUtils.isEmpty(request.getParameter("category"))?"0":request.getParameter("category");
        String good_id = StringUtils.isEmpty(request.getParameter("good_id"))?"":request.getParameter("good_id");
        try {
            if(id==null){
                return  new JsonResult(JsonResult.ERROR,"参数异常");
            }
            LmVideoCore lmVideoCore = lmVideoCoreService.findById(id);
            if(!StringUtils.isEmpty(img)){
                lmVideoCore.setImg(img);
            }
            if(!StringUtils.isEmpty(category)){
                lmVideoCore.setCategory(Integer.parseInt(category));
            }
            if(!StringUtils.isEmpty(good_id)){
                lmVideoCore.setGood_id(good_id);
            }
            if(!StringUtils.isEmpty(video)){
                lmVideoCore.setVideo(video);
            }
            if(!StringUtils.isEmpty(content)){
                lmVideoCore.setContent(content);
            }
            if(!StringUtils.isEmpty(tag)){
                lmVideoCore.setTag(tag);
            }
            if(!StringUtils.isEmpty(name)){
                lmVideoCore.setName(name);
            }
            lmVideoCoreService.updateService(lmVideoCore);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult(e);
        }
        return new JsonResult();
    }





    @RequestMapping("categoryinsert")
    @ResponseBody
    public JsonResult addgoodcategory(HttpServletRequest request){
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String orderno = StringUtils.isEmpty(request.getParameter("orderno"))?"0":request.getParameter("orderno");
        String status = StringUtils.isEmpty(request.getParameter("status"))?"0":request.getParameter("status");
        String pid = StringUtils.isEmpty(request.getParameter("pid"))?"0":request.getParameter("pid");

        try {
            if(name==null||orderno==null||status==null){
                return  new JsonResult(JsonResult.ERROR,"参数异常");
            }
            LmVideoCategoary lmVideoCategoary = new LmVideoCategoary();
            lmVideoCategoary.setPid(Integer.parseInt(pid));
            lmVideoCategoary.setStatus(Integer.parseInt(status));
            lmVideoCategoary.setName(name);
            lmVideoCategoary.setOrderno(Integer.parseInt(orderno));
            lmVideoCategoaryService.addService(lmVideoCategoary);
        } catch (Exception e) {
            e.printStackTrace();
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
                lmVideoCategoaryService.deleteService(Integer.parseInt(id));
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
        String pid = StringUtils.isEmpty(request.getParameter("pid"))?"0":request.getParameter("pid");

        try {
            if(id==null){
                return  new JsonResult(JsonResult.ERROR,"参数异常");
            }
            LmVideoCategoary lmVideoCategoary =lmVideoCategoaryService.findById(id);
            if(!StringUtils.isEmpty(status)){
                lmVideoCategoary.setStatus(Integer.parseInt(status));
            }
            if(!StringUtils.isEmpty(orderno)){
                lmVideoCategoary.setOrderno(Integer.parseInt(orderno));
            }
            if(!StringUtils.isEmpty(pid)){
                lmVideoCategoary.setPid(Integer.parseInt(pid));
            }
            if(!StringUtils.isEmpty(name)){
                lmVideoCategoary.setName(name);
            }
            lmVideoCategoaryService.updateService(lmVideoCategoary);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult(e);
        }
        return new JsonResult();
    }


    /**
     * 上传文件
     * @param file
     * @param request
     * @return
     * @throws IllegalStateException
     * @throws IOException
     */
    @RequestMapping("/uploadfile")
    @ResponseBody
    public Map<String,String> uploadfile(@RequestParam("file") MultipartFile file, HttpServletRequest request)
            throws IllegalStateException, IOException {
        String id = StringUtils.isEmpty(request.getParameter("id"))?"-1":request.getParameter("id");
        Map<String,String> map=new HashMap<String,String>();
        map.put("code", "0");
        map.put("msg","上传成功");
        String filepath ="";
        if(file!=null){
            try {
                Configs configs = configsService.findByTypeId(Configs.type_upload);
                if(configs!=null){
                    String config = configs.getConfig();
                    Map configmap =  JSONObject.parseObject(config);
                    String uploadtype = configmap.get("type_value")+"";
                    //本地上传
                    String fileOrigName=file.getOriginalFilename();// 文件原名称
                    if (!fileOrigName.contains(".")) {
                        throw new IllegalArgumentException("缺少后缀名");
                    }
                    String fileDirPath = configmap.get("localurl")+"";
                    String newname = new Date().getTime()+fileOrigName;
                    if("1".equals(uploadtype)){
                        File fileDir = new File(fileDirPath);
                        if(!fileDir.exists()){
                            // 递归生成文件夹
                            fileDir.mkdirs();
                        }
                        // 构建真实的文件路径
                        File newFile = new File(fileDir.getAbsolutePath() + File.separator +newname );
                        file.transferTo(newFile);
                        filepath = newFile.getAbsolutePath();
                    }else{
                        //七牛上传
                        Auth auth = Auth.create(configmap.get("accesskey")+"", configmap.get("secretkey")+"");
                        QiniuUploadUtil qiniuUploadUtil = new QiniuUploadUtil(configmap.get("url")+"", configmap.get("bucket")+"", auth);
                        String videourl = qiniuUploadUtil.uploadFile("/video/", file);
                        filepath = videourl;
                    }
                    map.put("videourl",filepath);
                }else{
                    throw new CustomException("配置异常");
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } finally {

            }
        }
        return map;
    }

}
