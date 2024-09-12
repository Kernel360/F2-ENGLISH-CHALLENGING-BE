package com.echall.platform.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.echall.platform.user.domain.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

}
