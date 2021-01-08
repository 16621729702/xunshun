package com.wink.livemall.admin.api.video;

import com.wink.livemall.admin.api.good.GoodController;
import com.wink.livemall.admin.util.DateUtils;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.PageUtil;
import com.wink.livemall.goods.dto.Good;
import com.wink.livemall.goods.dto.GoodCategory;
import com.wink.livemall.live.service.LmLiveService;
import com.wink.livemall.member.dto.LmMember;
import com.wink.livemall.member.dto.LmMemberFav;
import com.wink.livemall.member.dto.LmMemberLevel;
import com.wink.livemall.member.service.LmMemberFavService;
import com.wink.livemall.member.service.LmMemberLevelService;
import com.wink.livemall.member.service.LmMemberService;
import com.wink.livemall.merch.dto.LmMerchInfo;
import com.wink.livemall.merch.service.LmMerchInfoService;
import com.wink.livemall.utils.cache.redis.RedisUtil;
import com.wink.livemall.video.dto.LmVideoCategoary;
import com.wink.livemall.video.dto.LmVideoCore;
import com.wink.livemall.video.service.LmVideoCategoaryService;
import com.wink.livemall.video.service.LmVideoCoreService;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Api(tags = "视频信息接口")
@RestController
@RequestMapping("video")
public class VideoController {
    Logger logger = LogManager.getLogger(VideoController.class);
    @Autowired
    private RedisUtil redisUtils;
    @Autowired
    private LmVideoCategoaryService lmVideoCategoaryService;
    @Autowired
    private LmVideoCoreService lmVideoCoreService;
    @Autowired
    private LmMemberService lmMemberService;
    @Autowired
    private LmMemberLevelService lmMemberLevelService;
    @Autowired
    private LmMemberFavService lmMemberFavService;
    @Autowired
    private LmLiveService lmLiveService;

    /**
     * 获取所有顶级分类
     * @return
     */
    @ApiOperation(value = "获取所有顶级分类")
    @PostMapping("category")
    public JsonResult gettopcategory(){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            List<Map<String,String>> list = lmVideoCategoaryService.findtopcategoryByApi();
            jsonResult.setData(list);
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }


    /**
     * 获取顶部视频
     * @return
     */
    @ApiOperation(value = "获取顶部视频")
    @PostMapping("top")
    public JsonResult gettop(){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            Map<String,String> video = lmVideoCoreService.findTopVideo();
            jsonResult.setData(video);
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }

