package com.wink.livemall.admin.api.comment;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.qiniu.util.Auth;
import com.wink.livemall.admin.api.good.GoodController;
import com.wink.livemall.admin.exception.CustomException;
import com.wink.livemall.admin.util.*;
import com.wink.livemall.admin.util.filterUtils.CheckPicAPI;
import com.wink.livemall.admin.util.filterUtils.CheckTextAPI;
import com.wink.livemall.admin.util.filterUtils.FileAddUtil;
import com.wink.livemall.admin.util.filterUtils.GetAuthService;
import com.wink.livemall.goods.dto.LmGoodAuction;
import com.wink.livemall.goods.service.GoodService;
import com.wink.livemall.goods.service.LmGoodAuctionService;
import com.wink.livemall.goods.utils.HttpJsonResult;
import com.wink.livemall.live.dto.LmLive;
import com.wink.livemall.live.dto.LmLiveInfo;
import com.wink.livemall.live.service.LmLiveInfoService;
import com.wink.livemall.live.service.LmLiveService;
import com.wink.livemall.merch.dto.LmMerchConfigs;
import com.wink.livemall.merch.dto.LmMerchInfo;
import com.wink.livemall.merch.service.LmMerchConfigsService;
import com.wink.livemall.merch.service.LmMerchInfoService;
import com.wink.livemall.order.dto.LmOrder;
import com.wink.livemall.order.dto.LmOrderRefundLog;
import com.wink.livemall.order.service.LmOrderService;
import com.wink.livemall.sys.basic.dto.LmBasicConfig;
import com.wink.livemall.sys.basic.service.LmBasicConfigService;
import com.wink.livemall.sys.image.dto.LmImageCore;
import com.wink.livemall.sys.image.service.LmImageCoreService;
import com.wink.livemall.sys.setting.dto.Configs;
import com.wink.livemall.sys.setting.dto.Lideshow;
import com.wink.livemall.sys.setting.dto.Version;
import com.wink.livemall.sys.setting.dto.VersionIOS;
import com.wink.livemall.sys.setting.service.ConfigsService;
import com.wink.livemall.sys.setting.service.LideshowService;
import com.wink.livemall.sys.setting.service.VersionService;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * ?????????????????? ??? ????????????
 */
@RestController
@Api(tags = "????????????")
public class CommentController {
    Logger logger = LogManager.getLogger(CommentController.class);

    @Autowired
    private LideshowService lideshowService;
    @Autowired
    private ConfigsService configsService;
    @Autowired
    private LmOrderService lmOrderService;
    @Autowired
    private LmImageCoreService lmImageCoreService;
    @Autowired
    private LmMerchInfoService lmMerchInfoService;
    @Autowired
    private LmLiveService lmLiveService;
    @Autowired
    private GoodService goodService;
    @Autowired
    private VersionService versionService;
    @Autowired
    private LmGoodAuctionService lmGoodAuctionService;
    @Autowired
    private LmLiveInfoService lmLiveInfoService;


