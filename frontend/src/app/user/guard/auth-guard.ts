import { CanActivate, Router, UrlTree } from "@angular/router";
 import { UserService } from "../user.service";
import { Injectable } from "@angular/core";
@Injectable({
    providedIn: 'root'
  })
export class AuthGuard implements CanActivate {
    constructor(private authService: UserService, private router: Router) {}
  
    canActivate(): boolean {
        if (this.authService.isAuthenticated()) {
          return true;
        } else {
          this.router.navigate(['/auth/login']);
          return false;
        }
      }
  }
