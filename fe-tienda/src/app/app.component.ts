import { Component, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { GoogleTagManagerService } from 'angular-google-tag-manager';
import { AuthService } from './core/services/auth.service';
import { JwtDTO } from './core/models/jwt-dto';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  constructor(private router: Router,
    private gtmService: GoogleTagManagerService,
    private authService: AuthService) {
    this.gtmService.addGtmToDom();
  }

  ngOnInit(): void {
    this.setUpGoogleTagManager();
    this.executeRefreshToken();
  }
  

  /* Ejemplo de Eventos Custom que podes poner el siguiente codigo en cualquier parte de app si queres enviar algun dato en particular que no 
  este en la url y quieras enviarlo al dato igualmente a google analitics.
   const gtmTag = {
    event: 'button-click',
    data: 'my-custom-event',
  };
  this.gtmService.pushTag(gtmTag);
  */

  private setUpGoogleTagManager() {
    this.router.events.forEach(item => {
      if (item instanceof NavigationEnd) {
        const gtmTag = {
          event: 'page',
          pageName: item.url
        };
        this.gtmService.pushTag(gtmTag);
      }
    });
  }

  executeRefreshToken() {
    this.authService.executeTokenRefresh().subscribe();
  }

}
