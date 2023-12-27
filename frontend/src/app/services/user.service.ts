import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { StorageService } from './storage.service';
import { User } from '../entities/user';
import { Login } from '../entities/login';
import { Router } from '@angular/router';
import { environment } from '../environment/environment'
import { AlertService } from './alert.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private API_URL = environment.API_URL_DEFAULT + 'user';

  constructor(private http: HttpClient, 
    private localStorage: StorageService, 
    private notifier: AlertService,
    private router: Router) { }

  login(username: any, password: any) {

    const body = {
      nickname: username,
      password: password
    }

    return this.http.post<Login>(this.API_URL+`/login`, body).subscribe( 
      res => {
        this.localStorage.set('authorization', res.token);
        this.localStorage.set('user_id', res.user_id);
        this.router.navigate(['/home']);
        this.notifier.success('Login efetuado com sucesso');
      }, (error) => {
        this.notifier.warn('Informações inválidas');
      });
  }

  getUserById(id: any) {
    const HTTP_OPTIONS = {
      headers: { authorization: 'Bearer ' + this.localStorage.get('authorization') }
    };
    return this.http.get<User>(this.API_URL+`/${id}`, HTTP_OPTIONS); 
  }

  addUser(user: any) {
    const HTTP_OPTIONS = {
      headers: { authorization: 'Bearer ' + this.localStorage.get('authorization') }
    };
    return this.http.post<any>(this.API_URL, user); 
  }

  updateUser(user: any, id: any) {
    const HTTP_OPTIONS = {
      headers: { authorization: 'Bearer ' + this.localStorage.get('authorization') }
    };
    return this.http.put<any>(this.API_URL+`/${id}`, user, HTTP_OPTIONS); 
  }

  deleteUser(id: any) {
    const HTTP_OPTIONS = {
      headers: { authorization: 'Bearer ' + this.localStorage.get('authorization') }
    };
    return this.http.delete<any>(this.API_URL+`/${id}`, HTTP_OPTIONS); 
  }

  getHallDaFama() {
    const HTTP_OPTIONS = {
      headers: { authorization: 'Bearer ' + this.localStorage.get('authorization') }
    };
    return this.http.get<User[]>(this.API_URL+'/hall-da-fama', HTTP_OPTIONS); 
  }
}
