import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { StorageService } from './storage.service';
import { Match } from '../entities/match';

@Injectable({
  providedIn: 'root'
})
export class MatchService {
  private API_URL = 'http://localhost:8080/api/match';

  constructor(private http: HttpClient, private localStorage: StorageService) { }

  getMatchById(id: any) {
    const httpOptions = {
      headers: { authorization: 'Basic ' + localStorage.getItem("authorization") },
    };
    return this.http.get<Match>(this.API_URL+`/${id}`, httpOptions); 
  }

  startNewMatch() {
    const httpOptions = {
      headers: { authorization: 'Basic ' + localStorage.getItem("authorization") },
    };
    var body = {
      'userAccountId': this.localStorage.get('user_id')
    }
    return this.http.post<number>(this.API_URL, body, httpOptions); 
  }

  updateMatch(match: any) {
    const httpOptions = {
      headers: { authorization: 'Basic ' + localStorage.getItem("authorization") },
    };
    return this.http.put<any>(this.API_URL, match, httpOptions); 
  }
}