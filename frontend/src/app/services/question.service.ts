import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Question } from '../entities/question';
import { StorageService } from './storage.service';

@Injectable({
  providedIn: 'root'
})
export class QuestionService {

  private API_URL = 'http://localhost:8080/api/question';

  constructor(private http: HttpClient, private localStorage: StorageService) { }

  getQuestionByIdAndMatchId(questionId: any, matchId: any) {
    const httpOptions = {
      headers: { authorization: 'Bearer ' + localStorage.getItem("authorization") },
    };
    return this.http.get<Question>(this.API_URL+`/${questionId}/match/${matchId}`, httpOptions); 
  }

  reportQuestion(id: any, isReport: any) {
    const httpOptions = {
      headers: { authorization: 'Bearer ' + localStorage.getItem("authorization") },
    };
    return this.http.put<any>(this.API_URL+`/${id}?isReport=${isReport}`, httpOptions); 
  }
}
