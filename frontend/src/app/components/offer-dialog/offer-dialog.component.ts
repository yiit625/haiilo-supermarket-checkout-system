import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { ProductService } from '../../services/product.service';
import { OfferService } from '../../services/offer.service';
import { Product } from '../../models/product.model';
import { BulkOffer } from '../../models/bulk-offer.model';
import { MatDividerModule } from '@angular/material/divider';
import { MatListModule } from '@angular/material/list';
import { MatIcon } from '@angular/material/icon';
import { NotificationService } from '../../services/notification.service';
import { MatSnackBarModule } from '@angular/material/snack-bar';

@Component({
  selector: 'app-offer-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatListModule,
    MatDividerModule,
    MatIcon,
    MatSnackBarModule
  ],
  templateUrl: './offer-dialog.component.html',
  styleUrls: ['./offer-dialog.component.scss']
})
export class OfferDialogComponent implements OnInit {
  offerForm: FormGroup;
  products: Product[] = [];
  existingOffers: BulkOffer[] = [];

  constructor(
    private fb: FormBuilder,
    private productService: ProductService,
    private offerService: OfferService,
    private dialogRef: MatDialogRef<OfferDialogComponent>,
    private notificationService: NotificationService
  ) {
    this.offerForm = this.fb.group({
      productId: [null, Validators.required],
      requiredQuantity: [3, [Validators.required, Validators.min(2)]],
      offerPrice: [null, [Validators.required, Validators.min(0.01)]]
    });
  }

  ngOnInit(): void {
    this.productService.getProducts().subscribe(data => this.products = data);
    this.loadOffers();
  }

  loadOffers(): void {
    this.offerService.getOffers().subscribe(data => this.existingOffers = data);
  }

  onSubmit(): void {
    if (this.offerForm.valid) {
      this.offerService.createOffer(this.offerForm.value).subscribe({
        next: () => {
          this.notificationService.showSuccess('Offer created successfully!');
          this.dialogRef.close(true);
        },
        error: (err) => {
          const details = err.error?.details;
          if (Array.isArray(details) && details.length > 0) {
            details.forEach(msg => this.notificationService.showError(msg));
          } else {
            this.notificationService.showError(err.error?.message || 'Error occurred');
          }
        }
      });
    }
  }
}
