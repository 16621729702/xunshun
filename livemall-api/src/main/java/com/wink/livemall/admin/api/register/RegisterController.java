package com.wink.livemall.admin.api.register;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wink.livemall.admin.util.Errors;
import com.wink.livemall.admin.util.TokenUtil;
import com.wink.livemall.admin.util.filterUtils.CheckTextAPI;
import com.wink.livemall.admin.util.filterUtils.GetAuthService;
import com.wink.livemall.admin.util.idCardUtils.Bean.JsonRootBean;
import com.wink.livemall.admin.util.idCardUtils.BusinessLicense;
import com.wink.livemall.admin.util.idCardUtils.IDCardRecognitionUtil;
import com.wink.livemall.admin.util.idCardUtils.IDCardResult;
import com.wink.livemall.admin.util.idCardUtils.PersonVerifyUtil;
import com.wink.livemall.merch.dao.LmMerchPriceLogDao;
import com.wink.livemall.merch.dto.LmMerchPriceLog;
import com.wink.livemall.merch.service.LmMerchInfoService;
import com.wink.livemall.merch.service.LmMerchPriceLogService;
import com.wink.livemall.utils.cache.redis.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
 *??????????????????
 */
@Api(tags ="????????????")
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
	@Autowired
	private RedisUtil redisUtil;
	/**
	 * ????????????????????????
	 * @param request
	 * @return
	 */
	@RequestMapping("get_info")
    @ResponseBody
	public JsonResult get_info(HttpServletRequest request) {
		JsonResult jsonResult=new JsonResult();
		
		try {
			LmMerchConfigs configs=lmMerchConfigsService.findByType("????????????");
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
	 * ???????????????
	 * ??????
	 * avatar?????? 
	 * store_name????????????
	 * description????????????
	 * weixin???????????????
	 * mobile???????????????
	 * bg_image???????????????
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
			//??????????????????
			//????????????????????????
			String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9])|(16[6]))\\d{8}$";
			Pattern p = Pattern.compile(regex);
			Matcher match = p.matcher(mobile);
			boolean isMatch = match.matches();
			if (!isMatch) {
				jsonResult.setCode(JsonResult.ERROR);
				jsonResult.setMsg("????????????????????????");
				return jsonResult;
			}
			int r=lmMerchRegisterService.addMerchInfo(info);
			if(r>0) {
				int id=info.getId();
				Map< String, Object> m=new HashMap<String, Object>();
				m.put("id",id);
				jsonResult.setData(m);
				jsonResult.setCode(jsonResult.SUCCESS);
				
			}else {
				jsonResult.setCode(jsonResult.ERROR);
				jsonResult.setMsg("??????????????????");
			}
		}
		return jsonResult;
	}
	
	
	/**
	 * ???????????????
	 * ??????
	 * categoryid????????????id
	 * businessid??????????????????
	 * sellid????????????
	 * realname????????????
	 * idcard????????????
	 * idcard_front???????????????
	 * idcard_back???????????????
	 * idcard_hold?????????????????????
	 * id??????????????????????????????ID
	 * @return
	 */

	@RequestMapping("/merchOpenShop")
	@ResponseBody
	@ApiOperation(value = "??????????????????",notes = "??????????????????")
	public JsonResult checkMerchIdCard(HttpServletRequest request) throws Exception {
		JsonResult jsonResult=new JsonResult();
		JsonResult insertMerchInfoResult = insertMerchInfo(request, jsonResult);
		return insertMerchInfoResult;
	}





	public JsonResult insertMerchInfo(HttpServletRequest request,JsonResult jsonResult) throws Exception {

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
		jsonResult.setCode(jsonResult.ERROR);
		LmMerchInfo info=new LmMerchInfo();
		String mobile=request.getParameter("mobile");
		String categoryid=request.getParameter("categoryid");
		String businessid=request.getParameter("businessid");
		String sellid=request.getParameter("sellid");
		//????????????
		info.setMember_id(Integer.parseInt(userid));
		info.setStore_name(request.getParameter("store_name"));
		info.setAvatar(request.getParameter("avatar"));
		info.setBg_image(request.getParameter("bg_image"));
		info.setMobile(mobile);
		info.setDescription(request.getParameter("description"));
		info.setCreate_at(new Date());
		info.setState(0);
		info.setStep(1);
		info.setBackper(0.00);
		info.setSuccessper(0.00);
		info.setGoodper(0.00);
		//??????????????????
		//????????????????????????
		String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9])|(16[6]))\\d{8}$";
		Pattern p = Pattern.compile(regex);
		Matcher match = p.matcher(mobile);
		boolean isMatch = match.matches();
		if (!isMatch) {
			jsonResult.setCode(JsonResult.ERROR);
			jsonResult.setMsg("????????????????????????");
			return jsonResult;
		}
		if(!StringUtils.isEmpty(request.getParameter("idcard_front"))) {
			//?????????????????????
			IDCardResult idCardFrontResult = IDCardRecognitionUtil.readIDCard(true, true, "front", request.getParameter("idcard_front"));
			//????????????  ???????????????
			if ("normal".equals(idCardFrontResult.getImage_status())) {

				String merchName = idCardFrontResult.getWords_result().get("??????").getWords();
				if (!request.getParameter("realname").equals(merchName)) {
					jsonResult.setCode(jsonResult.ERROR);
					jsonResult.setMsg("????????????????????????");
					return jsonResult;
				} else {
					info.setRealname(request.getParameter("realname"));
				}

				String merchIdCard = idCardFrontResult.getWords_result().get("??????????????????").getWords();
				if (!request.getParameter("idcard").equals(merchIdCard)) {
					jsonResult.setCode(jsonResult.ERROR);
					jsonResult.setMsg("???????????????????????????");
					return jsonResult;
				} else {
					info.setIdcard(request.getParameter("idcard"));
				}
				info.setIdcard_front(request.getParameter("idcard_front"));
			} else {
				jsonResult.setCode(jsonResult.ERROR);
				jsonResult.setMsg("?????????????????????????????????");
				return jsonResult;
			}
		}
		if(!StringUtils.isEmpty(request.getParameter("idcard_back"))) {
			//?????????????????????
			IDCardResult idCardFrontResult1 = IDCardRecognitionUtil.readIDCard(true, true, "back", request.getParameter("idcard_back"));
			if (!"normal".equals(idCardFrontResult1.getImage_status())) {
				jsonResult.setCode(jsonResult.ERROR);
				jsonResult.setMsg("?????????????????????????????????");
				return jsonResult;
			} else {
				info.setIdcard_back(request.getParameter("idcard_back"));
			}
		}
		if(!StringUtils.isEmpty(request.getParameter("idcard_hold"))) {
			String result = PersonVerifyUtil.personVerify(request.getParameter("idcard_hold"), request.getParameter("realname"), request.getParameter("idcard"));
			if (JSONObject.parseObject(result).get("error_msg").toString().equals("SUCCESS") && JSONObject.parseObject(result).get("error_code").toString().equals("0")) {
				Object resultJson = JSONObject.parseObject(result).get("result");
				String score = JSONObject.parseObject(resultJson.toString()).get("score").toString();
				BigDecimal scores = new BigDecimal(score).setScale(2, BigDecimal.ROUND_HALF_UP);
				BigDecimal old = new BigDecimal(70).setScale(2, BigDecimal.ROUND_HALF_UP);
				if (scores.compareTo(old) > -1) {
					info.setIdcard_hold(request.getParameter("idcard_hold"));
				} else {
					jsonResult.setCode(jsonResult.ERROR);
					jsonResult.setMsg("?????????????????????????????????");
					return jsonResult;
				}
			} else {
				jsonResult.setCode(jsonResult.ERROR);
				jsonResult.setMsg("?????????????????????????????????");
				return jsonResult;
			}
		}
		int no = lmMerchInfoService.findmaxno();
		no = no+1;
		String num = String.format("%05d",no);
		//??????6????????????
		String merchno = categoryid+num;
		info.setMerchno(Integer.parseInt(merchno));
		info.setIsauction(0);
		info.setIsdirect(0);
		info.setIsquality(0);
		info.setPostage(0);
		info.setRefund(0);
		info.setIsoem(0);
		info.setCategoryid(Integer.parseInt(categoryid));
		info.setType(Integer.parseInt(categoryid));
		if(Integer.parseInt(categoryid)==3){
			info.setIslive(1);
			info.setIschipped(0);
		}else if(Integer.parseInt(categoryid)==4){
			info.setIschipped(1);
			info.setIslive(1);
		}else if(Integer.parseInt(categoryid)==2){
			info.setIschipped(0);
			info.setIslive(1);
		}else{
			info.setIschipped(0);
			info.setIslive(0);
		}
		info.setBusinessid(Integer.parseInt(businessid));
		info.setSellid(Integer.parseInt(sellid));
		info.setMargin(new BigDecimal(0));
		if(!StringUtils.isEmpty(request.getParameter("business_license"))){
			String license = BusinessLicense.businessLicense(request.getParameter("business_license"));
			Object words_result = JSONObject.parseObject(license).get("words_result");
			Object uniform_social_credit_code =JSONObject.parseObject(words_result.toString()).get("??????????????????");
			Object artificial_person =JSONObject.parseObject(words_result.toString()).get("??????");
			Object term_of_validity =JSONObject.parseObject(words_result.toString()).get("?????????");
			String uniform= JSONObject.parseObject(uniform_social_credit_code.toString()).get("words").toString();//??????????????????
			String artificial= JSONObject.parseObject(artificial_person.toString()).get("words").toString();//??????
			String validity= JSONObject.parseObject(term_of_validity.toString()).get("words").toString();//?????????
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy???MM???dd???");
			if(uniform.equals(request.getParameter("uniform_social_credit_code"))){
					if(artificial.equals(request.getParameter("realname"))){
						if(("?????????").equals(validity)){
							info.setRealname(request.getParameter("realname"));
							info.setBusiness_license(request.getParameter("business_license"));
							info.setIdcard(request.getParameter("idcard"));
						}else {
							Date validitys= sdf.parse(validity);
							Date date = new Date();
							if(validitys.getTime()>date.getTime()){
								info.setRealname(request.getParameter("realname"));
								info.setBusiness_license(request.getParameter("business_license"));
								info.setIdcard(request.getParameter("idcard"));
							}else {
								jsonResult.setCode(jsonResult.ERROR);
								jsonResult.setMsg("?????????????????????");
								return jsonResult;
							}
						}
					}else {
						jsonResult.setCode(jsonResult.ERROR);
						jsonResult.setMsg("?????????????????????");
						return jsonResult;
					}
			}else {
				jsonResult.setCode(jsonResult.ERROR);
				jsonResult.setMsg("??????????????????????????? ");
				return jsonResult;
			}
		}
		if(!StringUtils.isEmpty(request.getParameter("bank_account"))){
			info.setBank_account(request.getParameter("bank_account"));
		}
		info.setStep(2);
		int r=lmMerchRegisterService.addMerchInfo(info);
		redisUtil.delete(userid);
		redisUtil.set(userid,info.getId());
		if(r>0) {
			jsonResult.setCode(jsonResult.SUCCESS);
			jsonResult.setMsg("??????????????????");
			jsonResult.setData(info.getId());
		}else {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("??????????????????");
		}
		return jsonResult;
	}


	/**
	 * ???????????????
	 * ??????
	 * ???????????????
	 * id??????????????????????????????ID
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
		//???????????????????????????????????????
		Calendar calendar = Calendar.getInstance();
		String limittime="1";
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + Integer.parseInt(limittime));
		Date limittimes =calendar.getTime();
		calendar.setTime(new Date());
		LmMerchInfo info=lmMerchRegisterService.findMerchInfoByid(Integer.parseInt(id));
		info.setLimit_time(limittimes);
		info.setMargin(new BigDecimal(marginprice));
		//????????????????????????
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
			jsonResult.setMsg("??????????????????");
		}
		return jsonResult;
	}

	public JsonResult insertLmMerch(HttpServletRequest request,JsonResult jsonResult){

		LmMerchInfo lmMerchInfo = new LmMerchInfo();
		lmMerchInfo.setAvatar(request.getParameter("avatar"));
		lmMerchInfo.setStore_name(request.getParameter("storeName"));
		lmMerchInfo.setDescription(request.getParameter("description"));
		if(StringUtils.isEmpty(request.getParameter("mobile"))){

			lmMerchInfo.setMobile(request.getParameter("mobile"));
		}
		lmMerchInfo.setBg_image(request.getParameter("backGroundImage"));
		lmMerchInfo.setCategoryid(Integer.parseInt(request.getParameter("categoryId")));
		lmMerchInfo.setBusinessid(Integer.parseInt(request.getParameter("businessId")));
		lmMerchInfo.setSellid(Integer.parseInt(request.getParameter("sellId")));

		if(!StringUtils.isEmpty(request.getParameter("idCardFront"))) {
			//?????????????????????
			IDCardResult idCardFrontResult = IDCardRecognitionUtil.readIDCard(true, true, "front", request.getParameter("idCardFront"));
			//????????????  ???????????????
			if ("normal".equals(idCardFrontResult.getImage_status())) {

				String merchName = idCardFrontResult.getWords_result().get("??????").getWords();
				if (!request.getParameter("realName").equals(merchName)) {
					jsonResult.setCode(jsonResult.ERROR);
					jsonResult.setMsg("????????????????????????");
					return jsonResult;
				} else {
					lmMerchInfo.setRealname(request.getParameter("realName"));
				}

				String merchIdCard = idCardFrontResult.getWords_result().get("??????????????????").getWords();
				if (!request.getParameter("idCard").equals(merchIdCard)) {
					jsonResult.setCode(jsonResult.ERROR);
					jsonResult.setMsg("???????????????????????????");
					return jsonResult;
				} else {
					lmMerchInfo.setRealname(request.getParameter("idCard"));
				}
				lmMerchInfo.setIdcard_front(request.getParameter("idCardFront"));
			} else {
				jsonResult.setCode(jsonResult.ERROR);
				jsonResult.setMsg("?????????????????????????????????");
				return jsonResult;
			}
		}
		if(!StringUtils.isEmpty(request.getParameter("idCardBack"))) {
			//?????????????????????
			IDCardResult idCardFrontResult1 = IDCardRecognitionUtil.readIDCard(true, true, "back", request.getParameter("idCardBack"));
			if (!"normal".equals(idCardFrontResult1.getImage_status())) {
				jsonResult.setCode(jsonResult.ERROR);
				jsonResult.setMsg("?????????????????????????????????");
				return jsonResult;
			} else {
				lmMerchInfo.setIdcard_back(request.getParameter("idCardBack"));
			}
		}
		if(!StringUtils.isEmpty(request.getParameter("idCardHold"))) {
			String result = PersonVerifyUtil.personVerify(request.getParameter("idCardHold"), request.getParameter("realName"), request.getParameter("idCard"));
			if (JSONObject.parseObject(result).get("error_msg").equals("SUCCESS") && JSONObject.parseObject(result).get("error_code").equals("0")) {
				Object resultJson = JSONObject.parseObject(result).get("result");
				String score = JSONObject.parseObject(resultJson.toString()).get("score").toString();
				if (Integer.parseInt(score) > 80) {
					lmMerchInfo.setIdcard_hold(request.getParameter("idCardHold"));
				} else {
					jsonResult.setCode(jsonResult.ERROR);
					jsonResult.setMsg("?????????????????????????????????");
					return jsonResult;
				}
			} else {
				jsonResult.setCode(jsonResult.ERROR);
				jsonResult.setMsg("?????????????????????????????????");
				return jsonResult;
			}

		}
		boolean redisResult = redisUtil.set(request.getParameter("lmMemberId")+9999, lmMerchInfo, 10*60*1000);
		if(redisResult){
			logger.info("redis??????????????????????????????");
			jsonResult.setCode(jsonResult.SUCCESS);
			jsonResult.setMsg(jsonResult.SUCCESS);
			return jsonResult;
		}

		return jsonResult;
	}



	@RequestMapping("/isRepeat")
	@ResponseBody
	@ApiOperation(value = "????????????????????????",notes = "??????????????????????????????")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "store_name", value = "?????????",required =true,  dataType = "String",paramType = "query")
	})
	public JsonResult isRepeat(HttpServletRequest request,String store_name) {
		JsonResult jsonResult=new JsonResult();
		JsonResult isRepeat = isRepeat(request,store_name, jsonResult);
		return isRepeat;
	}


	public JsonResult isRepeat(HttpServletRequest request,String store_name,JsonResult jsonResult){
		int repeat = lmMerchRegisterService.isRepeat(store_name);
		if(repeat==1){
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("??????????????????????????????");
			return jsonResult;
		}
		jsonResult.setCode(jsonResult.SUCCESS);
		jsonResult.setMsg("??????????????????????????????");
		return jsonResult;
	}

}
