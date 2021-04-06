package com.wink.livemall.admin.api.user;


import com.wink.livemall.admin.dtovo.TransfersDto;
import com.wink.livemall.admin.util.Errors;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.TokenUtil;
import com.wink.livemall.admin.util.idCardUtils.BankCardUtils;
import com.wink.livemall.admin.util.payUtil.ConstantsEJS;
import com.wink.livemall.admin.util.payUtil.WechatpayUtil;
import com.wink.livemall.goods.dto.Good;
import com.wink.livemall.member.dto.LmMember;
import com.wink.livemall.member.service.LmMemberService;
import com.wink.livemall.merch.dto.LmMerchInfo;
import com.wink.livemall.merch.service.LmMerchInfoService;
import com.wink.livemall.sys.withdraw.dto.AllBankInfo;
import com.wink.livemall.sys.withdraw.dto.Bank;
import com.wink.livemall.sys.withdraw.dto.WithdrawLog;
import com.wink.livemall.sys.withdraw.service.AllBankInfoService;
import com.wink.livemall.sys.withdraw.service.BankService;
import com.wink.livemall.sys.withdraw.service.WithdrawLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Api(tags = "银行接口")
@RestController
@RequestMapping("/bankInfo")
public class BankController {

    Logger logger = LogManager.getLogger(BankController.class);

    private  static RequestConfig requestConfig;// 请求器的配置
    private  static CloseableHttpClient httpClient;// HTTP请求器
    private  int socketTimeout = 10000;// 连接超时时间，默认10秒
    private  int connectTimeout = 30000;// 传输超时时间，默认30秒
    private static final String TRANS_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";

    @Autowired
    private BankService bankService;
    @Autowired
    private AllBankInfoService allBankInfoService;
    @Autowired
    private WithdrawLogService withdrawLogService;
    @Autowired
    private LmMemberService lmMemberService;
    @Autowired
    private LmMerchInfoService lmMerchInfoService;
    /**
     *  所有银行列表接口
     * @param request
     * @return
     */
    @ApiOperation(value = "所有银行列表接口")
    @RequestMapping(value = "/bankList", method = RequestMethod.POST)
    @ApiImplicitParams({
    })
    public JsonResult marginList(HttpServletRequest request) {
        JsonResult jsonResult = new JsonResult();
        try {
            List<AllBankInfo> all = allBankInfoService.findAll();
            jsonResult.setData(all);
            jsonResult.setCode(JsonResult.SUCCESS);
        }catch (Exception e) {
            jsonResult.setCode(JsonResult.ERROR);
            jsonResult.setMsg(e.toString());
            return jsonResult;
        }
        return  jsonResult;
    }

