package com.echall.platform.exception.review;

import com.echall.platform.exception.challenge.ApiChallengeErrorSubCategory;
import com.echall.platform.exception.common.ApiErrorCategory;
import com.echall.platform.exception.common.ApiErrorSubCategory;
import com.echall.platform.exception.common.ApiException;

import lombok.Builder;

public class ApiReviewException extends ApiException {

	@Builder
	protected ApiReviewException(
		ApiErrorCategory category,
		ApiChallengeErrorSubCategory subCategory
	) {
		super(category, subCategory);
	}

	@Override
	public void processEachSubCategoryCase() {
		ApiErrorSubCategory subCategory = this.getErrorSubCategory();

		switch ((ApiReviewErrorSubCategory)subCategory) {
			case REVIEW_NOT_FOUND -> {
			}
			case REVIEW_ALREADY_EXISTS -> {
			}
			case REVIEW_DEACTIVATE -> {
			}
			default -> {
			}
		}
	}
}
