import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class BeerService {

  constructor(private http: HttpClient) { }

  getAll(): Observable<any> {
    // process.env.NODE_TLS_REJECT_UNAUTHORIZED = '0';
    return this.http.get('http://localhost:8080/good-beers'); // Va cherche le spring
  }
}
