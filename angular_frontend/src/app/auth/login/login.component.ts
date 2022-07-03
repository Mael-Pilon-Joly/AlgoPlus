import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/apiservices.service';
import { Router } from '@angular/router';
import { BehaviorSubject, Subject } from 'rxjs';
import { User } from '../../models/user.model';
import { RequestResponseUser } from '../../models/requestresponselogin.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  profile$: Subject<any> = new BehaviorSubject<any>({});

  userDTO ={
    username: "",
    password: "",
    rememberme: false,
  }

  constructor(private services: ApiService, private router: Router) { }
  emit(value: any) {
    this.profile$.next(value);
  }
  get profile(): BehaviorSubject<any> {
    return this.profile$ as BehaviorSubject<any>;
  }

  
  login(): void {
    const data = {
      username: this.userDTO.username,
      password: this.userDTO.password,
    };

    this.services.login(data)
    this.router.navigate(['/userboard']);
  }

  ngOnInit(): void {
  }

}
