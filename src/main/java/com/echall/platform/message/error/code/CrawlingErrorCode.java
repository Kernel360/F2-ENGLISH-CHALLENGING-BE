package com.echall.platform.message.error.code;

import org.springframework.http.HttpStatus;

import com.echall.platform.message.status.CrawlingServiceStatus;
import com.echall.platform.message.status.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CrawlingErrorCode implements ErrorCode {
	CRAWLING_OUT_OF_BOUNDS(
		HttpStatus.BAD_REQUEST, CrawlingServiceStatus.CRAWLING_OUT_OF_BOUNDS, "유튜브 영상 길이가 8분 이상입니다."
	),
	SELENIUM_RUNTIME_ERROR(
		HttpStatus.NO_CONTENT, CrawlingServiceStatus.CRAWLING_SELENIUM_FAILURE, "셀레니움 런타임 에러"
	),
	CRAWLING_TRANSLATE_FAILURE(
		HttpStatus.CONFLICT, CrawlingServiceStatus.CRAWLING_TRANSLATE_FAILURE, "파이썬 번역 에러"
	),
	CRAWLING_JSOUP_FAILURE(
		HttpStatus.NO_CONTENT, CrawlingServiceStatus.CRAWLING_JSOUP_FAILURE, "JSOUP 런타임 에러"
	)

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
