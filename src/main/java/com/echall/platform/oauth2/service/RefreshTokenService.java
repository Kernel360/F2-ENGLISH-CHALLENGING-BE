package com.echall.platform.oauth2.service;

import org.springframework.stereotype.Service;

import com.echall.platform.oauth2.domain.entity.RefreshToken;
import com.echall.platform.oauth2.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
	private final RefreshTokenRepository refreshTokenRepository;

	public RefreshToken getRefreshToken(String refreshToken) {
		return refreshTokenRepository.findByRefreshToken(refreshToken)
			.orElseThrow(
				() -> new RuntimeException(refreshToken)
			);
	}

}
