package com.echall.platform.oauth2.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.echall.platform.oauth2.domain.GoogleUserInfo;
import com.echall.platform.oauth2.domain.KakaoUserInfo;
import com.echall.platform.oauth2.domain.NaverUserInfo;
import com.echall.platform.oauth2.domain.OAuth2UserInfo;
import com.echall.platform.user.domain.entity.UserEntity;
import com.echall.platform.user.domain.enums.Role;
import com.echall.platform.user.domain.enums.UserStatus;
import com.echall.platform.user.repository.UserRepository;
import com.echall.platform.util.RandomNicknameGenerator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OAuth2UserCustomService extends DefaultOAuth2UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Value("${PASSWORD_KEY}")
	private String JWT_SECRET_KEY;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User user = super.loadUser(userRequest);
		String provider = userRequest.getClientRegistration().getRegistrationId();
		saveOrUpdate(user, provider);
		return user;
	}

	private void saveOrUpdate(OAuth2User oAuth2User, String provider) {
		OAuth2UserInfo oAuth2UserInfo = null;

		if (provider.equals("google")) {
			oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
		}
		if (provider.equals("kakao")) {
			oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
		}
		if (provider.equals("naver")) {
			oAuth2UserInfo = new NaverUserInfo(oAuth2User.getAttributes());
		}

		// String provider = oAuth2UserInfo.getProvider(); //google
		String providerId = oAuth2UserInfo.getProviderId(); //googleId
		String username = oAuth2UserInfo.getUsername();
		String email = oAuth2UserInfo.getEmail();
		String password = passwordEncoder.encode(JWT_SECRET_KEY);
		String nickname = RandomNicknameGenerator.setRandomNickname();

		UserEntity user = userRepository.findByEmail(email)
			.map(entity -> entity.updateUsername(username))
			.orElse(UserEntity.builder()
				.username(username)
				.nickname(nickname)
				.password(password)
				.email(email)
				.role(Role.ROLE_USER)
				.userStatus(UserStatus.USER_STATUS_CREATED)
				.provider(provider)
				.providerId(providerId)
				.build());

		userRepository.save(user);
	}
}
