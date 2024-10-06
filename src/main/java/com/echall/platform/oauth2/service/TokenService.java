package com.echall.platform.oauth2.service;

import com.echall.platform.oauth2.TokenProvider;
import com.echall.platform.user.domain.entity.UserEntity;
import com.echall.platform.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
	private final TokenProvider tokenProvider;
	private final RefreshTokenService refreshTokenService;
	private final UserService userService;

	public String createNewAccessToken(String refreshToken) {
		tokenProvider.validateToken(refreshToken);

		Long userId = refreshTokenService.getRefreshToken(refreshToken).getUserId();
		UserEntity user = userService.getUserById(userId);

		return tokenProvider.generateAccessToken(user);
	}
}
