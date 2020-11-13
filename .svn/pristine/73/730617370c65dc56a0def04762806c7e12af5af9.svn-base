package com.wink.livemall.admin.exception;


import com.wink.livemall.admin.util.JsonResult;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 权限异常处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public JsonResult handleTokenNotFoundException(CustomException e) {
        return new JsonResult(e);
    }

    @ExceptionHandler(AuthorizationException.class)
    @ResponseBody
    public JsonResult handleAuthorizationException(AuthorizationException e){
        return new JsonResult(JsonResult.ERROR,"ERROR,请返回首页");
    }
}
