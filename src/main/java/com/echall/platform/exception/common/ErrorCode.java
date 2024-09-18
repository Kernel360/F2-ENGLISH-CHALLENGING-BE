package com.echall.platform.exception.common;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {

	// Common Error
	INVALID_INPUT_VALUE("C-001", "Invalid Input Value", HttpStatus.BAD_REQUEST),
	METHOD_NOT_ALLOWED("C-002", "Method Not Allowed", HttpStatus.METHOD_NOT_ALLOWED),
	INTERNAL_SERVER_ERROR("C-004", "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR),
	INVALID_TYPE_VALUE("C-005", "Invalid Type Value", HttpStatus.BAD_REQUEST),
	HANDLE_ACCESS_DENIED("C-006", "Access Denied", HttpStatus.FORBIDDEN),

	// Business Error
	DUPLICATE("B-001", "Duplicate Value", HttpStatus.CONFLICT),
	NOT_FOUND("B-002", "Entity Not Found", HttpStatus.NOT_FOUND),

	// User Error
	EMAIL_DUPLICATE("M-001", "Duplicate Email", HttpStatus.CONFLICT),
	NICKNAME_DUPLICATE("M-002", "Duplicate Nickname", HttpStatus.CONFLICT),
	USER_ID_INVALID("M-003", "Invalid User ID", HttpStatus.BAD_REQUEST),
	USER_NOT_FOUND("M-004", "User Not Found", HttpStatus.NOT_FOUND),
	USER_UNAUTHORIZED("M-005", "Unauthorized User", HttpStatus.UNAUTHORIZED),

	//Oauth Error
	REFRESH_TOKEN_NOT_FOUND("O-001", "Refresh Token Not Found", HttpStatus.NOT_FOUND),

	// Category Error
	CATEGORY_NOT_FOUND("CT-001", "Category Not Found", HttpStatus.NOT_FOUND),
	CATEGORY_DUPLICATE("CT-002", "Duplicate Category", HttpStatus.CONFLICT),
	CATEGORY_INVALID("CT-003", "Invalid Category Data", HttpStatus.BAD_REQUEST),

	// Learning Content Error
	CONTENT_NOT_FOUND("LC-001", "Learning Content Not Found", HttpStatus.NOT_FOUND),
	CONTENT_DUPLICATE("LC-002", "Duplicate Learning Content", HttpStatus.CONFLICT),
	CONTENT_INVALID("LC-003", "Invalid Learning Content Data", HttpStatus.BAD_REQUEST),
	CONTENT_UPLOAD_FAILED("LC-004", "Content Upload Failed", HttpStatus.INTERNAL_SERVER_ERROR);

	private final String code;
	private final String message;
	private final HttpStatus status;

	ErrorCode(String code, String message, HttpStatus status) {
		this.code = code;
		this.message = message;
		this.status = status;
	}
}
