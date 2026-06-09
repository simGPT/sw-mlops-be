package com.springboot.swmlopsbe.domain.user.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.swmlopsbe.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {

  boolean existsByUsername(String username);
}
