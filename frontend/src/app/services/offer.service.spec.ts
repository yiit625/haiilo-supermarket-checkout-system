import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { OfferService } from './offer.service';
import { BulkOffer, BulkOfferRequest } from '../models/bulk-offer.model';

describe('OfferService', () => {
  let service: OfferService;
  let httpMock: HttpTestingController;
  const apiUrl = 'http://localhost:8080/api/offers';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [OfferService]
    });
    service = TestBed.inject(OfferService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch all offers via GET', () => {
    const dummyOffers: BulkOffer[] = [
      { id: 1, product: { id: 1, name: 'Apple', unitPrice: 0.5 }, requiredQuantity: 3, offerPrice: 1.0 }
    ];

    service.getOffers().subscribe(offers => {
      expect(offers.length).toBe(1);
      expect(offers).toEqual(dummyOffers);
    });

    const req = httpMock.expectOne(apiUrl);
    expect(req.request.method).toBe('GET');
    req.flush(dummyOffers);
  });

  it('should create an offer via POST', () => {
    const newOfferReq: BulkOfferRequest = { productId: 1, requiredQuantity: 2, offerPrice: 0.8 };
    const savedOffer: BulkOffer = { id: 2, product: { id: 1, name: 'Apple', unitPrice: 0.5 }, ...newOfferReq };

    service.createOffer(newOfferReq).subscribe(offer => {
      expect(offer).toEqual(savedOffer);
    });

    const req = httpMock.expectOne(apiUrl);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(newOfferReq);
    req.flush(savedOffer);
  });
});
