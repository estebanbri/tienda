import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { map, Observable } from 'rxjs';
import { AuthService } from 'src/app/core/services/auth.service';


@Injectable({
  providedIn: 'root'
})
export class LoginGuard implements CanActivate {
  
  constructor(private authService: AuthService, 
    private router: Router) {
  }

  canActivate(): Observable<boolean | UrlTree>   {
    return this.isUserLoggedIn();
  }
  
  private isUserLoggedIn(): Observable<boolean | UrlTree>  {
    return this.authService.loggedIn$.pipe(
      map(isLoggedIn => (isLoggedIn || this.router.parseUrl('/auth/login')),
    ));
  }

}
