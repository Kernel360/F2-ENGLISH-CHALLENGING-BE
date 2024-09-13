package com.echall.platform.exception.content;

import com.echall.platform.exception.common.ApiErrorCategory;
import com.echall.platform.exception.common.ApiErrorSubCategory;
import com.echall.platform.exception.common.ApiException;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ApiContentException extends ApiException {

	@Builder
	protected ApiContentException(
		ApiErrorCategory category,
		ApiContentErrorSubCategory subCategory
	) {
		super(category, subCategory);
	}

	@Override
	public void processEachSubCategoryCase() {
		ApiErrorSubCategory subCategory = this.getErrorSubCategory();

		switch ( (ApiContentErrorSubCategory)subCategory ) {
			case CONTENT_NOT_FOUND -> {}
			case CONTENT_ALREADY_EXISTS -> {}
			default -> {}
		}
	}
}
