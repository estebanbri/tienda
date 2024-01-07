import { Injectable } from '@angular/core';
import { GenericStorage } from '../utils/generic-storage-util';

@Injectable({
  providedIn: 'root'
})
export class LocalStorageService extends GenericStorage {

  constructor() {
    super(window.localStorage);
   }
}
