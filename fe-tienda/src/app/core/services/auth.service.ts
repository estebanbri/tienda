import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {
  BehaviorSubject,
  EMPTY,
  Observable,
  catchError,
  of,
  tap,
  throwError
} from 'rxjs';
import { LoginData, SignUpData } from '../models/login-data';
import { CartService } from './cart.service';
import { TokenService } from './token.service';
import {
  ActionRequestedByDTO,
  ActionRequestedByWithTokenDTO,
} from '../models/action-requested-dto';
import { PasswordChangeConfirmDTO } from '../models/password-change-confirm-dto';
import { JwtPairDTO } from '../models/jwt-pair-dto';
import { Router } from '@angular/router';
import { AUTH_ACTIVATE_ACCOUNT_ME,  AUTH_LOGIN,  AUTH_LOGIN_WITH_BODY_ONLY, AUTH_LOGIN_WITH_COOKIE_ONLY, AUTH_LOGOUT, AUTH_PASSWORD_CHANGE_CONFIRM, AUTH_PASSWORD_CHANGE_INTENT, AUTH_PASSWORD_CHANGE_VALIDATE_TOKEN, AUTH_REFRESH_TOKEN, AUTH_REFRESH_TOKEN_WITH_BODY, AUTH_REFRESH_TOKEN_WITH_COOKIE, AUTH_SIGNUP, AUTH_VERIFY_CAPTCHA_TOKEN } from '../constants/url-api-constants';
import { CookieConsentService } from 'src/app/core/components/cookie-consent/cookie-consent.service';
import { JwtDTO } from '../models/jwt-dto';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private _loggedIn = new BehaviorSubject<boolean>(false);
  public loggedIn$ = this._loggedIn.asObservable();

  constructor(
    private http: HttpClient,
    private cartService: CartService,
    private tokenService: TokenService,
    private router: Router,
    private cookieConsentService: CookieConsentService) {}

  logIn(loginData: LoginData): Observable<JwtDTO> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded',
    });
    let params = new HttpParams()
      .set('username', loginData.username)
      .set('password', loginData.password);
    ; 
    return this.http
    .post<JwtDTO>(
      AUTH_LOGIN, params, { headers }).pipe(
      tap((data: JwtDTO) => {
        this.tokenService.setAccessToken(data);
        this._loggedIn.next(true);
      })
    );
  }

  /**
  * @deprecated Este metodo ya no se deberia usar solo queda para mantener el ejemplo 
  */
  private loginWithBody(headers: HttpHeaders, params: HttpParams): Observable<JwtPairDTO> {
    return this.http
    .post<JwtPairDTO>(
      AUTH_LOGIN_WITH_BODY_ONLY, params, { headers }).pipe(
      tap((response: JwtPairDTO) => {
        this.tokenService.setAccessTokenToStorage(response.access_token);
        this.tokenService.setRefreshTokenToStorage(response.refresh_token);
        this._loggedIn.next(true);
      })
    );
  }

  /**
  * @deprecated  Este metodo ya no se deberia usar solo queda para mantener el ejemplo 
  */
  private loginWithCookie(headers: HttpHeaders, params: HttpParams): Observable<void> {
    return this.http
    .post<void>(AUTH_LOGIN_WITH_COOKIE_ONLY, params, { headers }).pipe(
      tap(() => {
        this._loggedIn.next(true);
      })
    );
  }

  signUp(dto: SignUpData): Observable<void> {
    return this.http.post<void>(AUTH_SIGNUP, dto);
  }

  activateAccount(dto: ActionRequestedByWithTokenDTO): Observable<void> {
    return this.http.post<void>(AUTH_ACTIVATE_ACCOUNT_ME, dto);
  }

  sendPasswordChangeRequest(dto: ActionRequestedByDTO): Observable<void> {
    return this.http.post<void>(AUTH_PASSWORD_CHANGE_INTENT, dto);
  }

  sendPasswordChangeRequestTokenValidation(
    dto: ActionRequestedByWithTokenDTO
  ): Observable<void> {
    dto.subject = dto.subject ?? this.tokenService.getSubject();
    return this.http.post<void>( AUTH_PASSWORD_CHANGE_VALIDATE_TOKEN, dto);
  }

  sendPasswordChangeConfirmation(
    dto: PasswordChangeConfirmDTO
  ): Observable<void> {
    dto.subject = dto.subject ?? this.tokenService.getSubject();
    return this.http.post<void>(
      AUTH_PASSWORD_CHANGE_CONFIRM,
      dto
    );
  }

  logoutAndRedirectTo(to: string): void {
      this.cartService.clear();
      this.http
        .post<void>(AUTH_LOGOUT, {}).pipe(
          catchError(error => {
            this.tokenService.cleanUpJwtTokens();
            this.router.navigate(['/auth/login']);
            this._loggedIn.next(false);
            return throwError( () => error);
          })
        )
        .subscribe(() => {
          this.tokenService.cleanUpJwtTokens();
          this._loggedIn.next(false);
          console.log('logged Out success');
          this.router.navigate([to]);
        });
  }

  executeTokenRefresh(): Observable<JwtDTO> {
    this.tokenService.setAccessToken(null);
    return this.http.get<JwtDTO>(AUTH_REFRESH_TOKEN).pipe(
      catchError(error => {
        console.error("Unable to refresh the access token, please check if the refresh token exists.");
        return of(null);
      }),
      tap((data: JwtDTO) => {
        if(data){
          // update state
          console.log('Successfully retrieved a fresh access token.')
          this.tokenService.setAccessToken(data);
          this._loggedIn.next(true);
        }
      }
    ));
  }

  generateRefreshTokenWithBody() {
    // return this._httpClient.get<IResponseRefreshToken>(URL_AUTH_REFRESH).pipe(delay(5000)); // Para simular una demora de la respuesta del servicio de obtencion refresco de tokens usa esta linea, valida en los logs tendrias que ver si le das dos veces acceder a un recurso protegido el segundo request se debe cancelar y lo veras en el log
    return this.http.get<JwtPairDTO>(AUTH_REFRESH_TOKEN_WITH_BODY);
  }

  generateRefreshTokenWithCookie() {
    // return this._httpClient.get<IResponseRefreshToken>(URL_AUTH_REFRESH).pipe(delay(5000)); // Para simular una demora de la respuesta del servicio de obtencion refresco de tokens usa esta linea, valida en los logs tendrias que ver si le das dos veces acceder a un recurso protegido el segundo request se debe cancelar y lo veras en el log
    return this.http.get<JwtPairDTO>(AUTH_REFRESH_TOKEN_WITH_COOKIE);
  }

  verifyCaptcha(token: any) : Observable<boolean> {
    return this.http
    .post<boolean>(AUTH_VERIFY_CAPTCHA_TOKEN, token);
  }

  private jwtPairExistsOnStorage() {
    return !this.cookieConsentService.cookieContentAccepted && this.tokenService.checkIfJwtPairExistOnStorage();
  }

  
}
