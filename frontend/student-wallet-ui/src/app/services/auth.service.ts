import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';

export interface AuthUser {
  userId: string;
  displayName: string;
  email: string;
  token: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
  displayName: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly storageKey = 'student_wallet_auth';
  private userSubject = new BehaviorSubject<AuthUser | null>(this.loadUser());

  user$ = this.userSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {}

  get token(): string | null {
    return this.userSubject.value?.token ?? null;
  }

  get isLoggedIn(): boolean {
    return !!this.token;
  }

  register(request: RegisterRequest): Observable<AuthUser> {
    return this.http.post<AuthUser>(`${environment.apiUrl}/auth/register`, request).pipe(
      tap(user => this.setUser(user))
    );
  }

  login(request: LoginRequest): Observable<AuthUser> {
    return this.http.post<AuthUser>(`${environment.apiUrl}/auth/login`, request).pipe(
      tap(user => this.setUser(user))
    );
  }

  logout(): void {
    localStorage.removeItem(this.storageKey);
    this.userSubject.next(null);
    this.router.navigate(['/login']);
  }

  private setUser(user: AuthUser): void {
    localStorage.setItem(this.storageKey, JSON.stringify(user));
    this.userSubject.next(user);
  }

  private loadUser(): AuthUser | null {
    const raw = localStorage.getItem(this.storageKey);
    if (!raw) return null;
    try {
      return JSON.parse(raw) as AuthUser;
    } catch {
      return null;
    }
  }
}
