package com.echall.platform.util;

import java.time.Duration;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CookieUtil {
	public static final String ACCESS_TOKEN_NAME = "access_token";
	public static final String REFRESH_TOKEN_NAME = "refresh_token";
	public static final Duration ACCESS_TOKEN_COOKIE_EXPIRE = Duration.ofDays(1);
	public static final Duration REFRESH_TOKEN_COOKIE_EXPIRE = Duration.ofDays(7);

	@Value("${spring.profiles.active}")
	private String activeProfile;

	public boolean verifyAccessTokenCookie(Cookie[] cookies) {
		return cookies != null && Arrays.stream(cookies).anyMatch(cookie -> cookie.getName().equals(ACCESS_TOKEN_NAME));
	}

	public void addAccessTokenCookie(HttpServletRequest request, HttpServletResponse response, String accessToken) {
		removeCookie(request, response, ACCESS_TOKEN_NAME);
		addCookie(response, ACCESS_TOKEN_NAME, accessToken, (int)ACCESS_TOKEN_COOKIE_EXPIRE.toSeconds());
	}

	public void addRefreshTokenCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
		removeCookie(request, response, REFRESH_TOKEN_NAME);
		addCookie(response, REFRESH_TOKEN_NAME, refreshToken, (int)REFRESH_TOKEN_COOKIE_EXPIRE.toSeconds());
	}

	public void removeAccessTokenCookie(HttpServletRequest request, HttpServletResponse response) {
		removeCookie(request, response, ACCESS_TOKEN_NAME);
	}

	public void removeRefreshTokenCookie(HttpServletRequest request, HttpServletResponse response) {
		removeCookie(request, response, REFRESH_TOKEN_NAME);
	}

	// Internal Methods=================================================================================================
	// TODO: https 연결하고 검토 필요
	private void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
		String cookieValue = createCookieValue(name, value, maxAge);

		response.addHeader("Set-Cookie", cookieValue);
	}

	private void removeCookie(HttpServletRequest request, HttpServletResponse response, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					String cookieValue = createCookieValue(name, "", 0);

					response.addHeader("Set-Cookie", cookieValue);
				}
			}
		}
	}

	// TODO: 추후에도 필요가 없어지면 삭제
	//	public String serialize(Object obj) {
	//		return Base64.getUrlEncoder()
	//			.encodeToString(SerializationUtils.serialize(obj));
	//	}
	//
	//	public <T> T deserialize(Cookie cookie, Class<T> cls) {
	//		byte[] decodedBytes = Base64.getUrlDecoder().decode(cookie.getValue());
	//
	//		try (
	//			 ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(decodedBytes))
	//		) {
	//			return cls.cast(objectInputStream.readObject());
	//		} catch (IOException | ClassNotFoundException e) {
	//			throw new IllegalArgumentException("Failed to deserialize object", e);
	//		}
	//	}

	private String createCookieValue(String name, String value, int maxAge) {
		ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie.from(name, value)
			.httpOnly(true)
			.path("/")
			.maxAge(maxAge);

		if ("local".equals(activeProfile)) {
			cookieBuilder.secure(false)
				.domain(".localhost");
		}

		if ("dev".equals(activeProfile) || "prod".equals(activeProfile)) {
			cookieBuilder.secure(true)
				.sameSite("None")
				.domain(".biengual.store");
		}

		return cookieBuilder.build().toString();
	}
}
