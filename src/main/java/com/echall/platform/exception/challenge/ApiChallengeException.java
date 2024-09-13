package com.echall.platform.exception.challenge;

import com.echall.platform.exception.common.ApiErrorCategory;
import com.echall.platform.exception.common.ApiErrorSubCategory;
import com.echall.platform.exception.common.ApiException;

import lombok.Builder;

public class ApiChallengeException extends ApiException {

	@Builder
	protected ApiChallengeException(
		ApiErrorCategory category,
		ApiChallengeErrorSubCategory subCategory
	) {
		super(category, subCategory);
	}

	@Override
	public void processEachSubCategoryCase() {
		ApiErrorSubCategory subCategory = this.getErrorSubCategory();

		switch ( (ApiChallengeErrorSubCategory)subCategory ) {
			case CHALLENGE_NOT_FOUND -> {}
			case CHALLENGE_ALREADY_EXISTS -> {}
			case CHALLENGE_DEACTIVATE -> {}
			default -> {}
		}
	}
}
