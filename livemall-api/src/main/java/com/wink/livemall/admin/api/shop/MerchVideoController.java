package com.wink.livemall.admin.api.shop;

import java.math.BigDecimal;
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
import com.wink.livemall.goods.dto.Good;
import com.wink.livemall.goods.dto.LmGoodAuction;
import com.wink.livemall.goods.service.GoodService;
import com.wink.livemall.goods.service.MerchGoodService;
import com.wink.livemall.member.dto.LmMember;
import com.wink.livemall.member.dto.LmMemberLog;
import com.wink.livemall.member.service.LmMemberService;
import com.wink.livemall.merch.dto.LmMerchInfo;
import com.wink.livemall.order.dto.LmExpress;
import com.wink.livemall.order.dto.LmOrder;
import com.wink.livemall.order.dto.LmOrderRefundLog;
import com.wink.livemall.order.service.LmMerchOrderService;
import com.wink.livemall.order.service.LmOrderExpressService;
import com.wink.livemall.order.service.LmOrderService;
import com.wink.livemall.video.dto.LmVideoCategoary;
import com.wink.livemall.video.dto.LmVideoCore;
import com.wink.livemall.video.service.LmVideoCategoaryService;
import com.wink.livemall.video.service.LmVideoCoreService;

@Controller
@RequestMapping("merchvideo")
public class MerchVideoController {

	Logger logger = LogManager.getLogger(GoodController.class);

	@Autowired
	private MerchGoodService merchGoodService;

	@Autowired
	private GoodService goodService;

	@Autowired
	private LmVideoCoreService lmVideoCoreService;

	@Autowired
	private LmVideoCategoaryService lmVideoCategoaryService;

	
	
	/**
	 * 
	 * 
	 * @return
	 */
	@RequestMapping("get_video_list")
	@ResponseBody
	public JsonResult findPage(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
	
		if (StringUtils.isEmpty(request.getParameter("userid"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("userid为空");
			return jsonResult;
		}
		
		try {
			Map<String, String> params = new HashMap<>();
			params.put("pagesize", request.getParameter("pagesize"));
			params.put("pageindex", request.getParameter("pageindex"));
			params.put("userid", request.getParameter("userid"));
			params.put("category", request.getParameter("category"));
			List<LmVideoCore> list=lmVideoCoreService.findPage(params);
			jsonResult.setCode(jsonResult.SUCCESS);
			Map<String, Object> res = new HashMap<String, Object>();
			res.put("pageindex", request.getParameter("pageindex"));
			res.put("pagesize", request.getParameter("pagesize"));
			res.put("list", list);
			jsonResult.setData(res);
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}

		return jsonResult;
	}

	/**
	 * 搜索商品
	 * @param request
	 * @return
	 */
	@RequestMapping("ser_goods")
	@ResponseBody
	public JsonResult getLiveGoods(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		if (StringUtils.isEmpty(request.getParameter("merchid"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("merchid为空");
			return jsonResult;
		}
		if (StringUtils.isEmpty(request.getParameter("keyword"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("搜索内容为空");
			return jsonResult;
		}
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("merchid", request.getParameter("merchid"));
			params.put("keyword", request.getParameter("keyword"));
			System.out.println(params.toString());
			List<Map<String, Object>> list = merchGoodService.findListByCondition(params);
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
	 * 添加视频
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("add_video")
	@ResponseBody
	public JsonResult addVideo(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		VerifyFields ver = new VerifyFields();
		if (StringUtils.isEmpty(request.getParameter("userid"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("userid为空");
			return jsonResult;
		}
		try {
			String[] fields = { "img", "video", "name", "content",  "category", "good_id"};
			Map<String, Object> res = ver.verifytoEntity(fields, request, "com.wink.livemall.video.dto.LmVideoCore");
			if ((int) res.get("error") == 0) {
				LmVideoCore videoCore=(LmVideoCore) res.get("entity");
				videoCore.setCreatuserid(Integer.parseInt(request.getParameter("userid")));
				videoCore.setCreattime(new Date());
			
				lmVideoCoreService.addService(videoCore);
				jsonResult.setCode(JsonResult.SUCCESS);
			}else {
				jsonResult.setMsg((String) res.get("msg"));
				jsonResult.setCode(JsonResult.ERROR);
			}
			
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}
		return jsonResult;
	}
	
	
	/**
	 * 修改视频
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("upd_video")
	@ResponseBody
	public JsonResult updVideo(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		VerifyFields ver = new VerifyFields();
		if (StringUtils.isEmpty(request.getParameter("id"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("id为空");
			return jsonResult;
		}
		try {
			String[] fields = { "img", "video", "name", "content",  "category", "good_id"};
			List<Map<String, String>> field_ = ver.checkEmptyField(fields, request);
			lmVideoCoreService.updateByFields(field_, request.getParameter("id"));
			jsonResult.setCode(JsonResult.SUCCESS);
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}
		return jsonResult;
	}


	/**
	 * 获取视频分类
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("get_video_cate")
	@ResponseBody
	public JsonResult getVideoCate(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		try {
			List<LmVideoCategoary> list = lmVideoCategoaryService.findtopcategory();
			Map<String, Object> res = new HashMap<String, Object>();
			res.put("list", list);
			jsonResult.setCode(jsonResult.SUCCESS);
			jsonResult.setData(res);
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}

		return jsonResult;
	}
	
	/**
	 * 删除视频
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("del_video")
	@ResponseBody
	public JsonResult delVideo(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		if (StringUtils.isEmpty(request.getParameter("id"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("id为空");
			return jsonResult;
		}
		try {
			lmVideoCoreService.deleteById(Integer.parseInt(request.getParameter("id")));
			jsonResult.setCode(jsonResult.SUCCESS);
		
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}

		return jsonResult;
	}

}
