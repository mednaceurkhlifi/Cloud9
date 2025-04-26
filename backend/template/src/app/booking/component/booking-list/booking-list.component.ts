import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Table, TableModule } from 'primeng/table';
import { InputTextModule } from 'primeng/inputtext';
import { TagModule } from 'primeng/tag';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';
import { InputIconModule } from 'primeng/inputicon';
import { IconFieldModule } from 'primeng/iconfield';
import { BookingService, Booking } from '../../service/booking.service';
import { Router } from '@angular/router';

@Component({
    selector: 'app-booking-list',
    standalone: true,
    imports: [
        CommonModule,
        RouterModule,
        MatButtonModule,
        MatIconModule,
        TableModule,
        InputTextModule,
        TagModule,
        ButtonModule,
        RippleModule,
        InputIconModule,
        IconFieldModule
    ],
    providers: [DatePipe],
    templateUrl: './booking-list.component.html',
    styleUrls: ['./booking-list.component.scss']
})
export class BookingListComponent implements OnInit {
    @ViewChild('dt1') dt1!: Table;

    bookings: Booking[] = [];
    loading = true;
    error: string | null = null;

    constructor(
        private bookingService: BookingService,
        private router: Router
    ) { }

    ngOnInit(): void {
        this.loadBookings();
    }

    loadBookings(): void {
        this.loading = true;
        this.bookingService.getAllBookings().subscribe({
            next: (bookings) => {
                this.bookings = bookings;
                this.loading = false;
            },
            error: (err) => {
                this.error = 'Failed to load bookings';
                this.loading = false;
                console.error(err);
            }
        });
    }

    deleteBooking(id: number): void {
        if (confirm('Are you sure you want to delete this booking? This action cannot be undone.')) {
            this.bookingService.deleteBooking(id).subscribe({
                next: () => {
                    this.bookings = this.bookings.filter(booking => booking.id !== id);
                },
                error: (err) => {
                    this.error = 'Failed to delete booking. Please try again later.';
                    console.error('Error deleting booking:', err);
                }
            });
        }
    }

    updateBooking(id: number): void {
        this.router.navigate(['/bookings', id]);
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
}
