import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomePageComponent } from './pages/home-page/home-page.component';
import { NotFoundComponent } from '../core/components/not-found/not-found.component';

const routes: Routes = [
  { path: '', component: HomePageComponent, pathMatch:"full" },
  { path: '**', component: NotFoundComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HomeRoutingModule { }