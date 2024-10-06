package com.echall.platform.user.domain.entity;

import com.echall.platform.bookmark.domain.entity.BookmarkEntity;
import com.echall.platform.oauth2.domain.info.OAuth2UserInfo;
import com.echall.platform.user.domain.dto.UserRequestDto;
import com.echall.platform.user.domain.enums.Gender;
import com.echall.platform.user.domain.enums.Role;
import com.echall.platform.user.domain.enums.UserStatus;
import com.echall.platform.util.RandomNicknameGenerator;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, columnDefinition = "varchar(255)")
	private String username;

	@Column(nullable = false, columnDefinition = "varchar(255)")
	private String nickname;

	@Column(columnDefinition = "varchar(255)")
	private String password;

	@Column(nullable = false, columnDefinition = "varchar(255)")
	private String email;

	@Column(name = "phone_number", columnDefinition = "varchar(255)")
	private String phoneNumber;

	@Column(columnDefinition = "date")
	private LocalDate birth;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, name = "user_status")
	private UserStatus userStatus;

	@Column(nullable = false, columnDefinition = "varchar(255)")
	private String provider;

	@Column(nullable = false, columnDefinition = "varchar(255)")
	private String providerId;

	@OneToMany()
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

	public static UserEntity createByOAuthUser(OAuth2UserInfo oAuth2UserInfo) {
		return UserEntity.builder()
			.username(oAuth2UserInfo.getUsername())
			.nickname(RandomNicknameGenerator.setRandomNickname())
			.email(oAuth2UserInfo.getEmail())
			.role(Role.ROLE_USER)
			.userStatus(UserStatus.USER_STATUS_CREATED)
			.provider(oAuth2UserInfo.getProvider())
			.providerId(oAuth2UserInfo.getProviderId())
			.build();
	}
}
