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
import {MatPaginatorModule, PageEvent} from '@angular/material/paginator';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatButtonModule, MatIconModule, MatDialogModule, MatPaginatorModule],
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss']
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];

  totalElements = 0;
  pageSize = 10;
  currentPage = 0;

  constructor(
    private productService: ProductService,
    private checkoutService: CheckoutService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.listProducts();
  }

  listProducts(): void {
    this.productService.getProducts(this.currentPage, this.pageSize).subscribe({
      next: (response) => {
        this.products = response.content;
        this.totalElements = response.totalElements;
      },
      error: (err) => console.error('Error:', err)
    });
  }

  onPageChange(event: PageEvent): void {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.listProducts();
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
        this.listProducts();
      }
    });
  }
}
