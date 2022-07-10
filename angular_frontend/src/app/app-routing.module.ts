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
  { path: 'updateprofile', component: UpdateprofileComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