    /**
     * ???????????????
     * @return
     */
    @ApiOperation(value = "??????????????????")
    @PostMapping("topimg")
    public JsonResult getlideshowlist(HttpServletRequest request, @ApiParam(name = "type", value = "??????3????????????4????????????", required = true)@RequestParam(required = true) String type){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            List<Lideshow> list = lideshowService.findListBytype(Integer.parseInt(type));
            List<Map> returnlist = new ArrayList<>();
            if(list!=null&&list.size()>0){
                for(Lideshow lideshow:list){
                    Map map = new HashMap();
                    map.put("name",lideshow.getName());
                    map.put("pic",lideshow.getPic());
                    map.put("url",lideshow.getWxappurl());
                    returnlist.add(map);
                }
            }
            jsonResult.setData(returnlist.size()>0?returnlist.get(0):null);
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
        }
        return jsonResult;
    }

    /**
     * ????????????????????????
     * @return
     */
    @ApiOperation(value = "????????????????????????")
    @PostMapping("config")
    public JsonResult config(HttpServletRequest request){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
           Configs configs =configsService.findByTypeId(Configs.type_upload);
           Map stringToMap =  JSONObject.parseObject(configs.getConfig());
            jsonResult.setData(stringToMap);
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
        }
        return jsonResult;
    }

    /**
     * ????????????
     * @param file
     * @param request
     * @return
     * @throws IllegalStateException
     * @throws IOException
     */
    @ApiOperation(value = "????????????")
    @PostMapping("/uploadfile")
    public JsonResult uploadfile(@ApiParam(name = "file", value = "??????????????????", required = true)@RequestParam("file") MultipartFile file,
                                 @ApiParam(name = "type", value = "??????head????????? idcard??????feedback??????comment", required = true)@RequestParam("type") String filepath,HttpServletRequest request)
            throws IllegalStateException, IOException {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setMsg("????????????");
        jsonResult.setCode(JsonResult.SUCCESS);
        CheckPicAPI checkPicAPI=new  CheckPicAPI();
        String access_token = GetAuthService.getAuth(checkPicAPI.apiKey,checkPicAPI.secretKey);
        if(file!=null){
            try {

                Configs configs = configsService.findByTypeId(Configs.type_upload);
                if(configs!=null){
                    String config = configs.getConfig();
                    Map configmap =  JSONObject.parseObject(config);
                    String fileOrigName=file.getOriginalFilename();// ???????????????
                    if (!fileOrigName.contains(".")) {
                        throw new IllegalArgumentException("???????????????");
                    }
                    String newname = new Date().getTime()+fileOrigName;
                    {
                        //????????????
                        Auth auth = Auth.create(configmap.get("accesskey")+"", configmap.get("secretkey")+"");
                        QiniuUploadUtil qiniuUploadUtil = new QiniuUploadUtil(configmap.get("url")+"", configmap.get("bucket")+"", auth);
                        String imgurl = qiniuUploadUtil.uploadBigFile("/"+filepath+"/", file);
                       boolean check = checkPicAPI.check(imgurl,access_token);
                        if(!check){
                            jsonResult.setData(imgurl);
                            jsonResult.setCode(JsonResult.ERROR);
                            jsonResult.setMsg("?????????????????????");
                            return jsonResult;
                        }
                        LmImageCore lmImageCore = new LmImageCore();
                        lmImageCore.setAttachment("");
                        lmImageCore.setCreatetime(new Date());
                        lmImageCore.setFilename(newname);
                        lmImageCore.setGroup_id(-1);
                        lmImageCore.setType(1);
                        lmImageCore.setUploadtype(2);
                        lmImageCore.setImgurl(imgurl);
                        lmImageCore.setModule_upload_dir("");
                        lmImageCoreService.insertService(lmImageCore);
                        jsonResult.setData(imgurl);
                    }
                }else{
                    throw new CustomException("????????????");
                }
            } catch (Exception e) {
                jsonResult.setCode(JsonResult.ERROR);
                jsonResult.setMsg(e.getMessage());
                e.printStackTrace();
            }
        }
        return jsonResult;
    }

    /**
     * ????????????
     * @param files
     * @return
     * @throws IllegalStateException
     * @throws IOException
     */
    @ApiOperation(value = "??????????????????")
    @PostMapping(value = "/uploadfiles",headers = "content-type=multipart/form-data")
    public JsonResult uploadfiles(@RequestParam("files") MultipartFile[] files,
                                 @RequestParam("type") String filepath, HttpServletRequest request)
            throws IllegalStateException, IOException {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setMsg("????????????");
        jsonResult.setCode(JsonResult.SUCCESS);
        CheckPicAPI checkPicAPI=new  CheckPicAPI();
        String access_token = GetAuthService.getAuth(checkPicAPI.apiKey,checkPicAPI.secretKey);
        List<String> urllist = new ArrayList<String>();
        if(files!=null){
            try {
                Configs configs = configsService.findByTypeId(Configs.type_upload);
                if(configs!=null){
                    String config = configs.getConfig();
                    Map configmap =  JSONObject.parseObject(config);
                    if(files.length>0){
                        for(MultipartFile file : files){
                            String fileOrigName=file.getOriginalFilename();// ???????????????
                            if (!fileOrigName.contains(".")) {
                                throw new IllegalArgumentException("???????????????");
                            }
                            String newname = new Date().getTime()+fileOrigName;
                            {
                                //????????????
                                Auth auth = Auth.create(configmap.get("accesskey")+"", configmap.get("secretkey")+"");
                                QiniuUploadUtil qiniuUploadUtil = new QiniuUploadUtil(configmap.get("url")+"", configmap.get("bucket")+"", auth);
                                String imgurl = qiniuUploadUtil.uploadFile("/"+filepath+"/", file);
                                boolean check = checkPicAPI.check(imgurl,access_token);
                                    if(!check){
                                        jsonResult.setData(imgurl);
                                        jsonResult.setCode(JsonResult.ERROR);
                                        jsonResult.setMsg("???????????????????????????");
                                        return jsonResult;
                                    }
                                LmImageCore lmImageCore = new LmImageCore();
                                lmImageCore.setAttachment("");
                                lmImageCore.setCreatetime(new Date());
                                lmImageCore.setFilename(newname);
                                lmImageCore.setGroup_id(-1);
                                lmImageCore.setType(1);
                                lmImageCore.setUploadtype(2);
                                lmImageCore.setImgurl(imgurl);
                                lmImageCore.setModule_upload_dir("");
                                lmImageCoreService.insertService(lmImageCore);
                                urllist.add(imgurl);
                            }
                        }
                    }
                    jsonResult.setData(urllist);
                }else{
                    throw new CustomException("????????????");
                }
            } catch (Exception e) {
                e.printStackTrace();
                jsonResult.setCode(JsonResult.ERROR);
                jsonResult.setMsg(e.getMessage());
                e.printStackTrace();
            }
        }
        return jsonResult;
    }
