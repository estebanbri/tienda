import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ForgotConfirmComponent } from './forgot-confirm.component';

describe('ForgotConfirmComponent', () => {
  let component: ForgotConfirmComponent;
  let fixture: ComponentFixture<ForgotConfirmComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ForgotConfirmComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ForgotConfirmComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
