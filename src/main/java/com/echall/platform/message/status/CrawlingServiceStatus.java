package com.echall.platform.message.status;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CrawlingServiceStatus implements ServiceStatus{
	// success
	CRAWLING_YOUTUBE_SUCCESS("U-CR-001"),
	CRAWLING_CNN_SUCCESS("U-CR-002"),

	// error,
	CRAWLING_YOUTUBE_FAILURE("U-CR-901"),
	CRAWLING_CNN_FAILURE("U-CR-902"),
	CRAWLING_OUT_OF_BOUNDS("U-CR-903"),
	CRAWLING_SELENIUM_FAILURE("U-CR-904")


	;
	private final String code;

	@Override
	public String getServiceStatus() {
		return code;
	}
}
