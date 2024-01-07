import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginGuard } from './core/guards/login.guard';

const routes: Routes = [
  // modules lazy load
  { path: 'home', loadChildren: () => import('./home/home.module').then( m => m.HomeModule) },
  { path: 'cart', loadChildren: () => import('./cart/cart.module').then( m => m.CartModule) },
  { path: 'auth', loadChildren: () => import('./auth/auth.module').then( m => m.AuthModule) },
  { path: 'user-profile', loadChildren: () => import('./user-profile/user-profile.module').then( m => m.UserProfileModule), canActivate: [LoginGuard] },
  { path: 'checkout', loadChildren: () => import('./checkout/checkout.module').then( m => m.CheckoutModule), canActivate: [LoginGuard] },
  { path: '**', redirectTo: 'home' }
];


@NgModule({
  imports: [RouterModule.forRoot(routes
    // , { enableTracing: true } //enable this to debug
    )],
  exports: [RouterModule]
})
export class AppRoutingModule { }
