import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { CheckoutService } from './checkout.service';
import { Product } from '../models/product.model';

describe('CheckoutService', () => {
  let service: CheckoutService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [CheckoutService]
    });
    service = TestBed.inject(CheckoutService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should add cart', () => {
    // Arrange
    const product: Product = { id: 101, name: 'Apple', unitPrice: 0.5 };

    // Act
    service.addToCart(product);
    service.addToCart(product); // Add the same product again

    // Assert
    const cartItems = service.getCartItems();
    expect(cartItems.length).toBe(1);
    expect(cartItems[0].product.id).toBe(101);
    expect(cartItems[0].quantity).toBe(2);
  })

  it('should remove cart', () => {
    // Arrange
    const product: Product = { id: 101, name: 'Apple', unitPrice: 0.5 };

    // Act
    service.addToCart(product);
    service.removeFromCart(product);

    // Assert
    expect(service.getCartItems().length).toBe(0);
  })

  it('should transform cart items into a flat list of IDs for the backend', () => {
    // Arrange
    const productA: Product = { id: 101, name: 'Apple', unitPrice: 0.5 };
    const productB: Product = { id: 102, name: 'Banana', unitPrice: 0.3 };

    service.addToCart(productA);
    service.addToCart(productA); // 2 unit Apple
    service.addToCart(productB); // 1 unit Banana

    // Act
    service.processCheckout().subscribe();

    // Assert
    const req = httpMock.expectOne('http://localhost:8080/api/checkout');
    expect(req.request.method).toBe('POST');
    expect(req.request.body.productIds).toEqual([101, 101, 102]);
    expect(req.request.body.productIds.length).toBe(3);
  });

  it('should clear the cart', () => {
    // Arrange
    const product: Product = { id: 101, name: 'Apple', unitPrice: 0.5 };
    service.addToCart(product);

    // Act
    service.clearCart();

    // Assert
    const cartItems = service.getCartItems();
    expect(cartItems.length).toBe(0);
  });
});
