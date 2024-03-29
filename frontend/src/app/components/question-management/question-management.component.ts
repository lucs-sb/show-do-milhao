import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, Validators } from '@angular/forms';
import { Question } from 'src/app/entities/question';
import { AlertService } from 'src/app/services/alert.service';
import { QuestionService } from 'src/app/services/question.service';
import { StorageService } from 'src/app/services/storage.service';

@Component({
  selector: 'app-question-management',
  templateUrl: './question-management.component.html',
  styleUrl: './question-management.component.css'
})
export class QuestionManagementComponent implements OnInit{

  questions: Question[] = [];
  question: Question | any;
  search: string = '';
  ordination: string = 'asc';
  type = 'all';

  formUpdateQuestion = this.formBuilder.group({
    questionId: '',
    statement: '',
    answers: this.formBuilder.array([])
  });

  formQuestion = this.formBuilder.group({
    userAccountId: this.localStorage.get("user_id"),
    statement: ['', [Validators.required]],
    answers: this.formBuilder.array([
      this.formBuilder.group({
        description: ['', [Validators.required]],
        correct: ['', [Validators.required]]
      }),
      this.formBuilder.group({
        description: ['', [Validators.required]],
        correct: ['', [Validators.required]]
      }),
      this.formBuilder.group({
        description: ['', [Validators.required]],
        correct: ['', [Validators.required]]
      }),
      this.formBuilder.group({
        description: ['', [Validators.required]],
        correct: ['', [Validators.required]]
      })
    ])
  });

  data: any;

  currentIndex = -1;
  page = 0;
  count = 0;
  pageSize = 3;
  pageSizes = [3, 6, 10];
  totalPages = [0];
  next = true;
  previous = true;

  constructor(private questionService: QuestionService, 
    private formBuilder: FormBuilder, 
    private notifier: AlertService,
    private localStorage: StorageService) { }

  ngOnInit(): void {
    this.retrieveQuestions();
    this.getQuestionsToApproval();
  }

  searchQuestions(){
    try {
      this.questions = [];
      if (this.type == 'all'){
        this.questionService.getQuestionsByUserId("size="+this.pageSize+"&page="+this.page+"&sort="+this.ordination).subscribe((res) => {
          this.questions = res.content;
          this.page = res.number;
          this.count = res.totalElements;
          this.pageSize = res.size;
          this.ordination = "desc";
          this.totalPages = [0];
  
          for(var i = 1; i < res.totalPages; i++)
            this.totalPages.push(i);
  
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
          this.notifier.warn('Algo inesperado aconteceu');
        });
      } else {
        this.questionService.getQuestionsByAccepted(this.type, "size="+this.pageSize+"&page="+this.page+"&sort="+this.ordination).subscribe((res) => {
          this.questions = res.content;
          this.page = res.number;
          this.count = res.totalElements;
          this.pageSize = res.size;
          this.ordination = "desc";
          this.totalPages = [0];
  
          for(var i = 1; i < res.totalPages; i++)
            this.totalPages.push(i);
  
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
          this.notifier.warn('Algo inesperado aconteceu');
        });
      }
    } catch (ex: any) {
      this.notifier.error(ex);
    }
  }

  addQuestion(): void{
    try {
      this.data = this.formQuestion.value;
      this.questionService.addQuestion(this.data).subscribe(() => {
        this.notifier.success('Pergunta criado com sucesso, espere a aprovação da comunidade.');
        this.formQuestion = this.formBuilder.group({
          userAccountId: this.localStorage.get("user_id"),
          statement: ['', [Validators.required]],
          answers: this.formBuilder.array([
            this.formBuilder.group({
              description: ['', [Validators.required]],
              correct: ['', [Validators.required]]
            }),
            this.formBuilder.group({
              description: ['', [Validators.required]],
              correct: ['', [Validators.required]]
            }),
            this.formBuilder.group({
              description: ['', [Validators.required]],
              correct: ['', [Validators.required]]
            }),
            this.formBuilder.group({
              description: ['', [Validators.required]],
              correct: ['', [Validators.required]]
            })
          ])
        });
        this.retrieveQuestions();
      }, () => {
        this.notifier.warn('Não foi possível adicionar a pergunta no momento, tente novamente mais tarde');
      });
    } catch (ex: any) {
      this.notifier.error(ex);
    }
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

    this.retrieveQuestions();
  }

  handlePageSizeChange(event: any): void {
    this.pageSize = event.target.value;
    this.retrieveQuestions();
  }

  retrieveQuestions(): void{
    try {
      this.questionService.getQuestionsByUserId("size="+this.pageSize+"&page="+this.page+"&sort="+this.ordination)
      .subscribe(
        res => {
          this.questions = res.content;
          this.question = this.questions[0];
          this.page = res.number;
          this.count = res.totalElements;
          this.pageSize = res.size;
          this.ordination = "desc";
          this.type = 'all';
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
          this.notifier.warn('Algo inesperado aconteceu');
        });
    }catch (ex: any) {
      this.notifier.error(ex);
    }
  }

  getQuestionById(questionId: any): void{
    try {
      this.formUpdateQuestion = this.formBuilder.group({
        questionId: '',
        statement: '',
        answers: this.formBuilder.array([])
      });

      this.questionService.getQuestionById(questionId).subscribe((res) => {
        this.question = res;
        this.formUpdateQuestion.controls['questionId'].setValue(res.questionId.toString());
        this.formUpdateQuestion.controls['statement'].setValue(res.statement);

        res.answers.forEach(answer => {
          const formGroupAnswer = this.formBuilder.group({
            answerId: answer.answerId,
            description: answer.description,
            correct: answer.correct
          });

          this.formUpdateAnswers.push(formGroupAnswer);
        });
      }, () => {
        this.notifier.warn('Algo inesperado aconteceu');
      });
    } catch (ex: any) {
      this.notifier.error(ex);
    }
  }

  editQuestion(){
    try {
      this.data = this.formUpdateQuestion.value;
      console.log(this.data);
      this.questionService.updateQuestion(this.data).subscribe(() => {
        this.notifier.success('Pergunta editada com sucesso, espere a aprovação da comunidade.');
        this.formUpdateQuestion = this.formBuilder.group({
          questionId: '',
          statement: '',
          answers: this.formBuilder.array([])
        });
        this.retrieveQuestions();
      }, () => {
        this.notifier.warn('Não foi possível editar a pergunta no momento, tente novamente mais tarde');
      });
    } catch (ex: any) {
      this.notifier.error(ex);
    }
  }

  get formUpdateAnswers(): FormArray {
    return this.formUpdateQuestion.get('answers') as FormArray;
  }

  get formAnswers(): FormArray {
    return this.formQuestion.get('answers') as FormArray;
  }

  getQuestionsToApproval(): void{
    try {
      this.questionService.getQuestionsToApprovals("size=10&page=0")
      .subscribe(
        res => {
          document.getElementById("btn-validation")?.setAttribute("data-count", res.totalElements.toString());
        }, () => {
          this.notifier.warn('Algo inesperado aconteceu');
        });
    }catch (ex: any) {
      this.notifier.error(ex);
    }
  }
}
