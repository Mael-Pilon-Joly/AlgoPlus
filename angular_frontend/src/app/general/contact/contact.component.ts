import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import { ApiService } from 'src/app/services/apiservices.service';

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.css']
})
export class ContactComponent implements OnInit {
  name = "";
  email = "";
  message = "";
  errorName =false;
  errorMessage=false;
  errorMail = false;

  constructor(private builder: FormBuilder, private service: ApiService) { }

  ngOnInit(): void {
   
  }

validateEmail(email:any) {
    var re = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;
    return re.test(email);
}

  sendEmail(){
   this.errorName=false;
   this.errorMessage=false;
   this.errorMail = false;
   if (this.name ==""){
   this.errorName=true;
   } else if (this.message==""){
    this.errorMessage = true;
   } else if (!this.validateEmail(this.email)){
     this.errorMail = true;
   } else {
    this.service.receiveMessage(this.email, this.name, this.message).subscribe();
   }
  }

}

