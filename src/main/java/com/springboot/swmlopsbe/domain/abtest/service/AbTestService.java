package com.springboot.swmlopsbe.domain.abtest.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.swmlopsbe.domain.abtest.entity.AbTestAssignment;
import com.springboot.swmlopsbe.domain.abtest.entity.AbTestOutcome;
import com.springboot.swmlopsbe.domain.abtest.entity.GroupType;
import com.springboot.swmlopsbe.domain.abtest.repository.AbTestAssignmentRepository;
import com.springboot.swmlopsbe.domain.abtest.repository.AbTestOutcomeRepository;
import com.springboot.swmlopsbe.domain.prediction.dto.request.FeatureRequest;
import com.springboot.swmlopsbe.domain.user.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AbTestService {

  private static final int MEASUREMENT_DAYS = 30;

  private final AbTestAssignmentRepository assignmentRepository;
  private final AbTestOutcomeRepository outcomeRepository;

  @Transactional
  public void recordAssignment(User user, FeatureRequest features, boolean isPersuadable) {
    GroupType groupType = isPersuadable ? GroupType.TREATMENT : GroupType.CONTROL;

    AbTestAssignment assignment =
        assignmentRepository.save(
            AbTestAssignment.builder()
                .user(user)
                .groupType(groupType)
                .accountAgeMonths(features.getAccountAgeMonths())
                .avgOrderValue(features.getAvgOrderValue())
                .totalOrders(features.getTotalOrders())
                .daysSinceLastPurchase(features.getDaysSinceLastPurchase())
                .discountUsageRate(features.getDiscountUsageRate())
                .returnRate(features.getReturnRate())
                .browsingFrequencyPerWeek(features.getBrowsingFrequencyPerWeek())
                .cartAbandonmentRate(features.getCartAbandonmentRate())
                .build());

    outcomeRepository.save(AbTestOutcome.builder().assignment(assignment).user(user).build());

    log.info(
        "[AB테스트] 배정 완료 - userId: {}, group: {}", user.getId(), groupType);
  }

  @Transactional
  public void recordConversion(User user) {
    LocalDateTime since = LocalDateTime.now().minusDays(MEASUREMENT_DAYS);
    List<AbTestOutcome> outcomes =
        outcomeRepository.findByUserAndMeasuredAtIsNullAndAssignment_AssignedAtAfter(user, since);

    if (outcomes.isEmpty()) return;

    LocalDateTime now = LocalDateTime.now();
    outcomes.forEach(outcome -> outcome.convert(now));
    log.info("[AB테스트] 구매 전환 기록 - userId: {}, {}건", user.getId(), outcomes.size());
  }

  @Transactional
  public void measureOutcomes() {
    LocalDateTime cutoff = LocalDateTime.now().minusDays(MEASUREMENT_DAYS);
    List<AbTestOutcome> outcomes =
        outcomeRepository.findByMeasuredAtIsNullAndAssignment_AssignedAtBefore(cutoff);

    LocalDateTime now = LocalDateTime.now();
    outcomes.forEach(outcome -> outcome.measure(now));
    log.info("[AB테스트] outcome 확정 완료 - {}건", outcomes.size());
  }
}
