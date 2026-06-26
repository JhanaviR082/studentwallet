package com.studentwallet.service;

import com.studentwallet.model.BudgetCycle;
import com.studentwallet.model.Expense;
import com.studentwallet.model.GamificationProfile;
import com.studentwallet.model.dto.BadgeDTO;
import com.studentwallet.model.dto.GamificationDTO;
import com.studentwallet.repository.ExpenseRepository;
import com.studentwallet.repository.GamificationProfileRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GamificationService {

    private final GamificationProfileRepository profileRepository;
    private final ExpenseRepository expenseRepository;
    private final CycleService cycleService;

    public GamificationService(GamificationProfileRepository profileRepository,
                               ExpenseRepository expenseRepository,
                               CycleService cycleService) {
        this.profileRepository = profileRepository;
        this.expenseRepository = expenseRepository;
        this.cycleService = cycleService;
    }

    public GamificationDTO getGamification(String userId, String displayName) {
        GamificationProfile profile = getOrCreateProfile(userId, displayName);
        BudgetCycle cycle = cycleService.getActiveCycle(userId);

        BigDecimal dailyAllowance = cycle != null ? cycle.getDailyBaseBudget() : BigDecimal.ZERO;
        BigDecimal todaySaved = computeTodaySaved(userId, cycle, dailyAllowance);
        BigDecimal totalDailySavings = computeTotalDailySavings(userId, cycle, dailyAllowance);

        profile.setTotalDailySavings(totalDailySavings);
        updateStreak(profile, userId, cycle, dailyAllowance);

        List<BadgeDTO> badges = computeSavingsBadges(profile, todaySaved, totalDailySavings);
        applyNewBonuses(profile, badges);

        profile.setEarnedBadges(badges.stream()
                .filter(BadgeDTO::isEarned)
                .map(BadgeDTO::getId)
                .toList());
        profileRepository.save(profile);

        GamificationDTO dto = new GamificationDTO();
        dto.setCurrentStreak(profile.getCurrentStreak());
        dto.setLongestStreak(profile.getLongestStreak());
        dto.setBadges(badges);
        dto.setDisplayName(profile.getDisplayName());
        dto.setSavingsVault(profile.getSavingsVault());
        dto.setTotalDailySavings(totalDailySavings);
        dto.setTodaySaved(todaySaved);
        return dto;
    }

    private GamificationProfile getOrCreateProfile(String userId, String displayName) {
        return profileRepository.findById(userId).orElseGet(() -> {
            GamificationProfile profile = new GamificationProfile();
            profile.setUserId(userId);
            profile.setDisplayName(displayName != null ? displayName : "Student");
            profile.setCurrentStreak(0);
            profile.setLongestStreak(0);
            profile.setEarnedBadges(new ArrayList<>());
            profile.setSavingsVault(BigDecimal.ZERO);
            profile.setTotalDailySavings(BigDecimal.ZERO);
            return profileRepository.save(profile);
        });
    }

    private BigDecimal computeTodaySaved(String userId, BudgetCycle cycle, BigDecimal dailyAllowance) {
        if (cycle == null || dailyAllowance.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal spentToday = cycleService.filterByCycle(
                expenseRepository.findByUserId(userId), cycle.getCycleId()).stream()
                .filter(e -> e.getExpenseDate().equals(LocalDate.now()))
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return cycleService.maxZero(dailyAllowance.subtract(spentToday));
    }

    private BigDecimal computeTotalDailySavings(String userId, BudgetCycle cycle, BigDecimal dailyAllowance) {
        if (cycle == null || dailyAllowance.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        LocalDate today = LocalDate.now();
        BigDecimal total = BigDecimal.ZERO;
        long daysElapsed = cycleService.daysBetweenInclusive(cycle.getStartDate(), today);

        for (long i = 0; i < daysElapsed; i++) {
            LocalDate day = cycle.getStartDate().plusDays(i);
            BigDecimal spent = cycleService.filterByCycle(
                    expenseRepository.findByUserId(userId), cycle.getCycleId()).stream()
                    .filter(e -> e.getExpenseDate().equals(day))
                    .map(Expense::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal saved = dailyAllowance.subtract(spent);
            if (saved.compareTo(BigDecimal.ZERO) > 0) {
                total = total.add(saved);
            }
        }
        return total;
    }

    private void updateStreak(GamificationProfile profile, String userId,
                              BudgetCycle cycle, BigDecimal dailyAllowance) {
        if (cycle == null) {
            return;
        }
        LocalDate today = LocalDate.now();
        BigDecimal spentToday = cycleService.filterByCycle(
                expenseRepository.findByUserId(userId), cycle.getCycleId()).stream()
                .filter(e -> e.getExpenseDate().equals(today))
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        boolean underBudget = spentToday.compareTo(dailyAllowance) <= 0;
        if (underBudget) {
            LocalDate last = profile.getLastUnderBudgetDate();
            if (last == null || ChronoUnit.DAYS.between(last, today) == 1) {
                profile.setCurrentStreak(profile.getCurrentStreak() + 1);
            } else if (ChronoUnit.DAYS.between(last, today) > 1) {
                profile.setCurrentStreak(1);
            }
            profile.setLastUnderBudgetDate(today);
            profile.setLongestStreak(Math.max(profile.getLongestStreak(), profile.getCurrentStreak()));
        }
    }

    private List<BadgeDTO> computeSavingsBadges(GamificationProfile profile,
                                                 BigDecimal todaySaved,
                                                 BigDecimal totalDailySavings) {
        Set<String> earned = new HashSet<>(profile.getEarnedBadges() != null ? profile.getEarnedBadges() : List.of());
        int totalSavedInt = totalDailySavings.intValue();
        int todaySavedInt = todaySaved.intValue();
        int vaultInt = profile.getSavingsVault().intValue() + totalSavedInt;

        return List.of(
                new BadgeDTO("first-saver", "First Saver",
                        "Save any amount under today's budget", "savings",
                        todaySavedInt > 0 || earned.contains("first-saver"),
                        Math.min(todaySavedInt, 1), 1, 50),
                new BadgeDTO("saver-100", "₹100 Saved",
                        "Accumulate ₹100 in daily savings", "account_balance",
                        totalSavedInt >= 100 || earned.contains("saver-100"),
                        Math.min(totalSavedInt, 100), 100, 100),
                new BadgeDTO("streak-3", "3-Day Saver",
                        "Stay under budget 3 days in a row", "local_fire_department",
                        profile.getCurrentStreak() >= 3 || earned.contains("streak-3"),
                        Math.min(profile.getCurrentStreak(), 3), 3, 100),
                new BadgeDTO("streak-7", "Week Saver",
                        "7-day under-budget streak", "emoji_events",
                        profile.getCurrentStreak() >= 7 || earned.contains("streak-7"),
                        Math.min(profile.getCurrentStreak(), 7), 7, 250),
                new BadgeDTO("vault-500", "₹500 Vault",
                        "Reach ₹500 combined savings + bonuses", "lock",
                        vaultInt >= 500 || earned.contains("vault-500"),
                        Math.min(vaultInt, 500), 500, 100)
        );
    }

    private void applyNewBonuses(GamificationProfile profile, List<BadgeDTO> badges) {
        Set<String> previouslyEarned = new HashSet<>(
                profile.getEarnedBadges() != null ? profile.getEarnedBadges() : List.of());
        BigDecimal vault = profile.getSavingsVault() != null ? profile.getSavingsVault() : BigDecimal.ZERO;

        for (BadgeDTO badge : badges) {
            if (badge.isEarned() && !previouslyEarned.contains(badge.getId())) {
                vault = vault.add(BigDecimal.valueOf(badge.getBonusAmount()));
            }
        }
        profile.setSavingsVault(vault);
    }
}
