import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { GamificationService } from '../../services/gamification.service';
import { Badge, Gamification } from '../../models/budget.models';

@Component({
  selector: 'app-gamification-panel',
  standalone: true,
  imports: [CommonModule, MatIconModule, MatProgressBarModule],
  templateUrl: './gamification-panel.component.html',
  styleUrl: './gamification-panel.component.scss'
})
export class GamificationPanelComponent implements OnInit {
  profile: Gamification | null = null;
  loading = true;

  constructor(private gamificationService: GamificationService) {}

  ngOnInit(): void {
    this.gamificationService.getProfile().subscribe({
      next: (data) => {
        this.profile = data;
        this.loading = false;
      },
      error: () => { this.loading = false; }
    });
  }

  badgeProgress(badge: Badge): number {
    return Math.min(100, (badge.progress / badge.target) * 100);
  }

  format(value: number): string {
    return new Intl.NumberFormat('en-IN', { maximumFractionDigits: 0 }).format(value);
  }
}
