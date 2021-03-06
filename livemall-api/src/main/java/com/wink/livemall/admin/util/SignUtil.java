package com.wink.livemall.admin.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 *  支付宝验签该工具类
 */
public class SignUtil {



    /**
     * 把request请求参数转换为Map<String,String>
     * @param request 该请求
     * @return Map<String,String>格式的参数
     */
    public static Map<String,String> request2Map(HttpServletRequest request){
        Enumeration<String> names = request.getParameterNames();
        Map<String, String> resData = new HashMap<String, String>();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            resData.put(name, request.getParameter(name));
        }
        return resData;
    }


}
