import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from 'src/app/core/services/auth.service';
import { UserDTO } from 'src/app/core/models/user-dto';
import { CartService } from 'src/app/core/services/cart.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  user$: Observable<UserDTO>;
  loggedIn$: Observable<boolean>; 

  constructor(
    private authService: AuthService,
    private cartService: CartService) { }

  ngOnInit() {
    console.log("NavbarComponent created");
    this.loggedIn$ = this.authService.loggedIn$;
  }

  logOut() {
    this.authService.logoutAndRedirectTo('/auth/logout');
  }

  get totalQuantity$() {
    return this.cartService.totalQuantity$;
  }

  get emptyCart$() {
    return this.cartService.emptyCart$;
  }

}

