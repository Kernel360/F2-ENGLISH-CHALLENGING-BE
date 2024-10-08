package com.echall.platform.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.echall.platform.user.domain.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

	Optional<UserEntity> findByEmail(String email);

	@Query("SELECT u FROM UserEntity u JOIN FETCH u.bookmarks WHERE u.id = :id")
	Optional<UserEntity> findUserWithBookmarks(Long id);
}
