package com.echall.platform.oauth2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.echall.platform.oauth2.domain.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	Optional<RefreshToken> findByUserId(Long userId);

	Optional<RefreshToken> findByRefreshToken(String refreshToken);

	void deleteByUserId(Long userId);
}
