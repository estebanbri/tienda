import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {  Stripe } from '@stripe/stripe-js';
import { CheckoutService } from '../../../core/services/checkout.service';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'checkout-success',
  templateUrl: './checkout-success.component.html',
  styleUrls: ['./checkout-success.component.css'],
})
export class CheckoutSuccessComponent implements OnInit {
  paymentId: string = '';
  clientSecret: string = '';
  stripe!: Stripe;
  message: string = '';

  constructor(
    private route: ActivatedRoute,
    private checkoutService: CheckoutService,
    private titleService: Title) {
    this.titleService.setTitle('Checkout Exitoso | Tienda');
  }
  ngOnInit(): void {
    console.log("CheckoutSuccessComponent created");
    this.paymentId =
      this.route.snapshot.queryParamMap.get('payment_intent') ?? '';
    this.clientSecret =
      this.route.snapshot.queryParamMap.get('payment_intent_client_secret') ??
      '';
    this.handlePayment();
  }

  handlePayment() {
    this.checkoutService
      .retrievePaymentIntent(this.paymentId)
      .subscribe((paymentIntent) => {
        console.log(paymentIntent);
        switch (paymentIntent?.status) {
          case 'succeeded':
            this.message = 'Success! Payment received.';
            break;

          case 'processing':
            this.message =
              "Payment processing. We'll update you when payment is received.";
            break;

          case 'requires_payment_method':
            this.message = 'Payment failed. Please try another payment method.';
            // Redirect your user back to your payment page to attempt collecting
            // payment again
            break;

          default:
            this.message = 'Something went wrong.';
            break;
        }
      });
  }
}
