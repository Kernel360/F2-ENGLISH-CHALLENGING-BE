package com.echall.platform.message.error.code;

import org.springframework.http.HttpStatus;

import com.echall.platform.message.status.BookmarkServiceStatus;
import com.echall.platform.message.status.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BookmarkErrorCode implements ErrorCode {
	BOOKMARK_NOT_FOUND(
		HttpStatus.NOT_FOUND, BookmarkServiceStatus.BOOKMARK_NOT_FOUND, "등록된 북마크가 없습니다."
	),
	BOOKMARK_DESCRIPTION_SAME(
		HttpStatus.BAD_REQUEST, BookmarkServiceStatus.BOOKMARK_DESCRIPTION_SAME, "북마크 설명이 기존과 동일합니다."
	),
	BOOKMARK_ALREADY_EXISTS(
		HttpStatus.BAD_REQUEST, BookmarkServiceStatus.BOOKMARK_ALREADY_EXISTS, "이미 존재하는 북마크입니다."
	),
	BOOKMARK_WORD_NEED_SENTENCE(
		HttpStatus.BAD_REQUEST, BookmarkServiceStatus.BOOKMARK_WORD_NEED_SENTENCE,
		"단어 북마크를 지정하려면 문장 인덱스가 필요합니다.");

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
