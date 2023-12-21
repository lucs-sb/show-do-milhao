import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, Validators } from '@angular/forms';
import { Question } from 'src/app/entities/question';
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

  formQuestion = this.formBuilder.group({
    questionId: '',
    statement: '',
    answers: this.formBuilder.array([])
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
    private localStorage: StorageService) { }

  ngOnInit(): void {
    this.retrieveQuestions();
  }

  searchQuestions(){
    try {
      this.questions = [];
      if (this.type == 'all'){
        this.questionService.getQuestionsByUserId("size="+this.pageSize+"&page="+this.page+"&sort="+this.ordination).subscribe((res) => {
          this.questions = res.content;
          this.question = this.questions[0];
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
          
        });
      }
    } catch (ex: any) {
        
    }
  }

  addQuestion(): void{
    /*try {
      if(!this.formChampionship.value.name || !this.formChampionship.value.description || !this.formChampionship.value.award || !this.formChampionship.value.teams)
        this.notifier.notify('error','Preencha todos os campos');

      else if(this.formChampionship.value.teams?.length! < 16)
        this.notifier.notify('error','Para inicar um novo campeonato é preciso selecionar 16 times');

      else{
        this.data = this.formChampionship.value;
        this.championshipService.addChampionship(this.data).subscribe(() => {
          this.notifier.notify('success', 'Campeonato criado com sucesso');
          this.formChampionship = this.formBuilder.group({
            name: ['', [Validators.required]],
            description: ['', [Validators.required]],
            award: ['', [Validators.required]],
            user: this.formBuilder.group({
              id: this.localStorage.get('user_id')
            }),
            teams: new FormBuilder().array([])
          });
          this.retrieveChampionships();
        }, () => {
          this.notifier.notify('error', 'Não foi possível criar um novo campeonato no momento, tente novamente mais tarde');
        });
      }
    }catch (ex: any) {
      this.notifier.notify('error', ex);
    }*/
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
          
        });
    }catch (ex: any) {
      
    }
  }

  getQuestionById(questionId: any): void{
    try {
      this.formQuestion = this.formBuilder.group({
        questionId: '',
        statement: '',
        answers: this.formBuilder.array([])
      });

      this.questionService.getQuestionById(questionId).subscribe((res) => {
        this.question = res;
        this.formQuestion.controls['questionId'].setValue(res.questionId.toString());
        this.formQuestion.controls['statement'].setValue(res.statement);

        res.answers.forEach(answer => {
          const formGroupAnswer = this.formBuilder.group({
            answerId: answer.answerId,
            description: answer.description,
            correct: answer.correct
          });

          this.formAnswers.push(formGroupAnswer);
        });
      });
    } catch (ex: any) {
      //this.notifier.notify('error', ex);
    }
  }

  editQuestion(){
    try {
      this.data = this.formQuestion.value;
      console.log(this.data);
      this.questionService.updateQuestion(this.data).subscribe(() => {
        //this.notifier.notify('success', 'Conta editada com sucesso');
        this.formQuestion = this.formBuilder.group({
          questionId: '',
          statement: '',
          answers: this.formBuilder.array([])
        });
        this.retrieveQuestions();
      }, () => {
        //this.notifier.notify('error', 'Não foi possível editar a conta no momento, tente novamente mais tarde');
      });
    } catch (ex: any) {
      //this.notifier.notify('error', ex);
    }
  }

  get formAnswers(): FormArray {
    return this.formQuestion.get('answers') as FormArray;
  }
}
