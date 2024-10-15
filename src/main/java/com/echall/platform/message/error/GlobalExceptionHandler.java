package com.echall.platform.message.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.echall.platform.message.ApiCustomResponse;
import com.echall.platform.message.ResponseEntityFactory;
import com.echall.platform.message.error.exception.CommonException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(CommonException.class)
	public ResponseEntity<ApiCustomResponse<Void>> commonException(CommonException e) {
		return ResponseEntityFactory.toResponseEntity(e.getErrorCode());
	}

}
