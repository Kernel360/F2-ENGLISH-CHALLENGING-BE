package com.echall.platform.user.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
public class UserEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="user_id", updatable = false, nullable = false)
	private Long userID;

	private String username;
	private String nickname;
	private String password;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(name="phone_number")
	private String phoneNumber;

	private LocalDate birth;

	private Gender gender;
	private Role role;
	private UserStatus userStatus;


}
