import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatBottomSheetRef } from '@angular/material/bottom-sheet';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ExpenseService } from '../../services/expense.service';
import { SmsImportResponse } from '../../models/budget.models';

@Component({
  selector: 'app-sms-import',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './sms-import.component.html',
  styleUrl: './sms-import.component.scss'
})
export class SmsImportComponent {
  form: FormGroup;
  importing = false;
  result: SmsImportResponse | null = null;

  readonly sampleSms = 'Rs.149 debited from A/c **1234 on 24-Jun to VPA zomato@paytm. UPI Ref 123456.';

  constructor(
    private fb: FormBuilder,
    private expenseService: ExpenseService,
    private bottomSheetRef: MatBottomSheetRef<SmsImportComponent>
  ) {
    this.form = this.fb.group({
      smsText: ['', [Validators.required, Validators.minLength(10)]]
    });
  }

  useSample(): void {
    this.form.patchValue({ smsText: this.sampleSms });
  }

  import(): void {
    if (this.form.invalid || this.importing) return;

    this.importing = true;
    this.result = null;

    this.expenseService.importSms({ smsText: this.form.value.smsText }).subscribe({
      next: (response) => {
        this.result = response;
        this.importing = false;
        if (response.parsed && response.expense) {
          setTimeout(() => this.bottomSheetRef.dismiss(true), 1200);
        }
      },
      error: () => {
        this.importing = false;
        this.result = { parsed: false, message: 'Import failed. Check backend connection.' };
      }
    });
  }

  close(): void {
    this.bottomSheetRef.dismiss(false);
  }
}
