package com.echall.platform.exception.user.oauth2;

import com.echall.platform.exception.common.ApiException;
import com.echall.platform.exception.common.ErrorCode;

import lombok.Getter;

@Getter
public class RefreshTokenNotFoundException extends ApiException {

	private String value;

	public RefreshTokenNotFoundException(String value) {
		this(value, ErrorCode.REFRESH_TOKEN_NOT_FOUND);
		this.value = value;
	}

	public RefreshTokenNotFoundException(String value, ErrorCode errorCode) {
		super(value, errorCode);
		this.value = value;
	}
}
