package com.studentwallet.service;

import com.studentwallet.model.BudgetCycle;
import com.studentwallet.model.dto.WhatIfResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class WhatIfService {

    private final CycleService cycleService;
    private final BudgetService budgetService;

    public WhatIfService(CycleService cycleService, BudgetService budgetService) {
        this.cycleService = cycleService;
        this.budgetService = budgetService;
    }

    public WhatIfResponse simulate(String userId, BigDecimal extraSavingsGoal) {
        BudgetCycle cycle = cycleService.getActiveCycle(userId);
        if (cycle == null) {
            throw new IllegalStateException("Set your budget before using the simulator.");
        }

        LocalDate today = LocalDate.now();
        long totalDays = cycleService.daysBetweenInclusive(cycle.getStartDate(), cycle.getEndDate());
        long daysElapsed = Math.min(cycleService.daysBetweenInclusive(cycle.getStartDate(), today), totalDays);
        long daysRemaining = Math.max(totalDays - daysElapsed, 0);

        var dashboard = budgetService.getDashboardForUser(userId);
        BigDecimal currentDaily = dashboard.getComingDaysAllowance();
        if (currentDaily.compareTo(BigDecimal.ZERO) <= 0) {
            currentDaily = dashboard.getDailyAllowance();
        }

        BigDecimal dailyReduction = cycleService.divide(extraSavingsGoal, Math.max(daysRemaining, 1));
        BigDecimal adjustedDaily = cycleService.maxZero(currentDaily.subtract(dailyReduction));

        List<String> tips = buildTips(currentDaily, adjustedDaily, dailyReduction, extraSavingsGoal);

        return new WhatIfResponse(
                currentDaily,
                adjustedDaily,
                dailyReduction,
                extraSavingsGoal,
                daysRemaining,
                tips
        );
    }

    private List<String> buildTips(BigDecimal current, BigDecimal adjusted,
                                   BigDecimal reduction, BigDecimal goal) {
        List<String> tips = new ArrayList<>();
        tips.add(String.format("Your daily budget drops from ₹%s to ₹%s.",
                current.setScale(0, java.math.RoundingMode.HALF_UP),
                adjusted.setScale(0, java.math.RoundingMode.HALF_UP)));

        if (reduction.compareTo(new BigDecimal("50")) >= 0) {
            tips.add("Skip eating out on Wednesdays — save ~₹" + reduction.setScale(0, java.math.RoundingMode.HALF_UP) + "/day.");
        }
        if (reduction.compareTo(new BigDecimal("30")) >= 0) {
            tips.add("Brew chai in your room instead of café runs.");
        }
        if (goal.compareTo(new BigDecimal("3000")) >= 0) {
            tips.add("Use campus mess for dinner 5 days a week.");
        }
        tips.add("Cut Swiggy/Zomato to weekends only for the rest of the month.");

        return tips;
    }
}
