package com.wink.livemall.admin.api.login;

import com.alibaba.fastjson.JSONObject;
import com.wink.livemall.admin.util.Errors;
import com.wink.livemall.admin.util.HttpJsonResult;
import com.wink.livemall.admin.util.TokenUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setCharacterEncoding("UTF-8");
        //从前端传的request中获取token.需要与前端确认传的key是啥,我的是Authorization
        String token = request.getHeader("Authorization");
        if (null != token) {
            //判断token是否通过验证
            HttpJsonResult<JSONObject>  jsonResult= TokenUtil.verify(token);
            if (jsonResult.getCode()==200) {
                return true;
            }else {
                response.getWriter().write(JSONObject.toJSONString(jsonResult));
                return false;
            }
        }
        HttpJsonResult<JSONObject> Result=new HttpJsonResult<>();
        Result.setCode(Errors.TOKEN_ERROR.getCode());
        Result.setMsg(Errors.TOKEN_ERROR.getMsg());
        response.getWriter().write(JSONObject.toJSONString(Result));
        return false;
    }
}
