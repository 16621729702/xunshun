package com.wink.livemall.admin.api.shop;

import com.google.gson.Gson;
import com.wink.livemall.admin.api.good.GoodController;
import com.wink.livemall.admin.api.index.IndexController;
import com.wink.livemall.admin.util.DateUtils;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.PageUtil;
import com.wink.livemall.admin.util.VerifyFields;
import com.wink.livemall.admin.util.httpclient.HttpClient;
import com.wink.livemall.goods.dto.LivedGood;
import com.wink.livemall.goods.dto.LmShareGood;
import com.wink.livemall.goods.service.GoodService;
import com.wink.livemall.goods.service.LotsService;
import com.wink.livemall.goods.service.MerchGoodService;
import com.wink.livemall.live.dto.LmLive;
import com.wink.livemall.live.service.LmLiveService;
import com.wink.livemall.member.dto.LmMember;
import com.wink.livemall.member.dto.LmMemberFollow;
import com.wink.livemall.member.dto.LmMemberLevel;
import com.wink.livemall.member.service.LmMemberFollowService;
import com.wink.livemall.member.service.LmMemberLevelService;
import com.wink.livemall.member.service.LmMemberService;
import com.wink.livemall.merch.dto.*;
import com.wink.livemall.merch.service.*;
import com.wink.livemall.order.dto.LmOrder;
import com.wink.livemall.order.dto.LmOrderGoods;
import com.wink.livemall.order.service.LmOrderCommentService;
import com.wink.livemall.order.service.LmOrderGoodsService;
import com.wink.livemall.order.service.LmOrderService;
import com.wink.livemall.sys.setting.dto.Configs;
import com.wink.livemall.sys.setting.dto.Lideshow;
import com.wink.livemall.sys.setting.service.ConfigsService;
import com.wink.livemall.sys.setting.service.LideshowService;
import com.wink.livemall.utils.cache.redis.RedisUtil;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
@Api(tags = "商户信息接口")
@RestController
@RequestMapping("merch")
public class MerchController {

	private static final Logger logger = LogManager.getLogger(MerchController.class);
	@Autowired
	private LmMerchCategoryService lmMerchCategoryService;
	@Autowired
	private LmOrderGoodsService lmOrderGoodsService;
	@Autowired
	private LmBusinessEntityService lmBusinessEntityService;
	@Autowired
	private LotsService lotsService;
	@Autowired
	private LmSellCateService lmSellCateService;
	@Autowired
	private LmOrderCommentService lmOrderCommentService;
	@Autowired
	private LmMerchInfoService lmMerchInfoService;
	@Autowired
	private LmMemberLevelService lmMemberLevelService;
	@Autowired
	private LmMerchBlackService lmMerchBlackService;
	@Autowired
	private LmLiveService lmLiveService;
	@Autowired
	private LideshowService lideshowService;
	@Autowired
	private GoodService goodService;
	@Autowired
	private LmOrderService lmOrderService;
	@Autowired
	private LmMemberFollowService lmMemberFollowService;
	@Autowired
	private RedisUtil redisUtils;
	@Autowired
	private MerchGoodService merchGoodService;
	@Autowired
	private LmMemberService lmMemberService;
	@Autowired
	private ConfigsService configsService;
	@Autowired
	private LmWithdrawalService lmWithdrawalService;
	
	
	
	   
	   
	/**
	 * 获取所有顶级分类
	 * @return
	 */
	@ApiOperation(value = "获取所有顶级分类")
	@PostMapping("category")
	public JsonResult category(){
		JsonResult jsonResult = new JsonResult();
		jsonResult.setCode(JsonResult.SUCCESS);
		try {
			List<LmMerchCategory> list = lmMerchCategoryService.findActiceListByApi();
			Map<String, Object> resdata=new HashMap<String, Object>();
			resdata.put("list",list);
			jsonResult.setData(resdata);
		} catch (Exception e) {
			e.printStackTrace();
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}
		return jsonResult;
	}

