import { Component, OnInit } from '@angular/core';
import { User } from '../../models/User';

import { AppTopbar } from '../../layout/component/app.topbar';
import { AppSidebar } from '../../layout/component/app.sidebar';
import { UserService } from '../user.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-profile',
 imports: [AppTopbar,AppSidebar,CommonModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss',
  standalone:true
})
export class ProfileComponent implements OnInit{ user: User | null = null;
  showProfile: boolean = false;
  isAuthenticated: boolean = false;

  constructor(
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.isAuthenticated = this.userService.isLoggedIn();
    if (this.isAuthenticated) {
      this.user = this.userService.getCurrentUser();
    } else {
      this.router.navigate(['/auth/login']);
    }
  }

  toggleProfile(): void {
    this.showProfile = !this.showProfile;
  }

  logout(): void {
    this.userService.logout();
    this.router.navigate(['/auth/login']);
  }
}
