import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PaymentIntent } from '@stripe/stripe-js';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { PaymentIntentDTO } from '../models/payment-intent-dto';
import { ConfirmPaymentIntentDTO } from '../models/confirm-payment-intent-dto';
import { STRIPE_PAYMENT_INTENTS_BASE } from '../constants/url-api-constants';

@Injectable({
  providedIn: 'root'
})
export class CheckoutService {

  constructor(private http: HttpClient) { }

  createPaymentIntent(paymentIntent: PaymentIntentDTO): Observable<PaymentIntent>  {
    return this.http.post<PaymentIntent>(STRIPE_PAYMENT_INTENTS_BASE, paymentIntent);
  }

  confirmPaymentIntent(intentId: string | undefined, confirmPaymentIntent: ConfirmPaymentIntentDTO): Observable<PaymentIntent> {
    return this.http.post<PaymentIntent>(`${STRIPE_PAYMENT_INTENTS_BASE}/${intentId}/confirm`, confirmPaymentIntent);
  }

  cancelPaymentIntent(intentId: string | undefined): Observable<PaymentIntent> {
    return this.http.post<PaymentIntent>(`${STRIPE_PAYMENT_INTENTS_BASE}/${intentId}/cancel`, {});
  }

  retrievePaymentIntent(intentId: string | undefined): Observable<PaymentIntent> {
    return this.http.get<PaymentIntent>(`${STRIPE_PAYMENT_INTENTS_BASE}/${intentId}`, {});
  }


}
