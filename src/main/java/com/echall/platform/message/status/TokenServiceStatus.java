package com.echall.platform.message.status;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TokenServiceStatus implements ServiceStatus {
	// success

	// failure
	REFRESH_TOKEN_NOT_FOUND("R-101")
	;
	private final String code;

	@Override
	public String getServiceStatus() {
		return code;
	}
}
