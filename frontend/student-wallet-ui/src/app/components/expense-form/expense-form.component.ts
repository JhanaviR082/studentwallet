import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_BOTTOM_SHEET_DATA, MatBottomSheetRef } from '@angular/material/bottom-sheet';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';

import { ExpenseService } from '../../services/expense.service';
import { EXPENSE_CATEGORIES, Expense } from '../../models/budget.models';

export interface ExpenseFormData {
  expense?: Expense;
}

@Component({
  selector: 'app-expense-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatIconModule
  ],
  templateUrl: './expense-form.component.html',
  styleUrl: './expense-form.component.scss'
})
export class ExpenseFormComponent {
  form: FormGroup;
  categories = EXPENSE_CATEGORIES;
  saving = false;
  isEdit = false;
  private expense?: Expense;

  constructor(
    private fb: FormBuilder,
    private expenseService: ExpenseService,
    private bottomSheetRef: MatBottomSheetRef<ExpenseFormComponent>,
    @Inject(MAT_BOTTOM_SHEET_DATA) data: ExpenseFormData
  ) {
    this.expense = data?.expense;
    this.isEdit = !!this.expense;

    this.form = this.fb.group({
      amount: [this.expense?.amount ?? '', [Validators.required, Validators.min(0.01)]],
      category: [this.expense?.category ?? 'Mess/Canteen', Validators.required],
      description: [this.expense?.description ?? '']
    });
  }

  save(): void {
    if (this.form.invalid || this.saving) return;

    this.saving = true;
    const request = this.form.value;

    const op = this.isEdit && this.expense
      ? this.expenseService.updateExpense(this.expense.expenseId, request)
      : this.expenseService.addExpense(request);

    op.subscribe({
      next: () => this.bottomSheetRef.dismiss(true),
      error: () => { this.saving = false; }
    });
  }

  cancel(): void {
    this.bottomSheetRef.dismiss(false);
  }
}
