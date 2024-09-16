package com.echall.platform.user.domain.dto;

import java.time.LocalDate;

import com.echall.platform.user.domain.entity.UserEntity;
import com.echall.platform.user.domain.enums.Gender;

public class UserResponseDto {
	public record UserUpdateResponse(
		Long userId
	) {
		public static UserUpdateResponse toDto(UserEntity user) {
			return new UserUpdateResponse(
				user.getUserId()
			);
		}

	}

	public record UserMyPageResponse(
		String username,
		String nickname,
		String email,
		String phoneNumber,
		LocalDate birth,
		Gender gender
	) {
		public static UserMyPageResponse toDto(UserEntity user) {
			return new UserMyPageResponse(
				user.getUsername(),
				user.getNickname(),
				user.getEmail(),
				user.getPhoneNumber(),
				user.getBirth(),
				user.getGender()
			);
		}
	}

	public record UserChallengeResponse(
		// TODO: challenge 완성되면 수정 필요, 아마 querydsl 사용해야 할 수도
		String challenge
	) {
		public static UserChallengeResponse toDto(UserEntity user) {
			return new UserChallengeResponse("user's challenge");
		}
	}
}
