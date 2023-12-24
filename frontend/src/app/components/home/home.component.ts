import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AlertService } from 'src/app/services/alert.service';
import { MatchService } from 'src/app/services/match.service';
import { StorageService } from 'src/app/services/storage.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit  {

  constructor( 
    private matchService: MatchService,
    private localStorage: StorageService,
    private notifier: AlertService,
    private router: Router) { }

  ngOnInit(): void {
  }

  play(): void{
    try {
        this.matchService.startNewMatch().subscribe((response) => {
          this.localStorage.set('match_id', response.toString());
          this.router.navigate(['/play']);
        }, (error) => {
          this.notifier.warn('Tente novamente mais tarde');
        });
    }
    catch (ex: any) {
      this.notifier.error(ex);
    }
  }
}
