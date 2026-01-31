import { TestBed } from '@angular/core/testing';
import { MatSnackBar } from '@angular/material/snack-bar';
import { NotificationService } from './notification.service';

describe('NotificationService', () => {
  let service: NotificationService;
  let snackBarSpy: jasmine.SpyObj<MatSnackBar>;

  beforeEach(() => {
    const spy = jasmine.createSpyObj('MatSnackBar', ['open']);

    TestBed.configureTestingModule({
      providers: [
        NotificationService,
        { provide: MatSnackBar, useValue: spy }
      ]
    });

    service = TestBed.inject(NotificationService);
    snackBarSpy = TestBed.inject(MatSnackBar) as jasmine.SpyObj<MatSnackBar>;
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call snackBar.open with success configuration', () => {
    const message = 'Success test message';
    service.showSuccess(message);

    expect(snackBarSpy.open).toHaveBeenCalledWith(message, 'OK', jasmine.objectContaining({
      duration: 3000,
      panelClass: ['success-snackbar'],
      horizontalPosition: 'end',
      verticalPosition: 'top'
    }));
  });

  it('should call snackBar.open with error configuration', () => {
    const message = 'Error test message';
    service.showError(message);

    expect(snackBarSpy.open).toHaveBeenCalledWith(message, 'Close', jasmine.objectContaining({
      duration: 5000,
      panelClass: ['error-snackbar'],
      horizontalPosition: 'end',
      verticalPosition: 'top'
    }));
  });
});
