import { Component, OnInit } from '@angular/core';
import { SafeUrl } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { ArticleserviceService } from 'src/app/services/articleservice.service';
import { RequestResponseUser } from '../../models/requestresponselogin.model';
import { User } from '../../models/user.model';
import { ApiService } from '../../services/apiservices.service';
import { FileservicesService } from '../../services/fileservices.service';


@Component({
  selector: 'app-adminboard',
  templateUrl: './adminboard.component.html',
  styleUrls: ['./adminboard.component.css']
})
export class AdminboardComponent implements OnInit {

  articleTitle = ""
  exerciseTitle = ""
  username =""

  constructor(private services: ApiService, private articleServices: ArticleserviceService, private fileservices: FileservicesService, private router:Router) { }
  ngOnInit(): void {
   this.validateAdminRole()
  }

  async validateAdminRole(): Promise<void> {
    this.services.validateAdminRole().then(res=>{
      console.log(res)
    }).catch(err =>{
      alert("Access denied")
      console.log(err)
      this.router.navigate(['/home']);
    })
  }

  searchArticles() {
    console.log("this title is:" + this.articleTitle)
    this.router.navigate(['/management', {articles: this.articleTitle}])
  }
  


}
