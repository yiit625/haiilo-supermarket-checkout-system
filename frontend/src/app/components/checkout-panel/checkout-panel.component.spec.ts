import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CheckoutPanelComponent } from './checkout-panel.component';
import { CheckoutService } from '../../services/checkout.service';
import {of, throwError} from 'rxjs';
import { MatListModule } from '@angular/material/list';
import { MatDividerModule } from '@angular/material/divider';
import { By } from '@angular/platform-browser';

describe('CheckoutPanelComponent', () => {
  let component: CheckoutPanelComponent;
  let fixture: ComponentFixture<CheckoutPanelComponent>;
  let checkoutServiceMock: any;

  const mockCheckoutResponse = {
    items: [
      {
        productName: 'Apple',
        quantity: 3,
        priceBeforeDiscount: 1.50,
        priceAfterDiscount: 1.25,
        discountApplied: true
      }
    ],
    finalTotal: 1.25
  };

  beforeEach(async () => {
    checkoutServiceMock = {
      getCartItems: jasmine.createSpy('getCartItems').and.returnValue([
        { product: { id: 1, name: 'Apple', unitPrice: 0.5 }, quantity: 3 }
      ]),
      processCheckout: jasmine.createSpy('processCheckout').and.returnValue(of(mockCheckoutResponse)),
      clearCart: jasmine.createSpy('clearCart')
    };

    await TestBed.configureTestingModule({
      imports: [CheckoutPanelComponent, MatListModule, MatDividerModule],
      providers: [
        { provide: CheckoutService, useValue: checkoutServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CheckoutPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should display cart items with basic info initially', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain('Apple');
    expect(compiled.textContent).toContain('3 x €0.50');
  });

  it('should display discounted prices (strike-through) after successful checkout', () => {
    const checkoutBtn = fixture.debugElement.query(By.css('button[color="primary"]')).nativeElement;
    checkoutBtn.click();

    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;

    // 2. Final total
    expect(compiled.textContent).toContain('Total to Pay: €1.25');

    // 3. Discounted prices should be shown deleted
    const oldPriceSpan = fixture.debugElement.query(By.css('.old-price'));
    const newPriceSpan = fixture.debugElement.query(By.css('.new-price'));

    expect(oldPriceSpan.nativeElement.textContent).toContain('€1.50');
    expect(newPriceSpan.nativeElement.textContent).toContain('€1.25');

    // 4. Discount icon
    const icon = fixture.debugElement.query(By.css('mat-icon'));
    expect(icon).toBeTruthy();
  });

  it('should call clearCart and reset local state when Clear button is clicked', () => {
    component.totalPrice = 1.25;
    component.checkoutDetails = mockCheckoutResponse.items;
    fixture.detectChanges();

    checkoutServiceMock.getCartItems.and.returnValue([]);

    const clearBtn = fixture.debugElement.query(By.css('button[color="warn"]')).nativeElement;
    clearBtn.click();

    expect(checkoutServiceMock.clearCart).toHaveBeenCalled();
    expect(component.totalPrice).toBeNull();
    expect(component.checkoutDetails.length).toBe(0);
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain('Your cart is empty');
  });

  it('should show error message when checkout fails', () => {
    checkoutServiceMock.processCheckout.and.returnValue(throwError(() => new Error('Server error')));

    component.onCheckout();
    fixture.detectChanges();

    const errorMsg = fixture.debugElement.query(By.css('.error'));
    expect(errorMsg).toBeTruthy();
    expect(errorMsg.nativeElement.textContent).toContain('Checkout failed');
  });
});
