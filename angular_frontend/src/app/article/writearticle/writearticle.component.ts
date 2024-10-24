import { HttpClient, HttpEventType, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
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
  sameimage = false;

  user: User = {
    id: 0,
    username:"",
    avatar: {},
    cv:{},
    roles:[]
  }

  article: Article = {
    id: "",
    username: "",
    title: "",
    content: "",
    language: ''
  }

  
  constructor(private apiservices: ApiService, private services: ArticleserviceService, private http: HttpClient, private router: Router, private route: ActivatedRoute) { }


  requestResponse: User = {}

  updateTitle(event:any) {
    console.log(event)
    this.user.username = this.requestResponse.username;
  }

  fileChange(event:any) {
    let fileList: FileList = event.target.files;
    if(fileList.length > 0) {
        this.image = fileList[0];
    }
  }

  onItemChange(event:any) {
    this.sameimage =! this.sameimage;
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
        this.user = this.requestResponse;
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

    async updateArticle(): Promise<void> {
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
            if (this.sameimage == false) {
            this.errorImage = true;
            } else {
              console.log(this.sameimage)
            }
           } 
           if (!this.errorTitle && !this.errorContent && !this.errorLanguage && !this.errorImage) {
            this.user = this.requestResponse;
            this.article.username = this.user.username!;
           
            await this.services.updateArticle(this.article, this.sameimage, this.image! ).then((res: any)=> {
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

  getByValue( searchValue: string) {
    for (let [key, value] of this.items.entries()) {
      if (value.val === searchValue)
        return key;
    }
    return '';
  }

  ngOnInit(): void {
    this.getProfile();
    if(this.route.snapshot.paramMap.get('id') != undefined && this.route.snapshot.paramMap.get('id') != null) {
      this.article.id = this.route.snapshot.paramMap.get('id')!;
      this.article.title = this.route.snapshot.paramMap.get('title')!;
      this.article.language = this.route.snapshot.paramMap.get('language')!;
      this.article.content = this.route.snapshot.paramMap.get('content')!;
      this.article.username = this.route.snapshot.paramMap.get('username')!;
      console.log("transmitted article:" + JSON.stringify(this.article))
    }
    console.log(this.requestResponse)
    this.apiservices.getUser.subscribe(user => { 
      this.requestResponse = user
    });

}
}

