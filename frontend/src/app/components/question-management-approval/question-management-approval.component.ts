import { Component } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { QuestionService } from 'src/app/services/question.service';
import { StorageService } from 'src/app/services/storage.service';
import { Question } from 'src/app/entities/question';

@Component({
  selector: 'app-question-management-approval',
  templateUrl: './question-management-approval.component.html',
  styleUrl: './question-management-approval.component.css'
})
export class QuestionManagementApprovalComponent {

  questions: Question[] = [];

  currentIndex = -1;
  page = 0;
  count = 0;
  pageSize = 3;
  pageSizes = [3, 6, 10];
  totalPages = [0];
  next = true;
  previous = true;

  constructor(private questionService: QuestionService,  
    private localStorage: StorageService,
    private location: Location) { }

  ngOnInit(): void {
    this.getQuestionsToApproval();
  }

  validateQuestion(questionId: any, approve: any): void{
    try {
      this.questionService.validateQuestion(questionId, approve).subscribe(() => {
        this.getQuestionsToApproval();
      }, () => {
        
      });
    }catch (ex: any) {}
  }

  handlePageChange(event: any): void {
    if('previous' == event)
      this.page -= 1;
    else if('next' == event)
      this.page += 1;
    else
      this.page = event;
    
    if(event == 0 || this.page == 0)
      this.previous = true;

    if(event == this.totalPages.length - 1 || this.page == this.totalPages.length - 1)
      this.next = true;
    
    for(var i = 0; i < this.totalPages.length; i++){
      if(document.getElementById(this.totalPages[i].toString())?.innerText != this.page.toString())
        document.getElementById(this.totalPages[i].toString())?.classList.remove("active");
      else
        document.getElementById(this.totalPages[i].toString())?.classList.add("active");
    }

    this.getQuestionsToApproval();
  }

  handlePageSizeChange(event: any): void {
    this.pageSize = event.target.value;
    this.getQuestionsToApproval();
  }

  getQuestionsToApproval(): void{
    try {
      this.questionService.getQuestionsToApprovals("size="+this.pageSize+"&page="+this.page)
      .subscribe(
        res => {
          this.questions = res.content;
          this.page = res.number;
          this.count = res.totalElements;
          this.pageSize = res.size;
          this.totalPages = [0];
          
          for(var i = 0; i < res.totalPages; i++)
            this.totalPages[i] = i;

          if(this.page == 0)
            this.previous = true;
          else
            this.previous = false;
      
          if(this.page == this.totalPages.length - 1)
            this.next = true;
          else
            this.next = false;

          for(var i = 0; i < this.totalPages.length; i++){
            if(document.getElementById(this.totalPages[i].toString())?.innerText != this.page.toString())
              document.getElementById(this.totalPages[i].toString())?.classList.remove("active");
            else
              document.getElementById(this.totalPages[i].toString())?.classList.add("active");
          }
        }, () => {
          
        });
    }catch (ex: any) {
      
    }
  }

  goBack(): void {
    this.location.back();
  }
}