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
    roles:[]
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
      var urlAvatar = this.fileservices.convertBlobToImage(res.user?.avatar.data);
      var urlCV = this.fileservices.convertBlobToText(res.user?.cv?.data )
      console.log("url avatar:"+ urlAvatar)
      console.log("url cv:" + urlCV)
      this.avatarUrl = urlAvatar
      this.cvUrl = urlCV
    })
  }

  ngOnInit(): void {
    this.getProfile();
    this.services.getUser.subscribe(user => { 
      this.requestResponse = user
    });

    
 
  }

}
