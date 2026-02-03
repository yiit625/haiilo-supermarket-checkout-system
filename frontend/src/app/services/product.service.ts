import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {Product, ProductRequest} from '../models/product.model';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl = 'http://localhost:8080/api/products';

  constructor(private http: HttpClient) { }

  getProducts(page: number, size: number, search?: string): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (search) {
      params = params.set('search', search);
    }

    return this.http.get<any>(this.apiUrl, { params });
  }

  createProduct(productRequest: ProductRequest): Observable<Product> {
    return this.http.post<Product>(this.apiUrl, productRequest);
  }

  deleteProduct(id: number): Observable<any> {
    return this.http.delete(this.apiUrl + "/" + id);
  }
}
