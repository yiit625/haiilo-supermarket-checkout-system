import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ProductListComponent } from './product-list.component';
import { ProductService } from '../../services/product.service';
import { of } from 'rxjs';
import { MatCardModule } from '@angular/material/card';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('ProductListComponent', () => {
  let component: ProductListComponent;
  let fixture: ComponentFixture<ProductListComponent>;
  let productServiceMock: any;

  beforeEach(async () => {
    productServiceMock = {
      getProducts: jasmine.createSpy('getProducts').and.returnValue(of([
        { id: 1, name: 'Apple', unitPrice: 0.5 },
        { id: 2, name: 'Banana', unitPrice: 0.3 }
      ]))
    };

    await TestBed.configureTestingModule({
      imports: [ProductListComponent, MatCardModule, HttpClientTestingModule],
      providers: [
        { provide: ProductService, useValue: productServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ProductListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should fetch products on init', () => {
    expect(productServiceMock.getProducts).toHaveBeenCalled();
    expect(component.products.length).toBe(2);
  });

  it('should render product cards', () => {
    const compiled = fixture.nativeElement as HTMLElement;

    expect(compiled.querySelectorAll('mat-card').length).toBe(2);
    expect(compiled.textContent).toContain('Apple');
  });
});
