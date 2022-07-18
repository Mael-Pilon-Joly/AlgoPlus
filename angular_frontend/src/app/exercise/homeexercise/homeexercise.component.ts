import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from 'src/app/models/user.model';
import { ApiService } from 'src/app/services/apiservices.service';

@Component({
  selector: 'app-homeexercise',
  templateUrl: './homeexercise.component.html',
  styleUrls: ['./homeexercise.component.css']
})
export class HomeexerciseComponent implements OnInit {
user?: User;

  constructor(private apiservices: ApiService, private router: Router) { }

  ngOnInit(): void {
    this.user = this.apiservices.getUserValue();
      console.log("getUser:" + JSON.stringify(this.user))
      if (this.user == undefined || this.user == null || this.user.username =="") {
        this.router.navigate(['/login']);
      }
  
  }

}
