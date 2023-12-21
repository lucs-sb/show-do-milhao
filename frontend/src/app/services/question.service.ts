import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Question } from '../entities/question';
import { StorageService } from './storage.service';
import { Pagination } from '../entities/pagination';
import { API_URL_DEFAULT } from '../env/environment'

@Injectable({
  providedIn: 'root'
})
export class QuestionService {

  private API_URL = API_URL_DEFAULT + 'question';

  constructor(private http: HttpClient, private localStorage: StorageService) { }

  getQuestionByIdAndMatchId(questionId: any, matchId: any) {
    const HTTP_OPTIONS = {
      headers: { authorization: 'Bearer ' + this.localStorage.get('authorization') }
    };
    return this.http.get<Question>(this.API_URL+`/${questionId}/match/${matchId}`, HTTP_OPTIONS); 
  }

  reportQuestion(id: any, isReport: any) {
    const HTTP_OPTIONS = {
      headers: { authorization: 'Bearer ' + this.localStorage.get('authorization') }
    };
    return this.http.put<any>(this.API_URL+`/${id}?isReport=${isReport}`, HTTP_OPTIONS); 
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
}
