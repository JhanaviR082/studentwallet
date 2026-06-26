package com.studentwallet.service;

import com.studentwallet.model.BudgetCycle;
import com.studentwallet.model.Expense;
import com.studentwallet.model.dto.ExpenseRequest;
import com.studentwallet.model.dto.ExpenseResponse;
import com.studentwallet.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    private static final Set<String> ESSENTIAL_CATEGORIES = Set.of(
            "Mess/Canteen", "Academic", "Travel/Auto"
    );

    private final ExpenseRepository expenseRepository;
    private final CycleService cycleService;

    public ExpenseService(ExpenseRepository expenseRepository, CycleService cycleService) {
        this.expenseRepository = expenseRepository;
        this.cycleService = cycleService;
    }

    public ExpenseResponse addExpense(String userId, ExpenseRequest request) {
        BudgetCycle cycle = cycleService.getActiveCycle(userId);
        if (cycle == null) {
            throw new IllegalStateException("No active budget cycle. Set your budget first.");
        }
        return saveExpense(userId, cycle.getCycleId(), null, request);
    }

    public ExpenseResponse updateExpense(String userId, UUID expenseId, ExpenseRequest request) {
        Expense existing = getOwnedExpense(userId, expenseId);
        return saveExpense(userId, existing.getCycleId(), existing, request);
    }

    public void deleteExpense(String userId, UUID expenseId) {
        Expense existing = getOwnedExpense(userId, expenseId);
        expenseRepository.delete(existing);
    }

    public List<ExpenseResponse> getTodayExpenses(String userId) {
        LocalDate today = LocalDate.now();
        return expenseRepository.findByUserIdAndExpenseDate(userId, today).stream()
                .sorted(Comparator.comparing(Expense::getCreatedAt).reversed())
                .map(e -> toResponse(e, computeVerdict(userId, e)))
                .toList();
    }

    public List<ExpenseResponse> getSpendingPattern(String userId) {
        return expenseRepository.findByUserId(userId).stream()
                .sorted(Comparator.comparing(Expense::getExpenseDate).reversed()
                        .thenComparing(Expense::getCreatedAt, Comparator.reverseOrder()))
                .limit(50)
                .map(e -> toResponse(e, computeVerdict(userId, e)))
                .toList();
    }

    private Expense getOwnedExpense(String userId, UUID expenseId) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new IllegalArgumentException("Expense not found"));
        if (!userId.equals(expense.getUserId())) {
            throw new IllegalArgumentException("Expense not found");
        }
        return expense;
    }

    private ExpenseResponse saveExpense(String userId, UUID cycleId, Expense existing, ExpenseRequest request) {
        LocalDate expenseDate = request.getExpenseDate() != null ? request.getExpenseDate() : LocalDate.now();

        Expense expense = existing != null ? existing : new Expense();
        if (existing == null) {
            expense.setExpenseId(UUID.randomUUID());
            expense.setUserId(userId);
            expense.setCycleId(cycleId);
            expense.setCreatedAt(Instant.now());
        }

        expense.setExpenseDate(expenseDate);
        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setDescription(request.getDescription() != null ? request.getDescription() : "");
        expense.setEssential(ESSENTIAL_CATEGORIES.contains(request.getCategory()));

        Expense saved = expenseRepository.save(expense);
        return toResponse(saved, computeVerdict(userId, saved));
    }

    private String computeVerdict(String userId, Expense expense) {
        Map<String, List<Expense>> byCategory = expenseRepository.findByUserId(userId).stream()
                .filter(e -> expense.getCategory().equals(e.getCategory()))
                .collect(Collectors.groupingBy(Expense::getCategory));

        List<Expense> categoryExpenses = byCategory.getOrDefault(expense.getCategory(), List.of());
        if (categoryExpenses.size() < 2) {
            return "Better";
        }

        BigDecimal average = categoryExpenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(categoryExpenses.size()), 2, RoundingMode.HALF_UP);

        if (average.compareTo(BigDecimal.ZERO) == 0) {
            return "Better";
        }

        BigDecimal ratio = expense.getAmount().divide(average, 2, RoundingMode.HALF_UP);
        if (ratio.compareTo(new BigDecimal("1.20")) >= 0) {
            return "Bad";
        }
        if (ratio.compareTo(new BigDecimal("0.80")) <= 0) {
            return "Better";
        }
        return "Neutral";
    }

    private ExpenseResponse toResponse(Expense expense, String verdict) {
        ExpenseResponse response = new ExpenseResponse();
        response.setExpenseId(expense.getExpenseId());
        response.setCategory(expense.getCategory());
        response.setDescription(expense.getDescription());
        response.setAmount(expense.getAmount());
        response.setExpenseDate(expense.getExpenseDate());
        response.setVerdict(verdict);
        response.setEssential(expense.isEssential());
        return response;
    }
}
