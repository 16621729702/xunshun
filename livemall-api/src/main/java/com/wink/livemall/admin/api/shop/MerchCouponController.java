package com.wink.livemall.admin.api.shop;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.wink.livemall.admin.util.*;
import com.wink.livemall.coupon.dto.LmConpouMember;
import com.wink.livemall.coupon.service.LmCouponMemberService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.omg.CORBA.INTERNAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wink.livemall.admin.api.good.GoodController;
import com.wink.livemall.coupon.dto.LmCoupons;
import com.wink.livemall.coupon.service.LmCouponsService;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/merchCoupon")
public class MerchCouponController {

	Logger logger = LogManager.getLogger(GoodController.class);

	@Autowired
	private LmCouponsService lmCouponsService;
	@Resource
	private LmCouponMemberService lmCouponMemberService;



	@RequestMapping("/merchCouponList")
	@ResponseBody
	@ApiOperation(value = "商家优惠券列表",notes = "商家优惠券列表接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "merchId", value = "商家Id", dataType = "Integer",paramType = "query")
	})
	private HttpJsonResult<List<LmCoupons>> merchCouponList(HttpServletRequest request,Integer merchId) throws Exception {
		HttpJsonResult<List<LmCoupons> > jsonResult=new HttpJsonResult<>();

		try {

			List<LmCoupons> lmCouponsList = lmCouponsService.findMerchCouponByMId(merchId);

			jsonResult.setData(lmCouponsList);
			jsonResult.setCode(Errors.ok.getCode());
			jsonResult.setMessage(Errors.ok.getMsg());
			return jsonResult;

		} catch (Exception e) {
			throw e;
		}
	}



	@RequestMapping("/memberGetCouponList")
	@ResponseBody
	@ApiOperation(value = "用户领取优惠券列表",notes = "用户领取优惠券列表接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "couponId", value = "优惠券Id", dataType = "Integer",paramType = "query"),
			@ApiImplicitParam(name = "memberId", value = "用户Id", dataType = "Integer",paramType = "query")
	})
	private HttpJsonResult memberGetCouponList(HttpServletRequest request,Integer couponId, Integer memberId) throws Exception {
		HttpJsonResult jsonResult=new HttpJsonResult<>();

		try {

			insertLmCouponMember(couponId,memberId);

			jsonResult.setCode(Errors.ok.getCode());
			jsonResult.setMessage(Errors.ok.getMsg());
			return jsonResult;

		} catch (Exception e) {
			throw e;
		}
	}


	public void insertLmCouponMember(Integer couponId, Integer memberId){

		LmCoupons lmCoupons = lmCouponsService.findById(couponId.toString());

		LmConpouMember lmConpouMember = new LmConpouMember();
		lmConpouMember.setMemberId(memberId);
		lmConpouMember.setSellerId(lmCoupons.getSellerId());
		lmConpouMember.setCouponId(lmCoupons.getId());
		lmConpouMember.setCanUse(1);
		lmConpouMember.setReceiveTime(new Date());
		lmConpouMember.setOrderId(0);
		lmConpouMember.setUseStartTime(null);
		lmConpouMember.setUseEndTime(null);
		lmConpouMember.setCreateTime(new Date());
		lmConpouMember.setUpdateTime(new Date());
		lmConpouMember.setProductIds(null);
		lmCouponMemberService.insert(lmConpouMember);

	}



	@RequestMapping("/addCoupon")
	@ResponseBody
	@ApiOperation(value = "商家发布优惠券",notes = "商家发布优惠券接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "lmCouponsVO", value = "接收商家填写信息对象", dataType = "Object",paramType = "query")
	})
	private HttpJsonResult<JSONObject> addCoupon(HttpServletRequest request, LmCoupons lmCouponsVO) throws Exception {

		HttpJsonResult<JSONObject> jsonResult=new HttpJsonResult<>();

		try {
			insertOrUpdateLC(1,lmCouponsVO);
			jsonResult.setCode(Errors.ok.getCode());
			jsonResult.setMessage(Errors.ok.getMsg());
			return jsonResult;

		} catch (Exception e) {
			throw e;
		}
	}


	@RequestMapping("/delectCoupon")
	@ResponseBody
	@ApiOperation(value = "删除优惠券",notes = "删除优惠券接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "couponId", value = "优惠券Id", dataType = "Integer",paramType = "query")
	})
	private HttpJsonResult delectCoupon(HttpServletRequest request,Integer couponId) throws Exception {
		HttpJsonResult jsonResult=new HttpJsonResult<>();

		try {

			lmCouponsService.deleteService(couponId.toString());

			jsonResult.setCode(Errors.ok.getCode());
			jsonResult.setMessage(Errors.ok.getMsg());
			return jsonResult;

		} catch (Exception e) {
			throw e;
		}
	}


	@RequestMapping("/updateCoupon")
	@ResponseBody
	@ApiOperation(value = "修改优惠券",notes = "修改优惠券接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "lmCouponsVO", value = "接收商家填写信息对象", dataType = "Object",paramType = "query")
	})
	private HttpJsonResult<JSONObject> updateCoupon(HttpServletRequest request, LmCoupons lmCouponsVO) throws Exception {

		HttpJsonResult<JSONObject> jsonResult=new HttpJsonResult<>();

		try {
			insertOrUpdateLC(2,lmCouponsVO);
			jsonResult.setCode(Errors.ok.getCode());
			jsonResult.setMessage(Errors.ok.getMsg());
			return jsonResult;

		} catch (Exception e) {
			throw e;
		}
	}


	public void insertOrUpdateLC(Integer type,  LmCoupons lmCouponsVO){

		LmCoupons lmCoupons=null;

		if(type == 2){
			lmCoupons = lmCouponsService.findById(String.valueOf(lmCouponsVO.getId()));
		}else{
			lmCoupons =  new LmCoupons();
		}

		lmCoupons.setCreateTime(new Date());
		lmCoupons.setRemark(lmCouponsVO.getRemark());
		lmCoupons.setSendEndTime(lmCouponsVO.getSendEndTime());
		lmCoupons.setCouponValue(lmCouponsVO.getCouponValue());
		lmCoupons.setMinAmount(lmCouponsVO.getMinAmount());
		lmCoupons.setSellerId(lmCouponsVO.getSellerId());
		lmCoupons.setCouponName(lmCouponsVO.getCouponName());
		lmCoupons.setTotalLimitNum(lmCouponsVO.getTotalLimitNum());
		lmCoupons.setSendStartTime(lmCouponsVO.getSendStartTime());
		lmCoupons.setStatus(lmCouponsVO.getStatus());
		lmCoupons.setPersonLimitNum(lmCouponsVO.getPersonLimitNum());
		lmCoupons.setProductType(lmCouponsVO.getProductType());
		lmCoupons.setChannel(1);

		if(type == 1){
			lmCouponsService.insertService(lmCoupons);
		}else{
			lmCouponsService.updateService(lmCoupons);
		}
	}

}
