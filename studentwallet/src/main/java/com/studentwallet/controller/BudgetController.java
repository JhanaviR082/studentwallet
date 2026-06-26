package com.studentwallet.controller;

import com.studentwallet.model.dto.BudgetCalculateRequest;
import com.studentwallet.model.dto.BudgetDashboardDTO;
import com.studentwallet.model.dto.WhatIfRequest;
import com.studentwallet.model.dto.WhatIfResponse;
import com.studentwallet.service.BudgetService;
import com.studentwallet.service.WhatIfService;
import com.studentwallet.util.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/budget")
public class BudgetController {

    private final BudgetService budgetService;
    private final WhatIfService whatIfService;

    public BudgetController(BudgetService budgetService, WhatIfService whatIfService) {
        this.budgetService = budgetService;
        this.whatIfService = whatIfService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<BudgetDashboardDTO> calculate(@Valid @RequestBody BudgetCalculateRequest request) {
        String userId = AuthUtil.currentUserId();
        return ResponseEntity.ok(budgetService.calculateDashboard(
                userId,
                request.getTotalIncome(),
                request.getStartDate(),
                request.getEndDate()
        ));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<BudgetDashboardDTO> getDashboard() {
        return ResponseEntity.ok(budgetService.getDashboardForUser(AuthUtil.currentUserId()));
    }

    @PostMapping("/what-if")
    public ResponseEntity<WhatIfResponse> whatIf(@Valid @RequestBody WhatIfRequest request) {
        return ResponseEntity.ok(whatIfService.simulate(AuthUtil.currentUserId(), request.getExtraSavingsGoal()));
    }
}
