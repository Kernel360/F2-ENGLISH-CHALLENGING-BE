package com.echall.platform.exception.content;

import com.echall.platform.exception.common.ApiErrorSubCategory;

public enum ApiContentErrorSubCategory implements ApiErrorSubCategory {
	CONTENT_NOT_FOUND("존재하지 않는 컨텐츠입니다."),

	CONTENT_ALREADY_EXISTS("이미 존재하는 컨텐츠입니다."),

	CONTENT_DEACTIVATE("삭제된 컨텐츠입니다.");

	private final String contentApiErrorSubCategory;

	ApiContentErrorSubCategory(String contentApiErrorSubCategory) {
		this.contentApiErrorSubCategory = contentApiErrorSubCategory;
	}

	@Override
	public String toString() {
		return String.format("[원인: %s]", this.contentApiErrorSubCategory);
	}
}
