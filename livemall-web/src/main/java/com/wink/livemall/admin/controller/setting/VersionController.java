package com.wink.livemall.admin.controller.setting;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.util.Auth;
import com.wink.livemall.admin.exception.CustomException;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.PageUtil;
import com.wink.livemall.admin.util.QiniuUploadUtil;
import com.wink.livemall.sys.dict.dto.LmSysDict;
import com.wink.livemall.sys.setting.dto.Configs;
import com.wink.livemall.sys.setting.dto.Version;
import com.wink.livemall.sys.setting.service.ConfigsService;
import com.wink.livemall.sys.setting.service.VersionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 安卓版本管理
 */
@Controller
@RequestMapping("version")
public class VersionController {
    private Logger LOG= LoggerFactory.getLogger(VersionController.class);

    @Autowired
    private VersionService versionService;
    @Autowired
    private ConfigsService configsService;

    @RequestMapping("query")
    public ModelAndView query(Model model,HttpServletRequest request){
        String page = StringUtils.isEmpty(request.getParameter("page"))?"1":request.getParameter("page");
        String pagesize = StringUtils.isEmpty(request.getParameter("pagesize"))?"20":request.getParameter("pagesize");
        List<Version> versionList = versionService.findByList();
        model.addAttribute("page",page);
        model.addAttribute("pagesize",pagesize);
        model.addAttribute("totalsize",versionList.size());
        model.addAttribute("returninfo", PageUtil.startPage(versionList,Integer.parseInt(page),Integer.parseInt(pagesize)));
        model.addAttribute("totalpage",versionList.size()/Integer.parseInt(pagesize)+1);
        return new ModelAndView("setting/versionlist");
    }

    @RequestMapping("addpage")
    public ModelAndView addpage(Model model){
        return new ModelAndView("setting/versionaddpage");
    }

    @RequestMapping("editpage")
    public ModelAndView editpage(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        Version version = versionService.findById(Integer.parseInt(id));
        model.addAttribute("version",version);
        return new ModelAndView("setting/versioneditpage");
    }


    @RequestMapping("add")
    @ResponseBody
    public JsonResult add(HttpServletRequest request, Model model){
        String url = StringUtils.isEmpty(request.getParameter("url"))?null:request.getParameter("url");
        String versionname = StringUtils.isEmpty(request.getParameter("version"))?null:request.getParameter("version");

        try {
            Version version = new Version();
            version.setUrl(url);
            version.setVersion(versionname);
            version.setStatus(0);
            versionService.insertService(version);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
            return new JsonResult(e);
        }

        return new JsonResult();
    }

    @RequestMapping("edit")
    @ResponseBody
    public JsonResult edit(HttpServletRequest request,Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        id = id.replaceAll(",","");
        String url = StringUtils.isEmpty(request.getParameter("url"))?null:request.getParameter("url");
        String versionname = StringUtils.isEmpty(request.getParameter("version"))?null:request.getParameter("version");
        String status = StringUtils.isEmpty(request.getParameter("status"))?null:request.getParameter("status");

        try {
            Version version = versionService.findById(Integer.parseInt(id));
            version.setUrl(url);
            version.setVersion(versionname);
            if("1".equals(status)){
                List<Version> versionList = versionService.findByList();
                for(Version other:versionList){
                    other.setStatus(0);
                    versionService.updateService(other);
                }
            }
            version.setStatus(Integer.parseInt(status));
            versionService.updateService(version);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
            return new JsonResult(e);
        }
        return new JsonResult();
    }

    @RequestMapping("changestatus")
    @ResponseBody
    public JsonResult changestatus(HttpServletRequest request,Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        id = id.replaceAll(",","");
        try {
            List<Version> versionList = versionService.findByList();
            Version version = versionService.findById(Integer.parseInt(id));
            if(version.getStatus()==1){
                version.setStatus(0);
            }else{
                for(Version other:versionList){
                    other.setStatus(0);
                    versionService.updateService(other);
                }
                version.setStatus(1);
            }
            versionService.updateService(version);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
            return new JsonResult(e);
        }
        return new JsonResult();
    }


    @RequestMapping("delete")
    @ResponseBody
    public JsonResult delete(HttpServletRequest request,Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        id = id.replaceAll(",","");
        try {
            versionService.deleteService(Integer.parseInt(id));
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
        id = id.replaceAll(",","");
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
                    String newname = fileOrigName;
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
                        String orgname=file.getOriginalFilename();// 文件原名称
                        Auth auth = Auth.create(configmap.get("accesskey")+"", configmap.get("secretkey")+"");
                        QiniuUploadUtil qiniuUploadUtil = new QiniuUploadUtil(configmap.get("url")+"", configmap.get("bucket")+"", auth);
                        String videourl = qiniuUploadUtil.uploadFile("/version/", file);
                        filepath = videourl+"?attname=orgname.apk";
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
