export interface BudgetDashboard {
  dailyAllowance: number;
  remainingForToday: number;
  comingDaysAllowance: number;
  monthEndSurplus: number;
  potentialSavings: number;
  spentToday: number;
  totalSpent: number;
}

export interface BudgetCalculateRequest {
  totalIncome: number;
  startDate?: string;
  endDate?: string;
}

export interface Expense {
  expenseId: string;
  category: string;
  description: string;
  amount: number;
  expenseDate: string;
  verdict: 'Better' | 'Bad' | 'Neutral';
  essential: boolean;
}

export interface ExpenseRequest {
  amount: number;
  category: string;
  description?: string;
  expenseDate?: string;
}

export const EXPENSE_CATEGORIES = [
  'Mess/Canteen',
  'Chai/Coffee',
  'Travel/Auto',
  'Swiggy/Zomato',
  'Academic',
  'Entertainment',
  'Subscriptions',
  'Other'
] as const;

export const CATEGORY_ICONS: Record<string, string> = {
  'Mess/Canteen': 'restaurant',
  'Chai/Coffee': 'local_cafe',
  'Travel/Auto': 'directions_car',
  'Swiggy/Zomato': 'delivery_dining',
  'Academic': 'school',
  'Entertainment': 'movie',
  'Subscriptions': 'subscriptions',
  'Other': 'more_horiz'
};

export interface Badge {
  id: string;
  name: string;
  description: string;
  icon: string;
  earned: boolean;
  progress: number;
  target: number;
  bonusAmount: number;
}

export interface Gamification {
  currentStreak: number;
  longestStreak: number;
  badges: Badge[];
  displayName: string;
  savingsVault: number;
  totalDailySavings: number;
  todaySaved: number;
}

export interface WhatIfRequest {
  extraSavingsGoal: number;
}

export interface WhatIfResponse {
  currentDailyBudget: number;
  adjustedDailyBudget: number;
  dailyReduction: number;
  extraSavingsGoal: number;
  daysRemaining: number;
  tips: string[];
}

export interface SmsImportRequest {
  smsText: string;
}

export interface SmsImportResponse {
  parsed: boolean;
  amount?: number;
  category?: string;
  merchant?: string;
  description?: string;
  expense?: Expense;
  message: string;
}
