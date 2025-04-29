import { Component, OnInit } from '@angular/core';
import { User } from '../../models/User';
import { ButtonModule } from 'primeng/button';
import { AppTopbar } from '../../layout/component/app.topbar';
import { AppClientTopBar } from '../../layout/component/app-client-top-bar';
import { AppSidebar } from '../../layout/component/app.sidebar';
import { UserService } from '../user.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { TokenService } from '../../token-service/token.service';
import { AppLayout } from '../../layout/component/app.layout';
import { FileUploadModule } from 'primeng/fileupload';

@Component({
  selector: 'app-profile',
 imports: [ButtonModule,CommonModule,AppClientTopBar,AppClientTopBar,AppSidebar],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss',
  standalone:true
})
export class ProfileComponent implements OnInit{ user: User | null = null;
  showProfile: boolean = false;
  isAuthenticated: boolean = false;

  connectedUser!:User;
   constructor(private tokenService: TokenService,private userService:UserService,private router:Router) {
    
       }
   ngOnInit(){
     console.log("email : " + this.tokenService.getUserEmail());
     console.log("user id : " + this.tokenService.getUserId());
     console.log("username : "+this.tokenService.getUserFullName());
     console.log("organization id : " + this.tokenService.getOrganizationId());
     const userId = Number(this.tokenService.getUserId());
     this.userService.getUserById(userId).subscribe(
       (user: User) => {
         this.connectedUser = user;
         console.log('Connected user:', this.connectedUser.firstName);
       },
       (error) => {
         console.error('Failed to fetch user:', error);
       }
     );
   }


  toggleProfile(): void {
    this.router.navigate(['/auth/editclientprofile'])
  }
  updatePassword():void{
    this.router.navigate(['/auth/updatepassword'])
  }

  logout(): void {
    this.userService.logout();
    this.router.navigate(['/auth/login']);
  }
}
