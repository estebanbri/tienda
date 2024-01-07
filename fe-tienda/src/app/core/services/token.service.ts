import { HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import jwt_decode from 'jwt-decode';
import { AUTH_REFRESH_TOKEN_WITH_BODY } from '../constants/url-api-constants';
import { SessionStorageService } from './session-storage.service';
import { JwtDTO } from '../models/jwt-dto';

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  private static ACCESS_TOKEN_KEY = 'access_token';
  private static REFRESH_TOKEN_KEY = 'refresh_token';
  isRefreshingToken: boolean;
  private accessToken: JwtDTO;

  constructor(private storageService: SessionStorageService) { }


  getRefreshTokenFromStorage(): string {
    return this.storageService.getItem(TokenService.REFRESH_TOKEN_KEY);
  }

  getAccessTokenFromStorage() {
    return this.storageService.getItem(TokenService.ACCESS_TOKEN_KEY);
  }

  setAccessToken(jwtDto: JwtDTO) {
    this.accessToken = jwtDto;
  }

  setAccessTokenToStorage(token: string): void {
    this.storageService.setItem(TokenService.ACCESS_TOKEN_KEY, token);
  }

  setRefreshTokenToStorage(token: string): void {
    this.storageService.setItem(TokenService.REFRESH_TOKEN_KEY, token);
  }

  cleanUpJwtTokens() {
    this.setAccessToken(null);
    this.deleteAccessToken();
    this.deleteRefreshToken();
  }

  checkIfJwtPairExistOnStorage(): boolean {
    return this.storageService.getItem(TokenService.ACCESS_TOKEN_KEY) != null
      && this.storageService.getItem(TokenService.ACCESS_TOKEN_KEY) != ''
      && this.storageService.getItem(TokenService.REFRESH_TOKEN_KEY) != null
      && this.storageService.getItem(TokenService.REFRESH_TOKEN_KEY) != '';
  }

  addJwtAccessTokenToAuthorizationHeaderIfPresent(request: HttpRequest<any>): HttpRequest<any> {
		return this.accessToken ? request.clone({ headers: request.headers.set('Authorization', 'Bearer ' + this.accessToken.token) }) : request;
  }

  addJwtToAuthorizationHeader(request: HttpRequest<any>): HttpRequest<any> {
    const token = request.url === AUTH_REFRESH_TOKEN_WITH_BODY ? this.getRefreshTokenFromStorage() : this.getAccessTokenFromStorage();
		return  request.clone({ headers: request.headers.set('Authorization', 'Bearer ' + token) });
  }

  getSubject(): string {
    const { token } = this.accessToken;
    var decoded = jwt_decode(token as string);
    console.log(decoded['sub']);
    return decoded['sub'];
  }

  private deleteAccessToken(): void {
    this.storageService.removeItem(TokenService.ACCESS_TOKEN_KEY);
  }

  private deleteRefreshToken(): void {
    this.storageService.removeItem(TokenService.REFRESH_TOKEN_KEY);
  }


}
