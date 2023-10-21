import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NotifierService } from 'angular-notifier';
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
    private localStorage: StorageService,
    private router: Router) { this.notifier = notifier; }

  ngOnInit(): void {
  }

  play(): void{
    this.router.navigate(['/play']);
  }
}
