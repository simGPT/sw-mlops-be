package com.springboot.swmlopsbe.domain.notification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.swmlopsbe.domain.notification.entity.Notification;
import com.springboot.swmlopsbe.domain.user.entity.User;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserOrderByCreatedAtDesc(User user);
}
