package com.echall.platform.oauth2.domain;

import java.util.LinkedHashMap;
import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo {

	private final Map<String, Object> kakaoAccount;

	public KakaoUserInfo(Map<String, Object> attributes ) {
		this.kakaoAccount = attributes;
	}

	@Override
	public String getProviderId() {
		return String.valueOf(this.kakaoAccount.get("id"));
	}

	@Override
	public String getProvider() {
		return "kakao";
	}

	@Override
	public String getEmail() {
		LinkedHashMap<String, Object> accountInfo = (LinkedHashMap<String, Object>)kakaoAccount.get("kakao_account");
		assert accountInfo != null;
		return String.valueOf(accountInfo.get("email"));
	}

	@Override
	public String getUsername() {
		return String.valueOf(kakaoAccount.get("username"));
	}

}