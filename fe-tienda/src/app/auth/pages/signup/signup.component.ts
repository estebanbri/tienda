import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, AbstractControl } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { catchError, throwError } from 'rxjs';
import { SignUpData } from 'src/app/core/models/login-data';
import { AuthService } from 'src/app/core/services/auth.service';
import { TokenService } from 'src/app/core/services/token.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  signingUp = false;
  signingUpDisabled = false;
  signupPendingConfirmation = false;
  signUpFormGroup: FormGroup;
  captchaResolved = false;
  captchaValid: boolean | undefined ;

  constructor(private authService: AuthService,
    private fb: FormBuilder,
    private titleService: Title,
    private tokenService: TokenService) {
    this.titleService.setTitle('Registro | Tienda');
    this.captchaValid = undefined;
  }

  ngOnInit(): void {
    console.log("SignupComponent created");
    this.tokenService.cleanUpJwtTokens();
    this.initializeForm();
  }

  initializeForm() {
    this.signUpFormGroup = this.fb.group({
      username: ['', [Validators.required]], 
      password: ['', [Validators.required, Validators.minLength(4)]],
      password_confirmation: ['', [Validators.required, this.passwordMatchValidator]],
      firstName: ['', [Validators.required]], 
      lastName: ['', [Validators.required]], 
      country: ['', [Validators.required]], 
      email: ['', [Validators.required, Validators.email]],
      recaptchaToken: ['', [Validators.required, this.captchaValidator]]
    });
  }

  passwordMatchValidator = (control: AbstractControl): {[key: string]: any} | null => {
    const password = this.signUpFormGroup?.get('password')?.value as string;
    const passwordConfirm = control.value as string;
    return password === passwordConfirm ? null : {passwordNotMatch: true};
  };

  captchaValidator = (control: AbstractControl): {[key: string]: any} | null => {
    //const recaptchaToken = this.signUpFormGroup?.get('recaptchaToken')?.value as string;
    //const passwordConfirm = control.value as string;
    return this.captchaValid === false ? {captchaInvalid: true} : null;
  };
  
  sendSignUpRequest() {
    this.signingUpDisabled = true;
    this.signUpFormGroup.disable();
    this.signUpFormGroup.removeControl('password_confirmation');
    const dto = this.signUpFormGroup.value as SignUpData;
    this.authService.signUp(dto)
    .pipe(catchError( error => {
      console.log(error);
      this.signingUpDisabled = false;
      this.signUpFormGroup.enable();
      return throwError(() => new Error('Signup error'))
    }))
    .subscribe(() => {
        this.signupPendingConfirmation = true;
    });
  }

  resolved(token: any) {
    // token generado en base al puzzle que resolvio el usuario debe ser enviado al backend para validar con el server de google recaptcha
    this.authService.verifyCaptcha(token).pipe(
      catchError( error => {
        console.error(error);
        return throwError(() => new Error('Captcha token invalid'));
      })
    ).subscribe( result => {
      console.log(`Captcha result (valid=${result})`);
      this.captchaValid = result;
    });
  }

  // handles issues like connection issues. It only shows an error if errorMode is set to be ‘handled’.
  onError(log: any) {
      console.log('error ', log);
  }

  get email() {
    return this.signUpFormGroup.controls['email'].value
  }

  get recaptchaToken() {
    return this.signUpFormGroup.controls['recaptchaToken']
  }
}