//    /**
//     * ??????????????????-?????????
//     * @return
//     */
//    @ApiOperation(value = "??????????????????-?????????")
//    @PostMapping("express2")
//    public JsonResult express2(HttpServletRequest request,
//                              @ApiParam(name = "id", value = "??????id", required = true)@RequestParam("id") String id){
//        JsonResult jsonResult = new JsonResult();
//        jsonResult.setCode(JsonResult.SUCCESS);
//        try {
//            LmOrder order = lmOrderService.findById(id);
//            String getShipperCode = kuaidiniaoUtil.getOrderTracesByJson(order.getExpresssn());
//            Map<String, String> shippers = (Map<String, String>) JSONObject.parseObject(getShipperCode)
//                    .getJSONArray("Shippers")
//                    .get(0);
//            String ShipperCode = shippers.get("ShipperCode");
//            String result = kuaidiniaoUtil.getOrderTracesByJson(ShipperCode, order.getExpresssn());
//            jsonResult.setData(result);
//        } catch (Exception e) {
//            e.printStackTrace();
//            jsonResult.setMsg(e.getMessage());
//            jsonResult.setCode(JsonResult.ERROR);
//        }
//        return jsonResult;
//    }
//
//    /**
//     * ??????????????????-?????????
//     * @return
//     */
//    @ApiOperation(value = "??????????????????-?????????")
//    @PostMapping("express3")
//    public JsonResult express3(HttpServletRequest request,
//                               @ApiParam(name = "expressid", value = "????????????", required = true)@RequestParam("expressid") String expressid){
//        JsonResult jsonResult = new JsonResult();
//        jsonResult.setCode(JsonResult.SUCCESS);
//        try {
//            String getShipperCode = kuaidiniaoUtil.getOrderTracesByJson(expressid);
//            Map<String, String> shippers = (Map<String, String>) JSONObject.parseObject(getShipperCode)
//                    .getJSONArray("Shippers")
//                    .get(0);
//            String ShipperCode = shippers.get("ShipperCode");
//            String result = kuaidiniaoUtil.getOrderTracesByJson(ShipperCode, expressid);
//            jsonResult.setData(result);
//        } catch (Exception e) {
//            e.printStackTrace();
//            jsonResult.setMsg(e.getMessage());
//            jsonResult.setCode(JsonResult.ERROR);
//        }
//        return jsonResult;
//    }
    /**
     * ??????????????????-??????100
     * @return
     */
    @ApiOperation(value = "??????????????????-??????100")
    @PostMapping("express")
    public JsonResult express(HttpServletRequest request,
                              @ApiParam(name = "id", value = "??????id", required = true)@RequestParam("id") String id,
                              @ApiParam(name = "type", value = "??????/????????????0??????1??????", required = true)@RequestParam("type") String type){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        Map<String,Object> map =new HashMap<>();
        try {
            LmOrder order = lmOrderService.findById(id);
            ExpressUtil expressUtil = new ExpressUtil();
            if(order!=null){
                if("0".equals(type)){
                    String result = expressUtil.synQueryData(order.getExpress(), order.getExpresssn(),"","","",0);
                    map.put("express",order.getExpressname());
                    map.put("results",result);
                    map.put("expresssn",order.getExpresssn());
                    jsonResult.setData(map);
                }else{
                    LmOrderRefundLog lmOrderRefundLog = lmOrderService.findRefundLogById(order.getRefundid());
                    if(lmOrderRefundLog!=null){
                        String result = expressUtil.synQueryData(lmOrderRefundLog.getExpress(), lmOrderRefundLog.getSn(),"","","",0);
                        map.put("express",lmOrderRefundLog.getExpressname());
                        map.put("results",result);
                        map.put("expresssn",lmOrderRefundLog.getSn());
                        jsonResult.setData(map);
                    }
                }
            }
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
        }
        return jsonResult;
    }


    /*
    */
