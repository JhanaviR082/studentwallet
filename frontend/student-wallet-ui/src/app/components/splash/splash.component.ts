import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-splash',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './splash.component.html',
  styleUrl: './splash.component.scss',
  animations: [
    trigger('walletState', [
      state('closed', style({
        transform: 'scale(0.8) rotateY(0deg)',
        opacity: 1
      })),
      state('opening', style({
        transform: 'scale(1) rotateY(180deg)',
        opacity: 1
      })),
      state('open', style({
        transform: 'scale(1.05) rotateY(180deg)',
        opacity: 0
      })),
      transition('closed => opening', animate('400ms ease-in-out')),
      transition('opening => open', animate('400ms ease-in-out'))
    ]),
    trigger('fadeIn', [
      transition(':enter', [
        style({ opacity: 0, transform: 'translateY(20px)' }),
        animate('500ms 600ms ease-out', style({ opacity: 1, transform: 'translateY(0)' }))
      ])
    ])
  ]
})
export class SplashComponent implements OnInit {
  @Output() splashComplete = new EventEmitter<void>();

  walletState: 'closed' | 'opening' | 'open' = 'closed';
  halvesOpen = false;
  showTagline = false;

  constructor(private router: Router) {}

  ngOnInit(): void {
    setTimeout(() => {
      this.showTagline = true;
    }, 500);

    setTimeout(() => this.startAnimation(), 1500);
  }

  onTap(): void {
    if (this.walletState === 'closed') {
      this.startAnimation();
    }
  }

  private startAnimation(): void {
    if (this.walletState !== 'closed') {
      return;
    }

    this.walletState = 'opening';

    setTimeout(() => {
      this.halvesOpen = true;
      this.walletState = 'open';
    }, 400);

    setTimeout(() => {
      this.router.navigate(['/dashboard']);
      this.splashComplete.emit();
    }, 900);
  }
}
