package com.echall.platform.oauth2;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.echall.platform.annotation.LoginLogging;
import com.echall.platform.message.error.exception.CommonException;
import com.echall.platform.oauth2.domain.info.OAuth2UserPrincipal;
import com.echall.platform.oauth2.service.RefreshTokenService;
import com.echall.platform.user.domain.entity.UserEntity;
import com.echall.platform.user.service.UserService;
import com.echall.platform.util.CookieUtil;
import com.echall.platform.util.HttpServletResponseUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
	private final TokenProvider tokenProvider;
	private final RefreshTokenService refreshTokenService;
	private final UserService userService;
	private final CookieUtil cookieUtil;

	@Value("${spring.security.oauth2.success.redirect-uri}")
	public String oAuth2SuccessRedirectUri;

	@Override
	@LoginLogging
	public void onAuthenticationSuccess(
		HttpServletRequest request, HttpServletResponse response, Authentication authentication
	) throws IOException {
		try {
			OAuth2UserPrincipal oAuth2UserPrincipal = (OAuth2UserPrincipal)authentication.getPrincipal();

			UserEntity user = userService.getUserByOAuthUser(oAuth2UserPrincipal);

			String refreshToken = tokenProvider.generateRefreshToken(user);
			cookieUtil.addRefreshTokenCookie(request, response, refreshToken);

			String accessToken = tokenProvider.generateAccessToken(user);
			cookieUtil.addAccessTokenCookie(request, response, accessToken);

			refreshTokenService.saveRefreshToken(user, refreshToken);
			oAuth2SuccessRedirectUri += "?" + request.getRequestURI().split("\\?")[1];

			response.sendRedirect(oAuth2SuccessRedirectUri);

		} catch (CommonException e) {
			log.error(e.getErrorCode().getCode() + " : " + e.getErrorCode().getMessage());

			HttpServletResponseUtil.createErrorResponse(response, e);
		}

		// TODO: 나머지 Exception에 대한 응답 컨벤션에 따라 추가될 수 있음 ex) Server Internal Error
	}
}