/**
     * ????????????
     * @return
     */

    @ApiOperation(value = "??????????????????")
    @PostMapping("violation")
    public JsonResult directlyinfo(HttpServletRequest request,
                                   @ApiParam(name = "violation", value = "????????????", required = true)@RequestParam("violation") String violation){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        CheckTextAPI checkTextAPI =new CheckTextAPI();
        String access_token = GetAuthService.getAuth(CheckTextAPI.apiKey,CheckTextAPI.secretKey);
        try {
            HttpJsonResult check = checkTextAPI.check(violation,access_token);
            if(check.getCode()!=200){
                jsonResult.setCode(JsonResult.ERROR);
                jsonResult.setMsg(check.getMsg());
                return jsonResult;
            }
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
        }
        return jsonResult;
    }

    /**
     * ????????????imid
     * @return
     */
    @ApiOperation(value = "????????????imid")
    @PostMapping("getimid")
    public JsonResult getimid(HttpServletRequest request){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            //?????????????????????
            LmMerchInfo lmMerchInfo = lmMerchInfoService.findDirectMerch();
            if(lmMerchInfo!=null){
                jsonResult.setData(lmMerchInfo.getMember_id());
            }else{
                jsonResult.setCode(JsonResult.ERROR);
                jsonResult.setMsg("????????????????????????");
            }
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
        }
        return jsonResult;
    }


    /**
     * ??????????????????????????????????????????????????????????????????
     * @param request
     * @return
     */
    @ApiOperation(value = "??????????????????????????????????????????")
    @PostMapping("/search")
    public JsonResult getgoodlist(HttpServletRequest request,
                                  @ApiParam(name = "name", value = "??????????????????",defaultValue = "")@RequestParam(value = "name",required = false) String name,
                                  @ApiParam(name = "type", value = "??????1??????2??????3??????",defaultValue = "") @RequestParam(value = "type",required = false) String type,
                                  @ApiParam(name = "page", value = "??????",defaultValue = "1",required=false) @RequestParam(value = "page",required = false,defaultValue = "1") int page,
                                  @ApiParam(name = "pagesize", value = "????????????",defaultValue = "10",required=false) @RequestParam(value = "pagesize",required = false,defaultValue = "10") int pagesize
    ){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            if(!StringUtils.isEmpty(name)){
                String regEx="[\n`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~???@#???%??????&*????????????+|{}?????????????????????????????? ??????]";
                String aa = " ";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(name);
                name = m.replaceAll(aa).trim();
            }
            if("1".equals(type)){
                List<Map> list = new ArrayList<>();
                //??????????????????
                Map<String,Object> map =lmLiveService.finddirectlyinfoByApi();
                if(map!=null){
                    int liveid=(int)map.get("id");
                    LmLive lmLive = lmLiveService.findbyId(liveid+"");
                    LmLiveInfo lmLiveInfo =lmLiveInfoService.findLiveInfo(liveid);
                    map.put("watchnum",lmLive.getWatchnum()+lmLiveInfo.getWatchnum()+lmLiveInfo.getAddnum());
                    map.put("showtype","live");
                    list.add(map);
                }
                //???????????????????????????????????? ??????????????????
                List<Map> goodlist =  goodService.findInfoByName(name);
                for(Map goodlists:goodlist){
                    Integer id =(int)goodlists.get("goodid");
                    Integer goodtype =(int)goodlists.get("type");
                    Integer types =0;
                    if(1==goodtype) {
                        LmGoodAuction auction = lmGoodAuctionService.findnowPriceByGoodidByApi(id, types);
                        if (!isEmpty(auction)) {
                            goodlists.put("price", auction.getPrice());
                        } else {
                            goodlists.put("price", 0);
                        }
                    }
                    goodlists.put("showtype","good");
                }
                if(goodlist!=null){
                    list.addAll(goodlist);
                }
                jsonResult.setData(PageUtil.startPage(list,page,pagesize));
            }
            if("2".equals(type)){
                List<Map<String,Object>> merchlist = lmMerchInfoService.findMerchInfoByNameByApi(name);
                List<Map<String,Object>> returnlist = new ArrayList<>();
                for(Map<String,Object> map : merchlist){
                    //??????????????????
                    Integer merchid = (int)map.get("id");
                    List<Map<String,Object>> goodlist = goodService.findByMerchIdByApi(merchid);
                    for(Map goodlists:goodlist){
                        int id =(int)goodlists.get("goodid");
                        Integer goodtype =(int)goodlists.get("type");
                        int types =0;
                        if(1==goodtype) {
                            LmGoodAuction lmGoodAuction = lmGoodAuctionService.findnowPriceByGoodidByApi(id, types);
                            if (!isEmpty(lmGoodAuction)) {
                                goodlists.put("goodprice", lmGoodAuction.getPrice());
                            } else {
                                goodlists.put("goodprice", 0);
                            }
                        }
                    }
                    if(goodlist!=null&&goodlist.size()>0){
                        map.put("goodlist",new Gson().toJson(goodlist));
                    }
                    LmLive lmLive = lmLiveService.findByMerchid(merchid);
                    if(lmLive!=null){
                        map.put("live",lmLive);
                    }else {
                        map.put("live",null);
                    }
                    returnlist.add(map);
                }
                jsonResult.setData(PageUtil.startPage(returnlist, page, pagesize));
            }
            if("3".equals(type)){
                List<Map<String,String>> livelist = lmLiveService.findListByNameByApi(name);
                jsonResult.setData(PageUtil.startPage(livelist,page,pagesize));
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }


    /**
     * ????????????????????????
     * @return
     */
    @ApiOperation(value = "????????????????????????")
    @PostMapping("getversion")
    public JsonResult getversion(HttpServletRequest request){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            //?????????????????????
            Version version = versionService.findActive();
            if(version!=null){
                jsonResult.setData(version);
            }else{
                jsonResult.setCode(JsonResult.ERROR);
                jsonResult.setMsg("??????????????????");
            }
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
        }
        return jsonResult;
    }

    /**
     * ????????????????????????
     * @return
     */
    @ApiOperation(value = "????????????????????????")
    @PostMapping("getIOSVersion")
    public JsonResult getIOSVersion(HttpServletRequest request){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            //?????????????????????
            VersionIOS versionIOS = versionService.findActiveIOS();
            if(versionIOS!=null){
                jsonResult.setData(versionIOS);
            }else{
                jsonResult.setCode(JsonResult.ERROR);
                jsonResult.setMsg("??????????????????");
            }
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
        }
        return jsonResult;
    }



}
