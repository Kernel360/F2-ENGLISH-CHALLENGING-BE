package com.echall.platform.oauth2.domain;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo {

	private final Map<String, Object> naverAccount;

	public NaverUserInfo(Map<String, Object> attributes) {
		this.naverAccount = attributes;
	}

	@Override
	public String getProviderId() {
		LinkedHashMap<String, Object> accountInfo = (LinkedHashMap<String, Object>) naverAccount.get("response");

		return String.valueOf(accountInfo.get("id"));
	}

	@Override
	public String getProvider() {
		return "naver";
	}

	@Override
	public String getEmail() {
		LinkedHashMap<String, Object> accountInfo = (LinkedHashMap<String, Object>)naverAccount.get("response");
		return String.valueOf(accountInfo.get("email"));
	}

	@Override
	public String getUsername() {
		LinkedHashMap<String, Object> accountInfo = (LinkedHashMap<String, Object>)naverAccount.get("response");
		return String.valueOf(accountInfo.get("name"));
	}

}