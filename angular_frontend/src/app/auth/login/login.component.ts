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

  requestResponse: RequestResponseUser = {
    user: this.user
  }

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
    };

    
    if (this.requestResponse.user) {
    await  this.services.login(data).then( res=> {
    console.log(res);
    const result : Roles[] = res.user!.roles!;
    if (result.filter(e => e.name === 'ROLE_USER').length > 0) {
    this.router.navigate(['/userboard']);
    }

    if (result.filter(e => e.name === 'ROLE_ADMIN').length > 0) {
      this.router.navigate(['/adminboard']);
    }
  })
}
}

  ngOnInit(): void {
    this.services.getUser.subscribe(user => this.requestResponse = user);

  }

}
