package com.wink.livemall.admin.api.register;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.wink.livemall.merch.dao.LmMerchPriceLogDao;
import com.wink.livemall.merch.dto.LmMerchPriceLog;
import com.wink.livemall.merch.service.LmMerchInfoService;
import com.wink.livemall.merch.service.LmMerchPriceLogService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wink.livemall.admin.api.good.GoodController;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.VerifyFields;
import com.wink.livemall.merch.dto.LmMerchConfigs;
import com.wink.livemall.merch.dto.LmMerchInfo;
import com.wink.livemall.merch.service.LmMerchConfigsService;
import com.wink.livemall.merch.service.LmMerchRegisterService;

/**
 * @author Administrator
 *商户注册方法
 */
@Controller
@RequestMapping("register")
public class RegisterController {
	Logger logger = LogManager.getLogger(GoodController.class);
	
	@Autowired
	private LmMerchRegisterService lmMerchRegisterService;
	@Autowired
	private LmMerchInfoService lmMerchInfoService;
	@Autowired
	private LmMerchConfigsService lmMerchConfigsService;
	@Autowired
	private LmMerchPriceLogService lmMerchPriceLogService;
	
	/**
	 * 获取注册页面配置
	 * @param request
	 * @return
	 */
	@RequestMapping("get_info")
    @ResponseBody
	public JsonResult get_info(HttpServletRequest request) {
		JsonResult jsonResult=new JsonResult();
		
		try {
			LmMerchConfigs configs=lmMerchConfigsService.findByType("页面设置");
			String content=configs.getConfig();
			
			Gson gson=new Gson();
			Map<String, String> jsonmap=gson.fromJson(content, Map.class);
			jsonResult.setData(jsonmap);
		} catch (Exception e) {
			// TODO: handle exception
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg(e.getMessage());
		}
		
		return jsonResult;
	}
	
	
	/**
	 * 注册第一步
	 * 参数
	 * avatar头像 
	 * store_name店铺名称
	 * description店铺介绍
	 * weixin店铺维修号
	 * mobile联系手机号
	 * bg_image店铺背景图
	 * @return
	 */
	@RequestMapping("step1")
    @ResponseBody
	public JsonResult stp1(HttpServletRequest request) {
		JsonResult jsonResult=new JsonResult();
		jsonResult.setCode(jsonResult.ERROR);
		LmMerchInfo info=new LmMerchInfo();
		
		VerifyFields verify=new VerifyFields();
		String[] fields= {"avatar","store_name","description","mobile","bg_image","memberid"};
		Map<String, Object> res=verify.verify(fields, request);
		int error=(int) res.get("error");
		if(error==1) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg((String)res.get("msg"));
			
		}else if(error==0) {

			String member_id=request.getParameter("memberid");
			String avatar=request.getParameter("avatar");
			String description=request.getParameter("description");
			String mobile=request.getParameter("mobile");
			String bg_image=request.getParameter("bg_image");
			String store_name=request.getParameter("store_name");
			info.setMember_id(Integer.parseInt(member_id));
			info.setStore_name(store_name);
			info.setAvatar(avatar);
			info.setBg_image(bg_image);
			info.setMobile(mobile);
			info.setDescription(description);
			info.setCreate_at(new Date());
			info.setState(0);
			info.setStep(1);
			info.setBackper(0.00);
			info.setSuccessper(0.00);
			info.setGoodper(0.00);
			//验证手机号码
			//手机号码格式验证
			String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9])|(16[6]))\\d{8}$";
			Pattern p = Pattern.compile(regex);
			Matcher match = p.matcher(mobile);
			boolean isMatch = match.matches();
			if (!isMatch) {
				jsonResult.setCode(JsonResult.ERROR);
				jsonResult.setMsg("手机号格式不正确");
				return jsonResult;
			}
			//验证微信号码
