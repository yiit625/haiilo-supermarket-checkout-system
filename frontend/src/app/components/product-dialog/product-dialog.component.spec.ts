import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ProductDialogComponent } from './product-dialog.component';
import { ProductService } from '../../services/product.service';
import { NotificationService } from '../../services/notification.service';
import { MatDialogRef } from '@angular/material/dialog';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { of } from 'rxjs';

describe('ProductDialogComponent', () => {
  let component: ProductDialogComponent;
  let fixture: ComponentFixture<ProductDialogComponent>;

  let productServiceMock: any;
  let dialogRefMock: any;
  let notificationServiceMock: any;

  beforeEach(async () => {
    productServiceMock = {
      createProduct: jasmine.createSpy('createProduct').and.returnValue(of({ id: 1, name: 'Apple', unitPrice: 1.0 }))
    };
    dialogRefMock = {
      close: jasmine.createSpy('close')
    };
    notificationServiceMock = {
      showSuccess: jasmine.createSpy('showSuccess'),
      showError: jasmine.createSpy('showError')
    };

    await TestBed.configureTestingModule({
      imports: [
        ProductDialogComponent,
        NoopAnimationsModule
      ],
      providers: [
        { provide: ProductService, useValue: productServiceMock },
        { provide: MatDialogRef, useValue: dialogRefMock },
        { provide: NotificationService, useValue: notificationServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ProductDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should validate form correctly', () => {
    const form = component.productForm;
    expect(form.valid).toBeFalsy();

    form.controls['name'].setValue('Banana');
    form.controls['unitPrice'].setValue(0.5);

    expect(form.valid).toBeTruthy();
  });

  it('should prevent submission if unitPrice is less than 0.01', () => {
    const priceControl = component.productForm.controls['unitPrice'];
    priceControl.setValue(0);
    expect(priceControl.errors?.['min']).toBeTruthy();

    priceControl.setValue(0.01);
    expect(priceControl.errors?.['min']).toBeFalsy();
  });

  it('should call createProduct and close dialog on valid submit', () => {
    component.productForm.patchValue({
      name: 'Orange',
      unitPrice: 1.25
    });

    component.onSubmit();

    expect(productServiceMock.createProduct).toHaveBeenCalled();
    expect(notificationServiceMock.showSuccess).toHaveBeenCalledWith('Product created successfully!');
    expect(dialogRefMock.close).toHaveBeenCalledWith(true);
  });

  it('should not call createProduct if form is invalid', () => {
    component.productForm.patchValue({
      name: '',
      unitPrice: 0
    });

    component.onSubmit();

    expect(productServiceMock.createProduct).not.toHaveBeenCalled();
    expect(dialogRefMock.close).not.toHaveBeenCalled();
  });
});
