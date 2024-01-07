import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap} from 'rxjs';
import { UpdateUserDTO, UserDTO } from '../models/user-dto';
import { USER_BASE, USER_ME } from '../constants/url-api-constants';

@Injectable({
  providedIn: 'root',
})
export class UserService {

  private _user: BehaviorSubject<UserDTO> = new BehaviorSubject<UserDTO>(null);
  user$: Observable<UserDTO> = this._user.asObservable();


  constructor(private http: HttpClient) {}

  getCurrentLoggedUser(): Observable<UserDTO>  {
    return  this.http.get<UserDTO>(USER_ME).pipe(
      tap((data) => this._user.next(data))
    );
  }

  updateUser(userId: number, updateUser: UpdateUserDTO): Observable<UserDTO> {
    return this.http.put<UserDTO>(`${USER_BASE}/${userId}`, updateUser).pipe(
      tap((data) => this._user.next(data))
    );
  }

}
