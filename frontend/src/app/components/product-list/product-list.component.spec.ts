import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ProductListComponent } from './product-list.component';
import { ProductService } from '../../services/product.service';
import { CheckoutService } from '../../services/checkout.service';
import { NotificationService } from '../../services/notification.service';
import {MatDialog, MatDialogModule} from '@angular/material/dialog';
import { of } from 'rxjs';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientTestingModule } from '@angular/common/http/testing';


describe('ProductListComponent', () => {
  let component: ProductListComponent;
  let fixture: ComponentFixture<ProductListComponent>;

  let productServiceMock: any;
  let checkoutServiceMock: any;
  let notificationServiceMock: any;
  let dialogMock: any;

  const mockProduct = { id: 1, name: 'Apple', unitPrice: 0.5 };
  const mockPageResponse = {
    content: [mockProduct],
    totalElements: 1
  };

  beforeEach(async () => {
    productServiceMock = {
      getProducts: jasmine.createSpy('getProducts').and.returnValue(of(mockPageResponse)),
      deleteProduct: jasmine.createSpy('deleteProduct').and.returnValue(of({}))
    };

    checkoutServiceMock = {
      getCartItems: jasmine.createSpy('getCartItems').and.returnValue([]),
      addToCart: jasmine.createSpy('addToCart'),
      removeFromCart: jasmine.createSpy('removeFromCart'),
      clearCart: jasmine.createSpy('clearCart')
    };

    notificationServiceMock = {
      showSuccess: jasmine.createSpy('showSuccess'),
      showError: jasmine.createSpy('showError')
    };

    dialogMock = jasmine.createSpyObj('MatDialog', ['open']);
    dialogMock.open.and.returnValue({
      afterClosed: () => of(true)
    } as any);

    await TestBed.configureTestingModule({
      imports: [ProductListComponent, NoopAnimationsModule, HttpClientTestingModule],
      providers: [
        { provide: ProductService, useValue: productServiceMock },
        { provide: CheckoutService, useValue: checkoutServiceMock },
        { provide: NotificationService, useValue: notificationServiceMock },
        { provide: MatDialog, useValue: dialogMock }
      ]
    }).overrideComponent(ProductListComponent, {
      remove: { imports: [MatDialogModule] },
      add: { providers: [{ provide: MatDialog, useValue: dialogMock }] }
    })
      .compileComponents();

    fixture = TestBed.createComponent(ProductListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should add product to cart when addToCart is called', () => {
    component.addToCart(mockProduct);
    expect(checkoutServiceMock.addToCart).toHaveBeenCalledWith(mockProduct);
  });

  it('should remove product from cart when removeFromCart is called', () => {
    component.removeFromCart(mockProduct);
    expect(checkoutServiceMock.removeFromCart).toHaveBeenCalledWith(mockProduct);
  });


  it('should return correct quantity from checkout service', () => {
    checkoutServiceMock.getCartItems.and.returnValue([{ product: mockProduct, quantity: 3 }]);

    const quantity = component.getQuantity(mockProduct);

    expect(quantity).toBe(3);
    expect(checkoutServiceMock.getCartItems).toHaveBeenCalled();
  });

  it('should return 0 if product is not in cart', () => {
    checkoutServiceMock.getCartItems.and.returnValue([]);
    const quantity = component.getQuantity(mockProduct);
    expect(quantity).toBe(0);
  });

  it('should clear cart and refresh list when a product is deleted from system', () => {
    component.deleteProduct(1);

    expect(productServiceMock.deleteProduct).toHaveBeenCalledWith(1);
    expect(notificationServiceMock.showSuccess).toHaveBeenCalled();
    expect(checkoutServiceMock.clearCart).toHaveBeenCalled();
    expect(productServiceMock.getProducts).toHaveBeenCalled();
  });

  it('should call openOfferDialog when manage offers button is clicked', () => {
    component.openOfferDialog();

    expect(dialogMock.open).toHaveBeenCalled();
    expect(productServiceMock.getProducts).toHaveBeenCalled();
  });
});
