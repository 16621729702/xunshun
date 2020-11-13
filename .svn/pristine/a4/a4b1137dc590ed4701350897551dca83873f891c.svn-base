package com.wink.livemall.admin.controller.image;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.util.Auth;
import com.wink.livemall.admin.controller.ExcelController.ExportController;
import com.wink.livemall.admin.exception.CustomException;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.PageUtil;
import com.wink.livemall.admin.util.QiniuUploadUtil;
import com.wink.livemall.sys.image.dto.LmImageCore;
import com.wink.livemall.sys.image.dto.LmImageGroup;
import com.wink.livemall.sys.image.service.LmImageCoreService;
import com.wink.livemall.sys.image.service.LmImageGroupService;
import com.wink.livemall.sys.setting.dto.Configs;
import com.wink.livemall.sys.setting.service.ConfigsService;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;

@RequestMapping("image")
@Controller
public class ImageController {

    private Logger LOG= LoggerFactory.getLogger(ImageController.class);
    @Autowired
    private LmImageGroupService lmImageGroupService;
    @Autowired
    private LmImageCoreService lmImageCoreService;
    @Autowired
    private ConfigsService configsService;

    /**
     * 选择图片首页
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("query")
    public ModelAndView query(HttpServletRequest request, Model model){
        String classname = StringUtils.isEmpty(request.getParameter("classname"))?"":request.getParameter("classname");
        String page = StringUtils.isEmpty(request.getParameter("page"))?"1":request.getParameter("page");
        String groupid = StringUtils.isEmpty(request.getParameter("groupid"))?"":request.getParameter("groupid");
        String startdate = StringUtils.isEmpty(request.getParameter("startdate"))?"":request.getParameter("startdate");
        String enddate = StringUtils.isEmpty(request.getParameter("enddate"))?"":request.getParameter("enddate");
        Map<String,Object> condient = new HashedMap();

            condient.put("groupid",groupid);
            if(!StringUtils.isEmpty(startdate)){
                condient.put("startdate",startdate);
            }
            if(!StringUtils.isEmpty(enddate)){
                condient.put("enddate",enddate);
            }

        model.addAttribute("classname",classname);
            model.addAttribute("groupid",groupid);
        List<LmImageCore> lmImageCoreList = lmImageCoreService.findByCondient(condient);
        List<LmImageGroup> lmImageGroupList = lmImageGroupService.findAll();
        List<Map> grouplist = new ArrayList<>();
        if(StringUtils.isEmpty(groupid)){
            model.addAttribute("checkall","true");
        }else{
            model.addAttribute("checkall","false");
        }
        for(LmImageGroup lmImageGroup:lmImageGroupList){
            Map map = new HashMap();
            map.put("id",lmImageGroup.getId());
            map.put("name",lmImageGroup.getName());
            if(groupid.equals(lmImageGroup.getId()+"")){
                map.put("selected","true");
            }else{
                map.put("selected","false");
            }
            grouplist.add(map);
        }
        model.addAttribute("lmImageCoreList", PageUtil.startPage(lmImageCoreList, Integer.parseInt(page), 10));
        model.addAttribute("lmImageGroupList",grouplist);
        model.addAttribute("totalpage", lmImageCoreList.size() / 10 + 1);
        return new ModelAndView("image/selectimage");
    }

    /**
     * 删除图片
     * @param request
     * @return
     */
    @RequestMapping("deleteimg")
    @ResponseBody
    public JsonResult deleteimg(HttpServletRequest request){
        String ids = StringUtils.isEmpty(request.getParameter("ids"))?"":request.getParameter("ids");
        try {
            if(!StringUtils.isEmpty(ids)){
                String [] idarray = ids.split(",");
                for(String id:idarray){
                    lmImageCoreService.deletebyPK(Integer.parseInt(id));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
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

        if(file!=null){
            try {
                Configs configs = configsService.findByTypeId(Configs.type_upload);
                if(configs!=null){
                    String config = configs.getConfig();
                    Map configmap =  JSONObject.parseObject(config);
                    String uploadtype = configmap.get("type_value")+"";
                    //本地上传
                    boolean image = isImage(file.getInputStream());
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
                        LmImageCore lmImageCore = new LmImageCore();
                        lmImageCore.setAttachment(newFile.getAbsolutePath());
                        lmImageCore.setCreatetime(new Date());
                        lmImageCore.setFilename(newname);
                        lmImageCore.setGroup_id(Integer.parseInt(id));
                        lmImageCore.setType(1);
                        lmImageCore.setUploadtype(1);
                        lmImageCore.setModule_upload_dir("");
                        lmImageCore.setImgurl("");
                        lmImageCoreService.insertService(lmImageCore);
                    }else{
                        //七牛上传
                        Auth auth = Auth.create(configmap.get("accesskey")+"", configmap.get("secretkey")+"");
                        QiniuUploadUtil qiniuUploadUtil = new QiniuUploadUtil(configmap.get("url")+"", configmap.get("bucket")+"", auth);
                        String imgurl = qiniuUploadUtil.uploadFile("/filePath/", file);
                        LmImageCore lmImageCore = new LmImageCore();
                        lmImageCore.setAttachment("");
                        lmImageCore.setCreatetime(new Date());
                        lmImageCore.setFilename(newname);
                        lmImageCore.setGroup_id(Integer.parseInt(id));
                        lmImageCore.setType(1);
                        lmImageCore.setUploadtype(2);
                        lmImageCore.setImgurl(imgurl);
                        lmImageCore.setModule_upload_dir("");
                        lmImageCoreService.insertService(lmImageCore);
                    }
                }else{
                    throw new CustomException("配置异常");
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                LOG.error(e.getMessage());
            } catch (IOException e) {
                LOG.error(e.getMessage());
                e.printStackTrace();
            } catch (IllegalStateException e) {
                LOG.error(e.getMessage());
                e.printStackTrace();
            } finally {

            }
        }
        return map;
    }


    /**
     * 本地IO流读取图片 by:long
     * @return
     */
    @RequestMapping(value = "/IoReadImage/{imgname}", method = RequestMethod.GET)
    public String IoReadImage(@PathVariable String imgname, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletOutputStream out = null;
        FileInputStream ips = null;
        Configs configs = configsService.findByTypeId(Configs.type_upload);
        if(configs!=null){
            String config = configs.getConfig();
            Map configmap =  JSONObject.parseObject(config);
            try {
                //获取图片存放路径
                ips = new FileInputStream(new File(configmap.get("localurl")+"\\"+imgname));
                response.setContentType("multipart/form-data");
                out = response.getOutputStream();
                //读取文件流
                int len = 0;
                byte[] buffer = new byte[1024 * 10];
                while ((len = ips.read(buffer)) != -1){
                    out.write(buffer,0,len);
                }
                out.flush();
            }catch (Exception e){
                LOG.error(e.getMessage());
                e.printStackTrace();
            }finally {
                out.close();
                ips.close();
            }
        }
        return null;
    }

    /**
     * 添加分组
     * @param request
     * @return
     */
    @RequestMapping("addgroup")
    @ResponseBody
    public JsonResult addgroup(HttpServletRequest request){
        try {
            LmImageGroup lmImageGroup = new LmImageGroup();
            lmImageGroup.setName("未命名");
            lmImageGroup.setType(1);
            lmImageGroupService.insertService(lmImageGroup);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }


    /**
     * 修改分组信息
     * @param request
     * @return
     */
    @RequestMapping("editgroup")
    @ResponseBody
    public JsonResult editgroup(HttpServletRequest request){
        String id = StringUtils.isEmpty(request.getParameter("id"))?"":request.getParameter("id");
        String name = StringUtils.isEmpty(request.getParameter("name"))?"":request.getParameter("name");

        try {
            LmImageGroup lmImageGroup = lmImageGroupService.findByPk(id);
            lmImageGroup.setName(name);
            lmImageGroup.setType(1);
            lmImageGroupService.updateService(lmImageGroup);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
            return new JsonResult(e);
        }
        return new JsonResult();
    }


    /**
     * 删除分组信息
     * @param request
     * @return
     */
    @RequestMapping("deletegroup")
    @ResponseBody
    public JsonResult deletegroup(HttpServletRequest request){
        String id = StringUtils.isEmpty(request.getParameter("id"))?"":request.getParameter("id");
        try {
            if(!StringUtils.isEmpty(id)){
                lmImageGroupService.deleteByPK(Integer.parseInt(id));
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }


    public boolean isImage(InputStream inputStream) {
        if (inputStream == null) {
            return false;
        }
        Image img;
        try {
            img = ImageIO.read(inputStream);
            return !(img == null || img.getWidth(null) <= 0 || img.getHeight(null) <= 0);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
