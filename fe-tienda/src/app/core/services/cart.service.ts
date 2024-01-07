import { Injectable } from '@angular/core';
import { BehaviorSubject, catchError, map, Observable, throwError } from 'rxjs';
import { CartItem } from '../models/cart-item';
import { CartItemDTO } from '../models/cart-item-dto';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  private _cartItems = new BehaviorSubject<CartItem[]>([]);
  public cartItems$ = this._cartItems.asObservable();

  totalQuantity$ = this._cartItems.pipe(map( items => items.length));
  totalPrice$ = this._cartItems.pipe(map( items => items.reduce((acc, { price }) => (acc += price), 0)));

  constructor() {}

  addItem(itemCart: CartItem): void {
    this._cartItems.next([...this._cartItems.value, itemCart]);
  }

  deleteItem(itemIndex: number): void {
    this._cartItems.value.splice(itemIndex, 1);
    this._cartItems.next([...this._cartItems.value]);
  }
 
  getCartItemsByCatalogoId (id: number): Observable<CartItem | null> {
    return this._cartItems.pipe(map(items => items.find(item => item.catalogoItemId === id) || null));
  }

  countCartItemsByCatalogoId(catalogoId: number): Observable<number> {
    return this._cartItems.pipe(map( items => items.filter(item => item.catalogoItemId == catalogoId).length));
  }

  getCartItemsDTOs(): CartItemDTO[] { 
    return this._cartItems.value.map(({ catalogoItemId, quantity }) => ({ catalogoItemId, quantity }));
  }

  clear() {
    this._cartItems.next([]);
  }

  get amount$() {
    return this.totalPrice$.pipe(
      map(amount => amount / 100)
    );
  }

  get emptyCart$() {
    return this.totalQuantity$.pipe(
      map(qty => qty <= 0)
    );
  }
  

}
