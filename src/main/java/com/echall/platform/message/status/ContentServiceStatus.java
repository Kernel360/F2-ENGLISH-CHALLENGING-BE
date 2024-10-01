package com.echall.platform.message.status;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ContentServiceStatus implements ServiceStatus {
	// success
	CONTENT_CREATE_SUCCESS("C-001"),
	CONTENT_VIEW_SUCCESS("C-002"),
	CONTENT_MODIFY_SUCCESS("C-003"),
	CONTENT_DEACTIVATE_SUCCESS("C-004"),
	// error
	CONTENT_NOT_FOUND("C-101")
	;

	private final String code;

	@Override
	public String getServiceStatus() {
		return code;
	}
}
