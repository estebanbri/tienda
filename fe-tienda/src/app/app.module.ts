import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppComponent } from './app.component';

import { SharedModule } from './shared/shared.module';
import { CoreModule } from './core/core.module';
import { InitializerModule } from './initializer/initializer.module';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [ // EAGER LOAD MODULES
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    InitializerModule, // Inicializador de config asincronca (async via http call to be)
    SharedModule,
    CoreModule, // Unicamente el AppModule debe tener importado el CoreModule porque debe ser tratado como un singleton, todo el estado de tu app pertenece a CoreModule
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
