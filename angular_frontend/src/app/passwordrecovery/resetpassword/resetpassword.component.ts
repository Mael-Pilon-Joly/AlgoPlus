import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/apiservices.service';
import {Router} from '@angular/router';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-resetpassword',
  templateUrl: './resetpassword.component.html',
  styleUrls: ['./resetpassword.component.css']
})
export class ResetpasswordComponent implements OnInit {
  password: string = "";
  confirmpassword: string="";
  changeSuccess: boolean = false;
  errorChange: boolean = false;
  errorpasswordformat: boolean = false;
  expiredTokenPassword: boolean = false;
  passwordDontMatch: boolean = false;
  emptyPassword: boolean = false;
  emptyConfirmPassword: boolean = false;
  public token: any;


  constructor(private service: ApiService, private router: Router, private route: ActivatedRoute) { 
  }

  ngOnInit(): void {
    this.token = this.router.parseUrl(this.router.url).queryParams['token'] 
  }

  async updatePassword(password:string,  confirmpassword: string) {
    console.log(this.token)
    this.errorChange = false;
    this.errorpasswordformat = false;
    this.expiredTokenPassword = false;
    this.changeSuccess = false;
    this.passwordDontMatch = false;
   if ((password.length < 6 || password.length > 40 ) && password !="") {
      this.errorpasswordformat = true;
    } else if (password!=confirmpassword && password != "" && confirmpassword!="") {
      this.passwordDontMatch = true;
    } else if( password=="" ||  confirmpassword=="") {
      
        if(password=="") {
          this.emptyPassword= true;
        }

        if(confirmpassword=="") {
          this.emptyConfirmPassword = true;
        }
    } else {
    await this.service.updatePassword(password, this.token).then( res=> {
      console.log(JSON.stringify(res))
      this.changeSuccess = true;
    }).catch(error=> {
      console.log(JSON.stringify(error))
       if (error.status == 401) {
              this.errorpasswordformat = true;
      } else if (error.status == 406) {
               this.expiredTokenPassword= true;
      } else if (error.error.httpsStatus.includes('BAD_REQUEST')) {
               this.errorpasswordformat = true;
      } else{
              this.errorChange = true;
      }
    })
  }}

}
