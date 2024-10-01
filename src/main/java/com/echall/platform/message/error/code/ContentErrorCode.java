package com.echall.platform.message.error.code;

import org.springframework.http.HttpStatus;

import com.echall.platform.message.status.ContentServiceStatus;
import com.echall.platform.message.status.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ContentErrorCode implements ErrorCode {
	CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND, ContentServiceStatus.CONTENT_NOT_FOUND, "해당 컨텐츠 존재하지 않음")

	;

	private final HttpStatus httpStatus;
	private final ServiceStatus serviceStatus;
	private final String message;

	@Override
	public HttpStatus getStatus() {
		return httpStatus;
	}

	@Override
	public String getCode() {
		return serviceStatus.getServiceStatus();
	}

	@Override
	public String getMessage() {
		return message;
	}
}
