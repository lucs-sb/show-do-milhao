import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Answer } from 'src/app/entities/answer';
import { Match } from 'src/app/entities/match';
import { Question } from 'src/app/entities/question';
import { AlertService } from 'src/app/services/alert.service';
import { MatchService } from 'src/app/services/match.service';
import { QuestionService } from 'src/app/services/question.service';
import { StorageService } from 'src/app/services/storage.service';

@Component({
  selector: 'app-play',
  templateUrl: './play.component.html',
  styleUrls: ['./play.component.css']
})
export class PlayComponent implements OnInit {
  match?: Match;
  question?: Question;
  answers?: Answer[];
  prizeTable: string[][] = [
    ['R$ 1 mil', 'R$ 0', 'R$ 0'], 
    ['R$ 5 mil', 'R$ 1 mil', 'R$ 500'], 
    ['R$ 50 mil', 'R$ 5 mil', 'R$ 2.5 mil'], 
    ['R$ 100 mil', 'R$ 50 mil', 'R$ 25 mil'],
    ['R$ 300 mil', 'R$ 100 mil', 'R$ 50 mil'], 
    ['R$ 500 mil', 'R$ 300 mil', 'R$ 150 mil'],
    ['R$ 1 milhão', 'R$ 500 mil', 'R$ 0']];
    
  info: string[] = ['', '', ''];
  lastQuestionAnswered: number = 0;
  isDeleted?: Boolean;

  constructor(
    private matchService: MatchService, 
    private questionService: QuestionService,
    private router: Router,
    private notifier: AlertService,
    private localStorage: StorageService) { }

  ngOnInit(): void {
    this.getMatchById();
  }

  getMatchById(): void{
    try {
      this.matchService.getMatchById(this.localStorage.get("match_id")).subscribe((res) => {
        this.match = res;
        this.isDeleted = this.match.deletedAnswers;
        this.lastQuestionAnswered = this.match.lastQuestionAnswered;
        this.info = this.prizeTable[this.match.lastQuestionAnswered];

        this.questionService.getQuestionByIdAndMatchId(this.match.matchQuestions.find(x => x.position == this.match?.lastQuestionAnswered)?.questionId, this.match.matchId).subscribe(
          (res) => {
            this.question = res;
            this.answers = res.answers;
            this.match?.matchAnswers.filter(x => x.deleted == true).forEach(x => {
              this.answers = this.answers?.filter(y => y.answerId != x.answerId);
            });
          });
      }, () => {
        this.router.navigate(['/home']);
        this.notifier.warn('Algo inesperado aconteceu');
      }); 
    }catch (ex: any) {
      this.notifier.error(ex);
    }
  }

  respond(answer: Answer): void{
    try {
      var award;
      var ended;
      var lastQuestionAnswered;
      var reasonForClosing;

      if(answer.correct && this.lastQuestionAnswered == 6){
        award = 1000000;
        ended = true;
        lastQuestionAnswered = this.lastQuestionAnswered;
        reasonForClosing = 'Ganhou';
      }
      else if(answer.correct){
        ended = false;
        lastQuestionAnswered = this.lastQuestionAnswered++;
      }
      else{
        lastQuestionAnswered = this.lastQuestionAnswered;
        if(lastQuestionAnswered == 0 || lastQuestionAnswered == 6)
          award = 0;
        else if(lastQuestionAnswered == 1)
          award = 500;
        else{
          award = Number.parseInt(this.info[2].split(' ')[2]) * 1000;
        }

        ended = true;
        reasonForClosing = 'Perdeu';
      }

      const user = {
        userId: this.localStorage.get('user_id')
      }
  
      const body = {
        matchId: this.match?.matchId,
        user: user,
        award: award,
        ended: ended,
        lastQuestionAnswered: lastQuestionAnswered + 1,
        deletedAnswers: this.isDeleted,
        reasonForClosing: reasonForClosing
      };

      this.matchService.updateMatch(body).subscribe((res) => {
        if(body.ended == false){
          this.info = this.prizeTable[this.lastQuestionAnswered];
          console.log(this.lastQuestionAnswered);
          this.questionService.getQuestionByIdAndMatchId(this.match?.matchQuestions.find(x => x.position == this.lastQuestionAnswered)?.questionId, this.match?.matchId).subscribe(
            (res) => {
              this.question = res;
              this.answers = res.answers;
              this.match?.matchAnswers.filter(x => x.deleted == true).forEach(x => {
                this.answers = this.answers?.filter(y => y.answerId != x.answerId);
              });
          });
        }
        else{
          //TODO uma mensagem dizendo que perdeu
          //this.notifier.notify('error', 'Reposta errada');
          this.router.navigate(['/home']);
        }
        }, () => {
          this.notifier.warn('Algo inesperado aconteceu');
        }); 
    }catch (ex: any) {
      this.notifier.error(ex);
    }
  }

  stop(): void{
    try {
      var award;
      if(this.lastQuestionAnswered == 0)
        award = 0;
      else{
        award = Number.parseInt(this.info[1].split(' ')[1]) * 1000;
      }

      const user = {
        userId: this.localStorage.get('user_id')
      }
  
      const body = {
        matchId: this.match?.matchId,
        user: user,
        award: award,
        ended: true,
        lastQuestionAnswered: this.lastQuestionAnswered++,
        deletedAnswers: this.isDeleted,
        reasonForClosing: 'Parou'
      };

      this.matchService.updateMatch(body).subscribe(() => {
          //TODO uma mensagem dizendo que parou
          //this.notifier.notify('error', 'Reposta errada');
          this.router.navigate(['/home']);
        }, () => {
          this.notifier.warn('Algo inesperado aconteceu');
        });  
    }catch (ex: any) {
      this.notifier.error(ex);
    }
  }

  deleteAnswers(): void{
    var allAnswers = this.answers;
    var correctAnswer = this.answers?.filter(x => x.correct == true);
    this.answers = this.answers?.filter(x => x.correct == false).slice(1, 2);
    correctAnswer?.forEach(x => {
      this.answers = this.answers?.concat(x);
    });

    var deletedAnswers: number[] = [];

    allAnswers?.forEach(current => {
      console.log(this.answers?.indexOf(current))
      if(this.answers?.indexOf(current) == -1){
        deletedAnswers.push(current.answerId);
      }
    });

    this.isDeleted = true;

    const user = {
      userId: this.localStorage.get('user_id')
    }

    const body = {
      matchId: this.match?.matchId,
      user: user,
      questionId: this.question?.questionId,
      ended: false,
      lastQuestionAnswered: this.lastQuestionAnswered,
      deletedAnswers: this.isDeleted,
      answers: deletedAnswers
    };

    this.matchService.updateMatch(body).subscribe(); 
  }

  report(): void{
    this.questionService.reportQuestion(this.question?.questionId, true).subscribe();
  }
}
