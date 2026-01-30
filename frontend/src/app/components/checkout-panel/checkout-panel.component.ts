import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatListModule } from '@angular/material/list';
import { MatButtonModule } from '@angular/material/button';
import { MatDividerModule } from '@angular/material/divider';
import { CheckoutService } from '../../services/checkout.service';

@Component({
  selector: 'app-checkout-panel',
  standalone: true,
  imports: [CommonModule, MatListModule, MatButtonModule, MatDividerModule],
  templateUrl: './checkout-panel.component.html',
  styleUrls: ['./checkout-panel.component.scss']
})
export class CheckoutPanelComponent {
  totalPrice: number | null = null;
  errorMessage: string | null = null;

  constructor(public checkoutService: CheckoutService) {}

  onCheckout() {
    this.errorMessage = null;
    this.checkoutService.processCheckout().subscribe({
      next: (response) => {
        this.totalPrice = response;
      },
      error: (err) => {
        this.errorMessage = "Checkout failed. Please try again.";
        console.error(err);
      }
    });
  }

  onClear() {
    this.checkoutService.clearCart();
    this.totalPrice = null;
  }
}
