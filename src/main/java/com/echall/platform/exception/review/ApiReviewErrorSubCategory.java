package com.echall.platform.exception.review;

import com.echall.platform.exception.common.ApiErrorSubCategory;

public enum ApiReviewErrorSubCategory implements ApiErrorSubCategory {
	REVIEW_NOT_FOUND("존재하지 않는 리뷰입니다."),

	REVIEW_ALREADY_EXISTS("이미 존재하는 리뷰입니다."),

	REVIEW_DEACTIVATE("삭제된 리뷰입니다.");

	private final String reviewApiErrorSubCategory;

	ApiReviewErrorSubCategory(String reviewApiErrorSubCategory) {
		this.reviewApiErrorSubCategory = reviewApiErrorSubCategory;
	}

	@Override
	public String toString() {
		return String.format("[원인: %s]", this.reviewApiErrorSubCategory);
	}
}
