package com.echall.platform.exception.user;

import com.echall.platform.exception.common.ApiException;
import com.echall.platform.exception.common.ErrorCode;

import lombok.Getter;

@Getter
public class UserNotFoundException extends ApiException {
	private String value;

	public UserNotFoundException(String value) {
		this(value, ErrorCode.USER_NOT_FOUND);
	}

	public UserNotFoundException(String value, ErrorCode errorCode) {
		super(value, errorCode);
		this.value = value;
	}
}

