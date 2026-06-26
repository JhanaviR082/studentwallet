import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Gamification } from '../models/budget.models';

@Injectable({ providedIn: 'root' })
export class GamificationService {
  private readonly baseUrl = `${environment.apiUrl}/gamification`;

  constructor(private http: HttpClient) {}

  getProfile(): Observable<Gamification> {
    return this.http.get<Gamification>(this.baseUrl);
  }
}
