import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { BudgetCalculateRequest, BudgetDashboard, WhatIfRequest, WhatIfResponse } from '../models/budget.models';

@Injectable({ providedIn: 'root' })
export class BudgetService {
  private readonly baseUrl = `${environment.apiUrl}/budget`;

  constructor(private http: HttpClient) {}

  calculate(request: BudgetCalculateRequest): Observable<BudgetDashboard> {
    return this.http.post<BudgetDashboard>(`${this.baseUrl}/calculate`, request);
  }

  getDashboard(): Observable<BudgetDashboard> {
    return this.http.get<BudgetDashboard>(`${this.baseUrl}/dashboard`);
  }

  whatIf(request: WhatIfRequest): Observable<WhatIfResponse> {
    return this.http.post<WhatIfResponse>(`${this.baseUrl}/what-if`, request);
  }
}
