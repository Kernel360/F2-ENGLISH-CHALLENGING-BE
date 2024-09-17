package com.echall.platform.oauth2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.echall.platform.oauth2.domain.dto.TokenDto;
import com.echall.platform.oauth2.service.TokenService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class TokenApiController {
	private final TokenService tokenService;

	@PostMapping("/api/token")
	public ResponseEntity<TokenDto.CreateAccessTokenResponse> createToken(
		@RequestBody TokenDto.CreateAccessTokenRequest request
	) {
		String newAccessToken = tokenService.createNewAccessToken(request.refreshToken());
		return ResponseEntity.status(HttpStatus.CREATED).body(new TokenDto.CreateAccessTokenResponse(newAccessToken));
	}
}
