package com.echall.platform.user.domain.entity;

import java.time.LocalDate;

import com.echall.platform.user.domain.dto.UserRequestDto;
import com.echall.platform.user.domain.enums.Gender;
import com.echall.platform.user.domain.enums.Role;
import com.echall.platform.user.domain.enums.UserStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "user")
@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", updatable = false, nullable = false)
	private Long userId;

	private String username;
	private String nickname;
	private String password;

	@Column(nullable = false)
	private String email;

	@Column(name = "phone_number")
	private String phoneNumber;

	private LocalDate birth;

	private Gender gender;

	private Role role;

	@Column(name = "user_status")
	private UserStatus userStatus;

	public void setUserInitialInfo(UserRequestDto.UserUpdateRequest userUpdateRequest) {
		this.username = userUpdateRequest.username();
		this.nickname = userUpdateRequest.nickname();
		this.phoneNumber = userUpdateRequest.phoneNumber();
		this.birth = userUpdateRequest.birth();
		this.gender = userUpdateRequest.gender();
		this.role = Role.ROLE_USER;
		this.userStatus = UserStatus.USER_STATUS_CREATED;
	}

	public void updateUserInfo(UserRequestDto.UserUpdateRequest userUpdateRequest) {
		this.username = userUpdateRequest.username() == null ? this.username : userUpdateRequest.username();
		this.nickname = userUpdateRequest.nickname() == null ? this.nickname : userUpdateRequest.nickname();
		this.phoneNumber = userUpdateRequest.phoneNumber() == null ? this.phoneNumber : userUpdateRequest.phoneNumber();
		this.birth = userUpdateRequest.birth() == null ? this.birth : userUpdateRequest.birth();
	}
}
