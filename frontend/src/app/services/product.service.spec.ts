import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ProductService } from './product.service';
import { Product, ProductRequest } from '../models/product.model';

describe('ProductService', () => {
  let service: ProductService;
  let httpMock: HttpTestingController;
  const apiUrl = 'http://localhost:8080/api/products';

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

  it('should fetch paged products with search criteria', () => {
    // 1. GIVEN
    const mockPageResponse = {
      content: [
        { id: 1, name: 'Apple', unitPrice: 0.5 },
        { id: 2, name: 'Banana', unitPrice: 0.3 }
      ],
      totalElements: 2,
      totalPages: 1,
      size: 10,
      number: 0
    };

    const page = 0;
    const size = 5;
    const search = 'App';

    // 2. WHEN
    service.getProducts(page, size, search).subscribe(response => {
      expect(response.content.length).toBe(2);
      expect(response.content[0].name).toBe('Apple');
      expect(response.totalElements).toBe(2);
    });

    // 3. THEN
    const req = httpMock.expectOne(request =>
      request.url === apiUrl &&
      request.params.get('page') === '0' &&
      request.params.get('size') === '5' &&
      request.params.get('search') === 'App'
    );

    expect(req.request.method).toBe('GET');
    req.flush(mockPageResponse);
  });

  it('should fetch products without search parameter', () => {
    const mockPageResponse = { content: [], totalElements: 0 };

    service.getProducts(0, 10).subscribe();

    const req = httpMock.expectOne(request =>
      request.url === apiUrl && !request.params.has('search')
    );

    expect(req.request.params.get('page')).toBe('0');
    req.flush(mockPageResponse);
  });

  it('should create a product via POST', () => {
    const newProductReq: ProductRequest = { name: 'Apple', unitPrice: 0.5 };
    const savedProduct: Product = { id: 1, name: 'Apple', unitPrice: 0.5 };

    service.createProduct(newProductReq).subscribe(product => {
      expect(product).toEqual(savedProduct);
    });

    const req = httpMock.expectOne(apiUrl);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(newProductReq);
    req.flush(savedProduct);
  });
});
