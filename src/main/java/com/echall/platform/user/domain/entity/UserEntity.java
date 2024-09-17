package com.echall.platform.user.domain.entity;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.echall.platform.user.domain.dto.UserRequestDto;
import com.echall.platform.user.domain.enums.Gender;
import com.echall.platform.user.domain.enums.Role;
import com.echall.platform.user.domain.enums.UserStatus;
import com.mongodb.connection.ProxySettings;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "user")
@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseEntity implements UserDetails {

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

	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Enumerated(EnumType.STRING)
	private Role role;

	@Column(name = "user_status")
	@Enumerated(EnumType.STRING)
	private UserStatus userStatus;

	private String provider;

	private String providerId;

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



	// For Spring Security==============================================================================================
	@Builder
	public UserEntity(String username, String nickname, String password, String email, String phoneNumber,
		LocalDate birth, Gender gender, Role role, UserStatus userStatus, String provider, String providerId) {
		this.username = username;
		this.nickname = nickname;
		this.password = password;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.birth = birth;
		this.gender = gender;
		this.role = role;
		this.userStatus = userStatus;
		this.provider = provider;
		this.providerId = providerId;
	}

	public UserEntity updateUsername(String username) {
		this.username = username;
		return this;
	}

	// Override methods

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of();
	}

	@Override
	public boolean isAccountNonExpired() {
		return UserDetails.super.isAccountNonExpired();
	}

	@Override
	public boolean isAccountNonLocked() {
		return UserDetails.super.isAccountNonLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return UserDetails.super.isCredentialsNonExpired();
	}

	@Override
	public boolean isEnabled() {
		return UserDetails.super.isEnabled();
	}
}
