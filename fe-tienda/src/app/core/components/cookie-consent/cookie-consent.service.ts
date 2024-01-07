import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { LocalStorageService } from 'src/app/core/services/local-storage.service';

@Injectable({
  providedIn: 'root'
})
export class CookieConsentService {

  private _cookieConsentSeen = new BehaviorSubject<boolean>(this.cookieContentSeen);
  public cookieConsentSeen$: Observable<boolean> =  this._cookieConsentSeen.asObservable();

  constructor(private storageService: LocalStorageService) {
  
  }

  get cookieContentSeen() {
    return this.storageService.getItem('cookieConsentSeen') != null;
  }

  get cookieContentAccepted(): boolean {
    return this.storageService.getItem('cookieConsentAccepted');
  }

  setConsent(consent: boolean): void {
    this._cookieConsentSeen.next(true);
    this.storageService.setItem('cookieConsentSeen', true);
    this.storageService.setItem('cookieConsentAccepted', consent)
  }
}
