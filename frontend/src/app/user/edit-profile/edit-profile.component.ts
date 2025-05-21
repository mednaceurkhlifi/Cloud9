import { Component, NgModule, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, NgModel, ReactiveFormsModule, Validators } from '@angular/forms';
import { User } from '../../models/User';
import { TokenService } from '../../token-service/token.service';
import { UserService } from '../user.service';
import { Router } from '@angular/router';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { MenuModule } from 'primeng/menu';
import { RippleModule } from 'primeng/ripple';

import { CalendarModule } from 'primeng/calendar';

@Component({
  selector: 'app-edit-profile',
  imports: [FormsModule, ReactiveFormsModule, InputTextModule,
      ButtonModule,
      CardModule,
      MenuModule,
      RippleModule,CalendarModule],
  templateUrl: './edit-profile.component.html',
  styleUrl: './edit-profile.component.scss',
  standalone: true
})
export class EditProfileComponent implements OnInit {
  editForm: FormGroup;
  connectedUser!: User;

  constructor(
    private tokenService: TokenService,
    private userService: UserService,
    private fb: FormBuilder,
    private router: Router
  ) {
    this.editForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      birthDate:['',Validators.required],
    });
  }

  ngOnInit() {
    const userId = Number(this.tokenService.getUserId());
    this.userService.getUserById(userId).subscribe(
      (user: User) => {
        this.connectedUser = user;
        this.editForm.patchValue({
          firstName: user.firstName,
          lastName: user.lastName,
          email: user.email,
          birthDate:user.birthDate,
        });
      },
      (error) => {
        console.error('Failed to fetch user:', error);
      }
    );
  }

  onSubmit() {
    if (this.editForm.valid) {
      const updatedUser = {
        ...this.connectedUser,
        ...this.editForm.value
      };

      this.userService.updateUser(updatedUser.userId,updatedUser).subscribe(
        () => {
      
          this.router.navigate(['/auth/adminprofile']);
        },
        (error) => {
          console.error('Failed to update user:', error);
        }
      );
    }
  }

  onCancel() {
    this.router.navigate(['/auth/adminprofile']);
  }
}