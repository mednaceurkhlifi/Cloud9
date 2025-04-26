import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterModule,Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { CheckboxModule } from 'primeng/checkbox';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { RippleModule } from 'primeng/ripple';
import { UserService } from '../user.service';
import { CardModule } from 'primeng/card';          // <-- ADD THIS
import { MessageModule } from 'primeng/message';

@Component({
  selector: 'app-forgot-password',
  imports: [CardModule,MessageModule,CommonModule,ButtonModule, CheckboxModule, InputTextModule, PasswordModule, FormsModule, RouterModule, RippleModule,ReactiveFormsModule],
  templateUrl: './forgot-password.component.html',
  standalone:true,
  styleUrl: './forgot-password.component.scss'
})
export class ForgotPasswordComponent implements OnInit {
  forgotpassword!:FormGroup;
  message!: string ;
  constructor(private userservice:UserService){

  }
  ngOnInit(): void {
  this.forgotpassword= new FormGroup({
       
        email: new FormControl('')
      });
}

get email() {
  return this.forgotpassword.get('email');
}
onSubmit() : void {
  const email=this.forgotpassword.value.email;
  if (!email) {
    this.message = 'Please enter your email.';
    return;
  }
  this.userservice.sendResetEmail(email).subscribe({
    next: () => {
      this.message = " A reset link has been sent to your email.";
    },
    error: (err) => {
      this.message = " Error sending reset link. Try again.";
      console.error(err);
    }

  })
}

}

