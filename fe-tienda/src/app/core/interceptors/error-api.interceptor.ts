import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpInterceptor,
  HttpErrorResponse,
  HttpStatusCode,
} from '@angular/common/http';
import {
  EMPTY,
  Observable,
  catchError,
  switchMap,
  throwError,
} from 'rxjs';
import { AuthService } from '../services/auth.service';
import { TokenService } from '../services/token.service';
import { CookieConsentService } from 'src/app/core/components/cookie-consent/cookie-consent.service';
import { JwtPairDTO } from '../models/jwt-pair-dto';
import { JwtDTO } from '../models/jwt-dto';

@Injectable({
  providedIn: 'root',
})
export class ErrorApiInterceptor implements HttpInterceptor {
  constructor(
    private authService: AuthService,
    private tokenService: TokenService,
    private cookieConsentService: CookieConsentService
  ) {}

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    return next.handle(req).pipe(
      catchError((error: any) => {
        
          if (!req.url.includes('/login')
            && error instanceof HttpErrorResponse && error.status === HttpStatusCode.Unauthorized
            && !this.tokenService.isRefreshingToken) {
            // Puedes personalizar el manejo de diferentes tipos de errores aquí
              return this.handle401Error(req, next);
            } else {
              console.error(
                `Ocurrió un error al realizar la llamada http: , status=${error.status}, message=${error.message}`
              );
            }

        // Puedes lanzar el error nuevamente para que otros observadores lo manejen
        return throwError(() => error);
      })
    );
  }

  private handle401Error(req, next): Observable<any> {
    console.log(
      'Start process for requesting a new access_token, refresh_token will be sended to refresh endpoint'
    );
    return this.executeTokenRefresh(req, next);
  }

  private executeTokenRefresh(
    req: HttpRequest<any>,
    next: HttpHandler
  ) {
    this.tokenService.isRefreshingToken = true;
    req = req.clone({headers: req.headers.delete('Authorization')});
    return this.authService.executeTokenRefresh().pipe(
      switchMap((data: JwtDTO) => {
        const requestClone = this.tokenService.addJwtAccessTokenToAuthorizationHeaderIfPresent(req); // Agregamos los tokens fresquitos al request que le arrojo 401 o sea que se le expiró el access_token
        this.tokenService.isRefreshingToken = false;
        return next.handle(requestClone);
      }),
      catchError(() => {
        this.tokenService.isRefreshingToken = false;
        console.error('refresh_token is expired at header Authorization, you will be logout...'); // Si ingresa aqui es que el refresh token tambien esta expirado (no chance amiguito mandalo al login)
        this.authService.logoutAndRedirectTo('/auth/logout');
        return EMPTY;
      })
    );
  }

   /**
  * @deprecated Este metodo ya no se deberia usar solo queda para mantener el ejemplo, utilizaba el storage del navegador para almacenar el token y es peligroso por CSRF
  */
  private generateRefreshTokenWithBody(
    req: HttpRequest<any>,
    next: HttpHandler
  ) {
    return this.authService.generateRefreshTokenWithBody().pipe(
      switchMap((response: JwtPairDTO) => {
        // Actualizo los tokens devuelto por el endpoint refreshToken
        this.tokenService.setAccessTokenToStorage(response.access_token);
        this.tokenService.setRefreshTokenToStorage(response.refresh_token);
        console.log('New access_token recieved atheader Authorization, updating context');
        const requestClone = this.tokenService.addJwtToAuthorizationHeader(req); // Agregamos los tokens fresquitos al request que le arrojo 401 o sea que se le expiró el access_token
        this.tokenService.isRefreshingToken = false;
        return next.handle(requestClone);
      }),
      catchError(() => {
        this.tokenService.isRefreshingToken = false;
        console.error('refresh_token is expired at header Authorization, you will be logout...'); // Si ingresa aqui es que el refresh token tambien esta expirado (no chance amiguito mandalo al login)
        this.authService.logoutAndRedirectTo('/auth/logout');
        return EMPTY;
      })
    );
  }

   /**
  * @deprecated Este metodo ya no se deberia usar solo queda para mantener el ejemplo 
  */
  private generateRefreshTokenWithCookie(
    req: HttpRequest<any>,
    next: HttpHandler
  ) {
    return this.authService.generateRefreshTokenWithCookie().pipe(
      switchMap((response: JwtPairDTO) => {
        console.log(
          'New access_token renew recieved by cookie, updating context'
        );
        this.tokenService.isRefreshingToken = false
        return next.handle(req);
      }),
      catchError( error => {
        console.error(
          'Error while requesting access_token renew by cookie, you will be logout...'
        ); // Si ingresa aqui es que el refresh token tambien esta expirado (no chance amiguito mandalo al login)
        this.tokenService.isRefreshingToken = false;
        this.authService.logoutAndRedirectTo('/auth/logout');
        return EMPTY;
      })
    );
  }
}
