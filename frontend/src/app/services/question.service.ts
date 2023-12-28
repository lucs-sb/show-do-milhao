import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Question } from '../entities/question';
import { StorageService } from './storage.service';
import { Pagination } from '../entities/pagination';
import { environment } from '../environment/environment';

@Injectable({
  providedIn: 'root'
})
export class QuestionService {

  private API_URL = environment.API_URL_DEFAULT + 'question';

  constructor(private http: HttpClient, private localStorage: StorageService) { }

  getQuestionById(id: any) {
    const HTTP_OPTIONS = {
      headers: { authorization: 'Bearer ' + this.localStorage.get('authorization') }
    };
    return this.http.get<Question>(this.API_URL+`/${id}`, HTTP_OPTIONS); 
  }

  getQuestionByIdAndMatchId(questionId: any, matchId: any) {
    const HTTP_OPTIONS = {
      headers: { authorization: 'Bearer ' + this.localStorage.get('authorization') }
    };
    return this.http.get<Question>(this.API_URL+`/${questionId}/match/${matchId}`, HTTP_OPTIONS); 
  }

  reportQuestion(id: any) {
    const HTTP_OPTIONS = {
      headers: { authorization: 'Bearer ' + this.localStorage.get('authorization') }
    };
    return this.http.put<any>(this.API_URL+`/${id}/report`, HTTP_OPTIONS); 
  }

  getQuestionsByUserId(ordination: any) {
    const HTTP_OPTIONS = {
      headers: { authorization: 'Bearer ' + this.localStorage.get('authorization') }
    };
    return this.http.get<Pagination>(this.API_URL+`/user/${this.localStorage.get('user_id')}`+`?${ordination}`, HTTP_OPTIONS)
  }

  getQuestionsByAccepted(accepted: any, ordination: any) {
    const HTTP_OPTIONS = {
      headers: { authorization: 'Bearer ' + this.localStorage.get('authorization') }
    };
    return this.http.get<Pagination>(this.API_URL+`?${ordination}&userId=${this.localStorage.get('user_id')}&accepted=${accepted}`, HTTP_OPTIONS)
  }

  getQuestionsToApprovals(ordination: any) {
    const HTTP_OPTIONS = {
      headers: { authorization: 'Bearer ' + this.localStorage.get('authorization') }
    };
    return this.http.get<Pagination>(this.API_URL+`/user/${this.localStorage.get('user_id')}/approval?${ordination}`, HTTP_OPTIONS); 
  }

  validateQuestion(id: any, approve: any) {
    const HTTP_OPTIONS = {
      headers: { authorization: 'Bearer ' + this.localStorage.get('authorization') }
    };
    return this.http.put<any>(this.API_URL+`/${id}/validate?userId=${this.localStorage.get('user_id')}&validation=${approve}`, HTTP_OPTIONS); 
  }

  updateQuestion(data: any) {
    const HTTP_OPTIONS = {
      headers: { authorization: 'Bearer ' + this.localStorage.get('authorization') }
    };
    return this.http.put<any>(this.API_URL, data, HTTP_OPTIONS); 
  }

  addQuestion(data: any) {
    const HTTP_OPTIONS = {
      headers: { authorization: 'Bearer ' + this.localStorage.get('authorization') }
    };
    return this.http.post<any>(this.API_URL, data, HTTP_OPTIONS); 
  }
}