//			p = Pattern.compile("[\u4e00-\u9fa5]");
//			match = p.matcher(weixin);
//			if (match.find()) {
//				jsonResult.setCode(JsonResult.ERROR);
//				jsonResult.setMsg("微信号码不能包含中文");
//				return jsonResult;
//			}
			int r=lmMerchRegisterService.addMerchInfo(info);
			if(r>0) {
				int id=info.getId();
				Map< String, Object> m=new HashMap<String, Object>();
				m.put("id",id);
				jsonResult.setData(m);
				jsonResult.setCode(jsonResult.SUCCESS);
				
			}else {
				jsonResult.setCode(jsonResult.ERROR);
				jsonResult.setMsg("数据插入失败");
			}
		}
		return jsonResult;
	}
	
	
	/**
	 * 注册第二步
	 * 参数
	 * categoryid商户分类id
	 * businessid经营主体类型
	 * sellid主营类目
	 * realname真实姓名
	 * idcard身份证号
	 * idcard_front身份证正面
	 * idcard_back身份证背面
	 * idcard_hold手持身份证图片
	 * id第一步数据插入返回的ID
	 * @return
	 */
	@RequestMapping("step2")
    @ResponseBody
	public JsonResult stp2(HttpServletRequest request) {
		JsonResult jsonResult=new JsonResult();
		jsonResult.setCode(jsonResult.ERROR);
//		LmMerchInfo info=new LmMerchInfo();
		
		VerifyFields verify=new VerifyFields();
		String[] fields= {"categoryid","businessid","sellid","realname","idcard","id"};
		Map<String, Object> res=verify.verify(fields, request);
		int error=(int) res.get("error");
		if(error==1) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg((String)res.get("msg"));
			return jsonResult;
		
		}else if(error==0) {
			String id=request.getParameter("id");
			LmMerchInfo info=lmMerchRegisterService.findMerchInfoByid(Integer.parseInt(id));
			if(info==null) {
				jsonResult.setCode(jsonResult.ERROR);
				jsonResult.setMsg("商户不存在，检查id");
				return jsonResult;
			}

			String categoryid=request.getParameter("categoryid");
			String businessid=request.getParameter("businessid");
			String sellid=request.getParameter("sellid");
			String realname=request.getParameter("realname");
			String idcard=request.getParameter("idcard");
			String business_license=request.getParameter("business_license");
			String bank_account=request.getParameter("bank_account");
			String idcard_front=request.getParameter("idcard_front");
			String idcard_back=request.getParameter("idcard_back");
			String idcard_hold=request.getParameter("idcard_hold");
			int no = lmMerchInfoService.findmaxno();
			no = no+1;
			String num = String.format("%05d",no);
			//获取6位店铺号
			String merchno = categoryid+num;

			int isauction=StringUtils.isEmpty(request.getParameter("isauction"))?0:Integer.parseInt(request.getParameter("isauction"));
			int isdirect=StringUtils.isEmpty(request.getParameter("isdirect"))?0:Integer.parseInt(request.getParameter("isdirect"));
			int isquality=StringUtils.isEmpty(request.getParameter("isquality"))?0:Integer.parseInt(request.getParameter("isquality"));
			int postage=StringUtils.isEmpty(request.getParameter("postage"))?0:Integer.parseInt(request.getParameter("postage"));
			int refund=StringUtils.isEmpty(request.getParameter("refund"))?0:Integer.parseInt(request.getParameter("refund"));
			int isoem=StringUtils.isEmpty(request.getParameter("isoem"))?0:Integer.parseInt(request.getParameter("isoem"));
			String marginprice=StringUtils.isEmpty(request.getParameter("marginprice"))?"0":request.getParameter("marginprice");

			info.setMerchno(Integer.parseInt(merchno));
			info.setIsauction(isauction);
			info.setIsdirect(isdirect);
			info.setIsquality(isquality);
			info.setPostage(postage);
			info.setRefund(refund);
			info.setIsoem(isoem);
			info.setId(Integer.parseInt(id));
			info.setCategoryid(Integer.parseInt(categoryid));
			info.setType(Integer.parseInt(categoryid));
			if(Integer.parseInt(categoryid)==3){
				info.setIslive(1);
				info.setIschipped(0);
			}else if(Integer.parseInt(categoryid)==4){
				info.setIschipped(1);
				info.setIslive(1);
			}else{
				info.setIschipped(0);
				info.setIslive(0);
			}
			info.setBusinessid(Integer.parseInt(businessid));
			info.setSellid(Integer.parseInt(sellid));
			info.setRealname(realname);
			info.setIdcard(idcard);
			info.setMargin(new BigDecimal(marginprice));
			if(!StringUtils.isEmpty(business_license)){
				info.setBusiness_license(business_license);
			}
			if(!StringUtils.isEmpty(bank_account)){
				info.setBank_account(bank_account);
			}
			if(!StringUtils.isEmpty(idcard_hold)){
				info.setIdcard_hold(idcard_hold);
			}
			if(!StringUtils.isEmpty(idcard_front)){
				info.setIdcard_front(idcard_front);
			}
			if(!StringUtils.isEmpty(idcard_back)){
				info.setIdcard_back(idcard_back);
			}
			info.setStep(2);
			int r=lmMerchRegisterService.updateService(info);
			if(r>0) {
				jsonResult.setCode(jsonResult.SUCCESS);
			}else {
				jsonResult.setCode(jsonResult.ERROR);
				jsonResult.setMsg("数据更新失败");
			}
		}
		return jsonResult;
	}


	/**
	 * 注册第二步
	 * 参数
	 * 保证金金额
	 * id第一步数据插入返回的ID
	 * @return
	 */
	@RequestMapping("step3")
	@ResponseBody
	public JsonResult stp3(HttpServletRequest request) {
		JsonResult jsonResult=new JsonResult();
		jsonResult.setCode(jsonResult.ERROR);
//		LmMerchInfo info=new LmMerchInfo();

		VerifyFields verify=new VerifyFields();
		String[] fields= {"marginprice","id","freeprice"};
		Map<String, Object> res=verify.verify(fields, request);
		int error=(int) res.get("error");
		if(error==1) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg((String)res.get("msg"));
			return jsonResult;
		}
		String id=request.getParameter("id");
		String marginprice=request.getParameter("marginprice");
		String freeprice=request.getParameter("freeprice");
		//修改开店的保证金和结束时间
		Calendar calendar = Calendar.getInstance();
		String limittime="1";
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + Integer.parseInt(limittime));
		Date limittimes =calendar.getTime();
		calendar.setTime(new Date());
		LmMerchInfo info=lmMerchRegisterService.findMerchInfoByid(Integer.parseInt(id));
		info.setLimit_time(limittimes);
		info.setMargin(new BigDecimal(marginprice));
		//开店优惠金额记录
		LmMerchPriceLog lmMerchPriceLog=new LmMerchPriceLog();
		lmMerchPriceLog.setPrice(new BigDecimal(freeprice));
		lmMerchPriceLog.setCreate_time(new Date());
		lmMerchPriceLog.setMember_id(info.getMember_id());
		lmMerchPriceLog.setMerch_id(Integer.parseInt(id));
		lmMerchPriceLogService.insertService(lmMerchPriceLog);

		int r=lmMerchRegisterService.updateService(info);
		if(r>0) {
			jsonResult.setCode(jsonResult.SUCCESS);
		}else {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("数据更新失败");
		}
		return jsonResult;
	}
	
	
}
