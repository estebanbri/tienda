import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { CartService } from 'src/app/core/services/cart.service';

@Component({
  selector: 'app-cart-page',
  templateUrl: './cart-page.component.html',
  styleUrls: ['./cart-page.component.css']
})
export class CartPageComponent implements OnInit {

  constructor(private cartService: CartService,
    private titleService: Title) {
      this.titleService.setTitle('Carrito | Tienda');
  }

  ngOnInit(): void {
    console.log("CartComponent created");
  }

  deleteItem(itemIndex: number): void {
    this.cartService.deleteItem(itemIndex);
  }

  
  clearCart() {
    return this.cartService.clear();
  }

  get amount$() {
    return this.cartService.amount$;
  }

  get cartItems$() {
    return this.cartService.cartItems$;
  }

  get emptyCart$() {
    return this.cartService.emptyCart$;
  }

}

