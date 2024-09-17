package com.echall.platform.oauth2.domain;

public interface OAuth2UserInfo {
	String getProviderId();

	String getProvider();

	String getEmail();

	String getUsername();
}
