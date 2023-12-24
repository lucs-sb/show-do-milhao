import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { AlertService } from 'src/app/services/alert.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  formUser = this.formBuilder.group({
    email: ['', [Validators.required]],
    password: ['', [Validators.required]]
  });


    constructor(private loginService: UserService, 
      private notifier: AlertService,
      private formBuilder: FormBuilder,
      private router: Router
    ) { }

  ngOnInit(): void {
  }

  login() {
    try {
      if(!this.formUser.value.email || !this.formUser.value.password)
        this.notifier.error('Preencha todos os campos');

      this.loginService.login(this.formUser.value.email, this.formUser.value.password);
    }
    catch (ex: any) {
      this.notifier.error(ex);
    }
  }

  createAccount(): void{
    this.router.navigate(['/register']);
  }
}