    /**
     * 根据视频分类获取视频列表
     * @return
     */
    @ApiOperation(value = "根据视频分类获取视频列表")
    @PostMapping("/list/{categoryid}")
    public JsonResult list(HttpServletRequest request,
                           @ApiParam(name = "categoryid", value = "分类id",defaultValue = "0", required = true)@PathVariable String categoryid,
                           @ApiParam(name = "page", value = "页码",defaultValue = "1", required = false)@RequestParam(required = false) int page,
                           @ApiParam(name = "pagesize", value = "每页数量",defaultValue = "10", required = false)@RequestParam(required = false) int pagesize){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        List<Map> list = new ArrayList<>();
        try {
        	 /*Map<String,Object> map =lmLiveService.finddirectlyinfoByApi();
             if(map!=null){
             	 map.put("showtype","live");
             	 list.add(map);
             }*/
            //如果为0则是获取最热的个视频
            if("0".equals(categoryid)){
                List<Map<String,String>> videolist = lmVideoCoreService.findHotVideolist();
                for(Map mapinfo:videolist){
                    mapinfo.put("showtype","video");
                }
                list.addAll(videolist);
                if(StringUtils.isEmpty(page)&&StringUtils.isEmpty(pagesize)){
                    jsonResult.setData(list);
                }else{
                    list = PageUtil.startPage(list,page,pagesize);
                    jsonResult.setData(list);
                }

            }else{
                List<Map<String,String>> videolist = lmVideoCoreService.findByCategoryIdByApi(categoryid);
                for(Map mapinfo:videolist){
                    mapinfo.put("showtype","video");
                }
                list.addAll(videolist);
                if(StringUtils.isEmpty(page)&&StringUtils.isEmpty(pagesize)){
                    jsonResult.setData(list);
                }else{
                    list = PageUtil.startPage(list,page,pagesize);
                    jsonResult.setData(list);
                }
            }
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }


//    /**
//     * 获取猜你喜欢视频信息
//     * @return
//     */
//    @ApiOperation(value = "视频添加收藏")
//    @PostMapping("addcollect/{userid}")
//    public JsonResult addcollect(HttpServletRequest request,
//                          @ApiParam(name = "tag", value = "标签id",defaultValue = "1", required = true)@RequestParam(required = true) String tag){
//        JsonResult jsonResult = new JsonResult();
//        jsonResult.setCode(JsonResult.SUCCESS);
//        try {
//            List<LmVideoCore> list = lmVideoCoreService.findByTag(tag);
//            jsonResult.setData(list);
//        } catch (Exception e) {
//            jsonResult.setMsg(e.getMessage());
//            jsonResult.setCode(JsonResult.ERROR);
//            logger.error(e.getMessage());
//        }
//        return jsonResult;
//    }


    /**
     * 获取猜你喜欢视频信息
     * @return
     */
    @ApiOperation(value = "获取猜你喜欢视频信息")
    @PostMapping("fav")
    public JsonResult fav(HttpServletRequest request,
                          @ApiParam(name = "categoryid", value = "分类id",defaultValue = "1", required = true)@RequestParam(required = true) String categoryid){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        List<LmVideoCore> returnlist = new ArrayList<>();
        try {
            List<LmVideoCore> list = lmVideoCoreService.findByTag(categoryid);
            Random random = new Random();
            int n = random.nextInt(list.size());
            LmVideoCore lmVideoCore = list.get(n);
            returnlist.add(lmVideoCore);
            jsonResult.setData(returnlist);
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }

    /**
     * 视频详细信息
     * @return
     */
    @ApiOperation(value = "视频详细信息")
    @PostMapping("detail/{videoid}")
    @Transactional
    public JsonResult detail(HttpServletRequest request, @ApiParam(name = "videoid", value = "视频id",defaultValue = "1", required = true)@PathVariable int videoid){
        JsonResult jsonResult = new JsonResult();
        String header = request.getHeader("Authorization");
        String userid = "";
        if (StringUtils.isEmpty(header)) {
            jsonResult.setCode(JsonResult.LOGIN);
            return jsonResult;
        }else{
            userid = redisUtils.get(header)+"";
        }
        jsonResult.setCode(JsonResult.SUCCESS);
        Map<String,Object> map = new HashMap<>();
        try {
            LmVideoCore lmVideoCore = lmVideoCoreService.findById(videoid+"");
            if(lmVideoCore!=null){
                map.put("id",lmVideoCore.getId());
                map.put("video",lmVideoCore.getVideo());
                map.put("name",lmVideoCore.getName());
                map.put("content",lmVideoCore.getContent());
                map.put("tag",lmVideoCore.getTag());
                map.put("img",lmVideoCore.getImg());
                map.put("creattime", DateUtils.sdf_yMdHms.format(lmVideoCore.getCreattime()));
                map.put("category",lmVideoCore.getCategory());
                map.put("good_id",lmVideoCore.getGood_id());
                map.put("playnum",lmVideoCore.getPlaynum());
                if(!StringUtils.isEmpty(lmVideoCore.getCreatuserid())){
                    LmMember lmMember = lmMemberService.findById(lmVideoCore.getCreatuserid()+"");
                    if(lmMember!=null){
                        map.put("avatar",lmMember.getAvatar());
                        map.put("nickname",lmMember.getNickname());
                        //判断是否收藏
                        if(!StringUtils.isEmpty(userid)&&!"null".equals(userid)){
                        	LmMemberFav lmMemberFav = lmMemberFavService.findByMemberidAndVideoidByApi(Integer.parseInt(userid),lmVideoCore.getId());
                            if(lmMemberFav!=null){
                                map.put("favid",lmMemberFav.getId());
                                map.put("isfav","yes");
                            }else{
                                map.put("isfav","no");
                            }
                        }else{
                        	map.put("isfav","no");
                        }
                        if(!StringUtils.isEmpty(lmMember.getLevel_id())){
                            LmMemberLevel lmMemberLevel = lmMemberLevelService.findById(lmMember.getLevel_id()+"");
                            if(lmMemberLevel!=null){
                                map.put("levelname",lmMemberLevel.getName());
                                map.put("levelcode",lmMemberLevel.getCode());
                            }
                        }
                        
                    }
                   
                }
            }
            jsonResult.setData(map);
        } catch (Exception e) {
        	e.printStackTrace();
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return jsonResult;
    }


}
