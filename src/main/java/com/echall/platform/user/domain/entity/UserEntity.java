package com.echall.platform.user.domain.entity;

import com.echall.platform.bookmark.domain.entity.BookmarkEntity;
import com.echall.platform.user.domain.dto.UserRequestDto;
import com.echall.platform.user.domain.enums.Gender;
import com.echall.platform.user.domain.enums.Role;
import com.echall.platform.user.domain.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Table(name = "user")
@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

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

	@OneToMany
	private List<BookmarkEntity> bookmarks;

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

	public void updateUserBookmark(BookmarkEntity bookmark) {
		this.bookmarks.add(bookmark);
	}

	public UserEntity updateUsername(String username) {
		this.username = username;
		return this;
	}
}
