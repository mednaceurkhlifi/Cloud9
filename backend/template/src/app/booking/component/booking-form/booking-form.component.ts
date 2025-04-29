import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule} from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { BookingService, Booking, BookingRequest } from '../../service/booking.service';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { InputText } from "primeng/inputtext";
import { Select } from "primeng/select";
import { DropdownModule } from 'primeng/dropdown';

@Component({
    selector: 'app-booking-form',
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        RouterModule,
        MatButtonModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        DropdownModule,
        MatProgressSpinnerModule,
        MatIconModule,
        MatTooltipModule,
        FormsModule
    ],
    templateUrl: './booking-form.component.html',
    styleUrls: ['./booking-form.component.scss']
})
export class BookingFormComponent implements OnInit {
    bookingForm: FormGroup;
    isEdit = false;
    bookingId: number | null = null;
    loading = false;
    error: string | null = null;
    currentBooking: Booking | null = null;

    dropdownItems = [
        { name: 'Canceled', code: 'Canceled' },
        { name: 'Pending', code: 'Pending' },
        { name: 'Confirmed', code: 'Confirmed' }
    ];

    constructor(
        private fb: FormBuilder,
        private bookingService: BookingService,
        private route: ActivatedRoute,
        private router: Router
    ) {
        this.bookingForm = this.fb.group({
            userId: ['', [Validators.required, Validators.min(1)]],
            serviceId: ['', [Validators.required, Validators.min(1)]],
            status: ['PENDING', Validators.required]
        });
    }

    ngOnInit(): void {
        this.route.params.subscribe((params) => {
            if (params['id']) {
                this.isEdit = true;
                this.bookingId = +params['id'];
                this.loadBooking();
            }
        });
    }

    loadBooking(): void {
        if (this.bookingId) {
            this.loading = true;
            this.bookingService.getBookingById(this.bookingId).subscribe({
                next: (booking) => {
                    this.currentBooking = booking;
                    this.bookingForm.patchValue({
                        userId: booking.userId,
                        serviceId: booking.serviceId,
                        status: this.dropdownItems.find(i => i.code === booking.status)
                    });
                    this.loading = false;
                },
                error: (err) => {
                    this.error = 'Failed to load booking';
                    this.loading = false;
                    console.error(err);
                }
            });
        }
    }

    onSubmit(): void {
        if (this.bookingForm.valid) {
            this.loading = true;
            const bookingData: BookingRequest = {
                ...this.bookingForm.value,
                status: this.bookingForm.value.status.code
              };

            const request$ = this.isEdit && this.bookingId ? this.bookingService.updateBooking(this.bookingId, bookingData) : this.bookingService.createBooking(bookingData);

            request$.subscribe({
                next: () => {
                    this.router.navigate(['/dashboard/bookings']);
                },
                error: (err) => {
                    this.error = this.isEdit ? 'Failed to update booking' : 'Failed to create booking';
                    this.loading = false;
                    console.error(err);
                }
            });
        }
    }
}