    /**
     * 用户绑定银行卡
     * @param request
     * @param bankVo
     * @return
     */
    @RequestMapping("/addBank")
    @ApiOperation(value = "用户绑定银行卡",notes = "用户绑定银行卡接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bankVo", value = "接收商家填写信息对象", dataType = "Object",paramType = "query")
    })
    public JsonResult addBank(HttpServletRequest request, Bank bankVo) {
        JsonResult jsonResult = new JsonResult();
        String header = request.getHeader("Authorization");
        String userId="";
        if (!StringUtils.isEmpty(header)) {
            if(!StringUtils.isEmpty(TokenUtil.getUserId(header))){
                userId = TokenUtil.getUserId(header);
            }else{
                jsonResult.setCode(JsonResult.LOGIN);
                jsonResult.setMsg(Errors.TOKEN_PAST.getMsg());
                return jsonResult;
            }
        }
        try {
            boolean bankCard = BankCardUtils.checkBankCard(bankVo.getEnc_bank_no());
            if(!bankCard){
                jsonResult.setMsg("银行卡号不合规");
                jsonResult.setCode(JsonResult.ERROR);
                return jsonResult;
            }
            List<Map<String,Object>> listByUserId = bankService.findListByUserId(Integer.valueOf(userId));
            if(listByUserId.size()>5){
                jsonResult.setMsg("最多绑定六张银行卡");
                jsonResult.setCode(JsonResult.ERROR);
                return jsonResult;
            }
            bankVo.setUser_id(Integer.valueOf(userId));
            bankService.insertBank(bankVo);
            AllBankInfo listByCode = allBankInfoService.findListByCode(Integer.valueOf(bankVo.getBank_code()));
            jsonResult.setData(listByCode);
            jsonResult.setCode(JsonResult.SUCCESS);
        }catch (Exception e) {
            jsonResult.setCode(JsonResult.ERROR);
            jsonResult.setMsg(e.toString());
            return jsonResult;
        }
        return jsonResult;
    }


    /**
     * 用户银行卡列表
     * @param request
     * @return
     */
    @RequestMapping("/bankListByUserId")
    @ApiOperation(value = "用户银行卡列表",notes = "用户银行卡列表接口")
    @ApiImplicitParams({
    })
    public JsonResult bankListByUserId(HttpServletRequest request) {
        JsonResult jsonResult = new JsonResult();
        String header = request.getHeader("Authorization");
        String userId="";
        if (!StringUtils.isEmpty(header)) {
            if(!StringUtils.isEmpty(TokenUtil.getUserId(header))){
                userId = TokenUtil.getUserId(header);
            }else{
                jsonResult.setCode(JsonResult.LOGIN);
                jsonResult.setMsg(Errors.TOKEN_PAST.getMsg());
                return jsonResult;
            }
        }
        try {
            List<Map<String,Object>> listByUserId = bankService.findListByUserId(Integer.valueOf(userId));
            jsonResult.setData(listByUserId);
            jsonResult.setCode(JsonResult.SUCCESS);
        }catch (Exception e) {
            jsonResult.setCode(JsonResult.ERROR);
            jsonResult.setMsg(e.toString());
            return jsonResult;
        }
        return jsonResult;
    }

    /**
     * 用户解绑银行卡
     * @param request
     * @return
     */
    @RequestMapping("/delBankById")
    @ApiOperation(value = "用户解绑银行卡",notes = "用户解绑银行卡接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "Integer",paramType = "query")
    })
    public JsonResult delBankById(HttpServletRequest request,Integer id) {
        JsonResult jsonResult = new JsonResult();
        try {
            bankService.delBank(id);
            jsonResult.setCode(JsonResult.SUCCESS);
        }catch (Exception e) {
            jsonResult.setCode(JsonResult.ERROR);
            jsonResult.setMsg(e.toString());
            return jsonResult;
        }
        return jsonResult;
    }

    /**
     * 用户修改银行卡
     * @param request
     * @return
     */
    @RequestMapping("/updBankByUserId")
    @ApiOperation(value = "用户修改银行卡",notes = "用户修改银行卡接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bankVo", value = "接收商家填写信息对象", dataType = "Object",paramType = "query")
    })
    public JsonResult updBankByUserId(HttpServletRequest request,Bank bankVo) {
        JsonResult jsonResult = new JsonResult();
        String header = request.getHeader("Authorization");
        String userId="";
        if (!StringUtils.isEmpty(header)) {
            if(!StringUtils.isEmpty(TokenUtil.getUserId(header))){
                userId = TokenUtil.getUserId(header);
            }else{
                jsonResult.setCode(JsonResult.LOGIN);
                jsonResult.setMsg(Errors.TOKEN_PAST.getMsg());
                return jsonResult;
            }
        }
        try {
            boolean bankCard = BankCardUtils.checkBankCard(bankVo.getEnc_bank_no());
            if(!bankCard){
                jsonResult.setMsg("银行卡号不合规");
                jsonResult.setCode(JsonResult.ERROR);
                return jsonResult;
            }
            bankVo.setUser_id(Integer.valueOf(userId));
            bankService.updateBank(bankVo);
            jsonResult.setCode(JsonResult.SUCCESS);
        }catch (Exception e) {
            jsonResult.setCode(JsonResult.ERROR);
            jsonResult.setMsg(e.toString());
            return jsonResult;
        }
        return jsonResult;
    }

    /**
     * 用户or商户提现至银行卡
     * @param request
     * @return
     */
    @RequestMapping("/OnlineBankingWithdrawals")
    @ApiOperation(value = "用户or商户提现至银行卡",notes = "用户提现至银行卡接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "withdrawLogVo", value = "接收商家填写信息对象", dataType = "Object",paramType = "query"),
            @ApiImplicitParam(name = "merId", value = "商户id", dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "type", value = "1用户2商户", dataType = "Integer",paramType = "query")
    })
    public JsonResult OnlineBankingWithdrawals(HttpServletRequest request, WithdrawLog withdrawLogVo,Integer merId,Integer type) {
        JsonResult jsonResult = new JsonResult();
        String header = request.getHeader("Authorization");
        String userId="";
        if (!StringUtils.isEmpty(header)) {
            if(!StringUtils.isEmpty(TokenUtil.getUserId(header))){
                userId = TokenUtil.getUserId(header);
            }else{
                jsonResult.setCode(JsonResult.LOGIN);
                jsonResult.setMsg(Errors.TOKEN_PAST.getMsg());
                return jsonResult;
            }
        }
        try {
            boolean bankCard = BankCardUtils.checkBankCard(withdrawLogVo.getEnc_bank_no());
            if(!bankCard){
                jsonResult.setMsg("银行卡号不合规");
                jsonResult.setCode(JsonResult.ERROR);
                return jsonResult;
            }
            BigDecimal amount = withdrawLogVo.getAmount();
            BigDecimal min=new BigDecimal(1000);
            withdrawLogVo.setAmount(amount);
            if(amount.compareTo(min)>0){
                amount = amount.multiply(new BigDecimal(1001)).divide(new BigDecimal(1000)).setScale(2,BigDecimal.ROUND_HALF_DOWN);
            }else {
                amount = amount.add(new BigDecimal(1)).setScale(2,BigDecimal.ROUND_HALF_DOWN);
            }
            if(type==1){
                withdrawLogVo.setAccount_log_id(0);
                LmMember lmMember = lmMemberService.findById(userId);
                if(lmMember.getCredit2().compareTo(amount)<0){
                    jsonResult.setMsg("账号余额不足");
                    jsonResult.setCode(JsonResult.ERROR);
                    return jsonResult;
                }
                lmMember.setCredit2(lmMember.getCredit2().subtract(amount).setScale(2,BigDecimal.ROUND_HALF_DOWN));
                lmMemberService.updateService(lmMember);
            }else {
                withdrawLogVo.setAccount_log_id(merId);
                LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(String.valueOf(merId));
                if(lmMerchInfo.getCredit().compareTo(amount)<0){
                    jsonResult.setMsg("账号余额不足");
                    jsonResult.setCode(JsonResult.ERROR);
                    return jsonResult;
                }
                lmMerchInfo.setCredit(lmMerchInfo.getCredit().subtract(amount).setScale(2,BigDecimal.ROUND_HALF_DOWN));
                lmMerchInfoService.updateService(lmMerchInfo);
            }
            withdrawLogVo.setUser_id(Integer.valueOf(userId));
            withdrawLogVo.setState(0);
            withdrawLogVo.setDesc("银行卡提现");
            withdrawLogService.insertWithdrawLog(withdrawLogVo);
            jsonResult.setCode(JsonResult.SUCCESS);
        }catch (Exception e) {
            jsonResult.setCode(JsonResult.ERROR);
            jsonResult.setMsg(e.toString());
            return jsonResult;
        }
        return jsonResult;
    }

    /**
     * 用户提现至银行卡
     * @param request
     * @return
     */
    @RequestMapping("/userToCredit")
    @ApiOperation(value = "用户提现至零钱",notes = "用户提现至零钱接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "amount", value = "提现金额", dataType = "String",paramType = "query")
    })
    public JsonResult userToCredit(HttpServletRequest request,String amount) {
        JsonResult jsonResult = new JsonResult();
        String header = request.getHeader("Authorization");
        String userId="";
        if (!StringUtils.isEmpty(header)) {
            if(!StringUtils.isEmpty(TokenUtil.getUserId(header))){
                userId = TokenUtil.getUserId(header);
            }else{
                jsonResult.setCode(JsonResult.LOGIN);
                jsonResult.setMsg(Errors.TOKEN_PAST.getMsg());
                return jsonResult;
            }
        }
        LmMember lmMember = lmMemberService.findById(userId);
        //String CERT_PATH = "\\xiangmu\\xunshun\\livemall-parent\\livemall-api\\src\\main\\resources\\apiclient_cert.p12";
        String CERT_PATH = "/certificate/apiclient_cert.p12";
        String appKey = ConstantsEJS.WXPAY_PAY_API_KEY;
        try {
            BigDecimal amountPrice=new BigDecimal(amount);
            if(lmMember.getCredit2().compareTo(amountPrice)<0){
                jsonResult.setMsg("账号余额不足");
                jsonResult.setCode(JsonResult.ERROR);
                return jsonResult;
            }
            WithdrawLog withdrawLog=new WithdrawLog();
            withdrawLog.setAccount_log_id(0);
            withdrawLog.setPhone(lmMember.getMobile());
            withdrawLog.setUser_id(lmMember.getId());
            withdrawLog.setEnc_bank_no("");
            withdrawLog.setEnc_true_name("");
            withdrawLog.setBank_code("微信提现到零钱");
            withdrawLog.setAmount(amountPrice);
            withdrawLog.setDesc("用户申请提现");
            withdrawLog.setState(1);
            withdrawLog.setId_card("");
            lmMember.setCredit2(lmMember.getCredit2().subtract(amountPrice).setScale(2,BigDecimal.ROUND_HALF_DOWN));
            //提现
            TransfersDto transfersDto=new TransfersDto();
            transfersDto.setMch_appid(ConstantsEJS.MINI_APPID);
            transfersDto.setMchid(ConstantsEJS.WXPAY_PARTNER);
            String partnerTradeNo = transfersDto.getMchid() + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + (int) ((Math.random() * 9 + 1) * 1000);
            transfersDto.setPartner_trade_no(partnerTradeNo);
            transfersDto.setOpenid(lmMember.getOpen_id());
            transfersDto.setAmount(Double.valueOf(amount));
            transfersDto.setNonce_str(WechatpayUtil.getNonce_str());
            transfersDto.setDesc("用户提现到零钱");
            transfersDto.setAppkey(ConstantsEJS.WXPAY_PAY_API_KEY);
            StringBuilder stringBuilder = WechatpayUtil.doTransfers(appKey, CERT_PATH, transfersDto);
            //3.加载证书请求接口
            String result = postData(TRANS_URL, stringBuilder.toString());
            logger.error(("response =========== xml = " + result));
            withdrawLog.setPartner_trade_no(partnerTradeNo);
            withdrawLogService.insertWithdrawLog(withdrawLog);
            lmMemberService.updateService(lmMember);
            jsonResult.setCode(JsonResult.SUCCESS);
            jsonResult.setMsg("提现到零钱成功");
        }catch (Exception e) {
            jsonResult.setCode(JsonResult.ERROR);
            jsonResult.setMsg(e.toString());
            return jsonResult;
        }
        return jsonResult;
    }


    public  String postData(String url, String xmlObj) {
        // 加载证书
        try {
            initCert();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String result = null;
        HttpPost httpPost = new HttpPost(url);
        // 得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
        StringEntity postEntity = new StringEntity(xmlObj, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.setEntity(postEntity);
        // 根据默认超时限制初始化requestConfig
        requestConfig = RequestConfig.custom()
                .setSocketTimeout(socketTimeout)
                .setConnectTimeout(connectTimeout)
                .build();
        // 设置请求器的配置
        httpPost.setConfig(requestConfig);
        try {
            HttpResponse response = null;
            try {
                response = httpClient.execute(httpPost);
            }  catch (IOException e) {
                e.printStackTrace();
            }
            HttpEntity entity = response.getEntity();
            try {
                result = EntityUtils.toString(entity, "UTF-8");
            }  catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            httpPost.abort();
        }
        return result;
    }


    /**
     * 加载证书
     *
     */
    private static void initCert() throws Exception {
        // 证书密码，默认为商户ID
        String key = ConstantsEJS.WXPAY_PARTNER;
        // 商户证书的路径
//        ClassPathResource classPathResource = new ClassPathResource("certificate/apiclient_cert.p12");
        // 指定读取证书格式为PKCS12
        KeyStore keyStore = KeyStore.getInstance("PKCS12");

//        InputStream inputStream = classPathResource.getInputStream();

//        // 读取本机存放的PKCS12证书文件
        FileInputStream inputStream = new FileInputStream(new File("/certificate/apiclient_cert.p12"));
        try {
            // 指定PKCS12的密码(商户ID)
            keyStore.load(inputStream, key.toCharArray());
        } finally {
            inputStream.close();
        }

        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, key.toCharArray()).build();

        // 指定TLS版本
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        // 设置httpclient的SSLSocketFactory
        httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
    }

}
