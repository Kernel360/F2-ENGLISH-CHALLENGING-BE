package com.echall.platform.oauth2.domain;

import java.util.Map;

public class GoogleUserInfo implements OAuth2UserInfo {
	private final Map<String, Object> attributes; // oauth2User.getAttributes()

	public GoogleUserInfo(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String getProviderId() {
		return String.valueOf(attributes.get("sub"));
	}

	@Override
	public String getProvider() {
		return "google";
	}

	@Override
	public String getEmail() {
		return String.valueOf(attributes.get("email"));
	}

	@Override
	public String getUsername() {
		return String.valueOf(attributes.get("name"));
	}
}
