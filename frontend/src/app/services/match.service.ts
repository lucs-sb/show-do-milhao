import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { StorageService } from './storage.service';
import { Match } from '../entities/match';
import { HTTP_OPTIONS, API_URL_DEFAULT } from '../env/environment'

@Injectable({
  providedIn: 'root'
})
export class MatchService {
  private API_URL = API_URL_DEFAULT + 'match';

  constructor(private http: HttpClient, private localStorage: StorageService) { }

  getMatchById(id: any) {
    return this.http.get<Match>(this.API_URL+`/${id}`, HTTP_OPTIONS); 
  }

  startNewMatch() {
    return this.http.post<number>(this.API_URL+`?userId=${this.localStorage.get('user_id')}`, null, HTTP_OPTIONS); 
  }

  updateMatch(match: any) {
    return this.http.put<any>(this.API_URL, match, HTTP_OPTIONS); 
  }
}
