package com.echall.platform.user.repository;

import com.echall.platform.user.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

	Optional<UserEntity> findByEmail(String email);
	@Query("SELECT u FROM UserEntity u JOIN FETCH u.bookmarks WHERE u.id = :id")
	public UserEntity findUserWithBookmarks(Long id);
}
