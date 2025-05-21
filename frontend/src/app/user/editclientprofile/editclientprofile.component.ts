import { NgClass } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { CalendarModule } from 'primeng/calendar';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { MenuModule } from 'primeng/menu';
import { RippleModule } from 'primeng/ripple';
import { FileUploadModule } from 'primeng/fileupload';

import { TokenService } from '../../token-service/token.service';
import { UserService } from '../user.service';
import { Router } from '@angular/router';
import { User } from '../../models/User';

@Component({
    selector: 'app-editclientprofile',
    imports: [FormsModule, ReactiveFormsModule, InputTextModule,
        ButtonModule,
        CardModule,
        MenuModule,
        RippleModule,CalendarModule,
        FileUploadModule],
    templateUrl: './editclientprofile.component.html',
    styleUrl: './editclientprofile.component.scss',
    standalone:true,
})
export class EditclientprofileComponent implements OnInit {
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

                    this.router.navigate(['/auth/profile']);
                },
                (error) => {
                    console.error('Failed to update user:', error);
                }
            );
        }
    }


    onCancel() {
        this.router.navigate(['/auth/profile']);
    }
}
