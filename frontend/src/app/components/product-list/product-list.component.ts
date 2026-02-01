import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { ProductService } from '../../services/product.service';
import { CheckoutService } from '../../services/checkout.service';
import { Product } from '../../models/product.model';
import {MatDialog, MatDialogModule} from '@angular/material/dialog';
import {OfferDialogComponent} from '../offer-dialog/offer-dialog.component';
import {MatIconModule} from '@angular/material/icon';
import {ProductDialogComponent} from '../product-dialog/product-dialog.component';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatButtonModule, MatIconModule, MatDialogModule],
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss']
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];

  constructor(
    private productService: ProductService,
    private checkoutService: CheckoutService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.productService.getProducts().subscribe({
      next: (data) => this.products = data,
      error: (err) => console.error('Error:', err)
    });
  }

  addToCart(product: Product): void {
    this.checkoutService.addToCart(product);
  }

  openOfferDialog(): void {
    const dialogRef = this.dialog.open(OfferDialogComponent, {
      width: '400px'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        console.log('Give a successfully message in here!');
      }
    });
  }

  openProductDialog(): void {
    const dialogRef = this.dialog.open(ProductDialogComponent, {
      width: '400px'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        console.log('Give a successfully message in here!');
      }
    });
  }
}
