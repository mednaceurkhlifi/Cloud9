import { Component, OnInit } from '@angular/core';
import { User } from '../../models/User';
import { TokenService } from '../../token-service/token.service';
import { UserService } from '../user.service';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { HttpErrorResponse } from '@angular/common/http';
import { PasswordModule } from 'primeng/password';
import { MessageModule } from 'primeng/message';
import { CardModule } from 'primeng/card';
import { ToastModule } from 'primeng/toast';
import { ButtonModule } from 'primeng/button';
import { CommonModule } from '@angular/common';
import { Role } from '../../models/enum/role.enum';

@Component({
  selector: 'app-updatepassword',
  imports:[CommonModule,MessageModule ,PasswordModule,CardModule,ReactiveFormsModule,ToastModule,ButtonModule ],
  templateUrl: './updatepassword.component.html',
  styleUrls: ['./updatepassword.component.scss'],
  standalone:true,
  providers: [MessageService]
})
export class UpdatepasswordComponent implements OnInit {
  editForm: FormGroup;
  connectedUser!: User;
  isLoading = false;
  isSubmitting = false;
  constructor(
    private tokenService: TokenService,
    private userService: UserService,
    private router: Router,
    private fb: FormBuilder,
    private messageService: MessageService
  ) {
    this.editForm = this.fb.group({
      oldpassword: ['', Validators.required],
      newpassword: ['', [
        Validators.required,
        Validators.minLength(6),
        Validators.pattern(/^(?=.*[A-Za-z])(?=.*\d).+$/)
      ]],
      retypepassword: ['', Validators.required]
    }, { validator: this.passwordMatchValidator });
  }

  ngOnInit(): void {
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
  

  passwordMatchValidator(formGroup: FormGroup) {
    const newPassword = formGroup.get('newpassword')?.value;
    const retypePassword = formGroup.get('retypepassword')?.value;
    return newPassword === retypePassword ? null : { passwordMismatch: true };
  }

  onSubmit2() {
    if (this.editForm.invalid) {
      this.editForm.markAllAsTouched();
      alert("Please correct the form before submitting.");
      return;
    }
  
    this.isSubmitting = true;
    this.isLoading = true;
  
    const { oldpassword, newpassword } = this.editForm.value;
    const userId = Number(this.tokenService.getUserId());
  
    this.userService.updatePassword(userId, oldpassword, newpassword).subscribe({
      next: (response) => {
        alert("✅ Password updated successfully!");
        this.messageService.add({
          severity: 'success',
          summary: 'Success',
          detail: 'Password updated successfully'
        });
           
        this.router.navigate(['/auth/profile']); // ✅ moved here
      },
      error: (error: HttpErrorResponse) => {
        let errorMessage = 'An error occurred while updating password';
  
        if (error.status === 401) {
          errorMessage = '❌ Incorrect old password';
        } else if (error.status === 400) {
          errorMessage = '❌ Invalid new password format';
        }
  
        alert(errorMessage);
  
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: errorMessage
        });
  
        this.isLoading = false;
        this.isSubmitting = false;
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }
  onSubmit() {
    if (this.editForm.invalid) {
      this.editForm.markAllAsTouched();
      alert("Please correct the form before submitting.");
      return;
    }
  
    this.isSubmitting = true;
    this.isLoading = true;
  
    const { oldpassword, newpassword } = this.editForm.value;
    const userId = Number(this.tokenService.getUserId());
    const role = this.userService.getCurrentUserRole();
    this.userService.updatePassword(userId, oldpassword, newpassword).subscribe({
      next: (response) => {
        alert("✅ Password updated successfully!");
        this.messageService.add({
          severity: 'success',
          summary: 'Success',
          detail: 'Password updated successfully'
        });
        const userId = Number(this.tokenService.getUserId());
        const role = String(this.connectedUser.role);
        if (role=='CLIENT' ) {
          this.router.navigate(['/auth/profile']);
        } else if (role=='ADMIN' ){
          this.router.navigate(['/auth/adminprofile']);
        } else if (role=='SATFF' ) {
          this.router.navigate(['/auth/staffprofile']); // Add this if needed
        } else {
          this.router.navigate(['/auth']); // fallback route
        }
      },
      error: (error: HttpErrorResponse) => {
        let errorMessage = 'An error occurred while updating password';
  
        if (error.status === 401) {
          errorMessage = '❌ Incorrect password';
        } else if (error.status === 400) {
          errorMessage = '❌ Invalid new password format';
        }
  
        alert(errorMessage);
  
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: errorMessage
        });
  
        this.isLoading = false;
        this.isSubmitting = false;
      },
      complete: () => {
        this.isLoading = false;
      }
    });
    
  }
  

  onCancel() {
    const userId = Number(this.tokenService.getUserId());
    const role = String(this.connectedUser.role);
    if (role=='CLIENT' ) {
      this.router.navigate(['/auth/profile']);
    } else if (role=='ADMIN' ){
      this.router.navigate(['/auth/adminprofile']);
    } else if (role=='CLIENT' ) {
      this.router.navigate(['/auth/staffprofile']); // Add this if needed
    } else {
      this.router.navigate(['/auth']); // fallback route
    }
  }
}