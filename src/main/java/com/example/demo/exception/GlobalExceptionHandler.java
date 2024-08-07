package com.example.demo.exception;

import com.example.demo.controller.result.CommonResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 处理业务异常
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ResponseEntity<CommonResult<?>> handleCustomException(BusinessException ex) {
        CommonResult<Object> failResult = CommonResult.businessFailResult(ex.getMessage());
        return new ResponseEntity<>(failResult, HttpStatus.OK);
    }

    // 处理其他所有异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<CommonResult<?>> handleException(Exception ex) {
        CommonResult<Object> failResult = CommonResult.businessFailResult("unknown error, please contact customer service.");
        return new ResponseEntity<>(failResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
