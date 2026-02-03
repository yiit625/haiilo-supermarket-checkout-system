import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BulkOffer, BulkOfferRequest } from '../models/bulk-offer.model';

@Injectable({ providedIn: 'root' })
export class OfferService {
  private apiUrl = 'http://localhost:8080/api/offers';

  constructor(private http: HttpClient) {}

  getOffers(): Observable<BulkOffer[]> {
    return this.http.get<BulkOffer[]>(this.apiUrl);
  }

  createOffer(request: BulkOfferRequest): Observable<BulkOffer> {
    return this.http.post<BulkOffer>(this.apiUrl, request);
  }

  deleteOffer(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
}
