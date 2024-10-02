package com.echall.platform.message;

import org.springframework.http.HttpStatus;

public interface StatusCode {
	HttpStatus getStatus();
	String getCode();
	String getMessage();
}
