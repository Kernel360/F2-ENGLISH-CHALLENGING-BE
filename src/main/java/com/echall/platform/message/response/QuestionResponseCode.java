package com.echall.platform.message.response;

import org.springframework.http.HttpStatus;

import com.echall.platform.message.status.QuestionServiceStatus;
import com.echall.platform.message.status.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum QuestionResponseCode implements ResponseCode {
	QUESTION_CREATE_SUCCESS(HttpStatus.CREATED, QuestionServiceStatus.QUESTION_CREATE_SUCCESS, "문제 생성 성공"),
	QUESTION_VIEW_SUCCESS(HttpStatus.OK, QuestionServiceStatus.QUESTION_VIEW_SUCCESS, "문제 조회 성공"),

	;

	private final HttpStatus code;
	private final ServiceStatus serviceStatus;
	private final String message;

	@Override
	public HttpStatus getStatus() {
		return code;
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
