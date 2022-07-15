import { Component, OnInit } from '@angular/core';
import { SafeUrl } from '@angular/platform-browser';
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

  constructor(private services: ApiService, private fileservices: FileservicesService) { }

  async getProfile(): Promise<void> {
    await  this.services.getProfil().then( res=> {
      console.log(res);
      this.requestResponse = res;
      if (res!.user!.avatar != null) {
      var urlAvatar = this.fileservices.convertBlobToImage(res.user?.avatar.data);
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

  ngOnInit(): void {
    this.getProfile();
    this.services.getUser.subscribe(user => { 
      this.requestResponse = user
    });

    
 
  }

}
