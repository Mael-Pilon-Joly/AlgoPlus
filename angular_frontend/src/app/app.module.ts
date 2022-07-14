import { Injectable, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { MatIconModule } from '@angular/material/icon'
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HttpClient, HttpRequest, HttpInterceptor, HttpHandler, HttpEvent, HTTP_INTERCEPTORS } from '@angular/common/http';
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
import { FailedresetpasswordComponent } from './passwordrecovery/failedresetpassword/failedresetpassword.component';
import { AuthInterceptor } from './AuthInterceptor';
import { UpdateprofileComponent } from './board/updateprofile/updateprofile.component';
import {MatProgressBarModule} from '@angular/material/progress-bar';
import { HomearticlesComponent } from './article/homearticles/homearticles.component';
import { WritearticleComponent } from './article/writearticle/writearticle.component';
import { ReadarticleComponent } from './article/readarticle/readarticle.component';



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
    SidebarComponent,
    FailedresetpasswordComponent,
    UpdateprofileComponent,
    HomearticlesComponent,
    WritearticleComponent,
    ReadarticleComponent
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
    MatIconModule,
    MatProgressBarModule
    
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: CustomInterceptor ,
      multi: true
    },
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
  ], 
  bootstrap:    [ AppComponent ]
})
export class AppModule { 

  faClipboardList =  faClipboardList 
  faMailBulk = faMailBulk
  
}
