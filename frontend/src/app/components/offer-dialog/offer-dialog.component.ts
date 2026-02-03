import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { ProductService } from '../../services/product.service';
import { OfferService } from '../../services/offer.service';
import { Product } from '../../models/product.model';
import { BulkOffer } from '../../models/bulk-offer.model';
import { MatDividerModule } from '@angular/material/divider';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { NotificationService } from '../../services/notification.service';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatAutocompleteModule, MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';
import {Observable} from 'rxjs';

@Component({
  selector: 'app-offer-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatListModule,
    MatDividerModule,
    MatIconModule,
    MatSnackBarModule,
    MatAutocompleteModule
  ],
  templateUrl: './offer-dialog.component.html',
  styleUrls: ['./offer-dialog.component.scss']
})
export class OfferDialogComponent implements OnInit {
  offerForm: FormGroup;
  filteredProducts: Product[] = [];
  productSearchControl = new FormControl();

  existingOffers: BulkOffer[] = [];

  constructor(
    private fb: FormBuilder,
    private productService: ProductService,
    private offerService: OfferService,
    public dialogRef: MatDialogRef<OfferDialogComponent>,
    private notificationService: NotificationService
  ) {
    this.offerForm = this.fb.group({
      productId: [null, Validators.required],
      requiredQuantity: [3, [Validators.required, Validators.min(2)]],
      offerPrice: [null, [Validators.required, Validators.min(0.01)]],
      expiryDate: [null]
    });
  }

  ngOnInit(): void {
    this.loadOffers();

    this.productSearchControl.valueChanges.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(value => {
        if (typeof value === 'string' && value.length > 0) {
          return this.productService.getProducts(0, 10, value);
        } else {
          return new Observable<any>(observer => observer.next({ content: [] }));
        }
      })
    ).subscribe(response => {
      this.filteredProducts = response.content;
    });
  }

  onProductSelected(event: MatAutocompleteSelectedEvent): void {
    const selectedProduct = event.option.value;
    this.offerForm.get('productId')?.setValue(selectedProduct);
  }


  loadOffers(): void {
    this.offerService.getOffers().subscribe(data => this.existingOffers = data);
  }

  onDeleteOffer(id: number): void {
    this.offerService.deleteOffer(id).subscribe({
      next: () => {
        this.notificationService.showSuccess('Offer deleted successfully!');
        this.loadOffers();
      },
      error: (err) => {
        this.notificationService.showError(`Error:${err}`);
      }
    })
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
