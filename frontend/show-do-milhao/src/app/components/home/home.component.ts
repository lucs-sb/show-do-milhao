import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NotifierService } from 'angular-notifier';
import { MatchService } from 'src/app/services/match.service';
import { StorageService } from 'src/app/services/storage.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit  {
  private notifier: NotifierService;
  /**
   * Constructor
   *
   * @param {NotifierService} notifier Notifier service
   */
  constructor(notifier: NotifierService, 
    private matchService: MatchService,
    private localStorage: StorageService,
    private router: Router) { this.notifier = notifier; }

  ngOnInit(): void {
  }

  play(): void{
    try {
        this.matchService.startNewMatch().subscribe((response) => {
          this.localStorage.set('match_id', response.toString());
          this.router.navigate(['/play']);
        }, (error) => {
          this.notifier.notify('error', 'Tente novamente mais tarde');
        });
    }
    catch (ex: any) {
      this.notifier.notify('error', ex);
    }
  }
}
