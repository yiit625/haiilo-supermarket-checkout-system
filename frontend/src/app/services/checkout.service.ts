import { Injectable } from '@angular/core';
import { Product } from '../models/product.model';
import { CartItem } from '../models/cart-item.model';
import { Observable } from 'rxjs';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class CheckoutService {
  private cartItems: CartItem[] = [];
  private apiUrl = 'http://localhost:8080/api/checkout';

  constructor(private http: HttpClient) { }

  getCartItems(): CartItem[] {
    return this.cartItems;
  }

  addToCart(product: Product): void {
    const existingItem = this.cartItems.find(item => item.product.id === product.id);

    existingItem ? existingItem.quantity += 1 : this.cartItems.push({ product, quantity: 1 });
  }

  removeFromCart(product: Product): void {
    const existingItem = this.cartItems.find(item => item.product.id === product.id);

    if (existingItem) {
      if (existingItem.quantity > 1) {
        existingItem.quantity -= 1;
      } else {
        this.cartItems = this.cartItems.filter(item => item.product.id !== product.id);
      }
    }
  }

  processCheckout(): Observable<any> {
    const productIds = this.cartItems.flatMap(item =>
      Array(item.quantity).fill(item.product.id)
    );

    return this.http.post<any>(this.apiUrl, { productIds });
  }

  clearCart(): void {
    this.cartItems = [];
  }
}
