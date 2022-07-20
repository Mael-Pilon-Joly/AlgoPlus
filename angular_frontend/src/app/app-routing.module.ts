import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { EmailconfirmationComponent } from './auth/emailconfirmation/emailconfirmation.component';
import { InvalidtokenComponent } from './auth/invalidtoken/invalidtoken.component';
import { LoginComponent } from './auth/login/login.component';
import { PendingconfirmationComponent } from './auth/pendingconfirmation/pendingconfirmation.component';
import { SignupComponent } from './auth/signup/signup.component';
import { AdminboardComponent } from './board/adminboard/adminboard.component';
import { UnauthorizedComponent } from './board/unauthorized/unauthorized.component';
import { UserboardComponent } from './board/userboard/userboard.component';
import { HomeComponent } from './general/home/home.component';
import { PasswordrequestComponent } from './passwordrecovery/passwordrequest/passwordrequest.component';
import { ResetpasswordComponent } from './passwordrecovery/resetpassword/resetpassword.component';
import { FailedresetpasswordComponent } from './passwordrecovery/failedresetpassword/failedresetpassword.component';
import { UpdateprofileComponent } from './board/updateprofile/updateprofile.component';
import { HomearticlesComponent } from './article/homearticles/homearticles.component';
import { WritearticleComponent } from './article/writearticle/writearticle.component';
import { ReadarticleComponent } from './article/readarticle/readarticle.component';
import { ArticlesbylanguageComponent } from './article/articlesbylanguage/articlesbylanguage.component';
import { IdeComponent } from './exercise/ide/ide.component';
import { CreateexerciseComponent } from './exercise/createexercise/createexercise.component';
import { HomeexerciseComponent } from './exercise/homeexercise/homeexercise.component';
import { ExerciseslistComponent } from './exercise/exerciseslist/exerciseslist.component';
import { FullcalenderComponent } from './calender/fullcalender/fullcalender.component';
import { ModalModule } from 'ngx-bootstrap/modal';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';

const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'signup', component: SignupComponent },
  { path: 'login', component: LoginComponent },
  { path: 'home', component: HomeComponent },
  { path: 'adminboard', component: AdminboardComponent },
  { path: 'userboard', component: UserboardComponent},
  { path: 'unauthorized', component: UnauthorizedComponent },
  { path: 'pendingconfirmation', component: PendingconfirmationComponent },
  { path: 'passwordrequest', component: PasswordrequestComponent },
  { path: 'emailconfirmed', component: EmailconfirmationComponent },
  { path: 'invalidtoken', component: InvalidtokenComponent },
  { path: 'failedpasswordresetvalidation', component:  FailedresetpasswordComponent },
  { path: 'updatepassword', component: ResetpasswordComponent },
  { path: 'updateprofile', component: UpdateprofileComponent },
  { path: 'homearticles', component: HomearticlesComponent},
  { path: 'writearticle', component: WritearticleComponent},
  { path: 'readarticle', component: ReadarticleComponent},
  { path: 'articlesbylanguage', component: ArticlesbylanguageComponent},
  { path: 'ide', component:IdeComponent},
  { path: 'createexercise', component:CreateexerciseComponent},
  { path: 'homeexercise', component: HomeexerciseComponent},
  { path: 'listexercises', component: ExerciseslistComponent},
  { path: 'homecalender', component: FullcalenderComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes), 
    ModalModule.forRoot(),
    BrowserAnimationsModule,
    BsDatepickerModule.forRoot(),],
  exports: [RouterModule]
})
export class AppRoutingModule { }
