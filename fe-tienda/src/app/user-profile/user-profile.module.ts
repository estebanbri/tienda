import { NgModule } from '@angular/core';

import { UserProfileRoutingModule } from './user-profile-routing.module';
import { UserProfileComponent } from './pages/user-profile/user-profile.component';
import { SharedModule } from '../shared/shared.module';


@NgModule({
  declarations: [
    UserProfileComponent
  ],
  imports: [
    UserProfileRoutingModule,
    SharedModule
  ]
})
export class UserProfileModule { }
