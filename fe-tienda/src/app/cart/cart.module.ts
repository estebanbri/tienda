import { NgModule } from '@angular/core';

import { CartRoutingModule } from './cart-routing.module';
import { CartPageComponent } from './pages/cart-page/cart-page.component';
import { CartItemComponent } from './components/cart-item/cart-item.component';
import { SharedModule } from '../shared/shared.module';


@NgModule({
  declarations: [
    CartPageComponent,
    CartItemComponent
  ],
  imports: [
    CartRoutingModule,
    SharedModule
  ]
})
export class CartModule { }
