import { NgModule } from '@angular/core';

import { HomeRoutingModule } from './home-routing.module';
import { HomePageComponent } from './pages/home-page/home-page.component';
import { CatalogComponent } from './components/catalog/catalog.component';
import { CatalogItemComponent } from './components/catalog-item/catalog-item.component';
import { SharedModule } from '../shared/shared.module';


@NgModule({
  declarations: [
    HomePageComponent,
    CatalogComponent,
    CatalogItemComponent
  ],
  imports: [
    HomeRoutingModule,
    SharedModule
  ]
})
export class HomeModule { }
