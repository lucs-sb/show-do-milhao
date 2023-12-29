import { Component, OnInit } from '@angular/core';
import { Validators, FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { AlertService } from 'src/app/services/alert.service';
import { StorageService } from 'src/app/services/storage.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  
  formUser = this.formBuilder.group({
    name: ['', [Validators.required]],
    nickname: ['', [Validators.required]],
    password: ['', [Validators.required]],
    avatar: null
  });

  data: any;

  constructor(private registerService: UserService, 
    private formBuilder: FormBuilder,
    private notifier: AlertService,
    private storage: StorageService,
    private router: Router) { }

  ngOnInit(): void {
  }

  register(): void{
    try {
      if(!this.formUser.value.name || !this.formUser.value.nickname || !this.formUser.value.password)
        this.notifier.info('Preencha todos os campos obrigatórios');

      this.data = this.formUser.value;
      
      this.registerService.addUser(this.data).subscribe(() => {
        this.storage.logoutUser();
        this.router.navigate(['/']);
      }, () => {
        this.storage.logoutUser();
        this.notifier.warn('Não foi possível realizar o cadastro no momento, tente novamente mais tarde');
      });
    }catch (ex: any) {
      this.notifier.error(ex);
    }
  }

  login(): void{
    this.router.navigate(['/']);
  }
}
