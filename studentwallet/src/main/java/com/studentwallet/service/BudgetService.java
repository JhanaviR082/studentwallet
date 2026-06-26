package com.studentwallet.service;

import com.studentwallet.model.BudgetCycle;
import com.studentwallet.model.Expense;
import com.studentwallet.model.dto.BudgetDashboardDTO;
import com.studentwallet.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class BudgetService {

    private final ExpenseRepository expenseRepository;
    private final CycleService cycleService;

    public BudgetService(ExpenseRepository expenseRepository, CycleService cycleService) {
        this.expenseRepository = expenseRepository;
        this.cycleService = cycleService;
    }

    public BudgetDashboardDTO calculateDashboard(String userId, BigDecimal totalIncome,
                                                  LocalDate startDate, LocalDate endDate) {
        LocalDate[] dates = cycleService.resolveCycleDates(startDate, endDate);
        startDate = dates[0];
        endDate = dates[1];

        LocalDate today = LocalDate.now();
        long totalDays = cycleService.daysBetweenInclusive(startDate, endDate);
        long daysRemaining = Math.max(totalDays - cycleService.daysBetweenInclusive(startDate, today), 0);

        // Simple flat daily allowance: income ÷ days in month
        BigDecimal dailyAllowance = cycleService.divide(totalIncome, totalDays);

        BudgetCycle cycle = cycleService.getOrCreateCycle(
                userId, startDate, endDate, totalIncome, dailyAllowance);
        UUID cycleId = cycle.getCycleId();

        BigDecimal spentSoFar = getTotalSpentUpToDate(userId, cycleId, today);
        BigDecimal spentToday = getDailySpent(userId, cycleId, today);

        // Today's safe spend = daily allowance minus what you've already spent today
        BigDecimal remainingForToday = dailyAllowance.subtract(spentToday);

        BigDecimal remainingTotalIncome = totalIncome.subtract(spentSoFar);
BigDecimal comingDaysAllowance;
if (daysRemaining > 0) {
    comingDaysAllowance = cycleService.divide(remainingTotalIncome, daysRemaining);
} else {
    comingDaysAllowance = BigDecimal.ZERO;
}

        BigDecimal projectedSpendRemaining = dailyAllowance.multiply(BigDecimal.valueOf(daysRemaining));
        BigDecimal monthEndSurplus = cycleService.maxZero(remainingTotalIncome.subtract(projectedSpendRemaining));

        BigDecimal potentialSavings = computePotentialSavings(userId, cycleId, dailyAllowance, startDate, today);

        return new BudgetDashboardDTO(
                dailyAllowance,
                remainingForToday,
                comingDaysAllowance,
                monthEndSurplus,
                potentialSavings,
                spentToday,
                spentSoFar
        );
    }

    public BudgetDashboardDTO getDashboardForUser(String userId) {
        BudgetCycle cycle = cycleService.getActiveCycle(userId);
        if (cycle == null) {
            return new BudgetDashboardDTO(
                    BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                    BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO
            );
        }
        return calculateDashboard(
                userId,
                cycle.getTotalIncome(),
                cycle.getStartDate(),
                cycle.getEndDate()
        );
    }

    private BigDecimal computePotentialSavings(String userId, UUID cycleId,
                                                BigDecimal dailyAllowance,
                                                LocalDate startDate, LocalDate today) {
        BigDecimal totalSaved = BigDecimal.ZERO;
        long daysElapsed = cycleService.daysBetweenInclusive(startDate, today);

        for (long i = 0; i < daysElapsed; i++) {
            LocalDate day = startDate.plusDays(i);
            if (day.isAfter(today)) {
                break;
            }
            BigDecimal spent = getDailySpent(userId, cycleId, day);
            BigDecimal saved = dailyAllowance.subtract(spent);
            if (saved.compareTo(BigDecimal.ZERO) > 0) {
                totalSaved = totalSaved.add(saved);
            }
        }
        return totalSaved;
    }

    private BigDecimal getTotalSpentUpToDate(String userId, UUID cycleId, LocalDate date) {
        return cycleService.filterByCycle(expenseRepository.findByUserId(userId), cycleId).stream()
                .filter(e -> !e.getExpenseDate().isAfter(date))
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getDailySpent(String userId, UUID cycleId, LocalDate date) {
        return cycleService.filterByCycle(expenseRepository.findByUserId(userId), cycleId).stream()
                .filter(e -> e.getExpenseDate().equals(date))
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
