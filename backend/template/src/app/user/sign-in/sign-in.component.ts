import { Component } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { CheckboxModule } from 'primeng/checkbox';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { RippleModule } from 'primeng/ripple';
import { UserService } from '../user.service';
import { CommonModule } from '@angular/common';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { User } from '../../models/User';

@Component({
  selector: 'app-sign-in',
  imports: [ButtonModule, CommonModule,CheckboxModule, InputTextModule,
    PasswordModule, FormsModule, RouterModule,
    RippleModule,ReactiveFormsModule,ToastModule],
  templateUrl: './sign-in.component.html',
  standalone:true,
  styleUrl: './sign-in.component.scss',
  providers: [MessageService]
})
export class SignInComponent {
  loginForm!:FormGroup;
  response!:string;
  otherresponse!:string;
  connecteduser!:User;
  isLoading = false;
checked: boolean = false;
constructor(private ac:ActivatedRoute,
   private UserService:UserService,
   private router:Router,private messageService: MessageService){}
ngOnInit(): void {
  this.loginForm = new FormGroup({
    password: new FormControl(''),
    email: new FormControl('')
  });
  //if (this.UserService.isLoggedIn()){if(this.UserService.)}



}
get email(){ return this.loginForm.get("email");}
get password(){return this.loginForm.get("password");}

onLogin() {
  if (this.loginForm.invalid) {
    this.messageService.add({
      severity: 'warn',
      summary: 'Validation Error',
      detail: 'Please enter valid email and password',
      life: 3000
    });
    return;
  }

  this.isLoading = true;
  const { email, password } = this.loginForm.value;

  this.UserService.login(email, password).subscribe({
    next: (response: any) => {
      this.isLoading = false;
      this.messageService.add({
        severity: 'success',
        summary: 'Welcome',
        detail: response.message || 'Login successful',
        life: 3000
      });

      const role = this.UserService.getCurrentUserRole();
      if (role?.includes('ADMIN') || response === 'Welcome Admin') {
        this.router.navigate(['/dashboard']);
      } else if(role?.includes('STAFF')) {
          this.router.navigate(['/dashboard']);
      }
      else {
        this.router.navigate(['']);
        console.log(this.UserService.getToken());
        console.log('Is authenticated:', this.UserService.isLoggedIn());
        console.log('Current user:', this.UserService.getCurrentUser());
      }
    },
    error: (err) => {
      this.isLoading = false;
      this.messageService.add({
        severity: 'error',
        summary: 'Login Failed',
        detail: err.error?.message || 'Invalid email or password',
        life: 3000
      });
    }
  });
}



loginWithGoogle(){window.location.href='http://localhost:8082/api/v1/oauth2/authorization/google';}
loginWithGithub(){}
loginWithFacebook(){}
}
