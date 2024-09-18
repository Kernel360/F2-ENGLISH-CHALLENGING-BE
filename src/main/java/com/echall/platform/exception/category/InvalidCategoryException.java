package com.echall.platform.exception.category;

import com.echall.platform.exception.common.ApiException;
import com.echall.platform.exception.common.ErrorCode;

import lombok.Getter;

@Getter
public class InvalidCategoryException extends ApiException {

	private String value;

	public InvalidCategoryException(String value) {
		this(value, ErrorCode.CATEGORY_INVALID);
		this.value = value;
	}

	public InvalidCategoryException(String value, ErrorCode errorCode) {
		super(value, errorCode);
		this.value = value;
	}

}
