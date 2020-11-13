package com.wink.livemall.utils.sms;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

public class SmsUtils {
    /**
     *
     * @Description: 阿里云发送短信验证码
     * @author tianpengw
     * @return void
     */
    public static void sendValidCodeMsg(String phone, String validCode,String accessKeyId,String accessKeySecret,String signname,String smscode){
        try {
            //初始化ascClient,暂时不支持多region
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Dysmsapi", "dysmsapi.aliyuncs.com");
            IAcsClient acsClient = new DefaultAcsClient(profile);
            //组装请求对象
            SendSmsRequest request = new SendSmsRequest();
            //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
            request.setPhoneNumbers(phone);
            //必填:短信签名-可在短信控制台中找到
            request.setSignName(signname);
            //必填:短信模板-可在短信控制台中找到
            request.setTemplateCode(smscode);
            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
            request.setTemplateParam("{\"code\":\"" + validCode + "\"}");
            //可选-上行短信扩展码(无特殊需求用户请忽略此字段)
            //request.setSmsUpExtendCode("90997");
            //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
            //请求失败这里会抛ClientException异常
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
                //请求成功
            }
        } catch (ServerException e1) {
            e1.printStackTrace();
        } catch (ClientException e1) {
            e1.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SmsUtils.sendValidCodeMsg("13812117597","123456","LTAI4FzzMoHCJ74T8wcU4W6E","oqaKUKVJEAw7iuJWVrgZSdWPRieKRQ","滴雨轩","SMS_194900500");
    }
}
