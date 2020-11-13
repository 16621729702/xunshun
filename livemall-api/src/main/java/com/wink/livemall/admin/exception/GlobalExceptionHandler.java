package com.wink.livemall.admin.exception;


import com.wink.livemall.admin.util.JsonResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 权限异常处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public JsonResult handleTokenNotFoundException(CustomException e) {
        return new JsonResult(e);
    }

}
