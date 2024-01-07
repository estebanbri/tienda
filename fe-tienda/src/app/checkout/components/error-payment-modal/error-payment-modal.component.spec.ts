import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ErrorPaymentModalComponent } from './error-payment-modal.component';

describe('ErrorPaymentModalComponent', () => {
  let component: ErrorPaymentModalComponent;
  let fixture: ComponentFixture<ErrorPaymentModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ErrorPaymentModalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ErrorPaymentModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
