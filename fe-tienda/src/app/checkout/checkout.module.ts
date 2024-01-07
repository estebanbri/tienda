import { NgModule } from '@angular/core';

import { CheckoutRoutingModule } from './checkout-routing.module';
import { SharedModule } from '../shared/shared.module';

import { CheckoutComponent } from './pages/checkout-page/checkout.component';
import { CheckoutSuccessComponent } from './components/checkout-success/checkout-success.component';
import { ErrorPaymentModalComponent } from './components/error-payment-modal/error-payment-modal.component';


@NgModule({
  declarations: [
    CheckoutComponent,
    CheckoutSuccessComponent,
    ErrorPaymentModalComponent
  ],
  imports: [
    CheckoutRoutingModule,
    SharedModule
  ]
})
export class CheckoutModule { }
