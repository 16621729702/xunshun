package com.wink.livemall.admin.api.shop;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReactiveSetCommands.SRemCommand;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wink.livemall.admin.api.good.GoodController;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.VerifyFields;
import com.wink.livemall.coupon.dto.LmCoupons;
import com.wink.livemall.coupon.service.LmCouponsService;
import com.wink.livemall.coupon.service.LmMerchCouponsService;
import com.wink.livemall.goods.service.GoodService;
import com.wink.livemall.member.dto.LmMember;
import com.wink.livemall.member.dto.LmMemberLog;
import com.wink.livemall.member.service.LmMemberService;
import com.wink.livemall.merch.dto.LmMerchInfo;
import com.wink.livemall.merch.service.LmMerchInfoService;
import com.wink.livemall.order.dto.LmExpress;
import com.wink.livemall.order.dto.LmOrder;
import com.wink.livemall.order.dto.LmOrderRefundLog;
import com.wink.livemall.order.service.LmMerchOrderService;
import com.wink.livemall.order.service.LmOrderExpressService;
import com.wink.livemall.order.service.LmOrderService;

@Controller
@RequestMapping("merchcoupon")
public class MerchCouponController {

	Logger logger = LogManager.getLogger(GoodController.class);

	@Autowired
	private LmMerchCouponsService lmMerchCouponsService;

	@Autowired
	private LmMerchInfoService lmMerchInfoService;

	/**
	 * 获取所有有效的优惠券设置记录
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("get_coupon_list")
	@ResponseBody
	public JsonResult findAll(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		// 判断商户是否存在可用
		String merchid = request.getParameter("merch_id");
		
		try {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date now = new Date();
			Map<String, String> params = new HashMap<String, String>();
			params.put("date", sf.format(now));
			params.put("merch_id", merchid);
			List<Map<String, Object>> list = lmMerchCouponsService.findEnabled(params);
			if(list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map=list.get(i);
					Date start=(Date) map.get("start_date");
					Date end=(Date) map.get("end_date");
					map.put("start_date", sf.format(start));
					map.put("end_date", sf.format(end));
					
				}
			}
			Map<String, Object> res = new HashMap<String, Object>();
			res.put("list", list);
			jsonResult.setData(res);
			jsonResult.setCode(jsonResult.SUCCESS);
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}

		return jsonResult;
	}

	/**
	 * 添加优惠券 每个店铺最多三种优惠券 分批次发放 发放后要等优惠券全部消耗完获取优惠券过期之后才能重新发放 每次添加 判断优惠券是否过期 是否领完
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("add_coupon")
	@ResponseBody
	public JsonResult addCoupon(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		VerifyFields ver = new VerifyFields();

		// 判断商户是否存在可用
		String merchid = request.getParameter("merch_id");
		if (merchid == null) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("merch_id为空");
			return jsonResult;
		}
		int fg = lmMerchInfoService.checkMerchEnable(merchid);
		if (fg == 0) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("商户不存在或者被禁用");
			return jsonResult;
		}
		try {

			String[] fields = { "num", "rate", "useprice", "start_date", "end_date", "merch_id" };
			Map<String, Object> res = ver.verifytoEntity(fields, request, "com.wink.livemall.coupon.dto.LmCoupons");
			if ((int) res.get("error") == 0) {
	
				BigDecimal useprice=new BigDecimal(request.getParameter("useprice"));
				if (useprice.compareTo(new BigDecimal(0))==-1||useprice.compareTo(new BigDecimal(0))==0) {
					jsonResult.setCode(jsonResult.ERROR);
					jsonResult.setMsg("使用价格不能小于0");
					return jsonResult;
				}
				BigDecimal rate=new BigDecimal(request.getParameter("rate"));
			
				if (rate.compareTo(new BigDecimal(0))==-1||rate.compareTo(new BigDecimal(0))==0) {
					jsonResult.setCode(jsonResult.ERROR);
					jsonResult.setMsg("折扣不能小于0");
					return jsonResult;
				}
				int num = Integer.parseInt(request.getParameter("num"));
				if (num <= 0) {
					jsonResult.setCode(jsonResult.ERROR);
					jsonResult.setMsg("折数量不能小于0");
					return jsonResult;
				}
				String start_date = request.getParameter("start_date");
				String end_date = request.getParameter("end_date");
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date now = new Date();
				Date start = sf.parse(start_date);
				Date end = sf.parse(end_date);
				if (start.after(end)) {
					jsonResult.setCode(jsonResult.ERROR);
					jsonResult.setMsg("开始时间大于结束时间");
					return jsonResult;
				}
				if (now.after(end)) {
					jsonResult.setCode(jsonResult.ERROR);
					jsonResult.setMsg("结束时间小于当前时间");
					return jsonResult;
				}
				Map<String, String> params = new HashMap<String, String>();
				params.put("date", sf.format(now));
				params.put("merch_id", merchid);
				int coupon_num = lmMerchCouponsService.countEnabled(params);
				if (coupon_num >= 3) {
					jsonResult.setCode(jsonResult.ERROR);
					jsonResult.setMsg("超过可添加数量");
					return jsonResult;
				}
				LmCoupons coupons = (LmCoupons) res.get("entity");

				coupons.setLeft_num(num);
				coupons.setCreated_at(new Date());
				lmMerchCouponsService.insertService(coupons);
				jsonResult.setCode(JsonResult.SUCCESS);
			} else {
				jsonResult.setMsg((String) res.get("msg"));
				jsonResult.setCode(JsonResult.ERROR);
				return jsonResult;
			}

			jsonResult.setCode(jsonResult.SUCCESS);
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}

		return jsonResult;
	}

}
