import { Injectable, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HttpClient, HttpRequest, HttpInterceptor, HttpHandler, HttpEvent, HTTP_INTERCEPTORS } from '@angular/common/http';
import { ConfirmationsuccessComponent } from './passwordrecovery/confirmationsuccess/confirmationsuccess.component';
import { FailedresetpasswordComponent } from './passwordrecovery/failedresetpassword/failedresetpassword.component';
import { PasswordrequestComponent } from './passwordrecovery/passwordrequest/passwordrequest.component';
import { ResetpasswordComponent } from './passwordrecovery/resetpassword/resetpassword.component';
import { EmailconfirmationComponent } from './auth/emailconfirmation/emailconfirmation.component';
import { SignupComponent } from './auth/signup/signup.component';
import { LoginComponent } from './auth/login/login.component';
import { AdminboardComponent } from './board/adminboard/adminboard.component';
import { UserboardComponent } from './board/userboard/userboard.component';
import { NavbarComponent } from './general/navbar/navbar.component';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { HomeComponent } from './general/home/home.component';
import {
  FontAwesomeModule,
  FaIconLibrary,
} from '@fortawesome/angular-fontawesome';
import { faClipboardList } from '@fortawesome/free-solid-svg-icons';
import { faMailBulk } from '@fortawesome/free-solid-svg-icons';
import { Observable } from 'rxjs';
import { PdfViewerModule } from 'ng2-pdf-viewer';
import { PendingconfirmationComponent } from './auth/pendingconfirmation/pendingconfirmation.component';
import { InvalidtokenComponent } from './auth/invalidtoken/invalidtoken.component';
import { SidebarComponent } from './general/sidebar/sidebar.component';



export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http);
}

@Injectable()
export class CustomInterceptor implements HttpInterceptor { 
    
    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    
        request = request.clone({
            withCredentials: true
        });
    
        return next.handle(request);
    }
}




@NgModule({
  declarations: [
    AppComponent,
    ConfirmationsuccessComponent,
    FailedresetpasswordComponent,
    PasswordrequestComponent,
    ResetpasswordComponent,
    EmailconfirmationComponent,
    SignupComponent,
    LoginComponent,
    AdminboardComponent,
    UserboardComponent,
    NavbarComponent,
    HomeComponent,
    PendingconfirmationComponent,
    InvalidtokenComponent,
    SidebarComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
          useFactory: HttpLoaderFactory,
          deps: [HttpClient],
      }
    }),
    FontAwesomeModule,
    PdfViewerModule,
    
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: CustomInterceptor ,
      multi: true
    }
  ], 
  bootstrap:    [ AppComponent ]
})
export class AppModule { 

  faClipboardList =  faClipboardList 
  faMailBulk = faMailBulk
  
}
