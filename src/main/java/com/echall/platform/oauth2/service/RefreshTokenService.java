package com.echall.platform.oauth2.service;

import com.echall.platform.message.error.exception.CommonException;
import com.echall.platform.oauth2.domain.entity.RefreshToken;
import com.echall.platform.oauth2.repository.RefreshTokenRepository;
import com.echall.platform.user.domain.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.echall.platform.message.error.code.TokenErrorCode.REFRESH_TOKEN_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
	private final RefreshTokenRepository refreshTokenRepository;

	@Transactional(readOnly = true)
	public RefreshToken getRefreshToken(String refreshToken) {
		return refreshTokenRepository.findByRefreshToken(refreshToken)
			.orElseThrow(
				() -> new CommonException(REFRESH_TOKEN_NOT_FOUND)
			);
	}

	@Transactional
	public void saveRefreshToken(UserEntity user, String newRefreshToken) {
		RefreshToken refreshToken = refreshTokenRepository.findByUserId(user.getId())
			.orElseGet(() -> {
				RefreshToken initialRefreshToken = RefreshToken.of(user.getId(), newRefreshToken);

				return refreshTokenRepository.save(initialRefreshToken);
			});

		refreshToken.updateRefreshToken(newRefreshToken);
	}
}
