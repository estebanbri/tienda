import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { LoginData } from 'src/app/core/models/login-data';
import { AuthService } from 'src/app/core/services/auth.service';
import { TokenService } from 'src/app/core/services/token.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

 
  loginError = false;
  loginFormGroup: FormGroup;
 
  constructor(
    private authService: AuthService,
    private router: Router,
    private fb: FormBuilder,
    private titleService: Title,
    private tokenService: TokenService) {
    this.titleService.setTitle('Login | Tienda');
  }
  ngOnInit() {
    this.initializeForm();
    this.tokenService.cleanUpJwtTokens();
  }

  initializeForm() {
    this.loginFormGroup = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(4)]]
    });
  }

  logIn() {
    const dto = this.loginFormGroup.value as LoginData;
    this.authService.logIn(dto).pipe(
      catchError(error => {
      this.loginError = true;
      return throwError(() => new Error('Login error'))
    }))
    .subscribe(() => {

      /* this.appState.setAuthState({
        isAuthenticated: true,
        username: decodedJWT.sub,
        roles: decodedJWT.roles,
        token: {access_token}
      });
      */
      this.router.navigate(['/home']);
    });
  }

 
}


