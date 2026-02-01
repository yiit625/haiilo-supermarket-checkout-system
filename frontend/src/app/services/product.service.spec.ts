import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ProductService } from './product.service';
import { Product, ProductRequest } from '../models/product.model';

describe('ProductService', () => {
  let service: ProductService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ProductService]
    });
    service = TestBed.inject(ProductService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch products from API via GET', () => {
    const mockProducts = [
      { id: 1, name: 'Apple', unitPrice: 0.5 },
      { id: 2, name: 'Banana', unitPrice: 0.3 }
    ];

    service.getProducts().subscribe(products => {
      expect(products.length).toBe(2);
      expect(products).toEqual(mockProducts);
    });

    const req = httpMock.expectOne('http://localhost:8080/api/products');
    expect(req.request.method).toBe('GET');

    req.flush(mockProducts);
  });

  it('should create a product via POST', () => {
    const newProductReq: ProductRequest = { name: 'Apple', unitPrice: 0.5 };
    const savedProduct: Product = { id: 1, name: 'Apple', unitPrice: 0.5 };

    service.createProduct(newProductReq).subscribe(product => {
      expect(product).toEqual(savedProduct);
    });

    const req = httpMock.expectOne('http://localhost:8080/api/products');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(newProductReq);
    req.flush(savedProduct);
  });
});
