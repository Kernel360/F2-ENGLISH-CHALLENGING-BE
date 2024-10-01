package com.echall.platform.message.status;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BookmarkServiceStatus implements ServiceStatus {
	// success
	BOOKMARK_VIEW_SUCCESS("B-001"),
	BOOKMARK_UPDATE_SUCCESS("B-002"),

	// failure,
	BOOKMARK_VIEW_FAILURE("B-101")

	;
	private final String code;

	@Override
	public String getServiceStatus() {
		return code;
	}
}
