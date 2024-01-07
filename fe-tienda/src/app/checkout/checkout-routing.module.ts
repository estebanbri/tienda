import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginGuard } from '../core/guards/login.guard';
import { CheckoutSuccessComponent } from './components/checkout-success/checkout-success.component';
import { CheckoutComponent } from './pages/checkout-page/checkout.component';
import { NotFoundComponent } from '../core/components/not-found/not-found.component';

const routes: Routes = [
  { path: '', component: CheckoutComponent, pathMatch: 'full', canActivate: [LoginGuard] },
  { path: 'success', component: CheckoutSuccessComponent, canActivate: [LoginGuard] },
  { path: '**', component: NotFoundComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CheckoutRoutingModule { }
