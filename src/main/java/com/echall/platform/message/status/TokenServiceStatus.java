package com.echall.platform.message.status;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TokenServiceStatus implements ServiceStatus {
	// success

	// failure
	REFRESH_TOKEN_NOT_FOUND("U-R-901")
	;
	private final String code;

	@Override
	public String getServiceStatus() {
		return code;
	}
}
