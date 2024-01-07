import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { CustomerDTO } from '../models/customer-dto';
import { STRIPE_CUSTOMER_BASE, STRIPE_CUSTOMER_SEARCH } from '../constants/url-api-constants';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  constructor(private http: HttpClient) { }


  retrieveCustomerById(customerId: string | undefined): Observable<any> {
    return this.http.get<any>(`${STRIPE_CUSTOMER_BASE}/${customerId}`);
  }

  createCustomer(customerDTO: CustomerDTO): Observable<any>  {
    return this.http.post<any>(STRIPE_CUSTOMER_BASE, customerDTO);
  }

  searchCustomerByEmail(email: string): Observable<any>  {
    return this.http.get<any>(`${STRIPE_CUSTOMER_SEARCH}/${email}`);
  }

  updateCustomer(customerId: string, valuesToUpdate: any): Observable<any>  {
    return this.http.post<any>(`${STRIPE_CUSTOMER_BASE}/${customerId}`, valuesToUpdate);
  }


}
