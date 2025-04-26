import { Routes } from '@angular/router';
import { SignInComponent } from './sign-in/sign-in.component';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { RegisterComponent } from './register/register.component';
import { Oauth2RedirectComponent } from './oauth2-redirect/oauth2-redirect.component';
import { ClienthomeComponent } from './clienthome/clienthome.component';
import { AdminhomeComponent } from './adminhome/adminhome.component';
import { ProfileComponent } from './profile/profile.component';
import { AdminprofileComponent } from './adminprofile/adminprofile.component';
import { ResetpasswordComponent } from './resetpassword/resetpassword.component';

export default [

  { path: 'login', component: SignInComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'forgotpassword', component: ForgotPasswordComponent },
  { path: 'oauth2-redirect', component: Oauth2RedirectComponent },
  { path: 'client', component: ClienthomeComponent },
  { path: 'admin',component:AdminhomeComponent},
  {path:  'profile',component:ProfileComponent},
  {path:  'adminprofile',component:AdminprofileComponent},
  {path:  'resetpassword',component:ResetpasswordComponent},
  

  { path: '', redirectTo: 'login', pathMatch: 'full' }
] satisfies Routes;