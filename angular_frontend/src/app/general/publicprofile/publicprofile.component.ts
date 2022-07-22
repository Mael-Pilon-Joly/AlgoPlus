import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { ApiService } from 'src/app/services/apiservices.service';
import { FileservicesService } from 'src/app/services/fileservices.service';

@Component({
  selector: 'app-publicprofile',
  templateUrl: './publicprofile.component.html',
  styleUrls: ['./publicprofile.component.css']
})
export class PublicprofileComponent implements OnInit {
  user$: any;
  message="";
  loggedUser$:any;
  cvUrl: any;
  showCV:boolean = false;  
  constructor(private route:ActivatedRoute, private fileServices: FileservicesService, private service: ApiService, private translateService: TranslateService) { }

  convertAvatarToImage(u: any) {
    console.log(u);
    if (u.avatar != null) {
    return  this.fileServices.convertBlobToImage(u.avatar.data)
    } else {
     return" ../../../assets/avatar.png"
    }
  }

  convertDataToImage(u: any) {
    console.log(u);
    if (u.image != null) {
    return  this.fileServices.convertBlobToImage(u.image.data)
    } else {
     return" ../../../assets/avatar.png"
    }
  }

  ngOnInit(): void {
  this.user$ = JSON.parse(this.route.snapshot.paramMap.get('user')!)
  console.log(this.user$)
  if (this.user$!.cv != null) {
    var urlCV = this.fileServices.convertBlobToText(this.user$.cv?.data )
    this.cvUrl = urlCV
    }
    this.loggedUser$ = this.service.getUserValue()!;
  }

  messageBetweenUsers() {
    console.log(this.loggedUser$, this.user$)
    this.service.messageBetweenUsers(this.loggedUser$.username, this.loggedUser$.email, this.user$.email, this.message).then(
      res=>{
        alert(this.translateService.instant('messagesent'))
        console.log(res)
      }).then(
      err=>console.log(err))
  }

  
}
