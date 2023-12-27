import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/entities/user';
import { AlertService } from 'src/app/services/alert.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-halldafama',
  templateUrl: './halldafama.component.html',
  styleUrls: ['./halldafama.component.css']
})
export class HalldafamaComponent implements OnInit {

  users?: User[];

   constructor(private userService: UserService, private notifier: AlertService) { }

  ngOnInit(): void {
    this.getHallDaFama();
  }

  getHallDaFama(): void {
    try {
      this.userService.getHallDaFama().subscribe((res) => (this.users = res));
    } catch (ex: any) {
      this.notifier.error(ex);
    }
  }

  mask(n: string){
    var n = ''+n, t = n.length -1, novo = '';

    for( var i = t, a = 1; i >=0; i--, a++ ){
        var ponto = a % 3 == 0 && i > 0 ? '.' : '';
        novo = ponto + n.charAt(i) + novo;
    }

    return novo;
  }
}
