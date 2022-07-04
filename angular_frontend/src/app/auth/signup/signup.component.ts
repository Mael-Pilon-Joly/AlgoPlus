import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/apiservices.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  constructor(private service: ApiService) { }

  ngOnInit(): void {
  }

  errorSignUp: boolean = false;
  errorEmailFormat: boolean = false;
  errorUsernameTaken: boolean = false;
  errorEmailTaken: boolean = false;
  errorErrorFormat: boolean = false;
  errorPasswordDontMatch: boolean = false;


  signUp(username: string, email: string,role: string, password: string ){

    var res = this.service.signUp(username, email, "", password)
    if(res.response.data.httpsStatus.includes("INTERNAL_SERVER_ERROR")){
     this.errorSignUp = true;
    }
    if(res.response.data.httpsStatus.includes("NOT_ACCEPTABLE")){
      this.errorEmailFormat = true;
    }
    if(res.response.data.httpsStatus.includes("LOCKED")){
      this.errorUsernameTaken = true; 
    }
    if(res.response.data.httpsStatus.includes("CONFLICT")){
      this.errorEmailTaken = true; 
    }
    if(res.response.data.httpsStatus.includes("REQUESTED_RANGE_NOT_SATISFIABLE")){
      this.errorErrorFormat = true;
    } 
  }

}
