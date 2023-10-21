import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { StorageService } from './storage.service';
import { User } from '../entities/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private API_URL = 'http://localhost:8080/api/user';

  constructor(private http: HttpClient, private localStorage: StorageService) { }

  login(username: any, password: any): Observable<any>{

    const httpOptions = {
      headers: { authorization: 'Basic ' + btoa(username + ':' + password) }
    };

    return this.http.get<User>(this.API_URL+`/login?nickname=${username}&password=${password}`, httpOptions).pipe(
      tap((response: User) => {
        this.localStorage.set('authorization', btoa(username + ':' + password));
        this.localStorage.set('user_id', response.userAccountId.toString());
        return response
      })
    );
  }

  getUserById(id: any) {
    const httpOptions = {
      headers: { authorization: 'Basic ' + localStorage.getItem("authorization") },
    };
    return this.http.get<User>(this.API_URL+`/${id}`, httpOptions); 
  }

  addUser(user: any) {
    return this.http.post<any>(this.API_URL, user); 
  }

  updateUser(user: any, id: any) {
    const httpOptions = {
      headers: { authorization: 'Basic ' + localStorage.getItem("authorization") },
    };
    return this.http.put<any>(this.API_URL+`/${id}`, user, httpOptions); 
  }

  deleteUser(id: any) {
    const httpOptions = {
      headers: { authorization: 'Basic ' + localStorage.getItem("authorization") },
    };
    return this.http.delete<any>(this.API_URL+`/${id}`, httpOptions); 
  }
}
