package com.echall.platform.message;

import com.fasterxml.jackson.annotation.JsonInclude;

public record ApiCustomResponse<T>(
	String code,
	String message,
	@JsonInclude(JsonInclude.Include.NON_NULL)
	T data
) {
	public static <T> ApiCustomResponse<T> of(
		StatusCode statusCode, T data
	) {
		return new ApiCustomResponse<>(
			statusCode.getCode(),
			statusCode.getMessage(),
			data
		);
	}

	public static <T> ApiCustomResponse<T> of(StatusCode statusCode) {
		return new ApiCustomResponse<>(
			statusCode.getCode(),
			statusCode.getMessage(),
			null
		);
	}
}
