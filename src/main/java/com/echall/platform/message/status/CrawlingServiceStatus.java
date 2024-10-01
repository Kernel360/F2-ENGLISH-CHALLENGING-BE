package com.echall.platform.message.status;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CrawlingServiceStatus implements ServiceStatus{
	// success
	CRAWLING_YOUTUBE_SUCCESS("CR-001"),
	CRAWLING_CNN_SUCCESS("CR-002"),

	// error,
	CRAWLING_YOUTUBE_FAILURE("CR-101"),
	CRAWLING_CNN_FAILURE("CR-102"),
	CRAWLING_OUT_OF_BOUNDS("CR-103"),
	CRAWLING_SELENIUM_FAILURE("CR-104")


	;
	private final String code;

	@Override
	public String getServiceStatus() {
		return code;
	}
}
