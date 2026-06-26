import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { CATEGORY_ICONS, Expense } from '../../models/budget.models';

@Component({
  selector: 'app-expense-list',
  standalone: true,
  imports: [CommonModule, MatIconModule, MatButtonModule],
  templateUrl: './expense-list.component.html',
  styleUrl: './expense-list.component.scss'
})
export class ExpenseListComponent {
  @Input() expenses: Expense[] = [];
  @Output() edit = new EventEmitter<Expense>();
  @Output() delete = new EventEmitter<Expense>();

  getIcon(category: string): string {
    return CATEGORY_ICONS[category] ?? 'receipt';
  }

  formatAmount(amount: number): string {
    return new Intl.NumberFormat('en-IN', { maximumFractionDigits: 0 }).format(amount);
  }
}
