package com.echall.platform.oauth2.domain;

import java.util.Map;

public class GoogleUserInfo implements OAuth2UserInfo {
	private final Map<String, Object> attributes; // oauth2User.getAttributes()

	public GoogleUserInfo(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String getProviderId() {
		return attributes.get("sub").toString();
	}

	@Override
	public String getProvider() {
		return "google";
	}

	@Override
	public String getEmail() {
		return attributes.get("email").toString();
	}

	@Override
	public String getUsername() {
		return attributes.get("name").toString();
	}
}
