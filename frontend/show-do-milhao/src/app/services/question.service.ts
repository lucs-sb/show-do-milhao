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

  getQuestionById(id: any) {
    const httpOptions = {
      headers: { authorization: 'Basic ' + localStorage.getItem("authorization") },
    };
    return this.http.get<Question>(this.API_URL+`/${id}`, httpOptions); 
  }
}
