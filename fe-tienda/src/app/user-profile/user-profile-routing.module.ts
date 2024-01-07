import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserProfileComponent } from './pages/user-profile/user-profile.component';
import { LoginGuard } from '../core/guards/login.guard';
import { NotFoundComponent } from '../core/components/not-found/not-found.component';

const routes: Routes = [
  { path: '', component: UserProfileComponent, pathMatch: 'full', canActivate: [LoginGuard] },
  { path: '**', component: NotFoundComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserProfileRoutingModule { }
