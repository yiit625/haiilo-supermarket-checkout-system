import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CheckoutPanelComponent } from './checkout-panel.component';
import { CheckoutService } from '../../services/checkout.service';
import { of } from 'rxjs';
import { MatListModule } from '@angular/material/list';
import { MatDividerModule } from '@angular/material/divider';

describe('CheckoutPanelComponent', () => {
  let component: CheckoutPanelComponent;
  let fixture: ComponentFixture<CheckoutPanelComponent>;
  let checkoutServiceMock: any;

  beforeEach(async () => {
    checkoutServiceMock = {
      getCartItems: jasmine.createSpy('getCartItems').and.returnValue([
        { product: { id: 1, name: 'Apple', unitPrice: 0.5 }, quantity: 3 }
      ]),
      processCheckout: jasmine.createSpy('processCheckout').and.returnValue(of({ totalPrice: 1.0 })),
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

  it('should display cart items correctly', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain('Apple');
    expect(compiled.textContent).toContain('3 x €0.50');
  });

  it('should call processCheckout and show total price when button is clicked', () => {
    const checkoutBtn = fixture.nativeElement.querySelector('button[color="primary"]');
    checkoutBtn.click();

    expect(checkoutServiceMock.processCheckout).toHaveBeenCalled();

    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain('Total to Pay: €1.00');
  });

  it('should clear UI when Clear button is clicked', () => {
    const clearBtn = fixture.nativeElement.querySelector('button[color="warn"]');
    clearBtn.click();

    expect(checkoutServiceMock.clearCart).toHaveBeenCalled();
    expect(component.totalPrice).toBeNull();
  });
});
