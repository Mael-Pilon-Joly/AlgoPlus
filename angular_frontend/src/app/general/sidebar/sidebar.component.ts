import { Component, OnInit } from '@angular/core';
import { faMailBulk } from '@fortawesome/free-solid-svg-icons';
import { faClipboardList } from '@fortawesome/free-solid-svg-icons';
import { Roles } from 'src/app/models/roles.model';
import { ApiService } from '../../services/apiservices.service';
@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {

  constructor(private services:ApiService) { }
  isLoggedIn!: boolean;
  roles:Roles={};
  isUser = false;
  isAdmin = false;

  ngOnInit(): void {
    this.services.getLogged.subscribe(logged => this.isLoggedIn = logged);
    this.services.getRoles.subscribe(roles => {
    this.roles = roles
  });
    console.log(this.roles + "," + this.isUser+ "," + this.isAdmin)
  }
  faClipboardList =  faClipboardList 
  faMailBulk = faMailBulk
}
