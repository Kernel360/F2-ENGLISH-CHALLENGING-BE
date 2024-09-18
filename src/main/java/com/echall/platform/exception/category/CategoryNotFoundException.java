package com.echall.platform.exception.category;

import com.echall.platform.exception.common.ApiException;
import com.echall.platform.exception.common.ErrorCode;

import lombok.Getter;

@Getter
public class CategoryNotFoundException extends ApiException {

	private String value;

	public CategoryNotFoundException(String value) {
		this(value, ErrorCode.CATEGORY_NOT_FOUND);
		this.value = value;
	}

	public CategoryNotFoundException(String value, ErrorCode errorCode) {
		super(value, errorCode);
		this.value = value;
	}
}
