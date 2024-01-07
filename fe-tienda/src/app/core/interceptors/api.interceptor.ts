import {
  HttpHandler,
  HttpInterceptor,
  HttpRequest
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { TokenService } from '../services/token.service';
import { EMPTY } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ApiInterceptor implements HttpInterceptor {
  constructor(
    private tokenService: TokenService
  ) {}

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    if (!req.url.includes('/refresh-token') && this.tokenService.isRefreshingToken) {
      console.log('refresh_token process is in process, this request will be cancelled');
      return EMPTY; // Cancelo el request porque quiere decir que esta haciendo el trip al backend para refrescar el token
    }

    req = req.clone({
      withCredentials: true, // Habilitar el envío de cookies en cada request hecha por httpclient
    });

    req = this.tokenService.addJwtAccessTokenToAuthorizationHeaderIfPresent(req);


    return next.handle(req);
  }
}
