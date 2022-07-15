import { HttpClient, HttpEventType, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Article } from 'src/app/models/article.model';
import { RequestResponseUser } from 'src/app/models/requestresponselogin.model';
import { User } from 'src/app/models/user.model';
import { ApiService } from 'src/app/services/apiservices.service';
import { ArticleserviceService } from 'src/app/services/articleservice.service';

@Component({
  selector: 'app-writearticle',
  templateUrl: './writearticle.component.html',
  styleUrls: ['./writearticle.component.css']
})
export class WritearticleComponent implements OnInit {
  items = [
    {id:0, val:"Angular"},
    {id:10,val:"React"},
    {id:1, val:"Javascript"},
    {id:2, val:"Java"},
    {id:3, val:"Python"},
    {id:4, val:"Csharp"},
    {id:5, val:"Cplusplus"},
    {id:6, val:"C"},
    {id:7, val:"HTML_CSS"},
    {id:8, val:"SQL"},
    {id:9, val:"Other_Autre"}
  ]
  
  selectedFiles?: FileList;
  currentFile?: File;
  progress = 0;
  message = '';
  image?: File | undefined;
  fileInfos?: Observable<any>;
  errorTitle = false;
  errorContent = false;
  errorLanguage = false;
  errorImage = false;
  errorGeneral = false;

  user: User = {
    id: 0,
    username:"",
    avatar: {},
    cv:{},
    roles:[]
  }

  article: Article = {
    id: 0,
    username: "",
    title: "",
    content: "",
    language: ''
  }

  
  constructor(private apiservices: ApiService, private services: ArticleserviceService, private http: HttpClient, private router: Router) { }


  requestResponse: RequestResponseUser = {
    user: this.user
  }

  updateTitle(event:any) {
    console.log(event)
    this.user.username = this.requestResponse.user?.username;
  }

  fileChange(event:any) {
    let fileList: FileList = event.target.files;
    if(fileList.length > 0) {
        this.image = fileList[0];
    }
  }


 async sendArticle(): Promise<void> {
  this.errorTitle = false;
  this.errorContent = false;
  this.errorLanguage = false;
  this. errorImage = false;
  this.errorGeneral = false;

       if(this.article.title == "") {
        this.errorTitle = true;
       } 
       if(this.article.content.length < 100) {
        this.errorContent = true;
       } 
       if(this.article.language =="") {
        this.errorLanguage = true;
       } 
       if(this.image == undefined) {
        this.errorImage = true;
       } 
       if (!this.errorTitle && !this.errorContent && !this.errorLanguage && !this.errorImage) {
        this.user = this.requestResponse.user!;
        this.article.username = this.user.username!;
       
        await this.services.createArticle(this.article, this.image! ).then((res: any)=> {
          console.log(res);
          this.router.navigate(['/homearticles']);
        }).catch ( (error: { error: any; }) => {
          this.errorGeneral = true;
          console.log(JSON.stringify(error.error))
        })
      }
    }

  selectChangeHandler (event: any, i:any) {
        console.log(i)
        this.article.language = i;
      }

  async getProfile(): Promise<void> {
    await  this.apiservices.getProfil().then( res=> {
      console.log(res);
      this.requestResponse = res;
    }).catch(e => {
      this.router.navigate(['/login']);
    })
  }

  ngOnInit(): void {
    this.getProfile();
    console.log(this.requestResponse)
    this.apiservices.getUser.subscribe(user => { 
      this.requestResponse = user
    });

}
}

