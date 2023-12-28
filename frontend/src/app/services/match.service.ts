import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { StorageService } from './storage.service';
import { Match } from '../entities/match';
import { environment } from '../environment/environment';

@Injectable({
  providedIn: 'root'
})
export class MatchService {
  private API_URL = environment.API_URL_DEFAULT + 'match';

  constructor(private http: HttpClient, private localStorage: StorageService) { }

  getMatchById(id: any) {
    const HTTP_OPTIONS = {
      headers: { authorization: 'Bearer ' + this.localStorage.get('authorization') }
    };
    return this.http.get<Match>(this.API_URL+`/${id}`, HTTP_OPTIONS); 
  }

  startNewMatch() {
    const HTTP_OPTIONS = {
      headers: { authorization: 'Bearer ' + this.localStorage.get('authorization') }
    };
    return this.http.post<number>(this.API_URL+`?userId=${this.localStorage.get('user_id')}`, null, HTTP_OPTIONS); 
  }

  updateMatch(match: any) {
    const HTTP_OPTIONS = {
      headers: { authorization: 'Bearer ' + this.localStorage.get('authorization') }
    };
    return this.http.put<any>(this.API_URL, match, HTTP_OPTIONS); 
  }
}
