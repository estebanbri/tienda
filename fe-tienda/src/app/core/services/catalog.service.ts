import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, catchError, EMPTY, Observable, of, throwError } from 'rxjs';
import { CatalogItem } from '../models/catalog-item';
import { CATALGO_BASE } from '../constants/url-api-constants';

@Injectable({
  providedIn: 'root'
})
export class CatalogService {

  private _catalog: BehaviorSubject<CatalogItem[]> = new BehaviorSubject<CatalogItem[]>(null);
  catalog$: Observable<CatalogItem[]> = this._catalog.asObservable();

  private _filterSearchTerm: BehaviorSubject<string> = new BehaviorSubject<string>('');
  filterSearchTerm$: Observable<string> = this._filterSearchTerm.asObservable();

  constructor(private http: HttpClient) { }

  loadCatalog(): void {
    this.http.get<CatalogItem[]>(CATALGO_BASE).pipe(
      catchError( error => {
        this._catalog.next([])
        return EMPTY;
      })
    )
    .subscribe(data => this._catalog.next(data));
  }

  updateSearchTerm(term: string) {
    this._filterSearchTerm.next(term);
  }

}
