import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/apiservices.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  constructor(private service: ApiService, private router: Router) { }

  ngOnInit(): void {
  }

  errorSignUp: boolean = false;
  errorEmailFormat: boolean = false;
  errorUsernameTaken: boolean = false;
  errorEmailTaken: boolean = false;
  errorErrorFormat: boolean = false;
  errorPasswordDontMatch: boolean = false;
  emptyUserName: boolean = false;
  emptyEmail: boolean = false;
  emptyPassword: boolean = false;
  emptyConfirmPassword: boolean= false;
  username: string = "";
  email: string = "";
  password: string = "";
  confirmpassword: string = "";

  async signUp(username: string, email: string, password: string, confirmpassword: string){
    this.errorSignUp= false;
    this.errorEmailFormat = false;
    this.errorUsernameTaken = false;
    this.errorEmailTaken = false;
    this.errorErrorFormat = false;
    this.emptyUserName = false;
    this.emptyEmail = false;
    this.emptyPassword= false;
    this.emptyConfirmPassword = false;
    this.errorPasswordDontMatch = false;
    if (password!=confirmpassword && password != "" && confirmpassword!="") {
      this.errorPasswordDontMatch = true;
    } else if(username=="" || password=="" || email=="" || confirmpassword=="") {
        if(username=="") {
          this.emptyUserName = true;
        }

        if(email=="") {
          this.emptyEmail = true;
        }

        if(password=="") {
          this.emptyPassword= true;
        }

        if(confirmpassword=="") {
          this.emptyConfirmPassword = true;
        }
    } else {
 
    await this.service.signUp(username, email, null, password).then( res=> {
      this.router.navigate(['/pendingconfirmation']);
  }).catch (res=> {
    console.log(res)
    if(res.error.httpsStatus?.includes("INTERNAL_SERVER_ERROR")){
     this.errorSignUp = true;
    }
    if(res.error.httpsStatus?.includes("NOT_ACCEPTABLE")){
      this.errorEmailFormat = true;
    }
    if(res.error.httpsStatus?.includes("LOCKED")){
      this.errorUsernameTaken = true; 
    }
    if(res.error.httpsStatus?.includes("CONFLICT")){
      this.errorEmailTaken = true; 
    }
    if(res.error.httpsStatus?.includes("REQUESTED_RANGE_NOT_SATISFIABLE")){
      this.errorErrorFormat = true;
    } 
   } )
  }

}
}
