import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import {  PaymentIntent, Stripe, StripeElements, StripeElementsOptions, StripePaymentElementOptions, loadStripe } from '@stripe/stripe-js';
import { CheckoutService } from '../../../core/services/checkout.service';
import { EMPTY, Observable, Subscription, catchError, from, map, of, switchMap, tap } from 'rxjs';
import { CartService } from '../../../core/services/cart.service';
import { environment } from 'src/environments/environment';
import {MatDialog} from '@angular/material/dialog';
import { ErrorPaymentModalComponent } from '../../components/error-payment-modal/error-payment-modal.component';
import { Router } from '@angular/router';
import { CustomerService } from '../../../core/services/customer.service';
import { Title } from '@angular/platform-browser';
import { ConfigService } from 'src/app/initializer/services/config.service';

/** 
 *  1. Crear payment intent
 *  2. Confirmar payment intent
 */
@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit, OnDestroy {
  
  paymentIntentID: string = '';
  loading = false;
  stripe!: Stripe | null;
  elements!: StripeElements;
  @ViewChild('paymentElement') paymentElementRef!: ElementRef;
  @ViewChild('addressElement') addressElementRef!: ElementRef;
  loadCustomerSubscription: Subscription;
  payDisabled = true;
  goToPayDisabled = true;
  customerId!: string;
  customerName!: string;
  paymentMethodSelected!: string;

  constructor(
    private checkoutService: CheckoutService,
    private cartService: CartService,
    private dialog: MatDialog,
    private router: Router,
    private customerService: CustomerService,
    private titleService: Title,
    private configService: ConfigService) {
    this.titleService.setTitle('Checkout | Tienda');
  }

  ngOnInit() {
    console.log("CheckoutComponent created");
    //  1. Crear payment intent
    this.loadCustomerSubscription = this.loadCustomerId()
      .pipe(
        tap((customerId) => this.customerId = customerId),
        switchMap((customerId) => this.createPaymentIntent(customerId)),
        tap((intent) => console.log('PaymentIntent created:', intent)),
        switchMap((intent) => from(this.setupElements(intent)))
      )
      .subscribe(() => console.log('Elements created'));
  }

  async setupElements(intent: PaymentIntent) {
    this.stripe = await loadStripe(this.config.stripeKey);

    if (!this.stripe) {
      console.error('Failed to load stripe');
      return;
    }

    this.paymentIntentID = intent.id;

    const options: StripeElementsOptions = {
      appearance: {
        theme: 'stripe',
      },
      clientSecret: intent.client_secret ?? ''
    };

    this.elements = this.stripe.elements(options);
    this.setupPaymentElement();
    this.setupAddressElement();
  }

  private setupAddressElement() {
    // Create and mount the Address Element in shipping mode
    const addressElement: any = this.elements.create('address', {
      mode: 'shipping', // shipping mode: recolecta direccion de shipping y tambien le ofrece al cliente la opcion de usarlo como direccion de billing (billing mode solamente esta ultima opcion) 
      display: { name: 'split' }
    });
    addressElement.mount(this.addressElementRef.nativeElement);
    this.handleInputFromAddressElement(addressElement);
  }

  private setupPaymentElement() {
    const paymentElementOptions: StripePaymentElementOptions = {
      layout: 'tabs',
    };
    const paymentElement: any = this.elements.create('payment', paymentElementOptions);
    paymentElement.mount(this.paymentElementRef.nativeElement);
    this.handleInputFromPaymentElement(paymentElement);
  }

  private handleInputFromPaymentElement(paymentElement: any) {
    // Handle real-time validation errors from elements
    paymentElement.addEventListener('change', (event: any) => {
    if (event.complete) {
      this.paymentMethodSelected = event.value.type;
      console.log("Payment method selected:", this.paymentMethodSelected);
    }
      this.payDisabled = !event.complete;
    });
  }

  private handleInputFromAddressElement(addressElement: any) {
    addressElement.addEventListener('change', (event: any) => {
      if (event.complete && event.value.address.state) {
        //const address = event.value.address;
        this.customerName = event.value.name;
        this.goToPayDisabled = !event.complete;
      }
    });
  }

  loadCustomerId() {
    return this.customerService.searchCustomerByEmail('estebanbriceno1989@gmail.com') // TODO pasar email
    .pipe(
      map((customer: any) => customer?.id),
      switchMap((customerId: string) => {
        return customerId ? of(customerId) : this.saveCustomer();
      })
    );
  }

  saveCustomer() {
    return this.customerService.createCustomer({description: 'Creacion inicial desde desde app', email: 'estebanbriceno1989@gmail.com'}) // TODO pasar email
    .pipe((map (
      (customer: { id: any; }) =>  {
        console.log("New customer created");
        return customer?.id;
      }
    )));
  }

  updateCustomerData() {
    this.customerService.updateCustomer(this.customerId, {
        name: this.customerName
    }).subscribe( customer =>  console.log("Customer updated"));
  }
  
  async pay() {
    this.loading = true;
    //  2. Confirm payment intent AKA pay
    const result = await this.stripe?.confirmPayment({
      elements: this.elements,
      confirmParams: {
        return_url: `${window.location.origin}/success`,
        receipt_email: 'estebanbriceno1989@gmail.com' // TODO pasar email
      },
    })
    if(result?.error) {
      this. handlePaymentError(result?.error);
    } else {
      console.log("Payment confirmed", result);
      this.loading = false;
    }
  }

  cancelPaymentIntent(): void {
    this.checkoutService.cancelPaymentIntent(this.paymentIntentID)
    .subscribe( paymentIntent => {
      this.router.navigate(['/cart']);
    });
  }

  private handlePaymentError(error: any): Observable<never> {
    console.error('An unexpected error occurred.', error);
    this.dialog.open(ErrorPaymentModalComponent, {
      data: { message: error.message },
    });
    this.loading = false;
    return EMPTY
  }

  private createPaymentIntent(customerId: string): Observable<PaymentIntent> {
    // automatic_payment_methods por defecto esta activado y con esto trae todos los metodos de pagos que se encuentran activos en tu dashboard
    console.log('this.customerId',customerId);
    return this.checkoutService
      .createPaymentIntent({
        cartDTO: { cartItemDTOs: this.cartService.getCartItemsDTOs() },
        description: 'Payment for Cart #1',
        currency: 'eur',
        customer: customerId
      })
      .pipe(
        catchError((error) => {
          console.error('Error ocurred while payment intent creation:', error);
          this.router.navigate(['/cart']);
          return EMPTY;
        })
      );
  }

  get amount$() {
    return this.cartService.amount$;
  }

  get config()  {
    return this.configService.config;
  }

  ngOnDestroy() {
    this.loadCustomerSubscription?.unsubscribe();
  }
}

