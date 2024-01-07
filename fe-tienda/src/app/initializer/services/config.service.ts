import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ConfigDTO } from '../../core/models/config-dto';
import { BehaviorSubject, filter } from 'rxjs';
import { CONFIG_BASE } from 'src/app/core/constants/url-api-constants';

/**
 * Fuente de carga asincrona de configuracion https://www.youtube.com/watch?v=MoRScEseTf4
 */
@Injectable() 
export class ConfigService {

  private _config = new BehaviorSubject<ConfigDTO | null>(null);
  readonly config$ = this._config.asObservable().pipe(
    filter(data => !!data) // filtramos valores null estamos solo interesados en resolved data
  );

  constructor(private _http: HttpClient) { }

  loadConfig() {
    return this._http.get(CONFIG_BASE).subscribe({
      next: (data: ConfigDTO) => this._config.next(data),
      error: (error => console.error('Error al cargar la config', error))
    });
  }

  get config() {
    return this._config.getValue();
  }
}
