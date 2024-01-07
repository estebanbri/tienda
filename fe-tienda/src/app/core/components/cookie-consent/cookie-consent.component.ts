import { Component, OnInit } from '@angular/core';
import { CookieConsentService } from './cookie-consent.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-cookie-consent',
  templateUrl: './cookie-consent.component.html',
  styleUrls: ['./cookie-consent.component.css']
})
export class CookieConsentComponent implements OnInit{

  cookieConsentSeen$: Observable<boolean>;

  constructor(private cookieConsentService: CookieConsentService) {
    this.cookieConsentSeen$ = this.cookieConsentService.cookieConsentSeen$;
  }

  ngOnInit(): void {
  }

  giveConsent() {
    this.cookieConsentService.setConsent(true);
  }

  rejectConsent() {
    this.cookieConsentService.setConsent(false);
  }
}
