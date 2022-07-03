import { TranslateService } from '@ngx-translate/core';
import { Component, Input, OnInit, EventEmitter, Output } from '@angular/core';
import { faMailBulk } from '@fortawesome/free-solid-svg-icons';
import { faClipboardList } from '@fortawesome/free-solid-svg-icons';
import { ApiService } from '../../services/apiservices.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})

export class NavbarComponent implements OnInit {
  @Output() translation = new EventEmitter<string>();

  home!: string;
  login!: string;
  signup!: string;
  dashboard!: string;
  articles!: string;
  events!: string;
  contact!: string;
  isLoggedIn!: boolean;

  constructor(public translate: TranslateService, private services: ApiService, private router: Router) {
    translate.addLangs(['en', 'fr']);
    translate.setDefaultLang('en');  
    const browserLang = translate.getBrowserLang();
    translate.use(browserLang.match(/en|fr/) ? browserLang : 'en');
  }


  logout(): void {
    this.services.logout()
    localStorage.setItem("loggedin", "false");
    this.router.navigate(['/home']);
     
    };

  ngOnInit(): void {
    this.services.getLogged.subscribe(logged => this.isLoggedIn = logged);

  }


  faClipboardList =  faClipboardList 
  faMailBulk = faMailBulk
}

