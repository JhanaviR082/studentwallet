import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatBottomSheet, MatBottomSheetModule } from '@angular/material/bottom-sheet';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { animate, style, transition, trigger } from '@angular/animations';
import { debounceTime, distinctUntilChanged, filter } from 'rxjs/operators';

import { BudgetService } from '../../services/budget.service';
import { ExpenseService } from '../../services/expense.service';
import { AuthService } from '../../services/auth.service';
import { BudgetDashboard, Expense } from '../../models/budget.models';
import { ExpenseListComponent } from '../expense-list/expense-list.component';
import { ExpenseFormComponent } from '../expense-form/expense-form.component';
import { WhatIfSimulatorComponent } from '../what-if-simulator/what-if-simulator.component';
import { GamificationPanelComponent } from '../gamification-panel/gamification-panel.component';
import { SmsImportComponent } from '../sms-import/sms-import.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatIconModule,
    MatButtonModule,
    MatInputModule,
    MatFormFieldModule,
    MatProgressSpinnerModule,
    MatBottomSheetModule,
    MatSnackBarModule,
    ExpenseListComponent,
    WhatIfSimulatorComponent,
    GamificationPanelComponent
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
  animations: [
    trigger('fadeSlide', [
      transition(':enter', [
        style({ opacity: 0, transform: 'translateY(12px)' }),
        animate('300ms ease-out', style({ opacity: 1, transform: 'translateY(0)' }))
      ])
    ]),
    trigger('countUp', [
      transition('* => *', [
        style({ opacity: 0.5, transform: 'scale(0.95)' }),
        animate('200ms ease-out', style({ opacity: 1, transform: 'scale(1)' }))
      ])
    ])
  ]
})
export class DashboardComponent implements OnInit {
  budgetForm: FormGroup;
  dailyAllowance = 0;
  remainingForToday = 0;
  comingDaysAllowance = 0;
  monthEndSurplus = 0;
  potentialSavings = 0;
  spentToday = 0;
  isLoading = false;
  hasBudget = false;
  todayExpenses: Expense[] = [];
  today = new Date();
  userName = '';

  constructor(
    private fb: FormBuilder,
    private budgetService: BudgetService,
    private expenseService: ExpenseService,
    private auth: AuthService,
    private bottomSheet: MatBottomSheet,
    private snackBar: MatSnackBar
  ) {
    this.budgetForm = this.fb.group({
      income: ['', [Validators.required, Validators.min(1)]]
    });
  }

  ngOnInit(): void {
    this.auth.user$.subscribe(u => this.userName = u?.displayName ?? '');
    this.loadDashboard();
    this.loadTodayExpenses();

    this.budgetForm.get('income')?.valueChanges
      .pipe(
        debounceTime(400),
        distinctUntilChanged(),
        filter(value => value > 0)
      )
      .subscribe(amount => this.calculateDashboard(amount));
  }

  get todayProgress(): number {
    if (this.dailyAllowance <= 0) return 0;
    return Math.min(100, (this.spentToday / this.dailyAllowance) * 100);
  }

  get progressCircumference(): number {
    return 2 * Math.PI * 54;
  }

  get progressOffset(): number {
    return this.progressCircumference * (1 - this.todayProgress / 100);
  }

  calculateDashboard(amount: number): void {
    this.isLoading = true;
    this.budgetService.calculate({ totalIncome: amount }).subscribe({
      next: (data) => this.applyDashboard(data),
      error: () => {
        this.isLoading = false;
        this.snackBar.open('Could not calculate budget', 'Dismiss', { duration: 4000 });
      }
    });
  }

  onSetBudget(): void {
    const amount = this.budgetForm.get('income')?.value;
    if (amount > 0) {
      this.calculateDashboard(amount);
    }
  }

  logout(): void {
    this.auth.logout();
  }

  openExpenseForm(expense?: Expense): void {
    const ref = this.bottomSheet.open(ExpenseFormComponent, { data: { expense } });
    ref.afterDismissed().subscribe(saved => {
      if (saved) this.refreshAfterExpense();
    });
  }

  editExpense(expense: Expense): void {
    this.openExpenseForm(expense);
  }

  deleteExpense(expense: Expense): void {
    this.expenseService.deleteExpense(expense.expenseId).subscribe({
      next: () => {
        this.snackBar.open('Expense deleted', 'OK', { duration: 2000 });
        this.refreshAfterExpense();
      },
      error: () => this.snackBar.open('Could not delete expense', 'Dismiss', { duration: 3000 })
    });
  }

  openSmsImport(): void {
    const ref = this.bottomSheet.open(SmsImportComponent);
    ref.afterDismissed().subscribe(saved => {
      if (saved) {
        this.refreshAfterExpense();
        this.snackBar.open('Expense imported from SMS', 'OK', { duration: 3000 });
      }
    });
  }

  private refreshAfterExpense(): void {
    this.loadTodayExpenses();
    this.loadDashboard();
  }

  formatIndianNumber(value: number): string {
    return new Intl.NumberFormat('en-IN', { maximumFractionDigits: 0 }).format(value);
  }

  private loadDashboard(): void {
    this.budgetService.getDashboard().subscribe({
      next: (data) => {
        if (data.dailyAllowance > 0) {
          this.applyDashboard(data);
          this.hasBudget = true;
        }
      },
      error: () => {}
    });
  }

  private loadTodayExpenses(): void {
    this.expenseService.getTodayExpenses().subscribe({
      next: (expenses) => this.todayExpenses = expenses,
      error: () => {}
    });
  }

  private applyDashboard(data: BudgetDashboard): void {
    this.dailyAllowance = data.dailyAllowance;
    this.remainingForToday = data.remainingForToday;
    this.comingDaysAllowance = data.comingDaysAllowance;
    this.monthEndSurplus = data.monthEndSurplus;
    this.potentialSavings = data.potentialSavings;
    this.spentToday = data.spentToday ?? 0;
    this.isLoading = false;
    this.hasBudget = true;
  }
}
