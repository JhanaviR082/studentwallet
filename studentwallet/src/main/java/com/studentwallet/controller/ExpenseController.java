package com.studentwallet.controller;

import com.studentwallet.model.dto.ExpenseRequest;
import com.studentwallet.model.dto.ExpenseResponse;
import com.studentwallet.model.dto.SmsImportRequest;
import com.studentwallet.model.dto.SmsImportResponse;
import com.studentwallet.service.ExpenseService;
import com.studentwallet.service.SmsParserService;
import com.studentwallet.util.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final SmsParserService smsParserService;

    public ExpenseController(ExpenseService expenseService, SmsParserService smsParserService) {
        this.expenseService = expenseService;
        this.smsParserService = smsParserService;
    }

    @PostMapping
    public ResponseEntity<ExpenseResponse> addExpense(@Valid @RequestBody ExpenseRequest request) {
        ExpenseResponse response = expenseService.addExpense(AuthUtil.currentUserId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{expenseId}")
    public ResponseEntity<ExpenseResponse> updateExpense(
            @PathVariable UUID expenseId,
            @Valid @RequestBody ExpenseRequest request) {
        return ResponseEntity.ok(expenseService.updateExpense(AuthUtil.currentUserId(), expenseId, request));
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Void> deleteExpense(@PathVariable UUID expenseId) {
        expenseService.deleteExpense(AuthUtil.currentUserId(), expenseId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/today")
    public ResponseEntity<List<ExpenseResponse>> getTodayExpenses() {
        return ResponseEntity.ok(expenseService.getTodayExpenses(AuthUtil.currentUserId()));
    }

    @GetMapping("/pattern")
    public ResponseEntity<List<ExpenseResponse>> getSpendingPattern() {
        return ResponseEntity.ok(expenseService.getSpendingPattern(AuthUtil.currentUserId()));
    }

    @PostMapping("/import-sms")
    public ResponseEntity<SmsImportResponse> importSms(@Valid @RequestBody SmsImportRequest request) {
        SmsImportResponse response = smsParserService.importSms(AuthUtil.currentUserId(), request.getSmsText());
        return ResponseEntity.ok(response);
    }
}
