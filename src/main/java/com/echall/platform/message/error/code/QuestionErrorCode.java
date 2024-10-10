package com.echall.platform.message.error.code;

import org.springframework.http.HttpStatus;

import com.echall.platform.message.status.QuestionServiceStatus;
import com.echall.platform.message.status.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum QuestionErrorCode implements ErrorCode {
	QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, QuestionServiceStatus.QUESTION_NOT_FOUND, "컨텐츠 조회 실패");

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
