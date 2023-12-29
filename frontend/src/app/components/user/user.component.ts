import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from 'src/app/entities/user';
import { AlertService } from 'src/app/services/alert.service';
import { StorageService } from 'src/app/services/storage.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  user: User | any;
  formUser = this.formBuilder.group({
    name: '',
    nickname: '',
    password: '',
    avatar: null
  });
  data: any;

   constructor(private userService: UserService, 
    private formBuilder: FormBuilder, 
    private notifier: AlertService,
    private localStorage: StorageService,
    private router: Router) { }

  ngOnInit(): void {
    this.getUserById();
  }

  getUserById(): void {
    try {
      this.userService.getUserById(this.localStorage.get("user_id")).subscribe((res) => (this.user = res));
    } catch (ex: any) {
      this.notifier.error(ex);
    }
  }

  updateUser(): void{
    try {
      if(this.formUser.value.name || this.formUser.value.nickname || this.formUser.value.password || this.formUser.value.avatar){
        this.data = this.formUser.value;

        this.userService.updateUser(this.data, this.localStorage.get("user_id")).subscribe(() => {
          this.notifier.success('Conta editada com sucesso');
          this.formUser = this.formBuilder.group({
            name: '',
            nickname: '',
            password: '',
            avatar: null
          });
          this.getUserById();
        }, () => {
          this.notifier.warn('Não foi possível editar a conta no momento, tente novamente mais tarde');
        });
      }else
        this.notifier.info('Preencha algum campo');
    } catch (ex: any) {
      this.notifier.error(ex);
    }
  }

  deleteUser(): void{
    try {
      this.userService.deleteUser(this.localStorage.get("user_id")).subscribe(() => {
        this.router.navigate(['/login']);
      }, () => {
        this.notifier.warn('Não foi possível deletar sua conta no momento, tente novamente mais tarde');
      });
    } catch (ex: any) {
      this.notifier.error(ex);
    }
  }
}
