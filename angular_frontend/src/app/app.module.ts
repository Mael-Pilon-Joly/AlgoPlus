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
import {MatSidenavModule} from '@angular/material/sidenav';
import { ArticlesbylanguageComponent } from './article/articlesbylanguage/articlesbylanguage.component';
import { IdeComponent } from './exercise/ide/ide.component';
import { CreateexerciseComponent } from './exercise/createexercise/createexercise.component';
import { HomeexerciseComponent } from './exercise/homeexercise/homeexercise.component';
import { ExerciseslistComponent } from './exercise/exerciseslist/exerciseslist.component';
import { MatTableModule } from '@angular/material/table';
import { FullCalendarModule } from '@fullcalendar/angular';
import { FullcalenderComponent } from './calender/fullcalender/fullcalender.component'; 
import interactionPlugin from '@fullcalendar/interaction';
import dayGridPlugin from '@fullcalendar/daygrid';
import { ModalModule } from 'ngx-bootstrap/modal';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import {NgxMaterialTimepickerModule} from 'ngx-material-timepicker';
import {DatePipe} from '@angular/common';
import { MatDatepickerModule } from '@angular/material/datepicker';
import {MatSliderModule} from '@angular/material/slider';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import { MatNativeDateModule } from '@angular/material/core';
import { RulesComponent } from './general/rules/rules.component';
import { ContactComponent } from './general/contact/contact.component';
import { ReactiveFormsModule } from '@angular/forms';
import { ManagementboardComponent } from './board/managementboard/managementboard.component';
import { RankingComponent } from './general/ranking/ranking.component';  


export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http);
}
FullCalendarModule.registerPlugins([ 
  interactionPlugin,
  dayGridPlugin
]);

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
    ReadarticleComponent,
    ArticlesbylanguageComponent,
    IdeComponent,
    CreateexerciseComponent,
    HomeexerciseComponent,
    ExerciseslistComponent,
    FullcalenderComponent,
    RulesComponent,
    ContactComponent,
    ManagementboardComponent,
    RankingComponent
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
    MatProgressBarModule,
    MatTableModule,
    FullCalendarModule,
    NgxMaterialTimepickerModule,
    BsDatepickerModule.forRoot(),
    MatNativeDateModule,
    MatFormFieldModule,
    MatSliderModule,
    MatDatepickerModule,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule

  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: CustomInterceptor ,
      multi: true    },
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    DatePipe
  ], 
  bootstrap:    [ AppComponent ]
})
export class AppModule { 

  faClipboardList =  faClipboardList 
  faMailBulk = faMailBulk
  
}
