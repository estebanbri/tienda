import { Component, OnInit, ChangeDetectionStrategy, Input } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { AuthService } from 'src/app/core/services/auth.service';

@Component({
  selector: 'app-signup-confirm',
  templateUrl: './signup-confirm.component.html',
  styleUrls: ['./signup-confirm.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SignupConfirmComponent implements OnInit {

  @Input() email: string;
  activationMessageSuccess: string;
  tokenFormGroup: FormGroup;
  verifyTokenDisabled = false;

  constructor(private authService: AuthService,
    private router: Router,
    private fb: FormBuilder) { }

  ngOnInit(): void {
    console.log("SignupConfirmComponent created");
    this.initializeForm();
  }

  initializeForm() {
    this.tokenFormGroup = this.fb.group({
      token: ['', [Validators.required, Validators.pattern(/^\d{4}$/)]]
    });
  }

  verifyToken() {
    this.tokenFormGroup.disable();
    this.verifyTokenDisabled = true;
    this.authService.activateAccount({token: this.token, subject: this.email}).pipe(
      //switchMap(() => this.userService.getCurrentLoggedUser()),
      catchError( error => {
        this.tokenFormGroup.enable();
        this.verifyTokenDisabled = false;
        alert('Token no valido');
      return throwError(() => new Error('Token no valido'))
    } ))
    .subscribe(() => {
      // TODO crear un mensajito que aparezca en una esquina 
      alert('Cuenta activada exitosamente, en un momento será redireccionado al login');
      this.router.navigate(['/auth/login']);
    });
  }

  get token() {
    return this.tokenFormGroup.controls['token'].value;
  }

}


