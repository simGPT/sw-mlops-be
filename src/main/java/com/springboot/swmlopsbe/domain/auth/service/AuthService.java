package com.springboot.swmlopsbe.domain.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.swmlopsbe.domain.auth.dto.request.SignupRequest;
import com.springboot.swmlopsbe.domain.auth.dto.response.SignupResponse;
import com.springboot.swmlopsbe.domain.auth.exception.AuthErrorCode;
import com.springboot.swmlopsbe.domain.user.entity.User;
import com.springboot.swmlopsbe.domain.user.repository.UserRepository;
import com.springboot.swmlopsbe.global.exception.CustomException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public SignupResponse signup(SignupRequest request) {
    log.info("[회원가입] 요청 - username: {}", request.getUsername());

    if (userRepository.existsByUsername(request.getUsername())) {
      log.warn("[회원가입] 중복 아이디 - username: {}", request.getUsername());
      throw new CustomException(AuthErrorCode.DUPLICATE_USERNAME);
    }

    User user =
        User.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .name(request.getName())
            .build();

    userRepository.save(user);
    log.info("[회원가입] 완료 - username: {}, userId: {}", user.getUsername(), user.getId());

    return SignupResponse.from(user);
  }
}
