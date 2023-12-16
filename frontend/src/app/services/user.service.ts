import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { StorageService } from './storage.service';
import { User } from '../entities/user';
import { Login } from '../entities/login';
import { Router } from '@angular/router';
import { HTTP_OPTIONS, API_URL_DEFAULT } from '../env/environment'

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private API_URL = API_URL_DEFAULT + 'user';

  constructor(private http: HttpClient, 
    private storage: StorageService, 
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
      });
  }

  getUserById(id: any) {
    return this.http.get<User>(this.API_URL+`/${id}`, HTTP_OPTIONS); 
  }

  addUser(user: any) {
    return this.http.post<any>(this.API_URL, user); 
  }

  updateUser(user: any, id: any) {
    return this.http.put<any>(this.API_URL+`/${id}`, user, HTTP_OPTIONS); 
  }

  deleteUser(id: any) {
    return this.http.delete<any>(this.API_URL+`/${id}`, HTTP_OPTIONS); 
  }

  getHallDaFama() {
    return this.http.get<User[]>(this.API_URL+'/hall-da-fama', HTTP_OPTIONS); 
  }
}
