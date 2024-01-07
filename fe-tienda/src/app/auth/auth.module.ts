
import { NgModule } from '@angular/core';

import { AuthRoutingModule } from './auth-routing.module';

// third party
import { RecaptchaModule, RecaptchaFormsModule, RecaptchaSettings, RECAPTCHA_SETTINGS } from 'ng-recaptcha';

// componentes
import { LoginComponent } from './pages/login/login.component';
import { ForgotComponent } from './pages/forgot/forgot.component';
import { LogoutComponent } from './pages/logout/logout.component';
import { SignupComponent } from './pages/signup/signup.component';
import { ForgotConfirmComponent } from './components/forgot-confirm/forgot-confirm.component';
import { SignupConfirmComponent } from './components/signup-confirm/signup-confirm.component';

import { SharedModule } from '../shared/shared.module';
import { ConfigService } from '../initializer/services/config.service';

@NgModule({
  declarations: [
    LoginComponent,
    ForgotComponent,
    LogoutComponent,
    ForgotConfirmComponent,
    SignupConfirmComponent,
    SignupComponent
  ],
  imports: [
    AuthRoutingModule,
    RecaptchaModule,
    RecaptchaFormsModule,
    SharedModule
  ], 
  providers: [
    {
      provide: RECAPTCHA_SETTINGS,
      useFactory: (configService: ConfigService): RecaptchaSettings => {
        return { siteKey: configService.config.recaptchaKey };
      },
      deps: [ConfigService],
    }
  ]
})
export class AuthModule { }