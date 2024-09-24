package com.echall.platform.oauth2;

import static java.lang.Boolean.*;

import java.io.IOException;
import java.time.Duration;
import java.util.LinkedHashMap;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.echall.platform.oauth2.domain.entity.RefreshToken;
import com.echall.platform.oauth2.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.echall.platform.oauth2.repository.RefreshTokenRepository;
import com.echall.platform.user.domain.entity.UserEntity;
import com.echall.platform.user.service.UserService;
import com.echall.platform.util.CookieUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	public static final String ACCESS_TOKEN = "access_token";
	public static final String REFRESH_TOKEN = "refresh_token";
	public static final Duration ACCESS_TOKEN_EXPIRE = Duration.ofDays(1);
	public static final Duration REFRESH_TOKEN_EXPIRE = Duration.ofDays(7);
	public static final String OAUTH2_SUCCESS_REDIRECTION_PATH = "http://localhost:3000"; // TODO: to .env

	private final TokenProvider tokenProvider;
	private final RefreshTokenRepository refreshTokenRepository;
	private final OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository;
	private final UserService userService;

	@Override
	public void onAuthenticationSuccess(
		HttpServletRequest request, HttpServletResponse response, Authentication authentication
	) throws IOException {
		OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
		String email = null;
		if (oAuth2User.getAttribute("email") != null) {
			email = oAuth2User.getAttribute("email");
		}
		if (oAuth2User.getAttribute("response") != null) {
			email = ((LinkedHashMap<String, Object>)oAuth2User.getAttribute("response")).get("email").toString();
		}
		if (oAuth2User.getAttribute("kakao_account") != null) {
			email = ((LinkedHashMap<String, Object>)oAuth2User.getAttribute("kakao_account")).get("email").toString();
		}

		assert email != null;
		UserEntity user = userService.getUserByEmail(email);
		String refreshToken = tokenProvider.generateToken(user, REFRESH_TOKEN_EXPIRE);
		saveRefreshToken(user.getId(), refreshToken);
		addTokenToCookie(request, response, refreshToken, TRUE);

		String accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_EXPIRE);
		addTokenToCookie(request, response, accessToken, FALSE);

		clearAuthenticationAttributes(request);
		oAuth2AuthorizationRequestBasedOnCookieRepository.removeAuthorizationRequestCookies(request, response);
		getRedirectStrategy().sendRedirect(request, response, OAUTH2_SUCCESS_REDIRECTION_PATH);
	}

	// Internal Methods=================================================================================================

	private void saveRefreshToken(Long userId, String newRefreshToken) {
		RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
			.map(entity -> entity.update(newRefreshToken))
			.orElse(new RefreshToken(userId, newRefreshToken));
		refreshTokenRepository.save(refreshToken);
	}

	private void addTokenToCookie(
		HttpServletRequest request, HttpServletResponse response,
		String refreshToken, boolean type
	) {
		String token = null;
		long duration;

		if (type) {
			token = REFRESH_TOKEN;
			duration = REFRESH_TOKEN_EXPIRE.toSeconds();
		} else {
			token = ACCESS_TOKEN;
			duration = ACCESS_TOKEN_EXPIRE.toSeconds();
		}

		CookieUtil.removeCookie(request, response, token);
		CookieUtil.addCookie(response, token, refreshToken,
			Math.toIntExact(duration));
	}

}
