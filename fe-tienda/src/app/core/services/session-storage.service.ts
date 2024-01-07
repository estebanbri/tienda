import { Injectable } from '@angular/core';
import { GenericStorage } from '../utils/generic-storage-util';

@Injectable({
  providedIn: 'root'
})
export class SessionStorageService extends GenericStorage {

  constructor() { 
    super(window.sessionStorage);
  }
  
}
