import { ComponentFixture, TestBed } from '@angular/core/testing';
import { OfferDialogComponent } from './offer-dialog.component';
import { ProductService } from '../../services/product.service';
import { OfferService } from '../../services/offer.service';
import { MatDialogRef } from '@angular/material/dialog';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { of } from 'rxjs';
import { By } from '@angular/platform-browser';

describe('OfferDialogComponent', () => {
  let component: OfferDialogComponent;
  let fixture: ComponentFixture<OfferDialogComponent>;

  let productServiceMock: any;
  let offerServiceMock: any;
  let dialogRefMock: any;

  const mockProducts = [{ id: 1, name: 'Apple', unitPrice: 1.0 }];
  const mockOffers = [{ id: 101, product: { name: 'Apple' }, requiredQuantity: 3, offerPrice: 2.0 }];

  beforeEach(async () => {
    productServiceMock = {
      getProducts: jasmine.createSpy('getProducts').and.returnValue(of(mockProducts))
    };
    offerServiceMock = {
      getOffers: jasmine.createSpy('getOffers').and.returnValue(of(mockOffers)),
      createOffer: jasmine.createSpy('createOffer').and.returnValue(of({}))
    };
    dialogRefMock = {
      close: jasmine.createSpy('close')
    };

    await TestBed.configureTestingModule({
      imports: [
        OfferDialogComponent,
        NoopAnimationsModule
      ],
      providers: [
        { provide: ProductService, useValue: productServiceMock },
        { provide: OfferService, useValue: offerServiceMock },
        { provide: MatDialogRef, useValue: dialogRefMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(OfferDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges(); // ngOnInit'i tetikler
  });

  it('should create and load initial data', () => {
    expect(component).toBeTruthy();
    expect(productServiceMock.getProducts).toHaveBeenCalled();
    expect(offerServiceMock.getOffers).toHaveBeenCalled();
    expect(component.products.length).toBe(1);
    expect(component.existingOffers.length).toBe(1);
  });

  it('should validate form correctly', () => {
    const form = component.offerForm;
    expect(form.valid).toBeFalsy();

    form.controls['productId'].setValue(1);
    form.controls['requiredQuantity'].setValue(5);
    form.controls['offerPrice'].setValue(10);

    expect(form.valid).toBeTruthy();
  });

  it('should prevent submission if requiredQuantity is less than 2', () => {
    component.offerForm.controls['requiredQuantity'].setValue(1);
    expect(component.offerForm.get('requiredQuantity')?.errors?.['min']).toBeTruthy();
  });

  it('should call createOffer and close dialog on valid submit', () => {
    component.offerForm.patchValue({
      productId: 1,
      requiredQuantity: 3,
      offerPrice: 2.5
    });

    component.onSubmit();

    expect(offerServiceMock.createOffer).toHaveBeenCalled();
    expect(dialogRefMock.close).toHaveBeenCalledWith(true);
  });

  it('should display "no active offer" message when list is empty', () => {
    component.existingOffers = [];
    fixture.detectChanges();

    const debugElement = fixture.debugElement.query(By.css('div[style*="color: #999"]'));
    expect(debugElement.nativeElement.textContent).toContain('There is no active offer.');
  });

  it('should render active offers in the list', () => {
    const listItems = fixture.debugElement.queryAll(By.css('mat-list-item'));
    expect(listItems.length).toBe(1);
    expect(listItems[0].nativeElement.textContent).toContain('Apple');
  });
});
