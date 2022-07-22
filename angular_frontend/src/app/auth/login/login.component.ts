import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/apiservices.service';
import { Router } from '@angular/router';
import { BehaviorSubject, Subject } from 'rxjs';
import { User } from '../../models/user.model';
import { RequestResponseUser } from '../../models/requestresponselogin.model';
import { Roles } from '../../models/roles.model';

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

  user: User = {
    id: 0,
    username:"",
    roles:[]
  }

  requestResponse: User ={};

  errorLogin: boolean = false;
  errorPendingConfirmation: boolean = false;
  errorAccountLocked: boolean = false;

  constructor(private services: ApiService, private router: Router) { 
  }

  emit(value: any) {
    this.profile$.next(value);
  }
  get profile(): BehaviorSubject<any> {
    return this.profile$ as BehaviorSubject<any>;
  }

  
  async login(): Promise<void> {
    const data = {
      username: this.userDTO.username,
      password: this.userDTO.password,
      rememberme: this.userDTO.rememberme
    };

    
    if (this.requestResponse) {
    await  this.services.login(data, this.userDTO.rememberme).then( res=> {

      console.log("login successful")
      console.log(this.userDTO.rememberme);
    localStorage.setItem('loggedin', 'true');
    localStorage.setItem('username', this.userDTO.username);
    if (this.userDTO.rememberme == true) {
      localStorage.setItem('rememberme', 'true');
    } else {
      localStorage.setItem('rememberme', 'false');
    }
    console.log("return from login service request:" + res.roles!)
    const result : Roles[] = res.roles!;
    this.services.setValue(result);
    console.log("redirection to userboard...")
    this.router.navigate(['/userboard']);

  }).catch ( error => {
    console.log("login failed")

    this.errorLogin = false;
    this.errorAccountLocked = false;
    this.errorPendingConfirmation = false;
    console.log(JSON.stringify(error.error))
    this.errorLogin = false;
    this.errorPendingConfirmation = false;
    if (error.error.status == 412) {
      this.errorPendingConfirmation = true;
    } else if(error.error.status == 409) {
    this.errorAccountLocked = true;
    } else{
    this.errorLogin = true;
    }
  })
}
}

  ngOnInit(): void {
    this.services.getUser.subscribe(user => this.requestResponse = user);

  }

}
