import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FileservicesService } from 'src/app/services/fileservices.service';

@Component({
  selector: 'app-publicprofile',
  templateUrl: './publicprofile.component.html',
  styleUrls: ['./publicprofile.component.css']
})
export class PublicprofileComponent implements OnInit {
  user$: any;
  constructor(private route:ActivatedRoute, private fileServices: FileservicesService) { }

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
  }

}
