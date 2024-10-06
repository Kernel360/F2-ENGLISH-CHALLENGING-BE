package com.echall.platform.user.service;

import com.echall.platform.message.error.exception.CommonException;
import com.echall.platform.oauth2.domain.info.OAuth2UserPrincipal;
import com.echall.platform.user.domain.dto.UserRequestDto;
import com.echall.platform.user.domain.dto.UserResponseDto;
import com.echall.platform.user.domain.entity.UserEntity;
import com.echall.platform.user.domain.enums.UserStatus;
import com.echall.platform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.echall.platform.message.error.code.UserErrorCode.*;

@RequiredArgsConstructor
@Service
public class UserService {
	private final UserRepository userRepository;

	public UserResponseDto.UserUpdateResponse updateUserInfo(
		UserRequestDto.UserUpdateRequest userUpdateRequest,
		String email
	) {
		UserEntity user = this.getUserByEmail(email);
		AssertThat_UserAccountIsAppropriate(user);

		// set additional info when user created
		if (user.getUserStatus() == UserStatus.USER_STATUS_CREATED) {
			user.setUserInitialInfo(userUpdateRequest);
		}
		// update user info
		user.updateUserInfo(userUpdateRequest);

		return UserResponseDto.UserUpdateResponse.toDto(user);
	}

	public UserResponseDto.UserMyPageResponse getMyPage(String email) {
		UserEntity user = this.getUserByEmail(email);
		AssertThat_UserAccountIsAppropriate(user);

		return UserResponseDto.UserMyPageResponse.toDto(user);
	}

	public UserResponseDto.UserChallengeResponse getMyChallenge(String email) {
		UserEntity user = this.getUserByEmail(email);
		AssertThat_UserAccountIsAppropriate(user);
		return UserResponseDto.UserChallengeResponse.toDto(user);
	}

	public UserEntity getUserById(Long userId) {
		UserEntity user = userRepository.findById(userId)
			.orElseThrow(() -> new CommonException(USER_NOT_FOUND));
		AssertThat_UserAccountIsAppropriate(user);
		return user;
	}

	// Internal Methods=================================================================================================
	public UserEntity getUserByEmail(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new CommonException(USER_NOT_FOUND));
	}

	protected void AssertThat_UserAccountIsAppropriate(UserEntity user) {
		if (user.getUserStatus().equals(UserStatus.USER_STATUS_DEACTIVATED)) {
			throw new CommonException(USER_FAIL_DEACTIVATE);
		}
		if (user.getUserStatus().equals(UserStatus.USER_STATUS_SUSPENDED)) {
			throw new CommonException(USER_FAIL_SUSPEND);
		}
	}

	@Transactional
	public UserEntity getUserByOAuthUser(OAuth2UserPrincipal oAuthUser) {
		UserEntity user = userRepository.findByEmail(oAuthUser.getEmail())
			.orElseGet(() -> {
				UserEntity newUser = UserEntity.createByOAuthUser(oAuthUser);

				return userRepository.save(newUser);
			});

		user.updateAfterOAuth2Login(oAuthUser);

		return user;
	}
}
