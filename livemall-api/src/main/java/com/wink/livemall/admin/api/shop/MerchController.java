package com.wink.livemall.admin.api.shop;

import com.google.gson.Gson;
import com.wink.livemall.admin.util.*;
import com.wink.livemall.admin.util.filterUtils.CheckTextAPI;
import com.wink.livemall.admin.util.filterUtils.GetAuthService;
import com.wink.livemall.admin.util.httpclient.HttpClient;
import com.wink.livemall.goods.dto.LivedGood;
import com.wink.livemall.goods.dto.LmGoodAuction;
import com.wink.livemall.goods.dto.LmShareGood;
import com.wink.livemall.goods.service.GoodService;
import com.wink.livemall.goods.service.LmGoodAuctionService;
import com.wink.livemall.goods.service.LotsService;
import com.wink.livemall.goods.service.MerchGoodService;
import com.wink.livemall.goods.utils.HttpJsonResult;
import com.wink.livemall.live.dto.LmLive;
import com.wink.livemall.live.service.LmLiveService;
import com.wink.livemall.live.service.LmMerchLiveService;
import com.wink.livemall.member.dto.LmMember;
import com.wink.livemall.member.dto.LmMemberFollow;
import com.wink.livemall.member.dto.LmMemberLevel;
import com.wink.livemall.member.service.LmFalsifyService;
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
import com.wink.livemall.sys.withdraw.service.WithdrawLogService;
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

import static org.springframework.util.StringUtils.isEmpty;

