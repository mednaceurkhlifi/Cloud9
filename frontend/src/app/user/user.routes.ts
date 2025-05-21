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
import { EditProfileComponent } from './edit-profile/edit-profile.component';
import { EditclientprofileComponent } from './editclientprofile/editclientprofile.component';
import { AuthGuard } from './guard/auth-guard';
import { UpdatepasswordComponent } from './updatepassword/updatepassword.component';

export default [

    // Public routes (no auth required)
    { path: 'login', component: SignInComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'forgotpassword', component: ForgotPasswordComponent },
    { path: 'oauth2-redirect', component: Oauth2RedirectComponent },
    { path: 'reset-password', component: ResetpasswordComponent },

    // Protected routes (require auth)
    {
        path: 'client',
        component: ClienthomeComponent,
        canActivate: [AuthGuard]
    },
    {
        path: 'admin',
        component: AdminhomeComponent,
        canActivate: [AuthGuard]
    },
    {
        path: 'profile',
        component: ProfileComponent,
        canActivate: [AuthGuard]
    },
    {
        path: 'adminprofile',
        component: AdminprofileComponent,
        canActivate: [AuthGuard]
    },
    {
        path: 'editprofile',
        component: EditProfileComponent,
        canActivate: [AuthGuard]
    },
    {
        path: 'editclientprofile',
        component: EditclientprofileComponent,
        canActivate: [AuthGuard]
    },
    {
        path: 'updatepassword',
        component: UpdatepasswordComponent,
        canActivate: [AuthGuard]
    },
    { path: '', redirectTo: '/auth/login', pathMatch: 'full' }
] satisfies Routes;
