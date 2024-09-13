package com.echall.platform.exception.user;

import com.echall.platform.exception.common.ApiErrorCategory;
import com.echall.platform.exception.common.ApiErrorSubCategory;
import com.echall.platform.exception.common.ApiException;

import lombok.Builder;
import lombok.Getter;

/**
 * 사용자 기능에 대한 비즈니스 로직 예외 입니다.
 */
@Getter
public class ApiUserException extends ApiException {

	@Builder
	protected ApiUserException(
		ApiErrorCategory category,
		ApiUserErrorSubCategory subCategory
	) {
		super(category, subCategory);
	}

	@Override
	public void processEachSubCategoryCase() {
		ApiErrorSubCategory subCategory = this.getErrorSubCategory();

		switch ( (ApiUserErrorSubCategory)subCategory ) {
			case USER_NOT_FOUND -> {}
			case USER_ALREADY_EXISTS -> {}
			case USER_ALREADY_LOGGED_IN -> {}
			case USER_SCRAP_DUPLICATION -> {}
			default -> {}
		}
	}
}
