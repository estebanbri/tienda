import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormGroup, FormBuilder, Validators, AbstractControl } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { Observable, Subscription, catchError, throwError } from 'rxjs';
import { AuthService } from 'src/app/core/services/auth.service';
import { TokenService } from 'src/app/core/services/token.service';

@Component({
  selector: 'app-forgot',
  templateUrl: './forgot.component.html',
  styleUrls: ['./forgot.component.css']
})
export class ForgotComponent implements OnInit, OnDestroy {

  passwordChangeRequestGroup: FormGroup;
  passwordChangePendingTokenValidation = true;
  passwordChangeDisabled = false;
  emailNotFoundMessage: string;
  loggedInSub: Subscription;
  loggedIn$: Observable<boolean>;

  constructor(private fb: FormBuilder,
    private authService: AuthService,
    private titleService: Title,
    private tokenService: TokenService) {
    this.titleService.setTitle('Cambio password | Tienda');
  }

  ngOnInit(): void {
    console.log("PasswordChangeIntentComponent created");
    this.loggedIn$ = this.authService.loggedIn$;
    this.initializeForm();
  }

  initializeForm() {
    this.passwordChangeRequestGroup = this.fb.group({
      email: ['', [Validators.required, Validators.email, this.emailNotFoundValidator]],
    });
  }

  emailNotFoundValidator = (control: AbstractControl): {[key: string]: any} | null => {
    return this.emailNotFoundMessage != null ? null : {passwordNotMatch: true};
  };
  sendPasswordChangeRequest() {
    this.loggedInSub = this.loggedIn$.subscribe(
      loggedIn => {
        if(!loggedIn) this.tokenService.cleanUpJwtTokens(); // forzamos una limpieza de los tokens en caso de que login retorne false
      } 
    );
    this.passwordChangeDisabled = true;
    this.passwordChangeRequestGroup.disable();
    const actionRequest = { subject: this.email }
    return this.authService.sendPasswordChangeRequest(actionRequest)
    .pipe(catchError(error => {
      this.passwordChangeDisabled = false;
      this.passwordChangeRequestGroup.enable();
      this.emailNotFoundMessage = 'Email no valido';
      console.log(this.emailNotFoundMessage);
      return throwError(() => new Error(this.emailNotFoundMessage))
    }))
    .subscribe(() => {
      this.passwordChangeDisabled = true;
      this.passwordChangePendingTokenValidation = false;
    });
  }

  get email() {
    return this.passwordChangeRequestGroup.controls['email'].value;
  }

  ngOnDestroy(): void {
    this.loggedInSub?.unsubscribe();
  }
}

