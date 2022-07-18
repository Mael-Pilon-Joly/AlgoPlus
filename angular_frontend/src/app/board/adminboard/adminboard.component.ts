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
    articlesDTOs: []
  }

  requestResponse: User ={}
   avatarUrl: any;
   cvUrl: any;

  constructor(private services: ApiService, private fileservices: FileservicesService) { }

  async getProfile(): Promise<void> {
    await  this.services.getProfil().then( res=> {
      console.log(res);
      this.requestResponse = res;
      if (res!.avatar != null) {
      var urlAvatar = this.fileservices.convertBlobToImage(res.avatar.data);
      this.avatarUrl = urlAvatar
      }
      if (res!.cv != null) {
      var urlCV = this.fileservices.convertBlobToText(res.cv?.data )
      this.cvUrl = urlCV
      }
      this.user.articlesDTOs = res.articlesDTOs;
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
