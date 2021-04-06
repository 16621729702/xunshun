package com.wink.livemall.admin.api.shop;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import com.alibaba.fastjson.JSONObject;
import com.wink.livemall.admin.util.*;
import com.wink.livemall.admin.util.filterUtils.CheckTextAPI;
import com.wink.livemall.admin.util.filterUtils.GetAuthService;
import com.wink.livemall.coupon.dto.LmConpouMember;
import com.wink.livemall.coupon.dto.vo.LmCouponsVo;
import com.wink.livemall.coupon.service.LmCouponMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wink.livemall.coupon.dto.LmCoupons;
import com.wink.livemall.coupon.service.LmCouponsService;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "优惠卷接口")
@RestController
@RequestMapping("/merchCoupon")
public class MerchCouponController {

	Logger logger = LogManager.getLogger(MerchCouponController.class);

	@Autowired
	private LmCouponsService lmCouponsService;
	@Autowired
	private LmCouponMemberService lmCouponMemberService;


	@RequestMapping("/merchCouponList")
	@ResponseBody
	@ApiOperation(value = "商家优惠券列表",notes = "商家优惠券列表接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "merchId", value = "商家Id", dataType = "Integer",paramType = "query"),
			@ApiImplicitParam(name = "status", value = "0、开启 1、关闭 ", dataType = "Integer",paramType = "query"),
			@ApiImplicitParam(name = "page", value = "页码 ", dataType = "Integer",paramType = "query"),
			@ApiImplicitParam(name = "pageSize", value = "页码 ", dataType = "Integer",paramType = "query")
	})
	private HttpJsonResult<List<LmCoupons>> merchCouponList(HttpServletRequest request, Integer merchId, Integer status, Integer page, Integer pageSize) throws Exception {
		HttpJsonResult<List<LmCoupons> > jsonResult=new HttpJsonResult<>();
		try {
			List<LmCoupons> lmCouponsList = lmCouponsService.findMerchCoupon(merchId,status);
			List list = PageUtil.startPage(lmCouponsList, page, pageSize);
			jsonResult.setData(list);
			jsonResult.setCode(Errors.SUCCESS.getCode());
			jsonResult.setMsg(Errors.SUCCESS.getMsg());
			return jsonResult;
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(Errors.ERROR.getCode());
			logger.error(e.getMessage());
		}
		return jsonResult;
	}

	@RequestMapping("/memberCouponList")
	@ResponseBody
	@ApiOperation(value = "用户优惠券列表",notes = "用户优惠券列表接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "type", value = "类型:0 可使用，1 已使用，2 已过期",required =true,  dataType = "Integer",paramType = "query"),
			@ApiImplicitParam(name = "page", value = "页码 ", dataType = "Integer",paramType = "query",required =false),
			@ApiImplicitParam(name = "pageSize", value = "页码 ", dataType = "Integer",paramType = "query",required =false)
	})
	private HttpJsonResult memberCouponList(HttpServletRequest request,Integer merchId,Integer type, Integer page, Integer pageSize) throws Exception {
		HttpJsonResult   jsonResult=new HttpJsonResult<>();
		String header = request.getHeader("Authorization");
		String userId="";
		if (!StringUtils.isEmpty(header)) {
			if(!StringUtils.isEmpty(TokenUtil.getUserId(header))){
				userId = TokenUtil.getUserId(header);
			}else{
				jsonResult.setCode(Errors.TOKEN_PAST.getCode());
				jsonResult.setMsg(Errors.TOKEN_PAST.getMsg());
				return jsonResult;
			}
		}
		try {
			List<Map<String, Object>>  lmCouponsList = lmCouponsService.findMemberCouponByMId(Integer.parseInt(userId),merchId,type);
			List list = PageUtil.startPage(lmCouponsList, page, pageSize);
			jsonResult.setData(list);
			jsonResult.setCode(Errors.SUCCESS.getCode());
			jsonResult.setMsg(Errors.SUCCESS.getMsg());
			return jsonResult;
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(Errors.ERROR.getCode());
			logger.error(e.getMessage());
		}
		return jsonResult;
	}




	@RequestMapping("/memberGetCouponList")
	@ResponseBody
	@ApiOperation(value = "用户领取优惠券",notes = "用户领取优惠券接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "couponId", value = "优惠券Id", dataType = "Integer",paramType = "query")
	})
	private HttpJsonResult memberGetCouponList(HttpServletRequest request,Integer couponId) throws Exception {
		HttpJsonResult jsonResult=new HttpJsonResult<>();
		String header = request.getHeader("Authorization");
		String userId="";
		if (!StringUtils.isEmpty(header)) {
			if(!StringUtils.isEmpty(TokenUtil.getUserId(header))){
				userId = TokenUtil.getUserId(header);
			}else{
				jsonResult.setCode(Errors.TOKEN_PAST.getCode());
				jsonResult.setMsg(Errors.TOKEN_PAST.getMsg());
				return jsonResult;
			}
		}
		System.out.print("++++++++++++="+userId);
		try {
			insertLmCouponMember(couponId,Integer.parseInt(userId),jsonResult);
			return jsonResult;
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(Errors.ERROR.getCode());
			logger.error(e.getMessage());
		}
		return jsonResult;
	}


	public HttpJsonResult insertLmCouponMember(Integer couponId, Integer memberId,HttpJsonResult jsonResult){

		LmCoupons lmCoupons = lmCouponsService.findById(couponId.toString());

		List<LmConpouMember> lmConpouMembers = lmCouponMemberService.findByMemberId(memberId,couponId);
		if( null!=lmConpouMembers && lmConpouMembers.size()>0){
			LmConpouMember isCanUse =lmConpouMembers.get(0);
			System.out.print("++++++++"+isCanUse.getCanUse());
			if(isCanUse.getCanUse()==1){
				jsonResult.setCode(Errors.is_can_use.getCode());
				jsonResult.setMsg(Errors.is_can_use.getMsg());
				return jsonResult;
			}
		}
		if(lmCoupons.getPersonLimitNum() <= lmConpouMembers.size()){
			jsonResult.setCode(Errors.get_coupon_error.getCode());
			jsonResult.setMsg(Errors.get_coupon_error.getMsg());
			return jsonResult;
		}
		Integer receivedNum = lmCoupons.getReceivedNum();
		if(receivedNum == null){
			receivedNum = 0;
		}
		if(lmCoupons.getTotalLimitNum() >= receivedNum){
			lmCoupons.setReceivedNum(++receivedNum);
			lmCouponsService.updateService(lmCoupons);
		}else {
			jsonResult.setCode(Errors.get_coupon_error.getCode());
			jsonResult.setMsg(Errors.get_coupon_error.getMsg());
			return jsonResult;
		}

		LmConpouMember lmConpouMember = new LmConpouMember();
		lmConpouMember.setMemberId(memberId);
		lmConpouMember.setSellerId(lmCoupons.getSellerId());
		lmConpouMember.setCouponId(lmCoupons.getId());
		lmConpouMember.setCanUse(1);
		lmConpouMember.setReceiveTime(new Date());
		lmConpouMember.setOrderId(0);
		lmConpouMember.setUseStartTime(lmCoupons.getUseStartTime());
		lmConpouMember.setUseEndTime(lmCoupons.getUseEndTime());
		lmConpouMember.setCreateTime(new Date());
		lmConpouMember.setUpdateTime(new Date());
		lmConpouMember.setProductIds(null);
		lmCouponMemberService.insert(lmConpouMember);


		jsonResult.setCode(Errors.SUCCESS.getCode());
		jsonResult.setMsg(Errors.SUCCESS.getMsg());
		return jsonResult;

	}



	@RequestMapping("/addCoupon")
	@ResponseBody
	@ApiOperation(value = "商家发布优惠券",notes = "商家发布优惠券接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "lmCouponsVO", value = "接收商家填写信息对象", dataType = "Object",paramType = "query")
	})
	private HttpJsonResult<JSONObject> addCoupon(HttpServletRequest request, LmCoupons lmCouponsVO,HttpJsonResult jsonResult) throws Exception {
		try {
			insertOrUpdateLC(1,lmCouponsVO,jsonResult);
			return jsonResult;

		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(Errors.ERROR.getCode());
			logger.error(e.getMessage());
		}
		return jsonResult;
	}


	@RequestMapping("/delectCoupon")
	@ResponseBody
	@ApiOperation(value = "删除优惠券",notes = "删除优惠券接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "couponId", value = "优惠券Id", dataType = "Integer",paramType = "query")
	})
	private HttpJsonResult delectCoupon(HttpServletRequest request, Integer couponId) throws Exception {
		HttpJsonResult jsonResult=new HttpJsonResult<>();

		try {
			LmCoupons lmCoupons = lmCouponsService.findById(couponId.toString());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date useEndTimeDate = lmCoupons.getUseEndTime();
			long userEndTtime = useEndTimeDate.getTime();
			Date nowDate=new Date();
			nowDate.setTime(nowDate.getTime()+60*60l*1000);
			long nowTime = nowDate.getTime();

			if(userEndTtime-nowTime>0){
				//优惠券未过期
				jsonResult.setCode(Errors.coupon_error.getCode());
				jsonResult.setMsg(Errors.coupon_error.getMsg());
				return jsonResult;
			}else if(userEndTtime-nowTime<0){
				//优惠券已过期
				lmCoupons.setStatus(2);
				lmCouponsService.updateService(lmCoupons);
			}

			jsonResult.setCode(Errors.SUCCESS.getCode());
			jsonResult.setMsg(Errors.SUCCESS.getMsg());
			return jsonResult;

		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(Errors.ERROR.getCode());
			logger.error(e.getMessage());
		}
		return jsonResult;
	}


	@RequestMapping("/updateCoupon")
	@ResponseBody
	@ApiOperation(value = "修改优惠券",notes = "修改优惠券接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "lmCouponsVO", value = "接收商家填写信息对象", dataType = "Object",paramType = "query")
	})
	private HttpJsonResult<JSONObject> updateCoupon(HttpServletRequest request, LmCoupons lmCouponsVO,HttpJsonResult jsonResult) throws Exception {
		try {
			insertOrUpdateLC(2,lmCouponsVO,jsonResult);
			return jsonResult;
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(Errors.ERROR.getCode());
			logger.error(e.getMessage());
		}
		return jsonResult;
	}


	public void insertOrUpdateLC(Integer type,  LmCoupons lmCouponsVO,HttpJsonResult jsonResult){
         Date date =new Date();
		CheckTextAPI checkTextAPI =new CheckTextAPI();
		String access_token = GetAuthService.getAuth(CheckTextAPI.apiKey,CheckTextAPI.secretKey);
		LmCoupons lmCoupons=null;
		if(type == 2){
			lmCoupons = lmCouponsService.findById(String.valueOf(lmCouponsVO.getId()));
		}else{
			lmCoupons =  new LmCoupons();
		}
		if(!StringUtils.isEmpty(lmCouponsVO.getSellerId())){
			lmCoupons.setSellerId(lmCouponsVO.getSellerId());
		}else {
			jsonResult.setCode(Errors.ERROR.getCode());
			jsonResult.setMsg("商户ID"+"值为空");
			return;
		}
		if(!StringUtils.isEmpty(lmCouponsVO.getCouponName())){
			lmCoupons.setCouponName(lmCouponsVO.getCouponName());
			com.wink.livemall.goods.utils.HttpJsonResult check =checkTextAPI.check(lmCouponsVO.getCouponName(),access_token);
			if(check.getCode()!=200){
				jsonResult.setCode(Errors.ERROR.getCode());
				jsonResult.setMsg(check.getMsg());
				return;
			}
		}else {
			jsonResult.setCode(Errors.ERROR.getCode());
			jsonResult.setMsg("优惠券名称"+"值为空");
			return;
		}
		if(!StringUtils.isEmpty(lmCouponsVO.getCouponValue())){
			lmCoupons.setCouponValue(lmCouponsVO.getCouponValue());
		}else {
			jsonResult.setCode(Errors.ERROR.getCode());
			jsonResult.setMsg("优惠券面值"+"值为空");
			return;
		}
		if(!StringUtils.isEmpty(lmCouponsVO.getMinAmount())){
			lmCoupons.setMinAmount(lmCouponsVO.getMinAmount());
		}else {
			jsonResult.setCode(Errors.ERROR.getCode());
			jsonResult.setMsg("最低适用价格"+"值为空");
			return;
		}
		if(!StringUtils.isEmpty(lmCouponsVO.getUseStartTime())){
			lmCoupons.setUseStartTime(lmCouponsVO.getUseStartTime());
			if(date.getTime()<lmCouponsVO.getUseStartTime().getTime()){
				lmCoupons.setSendStartTime(date);
			}else {
				lmCoupons.setSendStartTime(lmCouponsVO.getUseStartTime());
			}
		}else {
			jsonResult.setCode(Errors.ERROR.getCode());
			jsonResult.setMsg("使用开始时间"+"值为空");
			return;
		}
		if(!StringUtils.isEmpty(lmCouponsVO.getUseEndTime())){
			lmCoupons.setUseEndTime(lmCouponsVO.getUseEndTime());
			lmCoupons.setSendEndTime(lmCouponsVO.getUseEndTime());
		}else {
			jsonResult.setCode(Errors.ERROR.getCode());
			jsonResult.setMsg("使用结束时间"+"值为空");
			return;
		}
		if(!StringUtils.isEmpty(lmCouponsVO.getPersonLimitNum())){
			lmCoupons.setPersonLimitNum(lmCouponsVO.getPersonLimitNum());
		}else {
			jsonResult.setCode(Errors.ERROR.getCode());
			jsonResult.setMsg("限领张数"+"值为空");
			return;
		}
		if(!StringUtils.isEmpty(lmCouponsVO.getTotalLimitNum())){
			lmCoupons.setTotalLimitNum(lmCouponsVO.getTotalLimitNum());
		}else {
			jsonResult.setCode(Errors.ERROR.getCode());
			jsonResult.setMsg("总卷数"+"值为空");
			return;
		}
		if(!StringUtils.isEmpty(lmCouponsVO.getRemark())){
			lmCoupons.setRemark(lmCouponsVO.getRemark());
			com.wink.livemall.goods.utils.HttpJsonResult check =checkTextAPI.check(lmCouponsVO.getCouponName(),access_token);
			if(check.getCode()!=200){
				jsonResult.setCode(Errors.ERROR.getCode());
				jsonResult.setMsg(check.getMsg());
				return;
			}
		}
		if(!StringUtils.isEmpty(lmCouponsVO.getProductType())){
			lmCoupons.setProductType(lmCouponsVO.getProductType());
		}else {
			lmCoupons.setProductType(3);
		}
		if(!StringUtils.isEmpty(lmCouponsVO.getGoodsType())){
			lmCoupons.setGoodsType(lmCouponsVO.getGoodsType());
		}else {
			lmCoupons.setGoodsType(2);
		}
		if(!StringUtils.isEmpty(lmCouponsVO.getCouponType())){
			lmCoupons.setCouponType(lmCouponsVO.getCouponType());
		}else {
			lmCoupons.setCouponType(0);
		}
		lmCoupons.setCreateTime(date);
		lmCoupons.setType(2);
		lmCoupons.setChannel(1);
		lmCoupons.setStatus(0);
		lmCoupons.setReceivedNum(0);
		if(type == 1){
			lmCouponsService.insertService(lmCoupons);
		}else{
			lmCouponsService.updateService(lmCoupons);
		}
		jsonResult.setCode(Errors.SUCCESS.getCode());
		jsonResult.setMsg(Errors.SUCCESS.getMsg());
	}


	@RequestMapping("/getNewCoupon")
	@ResponseBody
	@ApiOperation(value = "用户领取新人优惠券",notes = "用户领取新人优惠券接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "memberId", value = "用户Id", dataType = "Integer",paramType = "query")
	})
	private com.wink.livemall.utils.HttpJsonResult memberGetNewCouponList(HttpServletRequest request,Integer memberId) throws Exception {
		com.wink.livemall.utils.HttpJsonResult jsonResult=new com.wink.livemall.utils.HttpJsonResult();
		String header = request.getHeader("Authorization");
		String userId="";
		if (!StringUtils.isEmpty(header)) {
			if(!StringUtils.isEmpty(TokenUtil.getUserId(header))){
				userId = TokenUtil.getUserId(header);
			}else{
				jsonResult.setCode(Errors.TOKEN_PAST.getCode());
				jsonResult.setMsg(Errors.TOKEN_PAST.getMsg());
				return jsonResult;
			}
		}
		try {
			jsonResult =lmCouponsService.insertNewCouponService(Integer.parseInt(userId));
			return jsonResult;
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(Errors.ERROR.getCode());
			logger.error(e.getMessage());
		}
		return jsonResult;
	}


	@RequestMapping("/memberToMerchCouponList")
	@ResponseBody
	@ApiOperation(value = "用户领取商家优惠券列表",notes = "用户领取商家优惠券列表接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "merchId", value = "商户Id", dataType = "Integer",paramType = "query"),
			@ApiImplicitParam(name = "page", value = "页码 ", dataType = "Integer",paramType = "query"),
			@ApiImplicitParam(name = "pageSize", value = "页码 ", dataType = "Integer",paramType = "query")
	})
	private HttpJsonResult memberToMerchCouponList(HttpServletRequest request,Integer merchId ,Integer page, Integer pageSize) throws Exception {
		HttpJsonResult jsonResult=new HttpJsonResult();
		String header = request.getHeader("Authorization");
		String userId="";
		if (!StringUtils.isEmpty(header)) {
			if(!StringUtils.isEmpty(TokenUtil.getUserId(header))){
				userId = TokenUtil.getUserId(header);
			}else{
				jsonResult.setCode(Errors.TOKEN_PAST.getCode());
				jsonResult.setMsg(Errors.TOKEN_PAST.getMsg());
				return jsonResult;
			}
		}
		try {
			List<LmCouponsVo> lmCouponsList = lmCouponsService.findMerchCouponUsable(merchId,0,userId);
			List list = PageUtil.startPage(lmCouponsList, page, pageSize);
			jsonResult.setData(list);
			jsonResult.setCode(Errors.SUCCESS.getCode());
			jsonResult.setMsg(Errors.SUCCESS.getMsg());
			return jsonResult;
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(Errors.ERROR.getCode());
			logger.error(e.getMessage());
		}
		return jsonResult;
	}


	@RequestMapping("/orderCouponList")
	@ResponseBody
	@ApiOperation(value = "订单页面优惠券列表",notes = "订单页面优惠券列表接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "productPrice", value = "订单价格", dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "productType", value = "指定商品类型", dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "goodsType", value = "商品类型", dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "merId", value = "商家Id", dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "page", value = "页码 ", dataType = "Integer",paramType = "query"),
			@ApiImplicitParam(name = "pageSize", value = "页码 ", dataType = "Integer",paramType = "query")
	})
	private  HttpJsonResult orderCouponList(HttpServletRequest request,
											Integer memberId, String productPrice,
											String goodsType,String productType,String merId,
											Integer page, Integer pageSize){
		HttpJsonResult jsonResult=new HttpJsonResult();
		String header = request.getHeader("Authorization");
		String userId="";
		if (!StringUtils.isEmpty(header)) {
			if(!StringUtils.isEmpty(TokenUtil.getUserId(header))){
				userId = TokenUtil.getUserId(header);
			}else{
				jsonResult.setCode(Errors.TOKEN_PAST.getCode());
				jsonResult.setMsg(Errors.TOKEN_PAST.getMsg());
				return jsonResult;
			}
		}
		try {
			List<Map<String,Object>> lmCouponsList = lmCouponsService.orderCouponList(Integer.parseInt(userId),productPrice,goodsType,productType, merId);
			List list = PageUtil.startPage(lmCouponsList, page, pageSize);
			jsonResult.setData(list);
			jsonResult.setCode(Errors.SUCCESS.getCode());
			jsonResult.setMsg(Errors.SUCCESS.getMsg());
			return jsonResult;
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(Errors.ERROR.getCode());
			logger.error(e.getMessage());
		}
		return jsonResult;
	}


	@RequestMapping("/isEjectCoupon")
	@ResponseBody
	@ApiOperation(value = "是否弹出新人优惠券",notes = "是否弹出新人优惠券接口")
	@ApiImplicitParams({

	})
	private  com.wink.livemall.utils.HttpJsonResult isEjectCoupon(HttpServletRequest request){
		com.wink.livemall.utils.HttpJsonResult jsonResult=new com.wink.livemall.utils.HttpJsonResult();
		String header = request.getHeader("Authorization");
		String userId="";
		if (!StringUtils.isEmpty(header)) {
			if(!StringUtils.isEmpty(TokenUtil.getUserId(header))){
				userId = TokenUtil.getUserId(header);
			}else{
				jsonResult.setCode(Errors.TOKEN_PAST.getCode());
				jsonResult.setMsg(Errors.TOKEN_PAST.getMsg());
				return jsonResult;
			}
		}
		try {
			jsonResult = lmCouponsService.isEjectCoupon(Integer.parseInt(userId));
			return jsonResult;
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(Errors.ERROR.getCode());
			logger.error(e.getMessage());
		}
		return jsonResult;
	}




	@RequestMapping("/getCouponCentre")
	@ResponseBody
	@ApiOperation(value = "领券中心",notes = "领券中心接口")
	@ApiImplicitParams({
	})
	private HttpJsonResult getCouponCentre(HttpServletRequest request) throws Exception {
		HttpJsonResult jsonResult=new HttpJsonResult();
		try {
			Date date =new Date();
			List<LmCoupons> lmCoupons = lmCouponsService.findNewCouponByMId(3);
			List<LmCoupons> lmCouponList=new LinkedList<>();
			if(lmCoupons!=null&&lmCoupons.size()>0){
				for(LmCoupons lmCoupones:lmCoupons){
					if(lmCoupones.getStatus()==0){
						if(lmCoupones.getSendEndTime().getTime()>date.getTime()){
							lmCouponList.add(lmCoupones);
						}
					}
				}
			}
			if(lmCouponList!=null&&lmCouponList.size()>0){
				jsonResult.setData(lmCouponList);
			}else {
				List list =new ArrayList();
				jsonResult.setData(list);
			}
			jsonResult.setCode(Errors.SUCCESS.getCode());
			jsonResult.setMsg(Errors.SUCCESS.getMsg());
			return jsonResult;
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(Errors.ERROR.getCode());
			logger.error(e.getMessage());
		}
		return jsonResult;
	}

	@RequestMapping("/getNewCouponList")
	@ResponseBody
	@ApiOperation(value = "新人优惠券列表",notes = "新人优惠券列表接口")
	@ApiImplicitParams({
	})
	private HttpJsonResult getNewCouponList(HttpServletRequest request) throws Exception {
		HttpJsonResult jsonResult=new HttpJsonResult();
		try {
			List<LmCoupons> lmCoupons = lmCouponsService.findNewCouponList();
			if(lmCoupons!=null&&lmCoupons.size()>0){
				jsonResult.setData(lmCoupons);
			}else {
				jsonResult.setData(null);
			}
			jsonResult.setCode(Errors.SUCCESS.getCode());
			jsonResult.setMsg(Errors.SUCCESS.getMsg());
			return jsonResult;
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(Errors.ERROR.getCode());
			logger.error(e.getMessage());
		}
		return jsonResult;
	}


}
