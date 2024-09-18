package com.echall.platform.exception.common;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/**
 * 최상위 예외입니다
 */
@Getter
public class ApiException extends RuntimeException {

	private final ErrorCode errorCode;

	public ApiException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public HttpStatus getStatus() {
		return this.errorCode.getStatus();
	}

	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}
}
