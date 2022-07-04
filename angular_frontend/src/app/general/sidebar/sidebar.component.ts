import { Component, OnInit } from '@angular/core';
import { faMailBulk } from '@fortawesome/free-solid-svg-icons';
import { faClipboardList } from '@fortawesome/free-solid-svg-icons';
import { ApiService } from '../../services/apiservices.service';
@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {

  constructor(private services:ApiService) { }
  isLoggedIn!: boolean;
  ngOnInit(): void {
    this.services.getLogged.subscribe(logged => this.isLoggedIn = logged);
  }
  faClipboardList =  faClipboardList 
  faMailBulk = faMailBulk
}
