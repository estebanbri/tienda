import { NgModule, Optional, SkipSelf } from '@angular/core';
import { HTTP_INTERCEPTORS } from '@angular/common/http';

import { ApiInterceptor } from './interceptors/api.interceptor';
import { ErrorApiInterceptor } from './interceptors/error-api.interceptor';
import { throwIfAlreadyLoaded } from './utils/module-import-guard';

import { GoogleTagManagerModule } from 'angular-google-tag-manager';
import { ConfigService } from '../initializer/services/config.service';
import { SharedModule } from '../shared/shared.module';
import { CookieConsentComponent } from './components/cookie-consent/cookie-consent.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { SpinnerComponent } from './components/spinner/spinner.component';

@NgModule({
  declarations: [
    CookieConsentComponent,
    NavbarComponent,
    NotFoundComponent,
    SpinnerComponent
  ],
  imports: [
    GoogleTagManagerModule.forRoot(),
    SharedModule
  ],
  exports: [
    CookieConsentComponent,
    NavbarComponent,
    NotFoundComponent,
    SpinnerComponent
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: ApiInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: ErrorApiInterceptor, multi: true},
    {
      provide: 'googleTagManagerId',
      useFactory: (configService: ConfigService) => {
        return configService.config.gtmId;
      },
      deps: [ConfigService],
    }
  ]
})
export class CoreModule {
  constructor (@Optional() @SkipSelf() parentModule: CoreModule) {
    throwIfAlreadyLoaded(parentModule, 'CoreModule');
  }
}


