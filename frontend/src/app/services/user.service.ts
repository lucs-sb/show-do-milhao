import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { StorageService } from './storage.service';
import { User } from '../entities/user';
import { Login } from '../entities/login';
import { Router } from '@angular/router';
import { NotifierService } from 'angular-notifier';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private API_URL = 'http://localhost:8080/api/user';

  constructor(private http: HttpClient, 
    private storage: StorageService, 
    private notifier: NotifierService,
    private router: Router) { }

  login(username: any, password: any) {

    const body = {
      nickname: username,
      password: password
    }

    return this.http.post<Login>(this.API_URL+`/login`, body).subscribe( 
      res => {
        this.storage.set('authorization', res.token);
        this.storage.set('user_id', res.user_id);
        this.router.navigate(['/home']);
      }, () => {
        this.notifier.notify('error', 'Informações inválidas');
      });
  }

  getUserById(id: any) {
    const httpOptions = {
      headers: { authorization: 'Bearer ' + localStorage.getItem("authorization") },
    };
    return this.http.get<User>(this.API_URL+`/${id}`, httpOptions); 
  }

  addUser(user: any) {
    return this.http.post<any>(this.API_URL, user); 
  }

  updateUser(user: any, id: any) {
    const httpOptions = {
      headers: { authorization: 'Bearer ' + localStorage.getItem("authorization") },
    };
    return this.http.put<any>(this.API_URL+`/${id}`, user, httpOptions); 
  }

  deleteUser(id: any) {
    const httpOptions = {
      headers: { authorization: 'Bearer ' + localStorage.getItem("authorization") },
    };
    return this.http.delete<any>(this.API_URL+`/${id}`, httpOptions); 
  }

  getHallDaFama() {
    const httpOptions = {
      headers: { authorization: 'Bearer ' + localStorage.getItem("authorization") },
    };
    return this.http.get<User[]>(this.API_URL+'/hall-da-fama', httpOptions); 
  }
}
