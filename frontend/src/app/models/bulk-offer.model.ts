import { Product } from './product.model';

export interface BulkOffer {
  id?: number;
  product: Product;
  requiredQuantity: number;
  offerPrice: number;
}

export interface BulkOfferRequest {
  productId: number;
  requiredQuantity: number;
  offerPrice: number;
}
