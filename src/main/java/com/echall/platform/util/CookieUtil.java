package com.echall.platform.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;


@Component
public class CookieUtil {

	@Value("${spring.profiles.active}")
	private String activeProfile;

	// TODO: https 연결하고 검토 필요
	public void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
		String cookieValue = createCookieValue(name, value, maxAge);

		response.addHeader("Set-Cookie", cookieValue);
	}

	public void removeCookie(HttpServletRequest request, HttpServletResponse response, String name) {
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
			.httpOnly(false)
			.path("/")
			.maxAge(maxAge);

		if ("local".equals(activeProfile)) {
			cookieBuilder.secure(false)
				.domain(".localhost");
		}

		if ("prod".equals(activeProfile)) {
			cookieBuilder.secure(true)
				.sameSite("None")
				.domain(".f2-english-fe.vercel.app");
		}

		return cookieBuilder.build().toString();
	}
}
