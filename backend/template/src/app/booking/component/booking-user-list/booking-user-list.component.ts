import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { Table, TableModule } from 'primeng/table';
import { InputTextModule } from 'primeng/inputtext';
import { TagModule } from 'primeng/tag';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';
import { InputIconModule } from 'primeng/inputicon';
import { IconFieldModule } from 'primeng/iconfield';
import { QRCodeComponent } from 'angularx-qrcode';
import { BookingService, Booking, BookingRequest } from '../../service/booking.service';
import { User } from '../../../services/models/user';

@Component({
    selector: 'app-booking-user-list',
    standalone: true,
    imports: [
        CommonModule,
        RouterModule,
        MatButtonModule,
        MatIconModule,
        MatProgressSpinnerModule,
        TableModule,
        InputTextModule,
        TagModule,
        ButtonModule,
        RippleModule,
        InputIconModule,
        IconFieldModule,
        QRCodeComponent
    ],
    providers: [DatePipe],
    templateUrl: './booking-user-list.component.html',
    styleUrls: ['./booking-user-list.component.scss']
})
export class BookingUserListComponent implements OnInit {
    @ViewChild('dt1') dt1!: Table;

    bookings: Booking[] = [];
    loading = true;
    error: string | null = null;
    currentUser: User = {
        userId: 1, // Static user ID for now
        fullName: 'John Doe',
        email: 'john.doe@example.com'
    };

    constructor(
        private bookingService: BookingService
    ) { }

    ngOnInit(): void {
        this.loadUserBookings();
    }

    loadUserBookings(): void {
        this.loading = true;
        this.bookingService.getBookingsByUserId(this.currentUser.userId!).subscribe({
            next: (bookings) => {
                this.bookings = bookings;
                this.loading = false;
            },
            error: (err) => {
                this.error = 'Failed to load your bookings';
                this.loading = false;
                console.error(err);
            }
        });
    }

    getSeverity(status: string): 'success' | 'info' | 'warn' | 'danger' | 'secondary' | 'contrast' {
        switch (status.toLowerCase()) {
            case 'confirmed':
                return 'success';
            case 'pending':
                return 'warn';
            case 'cancelled':
                return 'danger';
            default:
                return 'info';
        }
    }

    onGlobalFilter(event: Event): void {
        this.dt1.filterGlobal((event.target as HTMLInputElement).value, 'contains');
    }

    clear(table: Table): void {
        table.clear();
    }

    getQRCodeData(booking: Booking): string {
        return JSON.stringify({
            reference: booking.reference,
            service: booking.serviceName,
            status: booking.status,
            date: booking.createdAt
        });
    }

    cancelBooking(booking: Booking): void {
        if (confirm('Are you sure you want to cancel this booking?')) {
            const bookingRequest: BookingRequest = {
                userId: booking.userId,
                serviceId: booking.serviceId,
                status: 'CANCELLED'
            };

            this.bookingService.updateBooking(booking.id, bookingRequest).subscribe({
                next: (updatedBooking) => {
                    const index = this.bookings.findIndex(b => b.id === booking.id);
                    if (index !== -1) {
                        this.bookings[index] = updatedBooking;
                    }
                },
                error: (err) => {
                    this.error = 'Failed to cancel booking. Please try again later.';
                    console.error('Error cancelling booking:', err);
                }
            });
        }
    }
}
