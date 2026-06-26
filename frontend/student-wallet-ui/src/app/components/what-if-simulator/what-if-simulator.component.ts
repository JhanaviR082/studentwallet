import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { BudgetService } from '../../services/budget.service';
import { WhatIfResponse } from '../../models/budget.models';

@Component({
  selector: 'app-what-if-simulator',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatIconModule,
    MatButtonModule,
    MatInputModule,
    MatFormFieldModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './what-if-simulator.component.html',
  styleUrl: './what-if-simulator.component.scss'
})
export class WhatIfSimulatorComponent {
  @Input() disabled = false;

  form: FormGroup;
  result: WhatIfResponse | null = null;
  loading = false;
  error = '';

  constructor(private fb: FormBuilder, private budgetService: BudgetService) {
    this.form = this.fb.group({
      extraSavings: ['', [Validators.required, Validators.min(1)]]
    });
  }

  simulate(): void {
    if (this.form.invalid || this.loading) return;

    this.loading = true;
    this.error = '';
    this.result = null;

    this.budgetService.whatIf({ extraSavingsGoal: this.form.value.extraSavings }).subscribe({
      next: (data) => {
        this.result = data;
        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
        this.error = err.error?.error ?? 'Set your budget first to use the simulator.';
      }
    });
  }

  format(value: number): string {
    return new Intl.NumberFormat('en-IN', { maximumFractionDigits: 0 }).format(value);
  }
}
