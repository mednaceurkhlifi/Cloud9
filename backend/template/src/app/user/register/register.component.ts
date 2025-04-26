import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { CheckboxModule } from 'primeng/checkbox';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { RippleModule } from 'primeng/ripple';
import { CommonModule } from '@angular/common';
import { UserService } from '../user.service';
import { CalendarModule } from 'primeng/calendar';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'app-register',
  imports: [
    CalendarModule,
    CommonModule,
    ButtonModule, 
    CheckboxModule, 
    InputTextModule, 
    PasswordModule, 
    FormsModule, 
    RouterModule, 
    RippleModule,
    ReactiveFormsModule,
    ToastModule // <-- Add this
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss',
  standalone: true,
  providers: [MessageService]
})
export class RegisterComponent implements OnInit {
  myForm!: FormGroup;
  isLoading = false;

  constructor(
    private ac: ActivatedRoute, 
    private userService: UserService,
    private router: Router,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.myForm = new FormGroup({
      password: new FormControl('', [Validators.required, Validators.minLength(6)]),
      firstName: new FormControl('', Validators.required),
      lastName: new FormControl('', Validators.required),
      birthDate: new FormControl('', Validators.required),
      email: new FormControl('', [Validators.required, Validators.email])
    });
  }

  onSubmit() {
    if (this.myForm.invalid) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Validation Error',
        detail: 'Please fill all required fields correctly',
        life: 3000
      });
      return;
    }

    this.isLoading = true;
    
    this.userService.addUser(this.myForm.value).subscribe({
      next: (res) => {
        this.messageService.add({
          severity: 'success',
          summary: 'Success',
          detail: 'Registration successful!',
          life: 3000
        });
        this.router.navigate(['/auth/login']);
      },
      error: (err) => {
        this.isLoading = false;
        let errorMessage = 'Registration failed';
        
        if (err.status === 400) {
          errorMessage = 'Invalid data. Please check your inputs.';
        } else if (err.error?.message) {
          errorMessage = err.error.message;
        }
        
        this.messageService.add({
          severity: 'error',
          summary: 'Registration Failed',
          detail: errorMessage,
          life: 5000
        });
      }
    });
  }


get password() { return this.myForm.get("password"); }
get firstName() { return this.myForm.get("firstName"); }
get lastName() { return this.myForm.get("lastName"); }

get birthDate() { return this.myForm.get("birthDate"); }

get email(){ return this.myForm.get("email");}
isFormValid(): boolean {
  return (
    !!this.firstName?.valid &&
    !!this.lastName?.valid &&
    !!this.email?.valid &&
    !!this.birthDate?.valid &&
    !!this.password?.valid
  );
 }
 
 loginWithGoogle(){  window.location.href = 'http://localhost:8082/oauth2/authorization/google';}
 loginWithFacebook(){ window.location.href='http://localhost:8082/oauth2/authorization/facebook';}
 loginWithGithub(){ window.location.href = 'http://localhost:8082/oauth2/authorization/github';}

}
