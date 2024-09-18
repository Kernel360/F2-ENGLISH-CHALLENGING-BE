package com.echall.platform.exception.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	Logger defaultLog = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	Logger exceptionLog = LoggerFactory.getLogger("ExceptionLogger");

	// 공통 로깅 메서드: 예외가 발생할 때 로그를 기록하는 메서드입니다.
	private void logError(Exception e) {
		defaultLog.error(e.getMessage());
		exceptionLog.error(e.getMessage(), e);
	}

	// 공통 응답 생성 메서드: 에러 응답을 생성해주는 메서드입니다.
	// 상태 코드, 에러 코드, 메시지를 기반으로 ErrorResponse 객체를 생성합니다.
	private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, ErrorCode errorCode, String message) {
		ErrorResponse response = ErrorResponse.of(status.value(), errorCode, message);
		return new ResponseEntity<>(response, status);
	}

	// 오버로딩: 메시지 없이 기본 응답을 생성하는 메서드입니다.
	private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, ErrorCode errorCode) {
		return buildErrorResponse(status, errorCode, null);
	}

	/**
	 * Validated - 바인딩 에러가 발생할 때 처리됩니다.
	 * @Valid 혹은 @Validated 애노테이션을 사용한 객체 검증 실패 시 발생하는 예외입니다.
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		logError(e);
		return buildErrorResponse(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult().toString());
	}

	/**
	 * @RequestParam, @PathVariable 등에서 타입이 맞지 않을 때 발생합니다.
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
		logError(e);
		return buildErrorResponse(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_INPUT_VALUE, e.getMessage());
	}

	/**
	 * 지원되지 않는 HTTP Method로 요청이 왔을 때 발생합니다.
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
		logError(e);
		return buildErrorResponse(HttpStatus.METHOD_NOT_ALLOWED, ErrorCode.METHOD_NOT_ALLOWED);
	}

	/**
	 * @RequestBody에서 JSON 형식이 잘못되었거나 타입이 맞지 않을 때 발생합니다.
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		logError(e);
		return buildErrorResponse(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_TYPE_VALUE, e.getMessage());
	}

	/**
	 * 커스텀 예외인 ApiException을 처리합니다.
	 * ApiException 내부에 정의된 ErrorCode에 따라 적절한 상태 코드와 메시지를 반환합니다.
	 */
	@ExceptionHandler(ApiException.class)
	protected ResponseEntity<ErrorResponse> handleApiException(ApiException e) {
		logError(e);  // 예외 로깅
		return buildErrorResponse(e.getStatus(), e.getErrorCode(), e.getMessage());
	}

	/**
	 * 기타 발생할 수 있는 모든 예외를 처리합니다.
	 * 명시적으로 처리되지 않은 예외들은 여기서 처리됩니다.
	 */
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponse> handleException(Exception e) {
		logError(e);
		return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR);
	}

}