import { Component, OnInit } from '@angular/core';
import { StorageService } from '../../services/storage.service';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  name: string = '';
  url_photo: string = '';

  constructor(private storage: StorageService, 
    private userService: UserService, 
    private router: Router) { }

  ngOnInit(): void {
    this.getUserById();
  }

  getUserById(): void{
    try {
      this.userService.getUserById(localStorage.getItem("user_id")).subscribe((req) => {
        this.name = req.name;
        this.url_photo = req.avatar;
      }, () => {
        this.storage.logoutUser();
        this.router.navigate(['/']);
      }); 
    }catch (ex: any) {
      //this.notifier.notify('error', ex);
    }
  }

  logout() {
    this.storage.clear();
  }
}
