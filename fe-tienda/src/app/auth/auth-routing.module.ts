import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { NotFoundComponent } from '../core/components/not-found/not-found.component';
import { ForgotConfirmComponent } from './components/forgot-confirm/forgot-confirm.component';
import { ForgotComponent } from './pages/forgot/forgot.component';
import { SignupComponent } from './pages/signup/signup.component';
import { LogoutComponent } from './pages/logout/logout.component';

// ,
        // children: [ 
        //   { path: 'confirm', component: ForgotConfirmComponent} 
        // ] 

const routes: Routes = [
  {
    path: '',
    children: [
      { path: 'login', component: LoginComponent },
      { path: 'logout', component: LogoutComponent },
      { path: 'signup', component: SignupComponent },
      { path: 'forgot', component: ForgotComponent },
      { path: '**', component: NotFoundComponent },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthRoutingModule { }
