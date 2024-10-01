package com.echall.platform.user.controller;

import static com.echall.platform.message.response.UserResponseCode.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.echall.platform.message.ApiCustomResponse;
import com.echall.platform.message.ResponseEntityFactory;
import com.echall.platform.user.domain.dto.UserRequestDto;
import com.echall.platform.user.domain.dto.UserResponseDto;
import com.echall.platform.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/u1/user")
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "*", methods = RequestMethod.GET)
@Tag(name = "User Private API", description = "가입된 유저 공통 API")
public class UserApiController {

	private final UserService userService;

	@PostMapping("/input-info")
	@Operation(summary = "소셜 회원가입 후 정보 입력", description = "회원 가입 시 정보 입력 할 때 사용하는 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "202", description = "이미 가입된 계정입니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "404", description = "데이터베이스 연결에 실패하였습니다.", content = @Content(mediaType = "application/json")),
	})
	public ResponseEntity<ApiCustomResponse<UserResponseDto.UserUpdateResponse>> setNewUserInfo(
		@RequestBody UserRequestDto.UserUpdateRequest userUpdateRequest,
		Authentication authentication
	) {

		return ResponseEntityFactory
			.toResponseEntity(USER_INPUT_INFO, userService.updateUserInfo(userUpdateRequest, authentication.getName()));
	}

	@GetMapping("/me")
	@Operation(summary = "회원 정보 조회", description = "유저가 본인의 정보를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "404", description = "데이터베이스 연결에 실패하였습니다.", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<ApiCustomResponse<UserResponseDto.UserMyPageResponse>> getMyPage(
		Authentication authentication) {

		return ResponseEntityFactory
			.toResponseEntity(USER_GET_INFO, userService.getMyPage(authentication.getName()));
	}

	@PatchMapping("/me")
	@Operation(summary = "회원 정보 수정", description = "유저가 본인의 정보를 수정합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "404", description = "데이터베이스 연결에 실패하였습니다.", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<ApiCustomResponse<UserResponseDto.UserUpdateResponse>> updateExistedUserInfo(
		@RequestBody UserRequestDto.UserUpdateRequest userUpdateRequest,
		Authentication authentication
	) {

		return ResponseEntityFactory
			.toResponseEntity(
				USER_UPDATE_INFO, userService.updateUserInfo(userUpdateRequest, authentication.getName())
			);
	}

	@GetMapping("/me/challenge")
	@Operation(summary = "회원 챌린지 조회", description = "유저가 본인의 챌린지를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "404", description = "데이터베이스 연결에 실패하였습니다.", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<ApiCustomResponse<UserResponseDto.UserChallengeResponse>> userChallenge(
		Authentication authentication
	) {
		return ResponseEntityFactory
			.toResponseEntity(USER_GET_CHALLENGE, userService.getMyChallenge(authentication.getName()));
	}

}
