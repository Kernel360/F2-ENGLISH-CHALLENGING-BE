package com.echall.platform.message.status;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserServiceStatus implements ServiceStatus{
	// success
	USER_INPUT_INFO("U-001"),
	USER_GET_INFO("U-002"),
	USER_UPDATE_INFO("U-003"),

	// failure
	USER_NOT_FOUND("U-101"),
	USER_FAIL_DEACTIVATE("U-102"),
	USER_FAIL_SUSPEND("U-103"),
	;

	private final String code;

	@Override
	public String getServiceStatus() {
		return code;
	}
}
