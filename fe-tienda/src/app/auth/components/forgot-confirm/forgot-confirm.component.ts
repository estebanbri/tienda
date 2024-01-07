import { Component, OnInit, ChangeDetectionStrategy, ChangeDetectorRef, Input, OnChanges, OnDestroy, SimpleChanges } from '@angular/core';
import { FormGroup, FormBuilder, Validators, AbstractControl } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { catchError, throwError, finalize, tap, Observable, Subscription } from 'rxjs';
import { UserDTO } from 'src/app/core/models/user-dto';
import { AuthService } from 'src/app/core/services/auth.service';
import { UserService } from 'src/app/core/services/user.service';

@Component({
  selector: 'app-forgot-confirm',
  templateUrl: './forgot-confirm.component.html',
  styleUrls: ['./forgot-confirm.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ForgotConfirmComponent implements OnChanges, OnInit, OnDestroy {

  @Input() email: string;

  passwordChangeConfirmGroup: FormGroup;
  tokenFormGroup: FormGroup;
  tokenValidationSuccess = false;
  verifyTokenDisabled = false;
  isLogged = false;
  sendPasswordConfirmDisabled = false;
  user: UserDTO;
  user$: Observable<UserDTO>
  userSub: Subscription;

  constructor(private fb: FormBuilder,
    private authService: AuthService,
    private cdr: ChangeDetectorRef,
    private titleService: Title,
    private userService: UserService) {
    this.titleService.setTitle('Cambio Password Exitoso | Tienda');
  }

  ngOnInit(): void {
    console.log("PasswordChangeConfirmComponent created");
    this.initializeForms();
    this.userSub = this.userService.user$.subscribe(user => {
        this.user = user;
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    for (let propName in changes) {
      let chng = changes[propName];
      console.log(`El valor @Input email ha cambiado desde el componente padre, valor previo: ${chng.previousValue} se ha actualizado a ${chng.currentValue}`);
    }
  }

  initializeForms() {
    this.tokenFormGroup = this.fb.group({
      token: ['', [Validators.required, Validators.pattern(/^\d{4}$/)]]
    });
    this.passwordChangeConfirmGroup = this.fb.group({
      password: ['', [Validators.required]],
      password_confirmation: ['', [Validators.required, this.passwordMatchValidator]],
    });
  }

  sendPasswordChangeTokenValidation() {
    this.verifyTokenDisabled = true;
    this.authService.sendPasswordChangeRequestTokenValidation({subject: this.getEmail(), token: this.token})
    .pipe(catchError(error => {
      this.verifyTokenDisabled = false;
      alert('Token no valido');
      return throwError(() => new Error('Token no valido'))
    }))
    .subscribe(() => {
      this.tokenValidationSuccess = true;
      this.cdr.markForCheck();
    });
  }

  sendPasswordChangeConfirm() {
    this.sendPasswordConfirmDisabled = true;
    let password = this.password;
    this.authService.sendPasswordChangeConfirmation({
      subject: this.getEmail(),
      password,
      token: this.token
    }).pipe(
      catchError(error => throwError(() => new Error('Confirm password error'))),
      finalize(() => this.sendPasswordConfirmDisabled = false),
      tap(() => this.authService.logoutAndRedirectTo('/auth/login')))
      .subscribe();
  }

  private passwordMatchValidator = (control: AbstractControl): {[key: string]: any} | null => {
    const password = this.passwordChangeConfirmGroup?.get('password')?.value as string;
    const passwordConfirm = control.value as string;
    return password === passwordConfirm ? null : {passwordNotMatch: true};
  };   

  get password() {
    return this.passwordChangeConfirmGroup.controls['password'].value
  }

  get token() {
    return this.tokenFormGroup.controls['token'].value
  }

  private getEmail() {
    return this.user ? this.user.email : this.email;
  }

  ngOnDestroy(): void {
    console.log("PasswordChangeConfirmComponent ngOnDestroy");
    this.userSub.unsubscribe();
  }

}



