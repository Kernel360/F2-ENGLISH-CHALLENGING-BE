package com.echall.platform.user.service;

import org.springframework.stereotype.Service;

import com.echall.platform.user.domain.dto.UserRequestDto;
import com.echall.platform.user.domain.dto.UserResponseDto;

@Service
public class UserService {

	public UserResponseDto.UserUpdateResponse updateUserInfo(UserRequestDto.UserUpdateRequest userUpdateRequest) {
		return new UserResponseDto.UserUpdateResponse();
	}
}
