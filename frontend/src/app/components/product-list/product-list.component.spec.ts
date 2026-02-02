import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ProductListComponent } from './product-list.component';
import { ProductService } from '../../services/product.service';
import { of } from 'rxjs';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { PageEvent } from '@angular/material/paginator';
import { CheckoutService } from '../../services/checkout.service';

describe('ProductListComponent', () => {
  let component: ProductListComponent;
  let fixture: ComponentFixture<ProductListComponent>;
  let productServiceMock: any;
  let checkoutServiceMock: any;

  const mockPageResponse = {
    content: [
      { id: 1, name: 'Apple', unitPrice: 0.5 },
      { id: 2, name: 'Banana', unitPrice: 0.3 }
    ],
    totalElements: 2
  };

  beforeEach(async () => {
    productServiceMock = {
      getProducts: jasmine.createSpy('getProducts').and.returnValue(of(mockPageResponse))
    };

    checkoutServiceMock = {
      addToCart: jasmine.createSpy('addToCart')
    };

    await TestBed.configureTestingModule({
      imports: [ProductListComponent, NoopAnimationsModule],
      providers: [
        { provide: ProductService, useValue: productServiceMock },
        { provide: CheckoutService, useValue: checkoutServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ProductListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should fetch products with default paging on init', () => {
    expect(productServiceMock.getProducts).toHaveBeenCalledWith(0, 10);
    expect(component.products.length).toBe(2);
    expect(component.totalElements).toBe(2);
  });

  it('should render product cards correctly', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    const cards = compiled.querySelectorAll('mat-card');

    expect(cards.length).toBe(2);
    expect(cards[0].querySelector('mat-card-title')?.textContent).toContain('Apple');
    expect(cards[1].querySelector('mat-card-title')?.textContent).toContain('Banana');
  });

  it('should refresh list when page changes', () => {
    const pageEvent: PageEvent = { pageIndex: 1, pageSize: 5, length: 2 };

    component.onPageChange(pageEvent);

    expect(component.currentPage).toBe(1);
    expect(component.pageSize).toBe(5);
    expect(productServiceMock.getProducts).toHaveBeenCalledWith(1, 5);
  });

  it('should show "No products found" when list is empty', () => {
    productServiceMock.getProducts.and.returnValue(of({ content: [], totalElements: 0 }));

    component.listProducts();
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('.no-products')).toBeTruthy();
    expect(compiled.querySelectorAll('mat-card').length).toBe(0);
  });
});
