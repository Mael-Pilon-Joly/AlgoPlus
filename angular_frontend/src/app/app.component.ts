import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})


export class AppComponent {
  title = 'angular_frontend';
  public eventsSubject: Subject<TranslateService> = new Subject<TranslateService>();
  router: string;


  constructor(public translate: TranslateService, private _router: Router) {
    this.router = _router.url; 
 
  }

  emitEventToChild(translate: TranslateService) {
    translate.addLangs(['en', 'fr']);
    translate.setDefaultLang('en');
    this.eventsSubject.next(translate);
  }
}
