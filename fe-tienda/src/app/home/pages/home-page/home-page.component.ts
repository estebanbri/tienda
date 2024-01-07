import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { CookieConsentService } from 'src/app/core/components/cookie-consent/cookie-consent.service';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent implements OnInit {

  length = 50;
  pageSize = 10;
  pageIndex = 0;
  pageSizeOptions = [5, 10, 25];

  hidePageSize = false;
  showPageSizeOptions = true;
  showFirstLastButtons = true;
  disabled = false;

  constructor(private cookiConsentService: CookieConsentService) { }

  ngOnInit(): void {
    console.log("HomeComponent created");
    if(!this.cookiConsentService.cookieContentSeen) {
      this.cookiConsentService.setConsent(false);
    }
  }

  pageEvent: PageEvent;

  handlePageEvent(e: PageEvent) {
    this.pageEvent = e;
    this.length = e.length;
    this.pageSize = e.pageSize;
    this.pageIndex = e.pageIndex;
  }

  setPageSizeOptions(setPageSizeOptionsInput: string) {
    if (setPageSizeOptionsInput) {
      this.pageSizeOptions = setPageSizeOptionsInput.split(',').map(str => +str);
    }
  }

  

}
