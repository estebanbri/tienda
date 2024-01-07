import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CartItem } from 'src/app/core/models/cart-item';

@Component({
  selector: 'app-cart-item',
  templateUrl: './cart-item.component.html',
  styleUrls: ['./cart-item.component.css'],
  
})
export class CartItemComponent implements OnInit {

  @Input() cartItem!: CartItem;

  @Output() cartItemDelete = new EventEmitter<void>();

  quantitiesList: any[] = [
    { value: 1, label: '1 unidad' },
    { value: 2, label: '2 unidades' },
    { value: 3, label: '3 unidades' },
    { value: 4, label: '4 unidades' },
    { value: 5, label: '5 unidades' },
    { value: 6, label: '6 unidades' }
  ];

  quantitySelected = this.quantitiesList[0];

  constructor() {}

  ngOnInit(): void {
    console.log("CartItemComponent created");
  }

  onQuantitySelected() {
    this.cartItem.price = this.cartItem.initialPrice * this.quantitySelected.value;
  }

  onDeleteClicked(): void {
    this.cartItemDelete.emit();
  }

}
