import { Component, OnInit } from '@angular/core';
import { SafeUrl } from '@angular/platform-browser';
import { RequestResponseUser } from '../../models/requestresponselogin.model';
import { User } from '../../models/user.model';
import { ApiService } from '../../services/apiservices.service';
import { FileservicesService } from '../../services/fileservices.service';
import { Router } from '@angular/router'
import { Article } from 'src/app/models/article.model';
import { CompleteArticle } from 'src/app/models/completearticle.model';

@Component({
  selector: 'app-userboard',
  templateUrl: './userboard.component.html',
  styleUrls: ['./userboard.component.css']
})
export class UserboardComponent implements OnInit {

  showCV:boolean = false;  
  user: User = {
    id: 0,
    username:"",
    avatar: {},
    cv:{},
    roles:[],
    articles: []
  }

  requestResponse: RequestResponseUser = {
    user: this.user
  }

   avatarUrl: any;
   cvUrl: any;

  constructor(private services: ApiService, private fileservices: FileservicesService, private router: Router) { }

  async getProfile(): Promise<void> {
    await  this.services.getProfil().then( res=> {
      console.log(res);
      this.requestResponse = res;
      if (res!.user!.avatar != null) {
      var urlAvatar = this.fileservices.convertBlobToImage(res.user?.avatar.data);
      console.log("url avatar" + urlAvatar)
      this.avatarUrl = urlAvatar
      }
      if (res!.user!.cv != null) {
      var urlCV = this.fileservices.convertBlobToText(res.user?.cv?.data )
      this.cvUrl = urlCV
      }
      this.user.articles = res.user?.articles;
      

    })
  }

  convertDataToImage(a: any) {
    console.log("article..." + a);
    return  this.fileservices.convertBlobToImage(a.image.data)
  }

  updateArticle(article: CompleteArticle){
    console.log("click event fired..."+JSON.stringify(article))
    this.router.navigate(['/writearticle', {id: article.id, username: article.username, title:article.title, image:article.image, content: article.content, language:article.language}]);
  }
  
  ngOnInit(): void {
    this.getProfile();
    this.services.getUser.subscribe(user => { 
      this.requestResponse = user
    });
  

}
}
