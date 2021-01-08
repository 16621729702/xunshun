package com.wink.livemall.admin.api.index;

import com.sun.org.apache.xpath.internal.objects.XObject;
import com.wink.livemall.admin.util.JedisUtil;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.PageUtil;
import com.wink.livemall.goods.dto.LmGoodAuction;
import com.wink.livemall.goods.service.GoodService;
import com.wink.livemall.goods.service.LmGoodAuctionService;
import com.wink.livemall.live.dto.LmLive;
import com.wink.livemall.live.dto.LmLiveInfo;
import com.wink.livemall.live.service.LmLiveInfoService;
import com.wink.livemall.live.service.LmLiveService;
import com.wink.livemall.sys.setting.dto.Lideshow;
import com.wink.livemall.sys.setting.service.LideshowService;
import com.wink.livemall.utils.cache.redis.RedisUtil;
import com.wink.livemall.video.dto.LmVideoCore;
import com.wink.livemall.video.service.LmVideoCoreService;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import qiniu.happydns.local.SystemDnsServer;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

import static org.springframework.util.StringUtils.isEmpty;

@Api(tags = "首页模块")
@RestController
@RequestMapping("/index")
public class IndexController {

    Logger logger = LogManager.getLogger(IndexController.class);
    @Autowired
    private LideshowService lideshowService;
    @Autowired
    private LmLiveService lmLiveService;
    @Autowired
    private GoodService goodService;

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private LmGoodAuctionService lmGoodAuctionService;
    @Autowired
    private LmLiveInfoService lmLiveInfoService;

    @Autowired
    private RedisUtil redisUtils;

    /**
     * 获取轮播图
     * @return
     */
    @ApiOperation(value = "获取轮播图")
    @PostMapping("lideshow")
    public JsonResult getlideshowlist(HttpServletRequest request, @ApiParam(name = "type", value = "类型1首页顶部，2店铺顶部", required = true)@RequestParam(required = true) String type){
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
                    map.put("merchid",lideshow.getMerchid());
                    returnlist.add(map);
                }
            }
            jsonResult.setData(returnlist);
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }


    /**
     * 获取今日热门
     * @return
     */
    @ApiOperation(value = "获取今日热门")
    @PostMapping("hot")
    public JsonResult hot(){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        List<Map<String,Object>> list = new ArrayList<>();
        try {
            //查询普通热门直播间
            list = lmLiveService.findhotlive();
            for(Map<String,Object> map:list){
               int liveid=(int)map.get("id");
                LmLive lmLive = lmLiveService.findbyId(liveid+"");
                LmLiveInfo lmLiveInfo =lmLiveInfoService.findLiveInfo(liveid);
                map.put("watchnum",lmLive.getWatchnum()+lmLiveInfo.getWatchnum()+lmLiveInfo.getAddnum());
                map.put("type","normal");
            }
            //查看合买直播间
            Map<String,Object> sharemap = lmLiveService.findsharehotlive();
            if(sharemap!=null){
                int liveid=(int)sharemap.get("id");
                LmLive lmLive = lmLiveService.findbyId(liveid+"");
                LmLiveInfo lmLiveInfo =lmLiveInfoService.findLiveInfo(liveid);
                sharemap.put("watchnum",lmLive.getWatchnum()+lmLiveInfo.getWatchnum()+lmLiveInfo.getAddnum());
                sharemap.put("type","share");
                list.add(sharemap);
            }
            jsonResult.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }


    /**
     * 获取精彩推荐
     * @return
     */
    @ApiOperation(value = "获取精彩推荐")
    @PostMapping("recommend")
    @Transactional
    public JsonResult recommend(HttpServletRequest request){
        String page = StringUtils.isEmpty(request.getParameter("page"))?"0":request.getParameter("page");
        String mobileid = StringUtils.isEmpty(request.getParameter("mobileid"))?"0":request.getParameter("mobileid");
        String pagesize = StringUtils.isEmpty(request.getParameter("pagesize"))?"10":request.getParameter("pagesize");
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        List<Map> list = new ArrayList<>();

        try {
        	 Map<String,Object> map =lmLiveService.finddirectlyinfoByApi();
             if(map!=null){
                 int liveid=(int)map.get("id");
                 LmLive lmLive = lmLiveService.findbyId(liveid+"");
                 LmLiveInfo lmLiveInfo =lmLiveInfoService.findLiveInfo(liveid);
                 map.put("watchnum",lmLive.getWatchnum()+lmLiveInfo.getWatchnum()+lmLiveInfo.getAddnum());
             	 map.put("showtype","live");
             	 list.add(map);
             }
            //查询推荐商品列表
            List<Map> recommendList = goodService.findRecommendList();
            for(Map mapinfo:recommendList){
                mapinfo.put("showtype","good");
            }
            //查询销售最多的10个商品
            List<Map> returnlist=null;
            List<Object> hotList1=null;
            if(Integer.parseInt(page)==1){
                redisUtils.delete("hotList"+mobileid);
                List<Map> hotList = goodService.findHotList();
                    for(Map hotLists:hotList){
                        int id =(int)hotLists.get("goodid");
                        int ordertype =(int)hotLists.get("type");
                        if(1==ordertype) {
                            int types = 0;
                            LmGoodAuction lmGoodAuction = lmGoodAuctionService.findnowPriceByGoodidByApi(id, types);
                            if (!isEmpty(lmGoodAuction)) {
                                hotLists.put("goodprice", lmGoodAuction.getPrice());
                            } else {
                                hotLists.put("goodprice", 0);
                            }
                        }
                    }
                Collections.shuffle(hotList);
                redisTemplate.opsForList().rightPushAll("hotList"+mobileid,hotList);
                //redisUtils.lSet("hotList"+mobileid,hotList);
               returnlist = PageUtil.startPage(hotList,Integer.parseInt(page),Integer.parseInt(pagesize));
            }else {
                hotList1 =redisTemplate.opsForList().range("hotList"+mobileid,0,-1);
                //hotList1 =redisUtils.lGet("hotList"+mobileid,0,-1);
                returnlist = PageUtil.startPage(hotList1,Integer.parseInt(page),Integer.parseInt(pagesize));
            }

            for(Map mapinfo:returnlist){
                mapinfo.put("showtype","good");
            }
            if(recommendList!=null){
                list.addAll(recommendList);
            }
            if(returnlist!=null){
                list.addAll(returnlist);
            }
            jsonResult.setData(list);
        } catch (Exception e) {
        	e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }


}
