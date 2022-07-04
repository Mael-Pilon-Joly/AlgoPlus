import { Component, OnInit } from '@angular/core';
import { RequestResponseUser } from '../../models/requestresponselogin.model';
import { User } from '../../models/user.model';
import { ApiService } from '../../services/apiservices.service';

@Component({
  selector: 'app-userboard',
  templateUrl: './userboard.component.html',
  styleUrls: ['./userboard.component.css']
})
export class UserboardComponent implements OnInit {

  constructor(private services: ApiService) { }
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
  ngOnInit(): void {
    this.services.getUser.subscribe(user => this.requestResponse = user);
    console.log()
  }

}
