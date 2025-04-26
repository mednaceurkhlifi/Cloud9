import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { CheckboxModule } from 'primeng/checkbox';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { RippleModule } from 'primeng/ripple';
import { UserService } from '../user.service';
import { CardModule } from 'primeng/card';          // <-- ADD THIS
import { MessageModule } from 'primeng/message';

@Component({
  selector: 'app-resetpassword',
  imports: [CardModule,MessageModule,CommonModule,ButtonModule, CheckboxModule, InputTextModule, PasswordModule, FormsModule, RouterModule, RippleModule,ReactiveFormsModule],
  templateUrl: './resetpassword.component.html',
  styleUrl: './resetpassword.component.scss'
})
export class ResetpasswordComponent implements OnInit {
  resetForm!: FormGroup;
  token!: string;
  successMessage = '';
  errorMessage = '';
  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private userService: UserService,
    private router: Router
  ) {}
ngOnInit(): void {
  this.token = this.route.snapshot.queryParamMap.get('token') || '';
  this.resetForm = this.fb.group({
    password: ['', [Validators.required, Validators.minLength(6)]]
  });
  
}

onSubmit(): void {
  const newPassword = this.resetForm.value.password;

  this.userService.resetPassword(this.token, newPassword).subscribe({
    next: (response) => {
      this.successMessage = response;
      setTimeout(() => {
        this.router.navigate(['/login']);
      }, 2000);
    },
    error: (err) => {
      this.errorMessage = err.error;
    }
  });
}
}
