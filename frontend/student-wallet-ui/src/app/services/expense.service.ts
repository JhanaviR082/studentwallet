import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Expense, ExpenseRequest, SmsImportRequest, SmsImportResponse } from '../models/budget.models';

@Injectable({ providedIn: 'root' })
export class ExpenseService {
  private readonly baseUrl = `${environment.apiUrl}/expenses`;

  constructor(private http: HttpClient) {}

  addExpense(request: ExpenseRequest): Observable<Expense> {
    return this.http.post<Expense>(this.baseUrl, request);
  }

  updateExpense(expenseId: string, request: ExpenseRequest): Observable<Expense> {
    return this.http.put<Expense>(`${this.baseUrl}/${expenseId}`, request);
  }

  deleteExpense(expenseId: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${expenseId}`);
  }

  getTodayExpenses(): Observable<Expense[]> {
    return this.http.get<Expense[]>(`${this.baseUrl}/today`);
  }

  getSpendingPattern(): Observable<Expense[]> {
    return this.http.get<Expense[]>(`${this.baseUrl}/pattern`);
  }

  importSms(request: SmsImportRequest): Observable<SmsImportResponse> {
    return this.http.post<SmsImportResponse>(`${this.baseUrl}/import-sms`, request);
  }
}
