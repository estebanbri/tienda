import { APP_INITIALIZER, NgModule } from '@angular/core';
import { ConfigService } from './services/config.service';
import { EMPTY, catchError, of, take } from 'rxjs';
import { AuthService } from '../core/services/auth.service';
import { TokenService } from '../core/services/token.service';
import { JwtDTO } from '../core/models/jwt-dto';

// CARGA LA CONFIGURACION DE LA APP DINAMICAMENTE HACIENDO UNA LLAMADA HTTP SIN NECESITDAD DE DEFINIR ESTATICAMENTE LA CONFIG EN environments.ts
// La desventaja de definir configuracion segun ambientes en environments.ts es decir de manera estatica, es que tenes que recompilar tu app 
// (es decir ejecutar ng build) por cada ambiente que tengas, por este motivo en versiones mas neuvas de angular directamente no existe mas environments.ts
@NgModule({
  providers: [
    ConfigService,
    { 
      provide: APP_INITIALIZER, 
      useFactory: (config: ConfigService) => {
        return () => {
          config.loadConfig();
          return config.config$.pipe(take(1));
        }
      }, 
      multi: true, 
      deps: [ConfigService]
    }
  ]
})
export class InitializerModule { }
