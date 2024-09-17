package com.echall.platform.config;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.echall.platform.oauth2.TokenProvider;
import com.echall.platform.oauth2.OAuth2SuccessHandler;

import groovy.util.logging.Slf4j;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {
	private final TokenProvider tokenProvider;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			filterChain.doFilter(request, response);
			return;
		}
		Arrays.stream(cookies)
			.filter(cookie -> cookie.getName().equals(OAuth2SuccessHandler.ACCESS_TOKEN))
			.findFirst()
			.ifPresent(cookie -> {
				try {
					String token = cookie.getValue();
					if(tokenProvider.validateToken(token)){
						Authentication authentication = tokenProvider.getAuthentication(token);
						SecurityContextHolder.getContext().setAuthentication(authentication);
					}
				} catch (Exception e) {
					logger.error("Failed to validate token", e);
				}
			});
		filterChain.doFilter(request, response);
	}
}
