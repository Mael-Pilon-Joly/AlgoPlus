import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from 'src/app/services/apiservices.service';
import { FileservicesService } from 'src/app/services/fileservices.service';

@Component({
  selector: 'app-ranking',
  templateUrl: './ranking.component.html',
  styleUrls: ['./ranking.component.css']
})
export class RankingComponent implements OnInit {

  users$!:any[];

  constructor(private apiServices: ApiService, private fileServices:FileservicesService, private router: Router) { }

  ngOnInit(): void {
    this.apiServices.getUsers().subscribe((list)=>{
      this.users$ = list as any[]
      console.log(this.users$)
    })
  }

  convertDataToImage(u: any) {
    console.log(u);
    if (u.avatar != null) {
    return  this.fileServices.convertBlobToImage(u.avatar.data)
    } else {
     return" ../../../assets/avatar.png"
    }
  }

  onUserClick(user:any){
  console.log(user);
  this.router.navigate(['/publicprofile', {user: JSON.stringify(user)}])
  }

}
