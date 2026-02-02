import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing'; // fakeAsync ve tick eklendi
import { OfferDialogComponent } from './offer-dialog.component';
import { ProductService } from '../../services/product.service';
import { OfferService } from '../../services/offer.service';
import { MatDialogRef } from '@angular/material/dialog';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { of } from 'rxjs';
import { By } from '@angular/platform-browser';
import { NotificationService } from '../../services/notification.service';

describe('OfferDialogComponent', () => {
  let component: OfferDialogComponent;
  let fixture: ComponentFixture<OfferDialogComponent>;

  let productServiceMock: any;
  let offerServiceMock: any;
  let dialogRefMock: any;
  let notificationServiceMock: any;

  const mockProductsResponse = { content: [{ id: 1, name: 'Apple', unitPrice: 1.0 }], totalElements: 1 };
  const mockOffers = [{ id: 101, product: { name: 'Apple' }, requiredQuantity: 3, offerPrice: 2.0 }];

  beforeEach(async () => {
    productServiceMock = {
      getProducts: jasmine.createSpy('getProducts').and.returnValue(of(mockProductsResponse))
    };
    offerServiceMock = {
      getOffers: jasmine.createSpy('getOffers').and.returnValue(of(mockOffers)),
      createOffer: jasmine.createSpy('createOffer').and.returnValue(of({}))
    };
    dialogRefMock = {
      close: jasmine.createSpy('close')
    };
    notificationServiceMock = {
      showSuccess: jasmine.createSpy('showSuccess'),
      showError: jasmine.createSpy('showError')
    };

    await TestBed.configureTestingModule({
      imports: [OfferDialogComponent, NoopAnimationsModule],
      providers: [
        { provide: ProductService, useValue: productServiceMock },
        { provide: OfferService, useValue: offerServiceMock },
        { provide: MatDialogRef, useValue: dialogRefMock },
        { provide: NotificationService, useValue: notificationServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(OfferDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should load initial offers on init', () => {
    expect(offerServiceMock.getOffers).toHaveBeenCalled();
    expect(component.existingOffers.length).toBe(1);
  });

  it('should fetch filtered products when user types in search control', fakeAsync(() => {
    component.productSearchControl.setValue('App');

    tick(300);
    fixture.detectChanges();

    expect(productServiceMock.getProducts).toHaveBeenCalledWith(0, 10, 'App');
    expect(component.filteredProducts.length).toBe(1);
    expect(component.filteredProducts[0].name).toBe('Apple');
  }));

  it('should validate form correctly', () => {
    const form = component.offerForm;
    expect(form.valid).toBeFalsy();

    form.controls['productId'].setValue(1);
    form.controls['requiredQuantity'].setValue(5);
    form.controls['offerPrice'].setValue(10);

    expect(form.valid).toBeTruthy();
  });

  it('should call createOffer and show success notification on valid submit', () => {
    component.offerForm.patchValue({
      productId: 1,
      requiredQuantity: 3,
      offerPrice: 2.5
    });

    component.onSubmit();

    expect(offerServiceMock.createOffer).toHaveBeenCalled();
    expect(notificationServiceMock.showSuccess).toHaveBeenCalled();
    expect(dialogRefMock.close).toHaveBeenCalledWith(true);
  });

  it('should display "no active offer" message when list is empty', () => {
    component.existingOffers = [];
    fixture.detectChanges();

    const noOfferDiv = fixture.debugElement.query(By.css('.offers-section div[style*="color: #999"]'));
    expect(noOfferDiv.nativeElement.textContent).toContain('There is no active offer.');
  });
});
