import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CartPageComponent } from './pages/cart-page/cart-page.component';
import { NotFoundComponent } from '../core/components/not-found/not-found.component';

const routes: Routes = [
  { path: '', component: CartPageComponent, pathMatch: 'full' },
  { path: '**', component: NotFoundComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CartRoutingModule { }
