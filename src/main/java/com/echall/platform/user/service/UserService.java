package com.echall.platform.user.service;

import org.springframework.stereotype.Service;

import com.echall.platform.exception.user.UserNotFoundException;
import com.echall.platform.user.domain.dto.UserRequestDto;
import com.echall.platform.user.domain.dto.UserResponseDto;
import com.echall.platform.user.domain.entity.UserEntity;
import com.echall.platform.user.domain.enums.UserStatus;
import com.echall.platform.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

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
			.orElseThrow(
				() -> new UserNotFoundException(userId.toString())
			);
		AssertThat_UserAccountIsAppropriate(user);
		return user;
	}

	// Internal Methods=================================================================================================
	public UserEntity getUserByEmail(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new RuntimeException("User not found"));
	}

	private void AssertThat_UserAccountIsAppropriate(UserEntity user) {
		if (user.getUserStatus().equals(UserStatus.USER_STATUS_DEACTIVATED)) {
			throw new RuntimeException("User deactivated");
		}
		if (user.getUserStatus().equals(UserStatus.USER_STATUS_SUSPENDED)) {
			throw new RuntimeException("User suspended");
		}
	}

}
