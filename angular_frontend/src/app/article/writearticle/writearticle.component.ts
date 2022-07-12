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
    {id:1, val:"Javascript"},
    {id:2, val:"Java"},
    {id:3, val:"Python"},
    {id:4, val:"C#"},
    {id:5, val:"C++"},
    {id:6, val:"C"},
    {id:7, val:"HTML/CSS"},
    {id:8, val:"SQL"},
    {id:9, val:"Other/Autre"}
  ]
  
  selectedFiles?: FileList;
  currentFile?: File;
  progress = 0;
  message = '';
  image?: File | undefined;
  fileInfos?: Observable<any>;
  user: User = {
    id: 0,
    username:"",
    avatar: {},
    cv:{},
    roles:[]
  }

  article: Article = {
    username: "",
    title: "",
    content: "",
    language: ''
  }

  
  constructor(private apiservices: ApiService, private services: ArticleserviceService, private http: HttpClient, private router: Router) { }


  requestResponse: RequestResponseUser = {
    user: this.user
  }

 async sendArticle(): Promise<void> {
   
        this.user = this.requestResponse.user!;
        this.article.username = this.user.username!;
        if (this.image == undefined) {
          let content = "";
          let data = new Blob([content], { type: 'application/doc' });
          let arrayOfBlob = new Array<Blob>();
          arrayOfBlob.push(data);
          this.image = new File(arrayOfBlob, "emptyimage.png");
        }
        await this.services.createArticle(this.article, this.image).then((res: any)=> {
          console.log(res);
        }).catch ( (error: { error: any; }) => {
          console.log(JSON.stringify(error.error))
        })
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
    this.apiservices.getUser.subscribe(user => { 
      this.requestResponse = user
    });

}
}

