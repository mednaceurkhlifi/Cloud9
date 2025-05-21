import { Component, OnInit } from '@angular/core';

import { AdminApplayout } from "../../layout/component/admin-applayout";
import { Dashboard } from "../../pages/dashboard/dashboard";

import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { MenuModule } from 'primeng/menu';
import { RippleModule } from 'primeng/ripple';
import { AppTopbar } from "../../layout/component/app.topbar";
import { AppSidebar } from "../../layout/component/app.sidebar";
import { TokenService } from '../../token-service/token.service';
import { UserService } from '../user.service';
import { User } from '../../models/User';
import { Router } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AppAdminTopBar } from "../../layout/component/app-admin-top-bar";
import { AppAdminSideBar } from "../../layout/component/app-admin-side-bar";

@Component({
  
  selector: 'app-adminprofile',
  imports: [CommonModule,
    FormsModule,
    // PrimeNG Modules
    InputTextModule,
    ButtonModule,
    CardModule,
    MenuModule,
    RippleModule, AppAdminTopBar, AppAdminSideBar],
  templateUrl: './adminprofile.component.html',
  styleUrl: './adminprofile.component.scss',
  standalone:true
})
export class AdminprofileComponent implements OnInit {
  profileMenuItems!: MenuItem[];
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
        console.log('Connected user:'+ this.connectedUser.firstName +this.connectedUser.lastName+this.connectedUser.created_at);
      },
      (error) => {
        console.error('Failed to fetch user:', error);
      }
    );

    this.profileMenuItems = [
      {
          label: 'Profile',
          icon: 'pi pi-user',
          command: () => this.toggleProfile()
      },
      {
          label: 'Settings',
          icon: 'pi pi-cog',
        
      },
      {
          separator: true
      },
      {
          label: 'Logout',
          icon: 'pi pi-sign-out',
          command: () => this.logout()
      }
  ];
  }

  toggleProfile(): void {
    this.router.navigate(['/auth/editprofile']);
  }

  logout(): void {
    this.userService.logout();
    this.router.navigate(['/auth/login']);
  }
  toggleSidebar(){}

  updatePassword():void{this.router.navigate(['/auth/updatepassword'])}
   
}

