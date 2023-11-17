import { Component, OnInit } from '@angular/core';
import { Validators, FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { NotifierService } from 'angular-notifier';
import { StorageService } from 'src/app/services/storage.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  private notifier: NotifierService;
  
  formUser = this.formBuilder.group({
    name: ['', [Validators.required]],
    nickname: ['', [Validators.required]],
    password: ['', [Validators.required]],
    avatar: ''
  });

  data: any;

  /**
   * Constructor
   *
   * @param {NotifierService} notifier Notifier service
   */
  constructor(private registerService: UserService, 
    private formBuilder: FormBuilder,
    private storage: StorageService, notifier: NotifierService,
    private router: Router) { this.notifier = notifier; }

  ngOnInit(): void {
  }

  register(): void{
    try {
      if(!this.formUser.value.name || !this.formUser.value.nickname || !this.formUser.value.password)
        this.notifier.notify('error','Preencha todos os campos obrigatórios');

      this.data = this.formUser.value;
      this.registerService.addUser(this.data).subscribe(() => {
        this.storage.logoutUser();
        this.router.navigate(['/']);
      }, () => {
        this.storage.logoutUser();
        this.notifier.notify('error', 'Não foi possível realizar o cadastro no momento, tente novamente mais tarde');
      });
    }catch (ex: any) {
      this.notifier.notify('error', ex);
    }
  }

  login(): void{
    this.router.navigate(['/']);
  }
}