@Api(tags = "??????????????????")
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
	@Autowired
	private LmGoodAuctionService lmGoodAuctionService;
	@Autowired
	private LmMerchLiveService lmMerchLiveService;
	@Autowired
	private LmMerchMarginLogService lmMerchMarginLogService;
	@Autowired
	private LmFalsifyService lmFalsifyService;
	@Autowired
	private WithdrawLogService withdrawLogService;

	/**
	 * ????????????????????????
	 * @return
	 */
	@ApiOperation(value = "????????????????????????")
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
	 * ?????????????????????????????????????????????
	 * @return
	 */
	@ApiOperation(value = "?????????????????????????????????????????????")
	@PostMapping("/list/{categoryid}")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "categoryid", value = "??????id", defaultValue = "0", required = true),
			@ApiImplicitParam(name = "page", value = "??????", defaultValue = "1", required = true),
			@ApiImplicitParam(name = "pagesize", value = "????????????", defaultValue = "3", required = true)
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
				//??????????????????
				Integer merchid = (int)map.get("id");
				List<Map<String,Object>> goodlist = goodService.findByMerchIdByApi(merchid);
				for(Map<String,Object> goodlists:goodlist){
					Integer id =(int)goodlists.get("goodid");
					Integer ordertype =(int)goodlists.get("type");
					if(1==ordertype) {
						int types = 0;
						LmGoodAuction lmGoodAuction = lmGoodAuctionService.findnowPriceByGoodidByApi(id, types);
						if (!isEmpty(lmGoodAuction)) {
							goodlists.put("goodprice", lmGoodAuction.getPrice());
						} else {
							goodlists.put("goodprice", 0);
						}
					}
				}
				LmLive lmLive = lmLiveService.findByMerchid(merchid);
				if(lmLive!=null){
					map.put("live",lmLive);
				}else {
					map.put("live",null);
				}
				if(goodlist!=null&&goodlist.size()>0){
					map.put("goodlist",new Gson().toJson(goodlist));
					returnlist.add(map);
				}
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
	 * ?????????????????????
	 * @return
	 */
	@ApiOperation(value = "?????????????????????")
	@RequestMapping(value = "/merSumTake", method = RequestMethod.POST)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "merId", value = "??????Id", dataType = "Integer",paramType = "query",required =false)
	})
	public JsonResult merSumTake(HttpServletRequest request, Integer merId){
		JsonResult jsonResult = new JsonResult();
		jsonResult.setCode(JsonResult.SUCCESS);
		Map<String,Object> map=new HashMap<>();
		try {
			LmMerchInfo byId = lmMerchInfoService.findById(String.valueOf(merId));
			BigDecimal merMarginSum = lmMerchMarginLogService.merMarginSum(merId);
			/*BigDecimal merOrderPriceSum = lmOrderService.merOrderPriceSum(merId);*/
			BigDecimal falsifySum = lmFalsifyService.falsifySum(merId);
			map.put("merMarginSum",merMarginSum);
			map.put("merPriceSum",byId.getCredit());
			map.put("falsifySum",falsifySum);
			map.put("freeze",byId.getFreeze());
			jsonResult.setData(map);
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
		}
		return jsonResult;
	}


	/**
	 * ??????????????????
	 * @return
	 */
	@ApiOperation(value = "??????????????????")
	@RequestMapping(value = "/merSumList", method = RequestMethod.POST)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "merId", value = "??????Id", dataType = "Integer",paramType = "query",required =false)
	})
	public JsonResult merSumList(HttpServletRequest request, Integer merId){
		JsonResult jsonResult = new JsonResult();
		jsonResult.setCode(JsonResult.SUCCESS);
		try {
			List<Map<String, Object>> listByLogId = withdrawLogService.findListByLogId(merId);
			List<Map<String, Object>> maps = lmFalsifyService.merFalsifyList(merId);
			List<Map<String, Object>> maps1 = lmOrderService.merOrderList(merId);
			listByLogId.addAll(maps);
			listByLogId.addAll(maps1);
			ListSort(listByLogId);
			jsonResult.setData(listByLogId);
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
		}
		return jsonResult;
	}

	private static void ListSort(List<Map<String, Object>> List) {
		//???Collections??????????????????list????????????
		Collections.sort(List, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					String timeOne = (String)o1.get("time");
					String timeTwo = (String)o2.get("time");
					Date dt1 = format.parse(timeOne);
					Date dt2 = format.parse(timeTwo);
					if (dt1.getTime() > dt2.getTime()) {
						return -1;//???????????????
					}else {
						return 1;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return 0;
			}
		});
	}

	/**
	 * ??????????????????
	 */
	@ApiOperation(value = "??????????????????")
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
	 * ??????????????????
	 */
	@ApiOperation(value = "??????????????????")
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
	 * ??????????????????
	 */
	@ApiOperation(value = "??????????????????")
	@PostMapping("detail")
	@ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "String", name = "Authorization", value = "token??????", required = false) })
	public JsonResult detail(HttpServletRequest  request,
							 @ApiParam(name = "id", value = "id",defaultValue = "1", required = true)@RequestParam(required = true) int id) {
		JsonResult jsonResult=new JsonResult();
		jsonResult.setCode(jsonResult.SUCCESS);
		String header = request.getHeader("Authorization");
		String userid = "";
		if (!StringUtils.isEmpty(header)) {
			if(!StringUtils.isEmpty(TokenUtil.getUserId(header))){
				userid = TokenUtil.getUserId(header);
			}else{
				jsonResult.setCode(JsonResult.LOGIN);
				return jsonResult;
			}
		}

		List<Map> goodlist = new ArrayList<>();
		Map<String,Object> returnmap = null;

		try {
			returnmap =lmMerchInfoService.findInfoByIdByApd(id);
			int categoryid = (int)returnmap.get("categoryid");
			List<LmMerchCategory> acticeListByApi = lmMerchCategoryService.findActiceListByApi();
			for(LmMerchCategory lmMerchCategory :acticeListByApi){
				if(lmMerchCategory.getId()==categoryid){
					returnmap.put("categoryname",lmMerchCategory.getName());
				}
			}
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
					Integer ids=(int)mapinfo.get("goodid");
					Integer goodtype =(int)mapinfo.get("type");
					int type =0;
					if(1==goodtype) {
						LmGoodAuction lmGoodAuction = lmGoodAuctionService.findnowPriceByGoodidByApi(ids, type);
						if (!isEmpty(lmGoodAuction)) {
							mapinfo.put("goodprice", lmGoodAuction.getPrice());
						} else {
							mapinfo.put("goodprice", 0);
						}
					}
					int types = (int)mapinfo.get("type");
					if(types==1){
						auctiongoodnum++;
					}else{
						pricegoodnum++;
					}
					mapinfo.put("showtype","good");
				}
				goodlist.addAll(goods);
				returnmap.put("goodlist",goodlist);
				returnmap.put("auctiongoodnum",auctiongoodnum);
				returnmap.put("pricegoodnum",pricegoodnum);
				List<Lideshow> lideshows = lideshowService.findListBytype(Lideshow.MERCH);
				returnmap.put("lideshowlist",lideshows);
				LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(id+"");
				returnmap.put("merchscore",100);//????????????
				returnmap.put("goodper",100.0);//?????????
				returnmap.put("successper",100.0);//?????????
				returnmap.put("backper",lmMerchInfo.getBackper());//?????????
				returnmap.put("credit",lmMerchInfo.getCredit());//??????
				LmLive lmLive = lmLiveService.findByMerchid(id);
				if(lmLive!=null){
					returnmap.put("live",lmLive);
				}else {
					returnmap.put("live",null);
				}
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
	 * ??????????????????
	 */
	@ApiOperation(value = "??????????????????")
	@PostMapping("merchgood")
	public JsonResult merchgood(HttpServletRequest  request,
							 @ApiParam(name = "id", value = "id",defaultValue = "1", required = true)@RequestParam(required = true) int id,
								@ApiParam(name = "page", value = "??????",defaultValue = "1", required = true)@RequestParam(required = true) int page,
								@ApiParam(name = "pagesize", value = "????????????",defaultValue = "10", required = true)@RequestParam(required = true) int pagesize,
								@ApiParam(name = "type", value = "0?????????1??????",defaultValue = "0", required = true)@RequestParam(required = true) int type) {

		JsonResult jsonResult=new JsonResult();
		jsonResult.setCode(jsonResult.SUCCESS);
		List<Map> goodlist = new ArrayList<>();
		try {
			/*Map<String,Object> map =lmLiveService.finddirectlyinfoByApi();
            if(map!=null){
            	 map.put("showtype","live");
     			goodlist.add(map);
            }*/
			List<Map<String,Object>> goods = goodService.findByMerchIdAndTypeByApi(id,type);
			for(Map<String,Object> mapinfo:goods){
				Integer ids =(int)mapinfo.get("goodid");
				Integer ordertype =(int)mapinfo.get("type");
				if(1==ordertype) {
					int types = 0;
					LmGoodAuction lmGoodAuction = lmGoodAuctionService.findnowPriceByGoodidByApi(ids, types);
					if (!isEmpty(lmGoodAuction)) {
						mapinfo.put("goodprice", lmGoodAuction.getPrice());
					} else {
						mapinfo.put("goodprice", 0);
					}
				}
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
	 * ??????????????????
	 * mobile?????? ???link????????????deposit?????????????????????????????????????????????????????????????????????????????????
	 */
	@RequestMapping("upd_merchinfo")
	@ResponseBody

	public JsonResult updateMerchinfo(HttpServletRequest  request) {
		
		JsonResult jsonResult = new JsonResult();
		if(StringUtils.isEmpty(request.getParameter("id"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("??????id??????");
			return jsonResult;
		}
		try {
			Map<String, Object> res=new HashMap<String, Object>();
			VerifyFields ver=new VerifyFields();
			String[] fields= {"link","mobile","deposit","defaults_open","defaults_num","refund_open","refund_num","level_open","isstep","autodeduct","store_name"
					,"bg_image","description","avatar","refund_address","refund_mobile","refund_link"};
		
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
	 * ??????????????????
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "??????????????????")
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
		LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(merchid);
		params.put("pagesize",pagesize);
		params.put("pageindex",pageindex);
		params.put("merchid",merchid);
		params.put("day",request.getParameter("day"));

		try {
			//??????????????????
			List<Map<String, Object>> list=lmMemberFollowService.findlist(params);
			int num = Integer.parseInt(pagesize) * Integer.parseInt(pageindex);
			int cha =num-10+list.size();
			if(cha<lmMerchInfo.getFocusnum()){
				if(num>lmMerchInfo.getFocusnum()){
					for(int i=cha;i<lmMerchInfo.getFocusnum();i++){
						Map<String, Object> map=new HashMap<String, Object>();
						map.put("avatar", "http://oss.xunshun.net/LOGO.jpg");
						map.put("nickname", (int) (Math.random() * (9999999 - 1000000) + 1000000));
						list.add(map);
					}
				}else {
					if(list.size()<10){
						for(int i=cha;i<num;i++){
							Map<String, Object> map=new HashMap<String, Object>();
							map.put("avatar", "http://oss.xunshun.net/LOGO.jpg");
							map.put("nickname", (int) (Math.random() * (9999999 - 1000000) + 1000000));
							list.add(map);
						}
					}
				}
			}
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

	public static void main(String[] args) {

	}

	/**
	 * ?????????????????????
	 * type 1?????????????????? 2??????????????????
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "?????????????????????type 1?????????????????? 2??????????????????")
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
	 *  ???????????????
	 */
	@ApiOperation(value = "???????????????")
	@PostMapping("add_black_merch")
	public JsonResult addBlackMerch(HttpServletRequest request) {
		JsonResult jsonResult=new JsonResult();

		try {
			String merchid=request.getParameter("merchid");
			String memberid=request.getParameter("memberid");
			if(StringUtils.isEmpty(merchid)||StringUtils.isEmpty(memberid)) {
				jsonResult.setCode(JsonResult.ERROR);
				jsonResult.setMsg("??????id????????????id??????");
				return jsonResult;
			}

			Map< String, Object> map=new HashMap<String, Object>();
			map.put("memberid", memberid);
			map.put("merchid", merchid);
			map.put("type",1);
			int count=lmMerchBlackService.findExistMerch(map);

			if(count>0) {
				jsonResult.setCode(JsonResult.ERROR);
				jsonResult.setMsg("????????????");
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
	 *  ?????? ?????????
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
	 * @return ???????????????????????????
	 */
	@RequestMapping("check_open")
	@ResponseBody
	public JsonResult checkOpen(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		String merchid = request.getParameter("id");
		if (merchid == null) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("id??????");
			return jsonResult;
		}
		LmMerchInfo info=lmMerchInfoService.findById(merchid);
		if(info==null) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("???????????????");
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
	 * @return ???????????????
	 */
	@RequestMapping("do_open")
	@ResponseBody
	public JsonResult doOpen(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		String merchid = request.getParameter("id");
		if (merchid == null) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("id??????");
			return jsonResult;
		}
		LmMerchInfo info=lmMerchInfoService.findById(merchid);
		if(info==null) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("???????????????");
			return jsonResult;
		}else {
			if(info.getState()==0) {
				jsonResult.setCode(jsonResult.ERROR);
				jsonResult.setMsg("?????????");
				return jsonResult;
			}else if(info.getState()==1){
				info.setIsopen(1);
				lmMerchInfoService.updateService(info);
				jsonResult.setCode(jsonResult.SUCCESS);
				return jsonResult;
			}else if(info.getState()==2){
				jsonResult.setCode(jsonResult.ERROR);
				jsonResult.setMsg("?????????");
				return jsonResult;
			}
		}

		return jsonResult;
	}
	
	
	/**
	 * @return
	 *  ????????????????????????
	 */
	@RequestMapping("get_merchinfo")
	@ResponseBody
	public JsonResult getMerchinfo(HttpServletRequest request) {
		JsonResult jsonResult=new JsonResult();
		if (StringUtils.isEmpty(request.getParameter("id"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("id??????");
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
				//??????????????????
				/*List<LmMemberFollow> merchfollowlist = lmMemberFollowService.findByMerchidAndType(1,lmMerchInfo.getId());
				if(merchfollowlist!=null&&merchfollowlist.size()>0){
					lmMerchInfo.setFocusnum(merchfollowlist.size());
				}*/
				if(lmMerchInfo.getScore()==null){
					lmMerchInfo.setScore(0.0);
				}
				//??????????????????
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
				LmLive lmLive = lmMerchLiveService.findLiveByMerchid(Integer.parseInt(request.getParameter("id")));
				if(lmLive!=null){
					res.put("lmLiveImg", lmLive.getImg());
				}else {
					res.put("lmLiveImg", null);
				}
				res.put("merBgImg", lmMerchInfo.getBg_image());
				jsonResult.setData(res);
				jsonResult.setCode(jsonResult.SUCCESS);
			}
			else
			{
				jsonResult.setMsg("????????????");
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
	 * ????????????
	 * @return
	 */
	@ApiOperation(value = "????????????")
	@PostMapping("impression")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "merchid", value = "??????id", defaultValue = "0", required = true),
			@ApiImplicitParam(name = "type", value = "??????1??????2??????", defaultValue = "1", required = true)
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
			//????????????
			//??????????????????
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
					//????????????
					returnmap.put("refund_address",lmMerchInfo.getRefund_address());
					returnmap.put("createtime", DateUtils.sdf_yMdHms.format(lmMerchInfo.getCreate_at()));
					returnmap.put("description",lmMerchInfo.getDescription());
					returnmap.put("deposit",lmMerchInfo.getDeposit());
				}else{
					//????????????
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
	 * ????????????
	 * @return
	 */
	@ApiOperation(value = "????????????")
	@PostMapping("withdrawalapply")
	@Transactional
	public JsonResult withdrawalapply(@ApiParam(name = "merchid", value = "??????id", required = true)@RequestParam(required = true,defaultValue = "1") String merchid,
									  @ApiParam(name = "money", value = "??????", required = true)@RequestParam(required = true,defaultValue = "1") String money,
									  @ApiParam(name = "type", value = "????????????", required = true)@RequestParam(required = true,defaultValue = "1") String type,
									  @ApiParam(name = "card", value = "????????????", required = true)@RequestParam(required = true,defaultValue = "1") String card,
									  @ApiParam(name = "name", value = "??????", required = true)@RequestParam(required = true,defaultValue = "1") String name
									  ){
		JsonResult jsonResult = new JsonResult();
		jsonResult.setCode(JsonResult.SUCCESS);
		try {
			//??????????????????
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
				//??????????????????????????????
				if(new BigDecimal(money).compareTo(new BigDecimal(withdraw_commission_free_min)) > -1 && new BigDecimal(money).compareTo(new BigDecimal(withdraw_commission_free_max)) < 1){
					lmWithdrawal.setPoundage(new BigDecimal(withdraw_commission));
					lmWithdrawal.setRealmoney(new BigDecimal(money).subtract(new BigDecimal(withdraw_commission)));
				}else{
					lmWithdrawal.setPoundage(new BigDecimal(0));
					lmWithdrawal.setRealmoney(new BigDecimal(money));
				}
				lmWithdrawalService.insertservice(lmWithdrawal);
				//????????????
				LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(merchid);
				lmMerchInfo.setCredit(lmMerchInfo.getCredit().subtract(new BigDecimal(money)));
				lmMerchInfoService.updateService(lmMerchInfo);
			}else{
				jsonResult.setCode(JsonResult.ERROR);
				jsonResult.setMsg("???????????????????????????????????????");
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
	 *  ?????????????????????
	 */
	@ApiOperation(value = "?????????????????????")
	@PostMapping("admin_list")
	public JsonResult setAdmin(HttpServletRequest request,
							   @ApiParam(name = "merchid", value = "??????id",defaultValue = "", required = true)@RequestParam(required = true) String merchid) {
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
	 *  ?????????????????????
	 */
	@ApiOperation(value = " ")
	@PostMapping("set_admin")
	public JsonResult setAdmin(HttpServletRequest request,
							   @ApiParam(name = "merchid", value = "??????id",defaultValue = "", required = true)@RequestParam(required = true) String merchid,
							   @ApiParam(name = "mobile", value = "???????????????",defaultValue = "", required = true)@RequestParam(required = true) String mobile) {
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
				jsonResult.setMsg("??????????????????");
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
	 *  ?????????????????????
	 */
	@ApiOperation(value = "?????????????????????")
	@PostMapping("delete_admin")
	public JsonResult deleteAdmin(HttpServletRequest request,
								  @ApiParam(name = "merchid", value = "??????id",defaultValue = "", required = true)@RequestParam(required = true) String merchid,
								  @ApiParam(name = "memberid", value = "???????????????id",defaultValue = "", required = true)@RequestParam(required = true) String memberid) {
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
	 *  ??????????????????
	 */
	@ApiOperation(value = "??????????????????")
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
		CheckTextAPI checkTextAPI =new CheckTextAPI();
		String access_token = GetAuthService.getAuth(CheckTextAPI.apiKey,CheckTextAPI.secretKey);
		try {
			LmLive lmLive = lmLiveService.findbyId(liveid);
			if(lmLive==null){
				jsonResult.setCode(JsonResult.ERROR);
				jsonResult.setMsg("??????????????????");
				return jsonResult;
			}else{
				if(StringUtils.isEmpty(lmLive.getLivegroupid())){
					jsonResult.setCode(JsonResult.ERROR);
					jsonResult.setMsg("?????????????????????");
					return jsonResult;
				}
			}
			//??????????????????????????????
			List<LmShareGood> oldgood = goodService.findshareGoodByLiveid(liveid);
			if(oldgood!=null&&oldgood.size()>0){
				jsonResult.setCode(JsonResult.ERROR);
				jsonResult.setMsg("??????????????????????????????????????????");
				return jsonResult;
			}
			HttpJsonResult check =checkTextAPI.check(name,access_token);
			if(check.getCode()!=200){
				jsonResult.setCode(JsonResult.ERROR);
				jsonResult.setMsg(check.getMsg());
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
			//???????????????app
			HttpClient httpClient = new HttpClient();
			httpClient.sendgroup(lmLive.getLivegroupid(),"?????????????????????",2);
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
	 *  ????????????????????????
	 */
	@ApiOperation(value = "????????????????????????")
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
									  @RequestParam(value = "buyway",required = false) String buyway,
									  @RequestParam(value = "img",required = true) String img) {
		JsonResult jsonResult=new JsonResult();
		CheckTextAPI checkTextAPI =new CheckTextAPI();
		String access_token = GetAuthService.getAuth(CheckTextAPI.apiKey,CheckTextAPI.secretKey);
		try {
			LmLive lmLive = lmLiveService.findbyId(liveid+"");
			if(lmLive==null){
				jsonResult.setCode(JsonResult.ERROR);
				jsonResult.setMsg("??????????????????");
				return jsonResult;
			}else{
				if(StringUtils.isEmpty(lmLive.getLivegroupid())){
					jsonResult.setCode(JsonResult.ERROR);
					jsonResult.setMsg("?????????????????????");
					return jsonResult;
				}
			}
			HttpJsonResult check =checkTextAPI.check(name,access_token);
			if(check.getCode()!=200){
				jsonResult.setCode(JsonResult.ERROR);
				jsonResult.setMsg(check.getMsg());
				return jsonResult;
			}
			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
			if(!StringUtils.isEmpty(buyway)){
				livedGood.setBuyway(Integer.parseInt(buyway));
			}
			if(!StringUtils.isEmpty(starttime)){
				System.out.println(starttime);
				Date parse = dateFormat.parse(starttime);
				livedGood.setStarttime(parse);
			}
			if(!StringUtils.isEmpty(delaytime)){
				livedGood.setDelaytime(Integer.parseInt(delaytime));
			}
			if(!StringUtils.isEmpty(endtime)){
				System.out.println(endtime);
				Date parse = dateFormat.parse(endtime);
				livedGood.setEndtime(parse);
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
			//???????????????app
			HttpClient httpClient = new HttpClient();
			if(type==0){
				httpClient.sendgroup(lmLive.getLivegroupid(),"???????????????????????????",5);
			}
			if(type==1){
					httpClient.sendgroup(lmLive.getLivegroupid(), "????????????????????????", 6);
			}
			if(type==2) {
					httpClient.sendgroup(lmLive.getLivegroupid(), livedGood.getTomemberid() + "", 4);
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
	 *  ??????????????????
	 */
	@ApiOperation(value = "??????????????????")
	@PostMapping("pullgood")
	public JsonResult pullgood(HttpServletRequest request,
									  @RequestParam(value = "type",required = true) int type,
									  @RequestParam(value = "liveid",required = true) int liveid){
		JsonResult jsonResult=new JsonResult();
		try {
			LmLive lmLive = lmLiveService.findbyId(liveid+"");
			HttpClient httpClient = new HttpClient();
			if(type==0){
				httpClient.sendgroup(lmLive.getLivegroupid(),"???????????????????????????",12);
			}else if(type==1){
				httpClient.sendgroup(lmLive.getLivegroupid(), "????????????????????????", 13);
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
	 *  ??????????????????
	 */
	@ApiOperation(value = "??????????????????")
	@PostMapping("sharelist")
	public JsonResult sharelist(HttpServletRequest request,
								  @ApiParam(name = "merchid", value = "??????id",defaultValue = "", required = true)@RequestParam(required = true) String merchid,
								  @ApiParam(name = "type", value = "1?????????2?????????",defaultValue = "", required = true)@RequestParam(required = true) String type) {
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
     *  ??????????????????
     */
    @ApiOperation(value = "??????????????????")
    @PostMapping("sharedetail")
    public JsonResult sharedetail(HttpServletRequest request,
                                @ApiParam(name = "orderid", value = "?????????",defaultValue = "", required = true)@RequestParam(required = true) String orderid){
        JsonResult jsonResult=new JsonResult();
        try {
        	Map<String,Object> returnmap = new HashMap<>();
        	//?????????????????????
			LmOrder lmOrder = lmOrderService.findByOrderId(orderid);
			if(lmOrder!=null){
				returnmap.put("id",lmOrder.getId());
				returnmap.put("orderno",lmOrder.getOrderid());
				//????????????????????????
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
						returnmap.put("status","?????????");
					}else{
						returnmap.put("status","?????????");
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
				//???????????????
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
						map.put("status","?????????");
					}else{
						map.put("status","?????????");
					}
					if(order.getStatus().equals("0")){
						map.put("orderstatus","?????????");
					}else if(order.getStatus().equals("1")){
						map.put("orderstatus","?????????");
					}else if(order.getStatus().equals("2")){
						map.put("orderstatus","?????????");
					}else{
						map.put("orderstatus","?????????");
					}
					LmMember lmMember = lmMemberService.findById(order.getMemberid()+"");
					if(lmMember!=null){
						map.put("username",lmMember.getMobile());
					}
					listinfo.add(map);
				}
				returnmap.put("childorderinfo",listinfo);
				//??????????????????
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