	/**
	 * 根据分类获取商铺信息和商品信息
	 * @return
	 */
	@ApiOperation(value = "根据分类获取商铺信息和商品信息")
	@PostMapping("/list/{categoryid}")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "categoryid", value = "分类id", defaultValue = "0", required = true),
			@ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", required = true),
			@ApiImplicitParam(name = "pagesize", value = "每页条数", defaultValue = "3", required = true)
	})
	@Transactional
	public JsonResult category(@PathVariable int categoryid, HttpServletRequest request,
							   @RequestParam(required = true,defaultValue = "1") int page,
							   @RequestParam(required = true,defaultValue = "3") int pagesize){
		JsonResult jsonResult = new JsonResult();
		jsonResult.setCode(JsonResult.SUCCESS);
		try {
			List<Map<String,Object>> list = lmMerchInfoService.findMerchInfoByCategoryByApi(categoryid);
			List<Map<String,Object>> returnlist = new ArrayList<>();
			for(Map<String,Object> map : list){
				//添加商品信息
				Integer merchid = (int)map.get("id");
				List<Map<String,Object>> goodlist = goodService.findByMerchIdByApi(merchid);
				if(goodlist!=null&&goodlist.size()>0){
					map.put("goodlist",new Gson().toJson(goodlist));
				}
				returnlist.add(map);
			}
			returnlist = PageUtil.startPage(returnlist, page, pagesize);
			jsonResult.setData(returnlist);
		} catch (Exception e) {
			e.printStackTrace();
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

		}
		return jsonResult;
	}


	/**
	 * 获取经营主体
	 */
	@ApiOperation(value = "获取经营主体")
	@PostMapping("businessentity")
	public JsonResult getBusinessEntity() {
		JsonResult jsonResult=new JsonResult();
		Map< String, String> condient=new HashMap<String, String>();
		condient.put("status", "1");
		try {
			Example example=new Example(LmBusinessEntity.class);
			Criteria criteria=example.createCriteria();
			criteria.andEqualTo("status", 1);
			List<LmBusinessEntity> list =lmBusinessEntityService.findByCondient(example);
			jsonResult.setCode(jsonResult.SUCCESS);
			Map<String, Object> resdata=new HashMap<String, Object>();
			resdata.put("list",list);
			jsonResult.setData(resdata);
		} catch (Exception e) {
			e.printStackTrace();
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}


		return jsonResult;
	}

	/**
	 * 获取主营类目
	 */
	@ApiOperation(value = "获取主营类目")
	@PostMapping("sellcate")
	public JsonResult getSellCate() {
		JsonResult jsonResult=new JsonResult();

		try {
			List<LmSellCate> list =lmSellCateService.findlist();
			jsonResult.setCode(jsonResult.SUCCESS);
			Map<String, Object> resdata=new HashMap<String, Object>();
			resdata.put("list",list);
			jsonResult.setData(resdata);
		} catch (Exception e) {
			e.printStackTrace();
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}

		return jsonResult;
	}


	/**
	 * 获取商户信息
	 */
	@ApiOperation(value = "获取商户信息")
	@PostMapping("detail")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "String", name = "Authorization", value = "token标记", required = false) })
	public JsonResult detail(HttpServletRequest  request,
							 @ApiParam(name = "id", value = "id",defaultValue = "1", required = true)@RequestParam(required = true) int id) {
		JsonResult jsonResult=new JsonResult();
		jsonResult.setCode(jsonResult.SUCCESS);
		String header = request.getHeader("Authorization");
		String userid = "";
		if (StringUtils.isEmpty(header)) {
			jsonResult.setCode(JsonResult.LOGIN);
			return jsonResult;
		}else{
			userid = redisUtils.get(header)+"";
		}

		List<Map> goodlist = new ArrayList<>();
		Map<String,Object> returnmap = null;

		try {
			returnmap =lmMerchInfoService.findInfoByIdByApd(id);
			if(returnmap!=null){
				if(!StringUtils.isEmpty(userid)&&!"null".equals(userid)){
					LmMemberFollow lmMemberFollow = lmMemberFollowService.findByMemberidAndMerchId(userid,id);
					if(lmMemberFollow!=null){
						returnmap.put("isfollow","yes");
						returnmap.put("followid",lmMemberFollow.getId());
					}else{
						returnmap.put("isfollow","no");
					}
				}

				Map<String,Object> map =lmLiveService.finddirectlyinfoByApi();
	             if(map!=null){
	             	 map.put("showtype","live");
	 				goodlist.add(map);
	             }
	             int auctiongoodnum = 0;
				int pricegoodnum = 0;

				List<Map<String,Object>> goods = goodService.findByMerchIdByApi(id);
				for(Map<String,Object> mapinfo:goods){
					mapinfo.put("showtype","good");
					int type = (int)mapinfo.get("type");
					if(type==1){
						auctiongoodnum++;
					}else{
						pricegoodnum++;
					}
				}
				goodlist.addAll(goods);
				returnmap.put("goodlist",goodlist);
				returnmap.put("auctiongoodnum",auctiongoodnum);
				returnmap.put("pricegoodnum",pricegoodnum);
				List<Lideshow> lideshows = lideshowService.findListBytype(Lideshow.MERCH);
				returnmap.put("lideshowlist",lideshows);
				LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(id+"");
				returnmap.put("merchscore",lmMerchInfo.getScore());//商户评分
				returnmap.put("goodper",lmMerchInfo.getGoodper());//好评率
				returnmap.put("successper",lmMerchInfo.getSuccessper());//成交率
				returnmap.put("backper",lmMerchInfo.getBackper());//退货率
				returnmap.put("credit",lmMerchInfo.getCredit());//余额


			}else{
				returnmap = new HashMap<>();
			}
			jsonResult.setData(returnmap);
		} catch (Exception e) {
			e.printStackTrace();
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}

		return jsonResult;
	}

	/**
	 * 商户商品信息
	 */
	@ApiOperation(value = "商户商品信息")
	@PostMapping("merchgood")
	public JsonResult merchgood(HttpServletRequest  request,
							 @ApiParam(name = "id", value = "id",defaultValue = "1", required = true)@RequestParam(required = true) int id,
								@ApiParam(name = "page", value = "页码",defaultValue = "1", required = true)@RequestParam(required = true) int page,
								@ApiParam(name = "pagesize", value = "每页条数",defaultValue = "10", required = true)@RequestParam(required = true) int pagesize,
								@ApiParam(name = "type", value = "0一口价1拍卖",defaultValue = "0", required = true)@RequestParam(required = true) int type) {

		JsonResult jsonResult=new JsonResult();
		jsonResult.setCode(jsonResult.SUCCESS);
		List<Map> goodlist = new ArrayList<>();
		try {
			Map<String,Object> map =lmLiveService.finddirectlyinfoByApi();
            if(map!=null){
            	 map.put("showtype","live");
     			goodlist.add(map);
            }
			List<Map<String,String>> goods = goodService.findByMerchIdAndTypeByApi(id,type);
			for(Map<String,String> mapinfo:goods){
				mapinfo.put("showtype","good");
			}
			goodlist.addAll(goods);
			jsonResult.setData(PageUtil.startPage(goodlist,page,pagesize));
		} catch (Exception e) {
			e.printStackTrace();
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}

		return jsonResult;
	}




	/**
	 * 修改商户信息
	 * mobile手机 ，link联系人，deposit押金，出价条件，是否使用定金，保证金自动从余额扣除设置
	 */
	@RequestMapping("upd_merchinfo")
	@ResponseBody

	public JsonResult updateMerchinfo(HttpServletRequest  request) {
		
		JsonResult jsonResult = new JsonResult();
		if(StringUtils.isEmpty(request.getParameter("id"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("商户id为空");
			return jsonResult;
		}
		try {
			Map<String, Object> res=new HashMap<String, Object>();
			VerifyFields ver=new VerifyFields();
			String[] fields= {"link","mobile","deposit","defaults_open","defaults_num","refund_open","refund_num","level_open","isstep","autodeduct","store_name"
					,"description","avatar","refund_address","refund_mobile","refund_link"};
		
			List<Map<String, String>> field_ = ver.checkEmptyField(fields, request);
			lmMerchInfoService.updateByFields(field_, request.getParameter("id"));
		} catch (Exception e) {
			e.printStackTrace();
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}

		return jsonResult;
	}


	/**
	 * 获取粉丝列表
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "获取粉丝列表")
	@PostMapping("get_follow_list")
	public JsonResult getFollowList(HttpServletRequest  request) {
		JsonResult jsonResult=new JsonResult();
		String pagesize=request.getParameter("pagesize");
		String pageindex=request.getParameter("pageindex");
		String merchid=request.getParameter("merchid");
		Map<String, Object> params=new HashMap<>();
		pagesize=StringUtils.isEmpty(pagesize)?"0":pagesize;
		pageindex=StringUtils.isEmpty(pageindex)?"0":pageindex;
		merchid=StringUtils.isEmpty(merchid)?"0":merchid;

		params.put("pagesize",pagesize);
		params.put("pageindex",pageindex);
		params.put("merchid",merchid);
		params.put("day",request.getParameter("day"));

		try {
			//获取粉丝列表
			List<Map<String, Object>> list=lmMemberFollowService.findlist(params);
			Map<String, Object> res=new HashMap<String, Object>();
			res.put("pageindex", pageindex);
			res.put("pagesize", pagesize);
			res.put("list",list);

			jsonResult.setData(res);
			jsonResult.setCode(jsonResult.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}


		return jsonResult;
	}


	/**
	 * 获取黑名单列表
	 * type 1商户拉黑客户 2客户拉黑商户
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "获取黑名单列表type 1商户拉黑客户 2客户拉黑商户")
	@PostMapping("get_black_list_merch")
	public JsonResult getBlackList(HttpServletRequest  request) {
		JsonResult jsonResult=new JsonResult();
		String pagesize=request.getParameter("pagesize");
		String pageindex=request.getParameter("pageindex");
		String merchid=request.getParameter("merchid");
		pagesize=StringUtils.isEmpty(pagesize)?"0":pagesize;
		pageindex=StringUtils.isEmpty(pageindex)?"0":pageindex;
		merchid=StringUtils.isEmpty(merchid)?"0":merchid;

		Map<String, Object> params=new HashMap<>();
		params.put("pagesize",pagesize);
		params.put("pageindex",pageindex);
		params.put("merchid",merchid);

		try {
			List<Map<String, Object>> list=lmMerchBlackService.findlist(params);
			Map<String, Object> res=new HashMap<String, Object>();
			res.put("pageindex", pageindex);
			res.put("pagesize", pagesize);
			res.put("list",list);

			jsonResult.setData(res);
			jsonResult.setCode(jsonResult.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}


		return jsonResult;
	}


	/**
	 * @return
	 *  加入黑名单
	 */
	@ApiOperation(value = "加入黑名单")
	@PostMapping("add_black_merch")
	public JsonResult addBlackMerch(HttpServletRequest request) {
		JsonResult jsonResult=new JsonResult();

		try {
			String merchid=request.getParameter("merchid");
			String memberid=request.getParameter("memberid");
			if(StringUtils.isEmpty(merchid)||StringUtils.isEmpty(memberid)) {
				jsonResult.setCode(JsonResult.ERROR);
				jsonResult.setMsg("商户id或者用户id为空");
				return jsonResult;
			}

			Map< String, Object> map=new HashMap<String, Object>();
			map.put("memberid", memberid);
			map.put("merchid", merchid);
			map.put("type",1);
			int count=lmMerchBlackService.findExistMerch(map);

			if(count>0) {
				jsonResult.setCode(JsonResult.ERROR);
				jsonResult.setMsg("已经拉黑");
				return jsonResult;
			}else {
				LmMerchBlack entity=new LmMerchBlack();
				entity.setMemberid(Integer.parseInt(memberid));
				entity.setMerchid(Integer.parseInt(merchid));
				entity.setType(1);
				entity.setCreatetime(new Date());

				lmMerchBlackService.addBlack(entity);
			}

			jsonResult.setCode(jsonResult.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}

		return jsonResult;
	}

	/**
	 * @return
	 *  删除 黑名单
	 */
	@RequestMapping("del_black_merch")
	@ResponseBody
	public JsonResult delBlack(HttpServletRequest request) {
		JsonResult jsonResult=new JsonResult();

		try {
			String id=request.getParameter("id");
			lmMerchBlackService.delBlack(Integer.parseInt(id));
			jsonResult.setCode(jsonResult.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}

		return jsonResult;
	}
	
	/**
	 * @return 检查是否开启商品库
	 */
	@RequestMapping("check_open")
	@ResponseBody
	public JsonResult checkOpen(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		String merchid = request.getParameter("id");
		if (merchid == null) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("id为空");
			return jsonResult;
		}
		LmMerchInfo info=lmMerchInfoService.findById(merchid);
		if(info==null) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("商户不存在");
			return jsonResult;
		}else {
			Map< String, Object> res=new HashMap<String, Object>();
			if(info.getIsopen()==0) {
				jsonResult.setCode(jsonResult.SUCCESS);
				res.put("status", 1);
				jsonResult.setData(res);
				return jsonResult;
			}else {
				jsonResult.setCode(jsonResult.SUCCESS);
				res.put("status", 2);
				jsonResult.setData(res);
				return jsonResult;
			}
		}

		
	}
	
	/**
	 * @return 开启商品库
	 */
	@RequestMapping("do_open")
	@ResponseBody
	public JsonResult doOpen(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		String merchid = request.getParameter("id");
		if (merchid == null) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("id为空");
			return jsonResult;
		}
		LmMerchInfo info=lmMerchInfoService.findById(merchid);
		if(info==null) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("商户不存在");
			return jsonResult;
		}else {
			if(info.getState()==0) {
				jsonResult.setCode(jsonResult.ERROR);
				jsonResult.setMsg("待审核");
				return jsonResult;
			}else if(info.getState()==1){
				info.setIsopen(1);
				lmMerchInfoService.updateService(info);
				jsonResult.setCode(jsonResult.SUCCESS);
				return jsonResult;
			}else if(info.getState()==2){
				jsonResult.setCode(jsonResult.ERROR);
				jsonResult.setMsg("已禁用");
				return jsonResult;
			}
		}

		return jsonResult;
	}
	
	
	/**
	 * @return
	 *  获取商户基础信息
	 */
	@RequestMapping("get_merchinfo")
	@ResponseBody
	public JsonResult getMerchinfo(HttpServletRequest request) {
		JsonResult jsonResult=new JsonResult();
		if (StringUtils.isEmpty(request.getParameter("id"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("id为空");
			return jsonResult;
		}
		try {
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			LmMerchInfo lmMerchInfo=lmMerchInfoService.findById(request.getParameter("id"));
			if(lmMerchInfo!=null)
			{
				Map<String, String> params=new HashMap<String, String>();
				params.put("merchid", request.getParameter("id"));
				params.put("status", "1");
				int goodsnum=merchGoodService.countByStatus(params);
				Map<String, Object> res=new HashMap<String, Object>();
				//商户是否关注
				List<LmMemberFollow> merchfollowlist = lmMemberFollowService.findByMerchidAndType(1,lmMerchInfo.getId());
				if(merchfollowlist!=null&&merchfollowlist.size()>0){
					lmMerchInfo.setFocusnum(merchfollowlist.size());
				}
				//合买订单数量
				List<LmOrder> orderlist = lmOrderService.findShareOrderListByMerchid(Integer.parseInt(request.getParameter("id")));
				res.put("shareorders", orderlist!=null?orderlist.size():0);

				res.put("merchinfo", lmMerchInfo);
				res.put("goodsnum", goodsnum);
				String createtime=sf.format(lmMerchInfo.getCreate_at());
				String limittime=sf.format(lmMerchInfo.getLimit_time());
				res.put("createtime", createtime);
				res.put("limittime", limittime);
				LmMember member=lmMemberService.findById(lmMerchInfo.getMember_id()+"");
				res.put("credit", lmMerchInfo.getCredit());
				jsonResult.setData(res);
				jsonResult.setCode(jsonResult.SUCCESS);
			}
			else
			{
				jsonResult.setMsg("参数异常");
				jsonResult.setCode(JsonResult.ERROR);
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
	 * 店铺印象
	 * @return
	 */
	@ApiOperation(value = "店铺印象")
	@PostMapping("impression")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "merchid", value = "店铺id", defaultValue = "0", required = true),
			@ApiImplicitParam(name = "type", value = "类型1简介2评价", defaultValue = "1", required = true)
	})
	@Transactional
	public JsonResult impression(@RequestParam(required = true,defaultValue = "1") String merchid,
								 @RequestParam(required = true,defaultValue = "1") int type,
								 @RequestParam(required = false,defaultValue = "1") int isimg
							   ){
		JsonResult jsonResult = new JsonResult();
		jsonResult.setCode(JsonResult.SUCCESS);
		Map<String,Object> returnmap = new HashMap<>();
		try {
			//是否关注
			//获取店铺信息
			LmMerchInfo lmMerchInfo  = lmMerchInfoService.findById(merchid);
			if(lmMerchInfo!=null){
				LmMember lmMember = lmMemberService.findById(lmMerchInfo.getMember_id()+"");
				if(lmMember!=null){
					returnmap.put("growthvalue",lmMember.getGrowth_value());
				}else{
					returnmap.put("growthvalue","0");
				}
				returnmap.put("name",lmMerchInfo.getStore_name());
				returnmap.put("avatar",lmMerchInfo.getAvatar());
				returnmap.put("postage",lmMerchInfo.getPostage());
				returnmap.put("isquality",lmMerchInfo.getIsquality());
				returnmap.put("isoem",lmMerchInfo.getIsoem());
				returnmap.put("isdirect",lmMerchInfo.getIsdirect());
				returnmap.put("isopen",lmMerchInfo.getIsopen());
				returnmap.put("refund",lmMerchInfo.getRefund());
				returnmap.put("score",lmMerchInfo.getScore());
				returnmap.put("state",lmMerchInfo.getState());
				returnmap.put("margin",lmMerchInfo.getMargin());
				LmMemberLevel lmMemberLevel = lmMemberLevelService.findById(lmMerchInfo.getLevelid()+"");
				if(lmMemberLevel!=null){
					returnmap.put("levelname",lmMemberLevel.getName());
					returnmap.put("levelcode",lmMemberLevel.getCode());
				}else{
					returnmap.put("levelname","");
					returnmap.put("levelcode","");
				}
				if(type==1){
					//店铺简介
					returnmap.put("refund_address",lmMerchInfo.getRefund_address());
					returnmap.put("createtime", DateUtils.sdf_yMdHms.format(lmMerchInfo.getCreate_at()));
					returnmap.put("description",lmMerchInfo.getDescription());
					returnmap.put("deposit",lmMerchInfo.getDeposit());
				}else{
					//店铺评价
					List<Map<String,Object>> list = lmOrderCommentService.findByMerchId(merchid);
					List<Map<String,Object>> returnlist = new ArrayList<>();
					int allnum = 0;
					int imgnum = 0;
					for(Map<String,Object> map:list){
						if(map.get("img")!=null){
							imgnum++;
							if(isimg==1){
								returnlist.add(map);
							}
						}
						allnum++;
						if(isimg==0){
							returnlist.add(map);
						}
						map.put("finishtime",DateUtils.sdf_yMdHms.format(map.get("finishtime")));
					}
					returnmap.put("allnum",allnum);
					returnmap.put("imgnum",imgnum);
					returnmap.put("commentlist",returnlist);
				}
			}
			jsonResult.setData(returnmap);
		} catch (Exception e) {
			e.printStackTrace();
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

		}
		return jsonResult;
	}

	/**
	 * 提现申请
	 * @return
	 */
	@ApiOperation(value = "提现申请")
	@PostMapping("withdrawalapply")
	@Transactional
	public JsonResult withdrawalapply(@ApiParam(name = "merchid", value = "商户id", required = true)@RequestParam(required = true,defaultValue = "1") String merchid,
									  @ApiParam(name = "money", value = "金额", required = true)@RequestParam(required = true,defaultValue = "1") String money,
									  @ApiParam(name = "type", value = "银行名称", required = true)@RequestParam(required = true,defaultValue = "1") String type,
									  @ApiParam(name = "card", value = "银行卡号", required = true)@RequestParam(required = true,defaultValue = "1") String card,
									  @ApiParam(name = "name", value = "姓名", required = true)@RequestParam(required = true,defaultValue = "1") String name
									  ){
		JsonResult jsonResult = new JsonResult();
		jsonResult.setCode(JsonResult.SUCCESS);
		try {
			//获取提现限制
			Configs tradconfigs =configsService.findByTypeId(Configs.type_trading);
			Map tradmap =  com.alibaba.fastjson.JSONObject.parseObject(tradconfigs.getConfig());
			String withdraw_limit=tradmap.get("withdraw_limit")+"";
			String withdraw_commission=tradmap.get("withdraw_commission")+"";
			String withdraw_commission_free_min=tradmap.get("withdraw_commission_free_min")+"";
			String withdraw_commission_free_max=tradmap.get("withdraw_commission_free_max")+"";
			if(new BigDecimal(money).compareTo(new BigDecimal(withdraw_limit)) > -1){
				LmWithdrawal lmWithdrawal = new LmWithdrawal();
				lmWithdrawal.setStatus(0);
				lmWithdrawal.setCreatedate(new Date());
				lmWithdrawal.setMerchid(Integer.parseInt(merchid));
				lmWithdrawal.setMoney(new BigDecimal(money));
				lmWithdrawal.setCard(card);
				lmWithdrawal.setName(name);
				lmWithdrawal.setType(type);
				//判断是否需要交手续费
				if(new BigDecimal(money).compareTo(new BigDecimal(withdraw_commission_free_min)) > -1 && new BigDecimal(money).compareTo(new BigDecimal(withdraw_commission_free_max)) < 1){
					lmWithdrawal.setPoundage(new BigDecimal(withdraw_commission));
					lmWithdrawal.setRealmoney(new BigDecimal(money).subtract(new BigDecimal(withdraw_commission)));
				}else{
					lmWithdrawal.setPoundage(new BigDecimal(0));
					lmWithdrawal.setRealmoney(new BigDecimal(money));
				}
				lmWithdrawalService.insertservice(lmWithdrawal);
				//扣除余额
				LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(merchid);
				lmMerchInfo.setCredit(lmMerchInfo.getCredit().subtract(new BigDecimal(money)));
				
				
				lmMerchInfoService.updateService(lmMerchInfo);
			}else{
				jsonResult.setCode(JsonResult.ERROR);
				jsonResult.setMsg("提现金额不满足最低提现要求");
				return jsonResult;
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
	 * @return
	 *  商户管理员列表
	 */
	@ApiOperation(value = "商户管理员列表")
	@PostMapping("admin_list")
	public JsonResult setAdmin(HttpServletRequest request,
							   @ApiParam(name = "merchid", value = "商户id",defaultValue = "", required = true)@RequestParam(required = true) String merchid) {
		JsonResult jsonResult=new JsonResult();
		try {
			List<Map<String,Object>> mapinfo = lmMerchInfoService.findAdminInfo(merchid);
			jsonResult.setData(mapinfo);
			jsonResult.setCode(JsonResult.SUCCESS);
		} catch (Exception e) {
				e.printStackTrace();
				jsonResult.setMsg(e.getMessage());
				jsonResult.setCode(JsonResult.ERROR);
				logger.error(e.getMessage());
			}
			return jsonResult;
		}
	/**
	 * @return
	 *  添加商户管理员
	 */
	@ApiOperation(value = "添加商户管理员")
	@PostMapping("set_admin")
	public JsonResult setAdmin(HttpServletRequest request,
							   @ApiParam(name = "merchid", value = "商户id",defaultValue = "", required = true)@RequestParam(required = true) String merchid,
							   @ApiParam(name = "mobile", value = "管理员账户",defaultValue = "", required = true)@RequestParam(required = true) String mobile) {
		JsonResult jsonResult=new JsonResult();
		try {
			LmMember lmMember = lmMemberService.findByMobile(mobile);
			if(lmMember!=null){
				LmMerchAdmin lmMerchAdmin = new LmMerchAdmin();
				lmMerchAdmin.setMemberid(lmMember.getId());
				lmMerchAdmin.setMerchid(Integer.parseInt(merchid));
				lmMerchInfoService.addMerchAdmin(lmMerchAdmin);
				jsonResult.setCode(jsonResult.SUCCESS);
			}else{
				jsonResult.setCode(jsonResult.ERROR);
				jsonResult.setMsg("该账户不存在");
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
	 * @return
	 *  删除商户管理员
	 */
	@ApiOperation(value = "删除商户管理员")
	@PostMapping("delete_admin")
	public JsonResult deleteAdmin(HttpServletRequest request,
								  @ApiParam(name = "merchid", value = "商户id",defaultValue = "", required = true)@RequestParam(required = true) String merchid,
								  @ApiParam(name = "memberid", value = "管理员用户id",defaultValue = "", required = true)@RequestParam(required = true) String memberid) {
		JsonResult jsonResult=new JsonResult();
		try {
			boolean isflag = lmMerchInfoService.deleteMerchAdmin(merchid,memberid);
			if(isflag){
				jsonResult.setCode(jsonResult.SUCCESS);
			}else{
				jsonResult.setCode(jsonResult.ERROR);
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
	 * @return
	 *  发布合买商品
	 */
	@ApiOperation(value = "发布合买商品")
	@PostMapping("publicshare")
	public JsonResult publicshare(HttpServletRequest request,
								  @RequestParam(value = "liveid",required = true) String liveid,
								  @RequestParam(value = "name",required = true) String name,
								  @RequestParam(value = "price",required = true) String price,
								  @RequestParam(value = "material",required = true) String material,
								  @RequestParam(value = "chipped_num",required = true) String chipped_num,
								  @RequestParam(value = "chipped_price",required = true) String chipped_price,
								  @RequestParam(value = "auction_start_time",required = true) String auction_start_time,
								  @RequestParam(value = "auction_end_time",required = true) String auction_end_time,
								  @RequestParam(value = "img",required = true) String img) {
		JsonResult jsonResult=new JsonResult();
		try {
			LmLive lmLive = lmLiveService.findbyId(liveid);
			if(lmLive==null){
				jsonResult.setCode(JsonResult.ERROR);
				jsonResult.setMsg("直播间不存在");
				return jsonResult;
			}else{
				if(StringUtils.isEmpty(lmLive.getLivegroupid())){
					jsonResult.setCode(JsonResult.ERROR);
					jsonResult.setMsg("直播间尚未开启");
					return jsonResult;
				}
			}
			//查看是否存在合买商品
			List<LmShareGood> oldgood = goodService.findshareGoodByLiveid(liveid);
			if(oldgood!=null&&oldgood.size()>0){
				jsonResult.setCode(JsonResult.ERROR);
				jsonResult.setMsg("当前已存在正在销售的合买商品");
				return jsonResult;
			}
			LmShareGood lmShareGood = new LmShareGood();
			lmShareGood.setAuction_end_time(DateUtils.sdf_yMdHms.parse(auction_end_time));
			lmShareGood.setAuction_start_time(DateUtils.sdf_yMdHms.parse(auction_start_time));
			lmShareGood.setCreate_at(new Date());
			lmShareGood.setName(name);
			lmShareGood.setMaterial(material);
			lmShareGood.setChipped_num(Integer.parseInt(chipped_num));
			lmShareGood.setPrice(new BigDecimal(price));
			if("1".equals(chipped_num)){
				lmShareGood.setChipped_price(new BigDecimal(price));
			}else{
				lmShareGood.setChipped_price(new BigDecimal(chipped_price));
			}
			lmShareGood.setStatus(0);
			lmShareGood.setLiveid(Integer.parseInt(liveid));
			lmShareGood.setImg(img);
			goodService.insertShare(lmShareGood);
			//推送消息给app
			HttpClient httpClient = new HttpClient();
			httpClient.sendgroup(lmLive.getLivegroupid(),"合买商品已发布",2);
		} catch (Exception e) {
			e.printStackTrace();
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}
		return jsonResult;
	}



	/**
	 * @return
	 *  发布实时直播商品
	 */
	@ApiOperation(value = "发布实时直播商品")
	@PostMapping("publiclivedgood")
	public JsonResult publiclivedgood(HttpServletRequest request,
								  @RequestParam(value = "liveid",required = true) int liveid,
								  @RequestParam(value = "name",required = true) String name,
								  @RequestParam(value = "price",required = true) String price,
								  @RequestParam(value = "type",required = true) int type,
								  @RequestParam(value = "tomemberid",required = false) String tomemberid,
								  @RequestParam(value = "stepprice",required = false) String stepprice,
								  @RequestParam(value = "starttime",required = false) String starttime,
								  @RequestParam(value = "endtime",required = false) String endtime,
									  @RequestParam(value = "delaytime",required = false) String delaytime,
									  @RequestParam(value = "img",required = true) String img) {
		JsonResult jsonResult=new JsonResult();
		try {
			LmLive lmLive = lmLiveService.findbyId(liveid+"");
			if(lmLive==null){
				jsonResult.setCode(JsonResult.ERROR);
				jsonResult.setMsg("直播间不存在");
				return jsonResult;
			}else{
				if(StringUtils.isEmpty(lmLive.getLivegroupid())){
					jsonResult.setCode(JsonResult.ERROR);
					jsonResult.setMsg("直播间尚未开启");
					return jsonResult;
				}
			}
			//查看是否已有竞拍一口价商品存在
			List<LivedGood> oldgood = goodService.findLivedGoodByLiveid(liveid);
			if(oldgood!=null&&oldgood.size()>0){
				if(type!=2){
					for(LivedGood good:oldgood){
						if(good.getType()==1){
							jsonResult.setCode(JsonResult.ERROR);
							jsonResult.setMsg("当前已存在正在销售的直播商品");
							return jsonResult;
						}
					}
				}
			}
			LivedGood livedGood = new LivedGood();
			livedGood.setLiveid(liveid);
			livedGood.setType(type);
			livedGood.setImg(img);
			livedGood.setStatus(0);
			livedGood.setPrice(new BigDecimal(price));
			livedGood.setName(name);
			if(!StringUtils.isEmpty(stepprice)){
				livedGood.setStepprice(new BigDecimal(stepprice));
			}
			if(!StringUtils.isEmpty(starttime)){
				System.out.println(starttime);
				livedGood.setStarttime(DateUtils.sdf_yMdHms.parse(starttime));
			}
			if(!StringUtils.isEmpty(delaytime)){
				livedGood.setDelaytime(Integer.parseInt(delaytime));
			}
			if(!StringUtils.isEmpty(endtime)){
				System.out.println(endtime);
				livedGood.setEndtime(DateUtils.sdf_yMdHms.parse(endtime));
			}
			if(!StringUtils.isEmpty(tomemberid)){
				livedGood.setTomemberid(Integer.parseInt(tomemberid));
			}
			if(type==2){
				Calendar nowdate = Calendar.getInstance();
				nowdate.add(Calendar.MINUTE, 5);
				Date after = nowdate.getTime();
				livedGood.setStarttime(new Date());
				livedGood.setEndtime(after);
			}
			if(type==0){
				Calendar nowdate = Calendar.getInstance();
				nowdate.add(Calendar.MINUTE, 5);
				Date after = nowdate.getTime();
				livedGood.setStarttime(new Date());
				livedGood.setEndtime(after);
			}
			goodService.insertLivedGood(livedGood);
			//推送消息给app
			HttpClient httpClient = new HttpClient();
			if(type==0){
				httpClient.sendgroup(lmLive.getLivegroupid(),"直播一口价商品发布",5);
			}
			if(type==1){
				httpClient.sendgroup(lmLive.getLivegroupid(),"直播拍卖商品发布",6);
			}
			if(type==2){
				httpClient.sendgroup(lmLive.getLivegroupid(),livedGood.getTomemberid()+"",4);
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
	 * @return
	 *  合买订单列表
	 */
	@ApiOperation(value = "合买订单列表")
	@PostMapping("sharelist")
	public JsonResult sharelist(HttpServletRequest request,
								  @ApiParam(name = "merchid", value = "商户id",defaultValue = "", required = true)@RequestParam(required = true) String merchid,
								  @ApiParam(name = "type", value = "1待完成2已完成",defaultValue = "", required = true)@RequestParam(required = true) String type) {
		JsonResult jsonResult=new JsonResult();
		try {
			List<Map<String,Object>> orderlist = lmOrderService.findMerchshareList(merchid,type);
			for(Map<String,Object> map:orderlist){
				map.put("chippedtime",map.get("chippedtime")!=null?DateUtils.sdf_yMdHms.format(map.get("chippedtime")):"");
				List<Map<String,Object>> list = lotsService.findLotsInfo((int)map.get("goodid"));
				map.put("lotinfo",list);
			}
			jsonResult.setData(orderlist);
		} catch (Exception e) {
			e.printStackTrace();
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}
		return jsonResult;
	}




    /**
     * @return
     *  合买订单详情
     */
    @ApiOperation(value = "合买订单详情")
    @PostMapping("sharedetail")
    public JsonResult sharedetail(HttpServletRequest request,
                                @ApiParam(name = "orderid", value = "订单号",defaultValue = "", required = true)@RequestParam(required = true) String orderid){
        JsonResult jsonResult=new JsonResult();
        try {
        	Map<String,Object> returnmap = new HashMap<>();
        	//获取总订单信息
			LmOrder lmOrder = lmOrderService.findByOrderId(orderid);
			if(lmOrder!=null){
				returnmap.put("id",lmOrder.getId());
				returnmap.put("orderno",lmOrder.getOrderid());
				//合买凑单成功时间
				if(lmOrder.getChippedtime()==null){
					returnmap.put("finishtime","");
					returnmap.put("status","");
					returnmap.put("resttimes",0);
				}else{
					returnmap.put("finishtime",lmOrder.getChippedtime()!=null?DateUtils.sdf_yMdHms.format(lmOrder.getChippedtime()):"");
					long finishtime = lmOrder.getChippedtime().getTime();
					long nowdatetime = System.currentTimeMillis();
					returnmap.put("resttimes",finishtime-nowdatetime);
					if(lmOrder.getLots_status()==0){
						returnmap.put("status","待开料");
					}else{
						returnmap.put("status","已开料");
					}
				}
				LmOrderGoods lmOrderGoods = lmOrderGoodsService.findByOrderid(lmOrder.getId());
				if(lmOrderGoods!=null){
					LmShareGood lmShareGood = goodService.findshareById(lmOrderGoods.getGoodid());
					if(lmShareGood!=null){
						returnmap.put("name",lmShareGood.getName());
						returnmap.put("thumb",lmShareGood.getImg());
						returnmap.put("price",lmShareGood.getPrice());
						returnmap.put("material",lmShareGood.getMaterial());
					}
				}
				LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(lmOrder.getMerchid()+"");
				if(lmMerchInfo!=null){
					returnmap.put("merchname",lmMerchInfo.getStore_name());
					returnmap.put("merchid",lmMerchInfo.getId());
				}
				//子订单信息
				List<LmOrder> lmOrderList = lmOrderService.findChildOrder(lmOrder.getId());
				List<Map<String,Object>> listinfo = new ArrayList<>();
				for(LmOrder order:lmOrderList){

					Map<String,Object> map = new HashMap<>();
					map.put("id",order.getId());
					map.put("orderno",order.getOrderid());
					map.put("lotsno",order.getLots_log_no());
					map.put("childorderno",order.getOrderid());
					map.put("memberid",order.getMemberid());
					if(order.getLots_status()==0){
						map.put("status","待开料");
					}else{
						map.put("status","已开料");
					}
					if(order.getStatus().equals("0")){
						map.put("orderstatus","未支付");
					}else if(order.getStatus().equals("1")){
						map.put("orderstatus","待发货");
					}else if(order.getStatus().equals("2")){
						map.put("orderstatus","待收货");
					}else{
						map.put("orderstatus","已完成");
					}
					LmMember lmMember = lmMemberService.findById(order.getMemberid()+"");
					if(lmMember!=null){
						map.put("username",lmMember.getMobile());
					}
					listinfo.add(map);
				}
				returnmap.put("childorderinfo",listinfo);
				//原料展示信息
				List<Map<String,Object>> list = lotsService.findLotsInfo(lmOrderGoods.getGoodid());
				for(Map map:list){
					map.put("createtime",map.get("createtime")!=null?DateUtils.sdf_yMdHms.format(map.get("createtime")):"");
				}
				returnmap.put("lotinfo",list);
			}
			jsonResult.setData(returnmap);
        } catch (Exception e) {
            e.printStackTrace();
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }
}