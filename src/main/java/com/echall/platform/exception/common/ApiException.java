package com.echall.platform.exception.common;

import org.springframework.http.HttpStatus;

import jakarta.annotation.Nullable;
import lombok.ToString;

@ToString
public abstract class ApiException extends RuntimeException {
	private final ApiErrorCategory category;
	private final ApiErrorSubCategory subCategory;

	public ApiException(
		ApiErrorCategory category,
		@Nullable ApiErrorSubCategory subCategory
	) {
		super(category.getErrorCategoryDescription());
		this.category = category;
		this.subCategory = subCategory;
	}

	/**
	 * 에러 서브-카테고리를 처리하기 위한 함수입니다.
	 */
	public abstract void processEachSubCategoryCase();

	public HttpStatus getHttpStatus() {
		return this.category.getErrorStatusCode();
	}

	public String getErrorCategoryDescription() {
		return this.category.getErrorCategoryDescription();
	}

	public String getErrorSubCategoryDescription() {
		return this.subCategory.toString();
	}

	public ApiErrorSubCategory getErrorSubCategory() {
		return this.subCategory;
	}

	public ApiErrorCategory getErrorCategory() {
		return this.category;
	}
}
