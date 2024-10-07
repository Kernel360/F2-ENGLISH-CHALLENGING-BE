package com.echall.platform.config;

import com.echall.platform.message.error.exception.CommonException;
import com.echall.platform.oauth2.TokenProvider;
import com.echall.platform.util.CookieUtil;
import com.echall.platform.util.HttpServletResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
	private final TokenProvider tokenProvider;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		try {
			Cookie[] cookies = request.getCookies();

			if (cookies == null) {
				filterChain.doFilter(request, response);
				return;
			}

			Arrays.stream(cookies)
				.filter(cookie -> cookie.getName().equals(CookieUtil.ACCESS_TOKEN_NAME))
				.findFirst()
				.ifPresent(cookie -> {
						String token = cookie.getValue();
						if(tokenProvider.validateToken(token)){
							Authentication authentication = tokenProvider.getAuthentication(token);
							SecurityContextHolder.getContext().setAuthentication(authentication);
						}
				});

			filterChain.doFilter(request, response);

		} catch (CommonException e) {
			HttpServletResponseUtil.createErrorResponse(response, e);
		}
	}
}
