package com.springboot.swmlopsbe.domain.auth.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.swmlopsbe.domain.auth.entity.RefreshToken;
import com.springboot.swmlopsbe.domain.user.entity.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

  Optional<RefreshToken> findByToken(String token);

  void deleteByUser(User user);
}
