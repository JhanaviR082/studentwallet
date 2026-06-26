package com.studentwallet.service;

import com.studentwallet.model.BudgetCycle;
import com.studentwallet.model.Expense;
import com.studentwallet.repository.BudgetCycleRepository;
import com.studentwallet.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class CycleService {

    private final BudgetCycleRepository cycleRepository;

    public CycleService(BudgetCycleRepository cycleRepository) {
        this.cycleRepository = cycleRepository;
    }

    public BudgetCycle getOrCreateCycle(String userId, LocalDate startDate, LocalDate endDate,
                                        BigDecimal totalIncome, BigDecimal dailyBaseBudget) {
        LocalDate today = LocalDate.now();
        List<BudgetCycle> cycles = cycleRepository.findByUserId(userId);

        BudgetCycle existing = cycles.stream()
                .filter(c -> !today.isBefore(c.getStartDate()) && !today.isAfter(c.getEndDate()))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            existing.setTotalIncome(totalIncome);
            existing.setDailyBaseBudget(dailyBaseBudget);
            existing.setStartDate(startDate);
            existing.setEndDate(endDate);
            return cycleRepository.save(existing);
        }

        BudgetCycle newCycle = new BudgetCycle();
        newCycle.setCycleId(UUID.randomUUID());
        newCycle.setUserId(userId);
        newCycle.setStartDate(startDate);
        newCycle.setEndDate(endDate);
        newCycle.setTotalIncome(totalIncome);
        newCycle.setDailyBaseBudget(dailyBaseBudget);
        newCycle.setCycleName(startDate.getMonth() + " " + startDate.getYear());
        return cycleRepository.save(newCycle);
    }

    public BudgetCycle getActiveCycle(String userId) {
        LocalDate today = LocalDate.now();
        return cycleRepository.findByUserId(userId).stream()
                .filter(c -> !today.isBefore(c.getStartDate()) && !today.isAfter(c.getEndDate()))
                .max(Comparator.comparing(BudgetCycle::getStartDate))
                .orElse(null);
    }

    public LocalDate[] resolveCycleDates(LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();
        LocalDate resolvedStart = startDate != null ? startDate : today.withDayOfMonth(1);
        LocalDate resolvedEnd = endDate != null ? endDate : YearMonth.from(resolvedStart).atEndOfMonth();
        return new LocalDate[]{resolvedStart, resolvedEnd};
    }

    public List<Expense> filterByCycle(List<Expense> expenses, UUID cycleId) {
        return expenses.stream()
                .filter(e -> cycleId.equals(e.getCycleId()))
                .toList();
    }

    public long daysBetweenInclusive(LocalDate start, LocalDate end) {
        return ChronoUnit.DAYS.between(start, end) + 1;
    }

    public BigDecimal zeroIfNull(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    public BigDecimal maxZero(BigDecimal value) {
        return value.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : value;
    }

    public BigDecimal divide(BigDecimal numerator, long divisor) {
        if (divisor <= 0) {
            return BigDecimal.ZERO;
        }
        return numerator.divide(BigDecimal.valueOf(divisor), 2, RoundingMode.HALF_UP);
    }
}
