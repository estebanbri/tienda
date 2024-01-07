import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule, HttpClientXsrfModule } from '@angular/common/http';
import { SpinnerInterceptor } from '../core/interceptors/spinner.interceptor';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MaterialModule } from './material.module';

// components
import { SearchBarComponent } from './components/search-bar/search-bar.component';


@NgModule({
  declarations: [
    SearchBarComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MaterialModule
  ],
  exports: [
    HttpClientXsrfModule,
    HttpClientModule,
    SearchBarComponent,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    MaterialModule,
    CommonModule,
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: SpinnerInterceptor, multi: true}
  ]
})
export class SharedModule { }
