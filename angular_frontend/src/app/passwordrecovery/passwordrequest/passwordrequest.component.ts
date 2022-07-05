import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/apiservices.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-passwordrequest',
  templateUrl: './passwordrequest.component.html',
  styleUrls: ['./passwordrequest.component.css']
})
export class PasswordrequestComponent implements OnInit {
  errorResetQuest: boolean = false;
  nonExitantEmail: boolean = false;
  invalidEmailFormat: boolean = false;
  successResetQuest: boolean = false;
  email: string = "";
  emptyEmail: boolean = false;

  constructor(private services: ApiService, private router:Router) { }

  ngOnInit(): void {
  }

  async requestResetPassword(email:string) {
    this.emptyEmail = false;
    this.errorResetQuest = false;
    this.nonExitantEmail = false;
    this.invalidEmailFormat = false;
    this.successResetQuest = false;
    if (email == "") {
      this.emptyEmail = true;
    } else {
    await this.services.passwordRequest(email).then( res=> {
    this.successResetQuest = true;
  }).catch(error=> {
    console.log(JSON.stringify(error.error))
     if (error.error.httpsStatus.includes('NOT_ACCEPTABLE')) {
            this.invalidEmailFormat = true;
    } else if (error.error.httpsStatus.includes('BAD_REQUEST')) {
            this.nonExitantEmail= true;
    } else {
            this.errorResetQuest = true;
    }
  })
    }
}
}