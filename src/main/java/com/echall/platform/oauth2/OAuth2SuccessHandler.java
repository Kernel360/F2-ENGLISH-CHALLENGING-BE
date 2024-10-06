package com.echall.platform.oauth2;

import com.echall.platform.oauth2.domain.entity.RefreshToken;
import com.echall.platform.oauth2.domain.info.OAuth2UserPrincipal;
import com.echall.platform.oauth2.repository.RefreshTokenRepository;
import com.echall.platform.user.domain.entity.UserEntity;
import com.echall.platform.user.service.UserService;
import com.echall.platform.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
	public static final String ACCESS_TOKEN = "access_token";
	public static final String REFRESH_TOKEN = "refresh_token";
	public static final Duration ACCESS_TOKEN_EXPIRE = Duration.ofDays(1);
	public static final Duration REFRESH_TOKEN_EXPIRE = Duration.ofDays(7);

	@Value("${spring.security.oauth2.success.redirect-uri}")
	public String oAuth2SuccessRedirectUri;

	private final TokenProvider tokenProvider;
	private final RefreshTokenRepository refreshTokenRepository;
	private final UserService userService;
	private final CookieUtil cookieUtil;

	@Override
	public void onAuthenticationSuccess(
		HttpServletRequest request, HttpServletResponse response, Authentication authentication
	) throws IOException {
		OAuth2UserPrincipal oAuth2UserPrincipal = (OAuth2UserPrincipal) authentication.getPrincipal();

		UserEntity user = userService.getUserByOAuthUser(oAuth2UserPrincipal.getOAuth2UserInfo());

		String refreshToken = tokenProvider.generateRefreshToken(user);
		cookieUtil.addRefreshTokenCookie(request, response, refreshToken);

		String accessToken = tokenProvider.generateAccessToken(user);
		cookieUtil.addAccessTokenCookie(request, response, accessToken);

		saveRefreshToken(user.getId(), refreshToken);

		response.sendRedirect(oAuth2SuccessRedirectUri);
	}

	// Internal Methods=================================================================================================

	private void saveRefreshToken(Long userId, String newRefreshToken) {
		RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
			.map(entity -> entity.update(newRefreshToken))
			.orElse(new RefreshToken(userId, newRefreshToken));
		refreshTokenRepository.save(refreshToken);
	}
}
