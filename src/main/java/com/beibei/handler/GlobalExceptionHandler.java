package com.beibei.handler;

import com.beibei.entity.RestBean;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

//    @ExceptionHandler(Exception.class)
//    public final RestBean<Object> handleAllExceptions(Exception ex) {
//        return RestBean.error(ex);
//    }
}
