// TODO: 추후에도 필요가 없어지면 삭제

//package com.echall.platform.oauth2.repository;
//
//import java.util.Objects;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
//import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.web.util.WebUtils;
//
//import com.echall.platform.util.CookieUtil;
//
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//@Component
//@RequiredArgsConstructor
//public class OAuth2AuthorizationRequestBasedOnCookieRepository
//	implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
//	public final static String OAUTH2_AUTHORIZATION_REQUEST_COOKIE = "oauth2_authorization_request";
//	public final static int COOKIE_EXPIRE_SECONDS = 18000;
//	private final CookieUtil cookieUtil;
//
//	@Override
//	public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
//		Cookie cookie = WebUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE);
//		if(cookie == null) {
//			return null;
//		}
//		return cookieUtil.deserialize(cookie, OAuth2AuthorizationRequest.class);
//	}
//
//	@Override
//	public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request,
//		HttpServletResponse response) {
//		if(authorizationRequest == null) {
//			removeAuthorizationRequest(request,response);
//			return;
//		}
//		cookieUtil.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE,
//			cookieUtil.serialize(authorizationRequest), COOKIE_EXPIRE_SECONDS);
//	}
//
//	@Override
//	public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
//		HttpServletResponse response) {
//		return this.loadAuthorizationRequest(request);
//	}
//
//	public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
//		cookieUtil.removeCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE);
//	}
//}
