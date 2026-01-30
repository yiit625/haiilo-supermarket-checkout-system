import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CheckoutPanelComponent } from './checkout-panel.component';

describe('CheckoutPanelComponent', () => {
  let component: CheckoutPanelComponent;
  let fixture: ComponentFixture<CheckoutPanelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CheckoutPanelComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CheckoutPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
