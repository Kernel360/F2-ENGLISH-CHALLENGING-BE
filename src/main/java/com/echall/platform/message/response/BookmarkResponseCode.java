package com.echall.platform.message.response;

import org.springframework.http.HttpStatus;

import com.echall.platform.message.status.BookmarkServiceStatus;
import com.echall.platform.message.status.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BookmarkResponseCode implements ResponseCode {
	BOOKMARK_VIEW_SUCCESS(HttpStatus.OK, BookmarkServiceStatus.BOOKMARK_VIEW_SUCCESS, "북마크 조회 성공"),
	BOOKMARK_UPDATE_SUCCESS(HttpStatus.OK, BookmarkServiceStatus.BOOKMARK_UPDATE_SUCCESS, "북마크 수정 성공")
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
