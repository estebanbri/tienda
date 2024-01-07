import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { switchMap, of, catchError, throwError } from 'rxjs';
import { AuthService } from 'src/app/core/services/auth.service';
import { UserDTO, UpdateUserDTO } from 'src/app/core/models/user-dto';
import { UserService } from 'src/app/core/services/user.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {

  saveUserDisabled = true;
  nonEditableFormGroup: FormGroup;
  editableFormGroup: FormGroup;
  userDTO: UserDTO;
  updateUserDTO: UpdateUserDTO;
  isEditing = false;
  editingMessage= 'Modificar';

  constructor(private fb: FormBuilder,
    private userService: UserService,
    private authService: AuthService,
    private router: Router) { }

  ngOnInit(): void {
    console.log("UserProfileComponent created");
    this.initializeForms();
    this.disableForms();
    this.setDefaultsValuesToForm();
  }

  initializeForms() {
    this.nonEditableFormGroup = this.fb.group({
      accountStatus: ['', [Validators.required]],
      username: ['', [Validators.required]],
      role: ['', [Validators.required]], 
      email: ['', [Validators.required, Validators.email]]
    });
    this.editableFormGroup = this.fb.group({
      firstName: ['', [Validators.required]], 
      lastName: ['', [Validators.required]], 
      country: ['', [Validators.required]]
    });
  }

  // Puedes usar este método en tu template para deshabilitar o habilitar el formulario.
  disableForms() {
    this.nonEditableFormGroup.disable();
    this.editableFormGroup.disable();
  }

  setDefaultsValuesToForm() {
    this.userService.user$.pipe(
    switchMap((user: UserDTO) => user == null ? this.userService.getCurrentLoggedUser() : of(user)))
    .subscribe((data: UserDTO) => {
      this.userDTO = data;
      this.nonEditableFormGroup.setValue({
        accountStatus: data.accountActivated ? 'Activada' : 'Pendiente de activación',
        username: data.username,
        role: data.role,
        email: data.email,
      });
      this.editableFormGroup.setValue({
        firstName: data.firstName, 
        lastName: data.lastName, 
        country: data.country, 
      });
    });
  }

  // TODO algo asi
  onEditDatosPersonalesForm() {
    if(!this.isEditing) {
      this.editableFormGroup.enable();
      this.editingMessage = 'Cancelar';
      this.saveUserDisabled = false;
    } else {
      this.editableFormGroup.disable();
      this.editingMessage = 'Modificar';
    }
    this.isEditing = !this.isEditing;
  }

  public onChangePassword() {
    this.userService.user$
    .subscribe(user => {
      this.authService.sendPasswordChangeRequest({subject: user.email})
        .pipe(catchError(error => {
          return throwError(() => new Error('Error sending email'))
        }))
        .subscribe(res => this.router.navigate(['/auth/forgot']));
    })
  }

  saveUser() {
    const editableForm = this.editableFormGroup.value as UpdateUserDTO;
    //this.copyProperties(userForm, this.updateUserDTO);
    this.updateUserDTO = {...editableForm};
    this.userService.updateUser(this.userDTO.id, this.updateUserDTO)
    .subscribe(() => {
      console.log('User saved...');
      this.router.navigate(['/home']);
    });
  }

  copyProperties(source: any, dest: any) {
    // Copiar los valores del primer objeto al segundo
    for (const key in source) {
      if (source.hasOwnProperty(key)) {
        dest[key] = source[key];
      }
    }
  }

}

